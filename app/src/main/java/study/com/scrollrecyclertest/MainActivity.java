package study.com.scrollrecyclertest;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private View viewStatusBar;
    private PersonalViewpager viewPager;
    private TestPagerAdapter testPagerAdapter;
    private List<Fragment> fragments;
    private ObservableScrollView scrollView;
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    private RelativeLayout relaTop;
    private LinearLayout lineTab;
    private LinearLayout lineTab2;

    private int maxDiff = 0;
    private int statusBarHeight = 0;
    private int topLayoutHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        relaTop = findViewById(R.id.rela_top);
        scrollView = findViewById(R.id.scrollView_main);
        viewPager = findViewById(R.id.viewpager_main);
        lineTab = findViewById(R.id.line_tab);
        lineTab2 = findViewById(R.id.line_tab2);
        viewPager.setCanScroll(false);//设置viewpager不能左右滑动
        viewStatusBar = findViewById(R.id.view_status_bar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //设置透明状态栏
        ViewGroup.LayoutParams lp = viewStatusBar.getLayoutParams();
        statusBarHeight = ScreenUtil.getStatusBarHeight(this);
        lp.height = statusBarHeight;
        viewStatusBar.setLayoutParams(lp);
    }

    private void initData() {
        topLayoutHeight = ScreenUtil.getMeasureHeight(relaTop);
        fragments = new ArrayList<>();
        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();
        fragments.add(fragmentOne);
        fragments.add(fragmentTwo);
        testPagerAdapter = new TestPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(testPagerAdapter);
    }

    private void initListener() {
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(NestedScrollView scrollView, int x, int y, int oldx, int oldy) {
                //悬停tab
                int[] location = new int[2];
                lineTab.getLocationOnScreen(location);
                int yPosition = location[1];
                int visibleY = statusBarHeight + topLayoutHeight;

                if (yPosition <= visibleY) {
                    lineTab2.setVisibility(View.VISIBLE);
                } else {
                    lineTab2.setVisibility(View.GONE);
                }
                if (yPosition - visibleY > maxDiff) {//获取最大间距
                    maxDiff = yPosition - visibleY;
                }
//            PluLog.i("LHD PersonalCenterActivity    y = $yPosition   maxDiff = $maxDiff")
                //tab 渐变
                if (maxDiff <= 0) return;
                int diff = yPosition - visibleY;
                DecimalFormat df = new DecimalFormat("0.0");
//            PluLog.i("LHD  距离 = $diff  maxDiff = $maxDiff" + "   比例 = " + df.format(diff * 255 / maxDiff) + "  最终值 = " + df.format(diff * 255 / maxDiff))
                if (diff >= 0) {
                    String valueLong = df.format(diff * 255 / maxDiff);
                    String value = valueLong.substring(0, valueLong.length() - 2);
//                PluLog.i("LHD 最终透明值 ： $value")
                    int alph = 255 - Integer.parseInt(value);
//                PluLog.i("LHD  透明值 = $alph    $value")
                    relaTop.setBackgroundColor(Color.argb(alph, 47, 48, 47));
                } else {
                    relaTop.setBackgroundColor(Color.argb(255, 47, 48, 47));
                }

                //上拉加载
                if (y == (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight())) {
                    Log.i("LHD", "LHHD >>>>>>>>>>>>>  滑动到底部");
                    fragmentOne.loadMore();
                }
            }
        });
    }

}
