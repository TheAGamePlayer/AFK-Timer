package dev.theagameplayer.afktimer.event;

import org.apache.logging.log4j.Logger;

import dev.theagameplayer.afktimer.AFKTimerMod;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class AFKServerEvents {
	private static final Logger LOGGER = AFKTimerMod.LOGGER;
	private static MinecraftServer server;
	public static boolean serverActive;
	public static int serverTime;
	public static int serverTick;

	public static final void serverStarted(final ServerStartedEvent pEvent) {
		server = pEvent.getServer();
	}

	public static final void serverTickPost(final ServerTickEvent.Post pEvent) {
		if (server == null || !serverActive) return;
		serverTick++;
		if (serverTick > serverTime) {
			serverActive = false;
			serverTime = 0;
			serverTick = 0;
			server.halt(false);
			LOGGER.info("Server Timer ended!");
		}
	}
}
