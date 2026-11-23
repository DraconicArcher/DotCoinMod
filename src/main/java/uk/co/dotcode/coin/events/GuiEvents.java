package uk.co.dotcode.coin.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.client.CoinScreenOverlay;
import uk.co.dotcode.coin.client.WalletGuiAxis;
import uk.co.dotcode.coin.client.WalletGuiPosition;
import uk.co.dotcode.coin.config.ConfigHandler;
import uk.co.dotcode.coin.item.ModItems;
import uk.co.dotcode.coin.mixin.AbstractContainerScreenAccessor;
import uk.co.dotcode.coin.packet.PacketHandler;
import uk.co.dotcode.coin.packet.SendAutoPickupPacket;
import uk.co.dotcode.coin.packet.SendCoinDepositPacket;
import uk.co.dotcode.coin.packet.SendCoinWithdrawPacket;



@EventBusSubscriber(
		modid = DCM.MOD_ID,
		value = Dist.CLIENT,
		bus = EventBusSubscriber.Bus.GAME
)
public class GuiEvents {



	public static CoinScreenOverlay overlay = new CoinScreenOverlay();

	@SubscribeEvent
	public static void onMouseButtonPressed(
			ScreenEvent.MouseButtonPressed.Pre event) {

		if (DCM.modConfig == null
				|| !DCM.modConfig.gui.walletEnabled
				|| overlay == null) {
			return;
		}

		Screen screen = event.getScreen();

		if (screen instanceof CreativeModeInventoryScreen) {
			return;
		}

		if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		int button = event.getButton();

		ItemStack carriedStack =
				containerScreen.getMenu().getCarried();

		if (overlay.hoverCopper) {
			if (!carriedStack.isEmpty()) {
				if (carriedStack.getItem() == ModItems.COPPER_COIN.get()) {
					PacketHandler.sendToServer(
							new SendCoinDepositPacket(0, carriedStack.getCount())
					);

					event.setCanceled(true);
					return;
				}
			} else {
				if (button == 0) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(
									0,
									Screen.hasShiftDown() ? 64 : 32
							)
					);

					event.setCanceled(true);
					return;
				} else if (button == 1) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(0, 1)
					);

