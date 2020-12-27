package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockGUI;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockTravelAnchor extends BlockGUI<TileTravelAnchor, ContainerTravelAnchor> {

    private static final VoxelShape SHAPE = VoxelShapes.create(0.01, 0.01, 0.01, 0.99, 0.99, 0.99);

    public BlockTravelAnchor(ModX mod, Class<TileTravelAnchor> teClass, ContainerType<ContainerTravelAnchor> container, Properties properties) {
        super(mod, teClass, container, properties);
    }

    public BlockTravelAnchor(ModX mod, Class<TileTravelAnchor> teClass, ContainerType<ContainerTravelAnchor> container, Properties properties, Item.Properties itemProperties) {
        super(mod, teClass, container, properties, itemProperties);
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
        return VoxelShapes.fullCube();
    }

    @Nonnull
    @Override
    public VoxelShape getRenderShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTravelAnchor) {
            BlockState mimic = ((TileTravelAnchor) tile).getMimic();
            if (mimic != null) {
                return mimic.getRenderShape(world, pos);
            }
        }
        return SHAPE;
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flags) {
        tooltip.add(new TranslationTextComponent("tooltip.travel_anchors.travel_anchor_block"));
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        if (!world.isRemote) {
            ItemStack item = player.getHeldItem(Hand.OFF_HAND);
            if (!item.isEmpty() && item.getItem() instanceof BlockItem && player.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileTravelAnchor) {
                    BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getStateForPlacement(new BlockItemUseContext(player, Hand.OFF_HAND, item, hit));
                    if (mimicState == null || mimicState.getBlock() == this) {
                        ((TileTravelAnchor) te).setMimic(null);
                    } else {
                        ((TileTravelAnchor) te).setMimic(mimicState);
                    }
                }
                return ActionResultType.SUCCESS;
            }
            super.onBlockActivated(state, world, pos, player, hand, hit);
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

