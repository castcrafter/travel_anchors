package de.castcrafter.travel_anchors.setup;

import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.blocks.TravelAnchorBlock;
import de.castcrafter.travel_anchors.container.TravelAnchorContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public final class ModContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, TravelAnchors.MOD_ID);

    public static final RegistryObject<ContainerType<TravelAnchorContainer>> TRAVEL_ANCHOR = CONTAINER_TYPES.register("travel_anchor", () ->
            IForgeContainerType.create(TravelAnchorContainer::new));

}
