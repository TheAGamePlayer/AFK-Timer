package dev.theagameplayer.afktimer.network.packet;

import java.util.function.Supplier;

import dev.theagameplayer.afktimer.AFKEventManager.ClientEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public final class CTimerStartPacket {
	private final int time;
	private final boolean quitGame;
	
	public CTimerStartPacket(int timeIn, boolean quitGameIn) {
		this.time = timeIn;
		this.quitGame = quitGameIn;
	}
	
	public static void encode(CTimerStartPacket msgIn, FriendlyByteBuf bufIn) {
		bufIn.writeInt(msgIn.time);
		bufIn.writeBoolean(msgIn.quitGame);
	}
	
	public static CTimerStartPacket decode(FriendlyByteBuf bufIn) {
		return new CTimerStartPacket(bufIn.readInt(), bufIn.readBoolean());
	}
	
	public static class Handler {
		public static boolean handle(CTimerStartPacket msgIn, Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static void handlePacket(CTimerStartPacket msgIn, Supplier<Context> ctxIn) {
			ClientEvents.clientTime = msgIn.time;
			ClientEvents.clientActive = true;
			ClientEvents.clientQuitGame = msgIn.quitGame;
			Minecraft mc = Minecraft.getInstance();
			mc.player.sendMessage(new TranslatableComponent("commands.afktimer.client.start", msgIn.time).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), mc.player.getUUID());
		}
	}
}
