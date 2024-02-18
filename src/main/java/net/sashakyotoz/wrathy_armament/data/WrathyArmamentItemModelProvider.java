package net.sashakyotoz.wrathy_armament.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class WrathyArmamentItemModelProvider extends ItemModelProvider {
    public WrathyArmamentItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WrathyArmament.MODID, existingFileHelper);
    }
    @Override
    protected void registerModels() {
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(WrathyArmament.MODID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleEggItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/template_spawn_egg"));
    }
    public void blockModel(RegistryObject<? extends Block> block) {
        withExistingParent(getName(block.get()), modLoc("block/" + getName(block.get())));
    }

    public String getName(Block block) {
        return block.builtInRegistryHolder().key().location().getPath();
    }
}