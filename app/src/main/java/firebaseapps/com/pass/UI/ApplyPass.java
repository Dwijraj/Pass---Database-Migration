package firebaseapps.com.pass.UI;

import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.*;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import com.android.volley.RequestQueue;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebaseapps.com.pass.SericeNReceiver.MyService;
import firebaseapps.com.pass.Utils.NetworkUtil;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.View_Pass;
import mohitbadwal.rxconnect.RxConnect;

public class ApplyPass extends AppCompatActivity {


    private Button applypass;
    private Button checkpassstatus;
    private Button ViewPass;
    private String user;
    private Button changepassdetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_pass);


        applypass=(Button)findViewById(R.id.getstarted);                                        //To apply for new pass
        checkpassstatus=(Button)findViewById(R.id.checkpassstatus);                             //To check pass status
        changepassdetails=(Button)findViewById(R.id.Changepassdetails);                         //To change the details of the pass
        ViewPass=(Button)findViewById(R.id.Viewpass);                                           //To view the pass
        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext());       //Gets the network status

        /**If network is enable but the service is not running
        * The service is not running
         * the system will start the service
         * */
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
                        .setPositiveButton("Change Date of Journey", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Intent ChangeDetails=new Intent(ApplyPass.this, firebaseapps.com.pass.UI.ChangeDetails.class);    //Allows user to change the DOJ
                                startActivity(ChangeDetails);
                            }
                        })
                        .setNegativeButton("Request refund", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Intent In=new Intent(new Intent(ApplyPass.this,Refund.class));
                                startActivity(In);
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
                Intent view_pass=new Intent(ApplyPass.this,View_Pass.class);            //Allows user to view a particular pass
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
