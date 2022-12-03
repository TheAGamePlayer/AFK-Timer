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

public final class CTimerExtendPacket {
	private final int time;
	
	public CTimerExtendPacket(final int timeIn) {
		this.time = timeIn;
	}
	
	public static final void encode(final CTimerExtendPacket msgIn, final FriendlyByteBuf bufIn) {
		bufIn.writeInt(msgIn.time);
	}
	
	public static final CTimerExtendPacket decode(final FriendlyByteBuf bufIn) {
		return new CTimerExtendPacket(bufIn.readInt());
	}
	
	public static final class Handler {
		public static final boolean handle(final CTimerExtendPacket msgIn, final Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static final void handlePacket(final CTimerExtendPacket msgIn, final Supplier<Context> ctxIn) {
			ClientEvents.clientTime += msgIn.time;
			final Minecraft mc = Minecraft.getInstance();
			mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.extend", msgIn.time).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}
}
