package net.satisfy.beachparty.block.furnitureblocks;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.block.entity.NewRadioBlockEntity;
import net.satisfy.beachparty.registry.EntityTypeRegistry;
import net.satisfy.beachparty.registry.SoundEventRegistry;
import net.satisfy.beachparty.util.BeachpartyUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRadioBlock extends BaseEntityBlock {

    public static final int CHANNELS = SoundEventRegistry.RADIO_SOUNDS.size();
    public static final int DELAY = 2 * 20;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ON = BooleanProperty.create("on");

    private static final VoxelShape SHAPE = Block.box(0.125, 0, 0.3125, 0.875, 0.5, 0.6875);
    private static final Map<Direction, VoxelShape> SHAPE_MAP = Util.make(new HashMap<>(), map -> {
        for (Direction d : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(d, BeachpartyUtil.rotateShape(Direction.NORTH, d, SHAPE));
        }
    });

    private final BlockState DEFAULT_PLACEMENT_BLOCKSTATE = defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ON, false);

    public NewRadioBlock(Properties properties) {
        super(properties);
        registerDefaultState(DEFAULT_PLACEMENT_BLOCKSTATE);
    }

    // START BLOCKSTATE
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ON);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return DEFAULT_PLACEMENT_BLOCKSTATE;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_MAP.get(blockState.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
    // END BLOCKSTATE

    // START BLOCK ENTITY
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return EntityTypeRegistry.NEW_RADIO_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return BaseEntityBlock.createTickerHelper(blockEntityType, EntityTypeRegistry.NEW_RADIO_BLOCK_ENTITY.get(), NewRadioBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {

        if (blockState.is(newState.getBlock())) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof NewRadioBlockEntity radioBlockEntity) {
            radioBlockEntity.stopPlayingOnRemove();
            level.removeBlockEntity(blockPos);
        }

        super.onRemove(blockState, level, blockPos, newState, isMoving);
    }
    // END BLOCK ENTITY

    // START CLIENT HANDLING
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

    private static void spawnParticles(Level world, BlockPos pos) {
        RandomSource random = world.random;

        double d = (double) pos.getX() + random.nextDouble();
        double e = pos.getY() + 0.5 + random.nextDouble();
        double f = (double) pos.getZ() + random.nextDouble();

        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();

        world.addParticle(ParticleTypes.NOTE, d, e, f, red, green, blue);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(ON)) {
            spawnParticles(level, blockPos);
        }
    }
    // END CLIENT HANDLING

    // START INTERACTION HANDLING

    // END INTERACTION HANDLING
}
