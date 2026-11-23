package uk.co.dotcode.coin;

import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

import uk.co.dotcode.coin.commands.ModCommands;
import uk.co.dotcode.coin.config.ConfigHandler;
import uk.co.dotcode.coin.config.ModConfig;

public class DCM {

	public static final String MOD_ID = "dotcoinmod";

	public static ModConfig modConfig;

	public static void init() {
		ConfigHandler.load();
	}

	@SubscribeEvent
	public static void onFinalizeSpawn(FinalizeSpawnEvent event) {

		if (DCM.modConfig.loot.lootFromSpawnerEntities) {
			return;
		}

		if (event.getSpawnType() == MobSpawnType.SPAWNER) {
			event.getEntity()
					.getPersistentData()
					.putBoolean("spawnedFromSpawner", true);
		}
	}
}