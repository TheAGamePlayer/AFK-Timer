package dev.theagameplayer.afktimer.network.packet;

import java.util.function.Supplier;

import dev.theagameplayer.afktimer.AFKEventManager.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public final class CTimerExtendPacket {
	private final int time;
	
	public CTimerExtendPacket(int timeIn) {
		this.time = timeIn;
	}
	
	public static void encode(CTimerExtendPacket msgIn, PacketBuffer bufIn) {
		bufIn.writeInt(msgIn.time);
	}
	
	public static CTimerExtendPacket decode(PacketBuffer bufIn) {
		return new CTimerExtendPacket(bufIn.readInt());
	}
	
	public static class Handler {
		public static boolean handle(CTimerExtendPacket msgIn, Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static void handlePacket(CTimerExtendPacket msgIn, Supplier<Context> ctxIn) {
			ClientEvents.clientTime += msgIn.time;
			Minecraft mc = Minecraft.getInstance();
			mc.player.sendMessage(new TranslationTextComponent("commands.afktimer.client.extend", msgIn.time).withStyle(Style.EMPTY.withColor(TextFormatting.GRAY)), mc.player.getUUID());
		}
	}
}
