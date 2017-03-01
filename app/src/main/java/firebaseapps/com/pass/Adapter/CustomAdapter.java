package firebaseapps.com.pass.Adapter;

/**
 * Created by 1405214 on 12-12-2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static firebaseapps.com.pass.R.*;
import static firebaseapps.com.pass.R.color.HintColor;

public class CustomAdapter extends BaseAdapter {
    Context context;
   // int flags[];
    ArrayList<String> countryNames;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<String> countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(layout.test, null);
        TextView names = (TextView) view.findViewById(id.textView);
        ImageView Image =(ImageView) view.findViewById(id.IMAGE_VIEW_SELECT);
        if(i==0)
        {
            names.setText("Tap to select");
            names.setTextColor(Color.GRAY);
        }
        else
        {
            Image.setVisibility(View.INVISIBLE);
            names.setText(countryNames.get(i));
        }
        return view;
    }
}