package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nonnull;

public class TileTravelAnchor extends BlockEntityBase {
    
    private String name = "";
    private BlockState mimic = null;

    public TileTravelAnchor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putString("travel_anchor_name", this.name);
        this.wribeMimic(compound);
        return super.save(compound);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.name = nbt.getString("travel_anchor_name");
        this.readMimic(nbt);
        if (this.level != null) {
            TravelAnchorList.get(this.level).setAnchor(this.level, this.worldPosition, this.name, this.mimic);
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putString("travel_anchor_name", this.name);
        this.wribeMimic(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        this.name = nbt.getString("travel_anchor_name");
        this.readMimic(nbt);
    }

    private void wribeMimic(CompoundTag tag) {
        if (this.mimic != null) {
            tag.put("mimic", NbtUtils.writeBlockState(this.mimic));
        }
    }

    private void readMimic(CompoundTag tag) {
        if (tag.contains("mimic")) {
            this.mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        if (this.level != null) {
            TravelAnchorList.get(this.level).setAnchor(this.level, this.worldPosition, name, this.mimic);
            this.setChanged();
            this.markDispatchable();
        }
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        if (this.level != null) {
            TravelAnchorList.get(this.level).setAnchor(this.level, this.worldPosition, this.name, mimic);
            this.setChanged();
            this.markDispatchable();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null) {
            TravelAnchorList.get(this.level).setAnchor(this.level, this.worldPosition, this.name, this.mimic);
        }
    }
}
