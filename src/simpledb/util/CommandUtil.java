package simpledb.util;

import simpledb.Constants;
import simpledb.cmd.BeginCommand;
import simpledb.cmd.Command;
import simpledb.cmd.CommitCommand;
import simpledb.cmd.EndCommand;
import simpledb.cmd.GetCommand;
import simpledb.cmd.NumequaltoCommand;
import simpledb.cmd.RollbackCommand;
import simpledb.cmd.SetCommand;
import simpledb.cmd.UnsetCommand;
import simpledb.model.Redis;

public class CommandUtil {

    public static Command parseCommand(final String cmd, Redis redis) {
        if (cmd == null) {
            return null;
        }
        String[] parts = cmd.split(" ");
        if (parts.length == 3 && parts[0].equalsIgnoreCase(Constants.Commands.SET)) {
            return new SetCommand(parts[1], parts[2], redis);
        } else if (parts.length == 2 && parts[0].equalsIgnoreCase(Constants.Commands.GET)) {
            return new GetCommand(parts[1], redis);
        } else if (parts.length == 2 && parts[0].equalsIgnoreCase(Constants.Commands.UNSET)) {
            return new UnsetCommand(parts[1], redis);
        } else if (parts.length == 2 && parts[0].equalsIgnoreCase(Constants.Commands.NUMEQUALTO)) {
            return new NumequaltoCommand(parts[1], redis);
        } else if (parts.length == 1 && parts[0].equalsIgnoreCase(Constants.Commands.BEGIN)) {
            return new BeginCommand(redis);
        } else if (parts.length == 1 && parts[0].equalsIgnoreCase(Constants.Commands.ROLLBACK)) {
            return new RollbackCommand(redis);
        } else if (parts.length == 1 && parts[0].equalsIgnoreCase(Constants.Commands.COMMIT)) {
            return new CommitCommand(redis);
        } else if (parts.length == 1 && parts[0].equalsIgnoreCase(Constants.Commands.END)) {
            return new EndCommand();
        }
        return null;
    }

}
