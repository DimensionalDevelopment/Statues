/**
 * Container class for the statue
 */

package org.dimdev.statues.shared.containers;

import net.minecraft.entity.player.InventoryPlayer;
//import pl.asie.lib.blocks.ContainerBase;
import net.minecraft.inventory.IInventory;
import org.dimdev.statues.shared.gui.SlotArmorStatue;
import org.dimdev.statues.shared.gui.SlotHand;
import org.dimdev.statues.shared.tileentities.TileEntityStatue;

public class ContainerStatue extends ContainerBase {
	TileEntityStatue tile;

	public ContainerStatue(InventoryPlayer inventory, TileEntityStatue te) {
		super(te, inventory);
		tile = te;

		addSlotToContainer(new SlotArmorStatue((IInventory) tile, 0, 80, 33, 0));
		addSlotToContainer(new SlotArmorStatue((IInventory) tile, 1, 80, 51, 1));
		addSlotToContainer(new SlotArmorStatue((IInventory) tile, 2, 80, 69, 2));
		addSlotToContainer(new SlotArmorStatue((IInventory) tile, 3, 80, 87, 3));
		addSlotToContainer(new SlotHand(tile, 4, 111, 51));
		addSlotToContainer(new SlotHand(tile, 5, 49, 51));
		
		this.bindPlayerInventory(inventory, 8, 144);
	}

}
