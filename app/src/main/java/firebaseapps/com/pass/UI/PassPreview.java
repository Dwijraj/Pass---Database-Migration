package firebaseapps.com.pass.UI;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarException;

import firebaseapps.com.pass.Application;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.QR_Codegenerator;
import firebaseapps.com.pass.View_Pass;
import mohitbadwal.rxconnect.RxConnect;

public class PassPreview extends AppCompatActivity {


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
    public Application app;
    private TextView ID_source;
    private static  String Changed_Values_flag="0";
    private TextView Gate;
    private TextView place_of_visit;
    private ImageButton DOJ_CHANGE;
    private Boolean Changed=false;
    private String Original_Date_Of_Journey;
    HashMap<String,String> PriceNPlace=new HashMap<>();
    ArrayList<String> PLACES;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_preview);


        place_of_visit=(TextView) findViewById(R.id.place);
        Gate=(TextView) findViewById(R.id.GATE);
        CarNumber=(TextView)findViewById(R.id.Carnumbers);
        DriverName=(TextView)findViewById(R.id.DriverNAME);
        final ImageView imageView=(ImageView) findViewById(R.id.BAR_CODE_SHOW);
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

            try {


                Glide.with(getApplicationContext())
                        .load(Profile)
                        .into(Profile2);


                Glide.with(getApplicationContext())
                        .load(ScanId)
                        .into(scan_id2);
                


            }
            catch (Exception e)
            {
                Log.v("Here",e.getLocalizedMessage());// e.printStackTrace();
            }

        }catch (JSONException js)
        {

        }









    }
}
