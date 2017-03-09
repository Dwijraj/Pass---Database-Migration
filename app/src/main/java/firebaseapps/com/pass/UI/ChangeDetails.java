package firebaseapps.com.pass.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.PayPalConfig;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.ViewPass;
import firebaseapps.com.pass.View_Pass;
import mohitbadwal.rxconnect.RxConnect;

/**
 * OTP System to be implemented
 * params to send
 * 1.Fields before getting OTP
 * 2.Old Date of Journey
 * 3.Registered Mobile
 * The i get Applicant mobile number
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
    private FirebaseAuth mAuth;
    private Button update;
    private String passno;
    private String state;
    private int mDay, mMonth ,mYear;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private Intent intent;
    private int N=1;
    private String REGISTERED_NUMBER;
    private String DateOfJourney;
    private ArrayList<String> UNAVAILABLE_DATES=new ArrayList<>();
    private RxConnect rxConnect1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    public void onDestroy() {
        Passdetails.THE_TEST=0;
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
        N=400;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);

        state="Payment_Pending";
        Passdetails.THE_TEST=1;

        rxConnect1=new RxConnect(ChangeDetails.this);
        rxConnect1.setCachingEnabled(false);
        mAuth=FirebaseAuth.getInstance();
        img_button=(ImageButton)findViewById(R.id.image_Button);
        img_button.setEnabled(false);
        Passno=(EditText)findViewById(R.id.change_pass_number);
        Idno=(EditText)findViewById(R.id.change_idno);
        DOJ=(TextView)findViewById(R.id.change_DOJ);
        update=(Button)findViewById(R.id.change_button);

        rxConnect=new RxConnect(ChangeDetails.this);
        rxConnect.setCachingEnabled(false);

        REGISTERED_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");

        rxConnect.setParam("user_mobile",REGISTERED_NUMBER);
        Log.v("Mobile",REGISTERED_NUMBER);
        rxConnect.execute(Constants.UNAVAILABLE_DOJ, RxConnect.POST, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {

                img_button.setEnabled(true);
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


        intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

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
                mDatePicker.getDatePicker().setMaxDate(m_three_months.getTimeInMillis());
                // mDatePicker.setTitle("Select date");
                mDatePicker.show();


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubmitChanges();

                //PAYMENT



            }
        });



    }
    public void SubmitChanges()
    {

        Log.v("Clicked","clidl");
        passno=Passno.getText().toString().trim();
        String PassId=Idno.getText().toString().trim();
        if(!(passno.isEmpty()||PassId.isEmpty()||DateOfJourney.isEmpty()))
        {


            rxConnect1.setParam("new_doj",DateOfJourney);
            rxConnect1.setParam("token_id",passno);
            rxConnect1.setParam("pass_id",PassId);
            rxConnect1.setParam("applicant_mob",REGISTERED_NUMBER);

            rxConnect1.execute(Constants.UPDATE_DETAILS_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                @Override
                public void onResult(String result) {

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

}
