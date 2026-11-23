package uk.co.dotcode.coin.config;

public class ModConfigGUI {

	public boolean walletEnabled = true; // Do you want to enable the wallet gui in the inventory screens? Default=true
	public String walletGuiPos = "MIDDLE"; // Where should wallet GUI appear? Possible values: top, middle, bottom
	public String walletGuiAxis = "LEFT"; // On what side of the inventory should the wallet GUI appear? Possible values: left, right, top, bottom
	
	public int walletMaxSlotSize = 999; // The maximum size for the wallet (the gui that appears alongisde other GUIs). Strongly recommended that you keep this below 1000. Default=999
}
