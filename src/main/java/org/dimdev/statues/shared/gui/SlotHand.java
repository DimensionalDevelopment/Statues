/**
 * Slot class for the slot hand
 */

package org.dimdev.statues.shared.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.dimdev.statues.Statues;
import org.dimdev.statues.shared.tileentities.TileEntityStatue;
//import net.minecraft.util.IIcon;

public class SlotHand extends Slot {
	public SlotHand(IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	public SlotHand(TileEntityStatue te, int i, int j, int k) {
		super((IInventory) te, i, j, k);
	}

	/**
	 * Returns the icon index on items.png that is used as background image of
	 * the slot.
	 */
	@Override
	public IIcon getBackgroundIconIndex() {
		return Statues.slotHand;
	}
}