package de.castcrafter.travel_anchors.items;

import com.sun.javafx.geom.Vec3d;
import de.castcrafter.travel_anchors.setup.ModSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
        RayTraceResult trace = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
        RayTraceResult newtrace = Minecraft.getInstance().objectMouseOver;

        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                Vector3d pos = playerIn.getPositionVec();
                float yaw = playerIn.rotationYaw * ((float) Math.PI / 180F);
                float pitch = playerIn.rotationPitch * ((float) Math.PI / 180F);
                playerIn.setPositionAndUpdate(pos.x - MathHelper.sin(yaw) * 5, pos.y + -MathHelper.sin(pitch) * 5, pos.z + MathHelper.cos(yaw) * 5);
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            }
            if(trace != null && trace.getType() == RayTraceResult.Type.BLOCK /*&& Block == TravelAnchorBlock*/){
                if(playerIn.isSneaking()){
                    Vec3d positionVector = new Vec3d(trace.getHitVec().x, trace.getHitVec().y, trace.getHitVec().z);
                    playerIn.setPositionAndUpdate(positionVector.x, positionVector.y, positionVector.z);
                }
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.travel_staff"));
    }
}
