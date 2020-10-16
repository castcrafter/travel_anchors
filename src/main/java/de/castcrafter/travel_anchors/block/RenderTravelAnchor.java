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

        if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && !Minecraft.getInstance().player.isSneaking() && TeleportHandler.canPlayerTeleport(Minecraft.getInstance().player)) {

            Pair<BlockPos, String> anchor = TeleportHandler.getAnchorToTeleport(Minecraft.getInstance().world, Minecraft.getInstance().player, Minecraft.getInstance().player.getPosition().toImmutable().down());

            BlockPos pos = tileEntity.getPos();
            float scale = (float) (Math.sqrt(0.0006 * Minecraft.getInstance().player.getPositionVec().distanceTo(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))));
            if (scale < 0.04f) {
                scale = 0.04f;
            }
            IFormattableTextComponent tc = new StringTextComponent(tileEntity.getName());
            int color = 0xFFFFFF;
            if (anchor != null && anchor.getLeft().equals(tileEntity.getPos())) {
                scale = 1.3f * scale;
                tc = tc.mergeStyle(TextFormatting.GOLD);
                color = TextFormatting.GOLD.getColor() == null ? 0xFFFFFF : TextFormatting.GOLD.getColor();
            }

            matrixStack.push();

            matrixStack.translate(0.5, 1.05 + (scale * Minecraft.getInstance().fontRenderer.FONT_HEIGHT), 0.5);
            matrixStack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
            matrixStack.scale(-scale, -scale, scale);

            Matrix4f matrix4f = matrixStack.getLast().getMatrix();

            float textOpacitySetting = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.5f);
            int alpha = (int) (textOpacitySetting * 255.0F) << 24;
            float halfWidth = (float) (-Minecraft.getInstance().fontRenderer.getStringPropertyWidth(tc) / 2);

            Minecraft.getInstance().fontRenderer.func_238416_a_(tc, halfWidth, 0, color, false, matrix4f, buffer, true, alpha, combinedLight);
            Minecraft.getInstance().fontRenderer.func_238416_a_(tc, halfWidth, 0, color, false, matrix4f, buffer, false, 0, combinedLight);

            matrixStack.pop();
        }
    }

    /*public static void renderText(String text, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        float widthHalf = Minecraft.getInstance().fontRenderer.getStringWidth(text) / 2f;
        float heightHalf = Minecraft.getInstance().fontRenderer.FONT_HEIGHT / 2f;

        matrixStack.push();
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        matrixStack.translate(0.5, 1.5, 0.5);
        matrixStack.scale(-0.1f, -0.1f, -0.1f);
        matrixStack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //noinspection deprecation
        GlStateManager.color4f(0.8f, 0.8f, 1f, 1);
        Minecraft.getInstance().getTextureManager().bindTexture(ANCHOR);
        AbstractGui.blit(matrixStack, -8, 2, 0, 0, 16, 16, 16, 16);

        //noinspection deprecation
        GlStateManager.color4f(1, 1, 1, 1);

        Minecraft.getInstance().fontRenderer.drawString(matrixStack, text, -widthHalf, -heightHalf, 0xFFFFFF);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }*/

    public static void bakeModels(final ModelBakeEvent event) {
        ANCHOR_MODEL_BAKED = event.getModelRegistry().get(ANCHOR_MODEL);
    }
}
