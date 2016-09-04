package helper;

/**
 * Created by RedShift on 1.9.2016..
 */
public class Constants {


    public class Database {

        public static final String DB_NAME = "emotiv.db";
        public static final String TNAME_USER = "USER";
        public static final String TNAME_BASELINE = "BASELINE";
        public static final String TNAME_BASELINE_MEASURE = "BASELINE_MEASURE";
        public static final String TNAME_TEST = "TEST";
        public static final String TNAME_TEST_MEASURE = "TEST_MEASURE";

        //public static final String DROP_QUERY = "DROP TABLE IF EXIST " + TABLE_NAME;
        public static final String GET_USERS_QUERY = "SELECT * FROM " + TNAME_USER;

        public class User {
            public static final String ID = "ID";
            public static final String USERNAME = "USERNAME";
            public static final String PASSWORD = "PASSWORD";
            /***
             * CREATE USER
             */
            public static final String INSERT_USER = "INSERT INTO USER (" + USERNAME + ", " + PASSWORD + ")" +
                    "VALUES (?,?);";

            public static final String FIND_USER_BY_NAME = "SELECT * FROM USER WHERE " + USERNAME + "= ?;";

            public static final String FIND_TEST_BY_USER = "SELECT * FROM TEST JOIN BASELINE " +
                    "ON TEST.BASELINE_ID = BASELINE.ID JOIN USER " +
                    "ON BASELINE.USER_ID = USER.ID " +
                    "WHERE USER.ID = ?";
        }

        public class Baseline {
            public static final String ID = "ID";
            public static final String USER_ID = "USER_ID";
            /***
             * CREATE USER
             */
            public static final String INSERT_BASELINE_BY_USER = "INSERT INTO BASELINE (" + USER_ID + ")" +
                    "VALUES (?);";

            public static final String FIND_BASELINE_BY_USER = "SELECT * FROM BASELINE WHERE " + USER_ID + "= ?;";


        }

        public class BaselineMeasure {
            public static final String BASELINE_ID = "BASELINE_ID";
            public static final String NODE_ID = "NODE_ID";
            public static final String ALPHA = "ALPHA";
            public static final String BETALOW = "BETALOW";
            public static final String BETAHIGH = "BETAHIGH";
            public static final String GAMMA = "GAMMA";
            public static final String THETA = "THETA";
            /***
             * CREATE USER
             */
            public static final String INSERT_BASELINE_MEASURE = "INSERT INTO BASELINE_MEASURE (" +
                    BASELINE_ID + ", " + NODE_ID + ", " + ALPHA +
                    ", " + BETALOW + ", " + BETAHIGH + ", " +
                    GAMMA + ", " + THETA +
                    ")" + "VALUES (?,?,?,?,?,?,?);";

            public static final String GET_AVERAGE_MEASURE = "SELECT AVG(ALPHA) as ALPHA, AVG(BETALOW) as " +
                    "BETA_LOW, AVG(BETAHIGH) as BETA_HIGH, AVG(GAMMA) as GAMMA , AVG(THETA) as THETA from BASELINE_MEASURE " +
                    " WHERE BASELINE_ID=? GROUP BY BASELINE_ID";
        }

        public class Test {
            public static final String BASELINE_ID = "BASELINE_ID";
            public static final String ID = "ID";
            public static final String DESCRIPTION = "DESCRIPTION";
            public static final String GENRE = "GENRE";
            public static final String SONGNAME = "SONGNAME";
            public static final String ARTIST = "ARTIST";
            /***
             * CREATE USER
             */
            public static final String INSERT_TEST = "INSERT INTO TEST (" +
                    BASELINE_ID + ", " + DESCRIPTION + ", " + GENRE + ", " + SONGNAME +
                    ", " + ARTIST + " )" + "VALUES (?,?,?,?,?)";

            public static final String FIND_TEST_BY_BASELINE= "SELECT * FROM TEST WHERE " + BASELINE_ID + "= ?;";
        }

        public class TestMeasure {
            public static final String BASELINE_ID = "BASELINE_ID";
            public static final String TEST_ID = "TEST_ID";
            public static final String NODE_ID = "NODE_ID";
            public static final String ALPHA = "ALPHA";
            public static final String BETALOW = "BETALOW";
            public static final String BETAHIGH = "BETAHIGH";
            public static final String GAMMA = "GAMMA";
            public static final String THETA = "THETA";
            /***
             * CREATE USER
             */
            public static final String INSERT_TEST_MEASURE = "INSERT INTO TEST_MEASURE (" +
                    BASELINE_ID + ", " + NODE_ID + ", " + TEST_ID + ", " + ALPHA +
                    ", " + BETALOW + ", " + BETAHIGH + ", " + GAMMA + ", " + THETA +
                    ")" + "VALUES (?,?,?,?,?,?,?,?)";
        }
    }


}
