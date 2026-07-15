package com.createmeow.airdrop.command;

import com.createmeow.airdrop.airdrop.AirdropData;
import com.createmeow.airdrop.airdrop.AirdropLootManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AirdropConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("airdrop-config")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("operation", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("add");
                                    builder.suggest("rm");
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 3))
                                        .then(Commands.argument("itemId", StringArgumentType.word())
                                                .then(Commands.argument("min", IntegerArgumentType.integer(1))
                                                        .then(Commands.argument("max", IntegerArgumentType.integer(1))
                                                                .then(Commands.argument("weight", IntegerArgumentType.integer(1))
                                                                        .executes(AirdropConfigCommand::execute))))))));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        String operation = StringArgumentType.getString(context, "operation");
        int level = IntegerArgumentType.getInteger(context, "level");
        String itemId = StringArgumentType.getString(context, "itemId");
        int min = IntegerArgumentType.getInteger(context, "min");
        int max = IntegerArgumentType.getInteger(context, "max");
        int weight = IntegerArgumentType.getInteger(context, "weight");

        AirdropData.Tier tier = AirdropData.Tier.fromLevel(level);

        if (operation.equalsIgnoreCase("add")) {
            AirdropLootManager.addEntry(tier, itemId, min, max, weight);
            context.getSource().sendSuccess(
                    () -> Component.translatable("airdrop.message.config.added", itemId, tier.getDisplayName()),
                    true);
        } else if (operation.equalsIgnoreCase("rm")) {
            boolean removed = AirdropLootManager.removeEntry(tier, itemId);
            if (removed) {
                context.getSource().sendSuccess(
                        () -> Component.translatable("airdrop.message.config.removed", tier.getDisplayName(), itemId),
                        true);
            } else {
                context.getSource().sendFailure(
                        Component.translatable("airdrop.message.config.not_found", itemId, tier.getDisplayName()));
            }
        } else {
            context.getSource().sendFailure(Component.translatable("airdrop.message.config.unknown_operation", operation));
        }

        return 1;
    }
}