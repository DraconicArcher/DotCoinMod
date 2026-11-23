package uk.co.dotcode.coin.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.item.ModItems;

public record SendCoinDepositPacket(int coinType, int amount)
		implements CustomPacketPayload {

	public static final Type<SendCoinDepositPacket> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"coin_deposit"
			));

	public static final StreamCodec<RegistryFriendlyByteBuf, SendCoinDepositPacket> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT,
					SendCoinDepositPacket::coinType,
					ByteBufCodecs.VAR_INT,
					SendCoinDepositPacket::amount,
					SendCoinDepositPacket::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(
			SendCoinDepositPacket packet,
			IPayloadContext context) {

		context.enqueueWork(() -> {

			if (!(context.player() instanceof ServerPlayer player)) {
				return;
			}

			// Validate the requested coin type.
			if (packet.coinType() < 0 || packet.coinType() > 3) {
				return;
			}

			// Validate the requested amount.
			if (packet.amount() <= 0) {
				return;
			}

			ItemStack carriedStack =
					player.containerMenu.getCarried();

			if (carriedStack.isEmpty()) {
				return;
			}

			// Make sure the carried item actually matches
			// the coin type claimed by the client.
			if (!isCorrectCoin(carriedStack, packet.coinType())) {
				return;
			}

			// The client may not deposit more than it actually carries.
			if (packet.amount() > carriedStack.getCount()) {
				return;
			}

			CoinUtil.depositCoins(
					player,
					packet.coinType(),
					packet.amount()
			);

			// Only remove the amount that was actually deposited.
			carriedStack.shrink(packet.amount());

			player.containerMenu.broadcastChanges();
		});
	}

	private static boolean isCorrectCoin(ItemStack stack, int coinType) {
		return switch (coinType) {
			case 0 -> stack.is(ModItems.COPPER_COIN.get());
			case 1 -> stack.is(ModItems.IRON_COIN.get());
			case 2 -> stack.is(ModItems.GOLD_COIN.get());
			case 3 -> stack.is(ModItems.PLATINUM_COIN.get());
			default -> false;
		};
	}
}