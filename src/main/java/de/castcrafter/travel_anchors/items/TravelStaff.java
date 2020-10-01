package de.castcrafter.travel_anchors.items;

import de.castcrafter.travel_anchors.blocks.TravelAnchorBlock;
import de.castcrafter.travel_anchors.blocks.TravelAnchorTile;
import de.castcrafter.travel_anchors.setup.ModSetup;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class TravelStaff extends Item {

    public TravelStaff(){
        super(new Item.Properties()
        .maxStackSize(1)
        .group(ModSetup.ITEM_GROUP));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        BlockRayTraceResult trace = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);

        if (!worldIn.isRemote) {
            System.out.println(worldIn.getBlockState(trace.getPos()).getBlock());
            if (playerIn.isSneaking()) {
                if(trace != null && worldIn.getBlockState(trace.getPos()).getBlock() == Registration.TRAVEL_ANCHOR_BLOCK.get()){
                    playerIn.setPositionAndUpdate(trace.getHitVec().x, trace.getHitVec().y + 1, trace.getHitVec().z);
                }
                else {
                    Vector3d pos = playerIn.getPositionVec();
                    float yaw = playerIn.rotationYaw * ((float) Math.PI / 180F);
                    float pitch = playerIn.rotationPitch * ((float) Math.PI / 180F);
                    playerIn.setPositionAndUpdate(pos.x - MathHelper.sin(yaw) * 5, pos.y + -MathHelper.sin(pitch) * 5, pos.z + MathHelper.cos(yaw) * 5);
                }
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.travel_staff"));
    }


}

