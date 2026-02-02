package net.petemc.mutantszombies.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.petemc.mutantszombies.config.Config;

public class CommonZombieEntity extends AbstractHordeZombieEntity implements NeutralMob {
    private static final double BASE_SPEED = 0.20; // Slow when calm
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    public CommonZombieEntity(EntityType<CommonZombieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false, entity -> {
                    return entity.hasEffect(net.petemc.mutantszombies.effect.ModEffects.SLIMED.get());
                }));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    private void alertOthers() {
        double $$0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB $$1 = AABB.unitCubeFromLowerCorner(this.position()).inflate($$0, 10.0, $$0);
        this.level()
                .getEntitiesOfClass(CommonZombieEntity.class, $$1, EntitySelector.NO_SPECTATORS)
                .stream()
                .filter($$0x -> $$0x != this)
                .filter($$0x -> $$0x.getTarget() == null)
                .filter($$0x -> !$$0x.isAlliedTo(this.getTarget()))
                .forEach($$0x -> $$0x.setTarget(this.getTarget()));
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, BASE_SPEED) // Start slow
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 0.0);
    }

    public static void init() {
        SpawnPlacements.register(ModEntities.COMMON_ZOMBIE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevel, reason, pos, random) -> Config.getCommonZombiesSpawnNaturally()
                        && !(serverLevel.getBiome(pos).is(Biomes.MUSHROOM_FIELDS))
                        && !(serverLevel.getBiome(pos).is(Biomes.DEEP_DARK))
                        && serverLevel.getDifficulty() != Difficulty.PEACEFUL
                        && Mob.checkMobSpawnRules(entityType, serverLevel, reason, pos, random));
    }
}
