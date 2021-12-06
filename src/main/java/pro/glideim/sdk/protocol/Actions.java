package pro.glideim.sdk.protocol;

public class Actions {

    // server down
    public static final String ACTION_ACK_MESSAGE = "ack.message";
    public static final String ACTION_ACK_NOTIFY = "ack.notify";
    // client up
    public static final String ACTION_AKC_REQUEST = "ack.request";
    public static final String ACTION_ACK_GROUP_MSG = "ack.group.msg";

    // message
    public static final String ACTION_MESSAGE_CHAT = "message.chat";
    public static final String ACTION_MESSAGE_GROUP = "message.group";
    public static final String ACTION_MESSAGE_CHAT_RETRY = "message.chat.retry";
    public static final String ACTION_MESSAGE_CHAT_RESEND = "message.chat.resend";
    public static final String ACTION_MESSAGE_FAILED_SEND = "message.failed.send";

    // control
    public static final String ACTION_HEARTBEAT = "heartbeat";
    public static final String ACTION_NOTIFY = "notify";
}
