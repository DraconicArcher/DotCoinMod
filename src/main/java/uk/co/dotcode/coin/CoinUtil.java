package uk.co.dotcode.coin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import uk.co.dotcode.coin.entity.CoinData;
import uk.co.dotcode.coin.entity.PlayerEntityExtension;
import uk.co.dotcode.coin.item.ModItems;

public class CoinUtil {

	public static int copperCoinClient = 0;
	public static int ironCoinClient = 0;
	public static int goldCoinClient = 0;
	public static int platinumCoinClient = 0;

	public static void withdrawCoins(Player player, int slot, int amount) {
		if (slot < 0 || slot > 3 || amount <= 0) {
			return;
		}

		CoinData coins = ((PlayerEntityExtension) player).getCoinData();

		if (coins == null) {
			coins = new CoinData();
			coins.setAll(0, 0, 0, 0);
			((PlayerEntityExtension) player).setCoinData(coins);
		}

		int currentBalance = coins.getValue(slot);

		if (currentBalance < 1) {
			return;
		}

		if (currentBalance < amount) {
			ItemStack stack = generateCoinStack(slot, currentBalance);

			if (!stack.isEmpty()) {
				player.getInventory().placeItemBackInInventory(stack);
			}

			coins.setValue(slot, 0);
		} else {
			ItemStack stack = generateCoinStack(slot, amount);

			if (!stack.isEmpty()) {
				player.getInventory().placeItemBackInInventory(stack);
			}

			coins.setValue(slot, currentBalance - amount);
		}

		coins.sendChangedData(player);
		player.containerMenu.broadcastChanges();
	}

	public static void depositCoins(Player player, int slot, int amount) {
		// Reject invalid requests.
		if (slot < 0 || slot > 3 || amount <= 0) {
			return;
		}

		CoinData coins = ((PlayerEntityExtension) player).getCoinData();

		if (coins == null) {
			coins = new CoinData();
			coins.setAll(0, 0, 0, 0);
			((PlayerEntityExtension) player).setCoinData(coins);
		}

		ItemStack remainder = coins.addToTotal(slot, amount);

		if (!remainder.isEmpty()) {
			player.getInventory().placeItemBackInInventory(remainder);
		}

		coins.sendChangedData(player);
		player.containerMenu.broadcastChanges();
	}

	public static ItemStack generateCoinStack(int slot, int amount) {
		if (amount <= 0) {
			return ItemStack.EMPTY;
		}

		return switch (slot) {
			case 0 -> new ItemStack(ModItems.COPPER_COIN.get(), amount);
			case 1 -> new ItemStack(ModItems.IRON_COIN.get(), amount);
			case 2 -> new ItemStack(ModItems.GOLD_COIN.get(), amount);
			case 3 -> new ItemStack(ModItems.PLATINUM_COIN.get(), amount);
			default -> ItemStack.EMPTY;
		};
	}

	public static int typeId(String type) {
		if (type.equalsIgnoreCase("copper")) {
			return 0;
		} else if (type.equalsIgnoreCase("iron")) {
			return 1;
		} else if (type.equalsIgnoreCase("gold")) {
			return 2;
		} else if (type.equalsIgnoreCase("platinum")) {
			return 3;
		} else {
			return -1;
		}
	}

	public static boolean isIncluded(String entityKey) {
		if (entityKey == null) {
			return false;
		}

		for (String includedEntity : DCM.modConfig.entities.includedEntities) {
			if (entityKey.equalsIgnoreCase(includedEntity)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isEntityBlacklisted(String entityKey) {
		if (entityKey == null) {
			return false;
		}

		for (String blacklistedEntity : DCM.modConfig.entities.blacklistedEntities) {
			if (entityKey.equalsIgnoreCase(blacklistedEntity)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isCurrentDimensionAllowed(String dimensionKey) {
		if (DCM.modConfig.entities.isDimensionWhitelistEnabled()) {
			for (String whitelistedDimension :
					DCM.modConfig.entities.whitelistedDimensions) {

				if (dimensionKey.equalsIgnoreCase(whitelistedDimension)) {
					return true;
				}
			}

			return false;
		}

		return true;
	}

	public static float dropLevelFromHealth(float health) {
		if (health < DCM.modConfig.loot.droprates.minimumHealthDroprateMultiplier) {
			return 1;
		}

		return (float) Math.ceil(
				health
						/ DCM.modConfig.loot.droprates.minimumHealthDroprateMultiplier
						/ 2
		);
	}
}