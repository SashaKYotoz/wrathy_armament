package net.sashakyotoz.wrathy_armament.utils;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.particles.options.CapturedSoulParticleOption;
import net.sashakyotoz.wrathy_armament.entities.technical.BladeOfChaosEntity;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.*;
import net.sashakyotoz.wrathy_armament.utils.capabilities.HalfZatoichiAbilityCapability;
import net.sashakyotoz.wrathy_armament.utils.capabilities.MistsplitterDefenseCapability;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;
import org.antlr.v4.runtime.misc.Triple;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber
public class OnActionsTrigger {
    public static HashMap<String, Triple<Integer, Integer, Integer>> playerCameraData = new HashMap<>();
    public static Vec3 vec3 = new Vec3(0, -256, 0);
    private static final UUID HEALTH_MODIFIER = UUID.fromString("2656de64-a009-47c8-8d53-cc0919b59bc9");
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END) {
            player.getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(context -> {
                if (player.getAttribute(Attributes.MAX_HEALTH) != null) {
                    if (context.isInAdrenalinMode() && (player.getAttribute(Attributes.MAX_HEALTH).getModifier(HEALTH_MODIFIER) == null))
                        player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                                new AttributeModifier(HEALTH_MODIFIER, "Weapon debuff", -7 +
                                        player.getMainHandItem().getOrCreateTag().getInt("Sparkles"), AttributeModifier.Operation.ADDITION));
                    else if (!context.isInAdrenalinMode() && player.getAttribute(Attributes.MAX_HEALTH).getModifier(HEALTH_MODIFIER) != null)
                        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEALTH_MODIFIER);
                }
            });
            player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(context -> {
                if (context.isDefenseModeOn() && !player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && !player.level().isClientSide())
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 110, 1));
            });
            playerCameraData.forEach((uuid, triple) -> {
                int shakingTime = triple.a;
                int rollingTime = triple.b;
                int rotatingTime = triple.c;

                if (shakingTime > 0) shakingTime--;
                if (rollingTime > 0) rollingTime -= 15;
                if (rotatingTime > 0) rotatingTime -= 15;

                playerCameraData.put(uuid, new Triple<>(shakingTime, rollingTime, rotatingTime));
            });
