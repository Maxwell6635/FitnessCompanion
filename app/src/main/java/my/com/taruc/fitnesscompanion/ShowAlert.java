package my.com.taruc.fitnesscompanion;

/**
 * Created by Hexa-Jackson Foo on 10/10/2015.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ShowAlert {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        if (status != null) {
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        // Showing Alert Message
        builder.show();
    }
}