package firebaseapps.com.pass.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Constants.OPTION_SELECTED;
import firebaseapps.com.pass.R;
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
        setContentView(R.layout.activity_view__pass);

        pass_no=(EditText)findViewById(R.id.view_pass_passno_);
        Id_no=(EditText) findViewById(R.id.view_pass_Id_no);
        view=(Button)findViewById(R.id.view_pass);
        DOJ_DISPLAY=(TextView) findViewById(R.id.DOJ_TEXT_VIEW);
        DATEPICKER=(ImageButton) findViewById(R.id.DOJ_VIEW_PASS);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        rxConnect=new RxConnect(View_Pass.this);
        rxConnect.setCachingEnabled(false);
        if(HomeScreen.OPTION_SELECTED.equals(OPTION_SELECTED.OPTION_APPLICATION_PREVIEW))
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
                if(PASS_NO.isEmpty()||ID_NO.isEmpty()||DateOfJourney==null)
                {
                    if(PASS_NO.isEmpty())
                    {
                        Toasty.info(getApplicationContext(),"Enter Application number",Toast.LENGTH_SHORT).show();

                    }
                    else if(ID_NO.isEmpty())
                    {
                        Toasty.info(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toasty.info(getApplicationContext(),"Enter Date of Journey",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {

                    if(HomeScreen.OPTION_SELECTED.equals(OPTION_SELECTED.OPTION_PASS_VIEW))
                    {
                        Log.v("ViewingPass","3");

                        String REGISTERED_MOBILE_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
                        rxConnect.setParam("token_id",PASS_NO);
                        rxConnect.setParam("app_mobile",ID_NO);
                        rxConnect.setParam("user_mobile",REGISTERED_MOBILE_NUMBER);
                        rxConnect.setParam("user","customer");
                        rxConnect.setParam("date_journey",DateOfJourney);

                        rxConnect.execute(Constants.ONLINE_GET_PASS_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                            @Override
                            public void onResult(String result) {

                                Log.v("OnPassGet",result);

                                try {
                                    JSONObject jsonObject=new JSONObject(result);

                                    if(jsonObject.getString("response_status").equals("1"))///*&&(jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER))||jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER)*/)
                                    {

                                        Intent PASS_VIEW=new Intent(View_Pass.this,DisplayPass.class);
                                        PASS_VIEW.putExtra("Pass",PASS_NO);
                                        PASS_VIEW.putExtra("PassDetails",result);
                                        finish();
                                        startActivity(PASS_VIEW);

                                    }
                                    else if(jsonObject.getString("response_status").equals("3"))
                                    {
                                        Toasty.error(getApplicationContext(),"No Such application exists",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toasty.warning(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                    }

                                }
                                catch (Exception e)
                                {

                                }



                            }

                            @Override
                            public void onNoResult() {

                                Log.v("Error","No Result");

                            }

                            @Override
                            public void onError(Throwable throwable) {


                                Log.v("Error",throwable.getLocalizedMessage());
                            }
                        });




                    }
                    else
                    {
                        String REGISTERED_MOBILE_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
                        rxConnect.setParam("token_id",PASS_NO);
                        rxConnect.setParam("app_mobile",ID_NO);
                        rxConnect.setParam("user_mobile",REGISTERED_MOBILE_NUMBER);
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

                                        if(HomeScreen.OPTION_SELECTED.equals(OPTION_SELECTED.OPTION_APPLICATION_PREVIEW))
                                        {
                                            Intent PASS_PREVIEW=new Intent(View_Pass.this,ApplicationPreview.class);
                                            PASS_PREVIEW.putExtra("PassNumber",PASS_NO);
                                            PASS_PREVIEW.putExtra("editable","0");
                                            finish();
                                            startActivity(PASS_PREVIEW);
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

            }
        });

    }
}
