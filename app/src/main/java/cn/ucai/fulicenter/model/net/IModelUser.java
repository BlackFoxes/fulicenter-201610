package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by clawpo on 2017/1/11.
 */

public interface IModelUser {
    void login(Context context, String username,String password, OnCompleteListener<String> listener);
    void register(Context context, String username, String usernick, String password, OnCompleteListener<String> listener);
    void updatNick(Context context,String username,String usernick,OnCompleteListener<String> listener);
    void uploadAvatar(Context context,String username,File file,OnCompleteListener<String> listener);
    void collecCount(Context context,String username, OnCompleteListener<MessageBean> listener);
    void getCollects(Context context,String username,int pageId,int pageSize,OnCompleteListener<CollectBean[]> listener);
}
