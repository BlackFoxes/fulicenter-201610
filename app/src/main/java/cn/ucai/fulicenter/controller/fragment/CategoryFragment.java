package cn.ucai.fulicenter.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.adapter.CategoryAdapter;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.IModelNewCategory;
import cn.ucai.fulicenter.model.net.ModelCategory;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;

/**
 * Created by clawpo on 2017/1/11.
 */

public class CategoryFragment extends Fragment {
    private static final String TAG = CategoryFragment.class.getSimpleName();
    @BindView(R.id.elv_category)
    ExpandableListView mElvCategory;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;

    IModelNewCategory model;
    CategoryAdapter mAdapter;

    ArrayList<CategoryGroupBean> mGroupBeen = new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> mChildBeen = new ArrayList<>();

    int groupCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        mAdapter = new CategoryAdapter(getContext(),mGroupBeen,mChildBeen);
        mElvCategory.setAdapter(mAdapter);
        initView(false);
        initData();
        return layout;
    }

    private void initData() {
        model = new ModelCategory();
        model.downData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result!=null){
                    initView(true);
                    ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    mGroupBeen.addAll(list);
                    for (int i=0;i<list.size();i++){
                        mChildBeen.add(new ArrayList<CategoryChildBean>());
                        downloadChildData(list.get(i).getId(),i);
                    }
                }else{
                    initView(false);
                }
            }

            @Override
            public void onError(String error) {
                initView(false);
                L.e(TAG,"error="+error);
            }
        });
    }

    private void downloadChildData(int id,final int index) {
        model.downData(getContext(), id, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                if(result!=null){
                    ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                    mChildBeen.set(index,list);
                }
                if (groupCount==mGroupBeen.size()){
                    mAdapter.initData(mGroupBeen,mChildBeen);
                }
            }

            @Override
            public void onError(String error) {
                groupCount++;
                L.e(TAG,"error="+error);
            }
        });
    }

    private void initView(boolean hasData) {
        mTvNomore.setVisibility(hasData?View.GONE:View.VISIBLE);
        mElvCategory.setVisibility(hasData?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.tv_nomore) void onClick(){
        initData();
    }

}
