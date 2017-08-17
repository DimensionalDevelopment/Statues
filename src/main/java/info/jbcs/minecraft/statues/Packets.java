package info.jbcs.minecraft.statues;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
//import pl.asie.lib.network.MessageHandlerBase;

public class Packets extends MessageHandlerBase
{
	public static final int SCULPTURE_CREATION = 1;
	public static final int SCULPTURE_ADJUSTMENT = 2;
	public static final int SCULPTED = 3;

	public void onSculptureCreation(Packet packet, EntityPlayer player) throws IOException
	{
		final int x = packet.readInt();
		final int y = packet.readInt();
		final int z = packet.readInt();
		int face = packet.readByte();
		BlockPos bp = new BlockPos(new Vec3i(x, y, z));
		final Block block = player.world.getBlockState(bp).getBlock();
		if (!Statues.canSculpt(block, player.world, x, y, z))
			return;

		final int meta = block.getMetaFromState(player.world.getBlockState(bp));// player.world.getBlockMetadata(x,y,z);
		if (!block.equals(player.world.getBlockState(new BlockPos(new Vec3i(x, y + 1, z))).getBlock()) || meta != player.world.getBlockState(new BlockPos(new Vec3i(x, y + 1, z))).getBlock().getMetaFromState(player.world.getBlockState(new BlockPos(new Vec3i(x, y + 1, z)))))
			return;

		// player.world.setBlock(x, y, z, Statues.statue, face, 3);
		player.world.setBlockState(bp, (IBlockState) Statues.statue, face);// ( face, 3);

		TileEntity tileEntity = player.world.getTileEntity(bp);
		if (!(tileEntity instanceof TileEntityStatue))
			return;
		TileEntityStatue entity = (TileEntityStatue) tileEntity;

		entity.skinName = packet.readString();
		entity.pose.read(packet);
		entity.block = block;
		entity.meta = meta;
		entity.facing = 2;

		player.world.setBlockState(new BlockPos(new Vec3i(x, y + 1, z)), (IBlockState) Statues.statue, face | 4);
		// player.world.setBlockState((x, y + 1, z, Statues.statue, face | 4, 3);
		player.world.notifyNeighborsOfStateChange(bp, Statues.statue.getBlockState().getBlock(), true);
		// BlocksOfNeighborChange(x, y, z, Statues.statue);
		// player.world.notifyBlocksOfNeighborChange(x, y, z, Statues.statue);
		player.world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		// player.world.markBlockForUpdate(x, y, z);

		Packet sculpted = Statues.packet.create(Packets.SCULPTED)
			.writeInt(x).writeInt(y).writeInt(z).writeShort((short) Block.getIdFromBlock(block)).writeByte((byte) meta);
		Statues.packet.sendToAllAround(sculpted, player, 64);
	}

	public void onSculptureAdjustment(Packet packet, EntityPlayer player) throws IOException
	{
		final int x = packet.readInt();
		final int y = packet.readInt();
		final int z = packet.readInt();
		float itemLeftA = packet.readFloat();
		float itemRightA = packet.readFloat();

		TileEntity tileEntity = player.world.getTileEntity(new BlockPos(new Vec3i(x, y, z)));
		if (!(tileEntity instanceof TileEntityStatue))
			return;
		TileEntityStatue entity = (TileEntityStatue) tileEntity;

		entity.pose.itemLeftA = itemLeftA;
		entity.pose.itemRightA = itemRightA;
		player.world.markBlockRangeForRenderUpdate(x-1, y-1, z-1, x+1, y+1, z+1);;
	}

	public void onSculpted(Packet packet, EntityPlayer player) throws IOException
	{
		final int x = packet.readInt();
		final int y = packet.readInt();
		final int z = packet.readInt();
		final Block block = Block.getBlockById(packet.readShort());
		final byte meta = packet.readByte();

		GeneralStatueClient.spawnSculptEffect(x, y, z, block, meta);
		GeneralStatueClient.spawnSculptEffect(x, y + 1, z, block, meta);
	}

	@Override
	public void onMessage(pl.asie.lib.network.Packet packet, INetHandler handler, EntityPlayer player, int command) throws IOException
	{
		switch (command)
		{
			case SCULPTURE_CREATION:
				onSculptureCreation(packet, player);
				break;
			case SCULPTURE_ADJUSTMENT:
				onSculptureAdjustment(packet, player);
				break;
			case SCULPTED:
				onSculpted(packet, player);
				break;
		}
	}

	@Override
	public void onMessage(info.jbcs.minecraft.statues.Packet packet, INetHandler handler, EntityPlayer player, int command) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

}
