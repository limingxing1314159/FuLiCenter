package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class OrderActivity extends BaseActivity {
    private static final String TAG = OrderActivity.class.getSimpleName();

    @Bind(R.id.ed_order_name)
    EditText edOrderName;
    @Bind(R.id.ed_order_phone)
    EditText edOrderPhone;
    @Bind(R.id.spin_order_province)
    Spinner spinOrderProvince;
    @Bind(R.id.ed_order_street)
    EditText edOrderStreet;
    @Bind(R.id.tv_order_price)
    TextView tvOrderPrice;

    OrderActivity mContext;
    User user = null;
    String cartIds = "";
    ArrayList<CartBean> mList = null;
    String[] ids = new String[]{};
    int ranPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,getString(R.string.confirm_order));

    }

    @Override
    protected void initData() {
        cartIds = getIntent().getStringExtra(I.Cart.ID);
         user = FuLiCenterApplication.getUser();
        L.e(TAG,"cartIds="+cartIds);
        if (cartIds==null || cartIds.equals("") || user==null){
            finish();
        }
        ids = cartIds.split(",");
        getOrderList();
    }

    public void getOrderList() {
        NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                if (list==null || list.size()==0){
                    finish();
                }else {
                    mList.addAll(list);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void sumPrice() {
        ranPrice = 0;
        if (mList!=null && mList.size()>0){
            for (CartBean c:mList) {
                L.e(TAG,"c.id="+c.getId());
                for (String id : ids) {
                    L.e(TAG,"order.id="+id);
                    if (id.equals(String.valueOf(c.getId()))) {
                        ranPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }
            }
        }
        tvOrderPrice.setText("合计:￥"+Double.valueOf(ranPrice));
    }
    private int getPrice(String price){
        price = price.substring(price.indexOf("￥")+1);
        return Integer.valueOf(price);
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.tv_order_buy)
    public void onClick() {
        String receiveName = edOrderName.getText().toString();
        if (TextUtils.isEmpty(receiveName)){
            edOrderName.setError("收货人姓名不能为空");
            edOrderName.requestFocus();
            return;
        }
        String mobile = edOrderPhone.getText().toString();
        if (TextUtils.isEmpty(mobile)){
            edOrderPhone.setError("手机号不能为空");
            edOrderPhone.requestFocus();
            return;
        }
        if (!mobile.matches("[\\d]{11}")){
            edOrderPhone.setError("手机格式错误");
            edOrderPhone.requestFocus();
            return;
        }
        String area = spinOrderProvince.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)){
            Toast.makeText(OrderActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = edOrderStreet.getText().toString();
        if (TextUtils.isEmpty(address)) {
            edOrderStreet.setError("街道地址不能为空");
            edOrderStreet.requestFocus();
            return;
        }
        gotoStatements();
    }

    private void gotoStatements() {
        L.e(TAG,"ranPrice="+ranPrice);
    }

}
