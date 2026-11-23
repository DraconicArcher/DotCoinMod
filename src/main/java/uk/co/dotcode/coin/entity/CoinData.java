package uk.co.dotcode.coin.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.packet.PacketHandler;
import uk.co.dotcode.coin.packet.SendCoinInventoryPacket;

public class CoinData {

	private final int[] coinSlots = new int[4];

	public int getValue(int slotId) {
		return coinSlots[slotId];
	}

	public void setValue(int slotId, int value) {
		coinSlots[slotId] = value;
	}

	public ItemStack addToTotal(int slotId, int value) {
		ItemStack remainder = ItemStack.EMPTY;

		if (coinSlots[slotId] + value > DCM.modConfig.gui.walletMaxSlotSize) {
			int remaining =
					(coinSlots[slotId] + value)
							- DCM.modConfig.gui.walletMaxSlotSize;

			coinSlots[slotId] =
					DCM.modConfig.gui.walletMaxSlotSize;

			remainder = CoinUtil.generateCoinStack(slotId, remaining);
		} else {
			coinSlots[slotId] += value;
		}

		return remainder;
	}

	public void setAll(int copper, int iron, int gold, int platinum) {
		coinSlots[0] = copper;
		coinSlots[1] = iron;
		coinSlots[2] = gold;
		coinSlots[3] = platinum;
	}

	public void sendChangedData(Player player) {
		PacketHandler.sendToClient(
				SendCoinInventoryPacket.fromPlayer(player),
				(ServerPlayer) player
		);
	}
}