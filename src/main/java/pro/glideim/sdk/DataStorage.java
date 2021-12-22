package pro.glideim.sdk;

import pro.glideim.sdk.api.user.UserInfoBean;

import java.util.List;

public interface DataStorage {
    void storeToken(String token);

    void storeTempUserInfo(UserInfoBean userInfoBean);

    String loadToken();

    List<UserInfoBean> loadTempUserInfo();
}
