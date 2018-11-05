/**
 * TileEntity class of the statue
 */

package org.dimdev.statues.shared.tileentities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.dimdev.statues.shared.utils.StatueParameters;
import org.dimdev.statues.shared.entities.EntityStatuePlayer;
import org.dimdev.statues.shared.gui.GuiStatue;

public class TileEntityStatue extends TileEntity
{
	private EntityPlayer clientPlayer;
	public String skinName = "";
	public StatueParameters pose = new StatueParameters();

	public Block block = Blocks.STONE;
	public int meta = 0;
	public int facing = 0;
	boolean updated = true;

	void randomize(Random rand)
	{
	}

	public int getSizeInventory()
	{
		return 6;
	}

	public EntityStatuePlayer getStatue()
	{
		if (clientPlayer == null)
		{
			EntityStatuePlayer player = new EntityStatuePlayer(world, skinName);
			player.ticksExisted = 10;
			player.pose = pose;
			player.applySkin(skinName, block, facing, meta);

			clientPlayer = player;
			for (int i = 0; i < 6; i++)
			{
				this.onInventoryUpdate(i);
			}
		}

		return (EntityStatuePlayer) clientPlayer;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);

		skinName = nbttagcompound.getString("skin");
		pose.readFromNBT(nbttagcompound);

		block = Block.getBlockById(nbttagcompound.getShort("blockId"));
		if (block == null)
			block = Blocks.STONE;
		meta = nbttagcompound.getByte("meta");
		facing = nbttagcompound.getByte("face");

		updateModel();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setString("skin", skinName);
		pose.writeToNBT(nbttagcompound);

		nbttagcompound.setShort("blockId", (short) Block.getIdFromBlock(block));
		nbttagcompound.setByte("meta", (byte) meta);
		nbttagcompound.setByte("face", (byte) facing);
		return nbttagcompound;
	}

	public void openInventory()
	{

	}

	public void closeInventory()
	{

	}

	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	public Packet getDescriptionPacket()
	{
		if ((block.getMetaFromState(world.getBlockState(this.pos)) & 4) != 0)
			return null;

		NBTTagCompound tag = new NBTTagCompound();
		tag = writeToNBT(tag);
		return new SPacketUpdateTileEntity(this.pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		// func_148857_g();
		if (world.isRemote && Minecraft.getMinecraft().currentScreen instanceof GuiStatue)
		{
			GuiStatue gui = (GuiStatue) Minecraft.getMinecraft().currentScreen;
			pose.itemLeftA = gui.ila;
			pose.itemRightA = gui.ira;
		}
	}

	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	public void updateModel()
	{
		if (clientPlayer != null && world != null && world.isRemote)
		{
			((EntityStatuePlayer) clientPlayer).applySkin(skinName, block, facing, meta);
		}

		updated = false;
	}

	public void updateEntity()
	{
		if (updated)
			return;
		updated = true;

	}

	public boolean hasCustomInventoryName()
	{
		return false;
	}

	public void onInventoryUpdate(int slot)
	{
		if (clientPlayer != null)
		{
			clientPlayer.inventory.mainInventory.set(0, getStackInSlotOnClosing(4));
			clientPlayer.inventory.mainInventory.set(1, getStackInSlotOnClosing(5));
			clientPlayer.inventory.armorInventory.set(0, getStackInSlotOnClosing(3));
			clientPlayer.inventory.armorInventory.set(1, getStackInSlotOnClosing(2));
			clientPlayer.inventory.armorInventory.set(2, getStackInSlotOnClosing(1));
			clientPlayer.inventory.armorInventory.set(3, getStackInSlotOnClosing(0));
		}

		world.markBlockRangeForRenderUpdate(pos, pos);
	}

}
