package cn.ucai.fulicenter.mvp.login;

import android.content.Context;
import android.text.TextUtils;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;

import static com.baidu.wallet.core.plugins.pluginupgrade.PluginUpgradeUtils.TAG;

/**
 * Created by clawpo on 2017/2/7.
 */

public class LoginPresenter implements LoginContract.Presenter {
    IModelUser model;
    LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        model = new ModelUser();
    }

    @Override
    public void login(final Context context, String username, String password) {
        if(TextUtils.isEmpty(username)){
            mView.loginFailByUserName();
        }else if(TextUtils.isEmpty(password)){
            mView.loginFailByPassword();
        }else{
            mView.showDialog();
            model.login(context, username, password, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (s!=null){
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        L.e(TAG,"result="+result);
                        if(result!=null){
                            if (result.isRetMsg()){
                                User user = (User) result.getRetData();
                                boolean savaUser = UserDao.getInstance().savaUser(user);
                                L.e(TAG,"savaUser="+savaUser);
                                if (savaUser) {
                                    SharePrefrenceUtils.getInstence(context).saveUser(user.getMuserName());
                                    FuLiCenterApplication.setUser(user);
                                    mView.loginSuccess();
                                }
                            }else{
                                if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER){
                                    mView.showError(R.string.login_fail_unknow_user);
                                }
                                if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                                    mView.showError(R.string.login_fail_error_password);
                                }
                            }
                        }else{
                            mView.showError(R.string.login_fail);
                        }
                    }else{
                        mView.showError(R.string.login_fail);
                    }
                    mView.dismissDialog();
                }

                @Override
                public void onError(String error) {
                    mView.dismissDialog();
                    L.e(TAG,"error="+error);
                    mView.showError(error);
                }
            });
        }
    }

    @Override
    public void start() {

    }
}
