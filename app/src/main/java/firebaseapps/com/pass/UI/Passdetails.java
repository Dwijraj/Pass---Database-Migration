package firebaseapps.com.pass.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.Uri;

import android.provider.MediaStore;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Json;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeListener;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;


import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Adapter.CustomAdapter;
import firebaseapps.com.pass.Constants.ApplicationParams;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Constants.PaymentURL;
import firebaseapps.com.pass.Payment.ClientToken;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.NetworkUtil;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.GetMimeType;
import firebaseapps.com.pass.Utils.QR_Codegenerator;
import firebaseapps.com.pass.Utils.VolleyMultipartRequest;
import mohitbadwal.rxconnect.RxConnect;



/** MINOR BUG FIXES IMAGE VIEW IN SCAN_ID CANT PUT BIG IMAGES **/



public class Passdetails extends AppCompatActivity {


    private String MIME;
    private Uri imageuri =null;
    private Uri imageuriProfile=null;
    private Bitmap bitmap;
    private byte[] b;
    private byte[] PROFILE_PIC_BYTE_ARRAY;
    private ImageView scan_id;
    private ProgressDialog mDialog;
    private Uri scaniduri;
    private Uri profilephoto;
    private final int PROFILE_PHOTO=1321;       //To recognise in onActivityresult
    private final int SCAN_ID=12241;            //To recognise in onActivityResult
    private EditText Name;
    private LinearLayout ERROR_NAME;
    private LinearLayout ERROR_MOBILE;
    private LinearLayout ERROR_DATE;
    private EditText Address;
    private EditText Mobile;
    private TextView Dateofbirth;
    private TextView Dateofjourney;
    private TextView PRICE_FIELD;
    private String   PRICE_OF_PASS;
    private TextView Transaction_Id;
    private EditText ID_No;
    private EditText Purpose;
    private TextView Scan_id;
    private Dialog dialog;
    private Button   Payment;
    private ImageView Profile;
    private ImageButton DOBDate;
    private String Mobiles;
    private ImageButton DOJDate;
    private TextView Application_status;
    private RxConnect rxConnect; //To retrieve Prices and Places
    private String paymentAmount;
    private String state;
    private String ID_Source;
    private Spinner spinner;
    private String id;
    private String PLACE=null;
    private String TOKEN_PASS;
    private String PURPOSE_OF_VISIT;
    private Spinner REASON_OF_VISIT;
    private Spinner PLACES_SPINNER;
    public static final int PAYMENT_REQUEST_CODE = 123;
    public static int THE_TEST=0;
    private static  final ArrayList<String>paths =new ArrayList<>();// {"","Passport", "Driving License", "Adhar Card","PAN"};
    private static   ArrayList<String> PLACES;//={"","1","2","3"};
    private static final  ArrayList<String> REASONS=new ArrayList<>();//={"","Roam","Educational","Other"};
    private Calendar calendar;
    private int mDay, mMonth ,mYear;
    private HashMap<String,String> PriceNPlace;
    private RxConnect rxConnect1,rxConnect2; //rxConnect1 is for retreiving unavailable date rxConnect2 for Payment Confirmation
    private ArrayList<String> UNAVAILABLE_DATES=new ArrayList<>();
    String REGISTERED_NUMBER;


    //PAYMENT Configuration Object
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
        setContentView(R.layout.activity_passdetails);
        THE_TEST=1;


