package net.petemc.mutantszombies.entity;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractHordeZombieEntity extends Monster {

    public AbstractHordeZombieEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.0F);
        this.xpReward = 6;
    }

    /**
     * Template method for subclasses to add custom AI goals
     */
    // protected abstract void registerCustomGoals();

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
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
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.hurt")));
    }

    @Override
    public SoundEvent getDeathSound() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.death")));
    }

    /**
     * Hook method for custom step sound - override in subclasses for custom
     * behavior
     */
    protected SoundEvent getStepSoundEvent() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.step")));
    }

    /**
     * Hook method for custom ambient sound - override in subclasses for custom
     * behavior
     */
    @Override
    public SoundEvent getAmbientSound() {
        return Objects.requireNonNull(
                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.zombie.ambient")));
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
