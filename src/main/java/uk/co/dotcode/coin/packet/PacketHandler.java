package uk.co.dotcode.coin.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import uk.co.dotcode.coin.DCM;

@EventBusSubscriber(
		modid = DCM.MOD_ID,
		bus = EventBusSubscriber.Bus.MOD
)
public class PacketHandler {

	@SubscribeEvent
	public static void registerPayloads(
			RegisterPayloadHandlersEvent event) {

		PayloadRegistrar registrar = event.registrar("1");

		// =========================
		// Client <-> Server
		// =========================

		registrar.playBidirectional(
				SendCoinConversionRatePacket.TYPE,
				SendCoinConversionRatePacket.STREAM_CODEC,
				new DirectionalPayloadHandler<>(
						SendCoinConversionRatePacket::handleServer,
						SendCoinConversionRatePacket::handleClient
				)
		);

		registrar.playBidirectional(
				SendDisabledFeaturesPacket.TYPE,
				SendDisabledFeaturesPacket.STREAM_CODEC,
				new DirectionalPayloadHandler<>(
						SendDisabledFeaturesPacket::handleServer,
						SendDisabledFeaturesPacket::handleClient
				)
		);

		// =========================
		// Client -> Server
		// =========================

		registrar.playToServer(
				SendCoinWithdrawPacket.TYPE,
				SendCoinWithdrawPacket.STREAM_CODEC,
				SendCoinWithdrawPacket::handle
		);

		registrar.playToServer(
				SendCoinDepositPacket.TYPE,
				SendCoinDepositPacket.STREAM_CODEC,
				SendCoinDepositPacket::handle
		);

		registrar.playToServer(
				SendAutoPickupPacket.TYPE,
				SendAutoPickupPacket.STREAM_CODEC,
				SendAutoPickupPacket::handle
		);

		// =========================
		// Server -> Client
		// =========================

		registrar.playToClient(
				SendCoinInventoryPacket.TYPE,
				SendCoinInventoryPacket.STREAM_CODEC,
				SendCoinInventoryPacket::handle
		);
	}

	public static void sendToClient(
			CustomPacketPayload message,
			ServerPlayer player) {

		PacketDistributor.sendToPlayer(player, message);
	}

	public static void sendToServer(
			CustomPacketPayload message) {

		PacketDistributor.sendToServer(message);
	}
}