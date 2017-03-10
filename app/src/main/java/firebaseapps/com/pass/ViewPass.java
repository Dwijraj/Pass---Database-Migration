package firebaseapps.com.pass;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


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

import firebaseapps.com.pass.UI.Passdetails;
import firebaseapps.com.pass.UI.Vehicles;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.QR_Codegenerator;


public class ViewPass extends AppCompatActivity {

    private ImageView scan_id2;
    private TextView Name2;
    private TextView Address2;
    private Button Vehicle;
    private Button generatebarcode;
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
    private TextView Gate;
    private TextView place_of_visit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Passdetails.THE_TEST==1)
            finish();
        setContentView(R.layout.pass_history);

        place_of_visit=(TextView) findViewById(R.id.place);
        Gate=(TextView) findViewById(R.id.GATE);
        CarNumber=(TextView)findViewById(R.id.Carnumbers);
        DriverName=(TextView)findViewById(R.id.DriverNAME);
        final  ImageView imageView=(ImageView) findViewById(R.id.BAR_CODE_SHOW);
        final TextView  Pass=(TextView)findViewById(R.id.PassNumber);
        generatebarcode=(Button)findViewById(R.id.Display_Bar_Code);
        Vehicle=(Button)findViewById(R.id.vehicle);
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
        ApplicationRef2= FirebaseDatabase.getInstance().getReference().child("Applications");//Points to the root directory of the Database


        Intent i=getIntent();
        String Pass_number=i.getExtras().getString("PassNumber");
        String Editable=i.getExtras().getString("editable");

        if (Editable.equals("1"))
        {

            generatebarcode.setText("Submit Changes");
            //Submit the changes


        }

        try {


            JSONObject Values=new JSONObject(View_Pass.PASS_DETAILS);

            Log.v("Here","1");

            JSONObject jsonObject=Values.getJSONObject("application_info");


            Log.v("Here","2");
          //  jsonArray.getJSONObject()

            String Name= JsonParser.JSONValue(jsonObject,"applicant_name");

            Log.v("Here","3");
            String Address= JsonParser.JSONValue(jsonObject,"applicant_address");

            Log.v("Here","4");
            String PlaceOfVisit= JsonParser.JSONValue(jsonObject,"place_visting");

            Log.v("Here","5");
            String Mobile= JsonParser.JSONValue(jsonObject,"application_mobile");

            Log.v("Here","6");
            String IDNumber= JsonParser.JSONValue(jsonObject,"applicant_id_no");

            Log.v("Here","7");
            String IDSource= JsonParser.JSONValue(jsonObject,"applicant_id_source");

            Log.v("Here","8");
            String DateOfBirth= JsonParser.JSONValue(jsonObject,"dob");

            Log.v("Here","9");
            String Purpose= JsonParser.JSONValue(jsonObject,"purpose_visting");

            Log.v("Here","10");
            String DateOfJourney= JsonParser.JSONValue(jsonObject,"date_journey");

            Log.v("Here","11");
            String Res= JsonParser.JSONValue(jsonObject,"Photo");

            String Profile = Res.replaceAll("\"","");

            Log.v("Here","12"+Profile);
            String ResId= JsonParser.JSONValue(jsonObject,"Scan_id_photo");

            String ScanId=ResId.replaceAll("\"","");

            Log.v("Here","13"+ScanId);
            String ApplicationStatus=JsonParser.JSONValue(jsonObject,"paid_status");

            Log.v("Here","14");

            Name2.setText(Name);
            Address2.setText(Address);
            Mobile2.setText(Mobile);
            ID_No2.setText(IDNumber);
            Dateofbirth2.setText(DateOfBirth);
            Dateofjourney2.setText(DateOfJourney);
            Purpose2.setText(Purpose);
            Application_status2.setText(ApplicationStatus.toUpperCase());
            ID_source.setText(IDSource);
            // CarNumber.setText(Carnumber);
            // DriverName.setText(app.Drivername);
            // Gate.setText(app.Gate);
            place_of_visit.setText(PlaceOfVisit);

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

              /*  Glide.with(getApplicationContext())
                        .load(bitmap_QR_CODE)
                        .into(imageView); */




            }
            catch (Exception e)
            {
               Log.v("Here",e.getLocalizedMessage());// e.printStackTrace();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
