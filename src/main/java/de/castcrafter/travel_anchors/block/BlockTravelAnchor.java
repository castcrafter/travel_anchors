package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.TravelAnchorList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockTravelAnchor extends Block implements ITileEntityProvider {

    private static final VoxelShape SHAPE = VoxelShapes.create(0, .001, 0, 1, 1, 1);

    public BlockTravelAnchor(Properties properties) {
        super(properties);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTravelAnchor) {
            BlockState mimic = ((TileTravelAnchor) tile).getMimic();
            if (mimic != null) {
                return mimic.getLightValue(world, pos);
            }
        }
        return super.getLightValue(state, world, pos);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        TileEntity tile = reader.getTileEntity(pos);
        if (tile instanceof TileTravelAnchor) {
            BlockState mimic = ((TileTravelAnchor) tile).getMimic();
            if (mimic != null) {
                return mimic.getShape(reader, pos, context);
            }
        }
        return SHAPE;
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flags) {
        tooltip.add(new TranslationTextComponent("tooltip.travel_anchors.travel_anchor_block"));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTravelAnchor();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return new TileTravelAnchor();
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult trace) {
        if (!world.isRemote) {
            ItemStack item = player.getHeldItem(Hand.OFF_HAND);
            if (!item.isEmpty() && item.getItem() instanceof BlockItem && player.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileTravelAnchor) {
                    BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getStateForPlacement(new BlockItemUseContext(player, Hand.OFF_HAND, item, trace));
                    if (mimicState == null || mimicState.getBlock() == this) {
                        ((TileTravelAnchor) te).setMimic(null);
                    } else {
                        ((TileTravelAnchor) te).setMimic(mimicState);
                    }
                }
                return ActionResultType.SUCCESS;
            }
            TileEntity tile = world.getTileEntity(pos);
            if (tile == null) {
                throw new IllegalStateException("Expected a tile entity of type TravelAnchorTile but got none.");
            } else if (tile instanceof TileTravelAnchor) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.travel_anchors.travel_anchor");
                    }

                    @Override
                    public Container createMenu(int window, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
                        return new ContainerTravelAnchor(window, world, pos, inventory, player);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tile.getPos());
            } else {
                throw new IllegalStateException("Expected a tile entity of type TravelAnchorTile but got " + tile.getClass() + ".");
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        super.onReplaced(state, world, pos, newState, isMoving);
        TravelAnchorList.get(world).setAnchor(world, pos, null);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}

