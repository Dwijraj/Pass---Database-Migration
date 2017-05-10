package firebaseapps.com.pass.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.QR_Codegenerator;
import mohitbadwal.rxconnect.RxConnect;

public class DisplayPass extends AppCompatActivity {

    private ActionBar actionBar;
    private ImageView scan_id2;
    private ImageView DL_IMAGE_VIEW;
    private ImageView RC_BOOK_IMAGE_VIEW;
    private ImageView INSURANCE_IMAGE_VIEW;
    private TextView  Name2;
    private TextView  Address2;
    private TextView  Mobile2;
    private TextView  CarNumber;
    private TextView  DriverName;
    private TextView  Dateofbirth2;
    private TextView  Dateofjourney2;
    private TextView  DESTINATION;
    private TextView  ID_No2;
    private TextView  Purpose2;
    private TextView  DRIVER_LICENSE;
    private TextView  VEHICLE_MODEL;
    private TextView  Scan_id2;
    private ImageView Profile2;
    private String    pass;
    private TextView  ID_Sources;
    private TextView  Application_status2;
    private int       WIDTH_SCREEN;
    private String    APPLICATION_STATUS;
    private int       HEIGHT_SCREEN;
    private boolean   Check;
    private RxConnect rxConnect;
    private ImageView    QR_CODE;
    private String    PassDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_display);

        DateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
        Log.v("DATEFORMAT",dateFormat.format(new Date(System.currentTimeMillis())));

       DRIVER_LICENSE=(TextView) findViewById(R.id.driver_license_id_DISPLAY_PASS);
        VEHICLE_MODEL=(TextView) findViewById(R.id.vehicle_model_id_DISPLAY_PASS);
        DL_IMAGE_VIEW=(ImageView) findViewById(R.id.DL_PIC_DISPLAY_PASS);
        INSURANCE_IMAGE_VIEW=(ImageView) findViewById(R.id.INSURANCE_PIC_DISPLAY_PASS);
        RC_BOOK_IMAGE_VIEW=(ImageView) findViewById(R.id.RC_BOOK_PIC_DISPLAY_PASS);
        QR_CODE=(ImageView)findViewById(R.id.QR_CODE);
        rxConnect=new RxConnect(this);
        rxConnect.setCachingEnabled(false);
        DESTINATION=(TextView)findViewById(R.id.DESTINATION_DISPLAY_PASS);
        CarNumber=(TextView)findViewById(R.id.car_num_DISPLAY_PASS);
        DriverName=(TextView)findViewById(R.id.driver_name_DISPLAY_PASS);
        scan_id2=(ImageView)findViewById(R.id.SCAN_PIC_DISPLAY_PASS) ;
        Name2=(TextView)findViewById(R.id.SCAN_NAME_DISPLAY_PASS);
        Address2=(TextView)findViewById(R.id.SCAN_ADDRESS_DISPLAY_PASS);
        Mobile2=(TextView)findViewById(R.id.SCAN_MOBILE_DISPLAY_PASS);
        ID_No2=(TextView)findViewById(R.id.SCAN_ID_DISPLAY_PASS);
        ID_Sources=(TextView)findViewById(R.id.ID_Source_DISPLAY_PASS);
        Dateofbirth2=(TextView)findViewById(R.id.SCAN_DOB_DISPLAY_PASS);
        Dateofjourney2=(TextView)findViewById(R.id.SCAN_DOJ_DISPLAY_PASS);
        Purpose2=(TextView)findViewById(R.id.SCAN_REASON_DISPLAY_PASS);
        Profile2=(ImageView)findViewById(R.id.SCAN_PROFILE_DISPLAY_PASS);
        Application_status2=(TextView)findViewById(R.id.SCAN_STATUS_DISPLAY_PASS);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);




        Intent i=getIntent();
        Bundle extras=i.getExtras();
        pass=extras.getString("Pass");
        PassDetails=extras.getString("PassDetails");

        Log.v("ValuePassed",pass);
        Log.v("ValuePassed",PassDetails);

        Bitmap bitmap_QR_CODE = null;



        WindowManager wm = (WindowManager) DisplayPass.this.getSystemService(Context.WINDOW_SERVICE);
        android.view.Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        WIDTH_SCREEN = metrics.widthPixels;
        HEIGHT_SCREEN = metrics.heightPixels;


        android.view.ViewGroup.LayoutParams layoutParams = Profile2.getLayoutParams();
        layoutParams.width = WIDTH_SCREEN/2;
        layoutParams.height = HEIGHT_SCREEN/3;
        Profile2.setLayoutParams(layoutParams);




        try {

            /**
             * {"response_status":"1",
             *  "msg":"sucess",
             *  "application_info":{"token_no":"token_17-04-1017:05:4411",
             *                       "applicant_name":"Dwijraj",
             *                       "applicant_address":"dhhf",
             *                       "place_visting":"konark",
             *                       "application_mobile":"8093679890",
             *                       "applicant_id_source":"Passport",
             *                       "applicant_id_no":"scheme",
             *                       "dob":"2017-04-10",
             *                       "purpose_visting":"Roam",
             *                       "Photo":"http:\/\/192.168.2.2\/pass3\/upload\/pic\/17:05:44187487.jpeg",
             *                       "Scan_id_photo":"http:\/\/192.168.2.2\/pass3\/upload\/pic\/17:05:4469025.jpeg",
             *                       "paid_status":"1","status":"1","date_journey":"2017-04-15"},
             * "vehicle":{"token_no":"token_17-04-1017:05:4411",
             *           // "vehicle_novehicle_no":"fhhdbn",
             *            //"driver_name":"disowned",
             *            "driver_licence":"chunks",
             *            "vehicle_mode":"cjjxjfnd",
             *            "vehicle_rc_card_scan":"http:\/\/192.168.2.2\/pass3\/upload\/vehicle\/rc_book\/17:23:5269025.jpeg",
             *            "vehicle_insurance_scan":"http:\/\/192.168.2.2\/pass3\/upload\/vehicle\/insurance\/17:23:52186826.jpeg",
             *            "driver_licence_scan":"http:\/\/192.168.2.2\/pass3\/upload\/vehicle\/driver_scan\/17:23:5218982.jpeg"}}

             *
             *



            /**
             * {"response_status":"1",
             * "msg":"sucess",
             * "PASS_NOS":"aSCh8XP7MJcdxp61",
             * "application_info":{"token_no":"token_1705091818371",
             *                     "applicant_name":"scabs",
             *                     "applicant_address":"scabs",
             *                     "place_visting":"konark",
             *                     "application_mobile":"8093679890",
             *                     "applicant_id_source":"Adhar Card",
             *                     "applicant_id_no":"sbzbhzhzgzvzv",
             *                     "dob":"2017-05-09",
             *                     "purpose_visting":"Tourism",
             *                     "Photo":"http:\/\/innovadorslab.co.in\/pass3\/upload\/pic\/18:18:37197834.jpeg",
             *                     "Scan_id_photo":"http:\/\/innovadorslab.co.in\/pass3\/upload\/pic\/18:18:3769025.jpeg",
             *                     "paid_status":"1","status":"1","date_journey":"2017-05-14"},
             * "vehicle":{"token_no":"token_1705091818371",
             *            "vehicle_novehicle_no":"ggbvsg",
             *            "driver_name":"shaga",
             *            "driver_licence":"zhahha",
             *            "vehicle_mode":"manhandled",
             *            "vehicle_rc_card_scan":"http:\/\/innovadorslab.co.in\/pass3\/upload\/vehicle\/rc_book\/18:19:4969025.jpeg",
             *            "vehicle_insurance_scan":"http:\/\/innovadorslab.co.in\/pass3\/upload\/vehicle\/insurance\/18:19:4969025.jpeg",
             *            "driver_licence_scan":"http:\/\/innovadorslab.co.in\/pass3\/upload\/vehicle\/driver_scan\/18:19:4969025.jpeg"},
             * "today":"2017-05-09"}

             */



            JSONObject jsonObject2=new JSONObject(PassDetails);
            String DATE_TODAY= JsonParser.JSONValue(jsonObject2,"today");
            String PASS_NUMBER=JsonParser.JSONValue(jsonObject2,"PASS_NOS");
            Log.v("DATETODAY1",DATE_TODAY+"...");

            try {

                bitmap_QR_CODE = QR_Codegenerator.encodeAsBitmap(PASS_NUMBER,500);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap_QR_CODE.compress(Bitmap.CompressFormat.PNG, 100, stream);

                QR_CODE.setImageBitmap(bitmap_QR_CODE);


            }catch (WriterException w)
            {

            }



            JSONObject jsonObject=jsonObject2.getJSONObject("application_info");
            Log.v("Here","1");
            JSONObject jsonObject1=jsonObject2.getJSONObject("vehicle");
            Log.v("Here","1.1");
            String Vehicle_no=JsonParser.JSONValue(jsonObject1,"vehicle_novehicle_no");
            Log.v("Here","1.2");
            String Driver_Name=JsonParser.JSONValue(jsonObject1,"driver_name");
            Log.v("Here","1.3");
            String Driver_License=JsonParser.JSONValue(jsonObject1,"driver_licence");
            Log.v("Here","1.4");
            String Vehicle_Model=JsonParser.JSONValue(jsonObject1,"vehicle_mode");
            Log.v("Here","1.5");
            final String RC_LINK=JsonParser.JSONValue(jsonObject1,"vehicle_rc_card_scan").replaceAll("\"","");
            Log.v("Here","1.6");
            final String DL_LINK=JsonParser.JSONValue(jsonObject1,"driver_licence_scan").replaceAll("\"","");
            Log.v("Here","1.7");
            final String INSURANCE_LINK=JsonParser.JSONValue(jsonObject1,"vehicle_insurance_scan").replaceAll("\"","");
            Log.v("Here","1.8");
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
            final String Profile = Res.replaceAll("\"","");
            String ResId= JsonParser.JSONValue(jsonObject,"Scan_id_photo");
            final String ScanId=ResId.replaceAll("\"","");
            String ApplicationStatus=JsonParser.JSONValue(jsonObject,"paid_status");
            APPLICATION_STATUS=ApplicationStatus;
            DRIVER_LICENSE.setText(Driver_License);
            VEHICLE_MODEL.setText(Vehicle_Model);
            DriverName.setText(Driver_Name);
            CarNumber.setText(Vehicle_no);
            Name2.setText(Name);
            Address2.setText(Address);
            Mobile2.setText(Mobile);
            ID_No2.setText(IDNumber);
            Dateofbirth2.setText(DateOfBirth);
            Dateofjourney2.setText(DateOfJourney);
            Purpose2.setText(Purpose);
            Application_status2.setText(ApplicationStatus.toUpperCase());


            ID_Sources.setText(IDSource);
            DESTINATION.setText(PlaceOfVisit);


            Glide.with(getApplicationContext())
                    .load(Profile)
                    .into(Profile2);

            Profile2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Zoomed(Profile);

                }
            });


            Glide.with(getApplicationContext())
                    .load(ScanId)
                    .into(scan_id2);

            scan_id2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Zoomed(ScanId);

                }
            });

            Glide.with(getApplicationContext())
                    .load(RC_LINK)
                    .into(RC_BOOK_IMAGE_VIEW);

            RC_BOOK_IMAGE_VIEW.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    Zoomed(RC_LINK);
                }
            });

            Glide.with(getApplicationContext())
                    .load(INSURANCE_LINK)
                    .into(INSURANCE_IMAGE_VIEW);
            INSURANCE_IMAGE_VIEW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Zoomed(INSURANCE_LINK);
                }
            });

            Glide.with(getApplicationContext())
                    .load(DL_LINK)
                    .into(DL_IMAGE_VIEW)  ;

            DL_IMAGE_VIEW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Zoomed(DL_LINK);
                }
            });








        }catch (JSONException e)
        {

        }





    }
    public void Zoomed(String URL)
    {
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.zoomed);

        ImageView Image=(ImageView) dialog.findViewById(R.id.ZoomedInImage);

        Glide.with(DisplayPass.this)
                .load(URL)
                .into(Image);

        dialog.show();

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
}
