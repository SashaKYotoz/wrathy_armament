package net.sashakyotoz.wrathy_armament.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemWithDescription extends Item {
    private final String description;
    public ItemWithDescription(Properties properties,String s) {
        super(properties);
        description = s;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!description.isEmpty())
            list.add(Component.translatable(description).withStyle(ChatFormatting.DARK_AQUA));
    }
}
