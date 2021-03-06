package com.shynieke.statues.blocks.statues;

import com.shynieke.statues.blocks.AbstractStatueBase;
import com.shynieke.statues.init.StatueRegistry;
import com.shynieke.statues.recipes.StatueLootList;
import com.shynieke.statues.tiles.StatueTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BabyZombieStatueBlock extends AbstractStatueBase {
	private static final VoxelShape SHAPE = Block.makeCuboidShape(6.5D, 0.0D, 6.5D, 9.5D, 8.5D, 9.5D);

	public BabyZombieStatueBlock(Block.Properties builder) {
		super(builder.sound(SoundType.STONE));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if(this.isDecorative(state))
		{
			BlockState blockState = worldIn.getBlockState(pos.down());
			if (blockState.getBlock() == Blocks.LAPIS_BLOCK) {
				worldIn.addParticle(ParticleTypes.EXPLOSION, pos.down().getX(), pos.down().getY(), pos.down().getZ(), 0.0D, 0.0D, 0.0D);
				worldIn.setBlockState(pos.down(), StatueRegistry.FLOOD_STATUE.get().getDefaultState().with(HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite()));
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
				worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			if (blockState.getBlock() == StatueRegistry.CHICKEN_STATUE.get() && isDecorative(blockState)) {
				worldIn.addParticle(ParticleTypes.EXPLOSION, pos.down().getX(), pos.down().getY(), pos.down().getZ(), 0.0D, 0.0D, 0.0D);
				worldIn.setBlockState(pos.down(), StatueRegistry.CHICKEN_JOCKEY_STATUE.get().getDefaultState().with(HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite()));
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
				worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	public boolean isDecorative(BlockState state) {
		return !state.get(INTERACTIVE).booleanValue();
	}

	@Override
	public void executeStatueBehavior(StatueTile tile, BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult result) {
		tile.giveItem(StatueLootList.getLootInfo(getLootName()).getLoot(), playerIn);

//		tile.FakeMobs(new FakeZombie(worldIn), worldIn, pos, true);
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public String getLootName() {
		return "baby_zombie";
	}

	@Override
	public EntityType<?> getEntity() {
		return EntityType.ZOMBIE;
	}

	@Override
	public SoundEvent getSound(BlockState state) {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
