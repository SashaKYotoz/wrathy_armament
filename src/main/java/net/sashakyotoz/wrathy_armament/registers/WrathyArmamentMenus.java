package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.gui.MythrilAnvilMenu;
import net.sashakyotoz.wrathy_armament.blocks.gui.ChaosForgeMenu;

public class WrathyArmamentMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<MenuType<MythrilAnvilMenu>> MYTHRIL_ANVIL = MENUS.register("mythril_anvil", () -> IForgeMenuType.create(MythrilAnvilMenu::new));
    public static final RegistryObject<MenuType<ChaosForgeMenu>> CHAOS_FORGE = MENUS.register("chaos_forge", () -> IForgeMenuType.create(ChaosForgeMenu::new));
}
