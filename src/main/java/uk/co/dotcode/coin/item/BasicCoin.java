package uk.co.dotcode.coin.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import uk.co.dotcode.coin.DCM;

public class BasicCoin extends Item {

	private ArrayList<Component> tooltipNameList = new ArrayList<>();

	private Item superiorCurrency;
	private Item inferiorCurrency;

	private int conversionRate;
	private int currencyId;

	public boolean isDisabled = false;

	public BasicCoin(Item.Properties properties, int currencyId) {
		super(properties);

		this.currencyId = currencyId;
		this.conversionRate =
				DCM.modConfig.coinConfig.conversionRate;
	}

	@Override
	public void appendHoverText(
			ItemStack stack,
			Item.TooltipContext context,
			List<Component> tooltip,
			TooltipFlag flag) {

		super.appendHoverText(stack, context, tooltip, flag);

		for (Component component : tooltipNameList) {
			tooltip.add(component);
		}

		if (currencyId == 0) {
			tooltip.add(
					Component.literal("The cheapest form of currency")
							.withStyle(ChatFormatting.AQUA)
			);
		} else if (currencyId == 3) {
			tooltip.add(
					Component.literal("Wow! You're rich!")
							.withStyle(ChatFormatting.AQUA)
			);
		}

		if (conversionRate > 0) {

			if (!isDisabled) {

				if (superiorCurrency != null) {
					tooltip.add(
							Component.literal(
									"Right click to convert to "
											+ superiorCurrency
											.getName(
													new ItemStack(superiorCurrency)
											)
											.getString()
							).withStyle(ChatFormatting.GRAY)
					);
				}

				if (inferiorCurrency != null) {
					tooltip.add(
							Component.literal(
									"Shift right click to convert to "
											+ inferiorCurrency
											.getName(
													new ItemStack(inferiorCurrency)
											)
											.getString()
							).withStyle(ChatFormatting.GRAY)
					);
				}

			} else {

				tooltip.add(
						Component.literal(
								"This coin has been disabled on this server"
						).withStyle(ChatFormatting.RED)
				);
			}

		} else {

			tooltip.add(
					Component.literal(
							"Coin conversion is disabled on this server."
					).withStyle(ChatFormatting.GRAY)
			);
		}
	}

	public void setTooltip(Component text) {
		tooltipNameList.add(text);
	}

	public void clearTooltips() {
		tooltipNameList = new ArrayList<>();
	}

	public void setConversionRate(int conversionRate) {
		this.conversionRate = conversionRate;
	}

	public void isDisabled(int value) {
		isDisabled(value == 1);
	}

	public void isDisabled(boolean value) {
		this.isDisabled = value;
	}

	public void updateCoinConversions() {

		inferiorCurrency = null;
		superiorCurrency = null;

		for (int i = currencyId + 1; i < 4; i++) {
			if (!ModItems.currencyOrder[i].isDisabled) {
				superiorCurrency = ModItems.currencyOrder[i];
				break;
			}
		}

		for (int i = currencyId - 1; i > -1; i--) {
			if (!ModItems.currencyOrder[i].isDisabled) {
				inferiorCurrency = ModItems.currencyOrder[i];
				break;
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(
			Level world,
			Player player,
			InteractionHand hand) {

		if (!world.isClientSide) {

			if (conversionRate > 0) {

				if (player.isShiftKeyDown()) {

					if (inferiorCurrency != null) {

						ItemStack currentItemStack =
								player.getItemInHand(hand);

						if (currentItemStack.getCount() >= 1) {

							currentItemStack.shrink(1);

							ItemStack newStack =
									new ItemStack(inferiorCurrency);

							newStack.setCount(conversionRate);

							player.getInventory()
									.placeItemBackInInventory(newStack);

							return InteractionResultHolder.sidedSuccess(
									player.getItemInHand(hand),
									world.isClientSide
							);
						}
					}

				} else {

					if (superiorCurrency != null) {

						ItemStack currentItemStack =
								player.getItemInHand(hand);

						if (currentItemStack.getCount() >= conversionRate) {

							currentItemStack.shrink(conversionRate);

							ItemStack newStack =
									new ItemStack(superiorCurrency);

							newStack.setCount(1);

							player.getInventory()
									.placeItemBackInInventory(newStack);

							return InteractionResultHolder.sidedSuccess(
									player.getItemInHand(hand),
									world.isClientSide
							);

						} else {

							player.displayClientMessage(
									Component.literal(
											"You need "
													+ conversionRate
													+ " coins to convert this!"
									),
									true
							);
						}
					}
				}

			} else {

				player.displayClientMessage(
						Component.literal(
								"Coin conversion is disabled on this server."
						),
						true
				);
			}
		}

		return super.use(world, player, hand);
	}

	public void setCurrencyConversions(
			Item superiorCurrency,
			Item inferiorCurrency) {

		this.superiorCurrency = superiorCurrency;
		this.inferiorCurrency = inferiorCurrency;
	}
}