package uk.co.dotcode.coin.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;

public record SendCoinWithdrawPacket(int coinType, int amount)
		implements CustomPacketPayload {

	public static final Type<SendCoinWithdrawPacket> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"coin_withdraw"
			));

	public static final StreamCodec<RegistryFriendlyByteBuf, SendCoinWithdrawPacket> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT,
					SendCoinWithdrawPacket::coinType,
					ByteBufCodecs.VAR_INT,
					SendCoinWithdrawPacket::amount,
					SendCoinWithdrawPacket::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(
			SendCoinWithdrawPacket packet,
			IPayloadContext context) {

		context.enqueueWork(() ->
				CoinUtil.withdrawCoins(
						context.player(),
						packet.coinType(),
						packet.amount()
				)
		);
	}
}