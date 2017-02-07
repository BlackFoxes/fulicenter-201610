package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by clawpo on 2017/1/16.
 */
public class Login_OActivity extends AppCompatActivity {
    private static final String TAG = Login_OActivity.class.getSimpleName();
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;

    IModelUser model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.backClickArea, R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.btn_login:
                checkInput();
                break;
            case R.id.btn_register:
//                MFGT.gotoRegister(this);
                break;
        }
    }

    private void checkInput() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            mUsername.setError(getString(R.string.user_name_connot_be_empty));
            mUsername.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            mPassword.setError(getString(R.string.password_connot_be_empty));
            mPassword.requestFocus();
        }else{
            login(username,password);
        }
    }

    private void login(String username, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.logining));
        model = new ModelUser();
        model.login(this, username, password, new OnCompleteListener<String>() {
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
                                SharePrefrenceUtils.getInstence(Login_OActivity.this).saveUser(user.getMuserName());
                                FuLiCenterApplication.setUser(user);
                                setResult(RESULT_OK);
                                MFGT.finish(Login_OActivity.this);
                            }
                        }else{
                            if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER){
                                CommonUtils.showLongToast(getString(R.string.login_fail_unknow_user));
                            }
                            if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                                CommonUtils.showLongToast(getString(R.string.login_fail_error_password));
                            }
                        }
                    }else{
                        CommonUtils.showLongToast(getString(R.string.login_fail));
                    }
                }else{
                    CommonUtils.showLongToast(getString(R.string.login_fail));
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                L.e(TAG,"error="+error);
                CommonUtils.showLongToast(error);
            }
        });
    }
}
