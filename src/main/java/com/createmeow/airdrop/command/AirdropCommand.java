package com.createmeow.airdrop.command;

import com.createmeow.airdrop.airdrop.AirdropData;
import com.createmeow.airdrop.airdrop.AirdropScheduler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class AirdropCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("airdrop")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 3))
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                                .then(Commands.argument("z", IntegerArgumentType.integer())
                                                        .executes(AirdropCommand::execute))))));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        int x = IntegerArgumentType.getInteger(context, "x");
        int y = IntegerArgumentType.getInteger(context, "y");
        int z = IntegerArgumentType.getInteger(context, "z");

        AirdropData.Tier tier = AirdropData.Tier.fromLevel(level);
        BlockPos pos = new BlockPos(x, y, z);

        AirdropScheduler.spawnCommandAirdrop(context.getSource().getServer(), tier, pos);

        context.getSource().sendSuccess(
                () -> Component.literal("已生成" + tier.getSerializedName() + "空投于 (" + x + ", " + y + ", " + z + ")"),
                true);

        return 1;
    }
}