package firebaseapps.com.pass.UI;


//PayPal email dwijrajbhattacharyya@gmail.com
//Password Maina123
//Tourist
//Tourist123

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Random;

import firebaseapps.com.pass.Constants.ConstantResponse;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.GMailSender;
import mohitbadwal.rxconnect.RxConnect;

public class MainActivity extends AppCompatActivity {



    private LinearLayout OTPIDS;
    private EditText OTPS; //To enter OTP
    private EditText Phone; //To enter Phone number
    private EditText EMAILID; //To enter EMAIL ID
    private Button buttons;   //Submit
    private final int GALLERY_OPEN=90; //Code for Gallery Permission
    private final Integer CAMERA = 0x4; //Code for Camera Permission
    private ProgressDialog prog;  //Progress Dialog
    private int OTPint;            //OTP generated in int format
    private String OTPstring;      //OTP generated in STring format
    private RxConnect rxConnect;   //Instance of RxConnect to do network calls
    public static SharedPreferences.Editor USER;  //Shared Prefs to store User Credentials internally
    public static SharedPreferences SHARED_PREF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SHARED_PREF=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE);
        USER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).edit();
        EMAILID=(EditText) findViewById(R.id.EMAIL);
        OTPIDS=(LinearLayout) findViewById(R.id.OTPID);
        rxConnect=new RxConnect(MainActivity.this);
        rxConnect.setCachingEnabled(false);
        OTPS=(EditText)findViewById(R.id.OTP);
        Phone=(EditText)findViewById(R.id.editText3);
        prog=new ProgressDialog(this);
        buttons=(Button)findViewById(R.id.button);


        /**
         To take run time gallery and camera Permissions
        **/

        if(Build.VERSION.SDK_INT>=23)
        {

            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,GALLERY_OPEN);


        }

        String USeR=SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER"); //If user has logged in previously retrieve  his details else we get NO_USER
        boolean Value=!(USeR.equals("NO_USER"));                                 //Compares the User name with NO_USER
        if(Value)     //If User logged in previously allow him to move forward with Apply Pass
        {
            Log.v("Username",SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER"));
            Intent MAIN=new Intent(MainActivity.this,ApplyPass.class);     //Redirects the logged in user to Home page
            finish();
            startActivity(MAIN);
        }
        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                   if ((Phone.getText().toString().length()==10)&& !TextUtils.isEmpty(EMAILID.getText().toString().trim())
                        &&EMAILID.getText().toString().trim().contains("@")) {            //makes sure that user enter his/her phone number


                    prog.setMessage("Signing you..");

                       /**
                        Code to generate an OTP for user and send it Via SMS and Email
                        **/

                    final String IMEI = Phone.getText().toString().trim();
                    final String EMAIL = IMEI + "@" + IMEI + ".com";
                    final String PASSWORD = IMEI;
                    Random rn = new Random();
                    int n = 999 - 99;
                    int i = rn.nextInt() % n;
                    if(i<0)
                    {
                        i*=-1;
                    }
                    OTPint=  99 + i;
                    OTPstring=String.valueOf(OTPint);

                    Toast.makeText(getApplicationContext(),"YOUR OTP IS "+OTPstring,Toast.LENGTH_SHORT ).show(); //Displaying the OTP do remove while productiion

                       /**
                        Send the OTP to the user Via Email
                        **/
                       new Thread(new Runnable() {
                        @Override
                        public void run() {   //Network Calls should be done in background thread they don't work on UI Thread

                            GMailSender sender = new GMailSender(Constants.EMAIL_SENDER
                                    , Constants.EMAIL_PASSWORD_SENDER);
                            try {
                                sender.sendMail("OTP from Pass", "One time password is " +
                                                OTPstring, Constants.EMAIL_SENDER,
                                        EMAILID.getText().toString().trim());
                            } catch (Exception e) {

                                 Log.e("ERROR", e.getMessage(), e);

                            }

                        }
                    }).start();
                       /**
                        Send OTP  Via SMS on Entered Mobile Number
                        **/


                       OTPS.setVisibility(View.VISIBLE);
                       OTPS.setEnabled(true);
                       Phone.setEnabled(false);
                       OTPIDS.setVisibility(View.VISIBLE);





                    buttons.setText("GET STARTED");
                    OTPS.setVisibility(View.VISIBLE);
                    Phone.setEnabled(false);
                    OTPIDS.setVisibility(View.VISIBLE);
                    buttons.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final  String   OTP_INPUT=OTPS.getText().toString().trim();
                            if(OTP_INPUT.equals(OTPstring))   //Check if entered OTP and OTP generated are Equal
                            {

                                prog.show();

                                rxConnect.setParam(Constants.REGISTRATION_USER_MOBILE_KEY,Phone.getText().toString().trim());
                                rxConnect.setParam(Constants.REGISTRATION_USER_EMAIL_KEY,EMAILID.getText().toString().trim());
                                rxConnect.execute(Constants.OFFLINE_REGISTRATION_URL,RxConnect.POST, new RxConnect.RxResultHelper() {


                                    @Override
                                    public void onResult(String result) {

                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Log.v("Result",result);

                                        prog.dismiss();

                                        try {
                                            JSONObject jsonObject=new JSONObject(result);
                                            String MESSAGE=jsonObject.getString("msg");

                                            if(MESSAGE.equals(ConstantResponse.REGISTRATION_SUCCESS_RESPONSE)||MESSAGE.contains("Allowed"))
                                            {
                                                /**
                                                  If user's details are new then register him and let's him proceed forward else
                                                  if even mobile number or email is used previously a check is made if both email
                                                  and password belong's to a particular user then user is signed in and allowed
                                                  to proceed forward
                                                 **/

                                                USER.putString(Constants.SHARED_PREF_KEY,Phone.getText().toString().trim()).commit();
                                                Intent MAIN=new Intent(MainActivity.this,ApplyPass.class);
                                                finish();
                                                startActivity(MAIN);

                                            }

                                        }
                                        catch (Exception ex)
                                        {

                                        }

                                    }

                                    @Override
                                    public void onNoResult() {
                                        //do something

                                        prog.dismiss();
                                        Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        //do somenthing on error

                                        prog.dismiss();
                                        Log.v("Response",throwable.getMessage()+"MSG");
                                        Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();


                                    }


                                });

                            }
                            else if( OTPS.getVisibility()== View.INVISIBLE)
                            {

                                Toast.makeText(getApplicationContext(),"OTP couldn't be sent ensure internet connection",Toast.LENGTH_SHORT).show();

                            }
                            else if(OTP_INPUT.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(),"Enter the correct OTP",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"OTP doesnot match",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter Details ",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 90:
                askForPermission(android.Manifest.permission.CAMERA,CAMERA);
                break;

            case 0x4:

                break;
        }


    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {


                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }


    }


}