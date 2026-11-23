package uk.co.dotcode.coin.neoforge;

import java.util.Random;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.events.WorldJoinEvent;
import uk.co.dotcode.coin.item.ModItems;

public class EventHandler {



	@SubscribeEvent
	public void onWorldLogin(PlayerEvent.PlayerLoggedInEvent event) {
		ModItems.handleCurrencyStuff();
		WorldJoinEvent.onWorldLogin(event.getEntity());
	}

	@SubscribeEvent
	public void onLootLoad(LootTableLoadEvent event) {
		if (!DCM.modConfig.loot.generateChestLoot) {
			return;
		}

		String prefix = "minecraft:chests/";
		String name = event.getName().toString();

		if (!name.startsWith(prefix)) {
			return;
		}

		int copperWeight = DCM.modConfig.loot.droprates.copper;
		int ironWeight = DCM.modConfig.loot.droprates.iron;
		int goldWeight = DCM.modConfig.loot.droprates.gold;
		int platinumWeight = DCM.modConfig.loot.droprates.platinum;

		int randomVal =
				new Random().nextInt(DCM.modConfig.loot.chestLootLimit) + 1;

		LootPool.Builder pool = LootPool.lootPool()
				.setRolls(ConstantValue.exactly(randomVal));

		if (!ModItems.COPPER_COIN.get().isDisabled) {
			pool.add(
					LootItem.lootTableItem(ModItems.COPPER_COIN.get())
							.setWeight(copperWeight)
			);
		}

		if (!ModItems.IRON_COIN.get().isDisabled) {
			pool.add(
					LootItem.lootTableItem(ModItems.IRON_COIN.get())
							.setWeight(ironWeight)
			);
		}

		if (!ModItems.GOLD_COIN.get().isDisabled) {
			pool.add(
					LootItem.lootTableItem(ModItems.GOLD_COIN.get())
							.setWeight(goldWeight)
			);
		}

		if (!ModItems.PLATINUM_COIN.get().isDisabled) {
			pool.add(
					LootItem.lootTableItem(ModItems.PLATINUM_COIN.get())
							.setWeight(platinumWeight)
			);
		}

		event.getTable().addPool(pool.build());
	}
}