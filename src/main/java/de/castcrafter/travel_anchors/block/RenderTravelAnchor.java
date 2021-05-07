package de.castcrafter.travel_anchors.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchorList;
import de.castcrafter.travel_anchors.render.TravelAnchorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class RenderTravelAnchor extends TileEntityRenderer<TileTravelAnchor> {
    
    public RenderTravelAnchor(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull TileTravelAnchor tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null || (!TeleportHandler.canBlockTeleport(player) && !TeleportHandler.canItemTeleport(player, Hand.MAIN_HAND)
                && !TeleportHandler.canItemTeleport(player, Hand.OFF_HAND)) || (tile.getWorld() != null && TravelAnchorList.get(tile.getWorld()).getAnchor(tile.getPos()) == null)) {
            TravelAnchorRenderer.renderAnchor(matrixStack, buffer, null, tile.getMimic(), combinedLight, false, false, 0, null);
        }
    }
}
