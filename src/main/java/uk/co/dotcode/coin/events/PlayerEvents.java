package uk.co.dotcode.coin.events;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.entity.CoinData;
import uk.co.dotcode.coin.entity.PlayerEntityExtension;
import uk.co.dotcode.coin.item.ModItems;

public class PlayerEvents {

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (DCM.modConfig.loot.dropCoinsOnPlayerDeath) {
			return;
		}

		PlayerEntityExtension oldPlayer =
				(PlayerEntityExtension) event.getOriginal();

		PlayerEntityExtension newPlayer =
				(PlayerEntityExtension) event.getEntity();

		if (oldPlayer.getCoinData() != null) {
			newPlayer.setCoinData(oldPlayer.getCoinData());
		} else {
			CoinData newCoinData = new CoinData();
			newCoinData.setAll(0, 0, 0, 0);
			newPlayer.setCoinData(newCoinData);
		}
	}

	@SubscribeEvent
	public static void onItemPickup(ItemEntityPickupEvent.Pre event) {

		if (DCM.modConfig == null
				|| !DCM.modConfig.gui.walletEnabled) {
			return;
		}

		if (!(event.getPlayer() instanceof ServerPlayer player)) {
			return;
		}

		PlayerEntityExtension extension =
				(PlayerEntityExtension) player;

		if (!extension.isAutoPickupEnabled()) {
			return;
		}

		ItemEntity itemEntity = event.getItemEntity();
		ItemStack stack = itemEntity.getItem();

		int coinType;

		if (stack.is(ModItems.COPPER_COIN.get())) {
			coinType = 0;
		} else if (stack.is(ModItems.IRON_COIN.get())) {
			coinType = 1;
		} else if (stack.is(ModItems.GOLD_COIN.get())) {
			coinType = 2;
		} else if (stack.is(ModItems.PLATINUM_COIN.get())) {
			coinType = 3;
		} else {
			return;
		}

		int amount = stack.getCount();

		CoinUtil.depositCoins(player, coinType, amount);

		CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.trigger(
				player,
				stack,
				itemEntity
		);

		stack.setCount(0);
		itemEntity.setRemoved(RemovalReason.DISCARDED);

		event.setCanPickup(TriState.FALSE);
	}
}