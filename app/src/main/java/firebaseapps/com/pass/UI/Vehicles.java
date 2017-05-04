package firebaseapps.com.pass.UI;


import android.app.Dialog;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.ApplicationParams;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.GetMimeType;
import firebaseapps.com.pass.Utils.JsonParser;
import firebaseapps.com.pass.Utils.NetworkUtil;
import firebaseapps.com.pass.Utils.QR_Codegenerator;
import firebaseapps.com.pass.Utils.VolleyMultipartRequest;

public class Vehicles extends AppCompatActivity {

    public static String APPLICATION_NUMBER;
    private ImageView  DriverLicense;
    private ImageView  Insurance;
    private ImageView  RCBook;
    private Uri DRIVER_LICENSE_URI;
    private Uri INSURANCE_URI;
    private Uri RCBook_URI;
    private String RCBOOK_MIME;
    private String INSURANCE_MIME;
    private String DRIVER_LICENSE_MIME;
    private File INSURANCE_FILE;
    private File RC_BOOK_FILE;
    private File DRIVER_LICENSE_FILE;
    private byte[] RC_BOOK_BYTE_ARRAY;
    private byte[] DRIVER_LICENSE_BYTE_ARRAY;
    private byte[] INSURANCE_BYTE_ARRAY;
    private EditText Drivername;
    private EditText DriverLicenseNumber;
    private EditText VehicleNumber;
    private EditText VehicleModel;
    private int Selected;
    private final int DRIVER_LICENSE_REQUEST_CODE=343;       //To recognise in onActivityresult
    private final int INSURANCE_REQUEST_CODE=434;
    private final int RCBook_REQUEST_CODE=535;
    private Button Submit;
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
        setContentView(R.layout.activity_vehicles);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);



        Drivername=(EditText)findViewById(R.id.Driversname);
        DriverLicenseNumber=(EditText) findViewById(R.id.DriverLicenseNumber);
        VehicleNumber=(EditText) findViewById(R.id.VehcileNumber);
        VehicleModel=(EditText) findViewById(R.id.VehicleModel);
        DriverLicense = (ImageView) findViewById(R.id.driverlicense);
        Insurance = (ImageView) findViewById(R.id.insurance);
        RCBook = (ImageView) findViewById(R.id.RCBook);
        Submit = (Button) findViewById(R.id.submitVehicleDetails);

        Insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, INSURANCE_REQUEST_CODE);
            }
        });

        RCBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RCBook_REQUEST_CODE);
            }
        });

        DriverLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, DRIVER_LICENSE_REQUEST_CODE);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String DRIVERS_NAME=Drivername.getText().toString().trim();
                final String DRIVER_LICENSE_NUMBER=DriverLicenseNumber.getText().toString().trim();
                final String VEHICLE_NUMBER=VehicleNumber.getText().toString().trim();
                final String VEHICLE_MODEL=VehicleModel.getText().toString().trim();

                if(IsCorrect())
                {

                    Log.v("Response","Working2");
                    VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.ONLINE_VEHICLE_DETAILS_UPDATE_LINK, new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            String RESPONSE=new String(response.data);
                            try {

                                JSONObject jsonObject=new JSONObject(RESPONSE);
                                Log.v("Response",jsonObject.toString()+"...");

                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {



                            Map<String, String> params = new HashMap<>();
                            params.put("driver_name",DRIVERS_NAME);
                            params.put("driver_licence",DRIVER_LICENSE_NUMBER);
                            params.put("vehicle_no",VEHICLE_NUMBER);
                            params.put("vehicle_mode",VEHICLE_MODEL);
                            params.put("application_no",APPLICATION_NUMBER);

                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file base or direct access from real path
                            // for now just get bitmap data from ImageView


                            params.put("picture5", new DataPart(DRIVER_LICENSE_URI.getLastPathSegment()+"."+DRIVER_LICENSE_MIME, DRIVER_LICENSE_BYTE_ARRAY, "image/jpeg"));
                            params.put("picture3", new DataPart(RCBook_URI.getLastPathSegment()+"."+RCBOOK_MIME, RC_BOOK_BYTE_ARRAY, "image/jpeg"));
                            params.put("picture4", new DataPart(INSURANCE_URI.getLastPathSegment()+"."+INSURANCE_MIME, INSURANCE_BYTE_ARRAY, "image/jpeg"));

                            return params;
                        }

                    };
                    //Creating a Request Queue
                    RequestQueue requestQueue2 = Volley.newRequestQueue(Vehicles.this);
                    //Adding request to the queue
                    requestQueue2.add(multipartRequest);


                }
                else
                {
                    Toasty.warning(getApplicationContext(),"Please fill all details",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private Boolean IsCorrect()
    {
        final String DRIVERS_NAME=Drivername.getText().toString().trim();
        final String DRIVER_LICENSE_NUMBER=DriverLicenseNumber.getText().toString().trim();
        final String VEHICLE_NUMBER=VehicleNumber.getText().toString().trim();
        final String VEHICLE_MODEL=VehicleModel.getText().toString().trim();

        if(!(TextUtils.isEmpty(DRIVERS_NAME)&&TextUtils.isEmpty(DRIVER_LICENSE_NUMBER)&&TextUtils.isEmpty(VEHICLE_NUMBER)
                &&TextUtils.isEmpty(VEHICLE_MODEL)&&DRIVER_LICENSE_BYTE_ARRAY==null&&RC_BOOK_BYTE_ARRAY==null&&INSURANCE_BYTE_ARRAY==null))
        {

            return true;
        }

        return false;
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==DRIVER_LICENSE_REQUEST_CODE)
        {
            DRIVER_LICENSE_URI=data.getData();

            DRIVER_LICENSE_MIME= GetMimeType.GetMimeType(Vehicles.this,DRIVER_LICENSE_URI);

            DRIVER_LICENSE_MIME=GetMimeType.ReturnCorrectMime(DRIVER_LICENSE_MIME);

            try {
                //Getting the Bitmap from Gallery
                DRIVER_LICENSE_FILE=new File(getPath(DRIVER_LICENSE_URI));

                DRIVER_LICENSE_BYTE_ARRAY = new byte[(int) DRIVER_LICENSE_FILE.length()];
                FileInputStream fileInputStream = new FileInputStream(DRIVER_LICENSE_FILE);
                fileInputStream.read(DRIVER_LICENSE_BYTE_ARRAY);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), DRIVER_LICENSE_URI);
                int p[]=getResolution(bitmap.getWidth(),bitmap.getHeight());
                Bitmap scaled=Bitmap.createScaledBitmap(bitmap,p[0],p[1],true);



                DriverLicense.setImageBitmap(scaled);


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(),"FILE ERROR",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }




        }
        else if(resultCode==RESULT_OK && requestCode==RCBook_REQUEST_CODE)
        {
            RCBook_URI=data.getData();

            RCBOOK_MIME=GetMimeType.GetMimeType(Vehicles.this,DRIVER_LICENSE_URI);

            RCBOOK_MIME=GetMimeType.ReturnCorrectMime(RCBOOK_MIME);

            try {
                //Getting the Bitmap from Gallery
                RC_BOOK_FILE=new File(getPath(RCBook_URI));

                RC_BOOK_BYTE_ARRAY = new byte[(int) RC_BOOK_FILE.length()];
                FileInputStream fileInputStream = new FileInputStream(RC_BOOK_FILE);
                fileInputStream.read(RC_BOOK_BYTE_ARRAY);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), RCBook_URI);
                int p[]=getResolution(bitmap.getWidth(),bitmap.getHeight());
                Bitmap scaled=Bitmap.createScaledBitmap(bitmap,p[0],p[1],true);



                RCBook.setImageBitmap(scaled);


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(),"FILE ERROR",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }


        }
        else if(resultCode==RESULT_OK && requestCode==INSURANCE_REQUEST_CODE)
        {
            INSURANCE_URI=data.getData();

            INSURANCE_MIME=GetMimeType.GetMimeType(Vehicles.this,INSURANCE_URI);

            INSURANCE_MIME=GetMimeType.ReturnCorrectMime(INSURANCE_MIME);

            try {
                //Getting the Bitmap from Gallery
                INSURANCE_FILE=new File(getPath(INSURANCE_URI));

                INSURANCE_BYTE_ARRAY = new byte[(int) INSURANCE_FILE.length()];
                FileInputStream fileInputStream = new FileInputStream(INSURANCE_FILE);
                fileInputStream.read(INSURANCE_BYTE_ARRAY);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), INSURANCE_URI);
                int p[]=getResolution(bitmap.getWidth(),bitmap.getHeight());
                Bitmap scaled=Bitmap.createScaledBitmap(bitmap,p[0],p[1],true);



                Insurance.setImageBitmap(scaled);


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(),"FILE ERROR",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }




        }

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

}
