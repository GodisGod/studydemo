package study.com.scrollrecyclertest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  HONGDA on 2018/6/19.
 */
public class FragmentOne extends Fragment {

    private RecyclerView recyclerView;
    private List<String> strDatas;
    private AdapterFragment adapterFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_fragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        strDatas = new ArrayList<>();
        adapterFragment = new AdapterFragment(getContext(), strDatas);
        recyclerView.setAdapter(adapterFragment);
        WrappingGridLayoutManager gridLayoutManager = new WrappingGridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);//固定自身size不受adapter变化影响
        //消除滑动卡顿现象 ...
        recyclerView.setNestedScrollingEnabled(false);//限制recyclerview自身滑动特性,滑动全部靠scrollview完成
        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            strDatas.add("item  " + i);
        }
        adapterFragment.notifyDataSetChanged();
    }

    public void loadMore() {
        List<String> newStrs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newStrs.add("新增  " + i);
        }
        int length = strDatas.size();
        strDatas.addAll(newStrs);
//        adapterFragment.notifyItemRangeChanged(length, newStrs.size());
        adapterFragment.notifyDataSetChanged();
    }

}
