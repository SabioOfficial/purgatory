package net.sabio.purgatory.mod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.sabio.purgatory.mod.registry.ModBlocks;
import net.sabio.purgatory.mod.registry.ModItems;

public class StrippedPurgedLogBlock extends RotatedPillarBlock {
    private static final int REVERT_DELAY_TICKS = 6000;

    public StrippedPurgedLogBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (movedByPiston || oldState.getBlock() == state.getBlock() || level.isClientSide()) {
            return;
        }

        RandomSource random = level.getRandom();
        if (random.nextBoolean()) {
            Block.popResource(level, pos, new ItemStack(ModItems.SINFUL_DUST));
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.scheduleTick(pos, this, REVERT_DELAY_TICKS);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockState(pos).getBlock() == this) {
            level.setBlock(pos, ModBlocks.PURGED_LOG.defaultBlockState().setValue(AXIS, state.getValue(AXIS)), 3);
        }
    }
}
