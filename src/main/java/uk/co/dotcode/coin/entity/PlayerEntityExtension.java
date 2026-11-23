package uk.co.dotcode.coin.entity;

public interface PlayerEntityExtension {
	public CoinData getCoinData();
	
	public void setCoinData(CoinData coinData);

	public void setAutoPickup(int value);

	public boolean isAutoPickupEnabled();
}
