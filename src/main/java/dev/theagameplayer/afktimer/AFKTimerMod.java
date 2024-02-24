package dev.theagameplayer.afktimer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.theagameplayer.afktimer.event.AFKBaseEvents;
import dev.theagameplayer.afktimer.event.AFKClientEvents;
import dev.theagameplayer.afktimer.event.AFKServerEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = AFKTimerMod.MODID)
public final class AFKTimerMod {
	public static final String MODID = "afktimer";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final boolean DEBUG_SERVER = false;

	public AFKTimerMod(final IEventBus modEventBusIn) {
		if (FMLEnvironment.dist.isClient()) {
			attachClientEventListeners(modEventBusIn, NeoForge.EVENT_BUS);
			if (!DEBUG_SERVER) return;
		}
		attachServerEventListeners(modEventBusIn, NeoForge.EVENT_BUS);
	}

	public static final void attachClientEventListeners(final IEventBus modBusIn, final IEventBus forgeBusIn) {
		//Client
		forgeBusIn.addListener(AFKClientEvents::loggingOut);
		forgeBusIn.addListener(AFKClientEvents::registerClientCommands);
		forgeBusIn.addListener(AFKClientEvents::clientTick);
	}

	public static final void attachServerEventListeners(final IEventBus modBusIn, final IEventBus forgeBusIn) {
		//Base
		forgeBusIn.addListener(AFKBaseEvents::registerCommands);
		//Server
		forgeBusIn.addListener(AFKServerEvents::serverTick);
		forgeBusIn.addListener(AFKServerEvents::serverStarted);
	}
}