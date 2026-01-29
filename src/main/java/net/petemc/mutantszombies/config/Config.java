package net.petemc.mutantszombies.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.petemc.mutantszombies.MutantsZombies;

@Mod.EventBusSubscriber(modid = MutantsZombies.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static boolean getCommonZombiesSpawnNaturally() {
        return commonZombiesSpawnNaturally;
    }

    public static boolean getCrawlersSpawnNaturally() {
        return crawlersSpawnNaturally;
    }

    public static boolean getBoomersSpawnNaturally() {
        return boomersSpawnNaturally;
    }

    public static boolean getZombieBrutesSpawnNaturally() {
        return zombieBrutesSpawnNaturally;
    }

    public static double getBoomerAlertRadius() {
        return boomerAlertRadius;
    }

    public static int getBruteSpawnCap() {
        return bruteSpawnCap;
    }

    public static int getBruteSpawnCooldown() {
        return bruteSpawnCooldown;
    }

    public static boolean getZombieBrutesBreakLogsAndLeavesAroundThem() {
        return zombieBrutesBreakLogsAndLeavesAroundThem;
    }

    // Server Config
    private static final ForgeConfigSpec.Builder BUILDER_SERVER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue COMMON_ZOMBIES_SPAWN_NATURALLY = BUILDER_SERVER
            .comment("If true, Common Zombies will spawn naturally | default: true")
            .define("commonZombiesSpawnNaturally", true);

    private static final ForgeConfigSpec.BooleanValue CRAWLERS_SPAWN_NATURALLY = BUILDER_SERVER
            .comment("If true, Crawlers will spawn naturally | default: true")
            .define("crawlersSpawnNaturally", true);

    private static final ForgeConfigSpec.BooleanValue BOOMERS_SPAWN_NATURALLY = BUILDER_SERVER
            .comment("If true, Boomers will spawn naturally | default: true")
            .define("boomersSpawnNaturally", true);

    private static final ForgeConfigSpec.BooleanValue ZOMBIE_BRUTES_SPAWN_NATURALLY = BUILDER_SERVER
            .comment("If true, Zombie Brutes will spawn naturally | default: true")
            .define("zombieBrutesSpawnNaturally", true);

    private static final ForgeConfigSpec.DoubleValue BOOMER_ALERT_RADIUS = BUILDER_SERVER
            .comment("Radius in blocks for Boomer explosion to alert zombies | default: 30.0")
            .defineInRange("boomerAlertRadius", 30.0, 10.0, 50.0);

    private static final ForgeConfigSpec.IntValue BRUTE_SPAWN_CAP = BUILDER_SERVER
            .comment("Maximum number of zombies a Brute can have spawned at once | default: 20")
            .defineInRange("bruteSpawnCap", 20, 5, 50);

    private static final ForgeConfigSpec.IntValue BRUTE_SPAWN_COOLDOWN = BUILDER_SERVER
            .comment("Cooldown in ticks between Brute zombie spawns (20 ticks = 1 second) | default: 600")
            .defineInRange("bruteSpawnCooldown", 600, 100, 1200);

    private static final ForgeConfigSpec.BooleanValue ZOMBIE_BRUTES_BREAK_LOGS_AND_LEAVES = BUILDER_SERVER
            .comment("If true, Zombie Brutes will break logs and leaves in their path | default: false")
            .define("zombieBrutesBreakLogsAndLeavesAroundThem", false);

   public static final ForgeConfigSpec SPEC_SERVER = BUILDER_SERVER.build();

    // Client Config
    private static final ForgeConfigSpec.Builder BUILDER_CLIENT = new ForgeConfigSpec.Builder();
    // no client config
    public static final ForgeConfigSpec SPEC_CLIENT = BUILDER_CLIENT.build();


    private static boolean commonZombiesSpawnNaturally = true;
    private static boolean crawlersSpawnNaturally = true;
    private static boolean boomersSpawnNaturally = true;
    private static boolean zombieBrutesSpawnNaturally = true;
    private static double boomerAlertRadius = 30.0;
    private static int bruteSpawnCap = 20;
    private static int bruteSpawnCooldown = 600;
    private static boolean zombieBrutesBreakLogsAndLeavesAroundThem = false;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (SPEC_SERVER.isLoaded()) {
            MutantsZombies.LOGGER.info("Loading {} server config", MutantsZombies.MOD_ID);
            commonZombiesSpawnNaturally = COMMON_ZOMBIES_SPAWN_NATURALLY.get();
            crawlersSpawnNaturally = CRAWLERS_SPAWN_NATURALLY.get();
            boomersSpawnNaturally = BOOMERS_SPAWN_NATURALLY.get();
            zombieBrutesSpawnNaturally = ZOMBIE_BRUTES_SPAWN_NATURALLY.get();
            boomerAlertRadius = BOOMER_ALERT_RADIUS.get();
            bruteSpawnCap = BRUTE_SPAWN_CAP.get();
            bruteSpawnCooldown = BRUTE_SPAWN_COOLDOWN.get();
            zombieBrutesBreakLogsAndLeavesAroundThem = ZOMBIE_BRUTES_BREAK_LOGS_AND_LEAVES.get();
        }
        if (SPEC_CLIENT.isLoaded()) {
            MutantsZombies.LOGGER.info("Loading {} client config", MutantsZombies.MOD_ID);
           // no client config
        }
    }
}
