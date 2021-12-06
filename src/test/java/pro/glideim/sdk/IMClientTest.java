package pro.glideim.sdk;

import android.content.Context;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.glideim.sdk.api.user.LoginDto;
import pro.glideim.sdk.api.user.TokenBean;
import pro.glideim.sdk.http.RetrofitManager;
import pro.glideim.sdk.protocol.CommMessage;

class IMClientTest {

    @BeforeEach
    void setUp() {
        RetrofitManager.init(new Context(), "http://localhost/api/");
    }

    @Test
    void connect() throws InterruptedException {
        IMClient imClient = new IMClient();
        imClient.connect("ws://localhost:8080/ws");

        Thread.sleep(1000);

        LoginDto d = new LoginDto("abc", "abc", 1);
        Observable<CommMessage<TokenBean>> ob = imClient.request("api.user.login", TokenBean.class, false, d);
        ob.observeOn(Schedulers.single())
                .subscribe(new Observer<CommMessage<TokenBean>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        System.out.println("IMClientTest.onSubscribe");
                    }

                    @Override
                    public void onNext(@NotNull CommMessage<TokenBean> tokenBean) {
                        System.out.println("IMClientTest.onNext\t" + tokenBean);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        System.out.println("IMClientTest.onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("IMClientTest.onComplete");
                    }
                });

        Thread.sleep(5000);
        imClient.disconnect();
    }

    @Test
    void send() {

    }
}