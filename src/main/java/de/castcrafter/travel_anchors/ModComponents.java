package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.block.BlockTravelAnchor;
import de.castcrafter.travel_anchors.block.MenuTravelAnchor;
import de.castcrafter.travel_anchors.block.TileTravelAnchor;
import de.castcrafter.travel_anchors.item.ItemTravelStaff;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.libx.base.ItemBase;
import io.github.noeppi_noeppi.libx.base.tile.BlockMenu;
import io.github.noeppi_noeppi.libx.menu.BlockEntityMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

@RegisterClass
public class ModComponents {

    public static final ItemBase travelStaff = new ItemTravelStaff(TravelAnchors.getInstance(), new Item.Properties().stacksTo(1));
    public static final BlockMenu<TileTravelAnchor, MenuTravelAnchor> travelAnchor = new BlockTravelAnchor(TravelAnchors.getInstance(), TileTravelAnchor.class, BlockEntityMenu.createMenuType(MenuTravelAnchor::new), BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.0f));
}
