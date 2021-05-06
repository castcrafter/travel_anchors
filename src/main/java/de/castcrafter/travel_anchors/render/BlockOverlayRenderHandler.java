package de.castcrafter.travel_anchors.render;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.block.RenderTravelAnchor;
import de.castcrafter.travel_anchors.block.TileTravelAnchor;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.settings.GraphicsFanciness;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class BlockOverlayRenderHandler{
	
	private static final Random rand = new Random();
	private RegistryKey<World> dimension;
    private IRenderTypeBuffer buffer;
    private TileTravelAnchor te;
    private float scale;
    
    private static final BlockOverlayRenderHandler INSTANCE = new BlockOverlayRenderHandler();
    
    private BlockOverlayRenderHandler(){}
    
    public void set(RegistryKey<World> dimension, IRenderTypeBuffer buffer, TileTravelAnchor te,float scale) {
    	this.dimension = dimension;
		this.buffer = buffer;
		this.te = te;
		this.scale = scale;
    }
    
    private void reset() {
    	this.dimension = null;
		this.buffer = null;
		this.te = null;
		this.scale = 0;
    }
    
    public static BlockOverlayRenderHandler getInstance() {
    	return INSTANCE;
    }
	
	public void attachEventListeners(IEventBus bus) {
        bus.addListener(EventPriority.HIGH, this::render);
    }
    
    private void render(RenderWorldLastEvent event) {
        World renderWorld = Minecraft.getInstance().world;
        PlayerEntity player = Minecraft.getInstance().player;
        if (renderWorld == null || player == null || te == null || buffer == null) {
            return;
        }
        boolean canRender = player.getPosition().distanceSq(te.getPos()) <= Math.pow(TeleportHandler.getMaxDistance(player),2);
        if (!this.dimension.equals(renderWorld.getDimensionKey())) {
            canRender = false;
        }
        if (canRender) {
        	renderTA(renderWorld, event.getMatrixStack(), player.getPositionVec(),te,scale);
        	RenderTravelAnchor.renderText(te, event.getMatrixStack());
        	reset();
        }
    }
    
    public void renderTATest(World renderWorld, MatrixStack renderStack, Vector3d playerPos, TileEntity te, float scale) {

        int[] fullBright = new int[] { 15, 15 };

        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
        IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        BufferDecoratorBuilder decorator = new BufferDecoratorBuilder()
                .setLightmapDecorator((skyLight, blockLight) -> fullBright);

        Runnable transparentSetup = () -> {
            RenderSystem.disableAlphaTest();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_SRC_COLOR, GlStateManager.DestFactor.SRC_COLOR, //ONE,SRC_COLOR,ONE,ZERO
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            
        };
        Runnable transparentClean = () -> {
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
        };

        Vector3d vec = new Vector3d(0, 0, 0);
        if (Minecraft.getInstance().gameRenderer != null) {
            vec = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        }

        renderStack.push();
        renderStack.translate(-vec.getX(), -vec.getY(), -vec.getZ());

        BlockPos at = te.getPos();
        TileEntity renderTile = te;

        BlockState renderState = te.getBlockState();

        IModelData data = renderTile != null ? renderTile.getModelData() : EmptyModelData.INSTANCE;
            
        renderStack.push();
        scale += 1;
        float offset = (1 - scale) / 2;
        renderStack.translate(at.getX() + offset, at.getY() + offset, at.getZ() + offset);
        renderStack.translate(0.5, 0.5, 0.5);
        renderStack.scale(scale, scale, scale);

        RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(RenderType.getSolid(), transparentSetup, transparentClean);
        decorator.decorate(buffers.getBuffer(decorated), buf -> {
        	if(Minecraft.getInstance().gameSettings.graphicFanciness == GraphicsFanciness.FAST) {
        		brd.renderModel(renderState, at, renderWorld, renderStack, buf, true, rand, data);
        	}else {
        		brd.renderModel(renderState, at, renderWorld, renderStack, buf, false, rand, data);
        	}
        });
        buffers.finish();

        renderStack.pop();
        renderStack.pop();
    }
    
    public void renderTA(World renderWorld, MatrixStack renderStack, Vector3d playerPos, TileEntity te, float scale) {

        int[] fullBright = new int[] { 15, 15 };

        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
        IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        BufferDecoratorBuilder decorator = new BufferDecoratorBuilder()
                .setLightmapDecorator((skyLight, blockLight) -> fullBright);

        Runnable transparentSetup = () -> {
            RenderSystem.disableAlphaTest();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_SRC_COLOR, GlStateManager.DestFactor.SRC_COLOR, //ONE,SRC_COLOR,ONE,ZERO
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            
        };
        Runnable transparentClean = () -> {
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
        };

        Vector3d vec = new Vector3d(0, 0, 0);
        if (Minecraft.getInstance().gameRenderer != null) {
            vec = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        }

        renderStack.push();
        renderStack.translate(-vec.getX(), -vec.getY(), -vec.getZ());

        BlockPos at = te.getPos();
        TileEntity renderTile = te;

        BlockState renderState = te.getBlockState();

        IModelData data = renderTile != null ? renderTile.getModelData() : EmptyModelData.INSTANCE;
            
        renderStack.push();
        float offset = (1 - scale) / 2;
        renderStack.translate(at.getX() + offset, at.getY() + offset, at.getZ() + offset);
        renderStack.scale(scale, scale, scale);

        RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(RenderType.getSolid(), transparentSetup, transparentClean);
        decorator.decorate(buffers.getBuffer(decorated), buf -> {
        	if(Minecraft.getInstance().gameSettings.graphicFanciness == GraphicsFanciness.FAST) {
        		brd.renderModel(renderState, at, renderWorld, renderStack, buf, true, rand, data);
        	}else {
        		brd.renderModel(renderState, at, renderWorld, renderStack, buf, false, rand, data);
        	}
        });
        buffers.finish();

        renderStack.pop();
        renderStack.pop();
    }

}
