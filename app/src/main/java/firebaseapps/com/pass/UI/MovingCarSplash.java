package firebaseapps.com.pass.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import dwijraj.FriskyAnim.FriskyTanslations;
import firebaseapps.com.pass.R;

public class MovingCarSplash extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_car_splash);

        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int WIDTH=displayMetrics.heightPixels;
        int HEIGHT=displayMetrics.widthPixels;
        int PixelDensity=displayMetrics.densityDpi;

        ImageView imageView= (ImageView) findViewById(R.id.car);
        imageView.animate().translationXBy(-250).setInterpolator(new LinearInterpolator()).setDuration(0).start();
        findViewById(R.id.Wheels1).animate().translationXBy(-250).setInterpolator(new LinearInterpolator()).setDuration(0).start();
        findViewById(R.id.Wheels2).animate().translationXBy(-250).setInterpolator(new LinearInterpolator()).setDuration(0).start();

        int Distance=WIDTH;
        int Duration=2000;
        
        FriskyTanslations Wheel1=new FriskyTanslations(MovingCarSplash.this,R.id.Wheels1);
        FriskyTanslations Wheel2=new FriskyTanslations(MovingCarSplash.this,R.id.Wheels2);
        Wheel1.StartRotationClockWise();
        Wheel2.StartRotationClockWise();
        FriskyTanslations Car=new FriskyTanslations(MovingCarSplash.this,R.id.car);
        Car.MoveXBy(Distance,Duration);
        Wheel1.MoveXBy(Distance,Duration,true);
        Wheel2.MoveXBy(Distance,Duration,true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                HomeScreen.OPTION= "Update Vehicle Details";
                startActivity(new Intent(MovingCarSplash.this,ChangeDetails.class));
                finish();

            }
        },Duration);



    }
}
