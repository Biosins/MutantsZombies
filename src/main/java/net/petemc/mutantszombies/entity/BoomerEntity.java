package net.petemc.mutantszombies.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.petemc.mutantszombies.config.Config;
import net.petemc.mutantszombies.effect.ModEffects;

public class BoomerEntity extends AbstractHordeZombieEntity {

        public BoomerEntity(EntityType<BoomerEntity> type, Level world) {
                super(type, world);
        }

        @Override
        protected void registerGoals() {
                super.registerGoals();
                this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }

        @Override
        public void die(@NotNull DamageSource damageSource) {
                if (!this.level().isClientSide()) {
                        // Apply Slimed effect to all living entities in radius
                        double radius = Config.getBoomerAlertRadius();
                        AABB searchBox = new AABB(
                                        this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                                        this.getX() + radius, this.getY() + radius, this.getZ() + radius);

                        List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                                        LivingEntity.class,
                                        searchBox,
                                        (entity) -> true);

                        // Apply Slimed effect to all nearby entities
                        for (LivingEntity entity : nearbyEntities) {
                                entity.addEffect(new MobEffectInstance(
                                                ModEffects.SLIMED.get(),
                                                300, // 15 seconds
                                                0 // Level 1
                                ));
                        }
                }

                super.die(damageSource);
        }

        @Override
        public boolean doHurtTarget(Entity $$0) {
                boolean $$1 = super.doHurtTarget($$0);
                if ($$1) {
                        // Apply Slimed effect on melee hit
                        if ($$0 instanceof LivingEntity livingTarget) {
                                livingTarget.addEffect(new MobEffectInstance(
                                                ModEffects.SLIMED.get(),
                                                200, // 10 seconds
                                                0 // Level 1
                                ));
                        }
                }

                return $$1;
        }

        @Override
        public @NotNull SoundEvent getDeathSound() {
                return SoundEvents.SLIME_JUMP;
        }

        public static AttributeSupplier.Builder createAttributes() {
                return createBaseAttributes()
                                .add(Attributes.MAX_HEALTH, 75.0)
                                .add(Attributes.MOVEMENT_SPEED, 0.18)
                                .add(Attributes.ATTACK_DAMAGE, 3.0) // Melee attack with bile application
                                .add(Attributes.ARMOR, 2.0);
        }

        public static void init() {
                SpawnPlacements.register(ModEntities.BOOMER.get(),
                                SpawnPlacements.Type.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                (entityType, serverLevel, reason, pos, random) -> Config.getBoomersSpawnNaturally()
                                                && !(serverLevel.getBiome(pos).is(Biomes.MUSHROOM_FIELDS))
                                                && !(serverLevel.getBiome(pos).is(Biomes.DEEP_DARK))
                                                && serverLevel.getDifficulty() != Difficulty.PEACEFUL
                                                && Mob.checkMobSpawnRules(entityType, serverLevel, reason, pos,
                                                                random));
        }
}
