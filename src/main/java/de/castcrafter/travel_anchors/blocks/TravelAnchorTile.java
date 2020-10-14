package de.castcrafter.travel_anchors.blocks;

import de.castcrafter.travel_anchors.TravelAnchorList;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;

import static de.castcrafter.travel_anchors.setup.Registration.TRAVEL_ANCHOR_TILE;

public class TravelAnchorTile extends TileEntity {

    private String name = "";

    //Mimic Stuff
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    private BlockState mimic;

    public TravelAnchorTile() {
        super(TRAVEL_ANCHOR_TILE.get());
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getMimic() {
        return mimic;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        writeMimic(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = mimic;
        CompoundNBT tag = pkt.getNbtCompound();
        handleUpdateTag(mimic, tag);
        if (!Objects.equals(oldMimic, mimic)) {
            ModelDataManager.requestModelDataRefresh(this);
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .build();
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        readMimic(nbt);
        this.name = nbt.getString("travel_anchor_name");
        if (world != null && pos != null) {
            TravelAnchorList.get(world).setAnchor(pos, name);
        }
    }
    private void readMimic(CompoundNBT tag) {
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("travel_anchor_name", this.name);
        return super.write(compound);
    }
    private void writeMimic(CompoundNBT tag) {
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        TravelAnchorList.get(world).setAnchor(pos, name);
        this.markDirty();
    }
}
