package de.castcrafter.travel_anchors.blocks;

import de.castcrafter.travel_anchors.TravelAnchorList;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
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
public class TravelAnchorBlock extends Block implements ITileEntityProvider {

    private static final VoxelShape SHAPE = VoxelShapes.create(.2, .2, .2, .8, .8, .8);

    public TravelAnchorBlock(Properties properties) {
        super(properties);
    }


    //Mimic Stuff
    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TravelAnchorTile) {
            BlockState mimic = ((TravelAnchorTile) te).getMimic();
            if (mimic != null) {
                return mimic.getLightValue(world, pos);
            }
        }
        return super.getLightValue(state, world, pos);
    }
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        TileEntity te = reader.getTileEntity(pos);
        if (te instanceof TravelAnchorTile) {
            BlockState mimic = ((TravelAnchorTile) te).getMimic();
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
        return new TravelAnchorTile();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return new TravelAnchorTile();
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult trace) {
        if (!world.isRemote) {
            ItemStack item = player.getHeldItem(Hand.OFF_HAND);
            if(!item.isEmpty() && item.getItem() instanceof BlockItem){
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TravelAnchorTile) {
                    BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    ((TravelAnchorTile) te).setMimic(mimicState);
                }
                return ActionResultType.SUCCESS;
            }
            TileEntity tile = world.getTileEntity(pos);
            if (tile == null) {
                throw new IllegalStateException("Expected a tile entity of type TravelAnchorTile but got none.");
            } else if (tile instanceof TravelAnchorTile) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.travel_anchors.travel_anchor");
                    }

                    @Override
                    public Container createMenu(int window, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
                        return new TravelAnchorContainer(window, world, pos, inventory, player);
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
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, world, pos, newState, isMoving);
        TravelAnchorList.get(world).setAnchor(pos, null);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}

