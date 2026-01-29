package net.petemc.mutantszombies.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.petemc.mutantszombies.config.Config;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CommonZombieEntity extends AbstractHordeZombieEntity implements NeutralMob {
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    public CommonZombieEntity(EntityType<CommonZombieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerCustomGoals() {
        // Replace the default HurtByTargetGoal with one that alerts ALL horde zombies
        this.targetSelector.removeGoal(this.targetSelector.getAvailableGoals().stream()
                .filter(goal -> goal.getGoal() instanceof HurtByTargetGoal)
                .findFirst()
                .map(goal -> goal.getGoal())
                .orElse(null));
        
        // Add cross-type alert behavior
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this) {
            @Override
            public void start() {
                super.start();
                // Alert all horde zombies, not just same type
                alertNearbyZombies(this.mob.getLastHurtByMob());
            }
        }.setAlertOthers());
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypes.DROWN) || damageSource.is(DamageTypes.WITHER)) {
            return false;
        }
        
        // Set anger when hurt
        if (!this.level().isClientSide() && damageSource.getEntity() instanceof LivingEntity attacker) {
            this.startPersistentAngerTimer();
            this.setPersistentAngerTarget(attacker.getUUID());
        }
        
        return super.hurt(damageSource, amount);
    }

    // NeutralMob implementation
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(800 + this.random.nextInt(200)); // 40-50 seconds
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        
        // Update anger management
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readPersistentAngerSaveData(this.level(), tag);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 0.0);
    }

    public static void init() {
        SpawnPlacements.register(ModEntities.COMMON_ZOMBIE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevel, reason, pos, random) ->
                        Config.getCommonZombiesSpawnNaturally()
                                && !(serverLevel.getBiome(pos).is(Biomes.MUSHROOM_FIELDS))
                                && !(serverLevel.getBiome(pos).is(Biomes.DEEP_DARK))
                                && serverLevel.getDifficulty() != Difficulty.PEACEFUL
                                && serverLevel.getRawBrightness(pos, 0) <= 8
                                && Mob.checkMobSpawnRules(entityType, serverLevel, reason, pos, random));
    }
}
