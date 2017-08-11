package info.jbcs.minecraft.utilities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public abstract class InventoryStatic implements IInventory
{
	public final ItemStack items[];

	public InventoryStatic(int size)
	{
		items = new ItemStack[size];
	}

	public String getInventoryName()
	{
		return null;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}

	public void onInventoryChanged(int slot)
	{
	}

	@Override
	public int getSizeInventory()
	{
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return items[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (items[i] != null)
		{
			if (items[i].getCount() <= j)
			{
				ItemStack itemstack = items[i];
				items[i] = null;
				onInventoryChanged();
				onInventoryChanged(i);
				return itemstack;
			}

			ItemStack itemstack1 = items[i].splitStack(j);

			if (items[i].getCount() == 0)
			{
				items[i] = null;
			}

			onInventoryChanged();
			onInventoryChanged(i);
			return itemstack1;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		items[i] = itemstack;

		if (itemstack != null && itemstack.getCount() > getInventoryStackLimit())
		{
			itemstack.setCount(getInventoryStackLimit());
		}

		onInventoryChanged();
		onInventoryChanged(i);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("slot") & 0xff;

			if (j >= 0 && j < items.length)
			{
				items[j] = new ItemStack(nbttagcompound1);
			}
		}

		onInventoryChanged();
	}

	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null)
			{
				continue;
			}

			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("slot", (byte) i);
			items[i].writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("items", nbttaglist);
	}

	private int getFirstEmptyStack(int start, int end)
	{
		for (int i = start; i <= end; i++)
		{
			if (items[i] == null)
			{
				return i;
			}
		}

		return -1;
	}

	private int storeItemStack(ItemStack itemstack, int start, int end)
	{
		for (int i = start; i <= end; i++)
		{
			if (items[i] != null && items[i].getItem().equals(itemstack.getItem()) && items[i].isStackable() && items[i].getCount() < items[i].getMaxStackSize() && items[i].getCount() < getInventoryStackLimit() && (!items[i].getHasSubtypes() || items[i].getItemDamage() == itemstack.getItemDamage()))
			{
				return i;
			}
		}

		return -1;
	}

	private int storePartialItemStack(ItemStack itemstack, int start, int end)
	{
		Item i = itemstack.getItem();
		int j = itemstack.getCount();
		int k = storeItemStack(itemstack, start, end);

		if (k < 0)
		{
			k = getFirstEmptyStack(start, end);
		}

		if (k < 0)
		{
			return j;
		}

		if (items[k] == null)
		{
			items[k] = new ItemStack(i, 0, itemstack.getItemDamage());
		}

		int l = j;

		if (l > items[k].getMaxStackSize() - items[k].getCount())
		{
			l = items[k].getMaxStackSize() - items[k].getCount();
		}

		if (l > getInventoryStackLimit() - items[k].getCount())
		{
			l = getInventoryStackLimit() - items[k].getCount();
		}

		if (l == 0)
		{
			return j;
		}
		else
		{
			j -= l;
			items[k].setCount(items[k].getCount() + l);
			items[k].setAnimationsToGo(5);
			onInventoryChanged();
			onInventoryChanged(k);
			return j;
		}
	}

	public boolean addItemStackToInventory(ItemStack itemstack, int start, int end)
	{
		if (itemstack == null)
		{
			return true;
		}

		if (!itemstack.isItemDamaged())
		{
			int i;

			do
			{
				i = itemstack.getCount();
				itemstack.setCount(storePartialItemStack(itemstack, start, end));
			}
			while (itemstack.getCount() > 0 && itemstack.getCount() < i);

			return itemstack.getCount() < i;
		}

		int j = getFirstEmptyStack(start, end);

		if (j >= 0)
		{
			items[j] = itemstack.copy();
			items[j].setAnimationsToGo(5);
			itemstack.setCount(0);
			onInventoryChanged();
			onInventoryChanged(j);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean addItemStackToInventory(ItemStack itemstack)
	{
		return addItemStackToInventory(itemstack, 0, items.length - 1);
	}

	public ItemStack takeItems(Item item, int damage, int count)
	{
		ItemStack res = null;

		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null || !items[i].getItem().equals(item) || items[i].getItemDamage() != damage)
			{
				continue;
			}

			if (res == null)
			{
				res = new ItemStack(item, 0, damage);
			}

			while (items[i] != null && res.getCount() < count && items[i].getCount() > 0)
			{
				res.setCount(res.getCount() + 1);
				items[i].setCount(items[i].getCount() - 1);

				if (items[i].getCount() == 0)
				{
					items[i] = null;
				}

				onInventoryChanged(i);
			}

			if (res.getCount() >= count)
			{
				break;
			}
		}

		onInventoryChanged();
		return res;
	}

	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	public void onInventoryChanged()
	{
	}

	public void openInventory()
	{
	}

	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	public boolean isEmpty()
	{
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] != null && items[i].getItem() != null)
			{
				return false;
			}
		}

		return true;
	}

	public void clear()
	{
		for (int i = 0; i < items.length; i++)
		{
			items[i] = null;
		}
	}

	public void throwItems(World world, int x, int y, int z)
	{
		if (world.isRemote)
			return;

		for (int i = 0; i < items.length; i++)
		{
			ItemStack itemstack = items[i];
			if (itemstack == null)
				continue;

			items[i] = null;

			float xx = world.rand.nextFloat() * 0.8F + 0.1F;
			float yy = world.rand.nextFloat() * 0.8F + 0.1F;
			float zz = world.rand.nextFloat() * 0.8F + 0.1F;
			while (itemstack.getCount() > 0)
			{
				int c = world.rand.nextInt(21) + 10;
				if (c > itemstack.getCount())
				{
					c = itemstack.getCount();
				}

				itemstack.setCount(itemstack.getCount() - c);
				EntityItem entityitem = new EntityItem(world, x + xx, y + yy, z + zz, new ItemStack(itemstack.getItem(), c, itemstack.getItemDamage()));
				float f3 = 0.05F;
				entityitem.motionX = (float) world.rand.nextGaussian() * f3;
				entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
				world.spawnEntity(entityitem);
			}
		}

		onInventoryChanged();
	}

	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public void markDirty()
	{
		// TODO
	}
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		
		return null;
	}

}
