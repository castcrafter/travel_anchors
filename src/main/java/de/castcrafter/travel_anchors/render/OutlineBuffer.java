package de.castcrafter.travel_anchors.render;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nonnull;

public class OutlineBuffer implements IRenderTypeBuffer {

    public static final OutlineBuffer INSTANCE = new OutlineBuffer();
    
    private OutlineBuffer() {
        
    }
    
    @Nonnull
    @Override
    public IVertexBuilder getBuffer(@Nonnull RenderType type) {
        return Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().getBuffer(OutlineRenderType.get(type));
    }
}
