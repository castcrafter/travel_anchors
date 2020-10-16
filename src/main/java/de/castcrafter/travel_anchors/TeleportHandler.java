package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;

public class TeleportHandler {

    public static boolean anchorTeleport(World world, PlayerEntity player, @Nullable BlockPos except) {
        Pair<BlockPos, String> anchor = getAnchorToTeleport(world, player, except);

        if (anchor != null) {
            if (!player.getEntityWorld().isRemote) {
                player.setPositionAndUpdate(anchor.getLeft().getX() + 0.5, anchor.getLeft().getY() + 1, anchor.getLeft().getZ() + 0.5);
            }
            player.fallDistance = 0;
            player.swing(Hand.MAIN_HAND, true);
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

    public static Pair<BlockPos, String> getAnchorToTeleport(World world, PlayerEntity player, @Nullable BlockPos except) {
        double maxDistance = getMaxDistance(player);
        Vector3d positionVec = player.getPositionVec();
        if (!player.isSneaking()) {
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

    public static boolean shortTeleport(World world, PlayerEntity player) {
        Vector3d positionVec = player.getPositionVec();
        float yaw = player.rotationYaw * ((float) Math.PI / 180F);
        float pitch = player.rotationPitch * ((float) Math.PI / 180F);
        BlockPos target = new BlockPos(positionVec.x - MathHelper.sin(yaw) * 7, positionVec.y + -MathHelper.sin(pitch) * 7, positionVec.z + MathHelper.cos(yaw) * 7);
        if (canTeleportTo(world, target)) {
            if (!player.getEntityWorld().isRemote) {
                player.setPositionAndUpdate(target.getX(), target.getY(), target.getZ());
            }
            player.fallDistance = 0;
            player.swing(Hand.MAIN_HAND, true);
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

    public static boolean canPlayerTeleport(PlayerEntity player) {
        return canItemTeleport(player) || canBlockTeleport(player);
    }

    public static boolean canBlockTeleport(PlayerEntity player) {
        return (player.getEntityWorld().getBlockState(player.getPosition().toImmutable().down()).getBlock() == Registration.TRAVEL_ANCHOR_BLOCK.get()
                && !player.isSneaking());
    }

    public static boolean canItemTeleport(PlayerEntity player) {
        return player.getHeldItem(Hand.MAIN_HAND).getItem() == Registration.TRAVEL_STAFF.get()
                || EnchantmentHelper.getEnchantmentLevel(Registration.TELEPORTATION_ENCHANTMENT.get(), player.getHeldItem(Hand.MAIN_HAND)) >= 1;
    }

    private static double getAngleRadians(Vector3d positionVec, BlockPos anchor, float yaw, float pitch) {
        Vector3d blockVec = new Vector3d(anchor.getX() + 0.5 - positionVec.x, anchor.getY() - positionVec.y, anchor.getZ() + 0.5 - positionVec.z).normalize();
        Vector3d lookVec = Vector3d.fromPitchYaw(pitch, yaw).normalize();
        return Math.acos(lookVec.dotProduct(blockVec));
    }

    public static double getMaxDistance(PlayerEntity player) {
        ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
        int lvl = EnchantmentHelper.getEnchantmentLevel(Registration.RANGE_ENCHANTMENT.get(), stack);
        return ServerConfig.MAX_DISTANCE.get() * (1 + (lvl / 2d));
    }
}