//            if (player.tickCount % 5 == 0) {
//                if (!player.hasEffect(MobEffects.DIG_SLOWDOWN) && !player.level().isClientSide() && isCloseToProtectedStructure(player))
//                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 300, 1));
//            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof SwordLikeItem swordLikeItem)
            swordLikeItem.leftClickAttack(player, stack);
        if (stack.is(WrathyArmamentItems.MISTSPLITTER_REFORGED.get())) {
            if (event.getEntity().isAlive() && event.getTarget() instanceof LivingEntity target) {
                player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(context -> {
                    if (context.isDefenseModeOn()) {
                        if (target.getMobType() == MobType.UNDEAD && context.getDefenceType().equals("earth"))
                            target.hurt(player.damageSources().playerAttack(player), 1 + stack.getOrCreateTag().getInt("Sparkles"));
                        if (target.getMobType() == MobType.WATER && context.getDefenceType().equals("water"))
                            target.hurt(player.damageSources().playerAttack(player), 1 + stack.getOrCreateTag().getInt("Sparkles"));
                        if (target instanceof FlyingMob && context.getDefenceType().equals("air"))
                            target.hurt(player.damageSources().playerAttack(player), 1 + stack.getOrCreateTag().getInt("Sparkles"));
                        if (target.fireImmune() && context.getDefenceType().equals("fire"))
                            target.hurt(player.damageSources().playerAttack(player), 1 + stack.getOrCreateTag().getInt("Sparkles"));
                        if (target.getMobType() == MobType.UNDEFINED && context.getDefenceType().equals("elemental"))
                            target.hurt(player.damageSources().playerAttack(player), 1 + stack.getOrCreateTag().getInt("Sparkles"));
                        stack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                        if (player.getRandom().nextFloat() > 0.875)
                            context.setDefenseModeFlag(false);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getItemStack().getItem() instanceof SwordLikeItem item)
            item.leftClickAttack(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onEntityDiesEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.getMainHandItem().is(WrathyArmamentItems.HALF_ZATOICHI.get())) {
                if (player.getMainHandItem().getOrCreateTag().getInt("charge") < 3)
                    player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") + 1);
                player.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 1 + player.getMainHandItem().getOrCreateTag().getInt("charge")));
            }
            if (player.getMainHandItem().is(WrathyArmamentItems.FROSTMOURNE.get())) {
                if (player.getMainHandItem().getOrCreateTag().getInt("charge") < 5)
                    player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") + 1);
                CapturedSoulParticleOption option = new CapturedSoulParticleOption(new EntityPositionSource(event.getEntity(), 0), 30, 0.25f, 0.25f, 0.75f);
                if (player.level() instanceof ServerLevel level)
                    level.sendParticles(option, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0);
            }
            if (player.getMainHandItem().is(WrathyArmamentItems.BLACKRAZOR.get())) {
                if (player.getMainHandItem().getOrCreateTag().getInt("charge") < 20)
                    player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") + 1);
                CapturedSoulParticleOption option = new CapturedSoulParticleOption(new EntityPositionSource(event.getEntity(), 0), 30, 0.25f, 0.25f, 0.25f);
                if (player.level() instanceof ServerLevel level) {
                    level.sendParticles(option, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0);
                    level.sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1, player.getZ(), 5, 0, 1, 0, 1);
                    player.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof SwordLikeItem swordLikeItem) {
            if (!event.getEntity().isCrouching())
                swordLikeItem.rightClick(player, stack);
            else
                swordLikeItem.rightClickOnShiftClick(player, stack);
        }
        if (stack.is(WrathyArmamentItems.BLADE_OF_CHAOS.get())) {
            if (player.isCrouching()) {
                playPlayerAnimation(player.level(), player, "blade_ability_switch");
                player.setPose(Pose.FALL_FLYING);
            } else {
                String behavior = BladeOfChaosEntity.PossibleBehavior.values()[stack.getOrCreateTag().getInt("StateIndex")].behaviorName;
                if (behavior.equals(BladeOfChaosEntity.PossibleBehavior.CRUSH.behaviorName))
                    playPlayerAnimation(player.level(), player, "nemean_crush");
                else if (behavior.equals(BladeOfChaosEntity.PossibleBehavior.CYCLONE.behaviorName))
                    playPlayerAnimation(player.level(), player, "crooked_throw_of_blades");
            }
        }
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        if (stack.getItem() instanceof SwordLikeItem swordLikeItem)
            swordLikeItem.rightClickBlock(player, stack);
        if (stack.getEnchantmentLevel(WrathyArmamentMiscRegistries.PHANTOQUAKE.get()) > 0 && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockState(event.getPos().above(3)).isAir() && !event.getEntity().getCooldowns().isOnCooldown(event.getItemStack().getItem())) {
            player.setDeltaMovement(0, 0.5, 0);
            player.playSound(SoundEvents.PHANTOM_BITE);
            player.getCooldowns().addCooldown(event.getItemStack().getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 20, 80));
            addParticles(ParticleTypes.EXPLOSION, level, player.getX(), player.getY(), player.getZ(), 3);
            final Vec3 center = new Vec3(event.getPos().getX(), event.getPos().getY() + 1, event.getPos().getZ());
            List<Entity> entities = event.getLevel().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();
            for (Entity entityIterator : entities) {
                if (!(entityIterator == event.getEntity())) {
                    if (entityIterator instanceof LivingEntity entity) {
                        entity.setSecondsOnFire(20);
                        entity.setDeltaMovement(RandomSource.create().nextInt(0, 2) - 1, 0.75f, RandomSource.create().nextInt(0, 2) - 1);
                        event.getEntity().getCooldowns().addCooldown(event.getItemStack().getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 60, 120));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockRightClick(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.level().isClientSide() && !player.isCreative() && isCloseToProtectedStructure(player)) {
            player.displayClientMessage(Component.translatable("structure.wrathy_armament.protection"), true);
            event.setCanceled(true);
        }
    }

    private static boolean isCloseToProtectedStructure(Player player) {
        if (player.level() instanceof ServerLevel serverLevel && !player.isCreative()) {
            BlockPos pos = serverLevel.findNearestMapStructure(WrathyArmamentTags.Structures.VISIBLE_FOR_MASTER_SWORD, player.getOnPos(), 96, false);
            if (pos != null) {
                double distanceSquared = player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
                double normalizedDistance = Math.min(distanceSquared / 4613, 10.0);
                double lightsValue = 10.0 - normalizedDistance;
                return lightsValue > 8;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getCameraEntity() instanceof Player player && !player.isSpectator()) {
            String playerUUID = player.getUUID().toString();
            playerCameraData.computeIfAbsent(playerUUID, k -> new Triple<>(0, 0, 0));
            Triple<Integer, Integer, Integer> cameraData = playerCameraData.get(playerUUID);
            int shakingTime = cameraData.a;
            int rollingTime = cameraData.b;
            int rotatingTime = cameraData.c;
            if (player.distanceToSqr(vec3) < 16) {
                float delta = Minecraft.getInstance().getFrameTime();
                float ticksExistedDelta = player.tickCount + delta;
                float intensity = 0.025f;
                if (!minecraft.isPaused() && player.level().isClientSide() && shakingTime > 0) {
                    event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                    event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                    event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
                }
            }
            if (!minecraft.isPaused() && player.level().isClientSide()) {
                if (minecraft.options.getCameraType().isFirstPerson() && rollingTime > 0)
                    event.setPitch(-(event.getPitch() + rollingTime));
                if (minecraft.options.getCameraType().isFirstPerson() && rotatingTime > 0)
                    event.setYaw(event.getYaw() + rotatingTime);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onFogColorSetup(ViewportEvent.ComputeFogColor event) {
        if (event.getCamera().getEntity() instanceof LivingEntity entity && entity.hasEffect(WrathyArmamentMiscRegistries.BRIGHTNESS.get())) {
            event.setRed(1);
            event.setGreen(1);
            event.setBlue(1);
        }
    }

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("get_manual").executes(arguments -> {
            if (arguments.getSource().getEntity() instanceof Player player) {
                ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
                CompoundTag tag = new CompoundTag();
                ListTag pages = new ListTag();
                pages.add(StringTag.valueOf("{\"text\":\"Slot indexes:\\ncentral slot - 0\\nabove central slot \\u0020 clockwise, respectively 1-8\"}"));
                pages.add(StringTag.valueOf("[\"\",{\"text\":\"Zenith recipe:\\n0: \"},{\"text\":\"wild_armor_trim_smithing_template\",\"color\":\"dark_green\"},{\"text\":\"\\n1: \",\"color\":\"reset\"},{\"text\":\"mythril_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n2:\",\"color\":\"reset\"},{\"text\":\" copper_sword\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"3: \",\"color\":\"black\"},{\"text\":\"netherite_sword\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"4: \",\"color\":\"black\"},{\"text\":\"diamond_sword\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"5:\",\"color\":\"black\"},{\"text\":\" meowmere\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"6:\",\"color\":\"black\"},{\"text\":\" phantom_lancer\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"7:\",\"color\":\"black\"},{\"text\":\" golden_sword\",\"color\":\"#6AAB73\"},{\"text\":\"\\n8:\",\"color\":\"reset\"},{\"text\":\" mythril_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n \",\"color\":\"reset\"}]"));
                pages.add(StringTag.valueOf("[\"\",{\"text\":\"Blade of chaos recipe:\\n0: \"},{\"text\":\"shaper_armor_trim_smithing_template\",\"color\":\"#6AAB73\"},{\"text\":\"\\n1: \",\"color\":\"reset\"},{\"text\":\"chain\",\"color\":\"#6AAB73\"},{\"text\":\"\\n2:\",\"color\":\"reset\"},{\"text\":\" netherite_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"3: \",\"color\":\"black\"},{\"text\":\"blaze_rod\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"4: \",\"color\":\"black\"},{\"text\":\"nether_star\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"5:\",\"color\":\"black\"},{\"text\":\" deepslate\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"6:\",\"color\":\"black\"},{\"text\":\" blaze_rod\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"7:\",\"color\":\"black\"},{\"text\":\" netherite_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n8:\",\"color\":\"reset\"},{\"text\":\" chain\",\"color\":\"#6AAB73\"},{\"text\":\"\\n \",\"color\":\"reset\"}]"));
                pages.add(StringTag.valueOf("[\"\",{\"text\":\"Mistsplitter\",\"color\":\"black\"},{\"text\":\" recipe:\\n0: \",\"color\":\"reset\"},{\"text\":\"eye_armor_trim_smithing_template\",\"color\":\"#6AAB73\"},{\"text\":\"\\n1: \",\"color\":\"reset\"},{\"text\":\"end_rod\",\"color\":\"#6AAB73\"},{\"text\":\"\\n2:\",\"color\":\"reset\"},{\"text\":\" nether_star\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"3: \",\"color\":\"black\"},{\"text\":\"heart_of_the_sea\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"4: \",\"color\":\"black\"},{\"text\":\"purpur_block\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"5:\",\"color\":\"black\"},{\"text\":\" purpur_block\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"6:\",\"color\":\"black\"},{\"text\":\" end_crystal\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"7:\",\"color\":\"black\"},{\"text\":\" nether_star\",\"color\":\"#6AAB73\"},{\"text\":\"\\n8:\",\"color\":\"reset\"},{\"text\":\" end_rod\",\"color\":\"#6AAB73\"},{\"text\":\"\\n \",\"color\":\"reset\"}]"));
                pages.add(StringTag.valueOf("[\"\",{\"text\":\"Murasama\",\"color\":\"black\"},{\"text\":\" recipe:\\n0: \",\"color\":\"reset\"},{\"text\":\"spire_armor_trim_smithing_template\",\"color\":\"#6AAB73\"},{\"text\":\"\\n1: \",\"color\":\"reset\"},{\"text\":\"blaze_rod\",\"color\":\"#6AAB73\"},{\"text\":\"\\n2:\",\"color\":\"reset\"},{\"text\":\" redstone_block\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"3: \",\"color\":\"black\"},{\"text\":\"ender_eye\",\"color\":\"dark_green\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"4: \",\"color\":\"black\"},{\"text\":\"netherite_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"5:\",\"color\":\"black\"},{\"text\":\" netherite_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"6:\",\"color\":\"black\"},{\"text\":\" end_crystal\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"7:\",\"color\":\"black\"},{\"text\":\" redstone_block\",\"color\":\"#6AAB73\"},{\"text\":\"\\n8:\",\"color\":\"reset\"},{\"text\":\" stick\",\"color\":\"#6AAB73\"},{\"text\":\"\\n \",\"color\":\"reset\"}]"));
                pages.add(StringTag.valueOf("[\"\",{\"text\":\"Zatoichi\",\"color\":\"black\"},{\"text\":\" recipe:\\n0: \",\"color\":\"reset\"},{\"text\":\"raiser_armor_trim_smithing_template\",\"color\":\"#6AAB73\"},{\"text\":\"\\n1: \",\"color\":\"reset\"},{\"text\":\"stick\",\"color\":\"#6AAB73\"},{\"text\":\"\\n2:\",\"color\":\"reset\"},{\"text\":\" ancient_debris\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"3: \",\"color\":\"black\"},{\"text\":\"netherite_ingot\",\"color\":\"dark_green\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"4: \",\"color\":\"black\"},{\"text\":\"netherite_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"5:\",\"color\":\"black\"},{\"text\":\" netherite_ingot\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"6:\",\"color\":\"black\"},{\"text\":\" echo_shard\",\"color\":\"#6AAB73\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"7:\",\"color\":\"black\"},{\"text\":\" shard_of_mechanvil\",\"color\":\"#6AAB73\"},{\"text\":\"\\n8:\",\"color\":\"reset\"},{\"text\":\" stick\",\"color\":\"#6AAB73\"},{\"text\":\"\\n \",\"color\":\"reset\"}]"));

                tag.put("pages", pages);
                tag.putString("title", "Wrathy Armament Manual");
                tag.putString("author", "SashaKYotoz");
                tag.putInt("generation", 1);
                CompoundTag display = new CompoundTag();
                ListTag lore = new ListTag();
                lore.add(StringTag.valueOf("Helper to use worldshard workbench"));
                display.put("Lore", lore);
                tag.put("display", display);
                book.setTag(tag);
                if (!player.getInventory().contains(book))
                    player.spawnAtLocation(book);
            }
            return 0;
        }));
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HalfZatoichiAbilityCapability.class);
        event.register(MistsplitterDefenseCapability.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).isPresent())
                event.addCapability(new ResourceLocation(WrathyArmament.MODID, "properties"), new ModCapabilities());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(oldStore -> event.getOriginal().getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(newStore -> newStore.copyFrom(oldStore)));
            event.getOriginal().getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(oldStore -> event.getOriginal().getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(newStore -> newStore.copyFrom(oldStore)));
        }
    }

    public static double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public static double getYVector(double speed, double pitch) {
        return pitch * (-0.025) * speed;
    }

    public static double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }

    public static void addParticles(ParticleOptions type, Level level, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                level.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }

    public static void addParticlesWithDelay(ParticleOptions type, Level level, double x, double y, double z, float modifier, int delay) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0) {
                int finalI = i;
                queueServerWork(finalI * 5 + delay, () -> level.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(finalI) * 0.25d * modifier, 0.2d, Math.sin(finalI) * 0.25d * modifier));
            }
        }
    }

    public static void playPlayerAnimation(Level level, Player player, String path) {
        if (level.isClientSide()) {
            var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(WrathyArmament.createWALocation("animation"));
            if (animation != null)
                animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(WrathyArmament.createWALocation(path))).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public static float getPowerForTime(int i) {
        float f = (float) i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return f;
    }
}