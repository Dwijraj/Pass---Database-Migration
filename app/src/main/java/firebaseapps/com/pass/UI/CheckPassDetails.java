package firebaseapps.com.pass.UI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import firebaseapps.com.pass.Constants.Constants;
import firebaseapps.com.pass.R;
import firebaseapps.com.pass.Utils.JsonParser;
import mohitbadwal.rxconnect.RxConnect;

public class CheckPassDetails extends AppCompatActivity {

    private EditText tras_id;
    private TextView app_status;
    private Button check;
    private ProgressDialog m2Dialog;
    private DatabaseReference mDatabaseref;
    private RxConnect rxConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pass_details);

        rxConnect=new RxConnect(this);
        rxConnect.setCachingEnabled(false);
        mDatabaseref= FirebaseDatabase.getInstance().getReference().child("Applications");  //Reference to applications
        tras_id=(EditText)findViewById(R.id.viewtransactionid);
        app_status=(TextView)findViewById(R.id.viewstatus);
        check=(Button)findViewById(R.id.viewcheck);
        m2Dialog=new ProgressDialog(this);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m2Dialog.setMessage("Checking if application  exists and fetching the status");
                m2Dialog.show();

               final String tr_id=tras_id.getText().toString();


                String REGISTERED_NUMBER=getSharedPreferences(Constants.SHARED_PREFS_NAME,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY,"DEFAULT");

                if(!tr_id.isEmpty()) {
                    rxConnect.setParam("applicant_number", REGISTERED_NUMBER);
                    rxConnect.setParam("token_id", tr_id);
                    rxConnect.execute(Constants.STATUS_DETAIL_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
                        @Override
                        public void onResult(String result) {

                            try {

                                JSONObject jsonObject = new JSONObject(result);
                                String Status = JsonParser.JSONValue(jsonObject, "status");
                                app_status.setText(Status);

                            } catch (JSONException e) {

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
