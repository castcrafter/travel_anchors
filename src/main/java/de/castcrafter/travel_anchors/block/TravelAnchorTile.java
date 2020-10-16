package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.TravelAnchorList;
import de.castcrafter.travel_anchors.network.Networking;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class TravelAnchorTile extends TileEntity {

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private String name = "";
    private BlockState mimic = null;

    public TravelAnchorTile() {
        super(Registration.TRAVEL_ANCHOR_TILE.get());
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putString("travel_anchor_name", this.name);
        writeMimic(nbt);
        return super.write(nbt);
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        this.name = nbt.getString("travel_anchor_name");
        readMimic(nbt);
        if (world != null && pos != null) {
            TravelAnchorList.get(world).setAnchor(world, pos, name);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("travel_anchor_name", this.name);
        writeMimic(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.name = nbt.getString("travel_anchor_name");
        readMimic(nbt);
    }

    private void writeMimic(CompoundNBT tag) {
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
    }

    private void readMimic(CompoundNBT tag) {
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        TravelAnchorList.get(world).setAnchor(world, pos, name);
        this.markDirty();
        if (world != null && pos != null && !world.isRemote) {
            Networking.updateTE(world, pos);
        }
    }

    public BlockState getMimic() {
        return mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        markDirty();
        if (world != null && pos != null && !world.isRemote) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            Networking.updateTE(world, pos);
        }
    }

    @Override
    public void onLoad() {
        if (world != null && pos != null && world.isRemote) {
            Networking.requestTE(world, pos);
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .build();
    }
}
