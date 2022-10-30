package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;

public class TeleportHandler {

    public static boolean anchorTeleport(Level level, Player player, @Nullable BlockPos except, @Nullable InteractionHand hand) {
        Pair<BlockPos, String> anchor = getAnchorToTeleport(level, player, except);
        return teleportPlayer(player, anchor, hand);
    }

    public static Pair<BlockPos, String> getAnchorToTeleport(Level level, Player player, @Nullable BlockPos except) {
        if (!player.isShiftKeyDown()) {
            double maxDistance = getMaxDistance(player);
            Vec3 positionVec = player.position().add(0, player.getEyeHeight(), 0);
            Optional<Pair<BlockPos, String>> anchor = TravelAnchorList.get(level).getAnchorsAround(player.position(), Math.pow(maxDistance, 2))
                    .filter(pair -> except == null || !except.equals(pair.getLeft()))
                    .filter(p -> Math.abs(getAngleRadians(positionVec, p.getLeft(), player.getYRot(), player.getXRot())) <= Math.toRadians(CommonConfig.max_angle))
                    .min((p1, p2) -> {
                        double angle1 = getAngleRadians(positionVec, p1.getLeft(), player.getYRot(), player.getXRot());
                        double angle2 = getAngleRadians(positionVec, p2.getLeft(), player.getYRot(), player.getXRot());
                        if (Math.abs(Mth.wrapDegrees(angle1 - angle2)) < 0.1) { // About 4 deg
                            double dst1sqr = positionVec.distanceToSqr(p1.getLeft().getX() + 0.5, p1.getLeft().getY() + 1, p1.getLeft().getZ() + 0.5);
                            double dst2sqr = positionVec.distanceToSqr(p2.getLeft().getX() + 0.5, p2.getLeft().getY() + 1, p2.getLeft().getZ() + 0.5);
                            double anchorDistSqr = p1.getLeft().distSqr(p2.getLeft());
                            if (Math.min(dst1sqr, dst2sqr) < anchorDistSqr * 4) {
                                return Double.compare(dst1sqr, dst2sqr);
                            }
                        }
                        return Double.compare(Math.abs(angle1), Math.abs(angle2));
                    })
                    .filter(p -> canTeleportTo(level, p.getLeft()));
            return anchor.orElse(null);
        } else {
            return null;
        }
    }
    
    public static boolean teleportPlayer(Player player, @Nullable Pair<BlockPos, String> anchor, @Nullable InteractionHand hand) {
        if (anchor != null) {
            if (!player.getLevel().isClientSide) {
                Vec3 teleportVec = checkTeleport(player, anchor.getLeft().above());
                if (teleportVec == null) {
                    return false;
                }
                player.teleportTo(teleportVec.x(), teleportVec.y(), teleportVec.z());
            }
            player.fallDistance = 0;
            if (hand != null) {
                player.swing(hand, true);
            }
            player.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
            if (!player.level.isClientSide) {
                player.displayClientMessage(Component.translatable("travelanchors.tp.success", anchor.getRight()), true);
            }
            return true;
        } else {
            if (!player.level.isClientSide) {
                player.displayClientMessage(Component.translatable("travelanchors.tp.fail"), true);
            }
            return false;
        }
    }

    public static boolean shortTeleport(Level level, Player player, InteractionHand hand) {
        Vec3 targetVec = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 lookVec = player.getLookAngle();
        BlockPos target = null;
        for (double i = 7; i >= 2; i -= 0.5) {
            Vec3 v3d = targetVec.add(lookVec.multiply(i, i, i));
            target = new BlockPos(Math.round(v3d.x), Math.round(v3d.y), Math.round(v3d.z));
            if (canTeleportTo(level, target.below())) { //to use the same check as the anchors use the position below
                break;
            } else {
                target = null;
            }
        }
        if (target != null) {
            if (!player.getLevel().isClientSide) {
                Vec3 teleportVec = checkTeleport(player, target);
                if (teleportVec == null) {
                    return false;
                }
                player.teleportTo(teleportVec.x(), teleportVec.y(), teleportVec.z());
            }
            player.fallDistance = 0;
            player.swing(hand, true);
            player.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
            return true;
        } else {
            if (!player.level.isClientSide) {
                player.displayClientMessage(Component.translatable("travelanchors.hop.fail"), true);
            }
            return false;
        }
    }

