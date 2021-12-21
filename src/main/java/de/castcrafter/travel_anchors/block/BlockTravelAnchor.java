package de.castcrafter.travel_anchors.block;

import de.castcrafter.travel_anchors.ModComponents;
import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.base.tile.MenuBlockBE;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockTravelAnchor extends MenuBlockBE<TileTravelAnchor, MenuTravelAnchor> {

    private static final VoxelShape SHAPE = Shapes.box(0.01, 0.01, 0.01, 0.99, 0.99, 0.99);

    public BlockTravelAnchor(ModX mod, Class<TileTravelAnchor> teClass, MenuType<MenuTravelAnchor> menu, Properties properties) {
        super(mod, teClass, menu, properties);
    }

    public BlockTravelAnchor(ModX mod, Class<TileTravelAnchor> teClass, MenuType<MenuTravelAnchor> menu, Properties properties, Item.Properties itemProperties) {
        super(mod, teClass, menu, properties, itemProperties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        ItemBlockRenderTypes.setRenderLayer(ModComponents.travelAnchor, RenderType.cutoutMipped());
        MenuScreens.register(ModComponents.travelAnchor.menu, ScreenTravelAnchor::new);
        BlockEntityRenderers.register(ModComponents.travelAnchor.getBlockEntityType(), dispatcher -> new RenderTravelAnchor());

    }
    
    @Override
    @SuppressWarnings("deprecation")
    public int getLightBlock(@Nonnull BlockState state, BlockGetter level, @Nonnull BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof TileTravelAnchor) {
            BlockState mimic = ((TileTravelAnchor) tile).getMimic();
            if (mimic != null) {
                return mimic.getLightBlock(level, pos);
            }
        }
        return super.getLightBlock(state, level, pos);
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof TileTravelAnchor) {
            BlockState mimic = ((TileTravelAnchor) tile).getMimic();
            if (mimic != null) {
                return mimic.getShape(level, pos, context);
            }
        }
        return Shapes.block();
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof TileTravelAnchor) {
            BlockState mimic = ((TileTravelAnchor) tile).getMimic();
            if (mimic != null) {
                return mimic.getBlockSupportShape(level, pos);
            }
        }
        return SHAPE;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, @Nonnull TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.travel_anchors.travel_anchor_block"));
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!level.isClientSide) {
            ItemStack item = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!item.isEmpty() && item.getItem() instanceof BlockItem && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof TileTravelAnchor) {
                    BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getStateForPlacement(new BlockPlaceContext(player, InteractionHand.OFF_HAND, item, hit));
                    if (mimicState == null || mimicState.getBlock() == this) {
                        ((TileTravelAnchor) be).setMimic(null);
                    } else {
                        ((TileTravelAnchor) be).setMimic(mimicState);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            super.use(state, level, pos, player, hand, hit);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        TravelAnchorList.get(level).setAnchor(level, pos, null, ModComponents.travelAnchor.defaultBlockState());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}

