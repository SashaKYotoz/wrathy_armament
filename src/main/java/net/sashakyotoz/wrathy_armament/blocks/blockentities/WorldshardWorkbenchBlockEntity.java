package net.sashakyotoz.wrathy_armament.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;
import net.sashakyotoz.wrathy_armament.blocks.gui.WorldshardWorkbenchMenu;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlockEntities;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;
import net.sashakyotoz.wrathy_armament.utils.capabilities.items.XPTiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WorldshardWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    private static HashMap<String, List<ItemStack>> recipes = new HashMap<>();
    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 300;


    public WorldshardWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(WrathyArmamentBlockEntities.WORLDSHARD_WORKBENCH.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> WorldshardWorkbenchBlockEntity.this.progress;
                    case 1 -> WorldshardWorkbenchBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> WorldshardWorkbenchBlockEntity.this.progress = value;
                    case 1 -> WorldshardWorkbenchBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    /**
     * <p>String name - key of recipe
     * <p> First item in list have to instead SmithingTemplateItem if type of action is to craft
     * <p> If list contains only 3 items and first item is material - type of action is to upgrade
     */
    public static void addRecipe(String name, List<ItemStack> itemStacks) {
        if (itemStacks.size() > 10)
            throw new IllegalArgumentException("Worldshard Workbench recipes error: List for recipe can't exceed 9 items");
        recipes.put(name, itemStacks);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.wrathy_armament.worldshard_workbench");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("menus", itemHandler.serializeNBT());
        nbt.putInt("workbench.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("menus"));
        progress = nbt.getInt("workbench.progress");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new WorldshardWorkbenchMenu(i, inventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    //recipes system
    public void tick(Level level, BlockPos pos, BlockState state, WorldshardWorkbenchBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        ItemStack mainStack = blockEntity.itemHandler.getStackInSlot(0);
        boolean isCraftingAction = mainStack.getItem() instanceof SmithingTemplateItem;
        float[] colors = new float[3];
        switch (this.getModelVariantForRecipe()) {
            case 0 -> {
                colors[0] = 0.1f;
                colors[1] = 1f;
                colors[2] = 0f;
            }
            case 1 -> {
                colors[0] = 0.1f;
                colors[1] = 0.1f;
                colors[2] = 0.1f;
            }
            case 2 -> {
                colors[0] = 1f;
                colors[1] = 0f;
                colors[2] = 0.75f;
            }
            case 3 -> {
                colors[0] = 0.25f;
                colors[1] = 0.1f;
                colors[2] = 0.1f;
            }
        }
        if (getRecipe() != null || (!isCraftingAction && mainStack.getItem() instanceof SwordLikeItem)) {
            if (isCraftingAction) {
                if (RandomSource.create().nextBoolean())
                    blockEntity.progress++;
            } else {
                if (mainStack.getOrCreateTag().getInt("Sparkles") < 5) {
                    if (mainStack.getOrCreateTag().getInt("CombatExperience") > XPTiers.values()[mainStack.getOrCreateTag().getInt("Sparkles")].getNeededXP())
                        blockEntity.progress++;
                }
            }
            if (blockEntity.progress % 2 == 0 && level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(new ColorableParticleOption("sparkle",colors[0],colors[1],colors[2]), pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f, 2, 0, 0, 0, 0.5f);
            setChanged(level, pos, state);
            if (blockEntity.progress >= blockEntity.maxProgress) {
                if (isCraftingAction)
                    craftItem(blockEntity);
                else {
                    ItemStack modifiedStack = mainStack.copy();
                    modifiedStack.getOrCreateTag().putInt("CombatExperience", modifiedStack.getOrCreateTag().getInt("CombatExperience") - XPTiers.values()[modifiedStack.getOrCreateTag().getInt("Sparkles")].getNeededXP());
                    modifiedStack.getOrCreateTag().putInt("Sparkles", modifiedStack.getOrCreateTag().getInt("Sparkles") + 1);
                    blockEntity.itemHandler.extractItem(0, 1, false);
                    blockEntity.itemHandler.insertItem(9, modifiedStack, false);
                    blockEntity.resetProgress();
                }
            }
        } else {
            blockEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public ItemStack stackToRender() {
        return this.itemHandler.getStackInSlot(9);
    }

    public int getModelVariantForRecipe() {
        if (getRecipe() == null)
            return -1;
        else {
            List<ItemStack> matchedRecipe = getRecipe();
            ItemStack stackToIdentifyModel = matchedRecipe.get(0);
            if (stackToIdentifyModel.is(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE))
                return 0;
            else if (stackToIdentifyModel.is(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE))
                return 1;
            else if (stackToIdentifyModel.is(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE))
                return 2;
            else if (stackToIdentifyModel.is(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE))
                return 3;
        }
        return -1;
    }

    private void craftItem(WorldshardWorkbenchBlockEntity pEntity) {
        List<ItemStack> matchedRecipe = getRecipe();
        if (matchedRecipe != null) {
            for (int i = 0; i < 9; i++) {
                pEntity.itemHandler.extractItem(i, 1, false);
            }
            pEntity.itemHandler.insertItem(9, matchedRecipe.get(9), false);
            pEntity.resetProgress();
        }
    }

    public List<ItemStack> getRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        List<ItemStack> itemsInSlots = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            itemsInSlots.add(inventory.getItem(i));
        }
        for (Map.Entry<String, List<ItemStack>> entry : recipes.entrySet()) {
            int count = 0;
            for (int i = 0; i < entry.getValue().size() - 1; i++) {
                if (ItemStack.matches(entry.getValue().get(i), itemsInSlots.get(i)))
                    count++;
            }
            if (count >= 9)
                return entry.getValue();
        }
        return null;
    }
}