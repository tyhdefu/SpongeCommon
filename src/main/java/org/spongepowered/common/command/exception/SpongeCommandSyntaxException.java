package org.spongepowered.common.command.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.common.command.brigadier.context.SpongeCommandContext;
import org.spongepowered.common.text.SpongeTexts;

public class SpongeCommandSyntaxException extends CommandSyntaxException {

    private static final Text ERROR_MESSAGE = Text.of(TextColors.RED, "Error running command: ");

    private final CommandException innerException;
    private final SpongeCommandContext commandContext;

    public SpongeCommandSyntaxException(CommandException exception, SpongeCommandContext commandContext) {
        super(new SimpleCommandExceptionType(SpongeTexts.toComponent(exception.getText())), SpongeTexts.toComponent(exception.getText()));
        this.innerException = exception;
        this.commandContext = commandContext;
    }

    public SpongeCommandSyntaxException(CommandException exception, SpongeCommandContext commandContext, String command, int cursor) {
        super(new SimpleCommandExceptionType(SpongeTexts.toComponent(exception.getText())), SpongeTexts.toComponent(exception.getText()), command, cursor);
        this.innerException = exception;
        this.commandContext = commandContext;
    }

    @Override
    public synchronized Throwable getCause() {
        return this.innerException;
    }

    public SpongeCommandContext getCommandContext() {
        return this.commandContext;
    }

    public Text getTextMessage() {
        return Text.of(ERROR_MESSAGE, this.innerException.getText());
    }

    @Override
    public String getMessage() {
        return getTextMessage().toPlain();
    }

}
