package es.upv.master.android.ictus;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

class TextoAVoz {
   private static final TextoAVoz ourInstance = new TextoAVoz();

   static TextoAVoz getInstance() {
      return ourInstance;
   }

   private TextoAVoz() {
   }

   private Context contexto;
   private TextToSpeech textToSpeech;
   private AudioManager audioManager;

   public void inicializa(final Context contexto) {
      inicializaYHabla(contexto, "");
   }

   public void inicializaYHabla(final Context contexto, final String frase) {
      this.contexto = contexto;
      textToSpeech = new TextToSpeech(contexto, new TextToSpeech.OnInitListener() {
         @Override
         public void onInit(int status) {
            // TODO Auto-generated method stub
            if (status == TextToSpeech.SUCCESS) {
//               int result=tts.setLanguage(Locale.US);

               // Locale locale = Locale.getDefault();
               Locale locale = contexto.getResources().getConfiguration().locale; //
               //  if (!locale.getLanguage().equals("es")) locale = Locale.US;
               int result = textToSpeech.setLanguage(locale);
               //int result=textToSpeech.setLanguage(new Locale("esp", "ESP"));
               if (result == TextToSpeech.LANG_MISSING_DATA ||
                       result == TextToSpeech.LANG_NOT_SUPPORTED) {
                  Log.e("Ictus", "TTS: This Language is not supported");
               } else if (frase != null && !frase.isEmpty()) {
                  habla(frase);
               }
            } else
               Log.e("Ictus", "TTS:Initilization Failed!");
         }
      });

/*      audioManager = (AudioManager) contexto.getSystemService(AUDIO_SERVICE);
      audioManager.setMode(AudioManager.MODE_IN_CALL);
      audioManager.setSpeakerphoneOn(true);*/
   }

   public void habla(String str) {
      textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH/*QUEUE_ADD*/, null);
      //Si queremos un retardo tras la lectura
/*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         textToSpeech.playSilentUtterance(500, TextToSpeech.QUEUE_ADD, null);
      }*/
//      textToSpeech.setSpeechRate(0.0f);
//      textToSpeech.setPitch(0.0f);
   }

   public void habla(String str, UtteranceProgressListener escuchador) {
      textToSpeech.setOnUtteranceProgressListener(escuchador);
      HashMap<String, String> parametros = new HashMap<String, String>();
      parametros.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "123456");
      textToSpeech.speak(str, TextToSpeech.QUEUE_ADD, parametros);
   }

}