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

public final class CTimerQueryPacket {
	public static void encode(CTimerQueryPacket msgIn, FriendlyByteBuf bufIn) {}
	
	public static CTimerQueryPacket decode(FriendlyByteBuf bufIn) {
		return new CTimerQueryPacket();
	}
	
	public static class Handler {
		public static boolean handle(CTimerQueryPacket msgIn, Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static void handlePacket(CTimerQueryPacket msgIn, Supplier<Context> ctxIn) {
			Minecraft mc = Minecraft.getInstance();
			mc.player.sendMessage(new TranslatableComponent("commands.afktimer.client.query", ClientEvents.clientTime - ClientEvents.clientTick).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), mc.player.getUUID());
		}
	}
}
