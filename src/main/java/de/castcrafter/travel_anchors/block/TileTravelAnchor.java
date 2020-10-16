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

public class TileTravelAnchor extends TileEntity {

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private String name = "";
    private BlockState mimic = null;

    public TileTravelAnchor() {
        super(Registration.TRAVEL_ANCHOR_TILE.get());
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putString("travel_anchor_name", this.name);
        this.writeMimic(nbt);
        return super.write(nbt);
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        this.name = nbt.getString("travel_anchor_name");
        this.readMimic(nbt);
        if (this.world != null && this.pos != null) {
            TravelAnchorList.get(this.world).setAnchor(this.world, this.pos, this.name);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("travel_anchor_name", this.name);
        this.writeMimic(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.name = nbt.getString("travel_anchor_name");
        this.readMimic(nbt);
    }

    private void writeMimic(CompoundNBT tag) {
        if (this.mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(this.mimic));
        }
    }

    private void readMimic(CompoundNBT tag) {
        if (tag.contains("mimic")) {
            this.mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        if (this.world != null) {
            TravelAnchorList.get(this.world).setAnchor(this.world, this.pos, name);
            this.markDirty();
            if (this.world != null && this.pos != null && !this.world.isRemote) {
                Networking.updateTE(this.world, this.pos);
            }
        }
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        this.markDirty();
        if (this.world != null && this.pos != null && !this.world.isRemote) {
            this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            Networking.updateTE(this.world, this.pos);
        }
    }

    @Override
    public void onLoad() {
        if (this.world != null && this.pos != null && this.world.isRemote) {
            Networking.requestTE(this.world, this.pos);
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, this.mimic)
                .build();
    }
}
