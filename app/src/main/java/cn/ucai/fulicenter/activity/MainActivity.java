package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.NewgoodsFragment;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btnNewGoods)
    RadioButton mbtnNewGoods;
    @Bind(R.id.btnBoutique)
    RadioButton mbtnBoutique;
    @Bind(R.id.btnCategory)
    RadioButton mbtnCategory;
    @Bind(R.id.layoutCart)
    RadioButton mlayoutCart;
    @Bind(R.id.tvCartHint)
    TextView mtvCartHint;
    @Bind(R.id.btnMe)
    RadioButton mbtnMe;

    int index;
    RadioButton[] rbs;
    Fragment[] mFragments;
    NewgoodsFragment mNewgoodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initFragment();
    }

    private void initFragment() {
        mFragments = new Fragment[5];
        mNewgoodsFragment = new NewgoodsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mNewgoodsFragment)
                .show(mNewgoodsFragment)
                .commit();
    }

    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = mbtnNewGoods;
        rbs[1] = mbtnBoutique;
        rbs[2] = mbtnCategory;
        rbs[3] = mlayoutCart;
        rbs[4] = mbtnMe;
    }
    public void onCheckedChange(View v){
        switch (v.getId()){
            case R.id.btnNewGoods:
                index = 0;
                break;
            case R.id.btnBoutique:
                index = 1;
                break;
            case R.id.btnCategory:
                index = 2;
                break;
            case R.id.layoutCart:
                index = 3;
                break;
            case R.id.btnMe:
                index = 4;
                break;
        }
        setRadioButtonStatus();
    }
    private void setRadioButtonStatus() {
        for (int i = 0;i<rbs.length;i++){
            if (i==index){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }
        }
    }
}
