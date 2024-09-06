package net.satisfy.beachparty.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.beachparty.registry.EntityTypeRegistry;

public class NewRadioBlockEntity extends BlockEntity {

    private boolean playing = false;

    public NewRadioBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.NEW_RADIO_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, NewRadioBlockEntity newRadioBlockEntity) {
    }

    public void stopPlayingOnRemove() {
        if (this.playing) {
            this.stopPlaying();
        }
    }

    private void stopPlaying() {

        if (!this.playing) {
            return;
        }

        this.playing = false;

        if (this.level == null) {
            return;
        }

        final Level level = this.level;
        final BlockPos blockPos = this.worldPosition;
        final BlockState blockState = this.getBlockState();

        level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, blockPos, GameEvent.Context.of(blockState));
        level.levelEvent(LevelEvent.SOUND_STOP_JUKEBOX_SONG, blockPos, 0);
        level.updateNeighborsAt(blockPos, blockState.getBlock());
        this.setChanged();
    }
}
