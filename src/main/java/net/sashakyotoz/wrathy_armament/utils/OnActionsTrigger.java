package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.JohannesSpearEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;
import net.sashakyotoz.wrathy_armament.items.MasterSword;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber
public class OnActionsTrigger {
    private static int zenithIndex = 0;
    private static int timer;
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
    public static void onPlayerAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        int damage = 0;
        if (event.getSource().getEntity() instanceof Player player && player.getItemInHand(InteractionHand.MAIN_HAND).getEnchantmentLevel(WrathyArmamentEnchants.NIGHTMARE_JUMPING.get()) > 0) {
            if (Math.random() > 0.75)
                damage = RandomSource.create().nextInt(3, 6);
            player.setPos(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
            player.playSound(WrathyArmamentSounds.ITEM_LANCER_SHOT);
            entity.hurt(new DamageSource(event.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                @Override
                public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                    return Component.translatable("death.attack.wrathy_armament.nightmare_jumping");
                }
            }, event.getAmount() + damage);
            if (entity.isAlive())
                entity.setDeltaMovement(0, RandomSource.create().nextInt(5, 15) / 10f, 0);
        }
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().is(WrathyArmamentItems.MASTER_SWORD.get())) {
            masterSwordAttack(livingEntity.level(), entity, event.getSource().getEntity().getYRot(), event.getSource().getEntity().getXRot());
        }
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get())) {
            bladeOfChaosAttack((LivingEntity) event.getSource().getEntity());
        }
    }
    @SubscribeEvent
    public static void onEntityDiesEvent(LivingDeathEvent event){
        if (event.getSource().getEntity() instanceof Player player && player.getMainHandItem().is(WrathyArmamentItems.HALF_ZATOICHI.get())) {
            if (player.getMainHandItem().getOrCreateTag().getInt("charge")< 3)
                player.getMainHandItem().getOrCreateTag().putInt("charge",player.getMainHandItem().getOrCreateTag().getInt("charge")+1);
            player.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 1 + player.getMainHandItem().getOrCreateTag().getInt("charge")));
        }
    }
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        if (stack.is(WrathyArmamentItems.JOHANNES_SWORD.get()) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            johannesSwordDash(player, stack);
        }
        if (stack.is(WrathyArmamentItems.ZENITH.get()) && level instanceof ServerLevel serverLevel && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            zenithAbility(player, serverLevel);
            if (zenithIndex > 3) {
                stack.getOrCreateTag().putDouble("CustomModelData", 0);
                player.getCooldowns().addCooldown(stack.getItem(), 60);
            } else {
                zenithIndex++;
                timer = 30;
                stack.getOrCreateTag().putDouble("CustomModelData", zenithIndex > 0 ? 1 : 0);
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
        }
        if (stack.is(WrathyArmamentItems.MASTER_SWORD.get()) && stack.getOrCreateTag().getDouble("playerX") != 0){
            WrathyArmament.LOGGER.debug("Coordinate: " + stack.getOrCreateTag().getDouble("playerX"));
            MasterSword.timerToRerecord = 240;
            addParticles(ParticleTypes.GLOW_SQUID_INK,level,player.getX(),player.getY(),player.getZ(),2);
            player.teleportTo(stack.getOrCreateTag().getDouble("playerX"),stack.getOrCreateTag().getDouble("playerY"),stack.getOrCreateTag().getDouble("playerZ"));
            stack.getOrCreateTag().putDouble("playerX",0);
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (timer > 0)
            timer--;
        if (event.phase == TickEvent.Phase.END && player.getInventory().contains(new ItemStack(WrathyArmamentItems.ZENITH.get()))) {
            ItemStack stack = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(new ItemStack(WrathyArmamentItems.ZENITH.get())));
            if (stack.getOrCreateTag().getDouble("CustomModelData") == 1 && timer <= 0)
                stack.getOrCreateTag().putDouble("CustomModelData", 0);
        }
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        if (stack.is(WrathyArmamentItems.JOHANNES_SWORD.get()) && level instanceof ServerLevel serverLevel)
            johannesSwordAbility(player, stack, serverLevel);
        if (stack.getEnchantmentLevel(WrathyArmamentEnchants.PHANTOQUAKE.get()) > 0 && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockState(event.getPos().above(3)).isAir() && !event.getEntity().getCooldowns().isOnCooldown(event.getItemStack().getItem())) {
            player.setDeltaMovement(0, 0.5, 0);
            player.playSound(SoundEvents.PHANTOM_BITE);
            player.getCooldowns().addCooldown(event.getItemStack().getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 20, 80));
            addParticles(ParticleTypes.EXPLOSION,level,player.getX(),player.getY(),player.getZ(),3);
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
    private static double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    private static double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }
    private static void addParticles(SimpleParticleType type, Level world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }
    private static void johannesSwordDash(Player player, ItemStack stack) {
        player.setDeltaMovement(0, 0.5, 0);
        player.getCooldowns().addCooldown(stack.getItem(), 50);
        double speed = 1.5;
        double Yaw = player.getYRot();
        player.setDeltaMovement(new Vec3(getXVector(speed, Yaw), (player.getXRot() * (-0.025)) + 0.25, getZVector(speed, Yaw)));
    }

    private static void masterSwordAttack(Level level, LivingEntity target, double yaw, double xRot) {
        if (target.getRandom().nextBoolean()) {
            if (level instanceof ServerLevel serverLevel) {
                LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(serverLevel);
                entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(target.getX(), target.getY(), target.getZ())));
                serverLevel.addFreshEntity(entityToSpawn);
            }
        } else {
            if (!target.level().isClientSide()) {
                target.setSecondsOnFire(15);
            }
            if (target.level() instanceof ServerLevel serverLevel) {
                int tmp = RandomSource.create().nextInt(5, 11) + 3;
                for (int i = 0; i < tmp; i++) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, target.getX() + getXVector(i, yaw), target.getY() + 1, target.getZ() + getZVector(i, yaw), 27, 1 + getXVector(i, yaw), 1, 1 + getZVector(i, yaw), 1);
                }
            }
        }
    }

    private static void bladeOfChaosAttack(LivingEntity entity) {
        Level level = entity.level();
        float scaling = 0;
        WrathyArmament.LOGGER.debug("blade of chaos attack");
        double d0 = -Mth.sin(entity.getYRot() * ((float) Math.PI / 180F));
        double d1 = Mth.cos(entity.getYRot() * ((float) Math.PI / 180F));
        for (int i1 = 0; i1 < 9; i1++) {
            if (!level.getBlockState(new BlockPos(
                            level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX(),
                            level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY() + 1,
                            level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()))
                    .canOcclude())
                scaling = scaling + 1;
            level.addParticle(WrathyArmamentParticleTypes.FIRE_TRAIL.get(),
                    (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
                    (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY() + 1),
                    (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()), d0,
                    0.1, d1);
            if (i1+1 == 9 && !level.isClientSide()){
                BlockPos pos = new BlockPos(
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(9)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX(),
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(9)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY(),
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(9)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ());
                if (level.getBlockState(pos).isAir())
                    level.setBlock(pos, Blocks.FIRE.defaultBlockState(),3);
                BlockPos pos1 = new BlockPos(
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()-(int) d0,
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY(),
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()+(int) d1);
                if (level.getBlockState(pos1).isAir())
                    level.setBlock(pos1, Blocks.FIRE.defaultBlockState(),3);
                BlockPos pos2 = new BlockPos(
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX() +(int) d0,
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY(),
                        level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()- (int) d1);
                if (level.getBlockState(pos2).isAir())
                    level.setBlock(pos2, Blocks.FIRE.defaultBlockState(),3);
            }
            final Vec3 center = new Vec3(
                    (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
                    (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY() + 1),
                    (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()));
            List<Entity> entityList = entity.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(center))).toList();
            for (Entity entityIterator : entityList) {
                if (!(entityIterator == entity)) {
                    if (entityIterator instanceof LivingEntity livingEntity) {
                        livingEntity.setRemainingFireTicks(10);
                        livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                            @Override
                            public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                                return Component.translatable("death.attack.wrathy_armament.blade_of_chaos_message");
                            }
                        }, 15);
                    }
                }
            }
        }
    }

    private static void zenithAbility(Player player, ServerLevel level) {
        if (zenithIndex > 3)
            zenithIndex = 0;
        else
            zenithIndex++;
        WrathyArmament.LOGGER.debug(zenithIndex + "\r");
        ZenithEntity zenith = new ZenithEntity(level, player, zenithIndex);
        WrathyArmament.LOGGER.debug(zenith.timer + "<- timer ticks");
        zenith.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
        level.addFreshEntity(zenith);
    }

    private static void johannesSwordAbility(Player player, ItemStack stack, ServerLevel level) {
        double Yaw = player.getYRot();
        double d0 = getXVector(3, Yaw);
        double d1 = player.getXRot() * (-0.025);
        double d2 = getZVector(3, Yaw);
        for (int l = 0; l < 16; ++l) {
            createSpellEntity(level, player, d0, d1, d2, l);
        }
    }

    private static void createSpellEntity(ServerLevel level, Player player, double vx, double vy, double vz, int i) {
        level.addFreshEntity(new JohannesSpearEntity(level, player.getX() + vx * i, player.getY() + vy, player.getZ() + vz * i, 0, i, player));
        level.gameEvent(GameEvent.ENTITY_PLACE, new Vec3(player.getX() + vx * i, player.getY() + vy, player.getZ() + vz * i), GameEvent.Context.of(player));
    }
}

