/**
 * MrCrayfish's Furniture Mod
 * Copyright (C) 2014  MrCrayfish (http://www.mrcrayfish.com/)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mrcrayfish.furniture.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrcrayfish.furniture.tileentity.TileEntityComputer;

//Server Side
public class MessageMineBayClosed implements IMessage, IMessageHandler<MessageMineBayClosed, IMessage>
{

	private int x, y, z;

	public MessageMineBayClosed()
	{
	}

	public MessageMineBayClosed(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	@Override
	public IMessage onMessage(MessageMineBayClosed message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		TileEntity tile_entity = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (tile_entity instanceof TileEntityComputer)
		{
			TileEntityComputer tileEntityComputer = (TileEntityComputer) tile_entity;

			if (tileEntityComputer.getStackInSlot(0) != null)
			{
				player.entityDropItem(tileEntityComputer.getStackInSlot(0), 1);
				tileEntityComputer.setInventorySlotContents(0, null);
			}
			tileEntityComputer.setTrading(false);
		}
		player.worldObj.markBlockForUpdate(new BlockPos(message.x, message.y, message.z));
		return null;
	}

}
