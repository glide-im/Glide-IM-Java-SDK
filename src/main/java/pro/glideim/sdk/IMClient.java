package pro.glideim.sdk;

import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.glideim.sdk.http.RetrofitManager;
import pro.glideim.sdk.protocol.ChatMessage;
import pro.glideim.sdk.protocol.CommMessage;
import pro.glideim.sdk.ws.WsClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class IMClient {

    private static final int MESSAGE_VER = 1;

    private final Logger logger;
    private final WsClient wsClient;

    private boolean open;
    private long seq;
    private long uid;

    private final Map<Long, Emitter> requests = new HashMap<>();

    private final Type msgType = new TypeToken<CommMessage<Object>>() {
    }.getType();

    @SuppressWarnings("rawtypes")
    private static class Emitter {
        ObservableEmitter e;
        Type type;

        public Emitter(ObservableEmitter emitter, Type t) {
            this.e = emitter;
            this.type = t;
        }
    }

    public IMClient() {
        wsClient = new WsClient();
        logger = new Logger() {
            @Override
            public void d(String tag, String log) {
                System.out.println(tag + "\t" + log);
            }

            @Override
            public void e(String tag, Throwable t) {
                System.err.println(tag + "\t" + t.getMessage());
                t.printStackTrace();
            }
        };
    }

    public void connect(String url) {
        wsClient.setListener(webSocketListener);
        try {
            wsClient.connect(url);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        wsClient.disconnect();
    }

    public void sendMessage(long to, int type, ChatMessage message, SendMessageListener l) {

    }

    public <T> Observable<CommMessage<T>> request(String action, Class<T> clazz, boolean isArray, Object data) {
        if (!open) {
            return Observable.error(new Exception("the server is not connected"));
        }
        CommMessage<Object> m = new CommMessage<>(MESSAGE_VER, action, seq++, data);

        Type t;
        if (isArray) {
            t = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
            t = new ParameterizedTypeImpl(CommMessage.class, new Type[]{t});
        } else {
            t = new ParameterizedTypeImpl(CommMessage.class, new Class[]{clazz});
        }
        final Type finalT = t;
        return ObservableSubscribeOn.create(emitter -> {
            boolean success = wsClient.sendMessage(m);
            if (!success) {
                emitter.onError(new Exception("message send failed"));
            } else {
                requests.put(m.getSeq(), new Emitter(emitter, finalT));
            }
        });
    }

    private void onMessage(String t) {
        CommMessage<Object> m = RetrofitManager.fromJson(msgType, t);
        if (requests.containsKey(m.getSeq())) {
            onRequestMessage(m.getSeq(), t, m);
            return;
        }

        logger.d("IMClient.onMessage:", m.toString());
    }

    private void onRequestMessage(long seq, String t, CommMessage<Object> m) {
        Emitter emitter = requests.get(seq);
        if (m.getAction().equals("api.success")) {
            try {
                CommMessage<Object> o = RetrofitManager.fromJson(emitter.type, t);
                //noinspection unchecked
                emitter.e.onNext(o);
            } catch (Throwable e) {
                emitter.e.onError(new Exception("json parse error, " + e.getMessage()));
            }
        } else {
            emitter.e.onError(new Exception(m.getData().toString()));
        }
        emitter.e.onComplete();
    }

    public interface SendMessageListener {
        void complete(boolean success, String message);
    }

    private final WebSocketListener webSocketListener = new WebSocketListener() {

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            IMClient.this.onMessage(text);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            open = false;
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            open = true;
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            open = false;
        }
    };
}
