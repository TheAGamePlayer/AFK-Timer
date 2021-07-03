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

public final class CTimerStopPacket {
	public static void encode(CTimerStopPacket msgIn, PacketBuffer bufIn) {}
	
	public static CTimerStopPacket decode(PacketBuffer bufIn) {
		return new CTimerStopPacket();
	}
	
	public static class Handler {
		public static boolean handle(CTimerStopPacket msgIn, Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static void handlePacket(CTimerStopPacket msgIn, Supplier<Context> ctxIn) {
			ClientEvents.clientActive = false;
			ClientEvents.clientTick = 0;
			ClientEvents.clientQuitGame = false;
			Minecraft mc = Minecraft.getInstance();
			mc.player.sendMessage(new TranslationTextComponent("commands.afktimer.client.stop").withStyle(Style.EMPTY.withColor(TextFormatting.GRAY)), mc.player.getUUID());
		}
	}
}
