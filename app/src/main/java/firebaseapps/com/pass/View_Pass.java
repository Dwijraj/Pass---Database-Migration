package firebaseapps.com.pass;

import android.app.DatePickerDialog;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Constants.OPTION_SELECTED;
import firebaseapps.com.pass.UI.ApplyPass;
import firebaseapps.com.pass.UI.PassPreview;
import firebaseapps.com.pass.UI.Passdetails;
import firebaseapps.com.pass.UI.Vehicles;
import mohitbadwal.rxconnect.RxConnect;

public class View_Pass extends AppCompatActivity {


    private RxConnect rxConnect;
    private EditText pass_no;
    private Button view;
    private EditText Id_no;
    private TextView DOJ_DISPLAY;
    private ImageButton DATEPICKER;
    public static String PASS_DETAILS;
    private DatePicker datePicker;
    private int mYear,mMonth,mDay;
    private String DateOfJourney=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__pass);

        pass_no=(EditText)findViewById(R.id.view_pass_passno_);
        Id_no=(EditText) findViewById(R.id.view_pass_Id_no);
        view=(Button)findViewById(R.id.view_pass);
        DOJ_DISPLAY=(TextView) findViewById(R.id.DOJ_TEXT_VIEW);
        DATEPICKER=(ImageButton) findViewById(R.id.DOJ_VIEW_PASS);

        rxConnect=new RxConnect(View_Pass.this);
        rxConnect.setCachingEnabled(false);
        if(ApplyPass.OPTION_SELECTED.equals(OPTION_SELECTED.OPTION_APPLICATION_PREVIEW))
        {
            pass_no.setHint("Enter Application Number");
        }


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

                DatePickerDialog mDatePicker = new DatePickerDialog(View_Pass.this, new DatePickerDialog.OnDateSetListener() {
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


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                final String PASS_NO=pass_no.getText().toString().trim();
                final String ID_NO=Id_no.getText().toString().trim();
                if(PASS_NO.isEmpty()||ID_NO.isEmpty()||DateOfJourney.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter pass number",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    String REGISTERED_MOBILE_NUBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
                    rxConnect.setParam("token_id",PASS_NO);
                    rxConnect.setParam("app_mobile",ID_NO);
                    rxConnect.setParam("user_mobile",REGISTERED_MOBILE_NUBER);
                    rxConnect.setParam("user","customer");
                    rxConnect.setParam("date_journey",DateOfJourney);

                    rxConnect.execute(Constants.ONLINE_PASS_RETREIVE_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                        @Override
                        public void onResult(String result) {

                            try {
                                Log.v("ResponseViewPass",result);

                                JSONObject jsonObject=new JSONObject(result);

                                 if(jsonObject.getString("response_status").equals("1"))///*&&(jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER))||jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER)*/)
                                {
                                    PASS_DETAILS=result;

                                    if(ApplyPass.OPTION_SELECTED.equals(OPTION_SELECTED.OPTION_APPLICATION_PREVIEW))
                                    {
                                        Intent PASS_PREVIEW=new Intent(View_Pass.this,PassPreview.class);
                                        PASS_PREVIEW.putExtra("PassNumber",PASS_NO);
                                        PASS_PREVIEW.putExtra("editable","0");
                                        finish();
                                        startActivity(PASS_PREVIEW);
                                    }
                                    else if(ApplyPass.OPTION_SELECTED.equals(OPTION_SELECTED.OPTION_PASS_VIEW))
                                    {
                                        Intent PASS_VIEW=new Intent(View_Pass.this,ViewPass.class);
                                        PASS_VIEW.putExtra("PassNumber",PASS_NO);
                                        PASS_VIEW.putExtra("editable","0");
                                        finish();
                                        startActivity(PASS_VIEW);

                                    }
                                    else {
                                        Intent VEHICLE=new Intent(View_Pass.this, Vehicles.class);
                                        VEHICLE.putExtra("PassNumber",PASS_NO);
                                        VEHICLE.putExtra("editable","0");
                                        finish();
                                        startActivity(VEHICLE);


                                    }

                                }
                                else if(jsonObject.getString("response_status").equals("3"))
                                {
                                    Toasty.error(getApplicationContext(),"No Such application exists",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.v("RESPONSE_STATUS",jsonObject.getString("response_status")+"  ...");
                                    Toasty.info(getApplicationContext(),"You are not authorized to view this pass",Toast.LENGTH_SHORT).show();

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

    }
}
