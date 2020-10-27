package de.castcrafter.travel_anchors.block;

import io.github.noeppi_noeppi.libx.inventory.container.ContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerTravelAnchor extends ContainerBase<TileTravelAnchor> {

    public ContainerTravelAnchor(ContainerType<?> type, int window, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(type, window, world, pos, playerInventory, player, 0, 0);
        this.layoutPlayerInventorySlots(8, 51);
    }
}
