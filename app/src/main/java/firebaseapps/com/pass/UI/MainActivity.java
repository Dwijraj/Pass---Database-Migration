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
    private EditText OTPS;
    private EditText Phone;
    private EditText EMAILID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private Button buttons;
    private final int GALLERY_OPEN=90;
    static final Integer WRITE_EXST = 0x2;
    private final Integer CAMERA = 0x4;
    private ProgressDialog prog;
    private DatabaseReference mDatabaseref;
    private DatabaseReference OTPDatabase;
    private RxConnect rxConnect1;
    private int OTPint;
    private RequestQueue requestQueue;
    private String OTPstring;
    private Random random;
    private RxConnect rxConnect;
    private int SEND_STATUS=0;
    public static SharedPreferences.Editor USER;
    public static SharedPreferences SHARED_PREF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SHARED_PREF=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE);
        USER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).edit();
        EMAILID=(EditText) findViewById(R.id.EMAIL);
        OTPIDS=(LinearLayout) findViewById(R.id.OTPID);
        rxConnect1=new RxConnect(MainActivity.this);
        rxConnect1.setCachingEnabled(false);
        rxConnect=new RxConnect(MainActivity.this);
        rxConnect.setCachingEnabled(false);
        OTPDatabase=FirebaseDatabase.getInstance().getReference().child("OTP");
        OTPS=(EditText)findViewById(R.id.OTP);
        Phone=(EditText)findViewById(R.id.editText3);
        prog=new ProgressDialog(this);
        buttons=(Button)findViewById(R.id.button);
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Users");//Points to the Users child  of the root parent


        // telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  //Telephony manager object is initiated




        if(Build.VERSION.SDK_INT>=23)
        {

            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,GALLERY_OPEN);


        }

        String USeR=SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER");
        boolean Value=!(USeR.equals("NO_USER"));
        boolean ValueF=USeR.equals("NO_USER");

        Log.v("Check",Value+" Opposite "+ValueF);

        if(Value)
        {
            Log.v("Username",SHARED_PREF.getString(Constants.SHARED_PREF_KEY,"NO_USER"));
            Intent MAIN=new Intent(MainActivity.this,ApplyPass.class);
            finish();
            startActivity(MAIN);
        }
        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                   if ((Phone.getText().toString().length()==10)&& !TextUtils.isEmpty(EMAILID.getText().toString().trim())
                        &&EMAILID.getText().toString().trim().contains("@")) {            //makes sure that user enter his/her phone number


                    prog.setMessage("Signing you..");

                    final String IMEI = Phone.getText().toString().trim();
                    //  final String IMEI = "455567888344432";
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
//                    Log.v("OTP",OTPstring);

                    Toast.makeText(getApplicationContext(),"YOUR OTP IS "+OTPstring,Toast.LENGTH_SHORT ).show();

                       new Thread(new Runnable() {
                        @Override
                        public void run() {

                            GMailSender sender = new GMailSender(Constants.EMAIL_SENDER
                                    , Constants.EMAIL_PASSWORD_SENDER);
                            try {
                                sender.sendMail("OTP from Pass", "One time password is " +
                                                OTPstring, Constants.EMAIL_SENDER,
                                        EMAILID.getText().toString().trim());
                            } catch (Exception e) {

                                 Log.e("ERROR", e.getMessage(), e);
                                SEND_STATUS=1;

                            }

                        }
                    }).start();

                   buttons.setText("GET STARTED");
                    OTPS.setVisibility(View.VISIBLE);
                    Phone.setEnabled(false);
                    OTPIDS.setVisibility(View.VISIBLE);
                    buttons.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final  String   OTP_INPUT=OTPS.getText().toString().trim();
                            if(OTP_INPUT.equals(OTPstring))
                            {

                                prog.show();

                                rxConnect.setParam(Constants.REGISTRATION_USER_MOBILE_KEY,Phone.getText().toString().trim());
                                rxConnect.setParam(Constants.REGISTRATION_USER_EMAIL_KEY,EMAILID.getText().toString().trim());
                                rxConnect.execute(Constants.REGISTRATION_URL,RxConnect.POST, new RxConnect.RxResultHelper() {


                                    @Override
                                    public void onResult(String result) {

                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Log.v("Result",result);

                                        prog.dismiss();

                                        try {
                                            JSONObject jsonObject=new JSONObject(result);
                                            String MESSAGE=jsonObject.getString("msg");

                                            if(MESSAGE.equals(ConstantResponse.REGISTRATION_SUCCESS_RESPONSE))
                                            {
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
                    Toast.makeText(getApplicationContext(),"Enter Phone number...",Toast.LENGTH_SHORT).show();
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