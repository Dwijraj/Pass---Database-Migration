package firebaseapps.com.pass.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.Uri;

import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import firebaseapps.com.pass.Adapter.CustomAdapter;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Utils.PayPalConfig;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.GetMimeType;
import firebaseapps.com.pass.Utils.VolleyMultipartRequest;
import mohitbadwal.rxconnect.RxConnect;

//import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

//http://mobicomm.dove-sms.com/mobicomm//submitsms.jsp?user=SACHIN&key=d4c5c9993fXX&mobile=91(9437510178)&message=(test sms)&senderid=INFOSM&accusage=1

/** MINOR BUG FIXES IMAGE VIEW IN SCAN_ID CANT PUT BIG IMAGES **/



public class Passdetails extends AppCompatActivity {


    public static final int NOTIFICATION_ID = 10;
    private GoogleApiClient mGoogleAPiClient;
    private ImageView UploadedImage;
    private final int CAMERA_OPEN=9992;
    private Button Upload;
    private String ImagePath;
    private File destination;
    private Uri selectedImage;
    private String MIME;
    private String MIME_PROFILE;
    private RadioButton radioButtonIssue;
    private RadioButton radioButtonVisibility;
    private Double Longitude;
    private  ByteArrayOutputStream bytearrayoutputstream;
    private  byte[] BYTE;
    private Double Latitude;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=7;
    private Uri imageuri =null;
    private Uri imageuriProfile=null;
    private FirebaseAuth mFirebaseAuth;
    private RadioGroup radioGroupIssues;
    private RadioGroup radioGroupVisibility;
    private DatabaseReference Data;
    private Bitmap bitmap;
    private String Uploader;
    File f;
    private final int GALLERY_OPEN=9991;
   private final int GALLERY_OPEN_PROFILE=9989;
    private final Integer LOCATION = 0x1;
    private final Integer CAMERA = 0x5;
    private String PREF_NAME="Uploader";
    private SharedPreferences sharedpreferences ;// = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    private byte[] b;
    private byte[] PROFILE_PIC_BYTE_ARRAY;



