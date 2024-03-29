package dev.theagameplayer.afktimer.server.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import dev.theagameplayer.afktimer.event.AFKServerEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class ServerTimerCommand {
	public static final ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("server")
				.requires(player -> {
					return player.hasPermission(4);
				}).then(Commands.literal("start").then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
					AFKServerEvents.serverTime = IntegerArgumentType.getInteger(ctx, "time");
					AFKServerEvents.serverActive = true;
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.afktimer.server.start", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)), true);
					return 0;
				}))).then(Commands.literal("stop").executes(ctx -> {
					AFKServerEvents.serverActive = false;
					AFKServerEvents.serverTick = 0;
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.afktimer.server.stop").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)), true);
					return 0;
				})).then(Commands.literal("query").then(Commands.literal("time").executes(ctx -> {
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.afktimer.server.query", AFKServerEvents.serverTime - AFKServerEvents.serverTick).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)), false);
					return 0;
				}))).then(Commands.literal("extend").then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
					AFKServerEvents.serverTime += IntegerArgumentType.getInteger(ctx, "time");
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.afktimer.server.extend", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)), true);
					return 0;
				})));
	}
}
