package uk.co.dotcode.coin.config;

public class ModConfigEntities {


	public String[] whitelistedDimensions = new String[] {};
	
	

	public String[] includedEntities = new String[] { "minecraft:enderdragon", "minecraft:slime", "minecraft:shulker" };


	public String[] blacklistedEntities = new String[] {};
	
	
	public boolean isDimensionWhitelistEnabled() {
		return whitelistedDimensions.length > 0;
	}

	
	
}
