package pro.glideim.sdk.entity;

import io.reactivex.annotations.NonNull;

public interface MessageChangeListener {
    void onChange(long mid, @NonNull IMMessage message);

    void onInsertMessage(long mid, @NonNull IMMessage message);

    void onNewMessage(@NonNull IMMessage message);
}
