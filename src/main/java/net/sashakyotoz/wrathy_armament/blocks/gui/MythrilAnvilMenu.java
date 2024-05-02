package net.sashakyotoz.wrathy_armament.blocks.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.MythrilAnvilBlockEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMenus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MythrilAnvilMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public final static HashMap<String, Object> guiState = new HashMap<>();
    public final Level level;
    public final Player player;
    public int x, y, z;
    private ContainerLevelAccess access = ContainerLevelAccess.NULL;
    private IItemHandler internal;
    public final Map<Integer, Slot> customSlots = new HashMap<>();
    private boolean bound = false;
    private Supplier<Boolean> boundItemMatcher = null;
    private Entity boundEntity = null;
    private MythrilAnvilBlockEntity boundBlockEntity = null;

    public MythrilAnvilMenu(int id, Inventory inv) {
        super(WrathyArmamentMenus.MYTHRIL_ANVIL.get(), id);
        this.player = inv.player;
        this.level = inv.player.level();
        this.internal = new ItemStackHandler(5);
    }

    public MythrilAnvilMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(WrathyArmamentMenus.MYTHRIL_ANVIL.get(), id);
        this.player = inv.player;
        this.level = inv.player.level();
        this.internal = new ItemStackHandler(5);
        BlockPos pos = null;
        if (extraData != null) {
            pos = extraData.readBlockPos();
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
            access = ContainerLevelAccess.create(level, pos);
        }
        if (pos != null) {
            if (extraData.readableBytes() > 1) {
                extraData.readByte();
                boundEntity = level.getEntity(extraData.readVarInt());
                if (boundEntity != null)
                    boundEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                        this.internal = capability;
                        this.bound = true;
                    });
            } else {
                boundBlockEntity = (MythrilAnvilBlockEntity) this.level.getBlockEntity(pos);
                if (boundBlockEntity != null)
                    boundBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                        this.internal = capability;
                        this.bound = true;
                    });
            }
        }
        this.customSlots.put(0, this.addSlot(new SlotItemHandler(internal, 0, 83, 65)));//left
        this.customSlots.put(1, this.addSlot(new SlotItemHandler(internal, 1, 110, 110)));//bottom
        this.customSlots.put(2, this.addSlot(new SlotItemHandler(internal, 2, 128, 38)));//up
        this.customSlots.put(3, this.addSlot(new SlotItemHandler(internal, 3, 119, 74) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        }));//main slot
        this.customSlots.put(4, this.addSlot(new SlotItemHandler(internal, 4, 155, 83)));//right
        for (int y = 0; y < 3; ++y)
            for (int x = 0; x < 9; ++x)
                this.addSlot(new Slot(inv, x + (y + 1) * 9, 48 + x * 18, 142 + y * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(inv, i, 48 + i * 18, 200));
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.bound) {
            if (this.boundItemMatcher != null)
                return this.boundItemMatcher.get();
            else if (this.boundBlockEntity != null)
                return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
            else if (this.boundEntity != null)
                return this.boundEntity.isAlive();
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        Slot customSlot = this.customSlots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index < 5) {
                if (!this.moveItemStackTo(itemStack1, 5, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemStack1, itemstack);
            } else if (!this.moveItemStackTo(itemStack1, 0, 5, false)) {
                if (index < 5 + 27) {
                    if (!this.moveItemStackTo(itemStack1, 5 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemStack1, 5, 5 + 27, false))
                        return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (itemStack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
            if (itemStack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(player, itemStack1);
        }
        if (customSlot != null && customSlot.hasItem()){
            ItemStack itemStack1 = customSlot.getItem();
            itemstack = itemStack1.copy();
            if (this.customSlots.get(0).getItem().is(WrathyArmamentItems.MYTHRIL_INGOT.get()) && this.customSlots.get(1).getItem().is(Items.NETHERITE_SWORD) && this.customSlots.get(2).getItem().is(WrathyArmamentItems.COPPER_SWORD.get()) && this.customSlots.get(4).getItem().is(WrathyArmamentItems.MEOWMERE.get()) && this.boundBlockEntity.canCraftItem()){
                this.customSlots.get(3).set(new ItemStack(WrathyArmamentItems.ZENITH.get()));
            }
            if (itemStack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
            if (itemStack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
            customSlot.onTake(player,itemStack1);
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int mainI, int p_38906_, boolean b) {
        boolean flag = false;
        int i = mainI;
        if (b) {
            i = p_38906_ - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (b) {
                    if (i < mainI) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.set(itemstack);
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.set(itemstack);
                        flag = true;
                    }
                }
                if (b) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        if (!stack.isEmpty()) {
            if (b) {
                i = p_38906_ - 1;
            } else {
                i = mainI;
            }
            while (true) {
                if (b) {
                    if (i < mainI) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                if (b) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        return flag;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
                for (int j = 0; j < internal.getSlots(); ++j) {
                    playerIn.drop(internal.extractItem(j, internal.getStackInSlot(j).getCount(), false), false);
                }
            } else {
                for (int i = 0; i < internal.getSlots(); ++i) {
                    playerIn.getInventory().placeItemBackInInventory(internal.extractItem(i, internal.getStackInSlot(i).getCount(), false));
                }
            }
        }
    }

    public Map<Integer, Slot> get() {
        return customSlots;
    }
}
