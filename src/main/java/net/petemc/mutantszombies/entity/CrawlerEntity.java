package net.petemc.mutantszombies.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraftforge.registries.ForgeRegistries;
import net.petemc.mutantszombies.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CrawlerEntity extends AbstractHordeZombieEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(CrawlerEntity.class, EntityDataSerializers.BYTE);

    public CrawlerEntity(EntityType<CrawlerEntity> type, Level world) {
        super(type, world);
        this.xpReward = 5;
    }

    @Override
    protected void registerCustomGoals() {
        // Add Crawler-specific leap attack before melee
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
    }

    protected void dropCustomDeathLoot(@NotNull DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        //TODO add drop
    }

    @Override
    protected SoundEvent getStepSoundEvent() {
        return Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.cave_vines.step")));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.horse.breathe"));
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.husk.death")));
    }

    /**
     * Returns {@code true} if this entity should move as if it were on a ladder (either because it's actually on a
     * ladder, or for AI reasons)
     */
    public boolean onClimbable() {
        return this.isClimbing();
    }

    /**
     * Returns {@code true} if the WatchableObject (Byte) is 0x01 otherwise returns {@code false}. The WatchableObject is
     * updated using setBesideClimbableBlock.
     */
    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypes.FALL)) {
            return false;
        }
        return super.hurt(damageSource, amount);
    }

    public static void init() {
        SpawnPlacements.register(ModEntities.CRAWLER.get(), Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevel, reason, pos, random) ->
                        Config.getCrawlersSpawnNaturally()
                                && !(serverLevel.getBiome(pos).is(Biomes.MUSHROOM_FIELDS))
                                && !(serverLevel.getBiome(pos).is(Biomes.DEEP_DARK))
                                && serverLevel.getDifficulty() != Difficulty.PEACEFUL
                                && Monster.isDarkEnoughToSpawn(serverLevel, pos, random)
                                && Mob.checkMobSpawnRules(entityType, serverLevel, reason, pos, random));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
            .add(Attributes.MAX_HEALTH, 6.0)
            .add(Attributes.MOVEMENT_SPEED, 0.35)
            .add(Attributes.ATTACK_DAMAGE, 3.0)
            .add(Attributes.ARMOR, 0.0)
            .add(Attributes.ATTACK_KNOCKBACK, 0.0)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }
}
