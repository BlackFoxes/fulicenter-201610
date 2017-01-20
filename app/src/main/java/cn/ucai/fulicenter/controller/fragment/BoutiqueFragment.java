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
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
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
    IModelNewBoutique model;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;

    private void initData(final int action) {
        model.downData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                mSrl.setRefreshing(false);
                mTvRefresh.setVisibility(View.GONE);
                mSrl.setVisibility(View.VISIBLE);
                mTvNomore.setVisibility(View.GONE);
                if (result != null && result.length > 0) {
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                } else {
                    mSrl.setVisibility(View.GONE);
                    mTvNomore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                mSrl.setRefreshing(false);
                mTvRefresh.setVisibility(View.GONE);
                mSrl.setVisibility(View.GONE);
                mTvNomore.setVisibility(View.VISIBLE);
                CommonUtils.showShortToast(error);
                L.e(TAG, "error=" + error);
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
        mAdapter = new BoutiqueAdapter(getContext(), new ArrayList<BoutiqueBean>());
        mRv.setAdapter(mAdapter);
        mSrl.setVisibility(View.GONE);
        mTvNomore.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newgoods, container, false);
        ButterKnife.bind(this, layout);
        initView();
        model = new ModelBoutique();
        initData(I.ACTION_DOWNLOAD);
        setPullDownListener();
        return layout;
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mTvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    @OnClick(R.id.tv_nomore)
    public void onClick() {
        initData(I.ACTION_DOWNLOAD);
    }
}
