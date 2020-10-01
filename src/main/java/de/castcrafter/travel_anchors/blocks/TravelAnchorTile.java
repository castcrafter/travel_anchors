package de.castcrafter.travel_anchors.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

import static de.castcrafter.travel_anchors.setup.Registration.TRAVEL_ANCHOR_TILE;

public class TravelAnchorTile extends TileEntity {

    private String name = "";

    public TravelAnchorTile() {
        super(TRAVEL_ANCHOR_TILE.get());
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        this.name = nbt.getString("travel_anchor_name");
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("travel_anchor_name", this.name);
        return super.write(compound);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.markDirty();
    }
}
