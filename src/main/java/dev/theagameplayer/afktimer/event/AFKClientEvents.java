package dev.theagameplayer.afktimer.event;

import org.apache.logging.log4j.Logger;

import com.mojang.realmsclient.RealmsMainScreen;

import dev.theagameplayer.afktimer.AFKTimerMod;
import dev.theagameplayer.afktimer.client.commands.AFKClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

public final class AFKClientEvents {
	private static final Logger LOGGER = AFKTimerMod.LOGGER;
	public static boolean clientQuitGame;
	public static boolean clientActive;
	public static int clientTime;
	public static int clientTick;

	public static final void loggingOut(final ClientPlayerNetworkEvent.LoggingOut pEvent) {
		clientActive = false;
		clientTime = 0;
		clientTick = 0;
	}

	public static final void registerClientCommands(final RegisterClientCommandsEvent pEvent) {
		AFKClientCommands.build(pEvent.getDispatcher());
	}

	public static final void clientTickPost(final ClientTickEvent.Post pEvent) {
		if (!clientActive) return;
		clientTick++;
		if (clientTick > clientTime) {
			clientActive = false;
			clientTime = 0;
			clientTick = 0;
			disconnectClient(Minecraft.getInstance());
			LOGGER.info("Client Timer ended!");
		}
	}

	private static final void disconnectClient(final Minecraft pMc) {
		final boolean local = pMc.isLocalServer();
		final ServerData serverData = pMc.getCurrentServer();
		pMc.level.disconnect();
		if (local) {
			pMc.disconnect(new GenericMessageScreen(Component.translatable("menu.savingLevel")));
		} else {
			pMc.disconnect();
		}
		if (clientQuitGame) {
			pMc.stop();
		} else {
			final TitleScreen titleScreen = new TitleScreen();
			if (local) {
				pMc.setScreen(titleScreen);
			} else if (serverData != null && serverData.isRealm()) {
				pMc.setScreen(new RealmsMainScreen(titleScreen));
			} else {
				pMc.setScreen(new JoinMultiplayerScreen(titleScreen));
			}
		}
	}
}
