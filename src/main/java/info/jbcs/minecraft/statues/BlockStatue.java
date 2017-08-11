/**
 * Block class for the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
//import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
//import pl.asie.lib.util.ItemUtils;
import pl.asie.lib.AsieLibMod;

import java.util.Random;

public class BlockStatue extends BlockContainer
{
	public BlockStatue(Material material)
	{
		super(material);

		setLightOpacity(0);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return null;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockState(new BlockPos(new Vec3d(x, y, z))).getBlock().getMetaFromState(world.getBlockState(new BlockPos(new Vec3d(x, y, z))));// Metadata(x, y, z);

		if ((meta & 4) == 0)
		{
			setBlockBounds(0.1F, 0F, 0.1F, 0.9F, 2F, 0.9F);
		}
		else
		{
			setBlockBounds(0.1F, -1F, 0.1F, 0.9F, 1F, 0.9F);
		}
	}

	public static boolean isMainBlock(IBlockAccess world, int x, int y, int z)
	{
		return (world.getBlockState(new BlockPos(new Vec3d(x, y, z))).getBlock().getMetaFromState(world.getBlockState(new BlockPos(new Vec3d(x, y, z)))) & 4) == 0;
	}

	public static boolean isStatue(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockState(new BlockPos(new Vec3d(x, y, z))).getBlock() instanceof BlockStatue;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		if (world.isRemote)
			return true;

		if (!isMainBlock(world, x, y, z))
			y--;

		TileEntityStatue statue = (TileEntityStatue) world.getTileEntity(new BlockPos(new Vec3d(x, y, z)));
		if (statue instanceof TileEntityStatue)
			Statues.guiStatue.open(entityplayer, world, x, y, z);

		return true;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		// public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		int meta = state.getBlock().getMetaFromState(state);
		if ((meta & 4) == 0)
		{
			worldIn.setBlockToAir(pos.add(new Vec3i(0, 1, 0)));
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileEntityStatue)
			{
//				ItemUtils.dropItems(world, x, y, z, (TileEntityStatue) tile);
				EntityItem entityItem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(state.getBlock(), 0));

				if (tile.getUpdateTag()!=null)
				{
					entityItem.getEntityItem().setTagCompound(tile.getUpdateTag());
				}

				float factor = 0.05F;
				Random ran=new Random();
				entityItem.motionX = ran.nextGaussian() * factor;
				entityItem.motionY = ran.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = ran.nextGaussian() * factor;
				worldIn.spawnEntity(entityItem);

			}
		}
		else
		{
			worldIn.setBlockToAir(pos.add(0,-1,0));//(x, y - 1, z, Blocks.air);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		return new TileEntityStatue();
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.stone.getIcon(0, 0);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof TileEntityStatue))
			return Blocks.stone.getIcon(0, 0);
		TileEntityStatue statue = (TileEntityStatue) te;

		Block block = statue.block;
		if (block == null)
			return Blocks.stone.getIcon(0, 0);

		return block.getIcon(side, statue.meta);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		while (isStatue(world, x, y, z) && !isMainBlock(world, x, y, z))
			y--;
		if (!isStatue(world, x, y, z))
			return 0;

		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof TileEntityStatue))
			return 0;
		TileEntityStatue statue = (TileEntityStatue) te;

		return statue.block.getLightValue();
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		int ox = x, oy = y, oz = z;
		while (isStatue(world, x, y, z) && !isMainBlock(world, x, y, z))
			y--;
		if (!isStatue(world, x, y, z))
			return 0;

		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof TileEntityStatue))
			return 0;
		TileEntityStatue statue = (TileEntityStatue) te;

		if (statue.block == null)
			return 0;
		return statue.block.isProvidingWeakPower(world, ox, oy, oz, side);
	}
}
