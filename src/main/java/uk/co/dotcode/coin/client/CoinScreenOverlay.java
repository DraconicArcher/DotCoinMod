package uk.co.dotcode.coin.client;

import java.awt.Rectangle;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;

public class CoinScreenOverlay {

	public boolean visible = true;


	private static final ResourceLocation OVERLAY =
			ResourceLocation.fromNamespaceAndPath(
					DCM.MOD_ID,
					"textures/gui/coin_overlay.png"
			);

	public int walletTextureSizeX = 28;
	public int walletTextureSizeY = 100;

	public int textureSizeX = 100;
	public int textureSizeY = 128;

	public Rectangle copperBounds =
			new Rectangle(6, 6, 16, 16);

	public Rectangle ironBounds =
			new Rectangle(6, 24, 16, 16);

	public Rectangle goldBounds =
			new Rectangle(6, 42, 16, 16);

	public Rectangle platinumBounds =
			new Rectangle(6, 60, 16, 16);

	public Rectangle pickupBounds =
			new Rectangle(3, 84, 22, 11);

	public boolean hoverCopper = false;
	public boolean hoverIron = false;
	public boolean hoverGold = false;
	public boolean hoverPlatinum = false;
	public boolean hoverPickup = false;

	public boolean orientationIsTopBottom = false;

	public int guiPositionX;
	public int guiPositionY;

	private final Minecraft mc;

	public CoinScreenOverlay() {
		mc = Minecraft.getInstance();
	}

	public void updateCoordinates(
			WalletGuiAxis axis,
			WalletGuiPosition pos,
			int leftX,
			int topY) {

		guiPositionX = leftX;
		guiPositionY = topY;

		if (WalletGuiAxis.getPosition(
				DCM.modConfig.gui.walletGuiAxis
		) == WalletGuiAxis.TOP
				|| WalletGuiAxis.getPosition(
				DCM.modConfig.gui.walletGuiAxis
		) == WalletGuiAxis.BOTTOM) {

			copperBounds.x = leftX + 5;
			copperBounds.y = topY + 6;

			ironBounds.x = leftX + 23;
			ironBounds.y = topY + 6;

			goldBounds.x = leftX + 41;
			goldBounds.y = topY + 6;

			platinumBounds.x = leftX + 59;
			platinumBounds.y = topY + 6;

			pickupBounds.x = leftX + 75;
			pickupBounds.y = topY + 10;

			orientationIsTopBottom = true;

		} else {

			copperBounds.x = leftX + 6;
			copperBounds.y = topY + 6;

			ironBounds.x = leftX + 6;
			ironBounds.y = topY + 24;

			goldBounds.x = leftX + 6;
			goldBounds.y = topY + 42;

			platinumBounds.x = leftX + 6;
			platinumBounds.y = topY + 60;

			pickupBounds.x = leftX + 3;
			pickupBounds.y = topY + 84;

			orientationIsTopBottom = false;
		}
	}

	public void render(GuiGraphics g) {

		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.setShaderTexture(0, OVERLAY);

		int autoPickupYPosModifier;

		if (orientationIsTopBottom) {
			renderPartialTexture(
					g,
					guiPositionX,
					guiPositionY,
					0,
					100,
					walletTextureSizeY,
					walletTextureSizeX
			);

			autoPickupYPosModifier = -1;

		} else {
			renderPartialTexture(
					g,
					guiPositionX,
					guiPositionY,
					0,
					0,
					walletTextureSizeX,
					walletTextureSizeY
			);

			autoPickupYPosModifier = 0;
		}

		if (hoverCopper) {
			renderPartialTexture(
					g,
					copperBounds.x,
					copperBounds.y,
					28,
					0,
					16,
					16
			);

		} else if (hoverIron) {
			renderPartialTexture(
					g,
					ironBounds.x,
					ironBounds.y,
					28,
					0,
					16,
					16
			);

		} else if (hoverGold) {
			renderPartialTexture(
					g,
					goldBounds.x,
					goldBounds.y,
					28,
					0,
					16,
					16
			);

		} else if (hoverPlatinum) {
			renderPartialTexture(
					g,
					platinumBounds.x,
					platinumBounds.y,
					28,
					0,
					16,
					16
			);

		} else if (hoverPickup) {
			renderPartialTexture(
					g,
					pickupBounds.x,
					pickupBounds.y + autoPickupYPosModifier,
					28,
					40,
					22,
					11
			);
		}

		if (DCM.modConfig.loot.autoPickup
				&& DCM.modConfig.loot.serverAllowAutoPickup) {

			renderPartialTexture(
					g,
					pickupBounds.x + 1,
					pickupBounds.y + 1 + autoPickupYPosModifier,
					28,
					31,
					20,
					9
			);

		} else {

			renderPartialTexture(
					g,
					pickupBounds.x + 1,
					pickupBounds.y + 1 + autoPickupYPosModifier,
					28,
					22,
					20,
					9
			);
		}

		String copperAmount =
				Integer.toString(CoinUtil.copperCoinClient);

		String ironAmount =
				Integer.toString(CoinUtil.ironCoinClient);

		String goldAmount =
				Integer.toString(CoinUtil.goldCoinClient);

		String platinumAmount =
				Integer.toString(CoinUtil.platinumCoinClient);

		drawString(
				g,
				copperAmount,
				copperBounds.x + 18
						- mc.font.width(copperAmount),
				copperBounds.y + 10,
				ChatFormatting.DARK_GRAY.getColor()
		);

		drawString(
				g,
				copperAmount,
				copperBounds.x + 17
						- mc.font.width(copperAmount),
				copperBounds.y + 9,
				ChatFormatting.WHITE.getColor()
		);

		drawString(
				g,
				ironAmount,
				ironBounds.x + 18
						- mc.font.width(ironAmount),
				ironBounds.y + 10,
				ChatFormatting.DARK_GRAY.getColor()
		);

		drawString(
				g,
				ironAmount,
				ironBounds.x + 17
						- mc.font.width(ironAmount),
				ironBounds.y + 9,
				ChatFormatting.WHITE.getColor()
		);

		drawString(
				g,
				goldAmount,
				goldBounds.x + 18
						- mc.font.width(goldAmount),
				goldBounds.y + 10,
				ChatFormatting.DARK_GRAY.getColor()
		);

		drawString(
				g,
				goldAmount,
				goldBounds.x + 17
						- mc.font.width(goldAmount),
				goldBounds.y + 9,
				ChatFormatting.WHITE.getColor()
		);

		drawString(
				g,
				platinumAmount,
				platinumBounds.x + 18
						- mc.font.width(platinumAmount),
				platinumBounds.y + 10,
				ChatFormatting.DARK_GRAY.getColor()
		);

		drawString(
				g,
				platinumAmount,
				platinumBounds.x + 17
						- mc.font.width(platinumAmount),
				platinumBounds.y + 9,
				ChatFormatting.WHITE.getColor()
		);

		RenderSystem.disableBlend();
	}

	private void renderPartialTexture(
			GuiGraphics g,
			int screenX,
			int screenY,
			int imageX,
			int imageY,
			int imageWidth,
			int imageHeight) {

		g.blit(
				OVERLAY,
				screenX,
				screenY,
				imageX,
				imageY,
				imageWidth,
				imageHeight,
				textureSizeX,
				textureSizeY
		);
	}

	private void drawString(
			GuiGraphics g,
			String text,
			int x,
			int y,
			int color) {

		g.drawString(
				mc.font,
				text,
				x,
				y,
				color
		);
	}
}