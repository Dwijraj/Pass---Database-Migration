package firebaseapps.com.pass.UI;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


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
    private TextView APPLICATION_STATUS_SHOW;
    private ImageButton DATEPICKER;
    public static String  ONCHANGEDETAILS=null;
    private DatePicker datePicker;
    private int mYear,mMonth,mDay;
    private String DateOfJourney=null;
    private String APLICANT_MOBILE;
    private  String  OTPstring;
    public  static  String CHANGEABLE=null;
    public  static String WHICH_VEHICLE_STATS_STATUS=null;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;

        }


        return super.onOptionsItemSelected(item);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pass_details);

        APPLICATION_STATUS_SHOW=(TextView) findViewById(R.id.viewstatus);
        PROFILE_PIC_SCAN_PIC=(Button) findViewById(R.id.APPLICATION_CHANGE);
        VEHICLE=(Button) findViewById(R.id.VEHICLE_DETAIL_CHANGE);
        pass_no=(EditText)findViewById(R.id.view_pass_passno_check);
        Id_no=(EditText) findViewById(R.id.view_pass_Id_no_check);
        Check=(Button)findViewById(R.id.viewcheck);
        DOJ_DISPLAY=(TextView) findViewById(R.id.DOJ_TEXT_VIEW_CHECK);
        DATEPICKER=(ImageButton) findViewById(R.id.DOJ_VIEW_PASS_CHECK);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.OTP_LAYOUT).setVisibility(View.INVISIBLE);

        rxConnect=new RxConnect(this);
        rxConnect.setCachingEnabled(false);
        m2Dialog=new ProgressDialog(this);



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



                APPLICATION_STATUS_SHOW.setText("");


                final String PASS_NO=pass_no.getText().toString().trim();
                final String ID_NO=Id_no.getText().toString().trim();
                if(PASS_NO.isEmpty()||ID_NO.isEmpty()||DateOfJourney==null)
                {
                    if(PASS_NO.isEmpty())
                    Toasty.warning(getApplicationContext(),"Enter DisplayPass details",Toast.LENGTH_SHORT).show();

                    else  if(ID_NO.isEmpty())
                        Toasty.warning(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_SHORT).show();

                    else
                        Toasty.warning(getApplicationContext(),"Enter Date of Journey",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    String REGISTERED_MOBILE_NUBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
                    rxConnect=new RxConnect(CheckPassDetails.this);

                    RxConnect rxConnect1=new RxConnect(CheckPassDetails.this);
                    rxConnect1.setCachingEnabled(false);
                    rxConnect1.setParam("old_doj",DateOfJourney);
                    rxConnect1.setParam("application_no",PASS_NO);
                    rxConnect1.setParam("user_mob",REGISTERED_MOBILE_NUBER);

                    rxConnect1.execute(Constants.ONLINE_GET_REUPLOAD_DETAILS_LINK, RxConnect.POST, new RxConnect.RxResultHelper() {
                        @Override
                        public void onResult(String result) {

                            Log.v("ResponseCPDetails",result);

                            /**
                             * {"response_status":"1",
                             *  "msg":"Sucess-fully",
                             *  "applicant_status":{"application_mobile":"8093679890",
                             *                      "status":"4",
                             *                      "paid_status":"3"
                             *                      "pending_status":"0",
                             *                      "remarked":null}}
                             */

                            try {


                                APPLICATION_STATUS_SHOW.setTextColor(Color.RED);

                                JSONObject jsonObject=new JSONObject(result);

                                String APPLICATION_FOUND_STATUS=JsonParser.JSONValue(jsonObject,"response_status");
                                if(APPLICATION_FOUND_STATUS.equals("3"))
                                {
                                    APPLICATION_STATUS_SHOW.setText("NO SUCH APPLICATION EXISTS CHECK APPLICATION NUMBER AND DATE OF JOURNEY");
                                }

                                Vehicles.APPLICATION_NUMBER=PASS_NO;
                                Log.v("Here","1");

                                JSONObject jsonObject1=jsonObject.getJSONObject("applicant_status");

                                String PAID_STATUS=jsonObject1.getString("paid_status");

                                if(PAID_STATUS.equals("3"))
                                {
                                    APPLICATION_STATUS_SHOW.setText("NOT PAID");
                                }
                                else if(PAID_STATUS.equals("2"))
                                {
                                    APPLICATION_STATUS_SHOW.setText("PAYMENT FAILED");

                                }
                                else {


                                    Log.v("Here", "2");
                                    APLICANT_MOBILE = JsonParser.JSONValue(jsonObject1, "application_mobile");


                                    Log.v("Here", "3");
                                    String APPLICATION_STATUS = JsonParser.JSONValue(jsonObject1, "pending_status");

                                    Log.v("Here", "4");
                                    String REMARK = JsonParser.JSONValue(jsonObject1, "remarked");

                                    if (APPLICATION_STATUS.equals("2"))///*&&(jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER))||jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER)*/)
                                    {


                                        APPLICATION_STATUS_SHOW.setText(REMARK);
                                        PROFILE_PIC_SCAN_PIC.setVisibility(View.VISIBLE);

                                    } else if (APPLICATION_STATUS.contains("3")) {

                                        WHICH_VEHICLE_STATS_STATUS = APPLICATION_STATUS;
                                        APPLICATION_STATUS_SHOW.setText(REMARK);
                                        VEHICLE.setVisibility(View.VISIBLE);

                                    } else if (APPLICATION_STATUS.equals("0")) {
                                        Log.v("Here", "5");
                                        String STATUS_APPLICATIONS_SHOW = JsonParser.JSONValue(jsonObject1, "status");
                                        Log.v("Here", STATUS_APPLICATIONS_SHOW);

                                        boolean One = STATUS_APPLICATIONS_SHOW.equals("1");
                                        boolean Two = STATUS_APPLICATIONS_SHOW.equals("2");
                                        boolean Three = STATUS_APPLICATIONS_SHOW.equals("3");
                                        boolean Four = STATUS_APPLICATIONS_SHOW.equals("4");
                                        boolean Five = STATUS_APPLICATIONS_SHOW.equals("5");
                                        if (One) {


                                            Log.v("CheckPassHere1", "Reached1");
                                            //DisplayPass ready
                                            APPLICATION_STATUS_SHOW.setText("Pass is Ready you can Check your pass through VIEW PASS field");

                                            Toasty.success(getApplicationContext(), "Your Pass is Ready", Toast.LENGTH_SHORT).show();

                                        } else if (Two) {
                                            //Personal Info verified


                                            Log.v("CheckPassHere1", "Reached2");

                                            APPLICATION_STATUS_SHOW.setText("Personal details have been verified proceed to uploading Vehicle details");

                                        } else if (Three || Five) {
                                            //Vehicle Info verified

                                            Log.v("CheckPassHere1", "Reached3");

                                            APPLICATION_STATUS_SHOW.setText("Vehicle Information is being Verified");

                                        }

                                        /**
                                         *  {"response_status":"1",
                                         *   "msg":"Sucess-fully",
                                         *   "applicant_status":{"application_mobile":"8093679890",
                                         *                       "status":"4",
                                         *                       "pending_status":"0",
                                         *                       "remarked":null}}

                                         */
                                        else if (Four) {
                                            //Personal Info is being processed


                                            Log.v("CheckPassHere1", "Reached4");
                                            Log.v("Here", "7");
                                            APPLICATION_STATUS_SHOW.setText("Personal Information is being Verified");
                                        }

                                    }
                                }
                            }catch (JSONException e)
                            {

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
