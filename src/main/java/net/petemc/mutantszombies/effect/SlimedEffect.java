package net.petemc.mutantszombies.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SlimedEffect extends MobEffect {
    public SlimedEffect() {
        super(MobEffectCategory.HARMFUL, 0x4B7F00); // Green slime color
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        // Apply Blindness effect while slimed
        if (!entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
        }

        // Spawn green slime particles
        if (entity.level().isClientSide()) {
            for (int i = 0; i < 2; i++) {
                entity.level().addParticle(
                        ParticleTypes.ITEM_SLIME,
                        entity.getRandomX(0.5),
                        entity.getRandomY(),
                        entity.getRandomZ(0.5),
                        0.0, 0.0, 0.0
                );
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0; // Every 10 ticks (0.5 seconds)
    }
}
