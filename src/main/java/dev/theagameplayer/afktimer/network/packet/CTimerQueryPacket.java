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

public final class CTimerQueryPacket {
	public static void encode(CTimerQueryPacket msgIn, PacketBuffer bufIn) {}
	
	public static CTimerQueryPacket decode(PacketBuffer bufIn) {
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
			mc.player.sendMessage(new TranslationTextComponent("commands.afktimer.client.query", ClientEvents.clientTime - ClientEvents.clientTick).withStyle(Style.EMPTY.withColor(TextFormatting.GRAY)), mc.player.getUUID());
		}
	}
}
