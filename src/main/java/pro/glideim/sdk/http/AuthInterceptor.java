package pro.glideim.sdk.http;


import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pro.glideim.sdk.GlideIM;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private static final AuthInterceptor instance = new AuthInterceptor();

    public static AuthInterceptor create(){
        return instance;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();

        String jwtToken = GlideIM.getInstance().getDataStorage().loadToken();
        request.addHeader("Authorization", "Bearer "+jwtToken);
        return chain.proceed(request.build());
    }
}
