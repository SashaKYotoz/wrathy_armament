package net.sashakyotoz.wrathy_armament.entities.spawners;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.sashakyotoz.wrathy_armament.Config;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

import java.util.List;

public class SashaKYotozSpawner implements CustomSpawner {
    private int pastTick;

    public SashaKYotozSpawner() {
        WrathyArmament.LOGGER.debug("SashaKYotoz's spawner was set up");
    }

    public int tick(ServerLevel pLevel, boolean pSpawnEnemies, boolean pSpawnFriendlies) {
        List<ServerPlayer> playerList = pLevel.getPlayers(ServerPlayer::onGround);
        for (ServerPlayer serverPlayer : playerList) {
            int i = serverPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            if (i < Config.TIME_SINCE_REST_TO_SPAWN_SASHAKYOTOZ.get())
                pastTick++;
            else {
                if (pastTick >= Config.TIME_SINCE_REST_TO_SPAWN_SASHAKYOTOZ.get()) {
                    RandomSource random = serverPlayer.getRandom();
                    BlockPos blockpos = serverPlayer.getOnPos();
                    BlockPos blockpos1 = blockpos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    FluidState fluidstate = pLevel.getFluidState(blockpos1);
                    if (NaturalSpawner.isValidEmptySpawnBlock(pLevel, blockpos1, blockstate, fluidstate, WrathyArmamentEntities.SASHAKYOTOZ.get())) {
                        SashaKYotoz sashaKYotoz = WrathyArmamentEntities.SASHAKYOTOZ.get().create(pLevel);
                        if (sashaKYotoz != null && !serverPlayer.level().getDifficulty().equals(Difficulty.PEACEFUL) && !serverPlayer.level().isDay()) {
                            sashaKYotoz.moveTo(blockpos1, 0.0F, 0.0F);
                            pLevel.addFreshEntity(sashaKYotoz);
                            serverPlayer.getStats().setValue(serverPlayer, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);
                            pastTick = 0;
                            if (!serverPlayer.isSpectator())
                                serverPlayer.displayClientMessage(Component.translatable("boss.wrathy_armament.sashakyotoz.appeared"), true);
                        }
                    }
                } else
                    pastTick++;
            }
        }
        return pastTick;
    }
}