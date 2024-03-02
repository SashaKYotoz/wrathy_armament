package net.sashakyotoz.wrathy_armament.registers;

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
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import net.sashakyotoz.wrathy_armament.entities.technical.JohannesSpearEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WrathyArmamentEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<EntityType<ZenithEntity>> ZENITH = register("zenith_entity", EntityType.Builder.<ZenithEntity>of(ZenithEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.75f, 0.75f));
    public static final RegistryObject<EntityType<ParticleLikeEntity>> PARTICLE_LIKE_ENTITY = register("particle_like_entity", EntityType.Builder.<ParticleLikeEntity>of(ParticleLikeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(2f, 0.2f));
    public static final RegistryObject<EntityType<JohannesSpearEntity>> JOHANNES_SPEAR = register("johannes_spear", EntityType.Builder.<JohannesSpearEntity>of(JohannesSpearEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.7f, 1.9f));
    public static final RegistryObject<EntityType<SashaKYotoz>> SASHAKYOTOZ = register("sashakyotoz", EntityType.Builder.of(SashaKYotoz::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.7f, 1.9f));
    public static final RegistryObject<EntityType<LichKing>> LICH_KING = register("lich_king", EntityType.Builder.of(LichKing::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(1.6f, 2.8f));
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryName, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(registryName, () -> entityTypeBuilder.build(registryName));
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SASHAKYOTOZ.get(), SashaKYotoz.createAttributes().build());
        event.put(LICH_KING.get(), LichKing.createAttributes().build());
    }
}