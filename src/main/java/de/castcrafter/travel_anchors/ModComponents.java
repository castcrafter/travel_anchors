package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.block.BlockTravelAnchor;
import de.castcrafter.travel_anchors.block.ContainerTravelAnchor;
import de.castcrafter.travel_anchors.block.TileTravelAnchor;
import de.castcrafter.travel_anchors.item.ItemTravelStaff;
import io.github.noeppi_noeppi.libx.inventory.container.ContainerBase;
import io.github.noeppi_noeppi.libx.mod.registration.BlockGUI;
import io.github.noeppi_noeppi.libx.mod.registration.ItemBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class ModComponents {

    public static final BlockGUI<TileTravelAnchor, ContainerTravelAnchor> travelAnchor = new BlockTravelAnchor(TravelAnchors.getInstance(), TileTravelAnchor.class, ContainerBase.createContainerType(ContainerTravelAnchor::new), AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f));

    public static final ItemBase travelStaff = new ItemTravelStaff(TravelAnchors.getInstance(), new Item.Properties().maxStackSize(1));

    public static void register() {
        TravelAnchors.getInstance().register("travel_anchor", travelAnchor);
        TravelAnchors.getInstance().register("travel_staff", travelStaff);
    }
}
