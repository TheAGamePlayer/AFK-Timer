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

public final class CTimerQueryPacket {
	public static final void encode(final CTimerQueryPacket msgIn, final FriendlyByteBuf bufIn) {}
	
	public static final CTimerQueryPacket decode(final FriendlyByteBuf bufIn) {
		return new CTimerQueryPacket();
	}
	
	public static final class Handler {
		public static final boolean handle(final CTimerQueryPacket msgIn, final Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static final void handlePacket(final CTimerQueryPacket msgIn, final Supplier<Context> ctxIn) {
			final Minecraft mc = Minecraft.getInstance();
			mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.query", ClientEvents.clientTime - ClientEvents.clientTick).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}
}
