package cn.ucai.fulicenter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.DisplayUtils;

public class TermsOfServiceWebView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service_web_view);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "福利社软件许可及服务协议");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
}
