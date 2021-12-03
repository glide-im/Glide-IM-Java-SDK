package pro.glideim.sdk.api.contacts;

import io.reactivex.Observable;
import pro.glideim.sdk.api.Response;
import pro.glideim.sdk.http.RetrofitManager;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface ContactsApi {

    ContactsApi API = RetrofitManager.create(ContactsApi.class);

    @GET("/contacts/list")
    Observable<Response<Object>> register(@Body Object d);
}
