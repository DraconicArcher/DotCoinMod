package uk.co.dotcode.coin.config;

public class ModConfigLoot {

	public ModConfigDroprates droprates = new ModConfigDroprates();
	
	public boolean dropCoinsOnPlayerDeath = true; // Do you want players to drop their coins on death? Default=true
	
	public boolean generateChestLoot = true; // Should the mod add coins to chest loot? Default=true
	public int chestLootLimit = 3; // What is the maximum number of coins in chest loot? Default=3
	
	public boolean lootFromSpawnerEntities = true; // Should coins drop for entities that spawn from a spawner? Default = true
	
	public boolean serverAllowAutoPickup = true; // Allows users to use the autopickup feature for wallets. This is for server control. Default=true
	public boolean autoPickup = true; // This is used by the client side to see if the player would like auto pickup for wallets. Can be changed ingame. Default=true

}
