package cn.ucai.fulicenter.mvp.login;

import cn.ucai.fulicenter.mvp.BasePresenter;
import cn.ucai.fulicenter.mvp.BaseView;

/**
 * Created by clawpo on 2017/2/6.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter>{
        void loginSuccess();
        void showError(String error);
        void loginFailByUserName();
        void loginFailByPassword();
        void showDialog();
        void stopDialog();
    }

    interface Presenter extends BasePresenter{
        void login(String username,String password);
    }
}
