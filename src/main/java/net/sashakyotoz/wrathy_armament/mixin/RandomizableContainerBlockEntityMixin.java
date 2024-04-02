package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RandomizableContainerBlockEntity.class)
public abstract class RandomizableContainerBlockEntityMixin {

    @Shadow protected abstract NonNullList<ItemStack> getItems();

    @ModifyVariable(method = "canOpen", argsOnly = true, at = @At("RETURN"))
    public boolean canOpen(Player player){
        if (!player.getInventory().contains(new ItemStack(WrathyArmamentItems.BLADE_OF_CHAOS.get())) && getItems().contains(new ItemStack(WrathyArmamentItems.HALF_ZATOICHI.get()))){
            return false;
        }
        return canOpen(player);
    }
}
