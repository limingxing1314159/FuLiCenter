package cn.ucai.fulicenter.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.CatChildFilterButton;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CategoryChildActivity extends BaseActivity {

    @Bind(R.id.tv_refresh)
    TextView tvRefresh;
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;

    CategoryChildActivity mContext;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int pageId = 1;
    GridLayoutManager glm;
    int catId;
    @Bind(R.id.btn_sort_price)
    Button btnSortPrice;
    @Bind(R.id.btn_sort_addtime)
    Button btnSortAddtime;
    boolean addTimeAsc = false;
    boolean priceAsc = false;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    @Bind(R.id.btnCatChildFilter)
    CatChildFilterButton btnCatChildFilter;
    String groupName;
    ArrayList<CategoryChildBean> mChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext, mList);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        if (catId == 0) {
            finish();
        }
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        mChildList = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        btnCatChildFilter.setText(groupName);
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();

    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadCategoryGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downloadCategoryGoods(final int action) {
        NetDao.downloadCategoryGoods(mContext, catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e("error" + error);
            }
        });

    }

    private void setPullUpListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = glm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downloadCategoryGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = glm.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition == 0);
            }
        });
    }

    @Override
    protected void initData() {
        downloadCategoryGoods(I.ACTION_DOWNLOAD);
        btnCatChildFilter.setOnCatFilterClickListener(groupName,mChildList);
    }


    @OnClick(R.id.backClickArea)
    public void onClick() {
        MFGT.finish(this);
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                priceAsc = !priceAsc;
                break;
            case R.id.btn_sort_addtime:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                btnSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                addTimeAsc = !addTimeAsc;
                break;
        }
        mAdapter.setSortBy(sortBy);
    }
}
