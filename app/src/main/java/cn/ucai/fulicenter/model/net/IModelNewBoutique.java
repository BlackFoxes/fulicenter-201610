package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;

/**
 * Created by clawpo on 2017/1/11.
 */

public interface IModelNewBoutique {
    void downData(Context context, OnCompleteListener<BoutiqueBean[]> listener);
}
