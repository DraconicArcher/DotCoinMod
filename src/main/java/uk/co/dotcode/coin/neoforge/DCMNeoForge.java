package uk.co.dotcode.coin.neoforge;

import java.io.File;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

import net.neoforged.neoforge.common.NeoForge;
import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.commands.ModCommands;
import uk.co.dotcode.coin.config.ConfigHandler;
import uk.co.dotcode.coin.events.PlayerEvents;
import uk.co.dotcode.coin.item.ModItems;
import uk.co.dotcode.coin.packet.PacketHandler;

@Mod(DCM.MOD_ID)
public class DCMNeoForge {

	public DCMNeoForge(IEventBus modEventBus) {
		ConfigHandler.file = new File(
				FMLPaths.CONFIGDIR.get().toFile(),
				"dcm.json"
		);

		ModItems.ITEMS.register(modEventBus);



		DCM.init();
		NeoForge.EVENT_BUS.addListener(ModCommands::register);
		NeoForge.EVENT_BUS.register(PlayerEvents.class);

		EventHandler eventHandler = new EventHandler();
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(eventHandler);
	}
}