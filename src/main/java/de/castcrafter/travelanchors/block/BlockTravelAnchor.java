package de.castcrafter.travelanchors.block;

import de.castcrafter.travelanchors.ModBlocks;
import de.castcrafter.travelanchors.TeleportHandler;
import de.castcrafter.travelanchors.TravelAnchorList;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
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
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
    public void registerClient(SetupContext ctx) {
        MenuScreens.register(ModBlocks.travelAnchor.menu, ScreenTravelAnchor::new);
        BlockEntityRenderers.register(ModBlocks.travelAnchor.getBlockEntityType(), dispatcher -> new RenderTravelAnchor());
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
        tooltip.add(Component.translatable("tooltip.travelanchors.travel_anchor_block"));
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TileTravelAnchor anchor) {
            ItemStack stack = player.getItemInHand(hand);
            if (anchor.isLocked()) {
                if (stack.getItem() == Items.STICK) {
                    if (!level.isClientSide) {
                        anchor.setLocked(false);
                        player.displayClientMessage(Component.translatable("travelanchors.lock.unlocked"), true);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                if (!level.isClientSide && !TeleportHandler.canPlayerTeleportAnyHand(player)) {
                    player.displayClientMessage(Component.translatable("travelanchors.lock.interact"), true);
                }
                return InteractionResult.PASS;
            }
            
            ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
            if (hand == InteractionHand.MAIN_HAND && stack.isEmpty() && !offhand.isEmpty() && offhand.getItem() instanceof BlockItem blockItem) {
                if (!level.isClientSide) {
                    BlockState mimicState = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player, InteractionHand.OFF_HAND, offhand, hit));
                    if (mimicState == null || mimicState.getBlock() == this) {
                        anchor.setMimic(null);
                    } else {
                        anchor.setMimic(mimicState);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            
            return super.use(state, level, pos, player, hand, hit);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        TravelAnchorList.get(level).setAnchor(level, pos, null, ModBlocks.travelAnchor.defaultBlockState());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}

