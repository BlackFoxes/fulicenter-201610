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
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by clawpo on 2017/1/16.
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.nick)
    EditText mNick;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;

    IModelUser model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.backClickArea, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.btn_register:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String username = mUsername.getText().toString().trim();
        String usernick = mNick.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirm = mConfirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            mUsername.setError(getResources().getString(R.string.user_name_connot_be_empty));
            mUsername.requestFocus();
        }else if(!username.matches("[a-zA-z]\\w{5,15}")){
            mUsername.setError(getResources().getString(R.string.illegal_user_name));
            mUsername.requestFocus();
        }else if(TextUtils.isEmpty(usernick)){
            mNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            mNick.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            mPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            mPassword.requestFocus();
        }else if(TextUtils.isEmpty(confirm)){
            mConfirmPassword.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            mConfirmPassword.requestFocus();
        }else if(!password.equals(confirm)){
            mConfirmPassword.setError(getResources().getString(R.string.two_input_password));
            mConfirmPassword.requestFocus();
        }else{
            register(username,usernick,password);
        }
    }

    private void register(String username, String usernick, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.registering));
        model = new ModelUser();
        model.register(this, username, usernick, password, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s!=null){
                    Result result = ResultUtils.getResultFromJson(s, Result.class);
                    L.e(TAG,"result="+result);
                    if(result!=null){
                        if (result.isRetMsg()){
                            //register success
                            CommonUtils.showLongToast(R.string.register_success);
                            MFGT.finish(RegisterActivity.this);
                        }else{
                            //register fail,username 102
                            CommonUtils.showLongToast(R.string.register_fail_exists);
                        }
                    }else{
                        CommonUtils.showLongToast(R.string.register_fail);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showLongToast(error);
            }
        });
    }
}
