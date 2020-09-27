package de.castcrafter.travel_anchors.setup;

import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.blocks.TravelAnchorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TravelAnchors.MOD_ID);

    public static final RegistryObject<Block> TRAVEL_ANCHOR = BLOCKS.register("travel_anchor", () ->
            new TravelAnchorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5F)));

}
