package firebaseapps.com.pass.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Adapter.CustomAdapter;
import firebaseapps.com.pass.Constants.ApplicationParams;
import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.Constants.OPTION_SELECTED;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.GetMimeType;
import firebaseapps.com.pass.Utils.VolleyMultipartRequest;

public class Documentschange extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText ID_NUMBER;
    private Spinner  ID_SOURCE;
    private ImageView PROFILE_PIC;
    private ImageView SCAN_ID;
    private Button CLICK_PICTURE;
    private Button UPLOAD_SCAN_ID;
    private Button SUBMIT_UPDATED_INFO;
    private ImageView  DriverLicense;
    private ImageView  Insurance;
    private ImageView  RCBook;
    private Uri DRIVER_LICENSE_URI;
    private Uri INSURANCE_URI;
    private Uri RCBook_URI;
    private Uri PROFILE_PIC_CHANGE_URI;
    private Uri SCAN_ID_URI;
    private String RCBOOK_MIME;
    private String INSURANCE_MIME;
    private String DRIVER_LICENSE_MIME;
    private String PROFILE_PIC_CHANGE_MIME;
    private String SCAN_ID_CHANGE_MIME;
    private File INSURANCE_FILE;
    private File RC_BOOK_FILE;
    private File DRIVER_LICENSE_FILE;
    private File PROFILE_PIC_CHANGE_FILE;
    private File SCAN_ID_CHANGE_FILE;
    private byte[] RC_BOOK_BYTE_ARRAY;
    private byte[] DRIVER_LICENSE_BYTE_ARRAY;
    private byte[] INSURANCE_BYTE_ARRAY;
    private byte[]  PROFILE_PIC_CHANGE_BYTE_ARRAY;
    private byte[]  SCAN_ID_CHANGE_BYTE_ARRAY;
    private EditText Drivername;
    private EditText DriverLicenseNumber;
    private EditText VehicleNumber;
    private EditText VehicleModel;
    private int Selected;
    private final int DRIVER_LICENSE_REQUEST_CODE=343;       //To recognise in onActivityresult
    private final int INSURANCE_REQUEST_CODE=434;
    private final int RCBook_REQUEST_CODE=535;
    private final int PROFILE_PIC_CHANGE_REQUESTCODE=340;
    private final int SCAN_ID_CHANGE_REQUESTCODE=430;
    private Button Submit;
    private String SOURCE_DESCRIPTION;

    private ArrayList<String> paths=new ArrayList<>();

    private LinearLayout Vehicle_Layout;
    private LinearLayout Application_Layout;
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
        setContentView(R.layout.activity_documentschange);

        Vehicle_Layout=(LinearLayout) findViewById(R.id.VEHICLE_CHANGE_LAYOUT_1996);
        Application_Layout=(LinearLayout) findViewById(R.id.APPLICATION_CHANGE_LAYOUT_1996);

        progressDialog=new ProgressDialog(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        if(CheckPassDetails.CHANGEABLE.equals(OPTION_SELECTED.OPTION_CHANGEABLE_APPLICATION))
        {
            SOURCE_DESCRIPTION=null;
            paths.add("");
            paths.add("Passport");
            paths.add("Adhar Card");
            paths.add("Driving License");


             Vehicle_Layout.setVisibility(View.INVISIBLE);

            Log.v("Documents","HERE");

            PROFILE_PIC=(ImageView) findViewById(R.id.PROFILE_PICTURE_CHANGE);
            SCAN_ID=(ImageView) findViewById(R.id.ID_PROOF_SCAN_CHANGE);
            CLICK_PICTURE=(Button) findViewById(R.id.PROFILE_CHANGE);
            UPLOAD_SCAN_ID=(Button) findViewById(R.id.UPLOAD_ID_CHANGE);
            SUBMIT_UPDATED_INFO=(Button) findViewById(R.id.SUBMIT_APPLICATION);
            ID_NUMBER=(EditText) findViewById(R.id.id_no_change);
            ID_SOURCE=(Spinner) findViewById(R.id.ID_PROOF_change);

            CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),paths);
            ID_SOURCE.setAdapter(customAdapter);

            ID_SOURCE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position==0)
                        SOURCE_DESCRIPTION=null;

                    else
                        SOURCE_DESCRIPTION=paths.get(position);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            CLICK_PICTURE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent PICK_PROFILE_PIC=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(PICK_PROFILE_PIC, PROFILE_PIC_CHANGE_REQUESTCODE);

                }
            });

            UPLOAD_SCAN_ID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SCAN_ID_CHANGE_REQUESTCODE);

                }
            });


            SUBMIT_UPDATED_INFO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String ID=ID_NUMBER.getText().toString().trim();


                    if(PROFILE_PIC_CHANGE_BYTE_ARRAY!=null&&SCAN_ID_CHANGE_BYTE_ARRAY!=null&&ID!=null
                            &&SOURCE_DESCRIPTION!=null)
                    {

                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();
                        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.ONLINE_PROFILE_CHANGE_LINK, new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {



                                progressDialog.dismiss();
                                Log.v("Data",response.data+"...");

                                String jsonString = new String(response.data);
                                Log.v("Data",jsonString+"...");

                                try {

                                    JSONObject jsonObject= new JSONObject(jsonString);

                                    Log.v("JsonObject1",jsonObject.getString("response_status"));
                                    Log.v("JsonObject2",jsonObject.getString("msg"));

                                    if(jsonObject.getString("msg").equals("success"))
                                    {
                                        Toasty.success(Documentschange.this,"Changes Saved",Toast.LENGTH_SHORT).show();
                                    }

                                }catch (JSONException e)
                                {

                                }



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                progressDialog.dismiss();
                                Toasty.error(Documentschange.this,"Error Uploading Details",Toast.LENGTH_SHORT).show();

                           }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {



                                Map<String, String> params = new HashMap<>();

                                Log.v("application_no", Vehicles.APPLICATION_NUMBER+"....");
                                params.put("applicant_no",  Vehicles.APPLICATION_NUMBER);
                                params.put("applicant_id_source",SOURCE_DESCRIPTION);
                                params.put("applicant_id_number",ID);

                                return params;
                            }

                            @Override
                            protected Map<String, DataPart> getByteData() {
                                Map<String, DataPart> params = new HashMap<>();
                                // file name could found file base or direct access from real path
                                // for now just get bitmap data from ImageView

                                params.put(ApplicationParams.PICTURE, new DataPart(PROFILE_PIC_CHANGE_URI.getLastPathSegment()+"."+"jpeg",PROFILE_PIC_CHANGE_BYTE_ARRAY, "image/jpeg"));
                                params.put(ApplicationParams.PICTURE1, new DataPart(SCAN_ID_URI.getLastPathSegment()+"."+SCAN_ID_CHANGE_MIME, SCAN_ID_CHANGE_BYTE_ARRAY, "image/jpeg"));
                              //  params.put(ApplicationParams.PICTURE1, new DataPart(SCAN_ID_URI.getLastPathSegment()+"."+"jpeg", SCAN_ID_CHANGE_BYTE_ARRAY, "image/jpeg"));



                                Log.v("MIMEPROFILE",PROFILE_PIC_CHANGE_MIME+"....");

                                return params;
                            }

                        };
                        //Creating a Request Queue
                        RequestQueue requestQueue2 = Volley.newRequestQueue(Documentschange.this);

                        //Adding request to the queue
                        requestQueue2.add(multipartRequest);




                    }


                }
            });








        }
        else if(CheckPassDetails.CHANGEABLE.equals(OPTION_SELECTED.OPTION_CHANGEABLE_VEHICLE))
        {
            Application_Layout.setVisibility(View.INVISIBLE);

            Drivername=(EditText)findViewById(R.id.Driversname_doc_change);
            DriverLicenseNumber=(EditText) findViewById(R.id.DriverLicenseNumber_doc_change);
            VehicleNumber=(EditText) findViewById(R.id.VehcileNumber_doc_change);
            VehicleModel=(EditText) findViewById(R.id.VehicleModel_doc_change);
            DriverLicense = (ImageView) findViewById(R.id.driverlicense_doc_change);
            Insurance = (ImageView) findViewById(R.id.insurance_doc_change);
            RCBook = (ImageView) findViewById(R.id.RCBook_doc_change);
            Submit = (Button) findViewById(R.id.submitVehicleDetails_doc_change);

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

                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();
                        Log.v("Response","Working2");
                        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.ONLINE_VEHICLE_DETAILS_CHANGE_LINK, new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {

                                progressDialog.dismiss();
                                String RESPONSE= new String(response.data);

                                try {

                                    Log.v("Response",RESPONSE.toString());
                                    JSONObject jsonObject=new JSONObject(RESPONSE);
                                    Log.v("Response2",jsonObject.toString());
                                    Toasty.success(Documentschange.this,"Success",Toast.LENGTH_SHORT).show();


                                }catch (JSONException e)
                                {

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                progressDialog.dismiss();
                                Toasty.error(Documentschange.this,"Error Uploading",Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {



                                Map<String, String> params = new HashMap<>();
                                params.put("driver_name",DRIVERS_NAME);
                                params.put("driver_licence",DRIVER_LICENSE_NUMBER);
                                params.put("vehicle_no",VEHICLE_NUMBER);
                                params.put("vehicle_mode",VEHICLE_MODEL);
                                params.put("application_no",Vehicles.APPLICATION_NUMBER);

                                return params;
                            }

                            @Override
                            protected Map<String, DataPart> getByteData() {
                                Map<String, DataPart> params = new HashMap<>();
                                // file name could found file base or direct access from real path
                                // for now just get bitmap data from ImageView


                                if(DRIVER_LICENSE_BYTE_ARRAY!=null)
                                {
                                    params.put("picture5", new DataPart(DRIVER_LICENSE_URI.getLastPathSegment()+"."+DRIVER_LICENSE_MIME, DRIVER_LICENSE_BYTE_ARRAY, "image/jpeg"));

                                }
                                if(RC_BOOK_BYTE_ARRAY!=null)
                                {
                                    params.put("picture3", new DataPart(RCBook_URI.getLastPathSegment()+"."+RCBOOK_MIME, RC_BOOK_BYTE_ARRAY, "image/jpeg"));

                                }
                                if(INSURANCE_BYTE_ARRAY!=null)
                                {
                                    params.put("picture4", new DataPart(INSURANCE_URI.getLastPathSegment()+"."+INSURANCE_MIME, INSURANCE_BYTE_ARRAY, "image/jpeg"));

                                }

                                return params;
                            }

                        };
                        //Creating a Request Queue
                        RequestQueue requestQueue2 = Volley.newRequestQueue(Documentschange.this);
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

    }
    private Boolean IsCorrect()
    {
        final String DRIVERS_NAME=Drivername.getText().toString().trim();
        final String DRIVER_LICENSE_NUMBER=DriverLicenseNumber.getText().toString().trim();
        final String VEHICLE_NUMBER=VehicleNumber.getText().toString().trim();
        final String VEHICLE_MODEL=VehicleModel.getText().toString().trim();

        if((TextUtils.isEmpty(DRIVERS_NAME)||TextUtils.isEmpty(DRIVER_LICENSE_NUMBER)||TextUtils.isEmpty(VEHICLE_NUMBER)
                ||TextUtils.isEmpty(VEHICLE_MODEL))||DRIVER_LICENSE_BYTE_ARRAY==null||RC_BOOK_BYTE_ARRAY==null||INSURANCE_BYTE_ARRAY==null)
        {

            return false;
        }

        return true;
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==DRIVER_LICENSE_REQUEST_CODE)
        {
            DRIVER_LICENSE_URI=data.getData();

            DRIVER_LICENSE_MIME= GetMimeType.GetMimeType(Documentschange.this,DRIVER_LICENSE_URI);

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

            RCBOOK_MIME=GetMimeType.GetMimeType(Documentschange.this,DRIVER_LICENSE_URI);

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

            INSURANCE_MIME=GetMimeType.GetMimeType(Documentschange.this,INSURANCE_URI);

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
       else if(resultCode==RESULT_OK&&requestCode==PROFILE_PIC_CHANGE_REQUESTCODE)
        {
            PROFILE_PIC_CHANGE_URI= data.getData();

            Bitmap photo = (Bitmap) data.getExtras().get("data");


            int p[]=getResolution(photo.getWidth(),photo.getHeight());
            photo=Bitmap.createScaledBitmap(photo,p[0],p[1],true);




            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            PROFILE_PIC_CHANGE_BYTE_ARRAY = stream.toByteArray();


            PROFILE_PIC_CHANGE_URI=  ApplicationForm.getImageUri(getApplicationContext(), photo);


            Glide.with(Documentschange.this)
                    .load(PROFILE_PIC_CHANGE_BYTE_ARRAY)
                    .into(PROFILE_PIC);


        }
        else  if (resultCode==RESULT_OK && requestCode==SCAN_ID_CHANGE_REQUESTCODE)
        {
            SCAN_ID_URI=data.getData();

            SCAN_ID_CHANGE_MIME=GetMimeType.GetMimeType(Documentschange.this,SCAN_ID_URI);

            SCAN_ID_CHANGE_MIME=GetMimeType.ReturnCorrectMime(SCAN_ID_CHANGE_MIME);

            Log.v("MIMESCANID",SCAN_ID_CHANGE_MIME+"....");


            try {
                //Getting the Bitmap from Gallery
                SCAN_ID_CHANGE_FILE=new File(getPath(SCAN_ID_URI));

                SCAN_ID_CHANGE_BYTE_ARRAY = new byte[(int) SCAN_ID_CHANGE_FILE.length()];
                FileInputStream fileInputStream = new FileInputStream(SCAN_ID_CHANGE_FILE);
                fileInputStream.read(SCAN_ID_CHANGE_BYTE_ARRAY);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), SCAN_ID_URI);
                int p[]=getResolution(bitmap.getWidth(),bitmap.getHeight());
                Bitmap scaled=Bitmap.createScaledBitmap(bitmap,p[0],p[1],true);



                SCAN_ID.setImageBitmap(scaled);


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
