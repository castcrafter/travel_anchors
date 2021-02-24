package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.config.ServerConfig;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;

public class TeleportHandler {

    public static boolean anchorTeleport(World world, PlayerEntity player, @Nullable BlockPos except, @Nullable Hand hand) {
        Pair<BlockPos, String> anchor = getAnchorToTeleport(world, player, except);
        return teleportPlayer(player, anchor, hand);
    }

    public static Pair<BlockPos, String> getAnchorToTeleport(World world, PlayerEntity player, @Nullable BlockPos except) {
        if (!player.isSneaking()) {
            double maxDistance = getMaxDistance(player);
            Vector3d positionVec = player.getPositionVec().add(0, player.getEyeHeight(), 0);
            Optional<Pair<BlockPos, String>> anchor = TravelAnchorList.get(world).getAnchorsAround(player.getPositionVec(), Math.pow(maxDistance, 2))
                    .filter(pair -> except == null || !except.equals(pair.getLeft()))
                    .min((p1, p2) -> {
                        double angle1 = Math.abs(getAngleRadians(positionVec, p1.getLeft(), player.rotationYaw, player.rotationPitch));
                        double angle2 = Math.abs(getAngleRadians(positionVec, p2.getLeft(), player.rotationYaw, player.rotationPitch));
                        return Double.compare(angle1, angle2);
                    }).filter(p -> Math.abs(getAngleRadians(positionVec, p.getLeft(), player.rotationYaw, player.rotationPitch)) <= Math.toRadians(ServerConfig.MAX_ANGLE.get()))
                    .filter(p -> canTeleportTo(world, p.getLeft()));
            return anchor.orElse(null);
        } else {
            return null;
        }
    }
    
    public static boolean teleportPlayer(PlayerEntity player, @Nullable Pair<BlockPos, String> anchor, @Nullable Hand hand) {
        if (anchor != null) {
            if (!player.getEntityWorld().isRemote) {
                player.setPositionAndUpdate(anchor.getLeft().getX() + 0.5, anchor.getLeft().getY() + 1, anchor.getLeft().getZ() + 0.5);
            }
            player.fallDistance = 0;
            if (hand != null) {
                player.swing(hand, true);
            }
            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.tp.success", anchor.getRight()), 10, 60, 10));
            }
            return true;
        } else {
            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.tp.fail"), 10, 60, 10));
            }
            return false;
        }
    }

    public static boolean shortTeleport(World world, PlayerEntity player, Hand hand) {
        Vector3d targetVec = player.getPositionVec().add(0, player.getEyeHeight(), 0);
        Vector3d lookVec = player.getLookVec();
        BlockPos target = null;
        for (double i = 7; i >= 2; i -= 0.5) {
            Vector3d v3d = targetVec.add(lookVec.mul(i, i, i));
            target = new BlockPos(Math.round(v3d.x), Math.round(v3d.y), Math.round(v3d.z));
            if (canTeleportTo(world, target.down())) { //to use the same check as the anchors use the position below
                break;
            } else {
                target = null;
            }
        }
        if (target != null) {
            if (!player.getEntityWorld().isRemote) {
                player.setPositionAndUpdate(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
            }
            player.fallDistance = 0;
            player.swing(hand, true);
            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
            return true;
        } else {
            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.hop.fail"), 10, 60, 10));
            }
            return false;
        }
    }

    public static boolean canTeleportTo(IBlockReader world, BlockPos target) {
        return !world.getBlockState(target.toImmutable().up(1)).isSolid()
                && !world.getBlockState(target.toImmutable().up(2)).isSolid()
                && target.getY() > 0;
    }

    public static boolean canPlayerTeleport(PlayerEntity player, Hand hand) {
        return canItemTeleport(player, hand) || canBlockTeleport(player);
    }

    public static boolean canBlockTeleport(PlayerEntity player) {
        return (player.getEntityWorld().getBlockState(player.getPosition().toImmutable().down()).getBlock() == ModComponents.travelAnchor
                && !player.isSneaking());
    }

    public static boolean canItemTeleport(PlayerEntity player, Hand hand) {
        return player.getHeldItem(hand).getItem() == ModComponents.travelStaff
                || EnchantmentHelper.getEnchantmentLevel(ModEnchantments.teleportation, player.getHeldItem(hand)) >= 1;
    }

    private static double getAngleRadians(Vector3d positionVec, BlockPos anchor, float yaw, float pitch) {
        Vector3d blockVec = new Vector3d(anchor.getX() + 0.5 - positionVec.x, anchor.getY() + 1.0 - positionVec.y, anchor.getZ() + 0.5 - positionVec.z).normalize();
        Vector3d lookVec = Vector3d.fromPitchYaw(pitch, yaw).normalize();
        return Math.acos(lookVec.dotProduct(blockVec));
    }

    public static double getMaxDistance(PlayerEntity player) {
        int mainHandLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.range, player.getHeldItem(Hand.MAIN_HAND));
        int offHandLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.range, player.getHeldItem(Hand.OFF_HAND));
        int lvl = Math.max(mainHandLevel, offHandLevel);
        return ServerConfig.MAX_DISTANCE.get() * (1 + (lvl / 2d));
    }
    
    public static boolean canElevate(PlayerEntity player) {
        return player.getEntityWorld().getBlockState(player.getPosition().toImmutable().down()).getBlock() == ModComponents.travelAnchor;
    }
    
    public static boolean elevateUp(PlayerEntity player) {
        if (!canElevate(player)) {
            return false;
        }
        World world = player.getEntityWorld();
        BlockPos.Mutable searchPos = player.getPosition().toImmutable().toMutable();
        while (!World.isOutsideBuildHeight(searchPos) && (world.getBlockState(searchPos).getBlock() != ModComponents.travelAnchor || !canTeleportTo(world, searchPos))) {
            searchPos.move(Direction.UP);
        }
        BlockState state = world.getBlockState(searchPos);
        Pair<BlockPos, String> anchor = null;
        if (state.getBlock() == ModComponents.travelAnchor && canTeleportTo(world, searchPos)) {
            BlockPos target = searchPos.toImmutable();
            String name = ModComponents.travelAnchor.getTile(world, target).getName();
            if (!name.isEmpty()) {
                anchor = Pair.of(target, name);
            }
        }
        return teleportPlayer(player, anchor, null);
    }
    
    public static boolean elevateDown(PlayerEntity player) {
        if (!canElevate(player)) {
            return false;
        }
        World world = player.getEntityWorld();
        BlockPos.Mutable searchPos = player.getPosition().toImmutable().down(2).toMutable();
        while (!World.isOutsideBuildHeight(searchPos) && (world.getBlockState(searchPos).getBlock() != ModComponents.travelAnchor || !canTeleportTo(world, searchPos))) {
            searchPos.move(Direction.DOWN);
        }
        BlockState state = world.getBlockState(searchPos);
        Pair<BlockPos, String> anchor = null;
        if (state.getBlock() == ModComponents.travelAnchor && canTeleportTo(world, searchPos)) {
            BlockPos target = searchPos.toImmutable();
            String name = ModComponents.travelAnchor.getTile(world, target).getName();
            if (!name.isEmpty()) {
                anchor = Pair.of(target, name);
            }
        }
        return teleportPlayer(player, anchor, null);
    }
}
