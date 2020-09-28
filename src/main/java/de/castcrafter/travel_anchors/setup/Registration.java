package de.castcrafter.travel_anchors.setup;

import de.castcrafter.travel_anchors.blocks.TravelAnchorBlock;
import de.castcrafter.travel_anchors.blocks.TravelAnchorContainer;
import de.castcrafter.travel_anchors.blocks.TravelAnchorTile;
import de.castcrafter.travel_anchors.items.TravelStaff;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.swing.*;

import static de.castcrafter.travel_anchors.TravelAnchors.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static void init(){
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<TravelAnchorBlock> TRAVEL_ANCHOR_BLOCK = BLOCKS.register("travel_anchor", TravelAnchorBlock::new);
    public static final RegistryObject<Item> TRAVEL_ANCHOR_ITEM = ITEMS.register("travel_anchor", () -> new BlockItem(TRAVEL_ANCHOR_BLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<TravelAnchorTile>> TRAVEL_ANCHOR_TILE = TILES.register("travel_anchor", () -> TileEntityType.Builder.create(TravelAnchorTile::new, TRAVEL_ANCHOR_BLOCK.get()).build(null));

    public static final RegistryObject<ContainerType<TravelAnchorContainer>> TRAVEL_ANCHOR_CONTAINER = CONTAINERS.register("travel_anchor", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new TravelAnchorContainer(windowId, world, pos, inv, inv.player);
    })));

    public static final RegistryObject<Item> TRAVEL_STAFF = ITEMS.register("travel_staff", TravelStaff::new);
}