      //  getPayment();



/*        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this,
                    "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIxZjk4Yzk4NDUzNTE4OTNiYjBhMjdkNjlmODVmNWEzMjViNTcxYzExODYxMWUxNzdmM2E4MDMzOWJlM2NjNWMxfGNyZWF0ZWRfYXQ9MjAxNy0wNC0wNVQxMDoyODowMS4wMjc4NzQ0MzErMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        } */

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);




        paths.add("");
        paths.add("Passport");
        paths.add("Adhar Card");
        paths.add("Driving License");



        REASONS.add("");
        REASONS.add("Roam");
        REASONS.add("Tourism");
        REASONS.add("Educational");



      /*  DropInRequest dropInRequest = new DropInRequest()
                .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIxNTUwYWU2YjA2ZGE0NTIyNDc4NzI0ZTc2ZGZlNzgwOWI1ZGEzZDkwNjNjYjViODUxODBhZTA1NTgzZGM1NzM0fGNyZWF0ZWRfYXQ9MjAxNy0wNS0xMFQwMDozMTowNy4yMTY1ODE3MjErMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        startActivityForResult(dropInRequest.getIntent(this), PAYMENT_REQUEST_CODE);*/


        rxConnect=new RxConnect(Passdetails.this);
        rxConnect.setCachingEnabled(false);

        rxConnect2=new RxConnect(Passdetails.this);
        rxConnect2.setCachingEnabled(false);

        rxConnect1=new RxConnect(Passdetails.this);
        rxConnect1.setCachingEnabled(false);

        REGISTERED_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");

        dialog=new Dialog(Passdetails.this);

        PRICE_FIELD=(TextView) findViewById(R.id.PRICE_OF_PASS);
        PLACES_SPINNER=(Spinner) findViewById(R.id.spinnerPlaces);
        REASON_OF_VISIT=(Spinner) findViewById(R.id.spinnerPurpose);
        ERROR_DATE=(LinearLayout)findViewById(R.id.DATE_ERROR);
        ERROR_MOBILE=(LinearLayout)findViewById(R.id.MOBILE_ERROR);
        ERROR_NAME=(LinearLayout)findViewById(R.id.NAME_ERROR);
        rxConnect=new RxConnect(Passdetails.this);
        rxConnect.setCachingEnabled(false);
        ID_Source="Tap to select ID proof source";
        DOBDate=(ImageButton)findViewById(R.id.DOBDate);
        DOJDate=(ImageButton)findViewById(R.id.DOJDate);
        calendar = Calendar.getInstance();
        scan_id=(ImageView)findViewById(R.id.scan_pic) ;
        mDialog=new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        Name=(EditText)findViewById(R.id.name);
        Address=(EditText)findViewById(R.id.address);
        Mobile=(EditText)findViewById(R.id.mobile);
        Transaction_Id=(TextView)findViewById(R.id.transactionId);
        Transaction_Id.setText("SMS with application details sent to the applicant mobile number");
        Transaction_Id.setVisibility(View.INVISIBLE);
        ID_No=(EditText)findViewById(R.id.id_no);
        Dateofbirth=(TextView)findViewById(R.id.DOB);
        Dateofjourney=(TextView)findViewById(R.id.DOJ);
        Purpose=(EditText)findViewById(R.id.reason);
        Scan_id=(TextView)findViewById(R.id.scan);
        Profile=(ImageView) findViewById(R.id.profilephoto);
        Application_status=(TextView)findViewById(R.id.application_status);
        Payment=(Button)findViewById(R.id.payment);
        spinner = (Spinner)findViewById(R.id.spinner);

        Payment.setEnabled(false);


        rxConnect.setParam("user_mobile",REGISTERED_NUMBER);
        Log.v("Number",REGISTERED_NUMBER);
        rxConnect.execute(Constants.ONLINE_PRICING_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {


                Log.v("Response","RESULT"+result);

                GetPricesAndPlaces(result);




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
        rxConnect1.setParam("user_mobile",REGISTERED_NUMBER);

        rxConnect1.execute(Constants.ONLINE_UNAVAILABLE_DOJ, RxConnect.POST, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {


                Log.v("unavailable",result);
                try {


                    JSONObject jsonObject=new JSONObject(result);

                          /*  {       "response_status":"1",
                                    "msg":"sucess",
                                    "dates_info":[{"date_block":"2017-03-17"},{"date_block":"2017-03-30"},{"date_block":"2017-04-20"}]} */

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
                          Payment.setEnabled(true);


                    }
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









        REASON_OF_VISIT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PURPOSE_OF_VISIT=REASONS.get(position);

                Log.v("Purpose",PURPOSE_OF_VISIT);

                if(PURPOSE_OF_VISIT.equals("Other"))
                {
                    Purpose.setVisibility(View.VISIBLE);

                    Purpose.setHint("Enter your reason");
                }
                else {
                    Purpose.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    ID_Source=null;
                }
                else
                {
                    ID_Source=paths.get(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PLACES_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positiond, long id) {

                if(positiond!=0)
                {

                    PLACE=PLACES.get(positiond);

                    PRICE_FIELD.setText(PriceNPlace.get(PLACES.get(positiond)));
                    PRICE_OF_PASS=PriceNPlace.get(PLACES.get(positiond));



                }
                else
                {
                    PLACE=null;
                    PRICE_FIELD.setText("N/A");
                    PRICE_OF_PASS=null;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });





       Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


                String Number=extractNumber(s.toString());

                if(!Number.isEmpty())
                {
                    if(ERROR_NAME.getVisibility()== View.INVISIBLE)
                    {
                        ERROR_NAME.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    if(ERROR_NAME.getVisibility()== View.VISIBLE)
                    {
                        ERROR_NAME.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });

        Mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(TextUtils.isDigitsOnly(s)&& s.length()==0)
                {
                    if(ERROR_MOBILE.getVisibility()== View.VISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.INVISIBLE);
                    }
                }
                else if(s.length()==0)
                {
                    if(ERROR_MOBILE.getVisibility()== View.VISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.INVISIBLE);
                    }

                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0)
                {
                    if(ERROR_MOBILE.getVisibility()== View.VISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==0)
                {
                    if(ERROR_MOBILE.getVisibility()== View.VISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.INVISIBLE);
                    }
                }
                else if(s.length()!=10)
                {
                    if(ERROR_MOBILE.getVisibility()== View.INVISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.VISIBLE);
                    }

                }
                else if(TextUtils.isEmpty(s))
                {
                    if(ERROR_MOBILE.getVisibility()== View.VISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    if(ERROR_MOBILE.getVisibility()== View.VISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
        DOJDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                final Calendar mcurrentDate = Calendar.getInstance();

                final Calendar  m_three_months = Calendar.getInstance();

                m_three_months.add(Calendar.MONTH,2);
                mcurrentDate.add(Calendar.DAY_OF_MONTH,5);
                 mYear  = mcurrentDate.get(Calendar.YEAR);
                  mMonth = mcurrentDate.get(Calendar.MONTH);
                 mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Passdetails.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                      final  Calendar myCalendar = Calendar.getInstance();
                        //Calendar myCalenderCopy;
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                       // myCalenderCopy=myCalendar;

                        String myFormat = "dd-MM-yyyy"; //Change as you need
                        final    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                        if(!UNAVAILABLE_DATES.contains(sdf.format(myCalendar.getTime())))
                        {

                            Dateofjourney.setText(sdf.format(myCalendar.getTime()));

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"The selected date is not available",Toast.LENGTH_SHORT).show();
                        }

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());
                mDatePicker.getDatePicker().setMaxDate(m_three_months.getTimeInMillis());
               // mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }


        });
        DOBDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear  = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Passdetails.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        String myFormat = "dd-MM-yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        Dateofbirth.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });






        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PROFILE_PHOTO);

            }
        });

        scan_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SCAN_ID);


            }
        });
        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.v("Clicked","clicked");
                try {

                    String Names = Name.getText().toString().trim();
                    String Addresses = Address.getText().toString().trim();
                    Mobiles = Mobile.getText().toString().trim();
                    String ID_NO = ID_No.getText().toString().trim();
                    String Purposes;
                    if(PURPOSE_OF_VISIT.equals("Other"))
                    {
                        Purposes = Purpose.getText().toString().trim();
                    }
                    else
                    {
                        Purposes = PURPOSE_OF_VISIT;
                    }

                    final String DateOfBirth = Dateofbirth.getText().toString().trim();
                    final String DateOfJourney = Dateofjourney.getText().toString().trim();

                    Log.v("Purpose",Purposes+"dcd");



                    {
                        if(  !( ID_Source.contains("Tap")||ID_Source==null  || Purposes.contains("Tap") || TextUtils.isEmpty(Purposes) ||
                                TextUtils.isEmpty(PLACE) || TextUtils.isEmpty(PLACE) ||  TextUtils.isEmpty(Names) ||
                                TextUtils.isEmpty(Addresses) || TextUtils.isEmpty(DateOfJourney) || TextUtils.isEmpty(DateOfBirth) ||
                                TextUtils.isEmpty(Mobiles) || TextUtils.isEmpty(ID_NO) || TextUtils.isEmpty(Purposes))
                                &&ERROR_NAME.getVisibility()==View.INVISIBLE&&ERROR_MOBILE.getVisibility()==View.INVISIBLE&&
                                ERROR_DATE.getVisibility()==View.INVISIBLE)
                        {
                            Log.v("Working","Here123");

                             SubmitApplication();
                          //   getPayment();
                        }
                        else
                        {
                            if(ERROR_NAME.getVisibility()==View.VISIBLE||ERROR_MOBILE.getVisibility()==View.VISIBLE||ERROR_DATE.getVisibility()==View.VISIBLE)
                            {
                                if(ERROR_NAME.getVisibility()==View.VISIBLE)
                                {

                                    Toast.makeText(getApplicationContext(),"Please enter valid Name",Toast.LENGTH_SHORT).show();
                                }
                                else  if(ERROR_MOBILE.getVisibility()==View.VISIBLE)
                                {

                                    Toast.makeText(getApplicationContext(),"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
                                }
                                else  if(ERROR_DATE.getVisibility()==View.VISIBLE)
                                {

                                    Toast.makeText(getApplicationContext(),"Please enter valid Date",Toast.LENGTH_SHORT).show();
                                }
                                else if(PLACE==null)
                                {

                                    Toast.makeText(getApplicationContext(),"Please select destination",Toast.LENGTH_SHORT).show();


                                }



                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please fill the empty fields and upload applicantcant photo and Scan_id",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                }catch (Exception e)
                {
                    Log.v("ErrorFromNotNetConnect",e.getLocalizedMessage()+"..");
                }





            }
        });
    }
    public void GetPricesAndPlaces(String result)
    {
        try {



            Log.v("Error","Here0");
            JSONObject jsonObject=new JSONObject(result);
            Log.v("Error","Here1");
            final JSONArray jsonArray=JsonParser.GetJsonArray(jsonObject,"place_info");
            Log.v("Error","Here2");
            PriceNPlace=JsonParser.PricNPlace(jsonArray);

            Log.v("length",jsonArray.length()+"");
            PLACES=new ArrayList<>();
            PLACES.add("");

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                PLACES.add(i+1,JsonParser.JSONValue(jsonObject1,"place_name"));
            }


            Log.v("Error","Here3");


             CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),paths);
             spinner.setAdapter(customAdapter);


             CustomAdapter customAdapter1=new CustomAdapter(getApplicationContext(),PLACES);
             PLACES_SPINNER.setAdapter(customAdapter1);

             CustomAdapter customAdapter2=new CustomAdapter(getApplicationContext(),REASONS);
             REASON_OF_VISIT.setAdapter(customAdapter2);


           // PL=new ArrayList<String>(Place_list);

        }
        catch (JSONException e)
        {

        }
    }
    public void onDestroy() {
        THE_TEST=0;
        paths.clear();
        REASONS.clear();
        PLACES.clear();

        super.onDestroy();
    }


    private void getPayment(String Token) {
        //Getting the amount from editText

        Log.v("here123",Token);

        String TOKEN_GENERATED= Token;

/*        */

        DropInRequest dropInRequest = new DropInRequest()
               // .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIxNTUwYWU2YjA2ZGE0NTIyNDc4NzI0ZTc2ZGZlNzgwOWI1ZGEzZDkwNjNjYjViODUxODBhZTA1NTgzZGM1NzM0fGNyZWF0ZWRfYXQ9MjAxNy0wNS0xMFQwMDozMTowNy4yMTY1ODE3MjErMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
                .clientToken(Token);
        startActivityForResult(dropInRequest.getIntent(this), PAYMENT_REQUEST_CODE);


    }



    private void SubmitApplication() {


        final String Names=Name.getText().toString().trim();
        final String Addresses= Address.getText().toString().trim();
        Mobiles= Mobile.getText().toString().trim();
        final String ID_NO= ID_No.getText().toString().trim();
        final String Purposes;
        if(PURPOSE_OF_VISIT.equals("Other"))
        {
            Purposes = Purpose.getText().toString().trim();
        }
        else
        {
            Purposes = PURPOSE_OF_VISIT;
        }
        final String DateOfBirth=Dateofbirth.getText().toString().trim();
        final String DateOfJourney=Dateofjourney.getText().toString().trim();
        final String Registered_Mobile=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Submitting...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(Purposes!=null && ID_Source!=null )
        {
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.ONLINE_IMAGE_SEND_LINK, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {

                    Log.v("Executionhere","onResp");


                    progressDialog.dismiss();
                    String RESPONSE=new String(response.data);

                    Log.v("JSONRESPONSE",RESPONSE+response.statusCode);
                    try {




                        String jsonString = new String(response.data);


                        JSONObject jsonObject= new JSONObject(jsonString);

                        Log.v("JsonObject1",jsonObject.getString("response_status"));
                        Log.v("JsonObject2",jsonObject.getString("msg"));

                        TOKEN_PASS=JsonParser.JSONValue(jsonObject,"token_no");
                        Application_status.setText(JsonParser.JSONValue(jsonObject,"token_no"));
                        Transaction_Id.setVisibility(View.VISIBLE);

                        Payment.setEnabled(false);



                        RxConnect rxConnect3=new RxConnect(Passdetails.this);
                        rxConnect3.setParam(Constants.SMS_PARAM_KEY_USER,Constants.SMS_PARAM_VALUE_USER);
                        rxConnect3.setParam(Constants.SMS_PARAM_KEY_KEY,Constants.SMS_PARAM_VALUE_KEY);
                        rxConnect3.setParam(Constants.SMS_PARAM_KEY_MOBILE,"91"+Mobiles);
                        rxConnect3.setParam(Constants.SMS_PARAM_KEY_MESSAGE,"APPLICATION RECEIVED"+"\n"+"Application Number: "+TOKEN_PASS + "\n"
                                +"Applicant Name :"+Names+"\n"+"Date of Journey :"+DateOfJourney+"\n");
                        rxConnect3.setParam(Constants.SMS_PARAM_KEY_SENDERID,"INFOSM");
                        rxConnect3.setParam(Constants.SMS_PARAM_KEY_ACCUSAGE,"2");
                        rxConnect3.execute(Constants.SMS_URL,RxConnect.GET, new RxConnect.RxResultHelper() {
                            @Override
                            public void onResult(String result) {
                                //do something on result

                                Log.v("ResultOnSending",result);

                                Toasty.info(getApplicationContext(),"Application Details sent to Applicant Mobile",Toast.LENGTH_LONG,true).show();

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







                        String PAYMENT_TOKEN=JsonParser.JSONValue(jsonObject,"token");


                         getPayment(PAYMENT_TOKEN);


                        Log.v("JSONRESPONSE",jsonString+jsonObject);
                    }catch (Exception ec)
                    {
                        Log.v("JSONRESPONSE",ec.getLocalizedMessage());
                    }


                    Log.v("Response", String.valueOf(response.statusCode)+ response.headers.get("status"));






                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    error.printStackTrace();

                    progressDialog.dismiss();
                    Log.v("JSONRESPONSE",error.getMessage()+"EEROR");

                    if(imageuri==null)
                    {
                        Toast.makeText(Passdetails.this,"Upload Scan ID",Toast.LENGTH_SHORT).show();
                    }
                    else if(imageuriProfile==null)
                    {
                        Toast.makeText(Passdetails.this,"Upload Profile Picture",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        String Network_status= NetworkUtil.getConnectivityStatusString(Passdetails.this);

                        if(Network_status.equals("Not connected to Internet"))
                        {
                            Toast.makeText(Passdetails.this,"Failed to connect..",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(Passdetails.this,"Error Uploading",Toast.LENGTH_SHORT).show();

                    }





                }
            }) {
                @Override
                protected Map<String, String> getParams() {



                    Map<String, String> params = new HashMap<>();
                    params.put(ApplicationParams.Name,Names);
                    params.put(ApplicationParams.Address,Addresses);
                    params.put(ApplicationParams.Mobile,Mobiles);
                    params.put(ApplicationParams.PlaceOfVisit,PLACE);
                    params.put(ApplicationParams.IDNumber,ID_NO);
                    params.put(ApplicationParams.IDSource,ID_Source);
                    params.put(ApplicationParams.RegisteredMobile,Registered_Mobile);
                    params.put(ApplicationParams.DateOfBirthd,DateOfBirth);
                    params.put(ApplicationParams.DateOfJourney,DateOfJourney);
                    params.put(ApplicationParams.PurposeOfVisit,Purposes);

                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView


                    params.put(ApplicationParams.PICTURE1, new DataPart(imageuri.getLastPathSegment()+"."+MIME, b, "image/jpeg"));
                    params.put(ApplicationParams.PICTURE,new DataPart(imageuriProfile.getLastPathSegment()+"."+"jpeg",PROFILE_PIC_BYTE_ARRAY,"image/jpeg"));

                    //   Log.d("file",imageuriProfile.getLastPathSegment()+" " + b.length/1024);
                    return params;
                }

            };
            //Creating a Request Queue
            RequestQueue requestQueue2 = Volley.newRequestQueue(this);

            //Adding request to the queue
            requestQueue2.add(multipartRequest);


        }
        else
        {
            Toasty.error(getApplicationContext(),"Fill all details",Toast.LENGTH_SHORT).show();
        }



    }
    int[] getResolution(int a,int b)
    {   int[] p=new int[2];
        if(a>950&&a<1900)
        {
            p[0]=a/2;
            p[1]=b/2;
        }
        else if (a>=1900&&a<3800)
        {
            p[0]=a/4;
            p[1]=b/4;
        }
        else if (a>=3800&&a<7600)
        {
            p[0]=a/8;
            p[1]=b/8;
        }
        else
        {
            p[0]=a;
            p[1]=b;
        }
        return p;
    }

    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }
    public  String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    File file;
    Bitmap scaled;

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PROFILE_PHOTO) {
            //When Applicant photo is selected

            profilephoto = data.getData();

            Bitmap photo = (Bitmap) data.getExtras().get("data");


            int p[] = getResolution(photo.getWidth(), photo.getHeight());
            photo = Bitmap.createScaledBitmap(photo, p[0], p[1], true);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            PROFILE_PIC_BYTE_ARRAY = stream.toByteArray();


            imageuriProfile = getImageUri(getApplicationContext(), photo);


            Glide.with(Passdetails.this)
                    .load(PROFILE_PIC_BYTE_ARRAY)
                    .into(Profile);


        } else if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                Log.v("Payment123",result.getPaymentMethodNonce().getNonce());


                String NONCE=result.getPaymentMethodNonce().getNonce();


                RxConnect rxConnect=new RxConnect(Passdetails.this);
                rxConnect.setParam("token_id",TOKEN_PASS);
                rxConnect.setParam("user_mobile",REGISTERED_NUMBER);
                rxConnect.setParam("transaction_pay_id",NONCE);
                rxConnect.setParam("status_pay",PRICE_FIELD.getText().toString().trim());
                rxConnect.execute(Constants.ONLINE_CONFIRMATION_LINK, RxConnect.POST, new RxConnect.RxResultHelper() {
                    @Override
                    public void onResult(String result) {

                        Log.v("PaymentResponse",result);

                        try {

                            JSONObject jsonObject=new JSONObject(result);

                            String TRANSACTION_STATUS=JsonParser.JSONValue(jsonObject,"msg");

                            if(TRANSACTION_STATUS.equals("Success"))
                            {
                                Transaction_Id.append("\n"+"Transaction successful");
                            }
                            else
                            {
                                Transaction_Id.append("\n"+"Transaction failed");

                            }

                        }
                        catch (JSONException e)
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


                try {
                  BraintreeFragment  mBraintreeFragment = BraintreeFragment.newInstance(this, result.getPaymentMethodNonce().getNonce());
                    // mBraintreeFragment is ready to use!


                } catch (InvalidArgumentException e) {
                    // There was an issue with your authorization string.
                }




            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Toasty.error(getApplicationContext(), "PaymentCanceled", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == SCAN_ID) {
            //when applicant scanned user id is selected

            imageuri = data.getData();
            MIME = GetMimeType.GetMimeType(Passdetails.this, imageuri);

            if (MIME.contains("/jpg") || MIME.contains("/JPG")) {
                MIME = "jpg";
            } else if (MIME.contains("/png") || MIME.contains("/PNG")) {
                MIME = "png";

            } else if (MIME.contains("/jpeg") || MIME.contains("/JPEG")) {
                MIME = "jpeg";
            } else if (MIME.contains("/gif") || MIME.contains("/GIF")) {
                MIME = "gif";
            }


            try {
                //Getting the Bitmap from Gallery
                file = new File(getPath(imageuri));

                b = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(b);
                for (int i = 0; i < b.length; i++) {
                    System.out.print((char) b[i]);
                }
                Log.d("file", "file size:" + b.length / 1024);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                Log.d("file", "file size: 2 ");
                int p[] = getResolution(bitmap.getWidth(), bitmap.getHeight());
                scaled = Bitmap.createScaledBitmap(bitmap, p[0], p[1], true);


                Log.d("file", "file size: 2 ");


                scan_id.setImageBitmap(scaled);


                Log.d("file", "file size: 3 ");

            } catch (IOException e) {

                Toast.makeText(getApplicationContext(), "FILE ERROR", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

                Log.v("GALLERY", e.getLocalizedMessage());
            }


        } else {

            Log.v("PaymentTag","1");


        }
    }


}





