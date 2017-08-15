/**
 * Container class for the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
//import pl.asie.lib.block.ContainerBase;

public class ContainerShowcase extends ContainerBase {

	public ContainerShowcase(InventoryPlayer inventory, TileEntityShowcase tile) {
		super((TileEntity)tile, inventory);
		
		addSlotToContainer(new SlotHand(tile, 0, 120, 59));

		bindPlayerInventory(inventory, 48, 144);
		
		tile.openInventory();
	}
}
