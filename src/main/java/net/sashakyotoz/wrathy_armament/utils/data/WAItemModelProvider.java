package net.sashakyotoz.wrathy_armament.utils.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

public class WAItemModelProvider extends ItemModelProvider {
    public WAItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WrathyArmament.MODID, existingFileHelper);
    }
    @Override
    protected void registerModels() {
        simpleMaterialItem(WrathyArmamentItems.MYTHRIL_INGOT);
        simpleMaterialItem(WrathyArmamentItems.SHARD_OF_CLEAR_MYTHRIL);
        simpleMaterialItem(WrathyArmamentItems.SHARD_OF_MECHANVIL);
        simpleMaterialItem(WrathyArmamentItems.SHARD_OF_ORICHALCUM);
        simpleMaterialItem(WrathyArmamentItems.SHARD_OF_NETHERNESS);
        simpleMaterialItem(WrathyArmamentItems.LUNAR_VOODOO_DOLL);
    }
    private ItemModelBuilder simpleMaterialItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(WrathyArmament.MODID,"item/materials/" + item.getId().getPath()));
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