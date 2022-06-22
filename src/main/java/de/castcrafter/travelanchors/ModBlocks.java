package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.block.BlockTravelAnchor;
import de.castcrafter.travelanchors.block.MenuTravelAnchor;
import de.castcrafter.travelanchors.block.TileTravelAnchor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.moddingx.libx.annotation.registration.RegisterClass;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.BlockEntityMenu;

@RegisterClass(registry = "BLOCK_REGISTRY", priority = 1)
public class ModBlocks {

    public static final MenuBlockBE<TileTravelAnchor, MenuTravelAnchor> travelAnchor = new BlockTravelAnchor(TravelAnchors.getInstance(), TileTravelAnchor.class, BlockEntityMenu.createMenuType(MenuTravelAnchor::new), BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.0f));
}
