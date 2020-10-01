package de.castcrafter.travel_anchors.base;

import de.castcrafter.travel_anchors.util.Function4;
import de.castcrafter.travel_anchors.util.Function5;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * There are some things you need to pay attention to if you want to use this:
 * Always register player inventory slots with layoutPlayerInventorySlots
 * Register input slots, THEN output slots and THEN player inventory.
 *
 * Call the super constructor with
 *  firstOutputSlot    =  the number of input slot you have / the first output slot number
 *  firstInventorySlot =  the number of input slots and output slots you have / the first player inventory slot number.
 */
public abstract class ContainerBase<T extends TileEntity> extends Container {

    public final T tile;
    public final PlayerEntity player;
    public final IItemHandler playerInventory;
    public final BlockPos pos;
    public final World world;

    // Used for automatic transferStackInSlot. To further restrict this use Slot#isItemValid.
    public final int firstOutputSlot;
    public final int firstInventorySlot;

    protected ContainerBase(@Nullable ContainerType<?> type, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, int firstOutputSlot, int firstInventorySlot) {
        super(type, windowId);
        // This should always work. If it doesn't something is very wrong.
        //noinspection unchecked
        T tile = (T) world.getTileEntity(pos);
        if (tile == null) {
            throw new IllegalStateException("Can't create container without TileEntity.");
        }
        this.tile = tile;
        this.player = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.pos = pos;
        this.world = world;
        this.firstOutputSlot = firstOutputSlot;
        this.firstInventorySlot = firstInventorySlot;
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        //noinspection ConstantConditions
        return isWithinUsableDistance(IWorldPosCallable.of(this.tile.getWorld(), this.tile.getPos()), this.player, this.tile.getBlockState().getBlock());
    }

    @SuppressWarnings("SameParameterValue")
    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        this.addSlotBox(this.playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        this.addSlotRange(this.playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        return this.addSlotBox(handler, index, x, y, horAmount, dx, verAmount, dy, SlotItemHandler::new);
    }

    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        return this.addSlotRange(handler, index, x, y, amount, dx, SlotItemHandler::new);
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy, Function4<IItemHandler, Integer, Integer, Integer, Slot> slotFactory) {
        for (int j = 0; j < verAmount; j++) {
            index = this.addSlotRange(handler, index, x, y, horAmount, dx, slotFactory);
            y += dy;
        }
        return index;
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx, Function4<IItemHandler, Integer, Integer, Integer, Slot> slotFactory) {
        for (int i = 0; i < amount; i++) {
            this.addSlot(slotFactory.apply(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public World getWorld() {
        return this.world;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            final int inventorySize = this.firstInventorySlot;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index < this.firstOutputSlot) {
                if (!this.mergeItemStack(stack, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(stack, itemstack);
            } else if (index >= inventorySize) {
                if (!this.mergeItemStack(stack, 0, this.firstOutputSlot, false)) {
                    return ItemStack.EMPTY;
                } else if (index < playerInventoryEnd) {
                    if (!this.mergeItemStack(stack, playerInventoryEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarEnd && !this.mergeItemStack(stack, inventorySize, playerInventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return itemstack;
    }

    // As opposed to the super method this checks for Slot#isValid(ItemStack)
    @Override
    protected boolean mergeItemStack(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while(!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if (!itemstack.isEmpty() && areItemsAndTagsEqual(stack, itemstack) && slot.isItemValid(stack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while(true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();
                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.split(slot1.getSlotStackLimit()));
                    } else {
                        slot1.putStack(stack.split(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    public static <T extends  Container> ContainerType<T> createContainerType(Function5<Integer, World, BlockPos, PlayerInventory, PlayerEntity, T> constructor) {
        return IForgeContainerType.create((windowId1, inv, data) -> {
            BlockPos pos1 = data.readBlockPos();
            World world1 = inv.player.getEntityWorld();
            return constructor.apply(windowId1, world1, pos1, inv, inv.player);
        });
    }
}
