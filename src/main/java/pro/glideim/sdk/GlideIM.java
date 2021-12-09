package pro.glideim.sdk;

import pro.glideim.sdk.http.RetrofitManager;

public class GlideIM {

    private IMClient conn = new IMClient();

    private static class Holder {
        static GlideIM INSTANCE = new GlideIM();
    }

    public static void init(String urlWs, String urlApi) {
        RetrofitManager.init(urlApi);
        Holder.INSTANCE.conn.connect(urlWs);
    }
}
