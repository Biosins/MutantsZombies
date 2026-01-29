package net.petemc.mutantszombies.entity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petemc.mutantszombies.MutantsZombies;

public class RegisterEntityAttributes {
    @Mod.EventBusSubscriber(modid = MutantsZombies.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.COMMON_ZOMBIE.get(), CommonZombieEntity.createAttributes().build());
            event.put(ModEntities.CRAWLER.get(), CrawlerEntity.createAttributes().build());
            event.put(ModEntities.BOOMER.get(), BoomerEntity.createAttributes().build());
            event.put(ModEntities.ZOMBIE_BRUTE.get(), ZombieBruteEntity.createAttributes().build());
        }
    }

}


