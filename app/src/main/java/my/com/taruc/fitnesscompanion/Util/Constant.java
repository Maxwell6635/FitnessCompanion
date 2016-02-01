package my.com.taruc.fitnesscompanion.Util;

/**
 * Created by Hexa-Jackson on 1/1/2016.
 */
public class Constant {

    public static final Integer DB_Version = 3;
    public static final String alter_table_activityplan = "ALTER TABLE Activity_Plan " +
            "ADD COLUMN created_at DATETIME, " +
            "ADD COLUMN updated_at DATETIME";
    public static final String alter_table_event = "ALTER TABLE Event " +
            "ADD COLUMN title VARCHAR(255) AFTER url, " +
            "ADD COLUMN location VARCHAR(255), " +
            "ADD COLUMN eventdate VARCHAR(255)";
    public static final String FORGET_PASSWORD_URL = "http://www.seekt.asia/ServerRequest/ForgetPassword.php";
}
