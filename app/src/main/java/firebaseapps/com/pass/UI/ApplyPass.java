package firebaseapps.com.pass.UI;

import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.*;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.RequestQueue;


import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.OPTION_SELECTED;
import firebaseapps.com.pass.SericeNReceiver.MyService;
import firebaseapps.com.pass.Utils.NetworkUtil;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.View_Pass;
import mohitbadwal.rxconnect.RxConnect;

public class ApplyPass extends AppCompatActivity {


    private LinearLayout applypass;
    private LinearLayout checkpassstatus;
    private LinearLayout EnterVehicleDetails;
    private LinearLayout ViewPass;
    private String user;
    private LinearLayout ApplicationPreview;
    private LinearLayout changepassdetails;
    public static  String OPTION;
    public static  String OPTION_SELECTED; //Vehicle or Application Preview or Pass Preview

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);




        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_pass);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.mipmap.ic_home);

        ApplicationPreview=(LinearLayout) findViewById(R.id.ApplicationPreview);                     //Preview of the Application
        EnterVehicleDetails=(LinearLayout)findViewById(R.id.EnterVehicleDetails);                     //To Enter Vehicle info of a pass
        applypass=(LinearLayout)findViewById(R.id.ApplyForAPass);                                        //To apply for new pass
        checkpassstatus=(LinearLayout)findViewById(R.id.CheckPassStatus);                             //To check pass status
        changepassdetails=(LinearLayout)findViewById(R.id.ChangePassDetails);                         //To change the details of the pass
        ViewPass=(LinearLayout)findViewById(R.id.ViewPass);                                           //To view the pass
        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext());       //Gets the network status


        if(status.equals("Wifi enabled") || status.equals("Mobile data enabled"))
        {
           if(!isMyServiceRunning(MyService.class)    /*Checks if service is ruuning */ )
            {
                Intent serviceIntent = new Intent(ApplyPass.this, MyService.class);
                startService(serviceIntent);                                                //starts the service if it is not running
            }
        }
        applypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Takes user to the new activity on applyling pass
                Intent Passdetail=new Intent(ApplyPass.this,Passdetails.class);     //Allows user to fill up a new pass application
                startActivity(Passdetail);

            }
        });

       checkpassstatus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent check=new Intent(ApplyPass.this,CheckPassDetails.class);      //Allows user to check pass details
               startActivity(check);
           }
       });
        changepassdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {



                         new AlertDialog.Builder(ApplyPass.this)
                        .setTitle("Select appropriate option")
                        .setMessage("Select appropriate option")
                        .setPositiveButton("Change Application detail", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                OPTION="Change Details";
                                Intent ChangeDetails=new Intent(ApplyPass.this, firebaseapps.com.pass.UI.ChangeDetails.class);    //Allows user to change the DOJ
                                startActivity(ChangeDetails);
                            }
                        })
                        .setNegativeButton("Request Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {



                                OPTION="Cancel request";
                                Intent ChangeDetails=new Intent(ApplyPass.this, firebaseapps.com.pass.UI.ChangeDetails.class);    //Allows user to cancel application
                                startActivity(ChangeDetails);

                                  // do nothing
                            }
                        })
                        .setIcon(R.mipmap.pass)
                        .show();

            }
        });

        ViewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OPTION_SELECTED= firebaseapps.com.pass.Constants.OPTION_SELECTED.OPTION_PASS_VIEW;
                Intent view_pass=new Intent(ApplyPass.this,View_Pass.class);            //Allows user to view a  Pass
                startActivity(view_pass);
            }
        });
        EnterVehicleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OPTION= "Update Vehicle Details";
                Intent view_pass=new Intent(ApplyPass.this,MovingCarSplash.class);            //Allows user to enter Vehicle Details pass
                startActivity(view_pass);
             //   Intent Vehicle_Details=new Intent(ApplyPass.this,Vehicles.class);
              //  startActivity(Vehicle_Details);

            }
        });
        ApplicationPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OPTION_SELECTED= firebaseapps.com.pass.Constants.OPTION_SELECTED.OPTION_APPLICATION_PREVIEW;
                Intent view_pass=new Intent(ApplyPass.this,View_Pass.class);            //Allows user to view submitted Application
                startActivity(view_pass);



            }
        });


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {                         //Checks if MyService is running
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
