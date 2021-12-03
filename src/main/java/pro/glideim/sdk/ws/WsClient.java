package pro.glideim.sdk.ws;

import org.asynchttpclient.*;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import java.util.concurrent.ExecutionException;

public class WsClient implements WebSocketListener {

    private WebSocket ws;

    public void connect(String url) throws ExecutionException, InterruptedException {
        AsyncHttpClient client = Dsl.asyncHttpClient();

        ListenableFuture<Response> responseListenableFuture = client.executeRequest(Dsl.get("").build());

        BoundRequestBuilder builder = client.prepareGet(url);
        builder.execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(this).build()).get();
    }

    public void send() {
        this.ws.sendTextFrame("{}");
    }

    @Override
    public void onTextFrame(String payload, boolean finalFragment, int rsv) {
        System.out.println(payload);
    }

    @Override
    public void onOpen(WebSocket websocket) {
        this.ws = websocket;
    }

    @Override
    public void onClose(WebSocket websocket, int code, String reason) {

    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
