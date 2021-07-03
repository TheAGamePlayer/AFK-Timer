package dev.theagameplayer.afktimer.network;

import dev.theagameplayer.afktimer.AFKTimerMod;
import dev.theagameplayer.afktimer.network.packet.CTimerExtendPacket;
import dev.theagameplayer.afktimer.network.packet.CTimerQueryPacket;
import dev.theagameplayer.afktimer.network.packet.CTimerStartPacket;
import dev.theagameplayer.afktimer.network.packet.CTimerStopPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class AFKPacketHandler {
	private static final String PROTOCAL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(AFKTimerMod.MODID, "main_network_channel"), 
			() -> PROTOCAL_VERSION, 
			PROTOCAL_VERSION::equals, 
			PROTOCAL_VERSION::equals);
	
	public static void registerPackets() {
		int id = 0;
		//PLAY DEDICATED SERVER -> CLIENT
		CHANNEL.messageBuilder(CTimerStartPacket.class, id++).encoder(CTimerStartPacket::encode).decoder(CTimerStartPacket::decode).consumer(CTimerStartPacket.Handler::handle).add();
		CHANNEL.messageBuilder(CTimerStopPacket.class, id++).encoder(CTimerStopPacket::encode).decoder(CTimerStopPacket::decode).consumer(CTimerStopPacket.Handler::handle).add();
		CHANNEL.messageBuilder(CTimerQueryPacket.class, id++).encoder(CTimerQueryPacket::encode).decoder(CTimerQueryPacket::decode).consumer(CTimerQueryPacket.Handler::handle).add();
		CHANNEL.messageBuilder(CTimerExtendPacket.class, id++).encoder(CTimerExtendPacket::encode).decoder(CTimerExtendPacket::decode).consumer(CTimerExtendPacket.Handler::handle).add();
	}
	
	public static void sendToClient(Object msgIn, ServerPlayerEntity playerIn) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerIn), msgIn);
	}
}
