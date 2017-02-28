package firebaseapps.com.pass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import firebaseapps.com.pass.Constants.Constants;
import mohitbadwal.rxconnect.RxConnect;

public class View_Pass extends AppCompatActivity {


    private RxConnect rxConnect;
    private EditText pass_no;
    private Button view;
    public static String PASS_DETAILS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__pass);

        pass_no=(EditText)findViewById(R.id.view_pass_passno_);
        view=(Button)findViewById(R.id.view_pass);
        rxConnect=new RxConnect(View_Pass.this);
        rxConnect.setCachingEnabled(false);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                final String PASS_NO=pass_no.getText().toString().trim();
                if(PASS_NO.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter pass number",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    rxConnect.setParam("Passnumber",PASS_NO);
                    rxConnect.execute(Constants.PASS_RETREIVE_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                        @Override
                        public void onResult(String result) {

                            try {
                                JSONObject jsonObject=new JSONObject(result);

                                String REGISTERED_MOBILE_NUBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");
                                if(jsonObject.getString("result").equals("")/*&&(jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER))||jsonObject.getString("mobile").equals(REGISTERED_MOBILE_NUBER)*/)
                                {
                                    PASS_DETAILS=result;
                                    Intent VIEW_PASS=new Intent(View_Pass.this,ViewPass.class);
                                    VIEW_PASS.putExtra("PassNumber",PASS_NO);
                                    finish();
                                    startActivity(VIEW_PASS);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"No Such application exists",Toast.LENGTH_SHORT).show();
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

                   /* databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(PASS_NO))
                            {
                                DatabaseReference ValidTheUser;
                                ValidTheUser=databaseReference.child(PASS_NO).child("Uid");

                                Log.v("HereNow","working");

                                ValidTheUser.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Log.v("HereNow","working2");
                                        String User_Id=dataSnapshot.getValue(String.class);

                                        if(User_Id.equals(mAuth.getCurrentUser().getUid()))
                                        {
                                            Log.v("HereNow","working2");
                                            Intent view=new Intent(View_Pass.this,ViewPass.class);
                                            view.putExtra("Pass",PASS_NO);

                                            startActivity(view);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"This pass is not registered in your name",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Pass not found",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }); */
                }

            }
        });

    }
}
