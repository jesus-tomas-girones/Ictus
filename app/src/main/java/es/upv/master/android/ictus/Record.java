package es.upv.master.android.ictus;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by Jesús Tomás on 13/03/2018.
 */

public class Record {
   public static int ANGLE_COUNT = 100;
   boolean smile = false;
   double pSmile = 0.0;
   long iniSmile;
   boolean repeat = false;
   int numRepeat = 0;
   String lastBadRepeat = "";
   long iniRepeat;
   boolean raise = false;
//   double angleRaise = 0;
   long iniRaise;
   long iniResult;
   double[] angleRaise = new double[ANGLE_COUNT];
   int angleIndex = 0;


   private static String FICHERO = "puntuaciones.txt";
   private Context context;

   public void save(Context context) {
      try {
         String stadoSD = Environment.getExternalStorageState();
         if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "No puedo escribir en la memoria externa",
                    Toast.LENGTH_LONG).show();
            return;
         }
         String FICHERO = context.getExternalFilesDir(null) + "/cerebral_stroque.txt";
         FileOutputStream f = new FileOutputStream(FICHERO, true);
         String texto = getDate(iniSmile) + "; " +
                 smile + "; " + pSmile + "; " + (iniRepeat - iniSmile) + "; " +
                 repeat + "; " + numRepeat + "; " + lastBadRepeat + "; " + (iniRaise - iniRepeat) + "; " +
                 raise + "; " + (iniResult - iniRaise);
         for (int n=0; n < 100; n++ ) {
            texto += angleRaise[angleIndex]+ "; ";
            if (angleIndex==ANGLE_COUNT-1) angleIndex = 0; else angleIndex++;
         }
         texto += "\n";
         f.write(texto.getBytes());
         f.close();
      } catch (Exception e) {
         Log.e("Ictus", e.getMessage(), e);
      }
   }

   private String getDate(long time) {
      Calendar cal = Calendar.getInstance(Locale.ENGLISH);
      cal.setTimeInMillis(time);
      String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
      return date;
   }

   public void putAngle(double a) {
      angleRaise[angleIndex] = a;
      if (angleIndex==ANGLE_COUNT-1) angleIndex = 0; else angleIndex++;
   }
}
