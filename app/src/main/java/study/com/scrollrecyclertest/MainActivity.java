package study.com.scrollrecyclertest;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PersonalViewpager viewPager;
    private TestPagerAdapter testPagerAdapter;
    private List<Fragment> fragments;
    private ObservableScrollView scrollView;
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        scrollView = findViewById(R.id.scrollView_main);
        viewPager = findViewById(R.id.viewpager_main);
        viewPager.setCanScroll(false);
    }

    private void initData() {
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
                //上拉加载
                if (y == (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight())) {
                    Log.i("LHD", "LHHD >>>>>>>>>>>>>  滑动到底部");
                    fragmentOne.loadMore();
                }
            }
        });
    }

}
