package es.upv.master.android.ictus;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RaiseActivity extends AppCompatActivity implements SensorEventListener {
   private SensorManager sensorManager;
   boolean actividadActiva;
   int contador=0;
   private TextView angulo;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      MainActivity.rec.iniRaise = System.currentTimeMillis();
      super.onCreate(savedInstanceState);
      setContentView(R.layout.raise);
      angulo = (TextView) findViewById(R.id.angulo);
      sensorManager = (SensorManager) getSystemService(Context
              .SENSOR_SERVICE);
      TextoAVoz.getInstance().habla(getResources().getText(R.string.levanta_brazo_animación).toString());
   }

   @Override protected void onResume() {
      super.onResume();
      actividadActiva = true;
      contador = 0;
      Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
      if (sensor != null) {
         sensorManager.registerListener(this, sensor, SensorManager
                 .SENSOR_DELAY_NORMAL);
      } else {
         Toast.makeText(this, "¡Contador de pasos no encontrado!",
                 Toast.LENGTH_LONG).show();
      }
      ImageView anim = (ImageView) findViewById(R.id.imageView);
      ((AnimationDrawable) anim.getBackground()).start();

   }

   @Override protected void onPause() {
      super.onPause();
      actividadActiva = false;
      sensorManager.unregisterListener(this);
   }

   @Override public void onSensorChanged(SensorEvent event) {
      if (actividadActiva) {
         double a = event.values[1];
//         if (a<-90) a = a +180;
         a = a + 90;

         a += 180;
         a = a%360;
         a -=180;

         MainActivity.rec.putAngle(a);
         angulo.setText(String.valueOf(Math.abs(Math.round(a)))+"º");
         if (a>70 || a<-70)    contador ++;
         else if (contador>0) contador --;
         if (contador>25) {//&&(!MainActivity.rec.raise)) {
            MainActivity.rec.raise = true;
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 90);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            aResultados();
         }
      }
   }

   @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

   public void pass(View v){
      MainActivity.rec.raise = false;
      aResultados();
   }

   void aResultados(){
      actividadActiva = false;
      Intent i = new Intent(this, ResultsActivity.class);
      startActivity(i);
   }

}
