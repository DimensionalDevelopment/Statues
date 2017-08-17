/**
 * Block class for the statue
 */

package info.jbcs.minecraft.statues;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockShowcase extends BlockContainer
{
	public BlockShowcase(Material material)
	{
		super(material);
	}

	public ArrayList<ItemStack> getDrops(World world, BlockPos pos, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int meta = world.getBlockState(pos).getBlock().getMetaFromState(getDefaultState());
		if ((meta & 4) != 0)
			return ret;

		ret.add(new ItemStack(Statues.itemShowcase, 1));

		return ret;
	}

	/*
	 * @Override
	 * @SideOnly(Side.CLIENT) public Item itemPicked(World par1World, int par2, int par3, int par4){ return Statues.itemShowcase.itemID; }
	 */ // TODO

	public int getMobilityFlag()
	{
		return 2;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return -1;
	}

	/**
	 * return false if the block isn't a full 1*1 cube
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * return false if the block mustn't be rendered as a normal block
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));

		switch (meta)
		{
			case 0 | 4:
				setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.5f, 1.0F);
				break;
			case 2 | 4:
				setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.5f, 1.0F);
				break;
			case 1 | 4:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5f, 0.5F);
				break;
			case 3 | 4:
				setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.5f, 1.0F);
				break;
			default:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5f, 1.0F);
				break;
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack par6ItemStack)
	{

		int meta = MathHelper.floor((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
		int dx, dz;

		switch (meta)
		{
			default:
			case 0:
			case 2:
				dx = 1;
				dz = 0;
				break;
			case 1:
			case 3:
				dx = 0;
				dz = 1;
				break;
		}

		// world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		world.setBlockState(new BlockPos(new Vec3d(x, y, z)), Block.getBlockFromItem(par6ItemStack.getItem()).getDefaultState());
		world.notify();
		if (meta >= 2)
			meta -= 2;

		world.setBlockState(new BlockPos(new Vec3d(x + dx, y, z + dz)), this.getDefaultState());
		world.setBlockState(new BlockPos(new Vec3d(x - dx, y, z - dz)), this.getDefaultState());

		// world.setBlock(x+dx, y, z+dz, this, meta|4, 3);
		// world.setBlock(x-dx, y, z-dz, this, (meta+2)|4, 3);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		BlockPos pos = new BlockPos(new Vec3d(x, y, z));
		int meta = world.getBlockState(pos).getBlock().getMetaFromState(getDefaultState());

		if ((meta & 4) != 0)
		{
			switch (meta & 3)
			{
				default:
				case 0:
				case 2:
					if (isCenterBlock(world, new BlockPos(new Vec3d(x - 1, y, z))))
						x--;
					else if (isCenterBlock(world, new BlockPos(new Vec3d(x + 1, y, z))))
						x++;
					break;
				case 1:
				case 3:
					if (isCenterBlock(world, new BlockPos(new Vec3d(x, y, z - 1))))
						z--;
					else if (isCenterBlock(world, new BlockPos(new Vec3d(x, y, z + 1))))
						z++;
					break;
			}
		}

		if (!isCenterBlock(world, new BlockPos(new Vec3d(x, y, z))))
			return true;

		TileEntityShowcase teshowcase = (TileEntityShowcase) world.getTileEntity(new BlockPos(new Vec3d(x, y, z)));
		if (teshowcase instanceof TileEntityShowcase && !world.isRemote)
			Statues.guiShowcase.open(entityplayer, world, x, y, z);

		return true;
	}

	boolean isCenterBlock(World world, BlockPos pos)
	{
		return (world.getBlockState(pos).equals(this)) && (world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) & 4) == 0;
	}

	boolean isCenterBlock(Block block, int meta)
	{
		return (block.equals(this)) && (meta & 4) == 0;
	}

	public void breakBlock(World world, int xx, int yy, int zz, Block block, int meta)
	{
		int x = xx, y = yy, z = zz;
		boolean found = true;

		if (isCenterBlock(block, meta))
		{
			TileEntity tile = world.getTileEntity(new BlockPos(new Vec3d(x, y, z)));
			if (tile instanceof TileEntityShowcase)
				ItemUtils.dropItems(world, xx, yy, zz, ((TileEntityShowcase) tile));
		}
		else
		{
			switch (meta & 3)
			{
				default:
				case 0:
				case 2:
					if (isCenterBlock(world, new BlockPos(new Vec3d(x - 1, y, z))))
						x--;
					else if (isCenterBlock(world, new BlockPos(new Vec3d(x + 1, y, z))))
						x++;
					else
						found = false;
					break;
				case 1:
				case 3:
					if (isCenterBlock(world, new BlockPos(new Vec3d(x, y, z - 1))))
						z--;
					else if (isCenterBlock(world, new BlockPos(new Vec3d(x, y, z + 1))))
						z++;
					else
						found = false;
					break;
			}
		}

		if (!found && !isCenterBlock(block, meta))
			return;

		world.setBlockToAir(new BlockPos(new Vec3d(x, y, z)));

		switch (meta & 3)
		{
			case 0:
			case 2:
				world.setBlockToAir(new BlockPos(new Vec3d(x + 1, y, z)));
				world.setBlockToAir(new BlockPos(new Vec3d(x - 1, y, z)));
				break;
			case 1:
			case 3:
				world.setBlockToAir(new BlockPos(new Vec3d(x, y, z + 1)));
				world.setBlockToAir(new BlockPos(new Vec3d(x, y, z - 1)));

				break;
		}

		super.breakBlock(world, new BlockPos(new Vec3d(xx, yy, zz)), block.getDefaultState());
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		return new TileEntityShowcase();
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.PLANKS.getIcon(2, 0);
	}

}
