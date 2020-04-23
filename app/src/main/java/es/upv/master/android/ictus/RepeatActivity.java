package es.upv.master.android.ictus;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.List;

public class RepeatActivity extends AppCompatActivity {

   private static final int SPEECH_REQUEST_CODE = 1345;
   private boolean pulsadoPasar = false;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      MainActivity.rec.iniRepeat = System.currentTimeMillis();
      super.onCreate(savedInstanceState);
      setContentView(R.layout.repeat);
      TextoAVoz.getInstance().habla(getResources().getText(R.string.repite) + ". " + getResources().getText(R.string.frase_a_repetir),
              new UtteranceProgressListener() {
                 @Override
                 public void onStart(String utteranceId) {
                 }

                 @Override
                 public void onDone(String utteranceId) {
                    if (!pulsadoPasar) repeat(null);
                 }

                 @Override
                 public void onError(String utteranceId) {
                 }
              }
      );
   }

   public void repeat(View v) {
      Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
              RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      startActivityForResult(intent, SPEECH_REQUEST_CODE);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode,
                                   Intent datos) {
      if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
         List<String> results = datos.getStringArrayListExtra(
                 RecognizerIntent.EXTRA_RESULTS);
         MainActivity.rec.numRepeat++;
         String frase = getResources().getText(R.string.frase_a_repetir).toString().toLowerCase();
         for (String s : results) {
            Log.d("ICTUS", s);
            if (s.equals(frase)) {//"tengo que comprar una docena de huevos")
               MainActivity.rec.repeat = true;
               ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 90);
               toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
               pasarSiguienteActividad();
               return;
            }
         }
         TextView tv = findViewById(R.id.error);
         tv.setVisibility(View.VISIBLE);
         tv = findViewById(R.id.heEntendido);
         tv.setText(results.get(0));
         MainActivity.rec.lastBadRepeat = results.get(0);
         tv.setVisibility(View.VISIBLE);
         tv = findViewById(R.id.pulsa);
         tv.setText(getResources().getText(R.string.pulsa_altavoz)); //"Press on the speaker to try again."
         tv.setVisibility(View.VISIBLE);
      }
      super.onActivityResult(requestCode, resultCode, datos);
   }

   public void pass(View v) {
      MainActivity.rec.repeat = false;
      pulsadoPasar = true;
      pasarSiguienteActividad();
   }

   void pasarSiguienteActividad() {
      Intent i = new Intent(this, RaiseActivity.class);
      startActivity(i);
   }
}