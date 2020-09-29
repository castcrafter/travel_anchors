package de.castcrafter.travel_anchors.blocks;

import de.castcrafter.travel_anchors.items.TravelStaff;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

import static de.castcrafter.travel_anchors.setup.Registration.TRAVEL_ANCHOR_TILE;

public class TravelAnchorTile extends TileEntity implements ITickableTileEntity {

    public TravelAnchorTile(){
        super(TRAVEL_ANCHOR_TILE.get());
    }

    @Override
    public void tick(){
        if(world.isRemote){
            return;
        }
        System.out.println("ticking");
    }
}
