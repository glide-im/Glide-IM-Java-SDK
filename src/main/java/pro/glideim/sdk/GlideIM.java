package pro.glideim.sdk;

import android.content.Context;
import pro.glideim.sdk.http.RetrofitManager;

public class GlideIM {

    private IMClient conn = new IMClient();

    private static class Holder {
        static GlideIM INSTANCE = new GlideIM();
    }

    public static void init(Context context, String urlWs, String urlApi) {
        RetrofitManager.init(context, urlApi);
        Holder.INSTANCE.conn.connect(urlWs);
    }
}