    private FirebaseAuth mAuth;
    private ImageView scan_id;
    private ProgressDialog mDialog;
    private Uri scaniduri;
    private Uri profilephoto;
    private final int PROFILE_PHOTO=1321;       //To recognise in onActivityresult
    private final int SCAN_ID=12241;            //To recognise in onActivityResult
    private EditText Name;
    private byte[] byteArray;
    private Bitmap bitmap_QR_CODE;
    private LinearLayout ERROR_NAME;
    private LinearLayout ERROR_MOBILE;
    private LinearLayout ERROR_DATE;
    private int WRITE_EXST=1000;
    private EditText Address;
    private EditText Mobile;
    private TextView Dateofbirth;
    private TextView Dateofjourney;
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
    private byte[] BARCODE_BYTE_ARRAY;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private TextView Application_status;
    private DatabaseReference ApplicationRef;
    private DatabaseReference User_app_ref;
    private DatabaseReference UNAVAILABLE_DATES;
    private DatabaseReference PRICE_OF_PLACE;
    private StorageReference ApplicationStorageRef;
    private RxConnect rxConnect;
    //For PayPal Parts
    private String paymentAmount;
    private String state;
    private String ID_Source;
    private int flag;
    private TextView PLACE_OF_VISIT;
    private Spinner spinner;
    private String id;
    private String PLACE=null;
    private Spinner PLACES_SPINNER;
    public static final int PAYPAL_REQUEST_CODE = 123;
  //  private AwesomeValidation mAwesomeValidation;
    public static int THE_TEST=0;
    private String MOBILE_NUMBER;
    String URL="http://mobicomm.dove-sms.com/mobicomm/submitsms.jsp";//?user=SACHIN&key=d4c5c9993fXX&mobile=918093679890&message=(test sms)&senderid=INFOSM&accusage=1";
    private DatePicker datePicker;
    private static  final String[]paths = {"","Passport", "Driving License", "Adhar Card","PAN"};
    private static final  String[] PLACES={"","1","2","3"};
    private Calendar calendar;
    private int mDay, mMonth ,mYear;
    private DatabaseReference REFUND;
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            //Latter use when taking to production
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passdetails);
        THE_TEST=1;
        //mAwesomeValidation = new AwesomeValidation(BASIC);

        rxConnect=new RxConnect(Passdetails.this);
        rxConnect.setCachingEnabled(false);



        dialog=new Dialog(Passdetails.this);

        PRICE_OF_PLACE=FirebaseDatabase.getInstance().getReference();
        PRICE_OF_PLACE.keepSynced(true);
        PLACE_OF_VISIT=(TextView) findViewById(R.id.PLACE_OF_VISITS);
        PLACES_SPINNER=(Spinner) findViewById(R.id.spinnerPlaces);
        REFUND=FirebaseDatabase.getInstance().getReference().child("ToRefund");
        ERROR_DATE=(LinearLayout)findViewById(R.id.DATE_ERROR);
        ERROR_MOBILE=(LinearLayout)findViewById(R.id.MOBILE_ERROR);
        ERROR_NAME=(LinearLayout)findViewById(R.id.NAME_ERROR);
        rxConnect=new RxConnect(Passdetails.this);
        rxConnect.setCachingEnabled(false);
        ID_Source="Tap to select ID proof source";
        DOBDate=(ImageButton)findViewById(R.id.DOBDate);
        DOJDate=(ImageButton)findViewById(R.id.DOJDate);
        calendar = Calendar.getInstance();
        mAuth=FirebaseAuth.getInstance();
        scan_id=(ImageView)findViewById(R.id.scan_pic) ;
        mDialog=new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        Name=(EditText)findViewById(R.id.name);
        Address=(EditText)findViewById(R.id.address);
        Mobile=(EditText)findViewById(R.id.mobile);
        Transaction_Id=(TextView)findViewById(R.id.transactionId);
        Transaction_Id.setText("Please copy the transaction id after payment keep it safe to query about your application later");
        Transaction_Id.setEnabled(false);
        ID_No=(EditText)findViewById(R.id.id_no);
        Dateofbirth=(TextView)findViewById(R.id.DOB);
        Dateofjourney=(TextView)findViewById(R.id.DOJ);
        Purpose=(EditText)findViewById(R.id.reason);
        Scan_id=(TextView)findViewById(R.id.scan);
        Profile=(ImageView) findViewById(R.id.profilephoto);
        Application_status=(TextView)findViewById(R.id.application_status);
        Payment=(Button)findViewById(R.id.payment);
        UNAVAILABLE_DATES=FirebaseDatabase.getInstance().getReference().child("UnavailableDates");
        UNAVAILABLE_DATES.keepSynced(true);
        ApplicationRef= FirebaseDatabase.getInstance().getReference().child("Applications");//Points to the root directory of the Database
        User_app_ref=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        ApplicationStorageRef= FirebaseStorage.getInstance().getReference();        //Points to the root directory of the Storage
        spinner = (Spinner)findViewById(R.id.spinner);
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),paths);
        spinner.setAdapter(customAdapter);
        spinner.setPrompt("Select The source");

        CustomAdapter customAdapter1=new CustomAdapter(getApplicationContext(),PLACES);
        PLACES_SPINNER.setAdapter(customAdapter1);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ID_Source=paths[position];

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


                    PLACE=PLACES[positiond];
                    PLACE_OF_VISIT.setText(PLACES[positiond]);


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

                if(s.length()==0)
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

                if(s.length()!=10)
                {
                    if(ERROR_MOBILE.getVisibility()== View.INVISIBLE)
                    {
                        ERROR_MOBILE.setVisibility(View.VISIBLE);
                    }

                }
                else if(s.length()==0)
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


                        UNAVAILABLE_DATES.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {





                                if(dataSnapshot.hasChild(sdf.format(myCalendar.getTime())))
                                {
                                    Toast.makeText(getApplicationContext(),"Selected date is unavailable",Toast.LENGTH_SHORT).show();
                                    if(ERROR_DATE.getVisibility()== View.INVISIBLE)
                                    {
                                        ERROR_DATE.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                {
                                    Dateofjourney.setText(sdf.format(myCalendar.getTime()));

                                    if(ERROR_DATE.getVisibility()== View.VISIBLE)
                                    {
                                        ERROR_DATE.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });





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


        //PayPalService Call
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);



        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


             /*   String name = UUID.randomUUID().toString();
                destination = new File(Environment
                        .getExternalStorageDirectory(), name + ".jpg");
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                     /*  CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                               Uri.fromFile(destination));
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "CameraDemo");

                if (!mediaStorageDir.exists()) {
                    mediaStorageDir.mkdirs();
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                f=new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg");

                CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(f));
                startActivityForResult(CameraIntent,CAMERA_OPEN);*/

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

                final String Names = Name.getText().toString().trim();
                final String Addresses = Address.getText().toString().trim();
                Mobiles = Mobile.getText().toString().trim();
                final String ID_NO = ID_No.getText().toString().trim();
                final String Purposes = Purpose.getText().toString().trim();
                final String DateOfBirth = Dateofbirth.getText().toString().trim();
                final String DateOfJourney = Dateofjourney.getText().toString().trim();



                   // if(mAwesomeValidation.validate())
                  //  {
                      /*  if(  !( ID_Source.contains("Tap") || TextUtils.isEmpty(PLACE) ||  TextUtils.isEmpty(Names) || TextUtils.isEmpty(Addresses) || TextUtils.isEmpty(DateOfJourney) || TextUtils.isEmpty(DateOfBirth) || TextUtils.isEmpty(Mobiles) || TextUtils.isEmpty(ID_NO) || TextUtils.isEmpty(Purposes) || TextUtils.isEmpty(byteArray.toString()) || TextUtils.isEmpty(scaniduri.toString()) )
                                &&ERROR_NAME.getVisibility()==View.INVISIBLE&&ERROR_MOBILE.getVisibility()==View.INVISIBLE&&
                                ERROR_DATE.getVisibility()==View.INVISIBLE)
                        {
                            UNAVAILABLE_DATES.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {





                                    if(dataSnapshot.hasChild(DateOfJourney))
                                    {
                                        Toast.makeText(getApplicationContext(),"Selected date is unavailable",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        PRICE_OF_PLACE.child("Prices").child(PLACE).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                getPayment(dataSnapshot.getValue(String.class));

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
}

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            // getPayment();
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
                                Toast.makeText(getApplicationContext(),"Please fill the empty fields and upload your profile photo and Scan_id",Toast.LENGTH_SHORT).show();

                            }
                        }
                  //  } */

                SubmitApplication();





            }
        });
    }
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        THE_TEST=0;
        super.onDestroy();
    }
   Bitmap encodeAsBitmap(String str,int WIDTH) throws WriterException {
       BitMatrix result;
       try {
           result = new MultiFormatWriter().encode(str,
                   BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
       } catch (IllegalArgumentException iae) {
           // Unsupported format
           return null;
       }
       int w = result.getWidth();
       int h = result.getHeight();
       int[] pixels = new int[w * h];
       for (int y = 0; y < h; y++) {
           int offset = y * w;
           for (int x = 0; x < w; x++) {
               pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
           }
       }
       Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
       bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
       return bitmap;
   }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }


    private void getPayment(String cost) {
        //Getting the amount from editText
        paymentAmount = cost;

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Pass Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }



    private void SubmitApplication() {


        final String Names=Name.getText().toString().trim();
        final String Addresses= Address.getText().toString().trim();
        Mobiles= Mobile.getText().toString().trim();
        final String ID_NO= ID_No.getText().toString().trim();
        final String Purposes= Purpose.getText().toString().trim();
        final String DateOfBirth=Dateofbirth.getText().toString().trim();
        final String DateOfJourney=Dateofjourney.getText().toString().trim();



     /*   rxConnect.setParam(Constants.REGISTRATION_USER_MOBILE_KEY,"HELLO");
        rxConnect.setParam(Constants.REGISTRATION_USER_EMAIL_KEY,"HELLOIZ");
        rxConnect.execute(Constants.IMAGE_SEND_LINK,RxConnect.POST, new RxConnect.RxResultHelper() {


            @Override
            public void onResult(String result) {

                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                Log.v("Result",result);



                try {
                    JSONObject jsonObject=new JSONObject(result);
                    Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();


                }
                catch (Exception ex)
                {

                }

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


        }); */





        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.IMAGE_SEND_LINK, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {



                String RESPONSE=new String(response.data);

                Log.v("JSONRESPONSE",RESPONSE+response.statusCode);
                try {

                    String jsonString = new String(response.data, "UTF-8");
                    JSONObject jsonObject=new JSONObject(jsonString);

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
                Intent intents=new  Intent(getApplicationContext(),MainActivity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intents,PendingIntent.FLAG_UPDATE_CURRENT);


                Log.v("JSONRESPONSE",error.getMessage()+"EEROR");


                String json =error.toString();
                try
                {
                    JSONObject jsonObject=new JSONObject(json);



                }
                catch (Exception e)
                {

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {



                Map<String, String> params = new HashMap<>();
                params.put("filename","FILE");
                params.put("uploader","HELLO");
                params.put("issue", "HE");
                //params.put("submit","checked");
                params.put("uploadervisible","VISI");
                //   params.put("fileToUpload",ImageBase64);
                params.put("mode","android");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView


                params.put("picture", new DataPart(imageuri.getLastPathSegment()+"."+MIME, b, "image/jpeg"));
                params.put("picture1",new DataPart(imageuriProfile.getLastPathSegment()+"."+"jpeg",PROFILE_PIC_BYTE_ARRAY,"image/jpeg"));
                Log.d("file",imageuriProfile.getLastPathSegment()+" " + b.length/1024);
                return params;
            }

        };
        //Creating a Request Queue
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue2.add(multipartRequest);




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
    public String getPath(Uri uri)
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==PROFILE_PHOTO)
        {
            //When Applicant photo is selected

                profilephoto=data.getData();

                Bitmap photo = (Bitmap) data.getExtras().get("data");


                int p[]=getResolution(photo.getWidth(),photo.getHeight());
                photo=Bitmap.createScaledBitmap(photo,p[0],p[1],true);




                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                PROFILE_PIC_BYTE_ARRAY = stream.toByteArray();


                imageuriProfile=  getImageUri(getApplicationContext(), photo);

                Log.v("PATH_OF_FILE",imageuriProfile.getLastPathSegment());


                Glide.with(Passdetails.this)
                        .load(PROFILE_PIC_BYTE_ARRAY)
                        .into(Profile);

               // Profile.setImageURI(profilephoto);


        }
        else if(resultCode==RESULT_OK && requestCode==SCAN_ID)
        {
            //when applicant scanned user id is selected

            imageuri=data.getData();
//            Log.v("Dinkus25",imageuri.getPath());
            MIME= GetMimeType.GetMimeType(Passdetails.this,imageuri);

            if(MIME.contains("/jpg") || MIME.contains("/JPG"))
            {
                MIME="jpg";
            }
            else if(MIME.contains("/png") || MIME.contains("/PNG"))
            {
                MIME="png";

            }
            else if(MIME.contains("/jpeg") || MIME.contains("/JPEG"))
            {
                MIME="jpeg";
            }
            else if(MIME.contains("/gif") || MIME.contains("/GIF"))
            {
                MIME="gif";
            }


            ImagePath=imageuri.getPath();

            Log.v("Dinkus17",ImagePath+"  "+imageuri.toString());

            try {
                //Getting the Bitmap from Gallery
                file=new File(getPath(imageuri));

               b = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(b);
                for (int i = 0; i < b.length; i++) {
                    System.out.print((char)b[i]);
                }
                Log.d("file","file size:"+b.length/1024);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                Log.d("file","file size: 2 ");
                int p[]=getResolution(bitmap.getWidth(),bitmap.getHeight());
                scaled=Bitmap.createScaledBitmap(bitmap,p[0],p[1],true);

                Log.d("file","file size: 2 ");


                scan_id.setImageBitmap(scaled);

                Log.d("file","file size: 3 ");

            } catch (IOException e) {

                Toast.makeText(getApplicationContext(),"FILE ERROR",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

                Log.v("GALLERY",e.getLocalizedMessage());
            }


        }
        else  if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);



                //if confirmation is not null
                if (confirm != null) {
                    try {


                        JSONObject object=confirm.toJSONObject();
                        JSONObject response=object.getJSONObject("response");
                        state=response.getString("state");
                        id=response.getString("id");
               // bitmap_BAR_CODE = encodeAsBitmap(id, BarcodeFormat.CODE_128, 600, 300);  -->Originally
                        bitmap_QR_CODE = encodeAsBitmap(id,500);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap_QR_CODE.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        BARCODE_BYTE_ARRAY = stream.toByteArray();


                        if(state.equals("approved"))
                        {

                                SubmitApplication();
                        }
                    } catch (JSONException e) {

                    } catch (WriterException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Unable to generate barcode image",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

}
