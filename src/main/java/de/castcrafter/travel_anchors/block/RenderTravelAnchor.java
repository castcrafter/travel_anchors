package de.castcrafter.travel_anchors.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelBakeEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class RenderTravelAnchor extends TileEntityRenderer<TileTravelAnchor> {

    public static final ResourceLocation ANCHOR_MODEL = new ResourceLocation(TravelAnchors.MODID, "block/travel_anchor");
    private static IBakedModel ANCHOR_MODEL_BAKED = null;

    public static final ResourceLocation ANCHOR = new ResourceLocation(TravelAnchors.MODID, "textures/block/travel_anchor_port.png");

    public RenderTravelAnchor(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileTravelAnchor tileEntity, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        //noinspection deprecation
        if (tileEntity.getMimic() == null || tileEntity.getMimic().getBlock() == tileEntity.getBlockState().getBlock() || tileEntity.getMimic().isAir()) {
            IVertexBuilder vertexBuffer = buffer.getBuffer(RenderTypeLookup.func_239220_a_(tileEntity.getBlockState(), false));
            //noinspection deprecation
            Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
                    .renderModelBrightnessColor(matrixStack.getLast(), vertexBuffer, tileEntity.getBlockState(),
                            ANCHOR_MODEL_BAKED, 1, 1, 1, combinedLight, combinedOverlay);
        } else {
            Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(tileEntity.getMimic(), matrixStack, buffer, combinedLight, combinedOverlay, tileEntity.getModelData());
        }

        if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && !Minecraft.getInstance().player.isSneaking()
                && (TeleportHandler.canPlayerTeleport(Minecraft.getInstance().player, Hand.MAIN_HAND) || TeleportHandler.canPlayerTeleport(Minecraft.getInstance().player, Hand.OFF_HAND))) {

            Pair<BlockPos, String> anchor = TeleportHandler.getAnchorToTeleport(Minecraft.getInstance().world, Minecraft.getInstance().player, Minecraft.getInstance().player.getPosition().toImmutable().down());

            BlockPos pos = tileEntity.getPos();
            double doubleScale = Math.sqrt(0.0004 * Minecraft.getInstance().player.getPositionVec().distanceTo(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)));
            if (doubleScale < 0.04f) {
                doubleScale = 0.04f;
            }
            doubleScale = doubleScale * (Minecraft.getInstance().gameSettings.fov / 70d);
            float scale = (float) doubleScale;

            IFormattableTextComponent tc = new StringTextComponent(tileEntity.getName());
            int color = 0xFFFFFF;
            if (anchor != null && anchor.getLeft().equals(tileEntity.getPos())) {
                doubleScale = 1.3f * doubleScale;
                tc = tc.mergeStyle(TextFormatting.GOLD);
                color = TextFormatting.GOLD.getColor() == null ? 0xFFFFFF : TextFormatting.GOLD.getColor();
            }

            matrixStack.push();

            matrixStack.translate(0.5, 1.05 + (doubleScale * Minecraft.getInstance().fontRenderer.FONT_HEIGHT), 0.5);
            matrixStack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
            matrixStack.scale(-scale, -scale, scale);

            Matrix4f matrix4f = matrixStack.getLast().getMatrix();

            float textOpacitySetting = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.5f);
            int alpha = (int) (textOpacitySetting * 255.0F) << 24;
            float halfWidth = (float) (-Minecraft.getInstance().fontRenderer.getStringPropertyWidth(tc) / 2);

            Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, buffer, true, alpha, combinedLight);
            Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, buffer, false, 0, combinedLight);

            matrixStack.pop();
        }
    }

    public static void bakeModels(final ModelBakeEvent event) {
        ANCHOR_MODEL_BAKED = event.getModelRegistry().get(ANCHOR_MODEL);
    }
}
