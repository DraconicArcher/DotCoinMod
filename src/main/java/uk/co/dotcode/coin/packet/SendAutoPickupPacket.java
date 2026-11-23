package uk.co.dotcode.coin.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.entity.PlayerEntityExtension;

public record SendAutoPickupPacket(boolean autoPickup)
		implements CustomPacketPayload {

	public static final Type<SendAutoPickupPacket> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"send_auto_pickup"
			));

	public static final StreamCodec<
			RegistryFriendlyByteBuf,
			SendAutoPickupPacket
			> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.BOOL,
					SendAutoPickupPacket::autoPickup,
					SendAutoPickupPacket::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(
			SendAutoPickupPacket packet,
			IPayloadContext context) {

		context.enqueueWork(() -> {

			if (!(context.player() instanceof PlayerEntityExtension extension)) {
				return;
			}

			boolean allowed =
					DCM.modConfig.loot.serverAllowAutoPickup;

			if (!allowed) {
				extension.setAutoPickup(0);
				return;
			}

			extension.setAutoPickup(
					packet.autoPickup() ? 1 : 0
			);
		});
	}
}