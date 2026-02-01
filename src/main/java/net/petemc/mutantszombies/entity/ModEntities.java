package net.petemc.mutantszombies.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.mutantszombies.MutantsZombies;

public class ModEntities {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
                        .create(ForgeRegistries.ENTITY_TYPES, MutantsZombies.MOD_ID);

        public static final RegistryObject<EntityType<CommonZombieEntity>> COMMON_ZOMBIE = ENTITY_TYPES
                        .register("common_zombie",
                                        () -> EntityType.Builder.of(CommonZombieEntity::new, MobCategory.MONSTER)
                                                        .setShouldReceiveVelocityUpdates(true)
                                                        .setTrackingRange(50)
                                                        .setUpdateInterval(3)
                                                        .sized(0.6F, 1.95F)
                                                        .build("common_zombie"));

        public static final RegistryObject<EntityType<CrawlerEntity>> CRAWLER = ENTITY_TYPES.register("crawler",
                        () -> EntityType.Builder.of(CrawlerEntity::new, MobCategory.MONSTER)
                                        .setShouldReceiveVelocityUpdates(true)
                                        .setTrackingRange(15)
                                        .setUpdateInterval(3)
                                        .sized(1.1F, 0.9F)
                                        .build("crawler"));

        public static final RegistryObject<EntityType<BoomerEntity>> BOOMER = ENTITY_TYPES.register("boomer",
                        () -> EntityType.Builder.of(BoomerEntity::new, MobCategory.MONSTER)
                                        .setShouldReceiveVelocityUpdates(true)
                                        .setTrackingRange(55)
                                        .setUpdateInterval(3)
                                        .sized(1.8F, 2.7F)
                                        .build("boomer"));

        public static final RegistryObject<EntityType<ZombieBruteEntity>> ZOMBIE_BRUTE = ENTITY_TYPES
                        .register("zombie_brute",
                                        () -> EntityType.Builder.of(ZombieBruteEntity::new, MobCategory.MONSTER)
                                                        .setShouldReceiveVelocityUpdates(true)
                                                        .setTrackingRange(35)
                                                        .setUpdateInterval(3)
                                                        .sized(2.0F, 2.7F)
                                                        .build("zombie_brute"));

        public static void initModEntities() {
                CommonZombieEntity.init();
                CrawlerEntity.init();
                BoomerEntity.init();
                ZombieBruteEntity.init();
        }

        public static void register(IEventBus eventBus) {
                ENTITY_TYPES.register(eventBus);
        }
}
