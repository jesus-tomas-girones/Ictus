package es.upv.master.android.ictus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jesús Tomás on 06/03/2018.
 */

public class ResultsActivity extends Activity {
   private int errores;
   protected void onCreate(Bundle savedInstanceState) {
      MainActivity.rec.iniResult = System.currentTimeMillis();
      MainActivity.rec.save(this);
      super.onCreate(savedInstanceState);
      setContentView(R.layout.results);
      errores = 0;
      ponResultado(R.id.resultSmile, MainActivity.rec.smile);
      ponResultado(R.id.resultRepeat, MainActivity.rec.repeat);
      ponResultado(R.id.resultRaise, MainActivity.rec.raise);
      TextView tv = findViewById(R.id.contactingEmer);
      if (errores>1){
         tv.setVisibility(View.VISIBLE);
      } else {
         tv.setVisibility(View.INVISIBLE);
      }
      TextoAVoz.getInstance().habla(getResources().getText(R.string.resultados).toString());

   }

   void ponResultado(int id, boolean ok){
      TextView tv = findViewById(id);
      if (ok) {
         tv.setText("✓");
         tv.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
      } else {
         tv.setText("✗");
         tv.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
         errores++;
      }
   }

   public void dial(View v) {
      Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "061", null));
      startActivity(intent);
   }

   public void mainMenu(View v){
      Intent i = new Intent(this, MainActivity.class);
      startActivity(i);
   }

}