package firebaseapps.com.pass;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Adapter.CustomAdapter;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.UI.Passdetails;
import firebaseapps.com.pass.UI.Vehicles;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.QR_Codegenerator;
import mohitbadwal.rxconnect.RxConnect;

import static firebaseapps.com.pass.UI.ChangeDetails.UNAVAILABLE_DATES;


public class ViewPass extends AppCompatActivity {

    private ImageView scan_id2;
    private TextView Name2;
    private TextView Address2;
    private Button Vehicle;
    private TextView Mobile2;
    private TextView Dateofbirth2;
    private TextView Dateofjourney2;
    private TextView Transaction_Id2;
    private TextView ID_No2;
    private TextView Purpose2;
    private TextView Scan_id2;
    private TextView DriverName;
    private TextView CarNumber;
    private ImageView Profile2;
    private TextView Application_status2;
    private DatabaseReference ApplicationRef2;
    public  Application app;
    private TextView ID_source;
    private static  String Changed_Values_flag="0";
    private TextView Gate;
    private TextView place_of_visit;
    private ImageButton DOJ_CHANGE;
    private Boolean Changed=false;
    public static String CHANGED_DOJ;
    public static String CHANGED_PLACE_OF_VISIT;
    private String Original_Date_Of_Journey;
    HashMap<String,String> PriceNPlace=new HashMap<>();
    ArrayList<String>  PLACES;
    int  mYear ;
    int  mMonth ;//= mcurrentDate.get(Calendar.MONTH);
    int  mDay ;//= mcurrentDate.get(Calendar.DAY_OF_MONTH);
    private String Pass_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_history);



        place_of_visit=(TextView) findViewById(R.id.place);
        Gate=(TextView) findViewById(R.id.GATE);
        CarNumber=(TextView)findViewById(R.id.Carnumbers);
        DriverName=(TextView)findViewById(R.id.DriverNAME);
        final  ImageView imageView=(ImageView) findViewById(R.id.BAR_CODE_SHOW);
        final TextView  Pass=(TextView)findViewById(R.id.PassNumber);
        Vehicle=(Button)findViewById(R.id.Display_Bar_Code);

        scan_id2=(ImageView)findViewById(R.id.SCAN_PIC) ;
        Name2=(TextView)findViewById(R.id.SCAN_NAME);
        Address2=(TextView)findViewById(R.id.SCAN_ADDRESS);
        Mobile2=(TextView)findViewById(R.id.SCAN_MOBILE);
        ID_No2=(TextView)findViewById(R.id.SCAN_ID);
        Dateofbirth2=(TextView)findViewById(R.id.SCAN_DOB);
        Dateofjourney2=(TextView)findViewById(R.id.SCAN_DOJ);
        Purpose2=(TextView)findViewById(R.id.SCAN_REASON);
        ID_source=(TextView)findViewById(R.id.ID_Source_field);
        Profile2=(ImageView)findViewById(R.id.SCAN_PROFILE);
        Application_status2=(TextView)findViewById(R.id.SCAN_STATUS);
        DOJ_CHANGE=(ImageButton)findViewById(R.id.DOJ_CHANGE_BUTTON);
        DOJ_CHANGE.setVisibility(View.INVISIBLE);
        DOJ_CHANGE.setEnabled(false);

        Intent i=getIntent();
        Pass_number=i.getExtras().getString("PassNumber");
        String Editable=i.getExtras().getString("editable");



        try {


            JSONObject Values=new JSONObject(View_Pass.PASS_DETAILS);

            Log.v("Here","1");

            JSONObject jsonObject=Values.getJSONObject("application_info");



            String Name= JsonParser.JSONValue(jsonObject,"applicant_name");

            String Address= JsonParser.JSONValue(jsonObject,"applicant_address");


            String PlaceOfVisit= JsonParser.JSONValue(jsonObject,"place_visting");


            String Mobile= JsonParser.JSONValue(jsonObject,"application_mobile");


            String IDNumber= JsonParser.JSONValue(jsonObject,"applicant_id_no");


            String IDSource= JsonParser.JSONValue(jsonObject,"applicant_id_source");


            String DateOfBirth= JsonParser.JSONValue(jsonObject,"dob");


            String Purpose= JsonParser.JSONValue(jsonObject,"purpose_visting");


            String DateOfJourney= JsonParser.JSONValue(jsonObject,"date_journey");



            String Res= JsonParser.JSONValue(jsonObject,"Photo");

            String Profile = Res.replaceAll("\"","");


            String ResId= JsonParser.JSONValue(jsonObject,"Scan_id_photo");

            String ScanId=ResId.replaceAll("\"","");


            String ApplicationStatus=JsonParser.JSONValue(jsonObject,"paid_status");



            Name2.setText(Name);
            Address2.setText(Address);
            Mobile2.setText(Mobile);
            ID_No2.setText(IDNumber);
            Dateofbirth2.setText(DateOfBirth);
            Dateofjourney2.setText(DateOfJourney);
            Original_Date_Of_Journey=Dateofjourney2.getText().toString();
            Purpose2.setText(Purpose);
            Application_status2.setText(ApplicationStatus.toUpperCase());
            ID_source.setText(IDSource);
            place_of_visit.setText(PlaceOfVisit);

            CHANGED_DOJ=DateOfJourney;
            CHANGED_PLACE_OF_VISIT=PlaceOfVisit;


            try {

                Bitmap  bitmap_QR_CODE = null;

                bitmap_QR_CODE = QR_Codegenerator.encodeAsBitmap(Pass_number,500);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap_QR_CODE.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byte[] BARCODE_BYTE_ARRAY = stream.toByteArray();

                Glide.with(getApplicationContext())
                        .load(Profile)
                        .into(Profile2);


                Glide.with(getApplicationContext())
                        .load(ScanId)
                        .into(scan_id2);

                imageView.setImageBitmap(bitmap_QR_CODE);


            }
            catch (Exception e)
            {
               Log.v("Here",e.getLocalizedMessage());// e.printStackTrace();
            }

            if (Editable.equals("1"))
            {

                Vehicle.setText("Confirm Changes");

                final String  REGISTERED_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");


                final RxConnect rxConnect=new RxConnect(this);
                rxConnect.setCachingEnabled(false);
                rxConnect.setParam("user_mobile",REGISTERED_NUMBER);
                rxConnect.execute(Constants.PRICING_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                    @Override
                    public void onResult(String result) {

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
                            Vehicle.setText("Submit Changes");
                            DOJ_CHANGE.setVisibility(View.VISIBLE);
                            DOJ_CHANGE.setEnabled(true);


                            place_of_visit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final Dialog dialog=new Dialog(ViewPass.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.editable);

                                    Spinner spinner=(Spinner) dialog.findViewById(R.id.Editable_spinner_id);

                                    CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),PLACES);
                                    spinner.setAdapter(customAdapter);

                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            String Previous=CHANGED_PLACE_OF_VISIT;
                                            //CHANGED_PLACE_OF_VISIT=PLACES.get(position);
                                            if(position!=0)
                                            {
                                                CHANGED_PLACE_OF_VISIT=PLACES.get(position);
                                                if(!Previous.equals(CHANGED_PLACE_OF_VISIT))
                                                {

                                                    place_of_visit.setText(CHANGED_PLACE_OF_VISIT);
                                                    Changed=true;
                                                    dialog.dismiss();

                                                }
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    }) ;
                                    dialog.show();
                                }
                            });


                        }
                        catch (JSONException e)
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
                DOJ_CHANGE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final Calendar mcurrentDate = Calendar.getInstance();

                        final Calendar  m_three_months = Calendar.getInstance();

                        m_three_months.add(Calendar.MONTH,2);
                        mcurrentDate.add(Calendar.DAY_OF_MONTH,5);
                        mYear  = mcurrentDate.get(Calendar.YEAR);
                        mMonth = mcurrentDate.get(Calendar.MONTH);
                        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker = new DatePickerDialog(ViewPass.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                final  Calendar myCalendar = Calendar.getInstance();
                                //Calendar myCalenderCopy;
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                                // myCalenderCopy=myCalendar;

                                String myFormat = "dd-MM-yyyy"; //Change as you need
                                final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                                if(Original_Date_Of_Journey.equals(sdf.format(myCalendar.getTime())))
                                {
                                   Toasty.warning(getApplicationContext(),"Your Trip was already scheduled on this day",Toast.LENGTH_SHORT).show();
                                    Changed_Values_flag="0";
                                }
                               else if(!UNAVAILABLE_DATES.contains(sdf.format(myCalendar.getTime())))
                                {
                                    Dateofjourney2.setText(sdf.format(myCalendar.getTime()));
                                    CHANGED_DOJ=Dateofjourney2.getText().toString();
                                    Changed_Values_flag="1";
                                    Changed=true;
                                }
                                else
                                {
                                    Changed_Values_flag="0";
                                    Toasty.error(getApplicationContext(),"The selected date is not available",Toast.LENGTH_SHORT).show();
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

                Vehicle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      /*  if(Changed_Values_flag.equals("1"))
                        {


                           AlertDialog.Builder builder=new AlertDialog.Builder(ViewPass.this);
                            builder.setTitle("Confirm changes");
                            builder.setMessage("Are you sure you want to make these changes?");
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog alertDialog=builder.create();
                            alertDialog.show();


                        }
                        else {
                            Toasty.info(ViewPass.this,"There were no changes in original application",Toast.LENGTH_SHORT).show();
                        } */
                      try {
                          RxConnect rxConnect1=new RxConnect(ViewPass.this);
                          rxConnect1.setParam("application_mobile",Mobile2.getText().toString());
                          rxConnect1.setParam("application_no",Pass_number);
                          rxConnect1.setParam("user_mobile",REGISTERED_NUMBER);
                          rxConnect1.setParam("transaction_pay_id","transactionID");
                          rxConnect1.setParam("status_pay","1");
                          rxConnect1.setParam("date_journey",CHANGED_DOJ);
                          rxConnect1.setParam("pay_time",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(System.currentTimeMillis()));
                          rxConnect1.setParam("place_changed",CHANGED_PLACE_OF_VISIT);
                          rxConnect1.execute(Constants.UPDATE_DETAILS_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                              @Override
                              public void onResult(String result) {

                                  Log.v("OnchangeDetailsError",result);

                                  //  dialog.dismiss();
                                     Toasty.success(ViewPass.this,"Changes Made Successfully",Toast.LENGTH_SHORT).show();
                                  //If successful display the message
                              }

                              @Override
                              public void onNoResult() {

                              }

                              @Override
                              public void onError(Throwable throwable) {

                              }
                          });
                      }catch (Exception e)
                      {
                          Log.v("OnCgangingReqerror",e.getLocalizedMessage());
                      }





                    }
                });


            }
            else if(Editable.equals("0"))
            {
                if(!ApplicationStatus.equals("1"))
                {
                    Vehicle.setEnabled(false);
                }

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
