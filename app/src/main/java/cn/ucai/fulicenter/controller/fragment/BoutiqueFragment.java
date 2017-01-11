package cn.ucai.fulicenter.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelNewBoutique;
import cn.ucai.fulicenter.model.net.ModelBoutique;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

import static cn.ucai.fulicenter.R.id.rv;
import static cn.ucai.fulicenter.R.id.srl;
import static cn.ucai.fulicenter.R.id.tv_refresh;

/**
 * Created by clawpo on 2017/1/11.
 */

public class BoutiqueFragment extends Fragment {
    private static final String TAG = BoutiqueFragment.class.getSimpleName();

    @BindView(tv_refresh)
    TextView mTvRefresh;
    @BindView(rv)
    RecyclerView mRv;
    @BindView(srl)
    SwipeRefreshLayout mSrl;

    LinearLayoutManager gm;
    BoutiqueAdapter mAdapter;
    ArrayList<NewGoodsBean> mList = new ArrayList<>();
    IModelNewBoutique model;
    int pageId = 1;

    private void initData(final int action) {
        model.downData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                mSrl.setRefreshing(false);
                mTvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if(result!=null && result.length>0){
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    }else{
                        mAdapter.addData(list);
                    }
                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else{
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                mSrl.setRefreshing(false);
                mTvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e(TAG,"error="+error);
            }
        });
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        gm = new LinearLayoutManager(getContext());
        mRv.addItemDecoration(new SpaceItemDecoration(12));
        mRv.setLayoutManager(gm);
        mRv.setHasFixedSize(true);
        mAdapter = new BoutiqueAdapter(getContext(), null);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newgoods, container, false);
        ButterKnife.bind(this, layout);
        initView();
        model = new ModelBoutique();
        initData(I.ACTION_DOWNLOAD);
        setPullUpListener();
        setPullDownListener();
        return layout;
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mTvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }
    private void setPullUpListener() {
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if(newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount()-1
                        && mAdapter.isMore()){
                    pageId++;
                    initData(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = gm.findFirstVisibleItemPosition();
                mSrl.setEnabled(firstPosition==0);
            }
        });
    }
}
