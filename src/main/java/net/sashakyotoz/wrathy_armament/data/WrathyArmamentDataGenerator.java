package net.sashakyotoz.wrathy_armament.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = WrathyArmament.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WrathyArmamentDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(true,new WrathyArmamentLangProvider(packOutput,"en_us",false));
        generator.addProvider(true,new WrathyArmamentRecipeProvider(packOutput));
        generator.addProvider(true, new WrathyArmamentItemModelProvider(packOutput, existingFileHelper));
    }
}
