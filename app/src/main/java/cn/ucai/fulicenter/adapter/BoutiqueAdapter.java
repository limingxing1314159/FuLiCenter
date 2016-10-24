package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.bean.BoutiqueBean;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueAdapter extends Adapter<BoutiqueAdapter.BoutiqueViewHolder> {
    Context mContext;
    ArrayList<BoutiqueBean> mList;

    public BoutiqueAdapter(Context context,ArrayList<BoutiqueBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    @Override
    public BoutiqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BoutiqueViewHolder holder =new BoutiqueViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_boutique, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BoutiqueViewHolder holder, int position) {
            BoutiqueBean boutiqueBean = mList.get(position);
            ImageLoader.downloadImg(mContext,holder.mLvBoutiqueImg,boutiqueBean.getImageurl());
            holder.mTvBoutiqueTitle.setText(boutiqueBean.getTitle());
            holder.mTvBoutiqueName.setText(boutiqueBean.getName());
            holder.mTvBoutiqueDescription.setText(boutiqueBean.getDescription());
            holder.mLayoutBoutiqueItem.setTag(boutiqueBean);
        L.i("1111111" + boutiqueBean.toString());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if (mList!=null){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

     class BoutiqueViewHolder extends ViewHolder{
        @Bind(R.id.ivBoutiqueImg)
        ImageView mLvBoutiqueImg;
        @Bind(R.id.tvBoutiqueTitle)
        TextView mTvBoutiqueTitle;
        @Bind(R.id.tvBoutiqueName)
        TextView mTvBoutiqueName;
        @Bind(R.id.tvBoutiqueDescription)
        TextView mTvBoutiqueDescription;
        @Bind(R.id.layout_boutique_item)
        RelativeLayout mLayoutBoutiqueItem;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
         @OnClick(R.id.layout_boutique_item)
         public void onBoutiqueClick(){
             BoutiqueBean bean = (BoutiqueBean) mLayoutBoutiqueItem.getTag();
             L.i("２２２２２２２２２" +bean.toString());
             MFGT.gotoBoutiqueChildActivity(mContext,bean);
         }
    }
}
