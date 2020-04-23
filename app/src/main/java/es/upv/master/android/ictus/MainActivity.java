package es.upv.master.android.ictus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

   public static Record rec;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      rec = new Record();
      super.onCreate(savedInstanceState);
      setContentView(R.layout.inicio);
      TextoAVoz.getInstance().inicializaYHabla(this,
              this.getResources().getString(R.string.comenzar));
   }

   public void start(View v){
      Intent i = new Intent(this, FaceTrackerActivity.class);
      startActivity(i);
   }

   public static void changeLang(Context context, String lang) {
      Locale myLocale = new Locale(lang);
      Locale.setDefault(myLocale);
      android.content.res.Configuration config = new android.content.res.Configuration();
      config.locale = myLocale;
      context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
   }
}
