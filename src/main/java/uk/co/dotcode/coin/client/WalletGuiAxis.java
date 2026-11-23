package uk.co.dotcode.coin.client;

public enum WalletGuiAxis {
	LEFT, TOP, RIGHT, BOTTOM;

	public static WalletGuiAxis getPosition(String value) {
		switch (value.toUpperCase()) {
		case "TOP":
			return TOP;
		case "LEFT":
			return LEFT;
		case "BOTTOM":
			return BOTTOM;
		case "RIGHT":
			return RIGHT;
		default:
			return LEFT;
		}
	}
}
