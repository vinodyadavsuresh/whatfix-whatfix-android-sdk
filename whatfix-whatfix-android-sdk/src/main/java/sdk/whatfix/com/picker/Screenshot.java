package sdk.whatfix.com.picker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class Screenshot {

    public static String takescreenshot(View v) {

        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        /*encoding Base64*/
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        String imageString=Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        /*decoding Base64*/
        /*byte[] decodedByte = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);*/




        return imageString;
    }






}


