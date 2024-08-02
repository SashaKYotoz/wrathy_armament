package net.sashakyotoz.wrathy_armament.entities.spawners;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.alive.Guide;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

import java.util.List;

public class GuideSpawner implements CustomSpawner {
    private int pastTick;

    public GuideSpawner() {
        WrathyArmament.LOGGER.debug("Guide's spawner was set up");
    }

    public int tick(ServerLevel pLevel, boolean pSpawnEnemies, boolean pSpawnFriendlies) {
        while (true) {
            List<ServerPlayer> players = pLevel.getPlayers(Player::onGround);
            for (ServerPlayer player : players) {
                Iterable<BlockPos> posIterator = BlockPos.betweenClosed(player.getOnPos().offset(-16, -16, -16), player.getOnPos().offset(16, 16, 16));
                for (BlockPos pos : posIterator) {
                    if (pLevel.getBlockState(pos).is(BlockTags.DOORS) && pLevel.getCurrentDifficultyAt(pos).getDifficulty() != Difficulty.PEACEFUL) {
                        if (this.pastTick < 24000)
                            pastTick++;
                        else {
                            Guide guide = WrathyArmamentEntities.THE_GUIDE.get().create(pLevel);
                            BlockPos blockpos1 = pos.relative(player.getDirection().getOpposite(), 1);
                            if (guide != null && pLevel.getBlockState(blockpos1.below()).canOcclude() && pLevel.getBlockState(blockpos1.above()).isAir()) {
                                guide.moveTo(blockpos1, 0.0F, 0.0F);
                                pLevel.addFreshEntity(guide);
                                pastTick = 0;
                                break;
                            }
                        }
                    }
                }
            }
            return pastTick;
        }
    }
}