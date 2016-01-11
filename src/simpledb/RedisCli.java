package simpledb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import simpledb.cmd.Command;
import simpledb.model.Redis;
import simpledb.util.CommandUtil;

/**
 * Redis CLI main that accepts commands from the user and executes the commands.
 */
public class RedisCli {

    private Redis redis = new Redis();

    public static void main(String[] args) {
        boolean readFromFile = false;
        String file = null;
        if (args.length > 0) {
            readFromFile = true;
            file = args[0];
        }
        RedisCli redisCli = new RedisCli();
        if (readFromFile) {
            redisCli.executeCmdsFromFile(file);
        } else {
            redisCli.executeCmdsFromStdin();
        }
    }

    public void execute(final String cmd) {
        Command command = CommandUtil.parseCommand(cmd, redis);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command, try one of these: " + Constants.Commands.VALID_CMDS);
        }
    }

    //Accept commands from Std Input
    public void executeCmdsFromStdin() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                execute(br.readLine());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    //Accept commands from a file
    public void executeCmdsFromFile(final String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String cmd;
            while ((cmd = br.readLine()) != null) {
                System.out.println(cmd);
                execute(cmd);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Redis getRedisInstance() {
        return redis;
    }

}
