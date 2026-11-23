package uk.co.dotcode.coin.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.item.ModItems;

public record SendDisabledFeaturesPacket(
		boolean copperCoin,
		boolean ironCoin,
		boolean goldCoin,
		boolean platinumCoin,
		boolean walletEnabled,
		boolean autoPickupEnabled
) implements CustomPacketPayload {

	public static final Type<SendDisabledFeaturesPacket> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"disabled_features"
			));

	public static final StreamCodec<RegistryFriendlyByteBuf, SendDisabledFeaturesPacket> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.BOOL,
					SendDisabledFeaturesPacket::copperCoin,
					ByteBufCodecs.BOOL,
					SendDisabledFeaturesPacket::ironCoin,
					ByteBufCodecs.BOOL,
					SendDisabledFeaturesPacket::goldCoin,
					ByteBufCodecs.BOOL,
					SendDisabledFeaturesPacket::platinumCoin,
					ByteBufCodecs.BOOL,
					SendDisabledFeaturesPacket::walletEnabled,
					ByteBufCodecs.BOOL,
					SendDisabledFeaturesPacket::autoPickupEnabled,
					SendDisabledFeaturesPacket::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handleServer(
			SendDisabledFeaturesPacket packet,
			IPayloadContext context) {

		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				PacketHandler.sendToClient(
						new SendDisabledFeaturesPacket(
								DCM.modConfig.coinConfig.disableCopperCoin,
								DCM.modConfig.coinConfig.disableIronCoin,
								DCM.modConfig.coinConfig.disableGoldCoin,
								DCM.modConfig.coinConfig.disablePlatinumCoin,
								DCM.modConfig.gui.walletEnabled,
								DCM.modConfig.loot.serverAllowAutoPickup
						),
						player
				);
			}
		});
	}

	public static void handleClient(
			SendDisabledFeaturesPacket packet,
			IPayloadContext context) {

		context.enqueueWork(() -> {
			ModItems.updateDisabledCoins(
					packet.copperCoin() ? 1 : 0,
					packet.ironCoin() ? 1 : 0,
					packet.goldCoin() ? 1 : 0,
					packet.platinumCoin() ? 1 : 0
			);

			DCM.modConfig.gui.walletEnabled =
					packet.walletEnabled();

			DCM.modConfig.loot.serverAllowAutoPickup =
					packet.autoPickupEnabled();
		});
	}
}