package pro.glideim.sdk;

import android.content.Context;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.glideim.sdk.api.user.LoginDto;
import pro.glideim.sdk.api.user.TokenBean;
import pro.glideim.sdk.http.RetrofitManager;
import pro.glideim.sdk.protocol.AckMessage;
import pro.glideim.sdk.protocol.ChatMessage;
import pro.glideim.sdk.protocol.CommMessage;

class IMClientTest {

    IMClient imClient = new IMClient();

    @BeforeEach
    void setUp() throws InterruptedException {
        RetrofitManager.init(new Context(), "http://localhost/api/");
        imClient.connect("ws://localhost:8080/ws");
        Thread.sleep(1000);
    }

    @AfterEach
    void down() {
        imClient.disconnect();
    }

    @Test
    void connect() throws InterruptedException {
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
    }

    @Test
    void sendChatMessage() throws InterruptedException {
        ChatMessage c = new ChatMessage();
        c.setTo(543619);
        c.setcSeq(1);
        c.setContent("hello");
        c.setType(1);
        c.setMid(12343);
        c.setcTime(System.currentTimeMillis());
        Observable<AckMessage> o = imClient.sendChatMessage(c);
        o.subscribe(new TestObserver<>());
        Thread.sleep(4000);
    }

    @Test
    void send() {

    }
}