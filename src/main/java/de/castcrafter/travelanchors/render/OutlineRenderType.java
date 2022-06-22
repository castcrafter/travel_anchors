package de.castcrafter.travelanchors.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class OutlineRenderType extends RenderType {

    private static final Map<RenderType, OutlineRenderType> TYPES = new HashMap<>();

    private final RenderType parent;
    
    private OutlineRenderType(RenderType parent) {
        super("Outline" + parent.name, parent.format(), parent.mode(), parent.bufferSize(), parent.affectsCrumbling(), parent.sortOnUpload, parent::setupRenderState, parent::clearRenderState);
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
        if (Minecraft.getInstance().levelRenderer.entityTarget() != null) {
            //noinspection ConstantConditions
            Minecraft.getInstance().levelRenderer.entityTarget().bindWrite(false);
        }
    }

    @Override
    public void clearRenderState() {
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
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
