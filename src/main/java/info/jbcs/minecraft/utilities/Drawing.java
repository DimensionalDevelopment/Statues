package info.jbcs.minecraft.utilities;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IIcon;

public class Drawing {
	public static void drawBlock(Block block,IIcon icon, RenderBlocks renderer){
		Tessellator tessellator = Tessellator.getInstance();
//		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();

        tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();

        tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();

        tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
		
        tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();

		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
	}

	public static void drawBlock(Block block,int meta, RenderBlocks renderer){
		Tessellator tessellator = Tessellator.getInstance();

		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
        tessellator.draw();

		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
        tessellator.draw();

		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
        tessellator.draw();

		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
        tessellator.draw();

		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
        tessellator.draw();
        
		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.getBuffer().normal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
        tessellator.draw();
	}
}
