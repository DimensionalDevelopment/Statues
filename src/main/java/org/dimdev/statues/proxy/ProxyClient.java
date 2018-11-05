package org.dimdev.statues.proxy;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.dimdev.statues.shared.entities.EntityTextureFX;
import org.dimdev.statues.client.render.RenderStatue;
import org.dimdev.statues.client.render.RenderTextureFX;
import org.dimdev.statues.shared.tileentities.TileEntityStatue;

public class ProxyClient extends Proxy {
	@Override
	public void preInit() {

	}

	@Override
	public void init() {

		TileEntityRendererDispatcher.instance.renderers.put(TileEntityStatue.class, new RenderStatue());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityTextureFX.class, new RenderTextureFX());
		
//		RenderingRegistry.registerBlockHandler(new BlockCarpetRenderer());

//		RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(Chisel.itemCloudInABottle));
		
//		MinecraftForgeClient.registerItemRenderer(Chisel.needle.itemID, renderer);
	}
}
