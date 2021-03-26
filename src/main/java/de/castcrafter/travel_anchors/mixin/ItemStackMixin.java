package de.castcrafter.travel_anchors.mixin;

import de.castcrafter.travel_anchors.TeleportHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "onItemUse(Lnet/minecraft/item/ItemUseContext;)Lnet/minecraft/util/ActionResultType;", at = @At("RETURN"), cancellable = true)
    public void onClickBlock(ItemUseContext context, CallbackInfoReturnable<ActionResultType> cir) {
        if (cir.getReturnValue() == ActionResultType.SUCCESS) return;

        if (TeleportHandler.canPlayerTeleport(context.getPlayer(), context.getHand()) && TeleportHandler.getAnchorToTeleport(context.getWorld(), context.getPlayer(), context.getPlayer().getPosition().toImmutable().down()) != null) {
            TeleportHandler.anchorTeleport(context.getWorld(), context.getPlayer(), context.getPlayer().getPosition().toImmutable().down(), context.getHand());
            cir.setReturnValue(ActionResultType.SUCCESS);
        }

    }
}

