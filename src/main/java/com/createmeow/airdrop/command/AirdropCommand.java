package com.createmeow.airdrop.command;

import com.createmeow.airdrop.airdrop.AirdropData;
import com.createmeow.airdrop.airdrop.AirdropScheduler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class AirdropCommand {
    private static final List<String> TYPES = Arrays.asList("manual", "timed");

    private static final SuggestionProvider<CommandSourceStack> TYPE_SUGGESTION =
            (context, builder) -> {
                for (String type : TYPES) {
                    builder.suggest(type);
                }
                return builder.buildFuture();
            };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("airdrop")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 3))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(AirdropCommand::executeDefault)
                                        .then(Commands.argument("type", StringArgumentType.word())
                                                .suggests(TYPE_SUGGESTION)
                                                .executes(AirdropCommand::executeWithType)))));
    }

    private static int executeDefault(CommandContext<CommandSourceStack> context) {
        return execute(context, false);
    }

    private static int executeWithType(CommandContext<CommandSourceStack> context) {
        String type = StringArgumentType.getString(context, "type").toLowerCase();
        boolean isTimed = "timed".equals(type);
        return execute(context, isTimed);
    }

    private static int execute(CommandContext<CommandSourceStack> context, boolean isTimed) {
        int level = IntegerArgumentType.getInteger(context, "level");
        AirdropData.Tier tier = AirdropData.Tier.fromLevel(level);

        BlockPos pos;
        try {
            pos = BlockPosArgument.getSpawnablePos(context, "pos");
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("airdrop.message.command.invalid_position"));
            return 0;
        }

        AirdropScheduler.spawnCommandAirdrop(context.getSource().getServer(), tier, pos, isTimed);

        String typeName = isTimed ?
                Component.translatable("airdrop.type.timed").getString() :
                Component.translatable("airdrop.type.manual").getString();

        context.getSource().sendSuccess(
                () -> Component.translatable("airdrop.message.command.spawned",
                        tier.getDisplayName(),
                        pos.getX(), pos.getY(), pos.getZ(),
                        typeName),
                true);

        return 1;
    }
}