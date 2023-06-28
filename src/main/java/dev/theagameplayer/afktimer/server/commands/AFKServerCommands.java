package dev.theagameplayer.afktimer.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.theagameplayer.afktimer.AFKTimerMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class AFKServerCommands {
	public static final void build(final CommandDispatcher<CommandSourceStack> dispatcherIn, final Commands.CommandSelection selectionIn) {
		final LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.<CommandSourceStack>literal("afktimer");
		if (selectionIn == Commands.CommandSelection.DEDICATED || AFKTimerMod.DEBUG_SERVER)
			builder.then(ServerTimerCommand.register());
		dispatcherIn.register(builder);
	}
}
