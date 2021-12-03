package pro.glideim.sdk.ws;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class WsClientTest {

    @Test
    void connect() {
        WsClient c = new WsClient();
        try {
            c.connect("ws://localhost:8080/ws");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        c.send();
    }
}