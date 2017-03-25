package firebaseapps.com.pass.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.R;

public class Startup extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;


        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_startup);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                  SharedPreferences SHARED_PREF=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE);

                    String USeR=SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER");
                    boolean Value=!(USeR.equals("NO_USER"));
                    if(Value)
                    {

                        Intent MAIN=new Intent(Startup.this,ApplyPass.class);
                        finish();
                        startActivity(MAIN);
                    }
                    else
                    {
                        Intent mainIntent = new Intent(Startup.this,GetStarted.class);
                        Startup.this.startActivity(mainIntent);
                        Startup.this.finish();
                    }

                }
            }, SPLASH_DISPLAY_LENGTH);
        }


}
