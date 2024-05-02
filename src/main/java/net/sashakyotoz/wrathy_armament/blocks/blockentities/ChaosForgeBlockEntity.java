package net.sashakyotoz.wrathy_armament.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.blocks.ChaosForge;
import net.sashakyotoz.wrathy_armament.blocks.gui.ChaosForgeMenu;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ChaosForgeBlockEntity extends BlockEntity implements MenuProvider {
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);

    public ChaosForgeBlockEntity(BlockPos pos, BlockState state) {
        super(WrathyArmamentBlockEntities.CHAOS_FORGE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.wrathy_armament.chaos_forge");
    }

    public void tick(Level level, BlockPos pPos, BlockState state) {
        if (level.getBlockState(pPos.below()).is(BlockTags.FIRE) && state.getValue(ChaosForge.FIRING) < 3) {
            RandomSource random = RandomSource.create();
            if (Math.random() > 0.975){
                if (random.nextBoolean() && random.nextDouble() > 0.975)
                    level.setBlock(pPos, state.setValue(ChaosForge.FIRING, state.getValue(ChaosForge.FIRING) + 1), 3);
            }
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);

    }
    public int getContainerSize() {
        return stacks.size();
    }
    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.stacks);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ChaosForgeMenu(i, inventory);
    }
}
