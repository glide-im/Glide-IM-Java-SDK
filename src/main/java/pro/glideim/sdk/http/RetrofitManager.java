package pro.glideim.sdk.http;

import android.content.Context;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class RetrofitManager {
    private Retrofit retrofit;
    private Gson gson;

    private static RetrofitManager sInstance;

    public static <T> T create(Class<T> c) {
        return sInstance.retrofit.create(c);
    }

    public static String toJson(Object obj) {
        return sInstance.gson.toJson(obj);
    }

    public static <T> T fromJson(Type t, String json) throws JsonSyntaxException {
        return sInstance.gson.fromJson(json, t);
    }

    public static void init(Context context, String baseUrl) {

        RetrofitManager m = new RetrofitManager();
        File cacheDir = context.getExternalCacheDir();
        m.gson = new GsonBuilder()
                .setLenient()
                .setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true);

        if (cacheDir != null) {
            httpClient.cache(new Cache(cacheDir, 1024 * 1024 * 10));
        }
        m.retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(m.gson))
                .baseUrl(baseUrl)
                .build();
        sInstance = m;
    }

}
