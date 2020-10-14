package de.castcrafter.travel_anchors.blocks.mimic;

import de.castcrafter.travel_anchors.blocks.TravelAnchorTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.lighting.ILightListener;

import javax.annotation.Nullable;

public class Color implements IBlockColor {

    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tint) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TravelAnchorTile) {
            TravelAnchorTile fancy = (TravelAnchorTile) te;
            BlockState mimic = fancy.getMimic();
            if (mimic != null) {
                return Minecraft.getInstance().getBlockColors().getColor(mimic, world, pos, tint);
            }
        }

        return -1;
    }
}
