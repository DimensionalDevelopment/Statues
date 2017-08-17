/**
 * Item class for the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ItemShowcase extends Item
{

	public ItemShowcase(Block par2Block)
	{
		super();
		// (par2Block);
		setContainerItem(getItemFromBlock(par2Block));
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize = 64;
		this.setCreativeTab(CreativeTabs.DECORATIONS);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	/**
	 * When this method is called, your block should register all the icons it
	 * needs with the given IconRegister. This is the only chance you get to
	 * register icons.
	 */
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);

		Statues.slotHand = register.registerIcon("statues:slothand");
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
	{
		if (world.isRemote)
			return false;

		int meta = MathHelper.floor((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
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

		switch (side)
		{
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
			case 5:
				x++;
				break;
		}

		if (!player.canPlayerEdit(new BlockPos(new Vec3i(x, y, z)), EnumFacing.getFront(side), stack))
			return false;
		if (!player.canPlayerEdit(new BlockPos(new Vec3i(x + dx, y, z + dz)), EnumFacing.getFront(side), stack))
			return false;
		if (!player.canPlayerEdit(new BlockPos(new Vec3i(x - dx, y, z - dz)), EnumFacing.getFront(side), stack))
			return false;

		if (!world.isAirBlock(new BlockPos(new Vec3i(x, y, z))))
			return false;
		if (!world.isAirBlock(new BlockPos(new Vec3i(x + dx, y, z + dz))))
			return false;
		if (!world.isAirBlock(new BlockPos(new Vec3i(x - dx, y, z - dz))))
			return false;

		world.setBlockState(new BlockPos(new Vec3i(x, y, z)), (IBlockState) Statues.showcase, meta);
		if (meta >= 2)
			meta -= 2;
		world.setBlockState(new BlockPos(new Vec3i(x + dx, y, z + dz)), (IBlockState) Statues.showcase, meta | 4);
		world.setBlockState(new BlockPos(new Vec3i(x - dx, y, z - dz)), (IBlockState) Statues.showcase, (meta + 2) | 4);
		// world.setBlock(x - dx, y, z - dz, Statues.showcase, (meta + 2) | 4, 3);

		stack.getCount();
		stack.setCount(stack.getCount() - 1);
		return true;
	}

}
