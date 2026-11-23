package uk.co.dotcode.coin.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import org.lwjgl.glfw.GLFW;

import uk.co.dotcode.coin.DCM;
import uk.co.dotcode.coin.events.GuiEvents;

@EventBusSubscriber(
        modid = DCM.MOD_ID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public class WalletKeybinds {

    public static final KeyMapping TOGGLE_WALLET = new KeyMapping(
            "key.dotcoinmod.toggle_wallet",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.categories.dotcoinmod"
    );

    @SubscribeEvent
    public static void registerKeyMappings(
            RegisterKeyMappingsEvent event) {

        event.register(TOGGLE_WALLET);
    }

    @EventBusSubscriber(
            modid = DCM.MOD_ID,
            value = Dist.CLIENT,
            bus = EventBusSubscriber.Bus.GAME
    )
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {

            while (TOGGLE_WALLET.consumeClick()) {
                GuiEvents.overlay.visible =
                        !GuiEvents.overlay.visible;
            }
        }
    }
}