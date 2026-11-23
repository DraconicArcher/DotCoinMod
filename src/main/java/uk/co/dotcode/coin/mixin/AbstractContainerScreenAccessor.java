package uk.co.dotcode.coin.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {

    @Accessor
    int getLeftPos();

    @Accessor
    int getTopPos();

    @Accessor
    int getImageWidth();

    @Accessor
    int getImageHeight();

    @Accessor
    Slot getHoveredSlot();
}