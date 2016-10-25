package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/24.
 */
public class PersonalCenterFragment extends BaseFragment {
    private static final String TAG = PersonalCenterFragment.class.getSimpleName();
    @Bind(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;

    MainActivity mContext;
    User user = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getActivity();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        L.e(TAG,"user="+user);
        if (user==null){
            MFGT.gotoLogin(mContext);
        }else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
        }
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        L.e(TAG,"user="+user);
        if (user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_center_settings,R.id.center_user_info})
    public void gotoSettings() {
        MFGT.gotoSettings(mContext);
    }
}
