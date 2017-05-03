package firebaseapps.com.pass.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.WriterException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.ViewPass;
import firebaseapps.com.pass.View_Pass;
import mohitbadwal.rxconnect.RxConnect;

/**
 * OTP System to be implemented
 * params to send
 * Fields before getting OTP
 * 1.Old Date of Journey
 * 2.Registered Mobile
 * The I get Applicant mobile number
 * 1.Applicant Mobile
 * 2.Token_ID
 * OTP generate and check
 * Pass Generate and show editables
 * 1.DOJ
 * 2.Place Visiting
 * Set Minimum change in date of journey
 * view the form
 * The allow change of fields
 * Date of Journey
 * Place of journey
 */



public class ChangeDetails extends AppCompatActivity {

    private RxConnect rxConnect;
    private EditText Passno;
    private EditText Idno;
    private TextView DOJ;
    private ImageButton img_button;
    private Button update;
    private String passno;
    private String state;
    private int mDay, mMonth ,mYear;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private Intent intent;
    private int N=1;
    String APLICANT_MOBILE;
    private String REGISTERED_NUMBER;
    private String DateOfJourney;
    public static ArrayList<String> UNAVAILABLE_DATES=new ArrayList<>();
    private RxConnect rxConnect1;
    private String   OTPstring;


    public void onDestroy() {
        Passdetails.THE_TEST=0;
         super.onDestroy();
        N=400;
    }
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
        setContentView(R.layout.activity_change_details);

        state="Payment_Pending";
        Passdetails.THE_TEST=1;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        rxConnect1=new RxConnect(ChangeDetails.this);
        rxConnect1.setCachingEnabled(false);
        img_button=(ImageButton)findViewById(R.id.image_Button);
        img_button.setEnabled(false);
        Passno=(EditText)findViewById(R.id.change_pass_number);
        Idno=(EditText)findViewById(R.id.change_idno);
        DOJ=(TextView)findViewById(R.id.change_DOJ);
        update=(Button)findViewById(R.id.change_button);
        img_button.setEnabled(false);
        update.setEnabled(false);

        Idno.setVisibility(View.INVISIBLE);
        Idno.setEnabled(false);


        rxConnect=new RxConnect(ChangeDetails.this);
        rxConnect.setCachingEnabled(false);

        REGISTERED_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");

        rxConnect.setParam("user_mobile",REGISTERED_NUMBER);
        Log.v("Mobile",REGISTERED_NUMBER);
        rxConnect.execute(Constants.ONLINE_UNAVAILABLE_DOJ, RxConnect.POST, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {


                Log.v("unavailable",result);
                try {


                    JSONObject jsonObject=new JSONObject(result);


                          String Response_status= JsonParser.JSONValue(jsonObject,"response_status");
                          if(Response_status.equals("1"))
                          {
                              JSONArray jsonArray=JsonParser.GetJsonArray(jsonObject,"dates_info");

                              for(int i=0;i<jsonArray.length();i++)
                              {
                                  JSONObject Object= (JSONObject) jsonArray.get(i);
                                  UNAVAILABLE_DATES.add(JsonParser.JSONValue(Object,"date_block"));
                                  Log.v("DatesUnavailable",JsonParser.JSONValue(Object,"date_block"));

                              }
                              img_button.setEnabled(true);



                          }



                    //Add all Unavailable DOJ in the ArrayList

                 //   UNAVAILABLE_DATES.add("");



                }catch (JSONException e)
                {

                }


            }

            @Override
            public void onNoResult() {

                Toast.makeText(getApplicationContext(),"No Result",Toast.LENGTH_SHORT).show();
                Log.v("Response","RESULT NOPE");
            }

            @Override
            public void onError(Throwable throwable) {

                Toast.makeText(getApplicationContext(),throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                Log.v("Response","RESULT"+throwable.getMessage());
            }
        });



        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentDate = Calendar.getInstance();

                final Calendar  m_three_months = Calendar.getInstance();

