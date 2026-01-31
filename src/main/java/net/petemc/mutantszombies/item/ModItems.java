package net.petemc.mutantszombies.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.mutantszombies.MutantsZombies;
import net.petemc.mutantszombies.entity.ModEntities;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        MutantsZombies.MOD_ID);

        public static final RegistryObject<Item> CRAWLER_SPAWN_EGG = ITEMS.register("crawler_spawn_egg",
                        () -> new ForgeSpawnEggItem(ModEntities.CRAWLER, -10027264, -6684775, (new Item.Properties())));

        public static final RegistryObject<Item> ZOMBIE_BRUTE_SPAWN_EGG = ITEMS.register("zombie_brute_spawn_egg",
                        () -> new ForgeSpawnEggItem(ModEntities.ZOMBIE_BRUTE, -16764109, -16738048,
                                        (new Item.Properties())));

        public static final RegistryObject<Item> BOOMER_SPAWN_EGG = ITEMS.register("boomer_spawn_egg",
                        () -> new ForgeSpawnEggItem(ModEntities.BOOMER, -3407872, -16711936,
                                        (new Item.Properties())));

        public static final RegistryObject<Item> COMMON_ZOMBIE_SPAWN_EGG = ITEMS.register("common_zombie_spawn_egg",
                        () -> new ForgeSpawnEggItem(ModEntities.COMMON_ZOMBIE, -10066330, -16777216,
                                        (new Item.Properties())));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}
