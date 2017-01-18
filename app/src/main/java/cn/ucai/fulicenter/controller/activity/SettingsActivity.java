package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.MFGT;

import static cn.ucai.fulicenter.application.FuLiCenterApplication.getUser;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.iv_user_profile_avatar)
    ImageView mIvUserProfileAvatar;
    @BindView(R.id.tv_user_profile_name)
    TextView mTvUserProfileName;
    @BindView(R.id.tv_user_profile_nick)
    TextView mTvUserProfileNick;

    OnSetAvatarListener mOnSetAvatarListener;
    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this,"设置");
        initData();
    }

    private void initData() {
        User user = getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            MFGT.gotoLogin(this);
        }
    }

    private void loadUserInfo(User user) {
//        ImageLoader.downloadImg(getContext(),mIvUserAvatar,user.getAvatarPath());
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, mIvUserProfileAvatar);
        mTvUserProfileName.setText(user.getMuserName());
        mTvUserProfileNick.setText(user.getMuserNick());
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        FuLiCenterApplication.setUser(null);
        SharePrefrenceUtils.getInstence(this).removeUser();
        MFGT.gotoLogin(this);
        finish();
    }

    @OnClick(R.id.layout_user_profile_nickname)
    public void updateNick(){
        MFGT.gotoUpDataNick(this);
    }

    @OnClick(R.id.layout_user_profile_username)
    public void onClickUserName(){
        CommonUtils.showLongToast(R.string.username_connot_be_modify);
    }

    @OnClick(R.id.layout_user_profile_avatar)
    public void onClickAvatar(){
        L.e(TAG,"onClickAvatar....");
        mOnSetAvatarListener = new OnSetAvatarListener(this,
                R.id.layout_user_profile_avatar,
                getUser().getMuserName(),
                I.AVATAR_TYPE_USER_PATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e(TAG,"requestCode="+requestCode+",resultCode="+resultCode+",data="+data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        if(requestCode == I.REQUEST_CODE_NICK){
            mTvUserProfileNick.setText(getUser().getMuserNick());
        }else if(requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO){
            uploadAvatar();
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,mIvUserProfileAvatar);
    }

    private void uploadAvatar() {
        User user = FuLiCenterApplication.getUser();
        model = new ModelUser();
        L.e(TAG,"uploadAvatar.....user="+user);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_avatar));
        dialog.show();
        File file = null;
        file = new File(String.valueOf(OnSetAvatarListener.getAvatarFile(this,
                I.AVATAR_TYPE_USER_PATH+"/"+user.getMuserName()+user.getMavatarSuffix())));
        L.e(TAG,"file="+file.getAbsolutePath());
        model.uploadAvatar(this,
                user.getMuserName(),
                file,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        L.e(TAG,"s="+s);
                        int msg = R.string.update_user_avatar_fail;
                        if (s!=null){
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result!=null){
                                if (result.isRetMsg()){
                                    msg = R.string.update_user_avatar_success;
                                }
                            }
                        }
                        CommonUtils.showLongToast(msg);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast(error);
                        dialog.dismiss();
                        L.e(TAG,"error="+error);
                    }
                });
    }
}
