package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.FooterViewHolder;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by clawpo on 2017/1/11.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;
    boolean isMore;

    public GoodsAdapter(Context context, ArrayList<NewGoodsBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.setFooterString(mContext.getString(getFooterString()));
        }else {
            GoodsViewHolder vh = (GoodsViewHolder) holder;
            ImageLoader.downloadImg(mContext, vh.mIvGoodsThumb, mList.get(position).getGoodsThumb());
//        vh.mIvGoodsThumb.setImageResource(mList.get(position).getGoodsThumb());
            vh.mTvGoodsName.setText(mList.get(position).getGoodsName());
            vh.mTvGoodsPrice.setText(mList.get(position).getCurrencyPrice());
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoGoodsDetail(mContext,mList.get(position).getGoodsId());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<NewGoodsBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    public void sortGoods(final int sortBy){
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean leftBean, NewGoodsBean rightBean) {
                int result=0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (leftBean.getAddTime()-rightBean.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (rightBean.getAddTime()-leftBean.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(leftBean.getCurrencyPrice())-getPrice(rightBean.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(rightBean.getCurrencyPrice())-getPrice(leftBean.getCurrencyPrice());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }
    //currencyPrice":"￥88"
    int getPrice(String price) {
        int p = 0;
        p = Integer.valueOf(price.substring(price.indexOf("￥")+1));
        L.e("adapter","p="+p);
        return p;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView mIvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView mTvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView mTvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout mLayoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
