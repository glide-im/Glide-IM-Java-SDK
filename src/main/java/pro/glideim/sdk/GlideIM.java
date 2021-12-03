package pro.glideim.sdk;

import android.content.Context;
import pro.glideim.sdk.http.RetrofitManager;

public class GlideIM {

    public static void init(Context context, String hostHost, String socketPort, String baseUrlApi) {
        RetrofitManager.init(context, baseUrlApi);
    }
}
