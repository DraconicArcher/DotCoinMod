package uk.co.dotcode.coin.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.item.ModItems;

public record SendCoinConversionRatePacket(int data) implements CustomPacketPayload {

	public static final Type<SendCoinConversionRatePacket> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"coin_conversion_rate"
			));

	public static final StreamCodec<RegistryFriendlyByteBuf, SendCoinConversionRatePacket> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT,
					SendCoinConversionRatePacket::data,
					SendCoinConversionRatePacket::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handleServer(
			SendCoinConversionRatePacket packet,
			IPayloadContext context) {

		context.enqueueWork(() -> {
			if (packet.data() != DCM.modConfig.coinConfig.conversionRate
					&& context.player() instanceof ServerPlayer player) {

				PacketDistributor.sendToPlayer(
						player,
						new SendCoinConversionRatePacket(
								DCM.modConfig.coinConfig.conversionRate
						)
				);
			}
		});
	}

	public static void handleClient(
			SendCoinConversionRatePacket packet,
			IPayloadContext context) {

		context.enqueueWork(() ->
				ModItems.updateCoinConversion(packet.data())
		);
	}
}