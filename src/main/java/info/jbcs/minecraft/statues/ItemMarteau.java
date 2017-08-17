/**
 * Item Class for the hammer
 */

package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMarteau extends Item
{
	public ItemMarteau()
	{
		super();
		setMaxDamage(2);
		maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz)
	{
		if (world.isRemote && world.getTileEntity(new BlockPos(x, y, z)) instanceof TileEntityStatue)
		{
			int ox = x;
			int oy = y;
			int oz = z;

			while (BlockStatue.isStatue(world, x, y, z) && !BlockStatue.isMainBlock(world, x, y, z))
				y--;
			if (!BlockStatue.isStatue(world, x, y, z))
				return true;

			TileEntityStatue statue = (TileEntityStatue) world.getTileEntity(new BlockPos(x, y, z));

			GuiSculpt.pose.copyFrom(statue.pose);
			GeneralStatueClient.spawnCopyEffect(world, ox, oy, oz, side, hx, hy, hz, statue);

			return true;
		}

		Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
		if (!Statues.canSculpt(block, player.world, x, y, z))
			return false;
		if (world.isRemote)
			return true;

		int meta = block.getMetaFromState(block.getDefaultState());//world.getBlockMetadata(x, y, z);

		if (block.equals(world.getBlockState(new BlockPos(x, y + 1, z))) && meta == world.getBlockState(new BlockPos(x, y + 1, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y + 1, z))) && Statues.canSculpt(block, player.world, x, y + 1, z))
		{
			Statues.guiSculpt.open(player, world, x, y, z);
			return true;
		}

		if (block.equals(world.getBlockState(new BlockPos(x, y - 1, z))) && meta == world.getBlockState(new BlockPos(x, y + 1, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y + 1, z)))  && Statues.canSculpt(block, player.world, x, y - 1, z))
		{
			Statues.guiSculpt.open(player, world, x, y - 1, z);
			return true;
		}

		return true;
	}

}
