package firebaseapps.com.pass.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import firebaseapps.com.pass.Constants.OPTION_SELECTED;
import firebaseapps.com.pass.R;

public class Documentschange extends AppCompatActivity {

    private LinearLayout Vehicle_Layout;
    private LinearLayout Application_Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentschange);

        Vehicle_Layout=(LinearLayout) findViewById(R.id.VEHICLE_CHANGE_LAYOUT_1996);
        Application_Layout=(LinearLayout) findViewById(R.id.APPLICATION_CHANGE_LAYOUT_1996);



        if(CheckPassDetails.CHANGEABLE.equals(OPTION_SELECTED.OPTION_CHANGEABLE_VEHICLE))
        {
            Application_Layout.setVisibility(View.INVISIBLE);

        }
        else
        {
             Vehicle_Layout.setVisibility(View.INVISIBLE);
        }

    }
}
