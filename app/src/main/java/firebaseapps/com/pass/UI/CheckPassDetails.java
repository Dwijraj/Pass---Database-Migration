package firebaseapps.com.pass.UI;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Constants.OPTION_SELECTED;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.ViewPass;
import firebaseapps.com.pass.View_Pass;
import mohitbadwal.rxconnect.RxConnect;

public class CheckPassDetails extends AppCompatActivity {

    private ProgressDialog m2Dialog;
    private RxConnect rxConnect;
    private EditText pass_no;
    private Button PROFILE_PIC_SCAN_PIC;
    private Button VEHICLE;
    private Button Check;
    private EditText Id_no;
    private EditText OTP;
    private Button Submit;
    private TextView DOJ_DISPLAY;
    private ImageButton DATEPICKER;
    public static String  ONCHANGEDETAILS=null;
    private DatePicker datePicker;
    private int mYear,mMonth,mDay;
    private String DateOfJourney=null;
    private String APLICANT_MOBILE;
    private  String  OTPstring;
    public  static  String CHANGEABLE=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pass_details);

        PROFILE_PIC_SCAN_PIC=(Button) findViewById(R.id.APPLICATION_CHANGE);
        VEHICLE=(Button) findViewById(R.id.VEHICLE_DETAIL_CHANGE);
        pass_no=(EditText)findViewById(R.id.view_pass_passno_check);
        Id_no=(EditText) findViewById(R.id.view_pass_Id_no_check);
        Check=(Button)findViewById(R.id.viewcheck);
        DOJ_DISPLAY=(TextView) findViewById(R.id.DOJ_TEXT_VIEW_CHECK);
        DATEPICKER=(ImageButton) findViewById(R.id.DOJ_VIEW_PASS_CHECK);


        findViewById(R.id.OTP_LAYOUT).setVisibility(View.INVISIBLE);

        rxConnect=new RxConnect(this);
        rxConnect.setCachingEnabled(false);
        m2Dialog=new ProgressDialog(this);


     /*   CHANGEABLE=OPTION_SELECTED.OPTION_CHANGEABLE_VEHICLE;
        Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
        finish();
        startActivity(NEW_INTENT); */

        DATEPICKER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentDate = Calendar.getInstance();

                final Calendar  m_three_months = Calendar.getInstance();

                m_three_months.add(Calendar.MONTH,2);
                mcurrentDate.add(Calendar.DAY_OF_MONTH,5);
                mYear  = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(CheckPassDetails.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        final  Calendar myCalendar = Calendar.getInstance();
                        //Calendar myCalenderCopy;
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                        // myCalenderCopy=myCalendar;

                        String myFormat = "dd-MM-yyyy"; //Change as you need
                        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                        DOJ_DISPLAY.setText(sdf.format(myCalendar.getTime()));
                        DateOfJourney=sdf.format(myCalendar.getTime());





                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                // mDatePicker.setTitle("Select date");
                mDatePicker.show();


            }
        });
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                final String PASS_NO=pass_no.getText().toString().trim();
                final String ID_NO=Id_no.getText().toString().trim();
                if(PASS_NO.isEmpty()||ID_NO.isEmpty()||DateOfJourney.isEmpty())
                {
                    Toasty.warning(getApplicationContext(),"Enter pass number",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    String REGISTERED_MOBILE_NUBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
                    rxConnect=new RxConnect(CheckPassDetails.this);

                    rxConnect.setParam("token_id",PASS_NO);
                    rxConnect.setParam("app_mobile",ID_NO);
                    rxConnect.setParam("user_mobile",REGISTERED_MOBILE_NUBER);
                    rxConnect.setParam("user","customer");
                    rxConnect.setParam("date_journey",DateOfJourney);

                    rxConnect.execute(Constants.PASS_RETREIVE_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                        @Override
                        public void onResult(String result) {

                            try {
                                Log.v("ResponseViewPass",result);


                                Vehicles.APPLICATION_NUMBER=PASS_NO;
                                JSONObject jsonObject=new JSONObject(result);
                                Log.v("OTPSTRINGQ!@3",jsonObject.toString());


                                ONCHANGEDETAILS=result;



                                JSONObject jsonObject1=jsonObject.getJSONObject("application_info");



                                APLICANT_MOBILE=JsonParser.JSONValue(jsonObject1,"application_mobile");

                                Log.v("OTPSTRINGQ!@3",APLICANT_MOBILE);
                                String APPLICATION_STATUS=  JsonParser.JSONValue(jsonObject1,"paid_status");

                                if(APPLICATION_STATUS.equals("2"))///*&&(jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER))||jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER)*/)
                                {

                                        Toasty.info(getApplicationContext(),"ReUpload your picture and scan Id",Toast.LENGTH_SHORT).show();
                                        PROFILE_PIC_SCAN_PIC.setVisibility(View.VISIBLE);

                                }
                                else  if(APPLICATION_STATUS.equals("3"))
                                {

                                    Toasty.info(getApplicationContext(),"ReUpload vehicle details",Toast.LENGTH_SHORT).show();
                                        VEHICLE.setVisibility(View.VISIBLE);

                                }
                                else if (APPLICATION_STATUS.equals("0"))
                                {
                                    Toasty.success(getApplicationContext(),"Your Application is under process",Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onNoResult() {

                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });

                }

            }
        });


        VEHICLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  if (!ONCHANGEDETAILS.isEmpty())
               // {
                Random rn = new Random();
                int n = 999 - 99;
                int i = rn.nextInt() % n;
                if(i<0)
                {
                    i*=-1;
                }
                int  OTPint=  99 + i;
                OTPstring=String.valueOf(OTPint);
                //update.setEnabled(false);

                rxConnect=new RxConnect(CheckPassDetails.this);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_USER,Constants.SMS_PARAM_VALUE_USER);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_KEY,Constants.SMS_PARAM_VALUE_KEY);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_MOBILE,"91"+APLICANT_MOBILE);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_MESSAGE,"Your OTP to is "+OTPstring);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_SENDERID,"INFOSM");
                rxConnect.setParam(Constants.SMS_PARAM_KEY_ACCUSAGE,"2");
                rxConnect.execute(Constants.SMS_URL,RxConnect.GET, new RxConnect.RxResultHelper() {
                    @Override
                    public void onResult(String result) {



                        Toasty.info(getApplicationContext(),"Done"+OTPstring,Toast.LENGTH_SHORT).show();
                        findViewById(R.id.OTP_LAYOUT).setVisibility(View.VISIBLE);

                    }
                    @Override
                    public void onNoResult() {
                        //do something
                        Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //do somenthing on error
                        Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();
                    }
                });
                    CHANGEABLE=OPTION_SELECTED.OPTION_CHANGEABLE_VEHICLE;
                findViewById(R.id.SUBMIT_OTP_CHECK_PASS).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText editText=(EditText) findViewById(R.id.ENTER_OTP_CHECK_PASS);

                        String ENTERED_OTP=editText.getText().toString();

                        if(ENTERED_OTP.equals(OTPstring))
                        {
                            if(CheckPassDetails.CHANGEABLE.equals(OPTION_SELECTED.OPTION_CHANGEABLE_VEHICLE))
                            {
                                Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
                                finish();
                                startActivity(NEW_INTENT);

                            }
                            else
                            {
                                Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
                                finish();
                                startActivity(NEW_INTENT);
                            }
                        }

                    }
                });
                  /*  Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
                    finish();
                    startActivity(NEW_INTENT);*/
               // }
            }
        });


        PROFILE_PIC_SCAN_PIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   if (!ONCHANGEDETAILS.isEmpty())
              //  {

                // {
                Random rn = new Random();
                int n = 999 - 99;
                int i = rn.nextInt() % n;
                if(i<0)
                {
                    i*=-1;
                }
                int  OTPint=  99 + i;
                OTPstring=String.valueOf(OTPint);
                //update.setEnabled(false);

                rxConnect=new RxConnect(CheckPassDetails.this);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_USER,Constants.SMS_PARAM_VALUE_USER);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_KEY,Constants.SMS_PARAM_VALUE_KEY);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_MOBILE,"91"+APLICANT_MOBILE);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_MESSAGE,"Your OTP to is "+OTPstring);
                rxConnect.setParam(Constants.SMS_PARAM_KEY_SENDERID,"INFOSM");
                rxConnect.setParam(Constants.SMS_PARAM_KEY_ACCUSAGE,"2");
                rxConnect.execute(Constants.SMS_URL,RxConnect.GET, new RxConnect.RxResultHelper() {
                    @Override
                    public void onResult(String result) {

                        Log.v("OTPSENT",result+"...");

                        Toasty.info(getApplicationContext(),"Done"+OTPstring,Toast.LENGTH_SHORT).show();
                        findViewById(R.id.OTP_LAYOUT).setVisibility(View.VISIBLE);

                    }
                    @Override
                    public void onNoResult() {
                        //do something
                        Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //do somenthing on error
                        Toast.makeText(getApplicationContext(),"OTP could not be sent",Toast.LENGTH_SHORT).show();
                    }
                });
                CHANGEABLE=OPTION_SELECTED.OPTION_CHANGEABLE_APPLICATION;
                findViewById(R.id.SUBMIT_OTP_CHECK_PASS).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText editText=(EditText) findViewById(R.id.ENTER_OTP_CHECK_PASS);

                        String ENTERED_OTP=editText.getText().toString();

                        if(ENTERED_OTP.equals(OTPstring))
                        {
                            if(CheckPassDetails.CHANGEABLE.equals(OPTION_SELECTED.OPTION_CHANGEABLE_VEHICLE))
                            {
                                Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
                                finish();
                                startActivity(NEW_INTENT);

                            }
                            else
                            {
                                Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
                                finish();
                                startActivity(NEW_INTENT);
                            }
                        }

                    }
                });
                  /*  Intent NEW_INTENT=new Intent(CheckPassDetails.this,Documentschange.class);
                    finish();
                    startActivity(NEW_INTENT);*/
                // }
            }
        });






    }

}
