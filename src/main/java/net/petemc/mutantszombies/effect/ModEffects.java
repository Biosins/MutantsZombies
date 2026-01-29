package net.petemc.mutantszombies.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.mutantszombies.MutantsZombies;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MutantsZombies.MOD_ID);

    public static final RegistryObject<MobEffect> SLIMED = MOB_EFFECTS.register("slimed",
            () -> new SlimedEffect());

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
