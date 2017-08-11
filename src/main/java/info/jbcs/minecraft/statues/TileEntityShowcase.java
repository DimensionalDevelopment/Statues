/**
 * TileEntity class of the showcase
 */

package info.jbcs.minecraft.statues;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class TileEntityShowcase extends TileEntity
{
	public float lidAngle;
	public float prevLidAngle;
	private int ticksSinceSync;
	public int numUsingPlayers = 0;

	public int getSizeInventory()
	{
		return 1;
	}

	public String getInventoryName()
	{
		return "Showcase";
	}

	/**
	 * Called when the container is opened
	 */
	public void openInventory()
	{
		if (world.isRemote)
			return;

		numUsingPlayers++;
		world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

	/**
	 * Called when the container is closed
	 */
	public void closeInventory()
	{
		if (world.isRemote)
			return;

		numUsingPlayers--;
		world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

	public void updateEntity()
	{
		// super.updateEntity();

		if ((++ticksSinceSync % 20) * 4 == 0)
		{
			;
		}

		prevLidAngle = lidAngle;
		float f = 0.1F;

		if (numUsingPlayers > 0 && lidAngle == 0F)
		{
			double d = pos.getX() + 0.5D;
			double d1 = pos.getZ() + 0.5D;

			world.playSound(d, pos.getY() + 0.5D, d1, SoundEvent.REGISTRY.getObject(new ResourceLocation("random.chestopen")), SoundCategory.NEUTRAL, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F, false);
			// ( d, pos.getY() + 0.5D, d1, "random.chestopen", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			// world.playSound(d, pos.getY() + 0.5D, d1, "random.chestopen", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if ((numUsingPlayers == 0 && lidAngle > 0F) || (numUsingPlayers > 0 && lidAngle < 1F))
		{
			float f1 = lidAngle;

			if (numUsingPlayers > 0)
			{
				lidAngle += f;
			}
			else
			{
				lidAngle -= f;
			}

			if (lidAngle > 1F)
			{
				lidAngle = 1F;
			}
			if (lidAngle < 0F)
			{
				lidAngle = 0F;
			}

			float f2 = 0.5F;

			if (lidAngle < f2 && f1 >= f2)
			{
				double d2 = pos.getX() + 0.5D;
				double d3 = pos.getZ() + 0.5D;
				world.playSound(d2, pos.getY() + 0.5D, d3, SoundEvent.REGISTRY.getObject(new ResourceLocation("random.chestclosed")), SoundCategory.NEUTRAL, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F, false);
				// world.playSoundEffect(d2, pos.getY() + 0.5D, d3, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
		}
	}

	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	public Packet getDescriptionPacket()
	{
		if ((world.getBlockState(new BlockPos(new Vec3i(pos.getX(), pos.getY(), pos.getZ()))).getBlock().getMetaFromState(world.getBlockState(new BlockPos(new Vec3i(pos.getX(), pos.getY(), pos.getZ())))) & 4) != 0)
			// if ((world.getBlockMetadata(xCoord, yCoord, zCoord) & 4) != 0)
			return null;

		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		tag.setInteger("users", numUsingPlayers);
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		numUsingPlayers = pkt.getNbtCompound().getInteger("users");
	}

	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	public boolean hasCustomInventoryName()
	{
		return false;
	}

	public void onInventoryUpdate(int slot)
	{
		world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

}
