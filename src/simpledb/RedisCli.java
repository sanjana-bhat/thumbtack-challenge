package simpledb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import simpledb.cmd.Command;
import simpledb.model.Redis;
import simpledb.util.CommandUtil;

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

    public void executeCmdsFromStdin() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                execute(br.readLine());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

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

}
