package firebaseapps.com.pass.Payment;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import firebaseapps.com.pass.Constants.PaymentURL;
import firebaseapps.com.pass.Utils.JsonParser;
import mohitbadwal.rxconnect.RxConnect;

/**
 * Created by 1405214 on 05-04-2017.
 */

public class ClientToken {

    private static RxConnect rxConnect;
    public static  int CLIENT_TOKEN_REQUEST_CODE=1901;
    private static String CLIENT_TOKEN=null;
    public static String GetClientToken(Activity activity)
    {



        rxConnect=new RxConnect(activity);
        rxConnect.execute(PaymentURL.CLIENT_TOKEN_URL, RxConnect.POST, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {


                Log.v("PaymentResposne",result+"....");
                try {

                    JSONObject jsonObject=new JSONObject(result);

                    CLIENT_TOKEN= JsonParser.JSONValue(jsonObject,"client_token");


                }catch (JSONException json)
                {

                }


            }

            @Override
            public void onNoResult() {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });



        return CLIENT_TOKEN;
    }

}
