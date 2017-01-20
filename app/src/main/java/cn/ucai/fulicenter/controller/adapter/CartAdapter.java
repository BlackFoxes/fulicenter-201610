package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by clawpo on 2017/1/20.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;

    public CartAdapter(Context context, ArrayList<CartBean> mlist) {
        mContext = context;
        this.mList = new ArrayList<>();
        this.mList.addAll(mlist);
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CartViewHolder vh = new CartViewHolder(View.inflate(mContext, R.layout.item_cart, null));
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder vh = (CartViewHolder) holder;
        vh.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void initData(ArrayList<CartBean> list) {
        if (mList!=null){
            mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<CartBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cart_thumb)
        ImageView mIvCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView mTvCartGoodName;
        @BindView(R.id.tv_cart_count)
        TextView mTvCartCount;
        @BindView(R.id.tv_cart_price)
        TextView mTvCartPrice;
        @BindView(R.id.cb_cart_selected)
        CheckBox mSelected;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            GoodsDetailsBean detailsBean = mList.get(position).getGoods();
            if (detailsBean!=null) {
                ImageLoader.downloadImg(mContext, mIvCartThumb, detailsBean.getGoodsThumb());
                mTvCartGoodName.setText(detailsBean.getGoodsName());
                mTvCartPrice.setText(detailsBean.getCurrencyPrice());
            }
            mTvCartCount.setText("("+mList.get(position).getCount()+")");
            mSelected.setChecked(false);
        }
    }
}
