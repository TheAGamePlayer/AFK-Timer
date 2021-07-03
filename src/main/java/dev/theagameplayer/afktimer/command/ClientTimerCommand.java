package dev.theagameplayer.afktimer.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import dev.theagameplayer.afktimer.network.AFKPacketHandler;
import dev.theagameplayer.afktimer.network.packet.CTimerExtendPacket;
import dev.theagameplayer.afktimer.network.packet.CTimerQueryPacket;
import dev.theagameplayer.afktimer.network.packet.CTimerStartPacket;
import dev.theagameplayer.afktimer.network.packet.CTimerStopPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.TimeArgument;

public final class ClientTimerCommand {
	public static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("client")
				.requires(player -> {
					return player.hasPermission(0);
				}).then(Commands.literal("start").then(Commands.argument("time", TimeArgument.time()).then(Commands.argument("quitGame", BoolArgumentType.bool()) .executes(ctx -> {
					AFKPacketHandler.sendToClient(new CTimerStartPacket(IntegerArgumentType.getInteger(ctx, "time"), BoolArgumentType.getBool(ctx, "quitGame")), ctx.getSource().getPlayerOrException());
					return 0;
				})))).then(Commands.literal("stop").executes(ctx -> {
					AFKPacketHandler.sendToClient(new CTimerStopPacket(), ctx.getSource().getPlayerOrException());
					return 0;
				})).then(Commands.literal("query").executes(ctx -> {
					AFKPacketHandler.sendToClient(new CTimerQueryPacket(), ctx.getSource().getPlayerOrException());
					return 0;
				})).then(Commands.literal("extend").then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
					AFKPacketHandler.sendToClient(new CTimerExtendPacket(IntegerArgumentType.getInteger(ctx, "time")), ctx.getSource().getPlayerOrException());
					return 0;
				})));
	}
}
