package dev.theagameplayer.afktimer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.theagameplayer.afktimer.server.commands.AFKCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class AFKEventManager {
	private static final Logger LOGGER = LogManager.getLogger(AFKTimerMod.MODID);
	
	public static final class ClientEvents extends CommonEvents {
		public static boolean clientQuitGame;
		public static boolean clientActive;
		public static int clientTime;
		public static int clientTick;
		
		public ClientEvents() {
			super();
			this.forgeBus.addListener(ClientEvents::loggedOut);
			this.forgeBus.addListener(ClientEvents::clientTick);
		}
		
		public static final void loggedOut(final ClientPlayerNetworkEvent.LoggingOut eventIn) {
			clientActive = false;
			clientTime = 0;
			clientTick = 0;
		}
		
		public static final void clientTick(final TickEvent.ClientTickEvent eventIn) {
			if (eventIn.phase == TickEvent.Phase.END && clientActive) {
				clientTick++;
				if (clientTick > clientTime) {
					clientActive = false;
					clientTime = 0;
					clientTick = 0;
					disconnectClient(Minecraft.getInstance());
					LOGGER.info("Client Timer ended!");
				}
			}
		}
		
		private static final void disconnectClient(final Minecraft mcIn) {
			final boolean local = mcIn.isLocalServer();
			mcIn.level.disconnect();
			if (local) {
				mcIn.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
			} else {
				mcIn.clearLevel();
			}
			if (clientQuitGame) {
				mcIn.stop();
			} else {
				final TitleScreen titleScreen = new TitleScreen();
				if (local) {
					mcIn.setScreen(titleScreen);
				} else {
					mcIn.setScreen(new JoinMultiplayerScreen(titleScreen));
				}
			}
		}
	}
	
	public static abstract class CommonEvents {
		protected final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		
		public CommonEvents() {
			this.forgeBus.addListener(CommonEvents::registerCommands);
		}
		
		public static final void registerCommands(final RegisterCommandsEvent eventIn) {
			AFKCommands.build(eventIn.getDispatcher(), eventIn.getCommandSelection());
		}
	}
	
	public static final class ServerEvents extends CommonEvents {
		private static MinecraftServer server;
		public static boolean serverActive;
		public static int serverTime;
		public static int serverTick;
		
		public ServerEvents() {
			super();
			this.forgeBus.addListener(ServerEvents::serverTick);
			this.forgeBus.addListener(ServerEvents::serverStarted);
		}
		
		public static final void serverStarted(final ServerStartedEvent eventIn) {
			server = eventIn.getServer();
		}
		
		public static final void serverTick(final TickEvent.ServerTickEvent eventIn) {
			if (server != null && eventIn.phase == TickEvent.Phase.END && serverActive) {
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
	}
}
