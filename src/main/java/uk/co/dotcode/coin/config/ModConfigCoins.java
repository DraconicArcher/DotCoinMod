package uk.co.dotcode.coin.config;

public class ModConfigCoins {

	public boolean enableCoinDrops = true; // Should coin drops from entities be enabled? Default=true
	public boolean onlyHostileDrops = false; // Set to true if you only want hostile mobs to drop coins. Default=false
	
	public int conversionRate = 10; // How many coins are required to upgrade to the next tier? To disable coin conversion, set this as 0. Default=10
	
	public boolean disableCopperCoin = false; // Should copper coins be disabled? Default = false
	public boolean disableIronCoin = false; // Should iron coins be disabled? Default = false
	public boolean disableGoldCoin = false; // Should gold coins be disabled? Default = false
	public boolean disablePlatinumCoin = false; // Should platinum coins be disabled? Default = false

}
