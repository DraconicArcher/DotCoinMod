package uk.co.dotcode.coin.mixin;

import java.util.Random;

import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.item.ModItems;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

	@Inject(method = "die", at = @At("HEAD"))
	public void onDeath(DamageSource source, CallbackInfo callback) {

		LivingEntity entity = getInstance();
		Level level = entity.level();

		if (!level.isClientSide()) {

			boolean shouldDropCoins = false;

			if (DCM.modConfig.coinConfig.enableCoinDrops) {

				if (CoinUtil.isCurrentDimensionAllowed(
						level.dimension().location().toString())) {

					String entityName = entity.getEncodeId();

					if (!CoinUtil.isEntityBlacklisted(entityName)) {

						if (!CoinUtil.isIncluded(entityName)) {

							if ((!DCM.modConfig.loot.lootFromSpawnerEntities
									&& !entity.getPersistentData().contains("spawnedFromSpawner"))
									|| DCM.modConfig.loot.lootFromSpawnerEntities) {

								if (DCM.modConfig.coinConfig.onlyHostileDrops) {

									if (entity.getType().getCategory() == MobCategory.MONSTER) {
										shouldDropCoins = true;
									}

								} else {
									shouldDropCoins = true;
								}
							}

						} else {
							shouldDropCoins = true;
						}
					}
				}
			}

			if (shouldDropCoins) {
				dropCoins(source);
			}
		}
	}

	private void dropCoins(DamageSource source) {

		LivingEntity entity = getInstance();

		float dropLevel =
				CoinUtil.dropLevelFromHealth(entity.getMaxHealth());

		int dropMod =
				(int) Math.ceil(entity.getMaxHealth() / 10);

		int copperWeight =
				DCM.modConfig.loot.droprates.copper;

		int ironWeight =
				DCM.modConfig.loot.droprates.iron;

		int goldWeight =
				DCM.modConfig.loot.droprates.gold;

		int platinumWeight =
				DCM.modConfig.loot.droprates.platinum;

		if (DCM.modConfig.loot.droprates.enableHealthDroprates) {

			if (entity.getMaxHealth()
					<= DCM.modConfig.loot.droprates.copperDropboostHealth) {

				// Do nothing

			} else if (entity.getMaxHealth()
					> DCM.modConfig.loot.droprates.copperDropboostHealth
					|| entity.getMaxHealth()
					<= DCM.modConfig.loot.droprates.ironDropboostHealth) {

				copperWeight -= dropMod;
				ironWeight += dropMod;

			} else if (entity.getMaxHealth()
					> DCM.modConfig.loot.droprates.ironDropboostHealth
					|| entity.getMaxHealth()
					<= DCM.modConfig.loot.droprates.goldDropboostHealth) {

				copperWeight -= dropMod;
				ironWeight -= dropMod;
				goldWeight += dropMod;

			} else if (entity.getMaxHealth()
					> DCM.modConfig.loot.droprates.platinumDropboostHealth) {

				copperWeight -= dropMod;
				ironWeight -= dropMod;
				goldWeight -= dropMod;
				platinumWeight += dropMod;
			}
		}

		if (source != null) {

			Level level = entity.level();

			int lootingEnchant =
					level.getRandom().nextInt(
							DCM.modConfig.loot.droprates.lootingBonusMax + 1
					) + 1;

			LootPool.Builder pool =
					LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1))
							.when(LootItemKilledByPlayerCondition.killedByPlayer());

			if (!ModItems.COPPER_COIN.get().isDisabled) {
				pool.add(
						LootItem.lootTableItem(ModItems.COPPER_COIN.get())
								.setWeight(copperWeight)
				);
			}

			if (!ModItems.IRON_COIN.get().isDisabled) {
				pool.add(
						LootItem.lootTableItem(ModItems.IRON_COIN.get())
								.setWeight(ironWeight)
				);
			}

			if (!ModItems.GOLD_COIN.get().isDisabled) {
				pool.add(
						LootItem.lootTableItem(ModItems.GOLD_COIN.get())
								.setWeight(goldWeight)
				);
			}

			if (!ModItems.PLATINUM_COIN.get().isDisabled) {
				pool.add(
						LootItem.lootTableItem(ModItems.PLATINUM_COIN.get())
								.setWeight(platinumWeight)
				);
			}

			pool.apply(
					EnchantedCountIncreaseFunction.lootingMultiplier(
							((ServerLevel) entity.level()).registryAccess(),
							ConstantValue.exactly(1)
					).setLimit(lootingEnchant)
			);

			if (DCM.modConfig.loot.droprates.shouldDropRandomExtra) {

				int randValue =
						new Random().nextInt(
								DCM.modConfig.loot.droprates.dropExtraLimit
						) + 1;

				pool.setRolls(ConstantValue.exactly(randValue));
			}

			LootTable lootTable =
					LootTable.lootTable()
							.withPool(pool)
							.build();

			LootParams.Builder lootParamsBuilder =
					new LootParams.Builder((ServerLevel) entity.level());

			lootParamsBuilder
					.withParameter(
							LootContextParams.THIS_ENTITY,
							entity
					)
					.withParameter(
							LootContextParams.ORIGIN,
							entity.position()
					)
					.withParameter(
							LootContextParams.DAMAGE_SOURCE,
							source
					)
					.withOptionalParameter(
							LootContextParams.ATTACKING_ENTITY,
							source.getEntity()
					)
					.withOptionalParameter(
							LootContextParams.DIRECT_ATTACKING_ENTITY,
							source.getDirectEntity()
					);

			if (source.getEntity() instanceof Player player) {

				lootParamsBuilder
						.withParameter(
								LootContextParams.LAST_DAMAGE_PLAYER,
								player
						)
						.withLuck(player.getLuck());
			}

			for (int i = 0; i < dropLevel; i++) {

				lootTable.getRandomItems(
						lootParamsBuilder.create(
								LootContextParamSets.ENTITY
						)
				).forEach(entity::spawnAtLocation);
			}
		}
	}

	private LivingEntity getInstance() {
		return (LivingEntity) (Object) this;
	}
}