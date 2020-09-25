package com.example.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapters.CiKuAdapter;
import bases.MyApplication;
import beans.ZipBeans;
import inerfaces.ICikuCallback;
import presenters.CikuPresent;
import utils.ListPageUtils;
import utils.LogUtil;

public class CiKuActivity extends AppCompatActivity implements ICikuCallback {

    private static final String TAG = "CiKuActivity";
    private CiKuAdapter mAdapter;
    private RecyclerView mCikuRv;
    private CikuPresent mPresent;
    private TwinklingRefreshLayout mRefreshLayout;
    private boolean isLoader = true;
    private TextView mCikuTitle;
    private int mScrollY;//滑动的距离
    private int mTitleHeight;//标题控件的高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ci_ku);

        mPresent = CikuPresent.getPresent();
        mPresent.regesiterCikuCallback(this);

        initView();
    }

    private void initView() {

        mCikuTitle = findViewById(R.id.ci_ku_title);
        mRefreshLayout = findViewById(R.id.ci_ku_tw_refresh);
        mCikuRv = findViewById(R.id.ci_ku_recyle_view);
        mCikuRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CiKuAdapter();
        mPresent.requestAllWords();//先请求20条数据
        mCikuRv.setAdapter(mAdapter);


        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setEnableRefresh(false);


        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresent.loaderMore();
                isLoader=true;

            }
        });

        mCikuRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom=5;
                outRect.top=5;
            }
        });

        mCikuRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dy ↑正 ↓负   Rv滑动的时候 隐藏标题
                if (mTitleHeight==0){
                    mTitleHeight = mCikuTitle.getHeight();
                }
                //记录滑动的距离
                mScrollY +=dy ;
                if (mScrollY<=0){
                    mCikuTitle.setVisibility(View.VISIBLE);
                }else{
                    mCikuTitle.setVisibility(View.INVISIBLE);
                }
            }
        });


        mAdapter.setOnCiKuItemClickListener(new CiKuAdapter.onCiKuItemClickListener() {
            @Override
            public void onCiKuClickListener(String headWord, String tranCn, String tranCn1, String ukphone) {
                Intent intent=new Intent(CiKuActivity.this,CiKuDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("headWord",headWord);
                bundle.putString("tranCn",tranCn);
                bundle.putString("tranCn1",tranCn1);
                bundle.putString("ukphone",ukphone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    @Override
    public void showAllWords(List<ZipBeans> beansList) {
        if (beansList != null && isLoader) {
            mAdapter.setData(beansList);
        }
    }

    @Override
    public void LoaderMore(int size) {
        if (size>0) {
            mRefreshLayout.finishLoadmore();
            Toast.makeText(this, "加载了"+size+"条数据", Toast.LENGTH_SHORT).show();
        }else {
            isLoader = false;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.unRegesiterCikuCallback(this);
    }
}
