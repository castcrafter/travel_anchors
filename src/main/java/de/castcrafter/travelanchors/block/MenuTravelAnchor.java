package de.castcrafter.travelanchors.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.moddingx.libx.menu.BlockEntityMenu;

public class MenuTravelAnchor extends BlockEntityMenu<TileTravelAnchor> {

    public MenuTravelAnchor(MenuType<? extends BlockEntityMenu<?>> type, int window, Level level, BlockPos pos, Inventory playerContainer, Player player) {
        super(type, window, level, pos, playerContainer, player, 0, 0);
        this.layoutPlayerInventorySlots(8, 51);
    }
}
