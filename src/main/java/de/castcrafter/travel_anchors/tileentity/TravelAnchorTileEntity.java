package de.castcrafter.travel_anchors.tileentity;


import de.castcrafter.travel_anchors.setup.ModTileEntityTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TravelAnchorTileEntity extends TileEntity implements ITickableTileEntity{

    private ItemStackHandler handler;

    public TravelAnchorTileEntity(){
        super(ModTileEntityTypes.TRAVEL_ANCHOR.get());
    }

    @Override
    public void tick(){
    }
//
//
//
//    @Override
//    public CompoundNBT write(CompoundNBT tag) {
//        CompoundNBT compound = getHandler().serializeNBT();
//        tag.put("inv", compound);
//        return super.write(tag);
//    }
//
//    private ItemStackHandler getHandler(){
//        if (handler == null){
//            handler = new ItemStackHandler(1);
//        }
//        return handler;
//    }
//
//    @NotNull
//    @Override
//    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
//            return LazyOptional.of(() -> (T) getHandler());
//        }
//        return super.getCapability(cap, side);
//    }
}
