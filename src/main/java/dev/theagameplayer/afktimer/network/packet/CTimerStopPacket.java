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

public final class CTimerStopPacket {
	public static void encode(CTimerStopPacket msgIn, FriendlyByteBuf bufIn) {}
	
	public static CTimerStopPacket decode(FriendlyByteBuf bufIn) {
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
			mc.player.sendMessage(new TranslatableComponent("commands.afktimer.client.stop").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), mc.player.getUUID());
		}
	}
}
