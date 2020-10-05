package de.castcrafter.travel_anchors.items;

import de.castcrafter.travel_anchors.TravelAnchorList;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class TravelStaff extends Item {

    public static final double MAX_ANGLE = Math.toRadians(30);
    public static final double MAX_DISTANCE_SQ = Math.pow(20, 2);

    public TravelStaff(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (context.getPlayer() != null && context.getWorld().getBlockState(context.getPos()).getBlock() == Registration.TRAVEL_ANCHOR_BLOCK.get()) {
            onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand());
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isRemote) {
            Vector3d positionVec = player.getPositionVec();
            if(player.isSneaking()){
                float yaw = player.rotationYaw * ((float) Math.PI / 180F);
                float pitch = player.rotationPitch * ((float) Math.PI / 180F);
                BlockPos target = new BlockPos(positionVec.x - MathHelper.sin(yaw) * 7, positionVec.y + -MathHelper.sin(pitch) * 7, positionVec.z + MathHelper.cos(yaw) * 7);
                if(canTeleport(world, target)){
                    player.setPositionAndUpdate(target.getX(), target.getY(), target.getZ());
                    player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
                }
                else {
                    ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.hop.fail"), 10, 60, 10));
                }
            }
            else {
                Optional<Pair<BlockPos, String>> anchor = TravelAnchorList.get(world).getAnchorsAround(player.getPositionVec(), MAX_DISTANCE_SQ).map(p -> {
                    System.out.println(p);
                    return p;
                }).min((p1, p2) -> {
                    double angle1 = Math.abs(getAngleRadians(positionVec, p1.getLeft(), player.rotationYaw, player.rotationPitch));
                    double angle2 = Math.abs(getAngleRadians(positionVec, p2.getLeft(), player.rotationYaw, player.rotationPitch));
                    return Double.compare(angle1, angle2);
                }).filter(p -> Math.abs(getAngleRadians(positionVec, p.getLeft(), player.rotationYaw, player.rotationPitch)) <= MAX_ANGLE)
                        .filter(p -> canTeleport(world, p.getLeft()));
                if (anchor.isPresent()) {
                    player.setPositionAndUpdate(anchor.get().getLeft().getX() + 0.5, anchor.get().getLeft().getY() + 1, anchor.get().getLeft().getZ() + 0.5);
                    player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
                    if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.tp.success", anchor.get().getRight()), 10, 60, 10));
                    }
                } else if (player instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.tp.fail"), 10, 60, 10));
                }
            }
        }
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flags) {
        tooltip.add(new TranslationTextComponent("tooltip.travel_anchors.travel_staff"));
    }

    public static boolean canTeleport(IBlockReader world, BlockPos target) {
        return canTeleport(world.getBlockState(target.up(1))) && canTeleport(world.getBlockState(target.up(2)));
    }

    private static boolean canTeleport(BlockState blockState) {
        return !blockState.getMaterial().isSolid();
    }

    private static double getAngleRadians(Vector3d positionVec, BlockPos anchor, float yaw, float pitch) {
        Vector3d blockVec = new Vector3d(anchor.getX() + 0.5 - positionVec.x, anchor.getY() + 0.5 - positionVec.y, anchor.getZ() + 0.5 - positionVec.z).normalize();
        Vector3d lookVec = Vector3d.fromPitchYaw(pitch, yaw).normalize();
        return Math.acos(lookVec.dotProduct(blockVec));
    }
}

