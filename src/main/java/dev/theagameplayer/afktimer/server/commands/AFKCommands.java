package dev.theagameplayer.afktimer.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class AFKCommands {
	public static final void build(final CommandDispatcher<CommandSourceStack> dispatcherIn, final Commands.CommandSelection selectionIn) {
		final LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.<CommandSourceStack>literal("afktimer");
		builder.then(ClientTimerCommand.register());
		if (selectionIn == Commands.CommandSelection.DEDICATED)
			builder.then(ServerTimerCommand.register());
		dispatcherIn.register(builder);
	}
}
