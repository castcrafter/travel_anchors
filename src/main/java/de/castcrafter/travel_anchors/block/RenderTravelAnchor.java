package de.castcrafter.travel_anchors.block;

import com.mojang.blaze3d.vertex.PoseStack;
import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchorList;
import de.castcrafter.travel_anchors.render.TravelAnchorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.InteractionHand;

import javax.annotation.Nonnull;

public class RenderTravelAnchor implements BlockEntityRenderer<TileTravelAnchor> {
    
    @Override
    public void render(@Nonnull TileTravelAnchor blockEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || (!TeleportHandler.canBlockTeleport(player) && !TeleportHandler.canItemTeleport(player, InteractionHand.MAIN_HAND)
                && !TeleportHandler.canItemTeleport(player, InteractionHand.OFF_HAND)) || (blockEntity.getLevel() != null && TravelAnchorList.get(blockEntity.getLevel()).getAnchor(blockEntity.getBlockPos()) == null)) {
            TravelAnchorRenderer.renderAnchor(matrixStack, buffer, null, blockEntity.getMimic(), combinedLight, false, false, 0, null);
        }
    }
}
