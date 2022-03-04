package pro.glideim.sdk.api.ws;

import org.junit.jupiter.api.Test;

import io.reactivex.Single;
import pro.glideim.sdk.ws.NettyWsClient;

class NettyTest {

    @Test
    void connect2() throws InterruptedException {
        Single<Boolean> connect = new NettyWsClient("ws://localhost:8080/ws").connect();
        connect.blockingGet();
        Thread.sleep(3000);
    }
}