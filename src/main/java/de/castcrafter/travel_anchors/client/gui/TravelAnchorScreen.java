package de.castcrafter.travel_anchors.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.castcrafter.travel_anchors.container.TravelAnchorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class TravelAnchorScreen extends ContainerScreen<TravelAnchorContainer> {

    public TravelAnchorScreen(TravelAnchorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

    }
}