					event.setCanceled(true);
					return;
				}
			}
		}

		else if (overlay.hoverIron) {
			if (!carriedStack.isEmpty()) {
				if (carriedStack.getItem() == ModItems.IRON_COIN.get()) {
					PacketHandler.sendToServer(
							new SendCoinDepositPacket(1, carriedStack.getCount())
					);

					event.setCanceled(true);
					return;
				}
			} else {
				if (button == 0) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(
									1,
									Screen.hasShiftDown() ? 64 : 32
							)
					);

					event.setCanceled(true);
					return;
				} else if (button == 1) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(1, 1)
					);

					event.setCanceled(true);
					return;
				}
			}
		}

		else if (overlay.hoverGold) {
			if (!carriedStack.isEmpty()) {
				if (carriedStack.getItem() == ModItems.GOLD_COIN.get()) {
					PacketHandler.sendToServer(
							new SendCoinDepositPacket(2, carriedStack.getCount())
					);



					event.setCanceled(true);
					return;
				}
			} else {
				if (button == 0) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(
									2,
									Screen.hasShiftDown() ? 64 : 32
							)
					);

					event.setCanceled(true);
					return;
				} else if (button == 1) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(2, 1)
					);

					event.setCanceled(true);
					return;
				}
			}
		}

		else if (overlay.hoverPlatinum) {
			if (!carriedStack.isEmpty()) {
				if (carriedStack.getItem() == ModItems.PLATINUM_COIN.get()) {
					PacketHandler.sendToServer(
							new SendCoinDepositPacket(3, carriedStack.getCount())
					);

					event.setCanceled(true);
					return;


				}
			} else {
				if (button == 0) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(
									3,
									Screen.hasShiftDown() ? 64 : 32
							)
					);

					event.setCanceled(true);
					return;
				} else if (button == 1) {
					PacketHandler.sendToServer(
							new SendCoinWithdrawPacket(3, 1)
					);

					event.setCanceled(true);
					return;
				}
			}
		}

		else if (overlay.hoverPickup) {
			if (DCM.modConfig.loot.serverAllowAutoPickup
					&& button == 0) {

				DCM.modConfig.loot.autoPickup =
						!DCM.modConfig.loot.autoPickup;

				PacketHandler.sendToServer(
						new SendAutoPickupPacket(
								DCM.modConfig.loot.autoPickup
						)
				);

				ConfigHandler.save();

				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onScreenRender(ScreenEvent.Render.Post event) {
		if (DCM.modConfig == null
				|| !DCM.modConfig.gui.walletEnabled
				|| overlay == null
				|| !overlay.visible) {
			return;
		}

		Screen screen = event.getScreen();

		if (screen instanceof CreativeModeInventoryScreen) {
			return;
		}

		if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) {
			return;
		}

		int mouseX = event.getMouseX();
		int mouseY = event.getMouseY();

		overlay.hoverCopper =
				overlay.copperBounds.contains(mouseX, mouseY);

		overlay.hoverIron =
				overlay.ironBounds.contains(mouseX, mouseY);

		overlay.hoverGold =
				overlay.goldBounds.contains(mouseX, mouseY);

		overlay.hoverPlatinum =
				overlay.platinumBounds.contains(mouseX, mouseY);

		overlay.hoverPickup =
				overlay.pickupBounds.contains(mouseX, mouseY);

		int guiCoordLeft =
				((AbstractContainerScreenAccessor) containerScreen)
						.getLeftPos();

		int guiCoordTop =
				((AbstractContainerScreenAccessor) containerScreen)
						.getTopPos();

		int guiWidth =
				((AbstractContainerScreenAccessor) containerScreen)
						.getImageWidth();

		int guiHeight =
				((AbstractContainerScreenAccessor) containerScreen)
						.getImageHeight();

		WalletGuiAxis axis =
				WalletGuiAxis.getPosition(
						DCM.modConfig.gui.walletGuiAxis
				);

		WalletGuiPosition pos =
				WalletGuiPosition.getPosition(
						DCM.modConfig.gui.walletGuiPos
				);

		switch (axis) {
			case LEFT:
				switch (pos) {
					case TOP:
						guiCoordLeft -= overlay.walletTextureSizeX;
						break;

					case MIDDLE:
						guiCoordLeft -= overlay.walletTextureSizeX;
						guiCoordTop +=
								(guiHeight / 2)
										- (overlay.walletTextureSizeY / 2);
						break;

					case BOTTOM:
						guiCoordLeft -= overlay.walletTextureSizeX;
						guiCoordTop +=
								guiHeight - overlay.walletTextureSizeY;
						break;
				}
				break;

			case RIGHT:
				switch (pos) {
					case TOP:
						guiCoordLeft += guiWidth;
						break;

					case MIDDLE:
						guiCoordLeft += guiWidth;
						guiCoordTop +=
								(guiHeight / 2)
										- (overlay.walletTextureSizeY / 2);
						break;

					case BOTTOM:
						guiCoordLeft += guiWidth;
						guiCoordTop +=
								guiHeight - overlay.walletTextureSizeY;
						break;
				}
				break;

			case TOP:
				switch (pos) {
					case TOP:
						guiCoordTop -= overlay.walletTextureSizeX;
						break;

					case MIDDLE:
						guiCoordLeft +=
								(guiWidth / 2)
										- (overlay.walletTextureSizeY / 2);
						guiCoordTop -= overlay.walletTextureSizeX;
						break;

					case BOTTOM:
						guiCoordLeft +=
								guiWidth - overlay.walletTextureSizeY;
						guiCoordTop -= overlay.walletTextureSizeX;
						break;
				}
				break;

			case BOTTOM:
				switch (pos) {
					case TOP:
						guiCoordTop += guiHeight;
						break;

					case MIDDLE:
						guiCoordLeft +=
								(guiWidth / 2)
										- (overlay.walletTextureSizeY / 2);
						guiCoordTop += guiHeight;
						break;

					case BOTTOM:
						guiCoordLeft +=
								guiWidth - overlay.walletTextureSizeY;
						guiCoordTop += guiHeight;
						break;
				}
				break;
		}

		overlay.updateCoordinates(
				axis,
				pos,
				guiCoordLeft,
				guiCoordTop
		);

		overlay.render(event.getGuiGraphics());
	}
}