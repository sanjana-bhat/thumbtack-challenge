package simpledb;

import java.util.Arrays;
import java.util.List;

public class Constants {

    private Constants() {

    }

    public static class Commands {

        public static final String SET = "SET";
        public static final String UNSET = "UNSET";
        public static final String GET = "GET";
        public static final String NUMEQUALTO = "NUMEQUALTO";
        public static final String END = "END";
        public static final String BEGIN = "BEGIN";
        public static final String ROLLBACK = "ROLLBACK";
        public static final String COMMIT = "COMMIT";
        public static final List<String> VALID_CMDS = Arrays.asList(
            SET, GET, UNSET, NUMEQUALTO, END, BEGIN,
            ROLLBACK, COMMIT
        );

    }
}
