package net.sashakyotoz.wrathy_armament.entities.spawners;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

import java.util.Iterator;

public class SashaKYotozSpawner implements CustomSpawner {
    private int nextTick;
    public SashaKYotozSpawner() {
        WrathyArmament.LOGGER.debug("SashaKYotoz's spawner was set up");
    }

    public int tick(ServerLevel pLevel, boolean pSpawnEnemies, boolean pSpawnFriendlies) {
        if (!pSpawnEnemies) {
            return 0;
        } else {
            ServerPlayer serverplayer;
            RandomSource randomsource = pLevel.random;
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick += (60 + randomsource.nextInt(60)) * 20;
                if (pLevel.getSkyDarken() < 5 && pLevel.dimensionType().hasSkyLight()) {
                    return 0;
                } else {
                    int i = 0;
                    Iterator<ServerPlayer> var6 = pLevel.players().iterator();
                    while (true) {
                        DifficultyInstance difficultyinstance;
                        BlockPos blockpos1;
                        BlockState blockstate;
                        FluidState fluidstate;
                        do {
                            BlockPos blockpos;
                            int j;
                            do {
                                do {
                                    do {
                                        do {
                                            if (!var6.hasNext()) {
                                                return i;
                                            }
                                            serverplayer = var6.next();
                                        } while (serverplayer.isSpectator());

                                        blockpos = serverplayer.blockPosition();
                                    } while (pLevel.dimensionType().hasSkyLight() && (blockpos.getY() < pLevel.getSeaLevel() || !pLevel.canSeeSky(blockpos)));

                                    difficultyinstance = pLevel.getCurrentDifficultyAt(blockpos);
                                } while (!difficultyinstance.isHarderThan(randomsource.nextFloat() * 3.0F));

                                ServerStatsCounter serverstatscounter = serverplayer.getStats();
                                j = Mth.clamp(serverstatscounter.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
                            } while (randomsource.nextInt(j) < 96000);

                            blockpos1 = blockpos.above(20 + randomsource.nextInt(15)).east(-10 + randomsource.nextInt(21)).south(-10 + randomsource.nextInt(21));
                            blockstate = pLevel.getBlockState(blockpos1);
                            fluidstate = pLevel.getFluidState(blockpos1);
                        } while (!NaturalSpawner.isValidEmptySpawnBlock(pLevel, blockpos1, blockstate, fluidstate, WrathyArmamentEntities.SASHAKYOTOZ.get()));
                        SpawnGroupData spawngroupdata = null;
                        SashaKYotoz sashaKYotoz = WrathyArmamentEntities.SASHAKYOTOZ.get().create(pLevel);
                        if (sashaKYotoz != null) {
                            sashaKYotoz.moveTo(blockpos1, 0.0F, 0.0F);
                            spawngroupdata = sashaKYotoz.finalizeSpawn(pLevel, difficultyinstance, MobSpawnType.NATURAL, spawngroupdata, null);
                            pLevel.addFreshEntityWithPassengers(sashaKYotoz);
                            serverplayer.getStats().setValue(serverplayer,Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);
                            if (serverplayer != null && !serverplayer.isSpectator())
                                serverplayer.displayClientMessage(Component.translatable("boss.wrathy_armament.sashakyotoz.appeared"),true);
                            ++i;
                        }
                    }
                }
            }
        }
    }
}