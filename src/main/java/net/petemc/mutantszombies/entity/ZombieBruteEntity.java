package net.petemc.mutantszombies.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.petemc.mutantszombies.config.Config;
import net.petemc.mutantszombies.sound.ModSounds;

public class ZombieBruteEntity extends AbstractHordeZombieEntity {
    private int attackTicksLeft;
    private int treeBreakCooldown = 30;
    private final List<UUID> spawnedZombies = new ArrayList<>();
    private BlockPos lastTargetPos = null;
    private int stuckTicks = 0;
    private static final String BRUTE_EXPLOSION_SOURCE = "brute_melee_explosion";
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.NOTCHED_20);

    public ZombieBruteEntity(EntityType<ZombieBruteEntity> type, Level world) {
        super(type, world);
        this.xpReward = 15;
    }

    @Override
    protected void registerGoals() {
        // Add horde magnet goal
        this.goalSelector.addGoal(3, new HordeMagnetGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        // Update boss bar health
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(@javax.annotation.Nullable net.minecraft.network.chat.Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ROAR_SOUND.get();
    }

    @Override
    protected SoundEvent getStepSoundEvent() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.rooted_dirt.step")));
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return Objects
                .requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.death")));
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        // Immune to own melee explosion
        if (damageSource.getMsgId().equals(BRUTE_EXPLOSION_SOURCE)) {
            return false;
        }
        return super.hurt(damageSource, amount);
    }

    public void lavaHurt() {
        if (this.hurt(this.damageSources().lava(), 4.0F)) {
            this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }
    }

    public int getAttackAnimationTick() {
        return this.attackTicksLeft;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.attackTicksLeft > 0) {
            this.attackTicksLeft--;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        boolean bl = super.doHurtTarget(target);
        this.attackTicksLeft = 10;
        this.level().broadcastEntityEvent(this, (byte) 4);
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);

        // Only create explosion when pathfinding fails (brute is stuck)
        if (!this.level().isClientSide() && isStuckOrCannotPath()) {
            // Calculate explosion position offset away from brute (in front/towards target)
            double offsetDistance = this.getBbWidth() + 0.5; // Just beyond brute's hitbox
            net.minecraft.world.phys.Vec3 direction = target.position().subtract(this.position()).normalize();
            double explosionX = this.getX() + direction.x * offsetDistance;
            double explosionY = this.getY() + 0.5;
            double explosionZ = this.getZ() + direction.z * offsetDistance;

            // Create custom damage source for this specific explosion
            DamageSource bruteExplosionSource = new DamageSource(
                    this.level().registryAccess()
                            .registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.EXPLOSION),
                    this, this) {
                @Override
                public String getMsgId() {
                    return BRUTE_EXPLOSION_SOURCE;
                }
            };

            // Smaller explosion (1.5F instead of 3.0F)
            Explosion explosion = new Explosion(
                    this.level(),
                    this,
                    bruteExplosionSource,
                    new BlockBreakingExplosionCalculator(),
                    explosionX, explosionY, explosionZ,
                    1.5F,
                    false,
                    Explosion.BlockInteraction.DESTROY);
            explosion.explode();
            explosion.finalizeExplosion(false); // false = no sound/particles
        }

        return bl;
    }

    /**
     * Check if brute is stuck or cannot path to target
     */
    private boolean isStuckOrCannotPath() {
        if (this.getTarget() == null) {
            return false;
        }

        // Check if navigation is stuck/cannot reach target
        if (this.getNavigation().isDone() && this.distanceTo(this.getTarget()) > 3.0) {
            return true;
        }

        // Track if brute hasn't moved towards target
        BlockPos targetPos = this.getTarget().blockPosition();
        if (lastTargetPos != null && lastTargetPos.equals(targetPos)) {
            stuckTicks++;
            if (stuckTicks > 40) { // Stuck for 2 seconds
                stuckTicks = 0;
                return true;
            }
        } else {
            lastTargetPos = targetPos;
            stuckTicks = 0;
        }

        return false;
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == 4) {
            this.attackTicksLeft = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (Config.getZombieBrutesBreakLogsAndLeavesAroundThem()) {
            if (treeBreakCooldown > 0) {
                treeBreakCooldown--;
            } else {
                if (!this.level().isClientSide) {
                    treeBreakCooldown = 40;

                    AABB box = new AABB(this.position(), this.position());
                    box = box.inflate(2);
                    box = box.inflate(0, 1, 0);

                    BlockPos.MutableBlockPos.betweenClosedStream(box)
                            .filter(c -> ((level().getBlockState(c).getBlock().toString().contains("leaves")) ||
                                    (level().getBlockState(c).getBlock().toString().contains("log"))))
                            .forEach(c -> {
                                String blockName = level().getBlockState(c).getBlock().toString();
                                if (!(blockName.contains("securitycraft") && blockName.contains("reinforced"))) {

                                    if (blockName.contains("leaves")) {
                                        this.level().destroyBlock(c, false);
                                    }
                                    if (blockName.contains("log")) {
                                        this.level().destroyBlock(c, true);
                                    }
                                }
                            });
                }
            }
        }
    }

    public static void init() {
        SpawnPlacements.register(ModEntities.ZOMBIE_BRUTE.get(), Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevel, reason, pos, random) -> Config.getZombieBrutesSpawnNaturally()
                        && !(serverLevel.getBiome(pos).is(Biomes.MUSHROOM_FIELDS))
                        && !(serverLevel.getBiome(pos).is(Biomes.DEEP_DARK))
                        && serverLevel.getDifficulty() != Difficulty.PEACEFUL
                        && Mob.checkMobSpawnRules(entityType, serverLevel, reason, pos, random));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.MOVEMENT_SPEED, 0.21)
                .add(Attributes.ATTACK_DAMAGE, 16.0)
                .add(Attributes.ARMOR, 16.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    // Inner class: Horde Magnet Goal
    static class HordeMagnetGoal extends Goal {
        private final ZombieBruteEntity brute;
        private int cooldown = 0;

        public HordeMagnetGoal(ZombieBruteEntity brute) {
            this.brute = brute;
        }

        @Override
        public boolean canUse() {
            return brute.getTarget() != null;
        }

        @Override
        public void tick() {
            if (cooldown > 0) {
                cooldown--;
                return;
            }

            // Clean up dead zombies from tracking list
            brute.spawnedZombies.removeIf(uuid -> {
                Entity entity = ((net.minecraft.server.level.ServerLevel) brute.level())
                        .getEntity(uuid);
                return entity == null || !entity.isAlive();
            });

            // Spawn new zombies if under cap
            int maxSpawns = Config.getBruteSpawnCap();
            if (brute.spawnedZombies.size() < maxSpawns) {
                int toSpawn = Math.min(3, maxSpawns - brute.spawnedZombies.size());

                for (int i = 0; i < toSpawn; i++) {
                    // Spawn CommonZombie near the brute
                    CommonZombieEntity zombie = ModEntities.COMMON_ZOMBIE.get().create(brute.level());
                    if (zombie != null) {
                        double angle = brute.random.nextDouble() * Math.PI * 2;
                        double distance = 8.0 + brute.random.nextDouble() * 8.0;
                        double x = brute.getX() + Math.cos(angle) * distance;
                        double z = brute.getZ() + Math.sin(angle) * distance;
                        double y = brute.getY();

                        zombie.moveTo(x, y, z, brute.random.nextFloat() * 360, 0);
                        zombie.finalizeSpawn(
                                (net.minecraft.server.level.ServerLevel) brute.level(),
                                brute.level().getCurrentDifficultyAt(zombie.blockPosition()),
                                MobSpawnType.MOB_SUMMONED,
                                null,
                                null);

                        // Set target to same as brute
                        if (brute.getTarget() != null) {
                            zombie.setTarget(brute.getTarget());
                        }

                        brute.level().addFreshEntity(zombie);
                        brute.spawnedZombies.add(zombie.getUUID());
                    }
                }

                cooldown = Config.getBruteSpawnCooldown();
            }
        }
    }

    // Inner class: Block Breaking Explosion Calculator
    static class BlockBreakingExplosionCalculator extends ExplosionDamageCalculator {
        @Override
        public Optional<Float> getBlockExplosionResistance(
                Explosion explosion, BlockGetter level,
                BlockPos pos, BlockState state, FluidState fluid) {
            // Lower resistance = easier to break
            return Optional.of(0.5F);
        }

        @Override
        public boolean shouldBlockExplode(
                Explosion explosion, BlockGetter level,
                BlockPos pos, BlockState state, float power) {
            // Only break blocks within 3x3x3 area of explosion center
            BlockPos center = BlockPos.containing(explosion.getPosition());
            return Math.abs(pos.getX() - center.getX()) <= 1
                    && Math.abs(pos.getY() - center.getY()) <= 1
                    && Math.abs(pos.getZ() - center.getZ()) <= 1;
        }
    }
}
