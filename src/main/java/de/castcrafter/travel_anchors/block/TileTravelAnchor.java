package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.mod.registration.TileEntityBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;

public class TileTravelAnchor extends TileEntityBase {

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private String name = "";
    private BlockState mimic = null;

    public TileTravelAnchor(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared() {
        if (Minecraft.getInstance().player == null) {
            return super.getMaxRenderDistanceSquared();
        } else {
            double distance = TeleportHandler.getMaxDistance(Minecraft.getInstance().player);
            return distance * distance;
        }
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
            this.markDispatchable();
        }
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        this.markDirty();
        this.markDispatchable();
    }
}
