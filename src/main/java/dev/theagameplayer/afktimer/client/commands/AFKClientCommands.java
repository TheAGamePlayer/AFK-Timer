package dev.theagameplayer.afktimer.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;

public final class AFKClientCommands {
	public static final void build(final CommandDispatcher<CommandSourceStack> dispatcherIn) {
		final LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.<CommandSourceStack>literal("afktimer");
		builder.then(ClientTimerCommand.register());
		dispatcherIn.register(builder);
	}
}
