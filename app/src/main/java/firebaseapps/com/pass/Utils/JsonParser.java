package firebaseapps.com.pass.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 1405214 on 28-02-2017.
 */

public class JsonParser {

    public static String JSONValue(JSONObject jsonObject,String KEY) throws JSONException {
        String Value;
        Value=jsonObject.getString(KEY);
        return Value;
    }
    public static JSONArray GetJsonArray(JSONObject jsonObject,String KEY) throws JSONException {

        JSONArray jsonArray= jsonObject.getJSONArray(KEY);

        return  jsonArray;
    }
    public static HashMap<String,String> PricNPlace(JSONArray jsonArray) throws JSONException {
        HashMap<String,String> List=new HashMap<>();

        for(int i=0;i< jsonArray.length();i++)
        {
          JSONObject jsonObject=(JSONObject) jsonArray.getJSONObject(i);
          String Key=JsonParser.JSONValue(jsonObject,"place_name");
          String Value=JsonParser.JSONValue(jsonObject,"price_detail");
          List.put(Key,Value);
        }
        return  List;
    }
}
