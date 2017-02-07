package cn.ucai.fulicenter.mvp.login;

import android.content.Context;

import cn.ucai.fulicenter.mvp.BasePresenter;
import cn.ucai.fulicenter.mvp.BaseView;

/**
 * Created by clawpo on 2017/2/6.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter>{
        void loginSuccess();
        void showError(String error);
        void showError(int error);
        void loginFailByUserName();
        void loginFailByPassword();
        void showDialog();
        void dismissDialog();
    }

    interface Presenter extends BasePresenter{
        void login(Context context, String username, String password);
    }
}
