package org.dimdev.statues.shared.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPickBlock extends Slot
{
	ContainerPickBlock container;

	public SlotPickBlock(ContainerPickBlock c, int index, int x, int y)
	{
		super(c.inventory, index, x, y);
		container = c;
	}

	void click(EntityPlayer player, ItemStack itemstack, int count)
	{
		player.inventory.setItemStack(null);

		if (itemstack == null)
		{
			return;
		}

		if (container.gui == null)
		{
			return;
		}

		putStack(new ItemStack(itemstack.getItem(), itemstack.getCount(), itemstack.getItemDamage()));
		int newSize;

		if (container.resultSlot == this)
		{
			newSize = itemstack.getCount() - count;
		}
		else
		{
			newSize = itemstack.getCount();
			ItemStack otherstack = container.resultSlot.getStack();

			if (otherstack != null && otherstack.getItem().equals(itemstack.getItem()) && otherstack.getItemDamage() == itemstack.getItemDamage())
			{
				newSize = otherstack.getCount() + count;
			}
			else
			{
				newSize = count;
			}
		}

		if (newSize > 64)
		{
			newSize = 64;
		}

		container.resultSlot.putStack(newSize <= 0 ? null : new ItemStack(itemstack.getItem(), newSize, itemstack.getItemDamage()));
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
	{
		ItemStack item = super.onTake(thePlayer, stack);
		click(thePlayer, stack, 1);
		return item;
	}

	public ItemStack transferStackInSlot(EntityPlayer player)
	{
		click(player, getStack(), 64);
		return null;
	}
}
