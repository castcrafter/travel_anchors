package de.castcrafter.travel_anchors.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class OutlineRenderType extends RenderType {

    private static final Map<RenderType, OutlineRenderType> TYPES = new HashMap<>();

    private final RenderType parent;
    
    private OutlineRenderType(RenderType parent) {
        super("Outline" + parent.name, parent.getVertexFormat(), parent.getDrawMode(), parent.getBufferSize(), parent.isUseDelegate(), parent.needsSorting, parent::setupRenderState, parent::clearRenderState);
        this.parent = parent;
    }
    
    @Nonnull
    @Override
    public String toString() {
        return "Outline" + this.parent.toString();
    }

    @Override
    public void setupRenderState() {
        this.parent.setupRenderState();
        if (Minecraft.getInstance().worldRenderer.getEntityOutlineFramebuffer() != null) {
            Minecraft.getInstance().worldRenderer.getEntityOutlineFramebuffer().bindFramebuffer(false);
        }
    }

    @Override
    public void clearRenderState() {
        Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
        this.parent.clearRenderState();
    }
    
    public static OutlineRenderType get(RenderType parent) {
        if (parent instanceof OutlineRenderType) {
            return (OutlineRenderType) parent;
        } else {
            if (!TYPES.containsKey(parent)) {
                TYPES.put(parent, new OutlineRenderType(parent));
            }
            return TYPES.get(parent);
        }
    }
}
