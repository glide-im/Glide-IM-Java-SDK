package pro.glideim.sdk;

import pro.glideim.sdk.api.user.UserInfoBean;

import java.util.List;

class DefaultDataStoreImpl implements DataStorage{

    private String token = "";

    @Override
    public void storeToken(String token) {
        this.token = token;
    }

    @Override
    public void storeTempUserInfo(UserInfoBean userInfoBean) {

    }

    @Override
    public String loadToken() {
        return token;
    }

    @Override
    public List<UserInfoBean> loadTempUserInfo() {
        return null;
    }
}
