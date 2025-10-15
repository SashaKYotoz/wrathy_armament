package net.sashakyotoz.wrathy_armament.blocks.entities.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;

public class SimpleCraftingContainer implements Container, StackedContentsCompatible {
    private final NonNullList<ItemStack> stacks;
    private final int width;
    private final int height;

    public SimpleCraftingContainer(int width, int height, int extraSize) {
        this.stacks = NonNullList.withSize(width * height + (extraSize + 1), ItemStack.EMPTY);
        this.width = width;
        this.height = height;
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.stacks) {
            if (!itemStack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return pSlot >= this.getContainerSize() ? ItemStack.EMPTY : this.stacks.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemStack = ContainerHelper.removeItem(this.stacks, pSlot, pAmount);
        if (!itemStack.isEmpty())
            this.setChanged();
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.stacks, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.stacks.set(pSlot, pStack);
        if (!pStack.isEmpty() && pStack.getCount() > this.getMaxStackSize())
            pStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public void fillStackedContents(StackedContents pContents) {
        for (ItemStack itemStack : this.stacks) {
            pContents.accountStack(itemStack);
        }
    }
}