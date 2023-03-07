package de.castcrafter.travelanchors.block;

import de.castcrafter.travelanchors.ModBlocks;
import de.castcrafter.travelanchors.TravelAnchorList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.moddingx.libx.base.tile.BlockEntityBase;

import javax.annotation.Nonnull;

public class TileTravelAnchor extends BlockEntityBase {
    
    private String name = "";
    private BlockState mimic = null;
    private boolean locked = false;

    public TileTravelAnchor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("travel_anchor_name", this.name);
        nbt.putBoolean("is_locked", this.locked);
        this.writeMimic(nbt);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.name = nbt.getString("travel_anchor_name");
        this.locked = nbt.getBoolean("is_locked");
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
        nbt.putBoolean("is_locked", this.locked);
        this.writeMimic(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        this.name = nbt.getString("travel_anchor_name");
        this.locked = nbt.getBoolean("is_locked");
        this.readMimic(nbt);
    }

    private void writeMimic(CompoundTag tag) {
        tag.put("mimic", NbtUtils.writeBlockState(this.mimic == null ? ModBlocks.travelAnchor.defaultBlockState() : this.mimic));
    }

    private void readMimic(CompoundTag tag) {
        if (tag.contains("mimic") && this.level != null) {
            BlockState state = NbtUtils.readBlockState(this.level.registryAccess().lookupOrThrow(Registries.BLOCK), tag.getCompound("mimic"));
            if (state == ModBlocks.travelAnchor.defaultBlockState()) {
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

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        this.setChanged();
        this.setDispatchable();
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
