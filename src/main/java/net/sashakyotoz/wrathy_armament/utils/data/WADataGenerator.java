package net.sashakyotoz.wrathy_armament.utils.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

@Mod.EventBusSubscriber(modid = WrathyArmament.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WADataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(true,new WALangProvider(packOutput,"en_us",false));
        generator.addProvider(true,new WARecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), new WAGlobalLootModifiersProvider(packOutput));
        generator.addProvider(true, new WAItemModelProvider(packOutput, existingFileHelper));
    }
}