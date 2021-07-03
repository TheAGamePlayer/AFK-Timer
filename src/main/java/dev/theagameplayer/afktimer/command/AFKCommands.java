package dev.theagameplayer.afktimer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public final class AFKCommands {
	public static void build(CommandDispatcher<CommandSource> dispatcherIn, Commands.EnvironmentType environmentTypeIn) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("afktimer");
		builder.then(ClientTimerCommand.register());
		if (environmentTypeIn == Commands.EnvironmentType.DEDICATED)
			builder.then(ServerTimerCommand.register());
		dispatcherIn.register(builder);
	}
}
