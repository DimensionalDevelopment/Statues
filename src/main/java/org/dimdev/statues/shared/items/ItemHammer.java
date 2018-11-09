/**
 * Item Class for the hammer
 */

package org.dimdev.statues.shared.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.dimdev.statues.shared.utils.GeneralStatueClient;
import org.dimdev.statues.shared.gui.GuiSculpt;
import org.dimdev.statues.Statues;
import org.dimdev.statues.shared.tileentities.TileEntityStatue;
import org.dimdev.statues.shared.blocks.BlockStatue;

public class ItemHammer extends Item {

    public ItemHammer() {
        super();
        setRegistryName("statues:hammer");
        setTranslationKey("statues.hammer");
        setMaxDamage(2);
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hx, float hy, float hz) {
        if (world.isRemote && world.getTileEntity(pos) instanceof TileEntityStatue) {
            int ox = pos.getX();
            int oy = pos.getY();
            int oz = pos.getZ();

            int y = pos.getY();

            while (BlockStatue.isStatue(world, pos.getX(), y, pos.getZ()) && !BlockStatue.isMainBlock(world, pos.getX(), y, pos.getZ()))
                y--;
            if (!BlockStatue.isStatue(world, pos.getX(), y, pos.getZ()))
                return EnumActionResult.PASS;

            TileEntityStatue statue = (TileEntityStatue) world.getTileEntity(pos);

            GuiSculpt.pose.copyFrom(statue.pose);
            GeneralStatueClient.spawnCopyEffect(world, ox, oy, oz, facing.getIndex(), hx, hy, hz, statue);

            return EnumActionResult.PASS;
        }

        Block block = world.getBlockState(pos).getBlock();
        if (!Statues.canSculpt(block, player.world, pos.getX(), pos.getY(), pos.getZ()))
            return EnumActionResult.FAIL;
        if (world.isRemote)
            return EnumActionResult.PASS;

        int meta = block.getMetaFromState(block.getDefaultState());//world.getBlockMetadata(x, y, z);

        if (block.equals(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) && meta == world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock().getMetaFromState(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) && Statues.canSculpt(block, player.world, pos.getX(), pos.getY() + 1, pos.getZ())) {
            Statues.guiSculpt.open(player, world, pos.getX(), pos.getY(), pos.getZ());
            return EnumActionResult.PASS;
        }

        if (block.equals(world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()))) && meta == world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock().getMetaFromState(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) && Statues.canSculpt(block, player.world, pos.getX(), pos.getY() - 1, pos.getZ())) {
            Statues.guiSculpt.open(player, world, pos.getX(), pos.getY() - 1, pos.getZ());
            return EnumActionResult.PASS;
        }

        return EnumActionResult.PASS;
    }

}
