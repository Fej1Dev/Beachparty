package net.satisfy.beachparty.block;

import de.cristelknight.doapi.common.entity.ChairEntity;
import de.cristelknight.doapi.common.registry.DoApiEntityTypes;
import de.cristelknight.doapi.common.util.GeneralUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BeachTowelBlock extends BeachChairBlock {
	public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
	private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.box(0, 0, 0.0625, 0.9375, 0.0625, 0.9375));
		return shape;
	};

	private static final VoxelShape VOXEL_SHAPE = voxelShapeSupplier.get();

	public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
		for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
			map.put(direction, GeneralUtil.rotateShape(Direction.EAST, direction, VOXEL_SHAPE));
		}
	});

	public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Direction direction = getOppositePartDirection(state).getOpposite();
		return SHAPE.get(direction);
	}

	public BeachTowelBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PART, BedPart.FOOT));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockPos blockPos = hit.getBlockPos();
			if (!isOccupied(world, blockPos) && player.getItemInHand(hand).isEmpty()) {
				CustomChairEntity chairEntity = new CustomChairEntity(DoApiEntityTypes.CHAIR.get(), world, blockPos, player);
				chairEntity.moveTo((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.25 - 0.4, (double)blockPos.getZ() + 0.5, 0.0F, 0.0F);
				world.addFreshEntity(chairEntity);
				player.startRiding(chairEntity);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}
	}

	private boolean isOccupied(Level world, BlockPos pos) {
		return !world.getEntitiesOfClass(ChairEntity.class, new AABB(pos)).isEmpty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}

	public static class CustomChairEntity extends ChairEntity {
		private final BlockPos blockPos;
		private final Player player;

		public CustomChairEntity(EntityType<? extends ChairEntity> entityType, Level world, BlockPos blockPos, Player player) {
			super(entityType, world);
			this.blockPos = blockPos;
			this.player = player;
		}

		@Override
		public void ejectPassengers() {
			super.ejectPassengers();
			if (!this.level().isClientSide) {
				this.player.teleportTo(blockPos.getX() + 0.5, blockPos.getY() + 2, blockPos.getZ() + 0.5);
			}
		}
	}
}
