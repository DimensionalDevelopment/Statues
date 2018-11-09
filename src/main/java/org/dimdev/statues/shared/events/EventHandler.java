package org.dimdev.statues.shared.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.dimdev.statues.shared.blocks.BlockStatue;
import org.dimdev.statues.shared.items.ItemHammer;
import org.dimdev.statues.shared.items.ItemPalette;

public class EventHandler {

    public static Block statue = new BlockStatue();
    public static Item hammer = new ItemHammer();
    public static Item itemPalette = new ItemPalette();

    @SubscribeEvent
    public void blockRegistry(RegistryEvent.Register<Block> e) {

    }

}
