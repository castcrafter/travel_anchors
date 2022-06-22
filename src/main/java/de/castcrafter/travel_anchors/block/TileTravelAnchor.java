package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.ModComponents;
import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TileTravelAnchor extends BlockEntityBase {
    
    private String name = "";
    private BlockState mimic = null;

    public TileTravelAnchor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("travel_anchor_name", this.name);
        this.writeMimic(nbt);
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
        this.writeMimic(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        this.name = nbt.getString("travel_anchor_name");
        this.readMimic(nbt);
    }

    private void writeMimic(CompoundTag tag) {
        tag.put("mimic", NbtUtils.writeBlockState(this.mimic == null ? ModComponents.travelAnchor.defaultBlockState() : this.mimic));
    }

    private void readMimic(CompoundTag tag) {
        if (tag.contains("mimic")) {
            BlockState state = NbtUtils.readBlockState(tag.getCompound("mimic"));
            if (state == ModComponents.travelAnchor.defaultBlockState()) {
                this.mimic = null;
            } else {
                this.mimic = state;
            }
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
            this.setDispatchable();
        }
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        if (this.level != null) {
            TravelAnchorList.get(this.level).setAnchor(this.level, this.worldPosition, this.name, mimic);
        }
        this.setChanged();
        this.setDispatchable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null) {
            TravelAnchorList.get(this.level).setAnchor(this.level, this.worldPosition, this.name, this.mimic);
        }
    }
}
