package firebaseapps.com.pass.SericeNReceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**This service checks for change in application status of the logged in user if there is change in any of
 * his applied pass
 * this service sends a broadcast with a definite action which intern is responsible to firing
 * notifications
 */


public class MyService extends Service {

    public MyService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {       //Runs for the very first time the service runs
        super.onStart(intent, startId);



    }

    @Override
    public void onDestroy() {                               //Runs when service is destroyed
        super.onDestroy();
      //  Toast.makeText(getApplicationContext(),"Can't check application status please check network connectivity",Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {  //Runs everytime the service starts



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
