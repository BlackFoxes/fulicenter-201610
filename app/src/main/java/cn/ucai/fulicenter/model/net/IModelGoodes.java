package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;

/**
 * Created by clawpo on 2017/1/11.
 */

public interface IModelGoodes {
    void downData(Context context,int goodsId, OnCompleteListener<GoodsDetailsBean> listener);
}