                m_three_months.add(Calendar.MONTH,2);
                mcurrentDate.add(Calendar.DAY_OF_MONTH,5);
                mYear  = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ChangeDetails.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        final  Calendar myCalendar = Calendar.getInstance();
                        //Calendar myCalenderCopy;
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                        // myCalenderCopy=myCalendar;

                        String myFormat = "dd-MM-yyyy"; //Change as you need
                        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                        String SELECTED_DATE=sdf.format(myCalendar.getTime());



                        if(!UNAVAILABLE_DATES.contains(sdf.format(myCalendar.getTime())))
                        {
                            DOJ.setText(sdf.format(myCalendar.getTime()));
                            DateOfJourney=sdf.format(myCalendar.getTime());
                            update.setEnabled(true);

                        }
                        else
                        {
                            update.setEnabled(false);
                            Toasty.error(getApplicationContext(),"The selected date is not available",Toast.LENGTH_SHORT).show();
                        }

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                // mDatePicker.setTitle("Select date");
                mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());
                mDatePicker.getDatePicker().setMaxDate(m_three_months.getTimeInMillis());
                mDatePicker.show();


            }
        });

        if(ApplyPass.OPTION.equals("Change Details"))
        {

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SubmitChanges();
                  //  RequestCancel();

                    //PAYMENT



                }
            });
        }
        else if(ApplyPass.OPTION.equals("Update Vehicle Details"))
        {
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VehicleUpdate();
                }
            });
        }
        else
        {
            update.setText("REQUEST Cancel");
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // SubmitChanges();
                    SubmitChanges();

                    RequestCancel();

                }
            });

        }




    }
    public void VehicleUpdate()
    {
        passno=Passno.getText().toString().trim();

        if(!(passno.isEmpty() && DateOfJourney.isEmpty()))
        {


            update.setEnabled(false);
            rxConnect1.setParam("old_doj",DateOfJourney);
            rxConnect1.setParam("application_no",passno);
            rxConnect1.setParam("user_mob",REGISTERED_NUMBER);

            rxConnect1.execute(Constants.ONLINE_GET_APPLICANT_MOB_CHANGE_DETAIL, RxConnect.POST, new RxConnect.RxResultHelper() {
                @Override
                public void onResult(String result) {

                    Log.v("Response",result);
                    try {

                        Log.v("Response",result);

                        JSONObject jsonObject=new JSONObject(result);

                        Log.v("Response1","here4");
                        JSONObject jsonObject1=jsonObject.getJSONObject("applicant_mobile");

                        Log.v("Response1","here3");
                        APLICANT_MOBILE=JsonParser.JSONValue(jsonObject1,"application_mobile");

                        Log.v("Response1","here2");
                        String APPLICATION_STATUS=JsonParser.JSONValue(jsonObject1,"status_detail_vech");

                        Log.v("Response1","here1");
                        if(APPLICATION_STATUS.equals("2"))
                        {
                            Send_OTP();
                        }
                        else
                        {
                            Toasty.info(getApplicationContext(),"You are not authorized to enter your Vehicle Details Now",Toast.LENGTH_SHORT).show();
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
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  if(update.getText().toString().equals("Enter OTP"))


                    Log.v("FunctionChange","Vehicle");

                    // {
                    String ENTERED_OTP=Idno.getText().toString().trim();
                    if (ENTERED_OTP!=null && ENTERED_OTP.equals(OTPstring))
                    {
                            Intent VEHICLE_UPDATE=new Intent(ChangeDetails.this,Vehicles.class);
                            finish();
                            Vehicles.APPLICATION_NUMBER=passno;
                            startActivity(VEHICLE_UPDATE);
                    }
                    else {
                        Toasty.error(getApplicationContext(),"Wrong OTP",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else {
            Toasty.warning(getApplicationContext(),"Please fill the details",Toast.LENGTH_LONG).show();
        }

    }
    public void RequestCancel()
    {
        passno=Passno.getText().toString().trim();

        if(!(passno.isEmpty() && DateOfJourney.isEmpty()))
        {


            update.setEnabled(false);
            rxConnect1.setParam("old_doj",DateOfJourney);
            rxConnect1.setParam("application_no",passno);
            rxConnect1.setParam("user_mob",REGISTERED_NUMBER);

            rxConnect1.execute(Constants.ONLINE_GET_APPLICANT_MOB_CHANGE_DETAIL, RxConnect.POST, new RxConnect.RxResultHelper() {
                @Override
                public void onResult(String result) {

                    Log.v("ResponseRequestCancel",result);
                    try {

                        JSONObject jsonObject=new JSONObject(result);
                        JSONObject jsonObject1=jsonObject.getJSONObject("applicant_mobile");
                        APLICANT_MOBILE=JsonParser.JSONValue(jsonObject1,"application_mobile");
                       /* Idno.setVisibility(View.VISIBLE);
                        Idno.setHint("Enter the OTP you received on the mobile number used in application"); */

                        Send_OTP();
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
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  if(update.getText().toString().equals("Enter OTP"))


                    Log.v("FunctionChange","Refund");

                    // {
                    String ENTERED_OTP=Idno.getText().toString().trim();
                    if (ENTERED_OTP!=null && ENTERED_OTP.equals(OTPstring))
                    {




                        rxConnect.setParam("token_id",passno);
                        rxConnect.setParam("app_mobile",APLICANT_MOBILE);
                        rxConnect.setParam("user_mobile",REGISTERED_NUMBER);
                        rxConnect.setParam("date_journey",DateOfJourney);
                        rxConnect.execute(Constants.ONLINE_CANCEL_REQUEST, RxConnect.POST, new RxConnect.RxResultHelper() {
                            @Override
                            public void onResult(String result) {


                                try {
                                    JSONObject jsonObject1=new JSONObject(result);
                                    String resullt=JsonParser.JSONValue(jsonObject1,"status");

                                    if(resullt.equals("1"))
                                    {
                                        Toasty.success(getApplicationContext(),"Cancel request submitted",Toast.LENGTH_LONG).show();

                                    }
                                    else if(resullt.equals("2"))
                                    {
                                        Toasty.warning(getApplicationContext(),"Cancelation not possible",Toast.LENGTH_LONG).show();
                                    }
                                    else if(resullt.equals("3"))
                                    {
                                        Toasty.error(getApplicationContext(),"No such application exists",Toast.LENGTH_LONG).show();

                                    }
                                    else
                                    {
                                        Toasty.info(getApplicationContext(),"Your request is under review",Toast.LENGTH_LONG).show();
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
                        //   }
                    }
                    else {
                        Toasty.error(getApplicationContext(),"Wrong OTP",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else {
            Toasty.warning(getApplicationContext(),"Please fill the details",Toast.LENGTH_LONG).show();
        }

    }
    void Send_OTP() {

        Random rn = new Random();
        int n = 999 - 99;
        int i = rn.nextInt() % n;
        if(i<0)
        {
            i*=-1;
        }
        int  OTPint=  99 + i;
        OTPstring=String.valueOf(OTPint);
        update.setEnabled(false);

        rxConnect.setParam(Constants.SMS_PARAM_KEY_USER,Constants.SMS_PARAM_VALUE_USER);
        rxConnect.setParam(Constants.SMS_PARAM_KEY_KEY,Constants.SMS_PARAM_VALUE_KEY);
        rxConnect.setParam(Constants.SMS_PARAM_KEY_MOBILE,"91"+APLICANT_MOBILE);
        rxConnect.setParam(Constants.SMS_PARAM_KEY_MESSAGE,"Your OTP to is "+OTPstring);
        rxConnect.setParam(Constants.SMS_PARAM_KEY_SENDERID,"INFOSM");
        rxConnect.setParam(Constants.SMS_PARAM_KEY_ACCUSAGE,"2");
        rxConnect.execute(Constants.SMS_URL,RxConnect.GET, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {
                //do something on result
                Idno.setVisibility(View.VISIBLE);
                Idno.setEnabled(true);
                Idno.setHint("Enter the OTP you received on the mobile number used in application");
                Toast.makeText(getApplicationContext(),"OTP sent to mobile number of applicant "+OTPstring,Toast.LENGTH_LONG).show();
                update.setEnabled(true);


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
    }
    void GetPass()
    {
        String ENTERED_OTP=Idno.getText().toString().trim();
        if (ENTERED_OTP!=null && ENTERED_OTP.equals(OTPstring))
        {
            RxConnect rxConnect2=new RxConnect(ChangeDetails.this);
            rxConnect2.setParam("token_id",passno);
            rxConnect2.setParam("app_mobile",APLICANT_MOBILE);
            rxConnect2.setParam("user_mobile",REGISTERED_NUMBER);
            //    rxConnect2.setParam("user","customer");
            rxConnect2.setParam("date_journey",DateOfJourney);
            rxConnect2.execute(Constants.ONLINE_PASS_RETREIVE_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                @Override
                public void onResult(String result) {

                    Log.v("ResponseChangeDetails",result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);

                        if(JsonParser.JSONValue(jsonObject,"response_status").equals("1"))
                        {
                            String Response_status= JsonParser.JSONValue(jsonObject,"response_status");


                            Intent Change=new Intent(ChangeDetails.this, ViewPass.class);
                            Change.putExtra("PassNumber",passno);
                            Change.putExtra("editable","1");
                            View_Pass.PASS_DETAILS=result;
                            finish();
                            startActivity(Change);

                        }



                    }catch (JSONException e)
                    {

                    }


                }

                @Override
                public void onNoResult() {

                    Log.v("ResponseChange Details","NOresult");
                }

                @Override
                public void onError(Throwable throwable) {

                    Log.v("ResponseChange Details",throwable.getLocalizedMessage()+"ffdfd");
                }
            });
            //   }
        }
        else
        {
            Toasty.error(getApplicationContext(),"OTP invalid",Toast.LENGTH_SHORT).show();
        }
    }

    public void SubmitChanges()
    {

        passno=Passno.getText().toString().trim();
        //DateOfJourney=DOJ.getText().toString();
        DateOfJourney=DOJ.getText().toString();



        if(!(passno.isEmpty() || DateOfJourney.isEmpty()))
        {



            rxConnect1.setParam("old_doj",DateOfJourney);
            rxConnect1.setParam("application_no",passno);
            rxConnect1.setParam("user_mob",REGISTERED_NUMBER);

            rxConnect1.execute(Constants.ONLINE_GET_APPLICANT_MOB_CHANGE_DETAIL, RxConnect.POST, new RxConnect.RxResultHelper() {
                @Override
                public void onResult(String result) {

                    Log.v("ResultFromWeb",result);
                    try {
                        JSONObject jsonObject1=new JSONObject(result);

                        Log.v("ResultFromWeb",result);

                        if(JsonParser.JSONValue(jsonObject1,"response_status").equals("1"))
                        {
                            JSONObject jsonObject=new JSONObject(result);
                            APLICANT_MOBILE=JsonParser.JSONValue(jsonObject,"applicant_mobile");



                            JSONObject jsonObject2=new JSONObject(APLICANT_MOBILE);
                            APLICANT_MOBILE=JsonParser.JSONValue(jsonObject2,"application_mobile");

                            Log.v("Applicant",APLICANT_MOBILE);


                            Send_OTP();



                        }
                        else if(JsonParser.JSONValue(jsonObject1,"response_status").equals("2"))
                        {
                            Toasty.error(getApplicationContext(),"Registered Mobile Mismatch",Toast.LENGTH_SHORT).show();
                        }
                        else if(JsonParser.JSONValue(jsonObject1,"response_status").equals("3"))
                        {

                            Toasty.warning(getApplicationContext(),"Credentials don't match",Toast.LENGTH_SHORT).show();
                        }
                        else if(JsonParser.JSONValue(jsonObject1,"response_status").equals("0"))
                        {
                            Toasty.warning(getApplicationContext(),"Please Fill all details",Toast.LENGTH_SHORT).show();

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

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.v("FunctionChange","GetPass");
                    GetPass();

                }
            });




        }
        else {
            Toasty.warning(getApplicationContext(),"Please fill the details",Toast.LENGTH_LONG).show();
        }


    }

}
