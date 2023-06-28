package dev.theagameplayer.afktimer.client.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import dev.theagameplayer.afktimer.event.AFKClientEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class ClientTimerCommand {
	public static final ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("client")
				.requires(player -> {
					return player.hasPermission(0);
				}).then(Commands.literal("start").then(Commands.argument("time", TimeArgument.time()).then(Commands.literal("quitGame").executes(ctx -> {
					final Minecraft mc = Minecraft.getInstance();
					AFKClientEvents.clientTime = IntegerArgumentType.getInteger(ctx, "time");
					AFKClientEvents.clientActive = true;
					AFKClientEvents.clientQuitGame = true;
					mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.start", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
					return 0;
				})).executes(ctx -> {
					final Minecraft mc = Minecraft.getInstance();
					AFKClientEvents.clientTime = IntegerArgumentType.getInteger(ctx, "time");
					AFKClientEvents.clientActive = true;
					AFKClientEvents.clientQuitGame = false;
					mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.start", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
					return 0;
				}))).then(Commands.literal("stop").executes(ctx -> {
					final Minecraft mc = Minecraft.getInstance();
					AFKClientEvents.clientActive = false;
					AFKClientEvents.clientTick = 0;
					AFKClientEvents.clientQuitGame = false;
					mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.stop").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
					return 0;
				})).then(Commands.literal("query").then(Commands.literal("time").executes(ctx -> {
					final Minecraft mc = Minecraft.getInstance();
					mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.query", AFKClientEvents.clientTime - AFKClientEvents.clientTick).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
					return 0;
				}))).then(Commands.literal("extend").then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
					final Minecraft mc = Minecraft.getInstance();
					AFKClientEvents.clientTime += IntegerArgumentType.getInteger(ctx, "time");
					mc.player.sendSystemMessage(Component.translatable("commands.afktimer.client.extend", IntegerArgumentType.getInteger(ctx, "time")).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
					return 0;
				})));
	}
}
