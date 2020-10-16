package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.base.ContainerBase;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TravelAnchorContainer extends ContainerBase<TravelAnchorTile> {

    public TravelAnchorContainer(int window, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(Registration.TRAVEL_ANCHOR_CONTAINER.get(), window, world, pos, playerInventory, player, 0, 0);
        this.layoutPlayerInventorySlots(8, 51);
    }
}
