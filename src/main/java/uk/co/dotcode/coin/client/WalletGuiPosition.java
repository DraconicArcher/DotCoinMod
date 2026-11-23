package uk.co.dotcode.coin.client;

public enum WalletGuiPosition {
	TOP, MIDDLE, BOTTOM;

	public static WalletGuiPosition getPosition(String value) {
		switch (value.toUpperCase()) {
		case "TOP":
			return TOP;
		case "MIDDLE":
			return MIDDLE;
		case "BOTTOM":
			return BOTTOM;
		default:
			return MIDDLE;
		}
	}
}
