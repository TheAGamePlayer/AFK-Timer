package dev.theagameplayer.afktimer.network.packet;

import java.util.function.Supplier;

import dev.theagameplayer.afktimer.AFKEventManager.ClientEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public final class CTimerStopPacket {
	public static final void encode(final CTimerStopPacket msgIn, final FriendlyByteBuf bufIn) {}
	
	public static final CTimerStopPacket decode(final FriendlyByteBuf bufIn) {
		return new CTimerStopPacket();
	}
	
	public static final class Handler {
		public static final boolean handle(final CTimerStopPacket msgIn, final Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static final void handlePacket(final CTimerStopPacket msgIn, final Supplier<Context> ctxIn) {
			ClientEvents.clientActive = false;
			ClientEvents.clientTick = 0;
			ClientEvents.clientQuitGame = false;
			final Minecraft mc = Minecraft.getInstance();
			mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.stop").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}
}
