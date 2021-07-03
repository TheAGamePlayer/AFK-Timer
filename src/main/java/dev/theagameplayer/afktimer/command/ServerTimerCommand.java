package dev.theagameplayer.afktimer.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import dev.theagameplayer.afktimer.AFKEventManager.ServerEvents;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.TimeArgument;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public final class ServerTimerCommand {
	public static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("server")
				.requires(player -> {
					return player.hasPermission(4);
				}).then(Commands.literal("start").then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
					ServerEvents.serverTime = IntegerArgumentType.getInteger(ctx, "time");
					ServerEvents.serverActive = true;
					ctx.getSource().sendSuccess(new TranslationTextComponent("commands.afktimer.server.start", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)), true);
					return 0;
				}))).then(Commands.literal("stop").executes(ctx -> {
					ServerEvents.serverActive = false;
					ServerEvents.serverTick = 0;
					ctx.getSource().sendSuccess(new TranslationTextComponent("commands.afktimer.server.stop").withStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)), true);
					return 0;
				})).then(Commands.literal("query").executes(ctx -> {
					ctx.getSource().sendSuccess(new TranslationTextComponent("commands.afktimer.server.query", ServerEvents.serverTime - ServerEvents.serverTick).withStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)), false);
					return 0;
				})).then(Commands.literal("extend").then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
					ServerEvents.serverTime += IntegerArgumentType.getInteger(ctx, "time");
					ctx.getSource().sendSuccess(new TranslationTextComponent("commands.afktimer.server.extend", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)), true);
					return 0;
				})));
	}
}
