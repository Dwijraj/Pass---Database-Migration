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

import org.json.JSONObject;

import java.util.Random;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.ConstantResponse;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.GMailSender;
import mohitbadwal.rxconnect.RxConnect;

public class Login extends AppCompatActivity {



    private LinearLayout OTPIDS;
    private EditText OTPS; //To enter OTP
    private EditText Phone; //To enter Phone number
    private EditText EMAILID; //To enter EMAIL ID
    private Button Getting_Started;   //Submit
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
        setContentView(R.layout.activity_login);

        SHARED_PREF=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE);
        USER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).edit();
        EMAILID=(EditText) findViewById(R.id.EMAIL);
        OTPIDS=(LinearLayout) findViewById(R.id.OTPID);
        rxConnect=new RxConnect(Login.this);
        rxConnect.setCachingEnabled(false);
        OTPS=(EditText)findViewById(R.id.OTP);
        Phone=(EditText)findViewById(R.id.editText3);
        prog=new ProgressDialog(this);
        Getting_Started=(Button)findViewById(R.id.button);

        if(getSupportActionBar().isShowing())
        {
            getSupportActionBar().hide();
        }

        /**
         To take run time gallery and camera Permissions
        **/

        if(Build.VERSION.SDK_INT>=23)
        {

            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,GALLERY_OPEN);


        }

        String USeR=SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER"); //If user has logged in previously retrieve  his details else we get NO_USER
        boolean Value=!(USeR.equals("NO_USER"));                                 //Compares the User name with NO_USER
        if(Value)     //If User logged in previously allow him to move forward with Apply DisplayPass
        {
            Log.v("Username",SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER"));
            Intent MAIN=new Intent(Login.this,HomeScreen.class);     //Redirects the logged in user to Home page
            finish();
            startActivity(MAIN);
        }
        Getting_Started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                   if ((Phone.getText().toString().length()==10)&& !TextUtils.isEmpty(EMAILID.getText().toString().trim())
                        &&EMAILID.getText().toString().trim().contains("@")) {            //makes sure that user enter his/her phone number


                    prog.setMessage("Signing you..");

                       /**
                        Code to generate an OTP for user and send it Via SMS and Email
                        **/

                    final String MOBILE_NUMBER = Phone.getText().toString().trim();
                    final String EMAIL = MOBILE_NUMBER + "@" + MOBILE_NUMBER + ".com";
                    final String PASSWORD = MOBILE_NUMBER;
                    Random rn = new Random();
                    int n = 999 - 99;
                    int i = rn.nextInt() % n;
                    if(i<0)
                    {
                        i*=-1;
                    }
                    OTPint=  99 + i;
                    OTPstring=String.valueOf(OTPint);


                     //  Toasty.info(getApplicationContext(), "YOUR OTP IS "+OTPstring,Toast.LENGTH_SHORT, true).show();


                       /**
                        Send the OTP to the user Via Email
                        **/

                       new Thread(new Runnable() {
                        @Override
                        public void run() {   //Network Calls should be done in background thread they don't work on UI Thread

                            GMailSender sender = new GMailSender(Constants.EMAIL_SENDER
                                    , Constants.EMAIL_PASSWORD_SENDER);
                            try {
                                sender.sendMail("OTP from DisplayPass", "One time password is " +
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
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               rxConnect.setParam(Constants.SMS_PARAM_KEY_USER,Constants.SMS_PARAM_VALUE_USER);
                               rxConnect.setParam(Constants.SMS_PARAM_KEY_KEY,Constants.SMS_PARAM_VALUE_KEY);
                               rxConnect.setParam(Constants.SMS_PARAM_KEY_MOBILE,"91"+MOBILE_NUMBER);
                               rxConnect.setParam(Constants.SMS_PARAM_KEY_MESSAGE,"Your OTP is "+OTPstring);
                               rxConnect.setParam(Constants.SMS_PARAM_KEY_SENDERID,Constants.SMS_PARAM_VALUE_SENDERID);
                               rxConnect.setParam(Constants.SMS_PARAM_KEY_ACCUSAGE,Constants.SMS_PARAM_VALUE_ACCUSAGE);
                               rxConnect.execute(Constants.SMS_URL,RxConnect.GET, new RxConnect.RxResultHelper() {
                                   @Override
                                   public void onResult(String result) {
                                       //do something on result
                                       Toast.makeText(getApplicationContext(),OTPstring + result,Toast.LENGTH_SHORT).show();
                                       Toasty.info(getApplicationContext(),"OTP sent",Toast.LENGTH_LONG,true).show();
                                       Getting_Started.setText("GET STARTED");
                                       OTPS.setVisibility(View.VISIBLE);
                                       Phone.setEnabled(false);
                                       OTPIDS.setVisibility(View.VISIBLE);
                                   }
                                   @Override
                                   public void onNoResult() {
                                       //do something

                                       Toasty.info(getApplicationContext(),"Sorry OTP couldn't be send ",Toast.LENGTH_LONG,true).show();
                                       }
                                   @Override
                                   public void onError(Throwable throwable) {
                                       //do somenthing on error
                                       Toasty.info(getApplicationContext(),"Error sending OTP",Toast.LENGTH_LONG,true).show();

                                   }
                               });
                           }
                       }).start();


                       OTPS.setVisibility(View.VISIBLE);
                       OTPS.setEnabled(true);
                       Phone.setEnabled(false);
                       OTPIDS.setVisibility(View.VISIBLE);





                    Getting_Started.setText("GET STARTED");
                    OTPS.setVisibility(View.VISIBLE);
                    Phone.setEnabled(false);
                    OTPIDS.setVisibility(View.VISIBLE);
                    Getting_Started.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final  String   OTP_INPUT=OTPS.getText().toString().trim();
                            if(OTP_INPUT.equals(OTPstring))   //Check if entered OTP and OTP generated are Equal
                            {

                                prog.show();

                                rxConnect.setParam(Constants.REGISTRATION_USER_MOBILE_KEY,Phone.getText().toString().trim());
                                rxConnect.setParam(Constants.REGISTRATION_USER_EMAIL_KEY,EMAILID.getText().toString().trim());
                                rxConnect.execute(Constants.ONLINE_REGISTRATION_URL,RxConnect.POST, new RxConnect.RxResultHelper() {


                                    @Override
                                    public void onResult(String result) {


                                        prog.dismiss();

                                        Log.v("ResultSignIN",result);

                                        try {
                                            JSONObject jsonObject=new JSONObject(result);
                                            String MESSAGE=jsonObject.getString("msg");

                                            if(MESSAGE.equals(ConstantResponse.REGISTRATION_SUCCESS_RESPONSE)||MESSAGE.contains("Allowed"))
                                            {
                                                /**
                                                  If user's details are new then register him and let him proceed forward else
                                                  if even mobile number or email is used previously a check is made if both email
                                                  and password belongs to a particular user then user is signed in and allowed
                                                  to proceed forward
                                                 **/

                                                USER.putString(Constants.SHARED_PREF_KEY,Phone.getText().toString().trim()).commit();
                                                Intent MAIN=new Intent(Login.this,HomeScreen.class);
                                                finish();
                                                startActivity(MAIN);

                                            }
                                            else
                                            {
                                                Toasty.warning(getApplicationContext(),MESSAGE,Toast.LENGTH_SHORT).show();
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
                                        Toasty.error(getApplicationContext(),"Sorry there was an error",Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        //do somenthing on error

                                        prog.dismiss();

                                        Toasty.error(getApplicationContext(),"Sorry there was an error",Toast.LENGTH_LONG).show();


                                    }


                                });

                            }
                            else if( OTPS.getVisibility()== View.INVISIBLE)
                            {
                                Toasty.error(getApplicationContext(),"Sorry there was an error",Toast.LENGTH_LONG).show();


                            }
                            else if(OTP_INPUT.isEmpty())
                            {
                                Toasty.error(getApplicationContext(),"Please eneter OTP",Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toasty.error(getApplicationContext(),"Sorry there was an error",Toast.LENGTH_LONG).show();

                            }


                        }
                    });

                   }
                else
                {
                        Toasty.warning(getApplicationContext(),"Please enter your details",Toast.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(Login.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permission)) {


                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);
            }
        }


    }


}