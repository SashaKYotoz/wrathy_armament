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
import net.sashakyotoz.wrathy_armament.entities.alive.Guide;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;
import net.sashakyotoz.wrathy_armament.entities.bosses.*;
import net.sashakyotoz.wrathy_armament.entities.technical.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WrathyArmamentEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<EntityType<ZenithEntity>> ZENITH = register("zenith_entity", EntityType.Builder.<ZenithEntity>of(ZenithEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(1f, 1f));
    public static final RegistryObject<EntityType<BladeOfChaosEntity>> BLADE_OF_CHAOS = register("blade_of_chaos_entity", EntityType.Builder.<BladeOfChaosEntity>of(BladeOfChaosEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(1f, 1f));
    public static final RegistryObject<EntityType<ParticleLikeEntity>> PARTICLE_LIKE_ENTITY = register("particle_like_entity", EntityType.Builder.<ParticleLikeEntity>of(ParticleLikeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(2f, 0.2f));
    public static final RegistryObject<EntityType<HarmfulProjectileEntity>> HARMFUL_PROJECTILE_ENTITY = register("harmful_projectile_entity", EntityType.Builder.<HarmfulProjectileEntity>of(HarmfulProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setUpdateInterval(20).setTrackingRange(12).sized(0.6f, 0.6f));
    public static final RegistryObject<EntityType<JohannesSpearEntity>> JOHANNES_SPEAR = register("johannes_spear", EntityType.Builder.<JohannesSpearEntity>of(JohannesSpearEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.7f, 1.9f));
    public static final RegistryObject<EntityType<SashaKYotoz>> SASHAKYOTOZ = register("sashakyotoz", EntityType.Builder.of(SashaKYotoz::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).sized(0.7f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<Habciak>> HABCIAK = register("habciak", EntityType.Builder.of(Habciak::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).sized(0.7f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<LichKing>> LICH_KING = register("lich_king", EntityType.Builder.of(LichKing::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).sized(1.6f, 2.8f));
    public static final RegistryObject<EntityType<JohannesKnight>> JOHANNES_KNIGHT = register("johannes_knight", EntityType.Builder.of(JohannesKnight::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).sized(1.5f, 2.6f));
    public static final RegistryObject<EntityType<LichMyrmidon>> LICH_MYRMIDON = register("lich_myrmidon", EntityType.Builder.of(LichMyrmidon::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(1f, 1.8f));

    public static final RegistryObject<EntityType<MoonLord>> MOON_LORD = register("moon_lord", EntityType.Builder.of(MoonLord::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).sized(2f, 3.5f).fireImmune());
    public static final RegistryObject<EntityType<Guide>> THE_GUIDE = register("guide", EntityType.Builder.of(Guide::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).sized(0.6F, 1.8F));
    public static final RegistryObject<EntityType<TrueEyeOfCthulhu>> TRUE_EYE_OF_CTHULHU = register("true_eye_of_cthulhu", EntityType.Builder.of(TrueEyeOfCthulhu::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).sized(1.25F, 1.25F));
    public static final RegistryObject<EntityType<EyeOfCthulhuProjectile>> EYE_OF_CTHULHU_PROJECTILE = register("eye_of_cthulhu_projectile",  EntityType.Builder.<EyeOfCthulhuProjectile>of(EyeOfCthulhuProjectile::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).setUpdateInterval(20).sized(0.65F, 0.65F));
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryName, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(registryName, () -> entityTypeBuilder.build(registryName));
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SASHAKYOTOZ.get(), SashaKYotoz.createAttributes().build());
        event.put(HABCIAK.get(), Habciak.createAttributes().build());
        event.put(LICH_KING.get(), LichKing.createAttributes().build());
        event.put(JOHANNES_KNIGHT.get(), JohannesKnight.createAttributes().build());
        event.put(MOON_LORD.get(), MoonLord.createAttributes().build());
        event.put(TRUE_EYE_OF_CTHULHU.get(), TrueEyeOfCthulhu.createAttributes().build());
        event.put(LICH_MYRMIDON.get(), LichMyrmidon.createAttributes().build());
        event.put(THE_GUIDE.get(),Guide.createAttributes().build());
    }
}