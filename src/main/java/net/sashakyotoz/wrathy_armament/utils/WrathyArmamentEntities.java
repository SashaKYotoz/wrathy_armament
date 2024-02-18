package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.JohannesSpearEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WrathyArmamentEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<EntityType<ZenithEntity>> ZENITH = register("zenith_entity", EntityType.Builder.<ZenithEntity>of(ZenithEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.75f, 0.75f));
    public static final RegistryObject<EntityType<JohannesSpearEntity>> JOHANNES_SPEAR = register("johannes_spear", EntityType.Builder.<JohannesSpearEntity>of(JohannesSpearEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.7f, 1.9f));
    public static final RegistryObject<EntityType<SashaKYotoz>> SASHAKYOTOZ = register("sashakyotoz", EntityType.Builder.<SashaKYotoz>of(SashaKYotoz::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.7f, 1.9f));
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryName, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(registryName, () -> entityTypeBuilder.build(registryName));
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SASHAKYOTOZ.get(), SashaKYotoz.createAttributes().build());
    }
}
