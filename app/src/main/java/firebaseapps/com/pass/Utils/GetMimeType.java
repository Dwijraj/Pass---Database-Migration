package firebaseapps.com.pass.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by 1405214 on 19-02-2017.
 */


public class GetMimeType {

    public static String GetMimeType(Context context, Uri uriImage)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[] { MediaStore.MediaColumns.MIME_TYPE },
                null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
    }
}
