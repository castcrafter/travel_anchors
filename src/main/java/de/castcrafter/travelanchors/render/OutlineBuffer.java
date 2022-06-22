package de.castcrafter.travelanchors.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nonnull;

public class OutlineBuffer implements MultiBufferSource {

    public static final OutlineBuffer INSTANCE = new OutlineBuffer();
    
    private OutlineBuffer() {
        
    }
    
    @Nonnull
    @Override
    public VertexConsumer getBuffer(@Nonnull RenderType type) {
        return Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(OutlineRenderType.get(type));
    }
}
