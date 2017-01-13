package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by clawpo on 2017/1/13.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<CategoryGroupBean> mGroupBeen;
    ArrayList<ArrayList<CategoryChildBean>> mChildBeen;

    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupBeen,
                           ArrayList<ArrayList<CategoryChildBean>> childBeen) {
        mContext = context;
        mGroupBeen = new ArrayList<>();
        mGroupBeen.addAll(groupBeen);
        mChildBeen = new ArrayList<>();
        mChildBeen.addAll(childBeen);
    }

    @Override
    public int getGroupCount() {
        return mGroupBeen != null ? mGroupBeen.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildBeen != null && mChildBeen.get(groupPosition) != null ?
                mChildBeen.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupBeen.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        if (mChildBeen != null && mChildBeen.get(groupPosition) != null) {
            return mChildBeen.get(groupPosition).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpand, View view, ViewGroup viewGroup) {
        GroupViewHolder vh = null;
        if(view==null){
            view = View.inflate(mContext,R.layout.item_category_group,null);
            vh = new GroupViewHolder(view);
            view.setTag(vh);
        }else{
            vh = (GroupViewHolder) view.getTag();
        }
        ImageLoader.downloadImg(mContext,vh.mIvGroupThumb,mGroupBeen.get(groupPosition).getImageUrl());
        vh.mTvGroupName.setText(mGroupBeen.get(groupPosition).getName());
        vh.mIvIndicator.setImageResource(isExpand?R.mipmap.expand_off:R.mipmap.expand_on);
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isExpand, View view, ViewGroup viewGroup) {
        ChildViewHolder vh = null;
        if (view==null){
            view = View.inflate(mContext,R.layout.item_category_child,null);
            vh = new ChildViewHolder(view);
            view.setTag(vh);
        }else{
            vh = (ChildViewHolder) view.getTag();
        }
        ImageLoader.downloadImg(mContext,vh.mIvCategoryChildThumb,getChild(groupPosition,childPosition).getImageUrl());
        vh.mTvCategoryChildName.setText(getChild(groupPosition,childPosition).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoCategoryChild(mContext,mChildBeen.get(groupPosition).get(childPosition).getId());
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupBeen,
                         ArrayList<ArrayList<CategoryChildBean>> childBeen) {
        mGroupBeen.clear();
        mGroupBeen.addAll(groupBeen);
        mChildBeen.clear();
        mChildBeen.addAll(childBeen);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView mIvGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView mTvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView mIvIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_category_child_thumb)
        ImageView mIvCategoryChildThumb;
        @BindView(R.id.tv_category_child_name)
        TextView mTvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout mLayoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
