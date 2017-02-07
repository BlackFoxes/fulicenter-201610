package cn.ucai.fulicenter.mvp.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.activity.Login_OActivity;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by clawpo on 2017/2/7.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private static final String TAG = Login_OActivity.class.getSimpleName();
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    LoginContract.Presenter mPresenter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void loginSuccess() {
        setResult(RESULT_OK);
        MFGT.finish(this);
    }

    @Override
    public void showError(String error) {
        CommonUtils.showShortToast(error);
    }

    @Override
    public void showError(int error) {
        CommonUtils.showShortToast(error);
    }

    @Override
    public void loginFailByUserName() {
        mUsername.setError(getString(R.string.user_name_connot_be_empty));
        mUsername.requestFocus();
    }

    @Override
    public void loginFailByPassword() {
        mPassword.setError(getString(R.string.password_connot_be_empty));
        mPassword.requestFocus();
    }

    @Override
    public void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.logining));
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    @OnClick({R.id.backClickArea, R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.btn_login:
                mPresenter.login(this,mUsername.getText().toString(),mPassword.getText().toString());
                break;
            case R.id.btn_register:
                MFGT.gotoRegister(this);
                break;
        }
    }
}