    public static boolean canTeleportTo(BlockGetter level, BlockPos target) {
        return !level.getBlockState(target.immutable().above(1)).canOcclude()
                && !level.getBlockState(target.immutable().above(2)).canOcclude()
                && target.getY() >= level.getMinBuildHeight();
    }

    public static boolean canPlayerTeleport(Player player, InteractionHand hand) {
        return canItemTeleport(player, hand) || canBlockTeleport(player);
    }

    public static boolean canBlockTeleport(Player player) {
        return (player.getLevel().getBlockState(player.blockPosition().immutable().below()).getBlock() == ModBlocks.travelAnchor
                && !player.isShiftKeyDown());
    }

    public static boolean canItemTeleport(Player player, InteractionHand hand) {
        return player.getItemInHand(hand).getItem() == ModItems.travelStaff
                || player.getItemInHand(hand).getEnchantmentLevel(ModEnchantments.teleportation) >= 1;
    }

    private static double getAngleRadians(Vec3 positionVec, BlockPos anchor, float yRot, float xRot) {
        Vec3 blockVec = new Vec3(anchor.getX() + 0.5 - positionVec.x, anchor.getY() + 1.0 - positionVec.y, anchor.getZ() + 0.5 - positionVec.z).normalize();
        Vec3 lookVec = Vec3.directionFromRotation(xRot, yRot).normalize();
        return Math.acos(lookVec.dot(blockVec));
    }

    public static double getMaxDistance(Player player) {
        int mainHandLevel = player.getItemInHand(InteractionHand.MAIN_HAND).getEnchantmentLevel(ModEnchantments.range);
        int offHandLevel = player.getItemInHand(InteractionHand.OFF_HAND).getEnchantmentLevel(ModEnchantments.range);
        int lvl = Math.max(mainHandLevel, offHandLevel);
        return CommonConfig.max_distance * (1 + (lvl / 2d));
    }
    
    public static boolean canElevate(Player player) {
        return player.getLevel().getBlockState(player.blockPosition().immutable().below()).getBlock() == ModBlocks.travelAnchor;
    }
    
    public static boolean elevateUp(Player player) {
        if (!canElevate(player)) {
            return false;
        }
        Level level = player.getLevel();
        BlockPos.MutableBlockPos searchPos = player.blockPosition().immutable().mutable();
        while (!level.isOutsideBuildHeight(searchPos) && (level.getBlockState(searchPos).getBlock() != ModBlocks.travelAnchor || !canTeleportTo(level, searchPos))) {
            searchPos.move(Direction.UP);
        }
        BlockState state = level.getBlockState(searchPos);
        Pair<BlockPos, String> anchor = null;
        if (state.getBlock() == ModBlocks.travelAnchor && canTeleportTo(level, searchPos)) {
            BlockPos target = searchPos.immutable();
            String name = ModBlocks.travelAnchor.getBlockEntity(level, target).getName();
            if (!name.isEmpty()) {
                anchor = Pair.of(target, name);
            }
        }
        return teleportPlayer(player, anchor, null);
    }
    
    public static boolean elevateDown(Player player) {
        if (!canElevate(player)) {
            return false;
        }
        Level level = player.getLevel();
        BlockPos.MutableBlockPos searchPos = player.blockPosition().immutable().below(2).mutable();
        while (!level.isOutsideBuildHeight(searchPos) && (level.getBlockState(searchPos).getBlock() != ModBlocks.travelAnchor || !canTeleportTo(level, searchPos))) {
            searchPos.move(Direction.DOWN);
        }
        BlockState state = level.getBlockState(searchPos);
        Pair<BlockPos, String> anchor = null;
        if (state.getBlock() == ModBlocks.travelAnchor && canTeleportTo(level, searchPos)) {
            BlockPos target = searchPos.immutable();
            String name = ModBlocks.travelAnchor.getBlockEntity(level, target).getName();
            if (!name.isEmpty()) {
                anchor = Pair.of(target, name);
            }
        }
        return teleportPlayer(player, anchor, null);
    }
    
    @Nullable
    private static Vec3 checkTeleport(Player player, BlockPos target) {
        EntityTeleportEvent event = new EntityTeleportEvent(player, target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return null;
        }
        return new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
    }
}
