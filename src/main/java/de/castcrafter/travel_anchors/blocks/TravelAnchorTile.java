package de.castcrafter.travel_anchors.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static de.castcrafter.travel_anchors.setup.Registration.TRAVEL_ANCHOR_TILE;

public class TravelAnchorTile extends TileEntity implements ITickableTileEntity {

    public String name = "";

    public TravelAnchorTile(){
        super(TRAVEL_ANCHOR_TILE.get());
    }

    @Override
    public void tick() {
        if(this.world != null && this.world.isRemote){
            System.out.println("Name:" + name);
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        name = nbt.getString("name");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("name", name);
        return super.write(compound);
    }
}
