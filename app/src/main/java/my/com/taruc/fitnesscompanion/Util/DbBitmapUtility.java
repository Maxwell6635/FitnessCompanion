package my.com.taruc.fitnesscompanion.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Hexa-Jackson on 12/26/2015.
 */
public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, Base64.DEFAULT, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, Base64.DEFAULT, image.length);
    }

    // To convert Image to String
    public static String encodeImagetoString(Bitmap bitmap) {
        String encodedString;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return encodedString;
    }

    //To convert String to Image
    public static Bitmap getImageFromJSon(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.URL_SAFE);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
