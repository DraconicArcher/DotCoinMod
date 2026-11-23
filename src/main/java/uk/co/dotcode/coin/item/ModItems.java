package uk.co.dotcode.coin.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import uk.co.dotcode.coin.DCM;


@EventBusSubscriber(
		modid = DCM.MOD_ID,
		bus = EventBusSubscriber.Bus.MOD
)
public class ModItems {

	public static final DeferredRegister.Items ITEMS =
			DeferredRegister.createItems(DCM.MOD_ID);

	public static BasicCoin[] currencyOrder = new BasicCoin[4];

	public static final DeferredItem<BasicCoin> COPPER_COIN =
			ITEMS.registerItem(
					"copper_coin",
					properties -> new BasicCoin(properties, 0),
					new net.minecraft.world.item.Item.Properties()
			);

	public static final DeferredItem<BasicCoin> IRON_COIN =
			ITEMS.registerItem(
					"iron_coin",
					properties -> new BasicCoin(properties, 1),
					new net.minecraft.world.item.Item.Properties()
			);

	public static final DeferredItem<BasicCoin> GOLD_COIN =
			ITEMS.registerItem(
					"gold_coin",
					properties -> new BasicCoin(properties, 2),
					new net.minecraft.world.item.Item.Properties()
			);

	public static final DeferredItem<BasicCoin> PLATINUM_COIN =
			ITEMS.registerItem(
					"platinum_coin",
					properties -> new BasicCoin(properties, 3),
					new net.minecraft.world.item.Item.Properties()
			);

	public static final DeferredItem<Item> TOKEN =
			ITEMS.registerSimpleItem(
					"token"
			);

	public static void handleCurrencyStuff() {

		COPPER_COIN.get().setCurrencyConversions(
				IRON_COIN.get(),
				null
		);

		IRON_COIN.get().setCurrencyConversions(
				GOLD_COIN.get(),
				COPPER_COIN.get()
		);

		GOLD_COIN.get().setCurrencyConversions(
				PLATINUM_COIN.get(),
				IRON_COIN.get()
		);

		PLATINUM_COIN.get().setCurrencyConversions(
				null,
				GOLD_COIN.get()
		);

		currencyOrder[0] = COPPER_COIN.get();
		currencyOrder[1] = IRON_COIN.get();
		currencyOrder[2] = GOLD_COIN.get();
		currencyOrder[3] = PLATINUM_COIN.get();

		updateCoinConversion(
				DCM.modConfig.coinConfig.conversionRate
		);

		updateDisabledCoins(
				DCM.modConfig.coinConfig.disableCopperCoin,
				DCM.modConfig.coinConfig.disableIronCoin,
				DCM.modConfig.coinConfig.disableGoldCoin,
				DCM.modConfig.coinConfig.disablePlatinumCoin
		);
	}

	public static void updateCoinConversion(int conversionRate) {

		COPPER_COIN.get().clearTooltips();
		IRON_COIN.get().clearTooltips();
		GOLD_COIN.get().clearTooltips();
		PLATINUM_COIN.get().clearTooltips();

		COPPER_COIN.get().setConversionRate(conversionRate);
		IRON_COIN.get().setConversionRate(conversionRate);
		GOLD_COIN.get().setConversionRate(conversionRate);
		PLATINUM_COIN.get().setConversionRate(conversionRate);
	}

	public static void updateDisabledCoins(
			boolean copperDisabled,
			boolean ironDisabled,
			boolean goldDisabled,
			boolean platinumDisabled) {

		COPPER_COIN.get().isDisabled(copperDisabled);
		IRON_COIN.get().isDisabled(ironDisabled);
		GOLD_COIN.get().isDisabled(goldDisabled);
		PLATINUM_COIN.get().isDisabled(platinumDisabled);

		currencyOrder[0] = COPPER_COIN.get();
		currencyOrder[1] = IRON_COIN.get();
		currencyOrder[2] = GOLD_COIN.get();
		currencyOrder[3] = PLATINUM_COIN.get();

		COPPER_COIN.get().updateCoinConversions();
		IRON_COIN.get().updateCoinConversions();
		GOLD_COIN.get().updateCoinConversions();
		PLATINUM_COIN.get().updateCoinConversions();
	}

	public static void updateDisabledCoins(
			int copperDisabled,
			int ironDisabled,
			int goldDisabled,
			int platinumDisabled) {

		COPPER_COIN.get().isDisabled(copperDisabled);
		IRON_COIN.get().isDisabled(ironDisabled);
		GOLD_COIN.get().isDisabled(goldDisabled);
		PLATINUM_COIN.get().isDisabled(platinumDisabled);

		currencyOrder[0] = COPPER_COIN.get();
		currencyOrder[1] = IRON_COIN.get();
		currencyOrder[2] = GOLD_COIN.get();
		currencyOrder[3] = PLATINUM_COIN.get();

		COPPER_COIN.get().updateCoinConversions();
		IRON_COIN.get().updateCoinConversions();
		GOLD_COIN.get().updateCoinConversions();
		PLATINUM_COIN.get().updateCoinConversions();
	}

	@SubscribeEvent
	public static void buildCreativeContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			event.accept(COPPER_COIN);
			event.accept(IRON_COIN);
			event.accept(GOLD_COIN);
			event.accept(PLATINUM_COIN);
			event.accept(TOKEN);
		}
	}


}