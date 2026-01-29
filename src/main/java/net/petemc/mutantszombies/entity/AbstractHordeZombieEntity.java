package net.petemc.mutantszombies.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.petemc.mutantszombies.effect.ModEffects;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class AbstractHordeZombieEntity extends Monster {

    public AbstractHordeZombieEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.0F);
        this.xpReward = 6;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        
        // Common goals for all horde zombies
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        // Common targeting - Priority 1: Players with Slimed effect
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this, Player.class, 10, true, false,
                (entity) -> entity.hasEffect(ModEffects.SLIMED.get())
        ));
        
        // Priority 2: Retaliate when hurt
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this)
                .setAlertOthers(this.getClass()));
        
        // Priority 3: Normal player targeting
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                this, Player.class, false));
        
        // Priority 4: Target Iron Golems
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(
                this, IronGolem.class, true, true));
        
        // Priority 5: Target Villagers
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(
                this, AbstractVillager.class, true, true));
        
        // Hook for subclass-specific goals
        registerCustomGoals();
    }

    /**
     * Template method for subclasses to add custom AI goals
     */
    protected abstract void registerCustomGoals();

    /**
     * Alert nearby horde zombies when this entity is hurt
     */
    protected void alertNearbyZombies(LivingEntity attacker) {
        AABB searchBox = this.getBoundingBox().inflate(32.0);
        List<AbstractHordeZombieEntity> nearbyZombies = this.level().getEntitiesOfClass(
                AbstractHordeZombieEntity.class,
                searchBox,
                (entity) -> entity != this && entity.isAlive()
        );

        for (AbstractHordeZombieEntity zombie : nearbyZombies) {
            zombie.setTarget(attacker);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float amount) {
        // Alert nearby zombies when hurt
        if (!this.level().isClientSide() && damageSource.getEntity() instanceof LivingEntity attacker) {
            alertNearbyZombies(attacker);
        }

        return super.hurt(damageSource, amount);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(getStepSoundEvent(), 0.15F, 1.0F);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.hurt"))
        );
    }

    @Override
    public SoundEvent getDeathSound() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.death"))
        );
    }

    /**
     * Hook method for custom step sound - override in subclasses for custom behavior
     */
    protected SoundEvent getStepSoundEvent() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.step"))
        );
    }

    /**
     * Hook method for custom ambient sound - override in subclasses for custom behavior
     */
    @Override
    public SoundEvent getAmbientSound() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.ambient"))
        );
    }

    /**
     * Shared attribute base for all horde zombies
     */
    protected static AttributeSupplier.Builder createBaseAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1);
    }
}
