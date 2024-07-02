package net.sashakyotoz.wrathy_armament.blocks.gui;

import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.ChaosForge;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;

import javax.annotation.Nullable;
import java.util.Map;

public class ChaosForgeMenu extends ItemCombinerMenu {
    public int repairItemCountCost;
    @Nullable
    private String itemName;
    public BlockPos pos;
    public final Level level;
    private final DataSlot cost = DataSlot.standalone();
    public ChaosForgeMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(WrathyArmamentMiscRegistries.CHAOS_FORGE.get(), id, inventory, access);
        this.addDataSlot(this.cost);
        this.level = inventory.player.level();
        WrathyArmament.LOGGER.debug(getPos(level, inventory.player) + " ");
        this.pos = getPos(level, inventory.player);
    }
    public ChaosForgeMenu(int i, Inventory inventory){
        this(i,inventory,ContainerLevelAccess.NULL);
    }
    public ChaosForgeMenu(int i, Inventory inventory, FriendlyByteBuf extraData) {
        this(i,inventory,ContainerLevelAccess.create(inventory.player.level(), extraData.readBlockPos()));
    }
    private BlockPos getPos(Level level,Player player){
        for (int x = -7; x < 7; x++) {
            for (int y = -7; y < 7; y++) {
                for (int z = -7; z < 7; z++) {
                    if (level.getBlockState(player.getOnPos().offset(x,y,z)).is(WrathyArmamentBlocks.CHAOS_FORGE.get()))
                        return new BlockPos(player.getOnPos().offset(x,y,z));
                }
            }
        }
        return null;
    }
    public ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (stack) -> true).withSlot(1, 76, 47, (stack) -> true).withResultSlot(2, 134, 47).build();
    }
    protected boolean isValidBlock(BlockState state) {
        return state.is(WrathyArmamentBlocks.CHAOS_FORGE.get());
    }

    protected boolean mayPickup(Player player, boolean p_39024_) {
        return (player.getAbilities().instabuild || player.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    protected void onTake(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-this.cost.get());
        }
        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                itemstack.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }
        if (player.level().getBlockState(pos).getValue(ChaosForge.FIRING) > 0){
            BlockState state = player.level().getBlockState(pos);
            player.level().setBlock(pos,player.level().getBlockState(pos).setValue(ChaosForge.FIRING,state.getValue(ChaosForge.FIRING)-1),3);
        }

        this.cost.set(0);
    }

    public void createResult() {
        ItemStack itemstack = this.inputSlots.getItem(0);
        if (this.level.getBlockState(pos).getValue(ChaosForge.FIRING) > 1){
            this.cost.set(1);
            int i = 0;
            int j = 0;
            int k = 0;
            if (itemstack.isEmpty()) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
            } else {
                ItemStack zeroSlotStack = itemstack.copy();
                ItemStack firstSlotStack = this.inputSlots.getItem(1);
                if (zeroSlotStack.is(Items.NETHERITE_SWORD) && firstSlotStack.is(Items.NETHER_STAR)) {
                    this.resultSlots.setItem(0, new ItemStack(WrathyArmamentItems.BLADE_OF_CHAOS.get()));
                    this.broadcastChanges();
                } else {
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(zeroSlotStack);
                    j += itemstack.getBaseRepairCost() + (firstSlotStack.isEmpty() ? 0 : firstSlotStack.getBaseRepairCost());
                    this.repairItemCountCost = 0;
                    boolean flag = false;
                    if (!firstSlotStack.isEmpty()) {
                        flag = firstSlotStack.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(firstSlotStack).isEmpty();
                        if (zeroSlotStack.isDamageableItem() && zeroSlotStack.getItem().isValidRepairItem(itemstack, firstSlotStack)) {
                            int l2 = Math.min(zeroSlotStack.getDamageValue(), zeroSlotStack.getMaxDamage() / 4);
                            if (l2 <= 0) {
                                this.resultSlots.setItem(0, ItemStack.EMPTY);
                                this.cost.set(0);
                                return;
                            }

                            int i3;
                            for (i3 = 0; l2 > 0 && i3 < firstSlotStack.getCount(); ++i3) {
                                int j3 = zeroSlotStack.getDamageValue() - l2;
                                zeroSlotStack.setDamageValue(j3);
                                ++i;
                                l2 = Math.min(zeroSlotStack.getDamageValue(), zeroSlotStack.getMaxDamage() / 4);
                            }

                            this.repairItemCountCost = i3;
                        } else {
                            if (!flag && (!zeroSlotStack.is(firstSlotStack.getItem()) || !zeroSlotStack.isDamageableItem())) {
                                this.resultSlots.setItem(0, ItemStack.EMPTY);
                                this.cost.set(0);
                                return;
                            }

                            if (zeroSlotStack.isDamageableItem() && !flag) {
                                int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
                                int i1 = firstSlotStack.getMaxDamage() - firstSlotStack.getDamageValue();
                                int j1 = i1 + zeroSlotStack.getMaxDamage() * 12 / 100;
                                int k1 = l + j1;
                                int l1 = zeroSlotStack.getMaxDamage() - k1;
                                if (l1 < 0) {
                                    l1 = 0;
                                }

                                if (l1 < zeroSlotStack.getDamageValue()) {
                                    zeroSlotStack.setDamageValue(l1);
                                    i += 2;
                                }
                            }

                            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(firstSlotStack);
                            boolean flag2 = false;
                            boolean flag3 = false;

                            for (Enchantment enchantment1 : map1.keySet()) {
                                if (enchantment1 != null) {
                                    int i2 = map.getOrDefault(enchantment1, 0);
                                    int j2 = map1.get(enchantment1);
                                    j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                                    boolean flag1 = enchantment1.canEnchant(itemstack);
                                    if (this.player.getAbilities().instabuild || itemstack.is(Items.ENCHANTED_BOOK)) {
                                        flag1 = true;
                                    }

                                    for (Enchantment enchantment : map.keySet()) {
                                        if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                                            flag1 = false;
                                            ++i;
                                        }
                                    }

                                    if (!flag1) {
                                        flag3 = true;
                                    } else {
                                        flag2 = true;
                                        if (j2 > enchantment1.getMaxLevel()) {
                                            j2 = enchantment1.getMaxLevel();
                                        }

                                        map.put(enchantment1, j2);
                                        int k3 = switch (enchantment1.getRarity()) {
                                            case COMMON -> 1;
                                            case UNCOMMON -> 2;
                                            case RARE -> 3;
                                            case VERY_RARE -> 6;
                                        };

                                        if (flag) {
                                            k3 = Math.max(1, k3 / 2);
                                        }

                                        i += k3 * j2;
                                        if (itemstack.getCount() > 1) {
                                            i = 40;
                                        }
                                    }
                                }
                            }

                            if (flag3 && !flag2) {
                                this.resultSlots.setItem(0, ItemStack.EMPTY);
                                this.cost.set(0);
                                return;
                            }
                        }
                    }

                    if (this.itemName != null && !Util.isBlank(this.itemName)) {
                        if (!this.itemName.equals(itemstack.getHoverName().getString())) {
                            k = 1;
                            i += k;
                            zeroSlotStack.setHoverName(Component.literal(this.itemName));
                        }
                    } else if (itemstack.hasCustomHoverName()) {
                        k = 1;
                        i += k;
                        zeroSlotStack.resetHoverName();
                    }
                    if (flag && !zeroSlotStack.isBookEnchantable(firstSlotStack)) zeroSlotStack = ItemStack.EMPTY;

                    this.cost.set(j + i);
                    if (i <= 0) {
                        zeroSlotStack = ItemStack.EMPTY;
                    }

                    if (k == i && k > 0 && this.cost.get() >= 40) {
                        this.cost.set(39);
                    }

                    if (this.cost.get() >= 40 && !this.player.getAbilities().instabuild) {
                        zeroSlotStack = ItemStack.EMPTY;
                    }

                    if (!zeroSlotStack.isEmpty()) {
                        int k2 = zeroSlotStack.getBaseRepairCost();
                        if (!firstSlotStack.isEmpty() && k2 < firstSlotStack.getBaseRepairCost()) {
                            k2 = firstSlotStack.getBaseRepairCost();
                        }

                        if (k != i || k == 0) {
                            k2 = calculateIncreasedRepairCost(k2);
                        }

                        zeroSlotStack.setRepairCost(k2);
                        EnchantmentHelper.setEnchantments(map, zeroSlotStack);
                    }

                    this.resultSlots.setItem(0, zeroSlotStack);
                    this.broadcastChanges();
                }
            }
        }
    }

    public static int calculateIncreasedRepairCost(int i) {
        return i * 2 + 1;
    }
    public boolean setItemName(String s1) {
        String s = validateName(s1);
        if (s != null && !s.equals(this.itemName)) {
            this.itemName = s;
            if (this.getSlot(2).hasItem()) {
                ItemStack itemstack = this.getSlot(2).getItem();
                if (Util.isBlank(s)) {
                    itemstack.resetHoverName();
                } else {
                    itemstack.setHoverName(Component.literal(s));
                }
            }

            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private static String validateName(String s1) {
        String s = SharedConstants.filterText(s1);
        return s.length() <= 50 ? s : null;
    }
    public int getCost() {
        return this.cost.get();
    }
}
