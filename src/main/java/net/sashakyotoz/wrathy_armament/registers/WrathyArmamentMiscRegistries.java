package net.sashakyotoz.wrathy_armament.registers;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.entities.recipes.WorldshardWorkbenchRecipe;
import net.sashakyotoz.wrathy_armament.blocks.gui.WorldshardWorkbenchMenu;
import net.sashakyotoz.wrathy_armament.client.particles.options.CapturedSoulParticleOption;
import net.sashakyotoz.wrathy_armament.client.particles.options.FireSphereParticleOption;
import net.sashakyotoz.wrathy_armament.entities.bosses.*;
import net.sashakyotoz.wrathy_armament.miscs.enchants.NightmareJumping;
import net.sashakyotoz.wrathy_armament.miscs.enchants.PhantomFury;
import net.sashakyotoz.wrathy_armament.miscs.enchants.Phantoquake;
import net.sashakyotoz.wrathy_armament.utils.data.loot.AddItemModifier;
import net.sashakyotoz.wrathy_armament.utils.data.loot.AddSusSandItemModifier;

import java.util.function.Function;

public class WrathyArmamentMiscRegistries {
    //particles
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<SimpleParticleType> PHANTOM_RAY = PARTICLE_TYPES.register("phantom_ray", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ZENITH_WAY = PARTICLE_TYPES.register("zenith_way", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<FireSphereParticleOption>> FIRE_SPHERE = registerParticle("fire_sphere", true, FireSphereParticleOption.DESERIALIZER, (fireSphereParticleType) -> FireSphereParticleOption.CODEC);
    public static final RegistryObject<SimpleParticleType> FROST_SOUL_RAY = PARTICLE_TYPES.register("frostsoul_ray", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BEAM_SPARKLES = PARTICLE_TYPES.register("beam_sparkles", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<CapturedSoulParticleOption>> CAPTURED_SOUL = registerParticle("captured_soul", true, CapturedSoulParticleOption.DESERIALIZER, (soul) -> CapturedSoulParticleOption.CODEC);

    public static <T extends ParticleOptions> RegistryObject<ParticleType<T>> registerParticle(String pKey, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
        return PARTICLE_TYPES.register(pKey, () -> new ParticleType<>(pOverrideLimiter, pDeserializer) {
            @Override
            public Codec<T> codec() {
                return pCodecFactory.apply(this);
            }
        });
    }

    //enchantments
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, WrathyArmament.MODID);
    public static final RegistryObject<Enchantment> PHANTOM_FURY = ENCHANTMENTS.register("phantom_fury", () -> new PhantomFury(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> NIGHTMARE_JUMPING = ENCHANTMENTS.register("nightmare_jumping", () -> new NightmareJumping(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> PHANTOQUAKE = ENCHANTMENTS.register("phantoquake", () -> new Phantoquake(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //menus
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<MenuType<WorldshardWorkbenchMenu>> WORLDSHARD_WORKBENCH = MENUS.register("worldshard_workbench", () -> IForgeMenuType.create(WorldshardWorkbenchMenu::new));
    //entity data serializers
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, WrathyArmament.MODID);
    public static final RegistryObject<EntityDataSerializer<JohannesKnight.KnightPose>> KNIGHT_POSE = SERIALIZER.register("knight_pose", () -> EntityDataSerializer.simpleEnum(JohannesKnight.KnightPose.class));
    public static final RegistryObject<EntityDataSerializer<MoonLord.LordPose>> LORD_POSE = SERIALIZER.register("lord_pose", () -> EntityDataSerializer.simpleEnum(MoonLord.LordPose.class));
    public static final RegistryObject<EntityDataSerializer<SashaKYotoz.SashaKYotozPhase>> SASHAKYOTOZ_PHASE = SERIALIZER.register("sashakyotoz_phase", () -> EntityDataSerializer.simpleEnum(SashaKYotoz.SashaKYotozPhase.class));
    public static final RegistryObject<EntityDataSerializer<Habciak.HabciakPose>> HABCIAK_POSE = SERIALIZER.register("habciak_pose", () -> EntityDataSerializer.simpleEnum(Habciak.HabciakPose.class));
    public static final RegistryObject<EntityDataSerializer<LichKing.KingPose>> LICH_KING_POSE = SERIALIZER.register("lich_king_pose", () -> EntityDataSerializer.simpleEnum(LichKing.KingPose.class));
    //mob effects
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WrathyArmament.MODID);
    public static final RegistryObject<MobEffect> BRIGHTNESS = EFFECTS.register("brightness", () -> new MobEffect(MobEffectCategory.HARMFUL, 0xffffff).setFactorDataFactory(() -> new MobEffectInstance.FactorData(22)));
    //loot table modifiers
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, WrathyArmament.MODID);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_SUS_SAND_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_sus_sand_item", AddSusSandItemModifier.CODEC);
    //recipes
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, WrathyArmament.MODID);
    public static final RegistryObject<RecipeSerializer<WorldshardWorkbenchRecipe>> WORLDSHARD_WORKBENCH_CRAFTING =
            RECIPES.register("worldshard_workbench_crafting", () -> WorldshardWorkbenchRecipe.Serializer.INSTANCE);
    //tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WrathyArmament.MODID);
    public static final RegistryObject<CreativeModeTab> WRATHY_ARMAMENT_TAB = CREATIVE_MODE_TABS.register("wrathy_armament_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.wrathy_armament_tab"))
            .icon(() -> WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get());
                output.accept(WrathyArmamentItems.PHANTOM_LANCER.get());
                output.accept(WrathyArmamentItems.MIRROR_SWORD.get());
                output.accept(WrathyArmamentItems.ZENITH.get());
                output.accept(WrathyArmamentItems.JOHANNES_SWORD.get());
                output.accept(WrathyArmamentItems.MASTER_SWORD.get());
                output.accept(WrathyArmamentItems.BLADE_OF_CHAOS.get());
                output.accept(WrathyArmamentItems.FROSTMOURNE.get());
                output.accept(WrathyArmamentItems.MURASAMA.get());
                output.accept(WrathyArmamentItems.MISTSPLITTER_REFORGED.get());
                output.accept(WrathyArmamentItems.HALF_ZATOICHI.get());
                output.accept(WrathyArmamentItems.BLACKRAZOR.get());
                output.accept(WrathyArmamentItems.COPPER_SWORD.get());
                output.accept(WrathyArmamentItems.MEOWMERE.get());
                output.accept(WrathyArmamentItems.MYTHRIL_INGOT.get());
                output.accept(WrathyArmamentItems.LUNAR_VOODOO_DOLL.get());
                output.accept(WrathyArmamentItems.SHARD_OF_CLEAR_MYTHRIL.get());
                output.accept(WrathyArmamentItems.SHARD_OF_ORICHALCUM.get());
                output.accept(WrathyArmamentItems.SHARD_OF_MECHANVIL.get());
                output.accept(WrathyArmamentItems.SHARD_OF_NETHERNESS.get());
            }).build());

    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
        ENCHANTMENTS.register(bus);
        EFFECTS.register(bus);
        MENUS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        RECIPES.register(bus);
        SERIALIZER.register(bus);
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}