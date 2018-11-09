package org.dimdev.statues.shared.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import java.io.IOException;

public abstract class MessageHandlerBase {
	public abstract void onMessage(Packet packet, INetHandler handler, EntityPlayer player, int command) throws IOException;
	
	public Packet onMessage(Packet packet, INetHandler handler, EntityPlayer player) {
		try {
			onMessage(packet, handler, player, packet.readUnsignedShort());
		} catch(IOException e) {
			e.printStackTrace();
		}
		return packet;
	}

