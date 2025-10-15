package net.sashakyotoz.wrathy_armament.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
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
import net.sashakyotoz.wrathy_armament.blocks.entities.recipes.SimpleCraftingContainer;
import net.sashakyotoz.wrathy_armament.blocks.entities.recipes.WorldshardWorkbenchRecipe;
import net.sashakyotoz.wrathy_armament.blocks.gui.WorldshardWorkbenchMenu;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlockEntities;
import net.sashakyotoz.wrathy_armament.utils.capabilities.items.XPTiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WorldshardWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    public int progress = 0;
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
        Containers.dropContents(this.getLevel(), this.worldPosition, inventory);
    }

    public void tick(Level level, BlockPos pos, BlockState state, WorldshardWorkbenchBlockEntity blockEntity) {
        ItemStack mainStack = blockEntity.itemHandler.getStackInSlot(0);
        if (level.isClientSide()) {
            if (mainStack.getItem() instanceof SwordLikeItem) {
                if (mainStack.getOrCreateTag().getInt("Sparkles") < 5) {
                    if (mainStack.getOrCreateTag().getInt("CombatExperience") > XPTiers.values()[mainStack.getOrCreateTag().getInt("Sparkles")].getNeededXP()) {
                        int remainingUseTicks = blockEntity.maxProgress - blockEntity.progress;
                        float sin = (float) Math.sin(remainingUseTicks * Math.PI / 10);
                        float cos = (float) Math.cos(remainingUseTicks * Math.PI / 10);
                        if (blockEntity.progress % 15 == 0)
                            level.playLocalSound(pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.5f, 0.75f, true);
                        level.addParticle(ParticleTypes.END_ROD,
                                pos.getX() + 0.5f + sin, pos.getY() + 1.1f, pos.getZ() + 0.5f + cos, 0, 0, 0);
                    }
                }
            }
            if (hasRecipe()) {
                if (blockEntity.progress % 15 == 0)
                    level.playLocalSound(pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.5f, 0.75f, true);
            }
        }
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
        if (hasRecipe() || (!isCraftingAction && mainStack.getItem() instanceof SwordLikeItem)) {
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
                serverLevel.sendParticles(new ColorableParticleOption("sparkle", colors[0], colors[1], colors[2]), pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f, 2, 0, 0, 0, 0.5f);
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
        if (getCurrentRecipe().isEmpty())
            return -1;
        else {
            Optional<WorldshardWorkbenchRecipe> matchedRecipe = getCurrentRecipe();
            ItemStack stackToIdentifyModel = matchedRecipe.get().getResultItem(null);
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
        Optional<WorldshardWorkbenchRecipe> matchedRecipe = getCurrentRecipe();
        if (matchedRecipe.isPresent()) {
            ItemStack result = matchedRecipe.get().getResultItem(pEntity.getLevel().registryAccess());
            for (int i = 0; i < 9; i++) {
                pEntity.itemHandler.extractItem(i, 1, false);
            }
            this.itemHandler.setStackInSlot(9, new ItemStack(result.getItem(),
                    this.itemHandler.getStackInSlot(9).getCount() + result.getCount()));
            pEntity.resetProgress();
        }
    }

    public boolean hasRecipe() {
        Optional<WorldshardWorkbenchRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<WorldshardWorkbenchRecipe> getCurrentRecipe() {
        SimpleCraftingContainer inventory = new SimpleCraftingContainer(3, 3, this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        return this.getLevel().getRecipeManager().getRecipeFor(WorldshardWorkbenchRecipe.Type.INSTANCE, inventory, this.getLevel());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(9).isEmpty() || this.itemHandler.getStackInSlot(9).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(9).getCount() + count <= this.itemHandler.getStackInSlot(9).getMaxStackSize();
    }
}