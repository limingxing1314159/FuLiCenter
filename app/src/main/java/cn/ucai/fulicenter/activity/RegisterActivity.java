package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zhou.com.fulicenter.I;
import zhou.com.fulicenter.R;
import zhou.com.fulicenter.bean.Result;
import zhou.com.fulicenter.net.NetDao;
import zhou.com.fulicenter.net.OkHttpUtils;
import zhou.com.fulicenter.utils.CommonUtils;
import zhou.com.fulicenter.utils.L;
import zhou.com.fulicenter.utils.MFGT;
import zhou.com.fulicenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.backClickArea)
    LinearLayout mBackClickArea;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.r_username)
    EditText mUsername;
    @BindView(R.id.nick)
    EditText mNick;
    @BindView(R.id.r_password)
    EditText mPassword;
    @BindView(R.id.confirmPassword)
    EditText mConfirmPassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    String username;
    String nick;
    String password;
    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "用户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_register)
    public void checkedInput() {
        username = mUsername.getText().toString().trim();
        nick = mNick.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            mUsername.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            CommonUtils.showShortToast(R.string.illegal_user_name);
            mUsername.requestFocus();
            return;
        } else if (TextUtils.isEmpty(nick)) {
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            mNick.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            mPassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            mConfirmPassword.requestFocus();
            return;
        } else if (!password.equals(confirmPassword)) {
            CommonUtils.showShortToast(R.string.two_input_password);
            mConfirmPassword.requestFocus();
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.register(mContext, username, nick, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result == null) {
                    CommonUtils.showShortToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        CommonUtils.showLongToast(R.string.register_success);
                        setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, username));
                        MFGT.finish(mContext);
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        mUsername.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(error);
                L.e(TAG, "register error=" + error);
            }
        });
    }
}
