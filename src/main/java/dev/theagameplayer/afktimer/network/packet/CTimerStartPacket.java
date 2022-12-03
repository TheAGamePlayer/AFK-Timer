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

public final class CTimerStartPacket {
	private final int time;
	private final boolean quitGame;
	
	public CTimerStartPacket(final int timeIn, final boolean quitGameIn) {
		this.time = timeIn;
		this.quitGame = quitGameIn;
	}
	
	public static final void encode(final CTimerStartPacket msgIn, final FriendlyByteBuf bufIn) {
		bufIn.writeInt(msgIn.time);
		bufIn.writeBoolean(msgIn.quitGame);
	}
	
	public static final CTimerStartPacket decode(final FriendlyByteBuf bufIn) {
		return new CTimerStartPacket(bufIn.readInt(), bufIn.readBoolean());
	}
	
	public static final class Handler {
		public static final boolean handle(final CTimerStartPacket msgIn, final Supplier<Context> ctxIn) {
			ctxIn.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msgIn, ctxIn));
			});
			return true;
		}
		
		private static final void handlePacket(final CTimerStartPacket msgIn, final Supplier<Context> ctxIn) {
			ClientEvents.clientTime = msgIn.time;
			ClientEvents.clientActive = true;
			ClientEvents.clientQuitGame = msgIn.quitGame;
			final Minecraft mc = Minecraft.getInstance();
			mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.start", msgIn.time).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}
}
