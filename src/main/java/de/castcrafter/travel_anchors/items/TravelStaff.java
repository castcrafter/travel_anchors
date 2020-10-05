package de.castcrafter.travel_anchors.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class TravelStaff extends Item {

    private double distance;
    private double smallest = 180;

    private BlockPos anchor = BlockPos.ZERO;

    public TravelStaff(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);

        if (isSelected == true){

            for(int i = 0; i < 2 /*list size*/; i++){
                BlockPos anchor = BlockPos.ZERO; //anchor of list

                Vector3d blockVec = new Vector3d(anchor.getX() + 0.5 - entity.getPosX(), anchor.getY() + entity.getYOffset() - entity.getPosY(), anchor.getZ() + 0.5 - entity.getPosZ());
                Vector3d lookVec = Vector3d.fromPitchYaw(entity.rotationPitch, entity.rotationYaw).normalize();

                this.distance = blockVec.length();
                Vector3d blockVec_n = blockVec.normalize();
                double angle = Math.toDegrees(Math.acos(lookVec.dotProduct(blockVec_n)));

                if(angle < smallest){
                    smallest = angle;
                }
            }
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {

        int max_angle = 10; //will be a config value
        int max_distance = 20; //will be a config value

        if(!world.isRemote){
            if(smallest <= max_angle && distance <= max_distance && isClear(world, anchor)){
                //needs to be anchor with smallest angle
                player.setPositionAndUpdate(anchor.getX() + 0.5, anchor.getY() + 1, anchor.getZ() + 0.5);
                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
            }
            else if(distance > max_distance){
                Minecraft.getInstance().player.sendChatMessage("Anchor out of reach");
            }
            else if(!isClear(world, anchor)){
                Minecraft.getInstance().player.sendChatMessage("Anchor is obstructed");
            }
        }
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flags) {
        tooltip.add(new TranslationTextComponent("tooltip.travel_anchors.travel_staff"));
    }

    public static boolean isClear(IBlockReader world, BlockPos target) {
        return isClear(world.getBlockState(target.up(1))) && isClear(world.getBlockState(target.up(2)));
    }

    private static boolean isClear(BlockState blockState) {
        return !blockState.getMaterial().isSolid();
    }
}

