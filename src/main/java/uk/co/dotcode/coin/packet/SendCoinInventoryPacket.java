package uk.co.dotcode.coin.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.entity.CoinData;
import uk.co.dotcode.coin.entity.PlayerEntityExtension;

public record SendCoinInventoryPacket(
		int copper,
		int iron,
		int gold,
		int platinum
) implements CustomPacketPayload {

	public static final Type<SendCoinInventoryPacket> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"coin_inventory"
			));

	public static final StreamCodec<RegistryFriendlyByteBuf, SendCoinInventoryPacket> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT,
					SendCoinInventoryPacket::copper,
					ByteBufCodecs.VAR_INT,
					SendCoinInventoryPacket::iron,
					ByteBufCodecs.VAR_INT,
					SendCoinInventoryPacket::gold,
					ByteBufCodecs.VAR_INT,
					SendCoinInventoryPacket::platinum,
					SendCoinInventoryPacket::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static SendCoinInventoryPacket fromPlayer(Player player) {
		CoinData coins = ((PlayerEntityExtension) player).getCoinData();

		if (coins == null) {
			coins = new CoinData();
			coins.setAll(0, 0, 0, 0);
			((PlayerEntityExtension) player).setCoinData(coins);
		}

		return new SendCoinInventoryPacket(
				coins.getValue(0),
				coins.getValue(1),
				coins.getValue(2),
				coins.getValue(3)
		);
	}

	public static void handle(
			SendCoinInventoryPacket packet,
			IPayloadContext context) {

		context.enqueueWork(() -> {
			CoinUtil.copperCoinClient = packet.copper();
			CoinUtil.ironCoinClient = packet.iron();
			CoinUtil.goldCoinClient = packet.gold();
			CoinUtil.platinumCoinClient = packet.platinum();
		});
	}
}