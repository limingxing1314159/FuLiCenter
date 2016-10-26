package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */
public class CollectsAdapter extends Adapter {
    Context mContext;
    List<CollectBean> mList;
    boolean isMore;
    int soryBy = I.SORT_BY_ADDTIME_DESC;

    public CollectsAdapter(Context Context, List<CollectBean> list) {
        mContext = Context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    public void setSoryBy(int soryBy) {
        this.soryBy = soryBy;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CollectsViewHolder(View.inflate(mContext, R.layout.item_collects, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.tvFooter.setText(getFootString());
        } else {
            CollectsViewHolder vh = (CollectsViewHolder) holder;
            CollectBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext, vh.ivGoodsTh, goods.getGoodsThumb());
            vh.tvGoods.setText(goods.getGoodsName());
            vh.layoutGoods.setTag(goods);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CollectBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public int getFootString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    public void addData(ArrayList<CollectBean> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CollectsViewHolder extends ViewHolder {
        @Bind(R.id.ivGoodsTh)
        ImageView ivGoodsTh;
        @Bind(R.id.tvGoods)
        TextView tvGoods;
        @Bind(R.id.iv_collect_del)
        ImageView ivCollectDel;
        @Bind(R.id.layout_goods)
        RelativeLayout layoutGoods;

        CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.layout_goods)
        public void onGoodsItemClick(){
            CollectBean goods = (CollectBean) layoutGoods.getTag();
            MFGT.gotoGoodsDetailsActivity(mContext,goods.getGoodsId());
        }
        @OnClick(R.id.iv_collect_del)
        public void deleteCollect(){
            final CollectBean goods = (CollectBean) layoutGoods.getTag();
            String username = FuLiCenterApplication.getUser().getMuserName();
            NetDao.deleteCollect(mContext, username, goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result!=null && result.isSuccess()){
                        mList.remove(goods);
                        notifyDataSetChanged();
                    }else {
                        CommonUtils.showLongToast(result!=null?result.getMsg():mContext.getResources().getString(R.string.delete_collect_fail));
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("error="+error);
                    CommonUtils.showLongToast(mContext.getResources().getString(R.string.delete_collect_fail));
                }
            });
        }

    }
}