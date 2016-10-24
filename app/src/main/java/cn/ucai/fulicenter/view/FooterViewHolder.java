package cn.ucai.fulicenter.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/19.
 */
public class FooterViewHolder extends ViewHolder{
    @Bind(R.id.tvFooter)
    public
    TextView tvFooter;


    public FooterViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
