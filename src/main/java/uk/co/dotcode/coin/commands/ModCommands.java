package uk.co.dotcode.coin.commands;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import uk.co.dotcode.coin.CoinUtil;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.entity.CoinData;
import uk.co.dotcode.coin.entity.PlayerEntityExtension;

public class ModCommands {

	private static LiteralArgumentBuilder<CommandSourceStack> currencyCommand =
			Commands.literal("currency");

	private static LiteralArgumentBuilder<CommandSourceStack> payCommand =
			Commands.literal("pay");

	public static void register(RegisterCommandsEvent event) {

		loadCommands();

		event.getDispatcher().register(currencyCommand);
		event.getDispatcher().register(payCommand);
	}

	private static void loadCommands() {

		currencyCommand.then(
				Commands.literal("get")
						.then(
								Commands.argument(
												"player",
												EntityArgument.player()
										)
										.requires(source -> source.hasPermission(4))
										.executes(command ->
												getCurrencyOther(
														command.getSource(),
														EntityArgument.getPlayer(
																command,
																"player"
														)
												)
										)
						)
						.executes(command ->
								getCurrency(command.getSource())
						)
		);

		currencyCommand.then(
				Commands.literal("set")
						.requires(source -> source.hasPermission(4))
						.then(
								Commands.argument(
												"player",
												EntityArgument.player()
										)
										.then(
												Commands.argument(
																"type",
																StringArgumentType.word()
														)
														.suggests(
																ModCommands::wordArgumentSuggestions
														)
														.then(
																Commands.argument(
																				"amount",
																				IntegerArgumentType.integer(
																						0,
																						DCM.modConfig.gui.walletMaxSlotSize
																				)
																		)
																		.executes(command ->
																				setCurrency(
																						command.getSource(),
																						EntityArgument.getPlayer(
																								command,
																								"player"
																						),
																						StringArgumentType.getString(
																								command,
																								"type"
																						),
																						IntegerArgumentType.getInteger(
																								command,
																								"amount"
																						)
																				)
																		)
														)
										)
						)
		);

		payCommand.then(
				Commands.argument(
								"player",
								EntityArgument.player()
						)
						.then(
								Commands.argument(
												"type",
												StringArgumentType.word()
										)
										.suggests(
												ModCommands::wordArgumentSuggestions
										)
										.then(
												Commands.argument(
																"amount",
																IntegerArgumentType.integer(
																		0,
																		DCM.modConfig.gui.walletMaxSlotSize
																)
														)
														.executes(command ->
																sendCurrency(
																		command.getSource(),
																		EntityArgument.getPlayer(
																				command,
																				"player"
																		),
																		StringArgumentType.getString(
																				command,
																				"type"
																		),
																		IntegerArgumentType.getInteger(
																				command,
																				"amount"
																		)
																)
														)
										)
						)
		);
	}

	private static CompletableFuture<Suggestions> wordArgumentSuggestions(
			CommandContext<CommandSourceStack> context,
			SuggestionsBuilder builder) {

		return builder
				.suggest("copper")
				.suggest("iron")
				.suggest("gold")
				.suggest("platinum")
				.buildFuture();
	}

	private static int sendCurrency(
			CommandSourceStack source,
			ServerPlayer receiver,
			String type,
			int amount) {

		if (source.getEntity() instanceof Player) {

			ServerPlayer sender =
					(ServerPlayer) source.getEntity();

			CoinData senderCoins =
					((PlayerEntityExtension) sender).getCoinData();

			int typeInt = CoinUtil.typeId(type);

			if (typeInt != -1) {

				if (senderCoins.getValue(typeInt) >= amount) {

					CoinUtil.depositCoins(
							receiver,
							typeInt,
							amount
					);

					senderCoins.setValue(
							typeInt,
							senderCoins.getValue(typeInt) - amount
					);

					senderCoins.sendChangedData(sender);

					source.sendSuccess(
							() -> Component.literal(
									"Sent " + amount + " " + type
											+ " coins to "
											+ receiver.getName().getString()
							),
							true
					);

					receiver.sendSystemMessage(
							Component.literal(
									sender.getName().getString()
											+ " sent you "
											+ amount + " "
											+ type
											+ " coins!"
							)
					);

				} else {

					source.sendFailure(
							Component.literal("Insufficient funds!")
					);
				}

			} else {

				source.sendFailure(
						Component.literal(
								"Failed to send "
										+ type
										+ " to "
										+ receiver.getName().getString()
										+ " using typeInt "
										+ typeInt
										+ "! Contact mod developer."
						)
				);

				return 0;
			}
		}

		return 1;
	}

	private static int setCurrency(
			CommandSourceStack source,
			ServerPlayer player,
			String type,
			int value) {

		if (source.getEntity() instanceof Player) {

			CoinData coins =
					((PlayerEntityExtension) player).getCoinData();

			int typeInt = CoinUtil.typeId(type);

			if (typeInt != -1) {

				coins.setValue(typeInt, value);
				coins.sendChangedData(player);

			} else {

				source.sendFailure(
						Component.literal(
								"Failed to set "
										+ type
										+ " to "
										+ value
						)
				);

				return 0;
			}

			source.sendSuccess(
					() -> Component.literal(
							"Set "
									+ type
									+ " to "
									+ value
					),
					true
			);
		}

		return 1;
	}

	private static int getCurrency(CommandSourceStack source) {

		if (source.getEntity() instanceof Player) {

			ServerPlayer player =
					(ServerPlayer) source.getEntity();

			CoinData coins =
					((PlayerEntityExtension) player).getCoinData();

			String info =
					"Copper: " + coins.getValue(0)
							+ ", Iron: " + coins.getValue(1)
							+ ", Gold: " + coins.getValue(2)
							+ ", Platinum: " + coins.getValue(3);

			source.sendSuccess(
					() -> Component.literal(info),
					false
			);
		}

		return 1;
	}

	private static int getCurrencyOther(
			CommandSourceStack source,
			ServerPlayer player) {

		CoinData coins =
				((PlayerEntityExtension) player).getCoinData();

		String info =
				player.getName().getString()
						+ "'s wallet - Copper: "
						+ coins.getValue(0)
						+ ", Iron: "
						+ coins.getValue(1)
						+ ", Gold: "
						+ coins.getValue(2)
						+ ", Platinum: "
						+ coins.getValue(3);

		source.sendSuccess(
				() -> Component.literal(info),
				false
		);

		return 1;
	}
}