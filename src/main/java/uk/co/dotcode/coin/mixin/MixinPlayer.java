package uk.co.dotcode.coin.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.entity.CoinData;
import uk.co.dotcode.coin.entity.PlayerEntityExtension;

@Mixin(Player.class)
public abstract class MixinPlayer implements PlayerEntityExtension {

	private CoinData coinData;
	private int autoPickup = 1;

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void injectWriteSaveData(CompoundTag tag, CallbackInfo callback) {
		if (!DCM.modConfig.gui.walletEnabled) {
			return;
		}

		if (coinData == null) {
			coinData = new CoinData();
		}

		tag.putInt("coinslot0", coinData.getValue(0));
		tag.putInt("coinslot1", coinData.getValue(1));
		tag.putInt("coinslot2", coinData.getValue(2));
		tag.putInt("coinslot3", coinData.getValue(3));
		tag.putInt("autoPickup", autoPickup);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void injectReadSaveData(CompoundTag tag, CallbackInfo callback) {
		if (!DCM.modConfig.gui.walletEnabled) {
			return;
		}

		coinData = new CoinData();

		coinData.setValue(0, tag.getInt("coinslot0"));
		coinData.setValue(1, tag.getInt("coinslot1"));
		coinData.setValue(2, tag.getInt("coinslot2"));
		coinData.setValue(3, tag.getInt("coinslot3"));

		autoPickup = tag.getInt("autoPickup");
	}

	@Inject(method = "dropEquipment", at = @At("TAIL"))
	private void dropCoins(CallbackInfo callback) {
		Player player = (Player) (Object) this;
		Level level = player.level();

		if (level.isClientSide()) {
			return;
		}

		if (!DCM.modConfig.gui.walletEnabled) {
			return;
		}

		if (!DCM.modConfig.loot.dropCoinsOnPlayerDeath) {
			return;
		}

		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		if (coinData == null) {
			return;
		}

		serverPlayer.drop(
				CoinUtil.generateCoinStack(0, coinData.getValue(0)),
				true,
				false
		);

		serverPlayer.drop(
				CoinUtil.generateCoinStack(1, coinData.getValue(1)),
				true,
				false
		);

		serverPlayer.drop(
				CoinUtil.generateCoinStack(2, coinData.getValue(2)),
				true,
				false
		);

		serverPlayer.drop(
				CoinUtil.generateCoinStack(3, coinData.getValue(3)),
				true,
				false
		);

		coinData.setAll(0, 0, 0, 0);
		coinData.sendChangedData(serverPlayer);
	}

	@Override
	public CoinData getCoinData() {
		if (coinData == null) {
			coinData = new CoinData();
			coinData.setAll(0, 0, 0, 0);
		}

		return coinData;
	}

	@Override
	public void setCoinData(CoinData coinData) {
		this.coinData = coinData;
	}

	@Override
	public void setAutoPickup(int autoPickup) {
		this.autoPickup = autoPickup;
	}

	@Override
	public boolean isAutoPickupEnabled() {
		return autoPickup == 1;
	}
}