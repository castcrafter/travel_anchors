package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.blocks.TravelAnchorBlock;
import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.enchantments.RangeEnchantment;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;


@Mod.EventBusSubscriber(modid = TravelAnchors.MODID)
public class TeleportHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof ServerPlayerEntity){
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            if(isAnchor(player.world.getBlockState(player.getPosition().down()))){
                anchorTeleport(event.getEntityLiving().world, player);
            }
        }
    }

    public static void anchorTeleport(World world, PlayerEntity player){
        double MaxDistance = RangeEnchantment.getMaxDistance(player);
        Vector3d positionVec = player.getPositionVec();
        if(!player.isSneaking()){
            Optional<Pair<BlockPos, String>> anchor = TravelAnchorList.get(world).getAnchorsAround(player.getPositionVec(), Math.pow(MaxDistance, 2)).min((p1, p2) -> {
                double angle1 = Math.abs(getAngleRadians(positionVec, p1.getLeft(), player.rotationYaw, player.rotationPitch));
                double angle2 = Math.abs(getAngleRadians(positionVec, p2.getLeft(), player.rotationYaw, player.rotationPitch));
                return Double.compare(angle1, angle2);
            }).filter(p -> Math.abs(getAngleRadians(positionVec, p.getLeft(), player.rotationYaw, player.rotationPitch)) <= Math.toRadians(ServerConfig.MAX_ANGLE.get()))
                    .filter(p -> canTeleport(world, p.getLeft()));
            if (anchor.isPresent()) {
                player.setPositionAndUpdate(anchor.get().getLeft().getX() + 0.5, anchor.get().getLeft().getY() + 1, anchor.get().getLeft().getZ() + 0.5);
                player.fallDistance = 0;
                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
                if (player instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.tp.success", anchor.get().getRight()), 10, 60, 10));
                }
            } else if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("travel_anchors.tp.fail"), 10, 60, 10));
            }
        }
    }

    public static void shortTeleport(World world, PlayerEntity player){
        Vector3d positionVec = player.getPositionVec();
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

    public static boolean canTeleport(IBlockReader world, BlockPos target) {
        return canTeleport(world.getBlockState(target.up(1))) && canTeleport(world.getBlockState(target.up(2)));
    }

    private static boolean canTeleport(BlockState blockState) {
        return !blockState.getMaterial().isSolid();
    }

    public static boolean isAnchor(BlockState blockState){
        return  blockState.getBlock() instanceof TravelAnchorBlock;
    }

    private static double getAngleRadians(Vector3d positionVec, BlockPos anchor, float yaw, float pitch) {
        Vector3d blockVec = new Vector3d(anchor.getX() + 0.5 - positionVec.x, anchor.getY() + 0.5 - positionVec.y, anchor.getZ() + 0.5 - positionVec.z).normalize();
        Vector3d lookVec = Vector3d.fromPitchYaw(pitch, yaw).normalize();
        return Math.acos(lookVec.dotProduct(blockVec));
    }
}
