package de.castcrafter.travel_anchors.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.castcrafter.travel_anchors.ModComponents;
import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.annotation.Model;
import io.github.noeppi_noeppi.libx.render.RenderHelperWorld;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LightType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.OptionalDouble;

public class TravelAnchorRenderer {

    public static final RenderType BOLD_LINES = RenderType.makeType("travel_anchors_bold_lines",
            DefaultVertexFormats.POSITION_COLOR, 1, 256, RenderType.State.getBuilder()
                    .line(new RenderState.LineState(OptionalDouble.of(3)))
                    .layer(RenderState.field_239235_M_)
                    .transparency(RenderState.TRANSLUCENT_TRANSPARENCY)
                    .target(RenderState.field_241712_U_)
                    .writeMask(RenderState.COLOR_DEPTH_WRITE)
                    .build(false)
    );


    @Model("block/travel_anchor")
    public static IBakedModel MODEL = null;
    
    public static void renderAnchors(RenderWorldLastEvent event) {
        MatrixStack matrixStack = event.getMatrixStack();
        ClientWorld world = Minecraft.getInstance().world;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (world != null && player != null) {
            if (TeleportHandler.canBlockTeleport(player) || TeleportHandler.canItemTeleport(player, Hand.MAIN_HAND)
                    || TeleportHandler.canItemTeleport(player, Hand.OFF_HAND)) {
                double maxDistanceSq = TeleportHandler.getMaxDistance(player);
                maxDistanceSq = maxDistanceSq * maxDistanceSq;
                TravelAnchorList list = TravelAnchorList.get(Minecraft.getInstance().world);
                Vector3d vec = player.getEyePosition(event.getPartialTicks());
                double posX = vec.x;
                double posYeye = vec.y;
                double posY = vec.y - player.getEyeHeight();
                double posZ = vec.z;
                Pair<BlockPos, String> pair = TeleportHandler.getAnchorToTeleport(world, player, player.getPosition().down());
                for (BlockPos pos : list.anchors.keySet()) {
                    double distanceSq = pos.distanceSq(posX, posY, posZ, true);
                    if (distanceSq <= maxDistanceSq) {
                        TravelAnchorList.Entry entry = list.getEntry(pos);
                        if (entry != null) {
                            int light;
                            //noinspection deprecation
                            if (world.isBlockLoaded(pos)) {
                                light = LightTexture.packLight(world.getLightFor(LightType.BLOCK, pos), world.getLightFor(LightType.SKY, pos));
                            } else {
                                light = LightTexture.packLight(15, 15);
                            }
                            boolean active = pair != null && pos.equals(pair.getLeft());
                            boolean directText = distanceSq <= 15 * 15;
                            matrixStack.push();
                            RenderHelperWorld.loadProjection(matrixStack, pos);
                            if (distanceSq > 10 * 10) {
                                double distance = Math.sqrt(distanceSq);
                                matrixStack.translate(0.5, 0.5, 0.5);
                                double log = Math.log(distance) / 2.3;
                                float scale = (float) (log * log * log);
                                matrixStack.scale(scale, scale, scale);
                                matrixStack.translate(-0.5, -0.5, -0.5);
                            }
                            renderAnchor(matrixStack, OutlineBuffer.INSTANCE, directText ? entry.name : null, entry.state, light, true, active, distanceSq, null);
                            matrixStack.pop();
                            if (!directText && !entry.name.trim().isEmpty()) {
                                // Blit the text at the correct location
                                matrixStack.push();

                                double blockScale = Math.sqrt(0.0035 * Math.sqrt(distanceSq));
                                if (blockScale < 0.1f) {
                                    blockScale = 0.1f;
                                }
                                blockScale = blockScale * (Math.sin(Math.toRadians(Minecraft.getInstance().gameSettings.fov / 4d)));
                                if (active) {
                                    blockScale *= 1.3;
                                }
                                
                                RenderHelperWorld.loadProjection(matrixStack, posX, posYeye, posZ);
                                CircleRotation rot = rotateCircle(posX - (pos.getX() + 0.5), posYeye - (pos.getY() + 0.5 + (0.5 * blockScale)), posZ - (pos.getZ() + 0.5));
                                rot.apply(matrixStack);
                                matrixStack.translate(0, 5, 0);
                                
                                double doubleScale = 0.1;
                                doubleScale = doubleScale * (Math.sin(Math.toRadians(Minecraft.getInstance().gameSettings.fov / 4d)));
                                if (active) {
                                    doubleScale *= 1.3;
                                }
                                float scale = (float) doubleScale;

                                rot.reverse(matrixStack);
                                matrixStack.translate(0, 0.05 + (doubleScale * Minecraft.getInstance().fontRenderer.FONT_HEIGHT), 0);
                                matrixStack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
                                matrixStack.scale(-scale, -scale, scale);

                                int color = 0xFFFFFF;
                                if (active) {
                                    color = TextFormatting.GOLD.getColor() == null ? 0xFFFFFF : TextFormatting.GOLD.getColor();
                                }

                                Matrix4f matrix4f = matrixStack.getLast().getMatrix();
                                ITextComponent tc = new StringTextComponent(entry.name.trim());

                                float textOpacitySetting = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.5f);
                                int alpha = (int) (textOpacitySetting * 255) << 24;
                                float halfWidth = (float) (-Minecraft.getInstance().fontRenderer.getStringPropertyWidth(tc) / 2);

                                Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, OutlineBuffer.INSTANCE, true, alpha, LightTexture.packLight(15, 15));
                                Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, OutlineBuffer.INSTANCE, false, 0, LightTexture.packLight(15, 15));
                                
                                matrixStack.pop();
                            }
                        }
                    }
                }
                Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
            }
        }
    }
    
    public static void renderAnchor(MatrixStack matrixStack, IRenderTypeBuffer buffer, @Nullable String name, BlockState state, int light, boolean glow, boolean active, double distanceSq, @Nullable IModelData modelData) {
        if (state == null || state.getBlock() == ModComponents.travelAnchor) {
            IVertexBuilder vertex = buffer.getBuffer(RenderType.getSolid());
            //noinspection deprecation
            Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
                    .renderModelBrightnessColor(matrixStack.getLast(), vertex, state,
                            MODEL, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
        } else {
            Minecraft.getInstance().getBlockRendererDispatcher()
                    .renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY,
                            modelData == null ? EmptyModelData.INSTANCE : modelData);
        }
        if (glow) {
            IVertexBuilder vertex = buffer.getBuffer(BOLD_LINES);
            WorldRenderer.drawBoundingBox(matrixStack, vertex, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1);
        }
        if (name != null && !name.trim().isEmpty()) {
            double doubleScale = Math.sqrt(0.0035 * Math.sqrt(distanceSq));
            if (doubleScale < 0.1f) {
                doubleScale = 0.1f;
            }
            doubleScale = doubleScale * (Math.sin(Math.toRadians(Minecraft.getInstance().gameSettings.fov / 4d)));
            if (active) {
                doubleScale *= 1.3;
            }
            float scale = (float) doubleScale;

            matrixStack.push();
            matrixStack.translate(0.5, 1.05 + (doubleScale * Minecraft.getInstance().fontRenderer.FONT_HEIGHT), 0.5);
            matrixStack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
            matrixStack.scale(-scale, -scale, scale);
            
            int color = 0xFFFFFF;
            if (active) {
                color = TextFormatting.GOLD.getColor() == null ? 0xFFFFFF : TextFormatting.GOLD.getColor();
            }

            Matrix4f matrix4f = matrixStack.getLast().getMatrix();
            ITextComponent tc = new StringTextComponent(name.trim());

            float textOpacitySetting = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.5f);
            int alpha = (int) (textOpacitySetting * 255) << 24;
            float halfWidth = (float) (-Minecraft.getInstance().fontRenderer.getStringPropertyWidth(tc) / 2);

            Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, buffer, true, alpha, LightTexture.packLight(15, 15));
            Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, buffer, false, 0, LightTexture.packLight(15, 15));
            matrixStack.pop();
        }
    }
    
    // Y ist the direction pointing directly out of the circle.
    private static CircleRotation rotateCircle(double x, double y, double z) {
        float yr = Float.NaN;
        if (z != 0) {
            yr = (float) (Math.atan2(x, z) + (Math.PI / 2));
        }
        double hor = Math.sqrt((x * x) + (z * z));
        float zr = (float) (Math.atan2(hor, y) + Math.PI);
        return new CircleRotation(yr, zr);
    }
    
    private static class CircleRotation {
        
        private final float y;
        private final float z;

        public CircleRotation(float y, float z) {
            this.y = y;
            this.z = z;
        }
        
        public void apply(MatrixStack matrixStack) {
            if (!Float.isNaN(this.y)) {
                matrixStack.rotate(Vector3f.YP.rotation(this.y));
            }
            matrixStack.rotate(Vector3f.ZP.rotation(this.z));
        }
        
        public void reverse(MatrixStack matrixStack) {
            matrixStack.rotate(Vector3f.ZP.rotation(-this.z));
            if (!Float.isNaN(this.y)) {
                matrixStack.rotate(Vector3f.YP.rotation(-this.y));
            }
        }
    }
}
