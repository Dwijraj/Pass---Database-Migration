package firebaseapps.com.pass.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import dwijraj.FriskyAnim.FriskyTanslations;
import firebaseapps.com.pass.R;

public class MovingCarSplash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_car_splash);


        FriskyTanslations Wheel1=new FriskyTanslations(MovingCarSplash.this,R.id.Wheels1);
        FriskyTanslations Wheel2=new FriskyTanslations(MovingCarSplash.this,R.id.Wheels2);
        Wheel1.StartRotationClockWise();
        Wheel2.StartRotationClockWise();
        FriskyTanslations Car=new FriskyTanslations(MovingCarSplash.this,R.id.car);
        Car.MoveXBy(140,2000);
        Wheel1.MoveXBy(140,2000,true);
        Wheel2.MoveXBy(140,2000,true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ApplyPass.OPTION= "Update Vehicle Details";
                startActivity(new Intent(MovingCarSplash.this,ChangeDetails.class));
                finish();

            }
        },2000);



    }
}
