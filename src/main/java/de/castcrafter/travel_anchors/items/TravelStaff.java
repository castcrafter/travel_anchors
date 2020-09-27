package de.castcrafter.travel_anchors.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;


public class TravelStaff extends Item {

    public TravelStaff(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                Vector3d pos = playerIn.getPositionVec();
                float yaw = playerIn.rotationYaw * ((float) Math.PI / 180F);
                float pitch = playerIn.rotationPitch * ((float) Math.PI / 180F);
                playerIn.setPositionAndUpdate(pos.x - MathHelper.sin(yaw) * 5, pos.y + -MathHelper.sin(pitch) * 5, pos.z + MathHelper.cos(yaw) * 5);
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            }
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }
}
