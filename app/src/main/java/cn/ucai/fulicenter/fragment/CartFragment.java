package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/19.
 */
public class CartFragment extends BaseFragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    @Bind(R.id.tv_refresh)
    TextView mTvRefresh;
    @Bind(R.id.rv)
    RecyclerView mRv;
    @Bind(R.id.srl)
    SwipeRefreshLayout mSrl;

    LinearLayoutManager llm;
    MainActivity mContent;
    CartAdapter mAdapter;
    ArrayList<CartBean> mList;
    @Bind(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @Bind(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @Bind(R.id.layout_cart)
    RelativeLayout layoutCart;
    @Bind(R.id.tv_nothing)
    TextView tvNothing;
    private boolean cartLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        mContent = (MainActivity) getContext();
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContent, mList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {
        setPullDownListener();
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mTvRefresh.setVisibility(View.VISIBLE);
                downloadCart();
            }
        });
    }

    @Override
    protected void initData() {
        downloadCart();
    }

    public void downloadCart() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.downloadCart(mContent, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                    L.e(TAG, "result=" + list);
                    mSrl.setRefreshing(false);
                    mTvRefresh.setVisibility(View.GONE);
                    if (list != null && list.size() > 0) {
                        L.e(TAG, "list(0)=" + list.get(0));
                        mAdapter.initData(list);
                        setCartLayout(true);
                    }else {
                        setCartLayout(false);
                    }
                }

                @Override
                public void onError(String error) {
                    setCartLayout(false);
                    mSrl.setRefreshing(false);
                    mTvRefresh.setVisibility(View.GONE);
                    CommonUtils.showShortToast(error);
                    L.e("error" + error);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        llm = new LinearLayoutManager(mContent);
        mRv.setLayoutManager(llm);
        mRv.setHasFixedSize(true);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(new SpaceItemDecoration(12));
        setCartLayout(false);
    }

    public void setCartLayout(boolean hasCart) {
        layoutCart.setVisibility(hasCart?View.VISIBLE:View.GONE);
        tvNothing.setVisibility(hasCart?View.GONE:View.VISIBLE);
        mRv.setVisibility(hasCart?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.tv_cart_buy)
    public void onClick() {
    }
}
