package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

/**
 * Created by clawpo on 2017/1/11.
 */

public interface IModelNewCategory {
    void downData(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
    void downData(Context context,int parentId, OnCompleteListener<CategoryChildBean[]> listener);
}
