package de.castcrafter.travel_anchors.blocks;

import de.castcrafter.travel_anchors.setup.ModTileEntityTypes;
import net.minecraft.block.*;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;


import javax.annotation.Nullable;

public class TravelAnchorBlock extends Block {

    public TravelAnchorBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        // Always use TileEntityType#create to allow registry overrides to work.
        return ModTileEntityTypes.TRAVEL_ANCHOR.get().create();
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
    }
}
