package net.petemc.mutantszombies.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.petemc.mutantszombies.config.Config;
import net.petemc.mutantszombies.effect.ModEffects;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BoomerEntity extends AbstractHordeZombieEntity {

    public BoomerEntity(EntityType<BoomerEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerCustomGoals() {
        // Remove all attack goals to make Boomer passive
        // Only has movement and looking goals from base class
    }

    @Override
    protected void registerGoals() {
        // Override to remove melee attack goal
        this.goalSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        this.goalSelector.addGoal(4, new net.minecraft.world.entity.ai.goal.RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(6, new net.minecraft.world.entity.ai.goal.RandomLookAroundGoal(this));
        
        // Keep targeting goals from base class
        this.targetSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(
                this, net.minecraft.world.entity.player.Player.class, 10, true, false,
                (entity) -> entity.hasEffect(ModEffects.SLIMED.get())
        ));
        
        this.targetSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal(this)
                .setAlertOthers(this.getClass()));
        
        this.targetSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(
                this, net.minecraft.world.entity.player.Player.class, false));
        
        this.targetSelector.addGoal(4, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(
                this, net.minecraft.world.entity.animal.IronGolem.class, true, true));
        
        this.targetSelector.addGoal(5, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(
                this, net.minecraft.world.entity.npc.AbstractVillager.class, true, true));
        
        registerCustomGoals();
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        if (!this.level().isClientSide()) {
            // Apply Slimed effect to all living entities in radius
            double radius = Config.getBoomerAlertRadius();
            AABB searchBox = new AABB(
                    this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                    this.getX() + radius, this.getY() + radius, this.getZ() + radius
            );

            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    searchBox,
                    (entity) -> entity.distanceTo(this) <= radius && entity != this
            );

            // Apply Slimed effect to all nearby entities
            for (LivingEntity entity : nearbyEntities) {
                entity.addEffect(new MobEffectInstance(
                        ModEffects.SLIMED.get(),
                        300, // 15 seconds
                        0    // Level 1
                ));
            }

            // Alert all horde zombies in larger radius
            double alertRadius = 30.0;
            AABB alertBox = new AABB(
                    this.getX() - alertRadius, this.getY() - alertRadius, this.getZ() - alertRadius,
                    this.getX() + alertRadius, this.getY() + alertRadius, this.getZ() + alertRadius
            );

            List<AbstractHordeZombieEntity> nearbyZombies = this.level().getEntitiesOfClass(
                    AbstractHordeZombieEntity.class,
                    alertBox,
                    (entity) -> entity.distanceTo(this) <= alertRadius && entity != this
            );

            // Find slimed players to target
            List<LivingEntity> slimedPlayers = nearbyEntities.stream()
                    .filter(e -> e instanceof net.minecraft.world.entity.player.Player)
                    .filter(e -> e.hasEffect(ModEffects.SLIMED.get()))
                    .toList();

            if (!slimedPlayers.isEmpty()) {
                LivingEntity closestSlimed = slimedPlayers.get(0);
                for (AbstractHordeZombieEntity zombie : nearbyZombies) {
                    zombie.setTarget(closestSlimed);
                }
            }

            // Create explosion visual/audio (no block damage)
            this.level().explode(
                    this,
                    this.getX(), this.getY(), this.getZ(),
                    2.0F,
                    Level.ExplosionInteraction.NONE
            );
        }

        super.die(damageSource);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.ambient"))
        );
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.explode"))
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, 75.0)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.ATTACK_DAMAGE, 0.0) // No attack
                .add(Attributes.ARMOR, 2.0);
    }

    public static void init() {
        SpawnPlacements.register(ModEntities.BOOMER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevel, reason, pos, random) ->
                        Config.getBoomersSpawnNaturally()
                                && !(serverLevel.getBiome(pos).is(Biomes.MUSHROOM_FIELDS))
                                && !(serverLevel.getBiome(pos).is(Biomes.DEEP_DARK))
                                && serverLevel.getDifficulty() != Difficulty.PEACEFUL
                                && serverLevel.getRawBrightness(pos, 0) <= 8
                                && Mob.checkMobSpawnRules(entityType, serverLevel, reason, pos, random));
    }
}
