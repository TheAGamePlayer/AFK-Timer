package dev.theagameplayer.afktimer.event;

import dev.theagameplayer.afktimer.server.commands.AFKServerCommands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class AFKBaseEvents {
	public static final void registerCommands(final RegisterCommandsEvent eventIn) {
		AFKServerCommands.build(eventIn.getDispatcher(), eventIn.getCommandSelection());
	}
}
