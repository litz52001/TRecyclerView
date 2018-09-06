package com.rv;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.rv.itemView.ItemType;
import com.rv.itemView.banner;
import com.rv.pojo.BannerVo;
import com.rv.pojo.ItemVo;
import com.trecyclerview.SwipeRecyclerView;
import com.trecyclerview.TRecyclerView;
import com.trecyclerview.listener.OnLoadMoreListener;
import com.trecyclerview.listener.OnRefreshListener;
import com.trecyclerview.multitype.Items;
import com.trecyclerview.multitype.MultiTypeAdapter;
import com.trecyclerview.pojo.FootVo;
import com.trecyclerview.pojo.HeaderVo;
import com.trecyclerview.progressindicator.ProgressStyle;
import com.trecyclerview.view.FootViewHolder;
import com.trecyclerview.view.HeaderViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author：tqzhang on 18/8/22 13:48
 */
public class SwipeLinearLayoutActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRecyclerView tRecyclerView;
    private Items items;
    private MultiTypeAdapter adapter;

    private int indexPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type2);
        tRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        items = new Items();
        adapter = new MultiTypeAdapter.Builder()
                .bind(BannerVo.class, new banner(SwipeLinearLayoutActivity.this))
                .bind(ItemVo.class, new ItemType(SwipeLinearLayoutActivity.this))
                .bind(FootVo.class, new FootViewHolder(SwipeLinearLayoutActivity.this, ProgressStyle.Pacman))
                .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(SwipeLinearLayoutActivity.this);
        tRecyclerView.setAdapter(adapter);
        tRecyclerView.setLayoutManager(layoutManager);
        setListener();
        initData();
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, 60);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        items.add(new BannerVo());
                        for (int i = 0; i < 10; i++) {
                            items.add(new ItemVo());
                        }
                        tRecyclerView.refreshComplete(items, false);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }, 5000);
            }
        });


    }

    private void setListener() {
        tRecyclerView.addOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                final Items item = new Items();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        indexPage += 1;
                        for (int i = 0; i < 10; i++) {
                            item.add(new ItemVo());
                        }
                        items.addAll(item);
                        if (indexPage == 4) {
                            tRecyclerView.loadMoreComplete(item, true);
                        } else {
                            tRecyclerView.loadMoreComplete(item, false);
                        }

                    }

                }, 2000);
            }
        });
    }

    private void initData() {
        items.clear();
        items.add(new BannerVo());
        for (int i = 0; i < 20; i++) {
            items.add(new ItemVo());
        }
        tRecyclerView.refreshComplete(items, true);
    }
}
