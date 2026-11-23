package uk.co.dotcode.coin.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.item.ModItems;
import uk.co.dotcode.coin.packet.PacketHandler;
import uk.co.dotcode.coin.packet.SendAutoPickupPacket;
import uk.co.dotcode.coin.packet.SendCoinConversionRatePacket;
import uk.co.dotcode.coin.packet.SendCoinInventoryPacket;
import uk.co.dotcode.coin.packet.SendDisabledFeaturesPacket;

public class WorldJoinEvent {

	public static void onWorldLogin(Player player) {

		Level level = player.level();

		if (!level.isClientSide()) {

			ServerPlayer serverPlayer = (ServerPlayer) player;

			PacketHandler.sendToClient(
					new SendCoinConversionRatePacket(
							DCM.modConfig.coinConfig.conversionRate
					),
					serverPlayer
			);

			PacketHandler.sendToClient(
					new SendDisabledFeaturesPacket(
							DCM.modConfig.coinConfig.disableCopperCoin,
							DCM.modConfig.coinConfig.disableIronCoin,
							DCM.modConfig.coinConfig.disableGoldCoin,
							DCM.modConfig.coinConfig.disablePlatinumCoin,
							DCM.modConfig.gui.walletEnabled,
							DCM.modConfig.loot.serverAllowAutoPickup
					),
					serverPlayer
			);

			if (DCM.modConfig.gui.walletEnabled) {
				PacketHandler.sendToClient(
						SendCoinInventoryPacket.fromPlayer(serverPlayer),
						serverPlayer
				);
			}

		} else {

			ModItems.updateCoinConversion(
					DCM.modConfig.coinConfig.conversionRate
			);

			ModItems.updateDisabledCoins(
					DCM.modConfig.coinConfig.disableCopperCoin,
					DCM.modConfig.coinConfig.disableIronCoin,
					DCM.modConfig.coinConfig.disableGoldCoin,
					DCM.modConfig.coinConfig.disablePlatinumCoin
			);

			if (DCM.modConfig.gui.walletEnabled) {
				PacketHandler.sendToServer(
						new SendAutoPickupPacket(
								DCM.modConfig.loot.autoPickup
						)
				);
			}
		}
	}
}