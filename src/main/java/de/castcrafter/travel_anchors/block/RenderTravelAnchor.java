package de.castcrafter.travel_anchors.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.render.BlockOverlayRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class RenderTravelAnchor extends TileEntityRenderer<TileTravelAnchor> {

    public static final ResourceLocation ANCHOR_MODEL = new ResourceLocation(TravelAnchors.getInstance().modid, "block/travel_anchor");
    private static IBakedModel ANCHOR_MODEL_BAKED = null;
    private static IRenderTypeBuffer buffer;

    public RenderTravelAnchor(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileTravelAnchor tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer Renderbuffer, int combinedLight, int combinedOverlay) {

    	this.buffer = Renderbuffer;
        if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && !Minecraft.getInstance().player.isSneaking()
                && (TeleportHandler.canPlayerTeleport(Minecraft.getInstance().player, Hand.MAIN_HAND) || TeleportHandler.canPlayerTeleport(Minecraft.getInstance().player, Hand.OFF_HAND))) {
        	
        	Pair<BlockPos, String> anchor = TeleportHandler.getAnchorToTeleport(Minecraft.getInstance().world, Minecraft.getInstance().player, Minecraft.getInstance().player.getPosition().toImmutable().down());
            
        	BlockPos pos = tile.getPos();
        	double overlayScale = 0f;
        	if (anchor != null && anchor.getLeft().equals(tile.getPos())) {    
                PlayerEntity player = Minecraft.getInstance().player;
                overlayScale = TeleportHandler.getAngleRadians(player.getPositionVec(), pos, player.getYaw(partialTicks), player.getPitch(partialTicks));
                overlayScale = Math.toRadians(ServerConfig.MAX_ANGLE.get()) - overlayScale;
                double scaleDivisor = 4;
                overlayScale *= Math.sqrt(anchor.getLeft().distanceSq(player.getPosition())) / scaleDivisor;
                overlayScale = Math.max(1,overlayScale);
                overlayScale = Math.min(overlayScale,ServerConfig.MAX_DISTANCE.get() / scaleDivisor);
                matrixStack.push();
                displayClientStructurePreview(tile.getWorld(), tile, (float)overlayScale, matrixStack,player.getPositionVec());
                matrixStack.pop();
            }
        	renderText(tile,matrixStack);
            
        }
    }
    
    public static void renderText(TileTravelAnchor tile, MatrixStack matrixStack) {
    	
    	Pair<BlockPos, String> anchor = TeleportHandler.getAnchorToTeleport(Minecraft.getInstance().world, Minecraft.getInstance().player, Minecraft.getInstance().player.getPosition().toImmutable().down());
    	
        BlockPos pos = tile.getPos();
        double doubleScale = Math.sqrt(0.0035 * Minecraft.getInstance().player.getPositionVec().distanceTo(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)));
        if (doubleScale < 0.1f) {
            doubleScale = 0.1f;
        }
        doubleScale = doubleScale * (Math.sin(Math.toRadians(Minecraft.getInstance().gameSettings.fov / 4d)));
        float scale = (float) doubleScale;

        IFormattableTextComponent tc = new StringTextComponent(tile.getName());
        int color = 0xFFFFFF;
        if (anchor != null && anchor.getLeft().equals(tile.getPos())) {
            doubleScale = 1.3f * doubleScale;
            tc = tc.mergeStyle(TextFormatting.GOLD);
            color = TextFormatting.GOLD.getColor() == null ? 0xFFFFFF : TextFormatting.GOLD.getColor();
        }
        
        matrixStack.push();

        matrixStack.translate(0.5, 1.05 + (doubleScale * Minecraft.getInstance().fontRenderer.FONT_HEIGHT), 0.5);
        matrixStack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
        matrixStack.scale(-scale, -scale, scale);

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();

        float textOpacitySetting = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.5f); //0.5f
        int alpha = (int) (textOpacitySetting * 255.0F) << 24;
        float halfWidth = (float) (-Minecraft.getInstance().fontRenderer.getStringPropertyWidth(tc) / 2);

        Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, buffer, true, 0/*alpha*/, LightTexture.packLight(15, 15));
        //Minecraft.getInstance().fontRenderer.func_243247_a(tc, halfWidth, 0, color, false, matrix4f, buffer, false, 0, LightTexture.packLight(15, 15));
        matrixStack.pop();
    }
    
    @OnlyIn(Dist.CLIENT)
    private void displayClientStructurePreview(World world, TileTravelAnchor tile, float scale,MatrixStack renderStack, Vector3d playerPos) {
    	//BlockOverlayRenderHandler.getInstance().set(world.getDimensionKey(),buffer,tile,scale); //text unter ta rendern
    	BlockOverlayRenderHandler.getInstance().renderTATest(world, renderStack, playerPos, tile,scale); //text über ta rendern
    }
  
    public static void registerModels(final ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(RenderTravelAnchor.ANCHOR_MODEL);
    }

    public static void bakeModels(final ModelBakeEvent event) {
        ANCHOR_MODEL_BAKED = event.getModelRegistry().get(ANCHOR_MODEL);
    }
}
