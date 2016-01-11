package simpledb.cmd;

public class EndCommand implements Command {

    @Override
    public void execute() {
        System.exit(0);
    }

}
