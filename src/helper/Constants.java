package helper;

/**
 * Created by RedShift on 1.9.2016..
 */
public class Constants {


    public class Database {

        public static final String DB_NAME = "users.db";
        public static final int DB_VERSION = 1;
        public static final String TABLE_NAME = "emotivuser";

        public static final String DROP_QUERY = "DROP TABLE IF EXIST " + TABLE_NAME;

        public static final String GET_USERS_QUERY = "SELECT * FROM " + TABLE_NAME;

        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}
