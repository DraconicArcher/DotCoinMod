package uk.co.dotcode.coin.config;

public class ModConfigDroprates {

	public boolean enableHealthDroprates = false; // Should the entity health affect the rarity of coins? Default=false

	public int minimumHealthDroprateMultiplier = 20; // The minimum amount of health required for more coins to drop. If
														// the health goes over this number then it will drop this
														// value/2 (closest to the lowest 10) coins. Default=20

	public int copper = 80; // Droprate for copper coins. Default=80
	public int iron = 10; // Droprate for iron coins. Default=10
	public int gold = 4; // Droprate for gold coins. Default=4
	public int platinum = 1; // Droprate for platinum coins. Default=1

	public int copperDropboostHealth = 20; // If the health is between 0 and this number (and healthDroprates are enabled), drop rates will not change. Default=20
	public int ironDropboostHealth = 20; // If the health is between copperCoinHealthBoost and this number (and healthDroprates are enabled), an iron coin is more likely to drop. Default=40
	public int goldDropboostHealth = 60; // If the health is between ironCoinHealthBoost and this number (and healthDroprates are enabled), a gold coin is more likely to drop. Default=60
	public int platinumDropboostHealth = 80; // If the health is greater than this number (and healthDroprates are enabled), a diamond coin is more likely to drop. Default=80

	public int lootingBonusMax = 1; // Maximum bonus affected by the looting enchantment. Default=1
	
	public boolean shouldDropRandomExtra = false; // Should the mod drop a random amount of extra coins? Default=false
	public int dropExtraLimit = 3; // If shouldDropRandomExtra=true, what is the limit for amount of coins dropped? Default=3
	
	
}
