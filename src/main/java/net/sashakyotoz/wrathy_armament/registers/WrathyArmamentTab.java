package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class WrathyArmamentTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WrathyArmament.MODID);
    public static final RegistryObject<CreativeModeTab> WRATHY_ARMAMENT_TAB = CREATIVE_MODE_TABS.register("wrathy_armament_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.wrathy_armament_tab"))
            .icon(() -> WrathyArmamentItems.PHANTOM_LANCER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
            output.accept(WrathyArmamentItems.PHANTOM_LANCER.get());
            output.accept(WrathyArmamentItems.ZENITH.get());
            output.accept(WrathyArmamentItems.JOHANNES_SWORD.get());
            output.accept(WrathyArmamentItems.MASTER_SWORD.get());
            output.accept(WrathyArmamentItems.BLADE_OF_CHAOS.get());
            output.accept(WrathyArmamentItems.FROSTMOURNE.get());
            output.accept(WrathyArmamentItems.MURASAMA.get());
            output.accept(WrathyArmamentItems.MISTSPLITTER_REFORGED.get());
            output.accept(WrathyArmamentItems.HALF_ZATOICHI.get());
            output.accept(WrathyArmamentItems.COPPER_SWORD.get());
            output.accept(WrathyArmamentItems.MEOWMERE.get());
            output.accept(WrathyArmamentItems.MYTHRIL_INGOT.get());
            }).build());
}
