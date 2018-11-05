package org.dimdev.statues.shared.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.dimdev.statues.shared.utils.GeneralStatueClient;
import org.dimdev.statues.Statues;
import org.dimdev.statues.shared.tileentities.TileEntityStatue;

public class ItemPalette extends Item
{
	public ItemPalette()
	{
		super();
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		// public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz) {
		Block block;
		int meta;
		int y = pos.getY();
		int x= pos.getX();
		int z= pos.getZ();
		y++;
		do
		{
			y--;
			block = world.getBlockState(new BlockPos(new Vec3i(x, y, z))).getBlock();//(x, y, z);
			meta = block.getMetaFromState(world.getBlockState(new BlockPos(new Vec3i(x, y, z))));//world.getBlockMetadata(x, y, z);
		}
		while (block.equals(Statues.statue) && (meta & 4) != 0);

		if (block != Statues.statue)
			return EnumActionResult.SUCCESS;

		TileEntity te = world.getTileEntity(new BlockPos(new Vec3i(x, y, z)));
		if (!(te instanceof TileEntityStatue))
			return EnumActionResult.SUCCESS;
		TileEntityStatue statue = (TileEntityStatue) te;

		statue.block = Blocks.BEDROCK;
		ItemStack stack = this.getDefaultInstance();//ContainerItem();
		stack.damageItem(1, player);
//		stack.damageItem(1, player);
		world.markBlockRangeForRenderUpdate(new BlockPos(new Vec3i(x-1, y-1, z-1)), new BlockPos(new Vec3i(x+1, y+1, z+1)));

		stack.setCount(stack.getCount()-1);

		if (world.isRemote)
		{
			statue.updateModel();
			GeneralStatueClient.spawnPaintEffect(world, x, y, z);
			world.playSound(player, new BlockPos(new Vec3i(x+0.5, y+.05, z+0.5)), 			SoundEvent.REGISTRY.getObject(new ResourceLocation("statues:paint")), SoundCategory.VOICE, 0.1f, 0.1f);
//			(x + 0.5, y + 0.5, z + 0.5, "statues:paint", 1.0f, 1.0f, true);
//			world.playSound(x + 0.5, y + 0.5, z + 0.5, "statues:paint", 1.0f, 1.0f, true);
		}

		return EnumActionResult.SUCCESS;
	}

}
