package de.castcrafter.travel_anchors.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class TravelStaff extends Item {

    public TravelStaff(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {

        // TODO needs rewrite

        /*BlockRayTraceResult trace = rayTrace(world, player, RayTraceContext.FluidMode.NONE);

        if (!world.isRemote) {
            System.out.println(world.getBlockState(trace.getPos()).getBlock());
            if (player.isSneaking()) {
                if (trace != null && world.getBlockState(trace.getPos()).getBlock() == Registration.TRAVEL_ANCHOR_BLOCK.get()) {
                    player.setPositionAndUpdate(trace.getHitVec().x, trace.getHitVec().y + 1, trace.getHitVec().z);
                } else {
                    Vector3d pos = player.getPositionVec();
                    float yaw = player.rotationYaw * ((float) Math.PI / 180F);
                    float pitch = player.rotationPitch * ((float) Math.PI / 180F);
                    player.setPositionAndUpdate(pos.x - MathHelper.sin(yaw) * 5, pos.y + -MathHelper.sin(pitch) * 5, pos.z + MathHelper.cos(yaw) * 5);
                }
                return ActionResult.resultSuccess(player.getHeldItem(hand));
            }
        }*/

        return ActionResult.resultPass(player.getHeldItem(hand));
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flags) {
        tooltip.add(new TranslationTextComponent("tooltip.travel_anchors.travel_staff"));
    }
}

