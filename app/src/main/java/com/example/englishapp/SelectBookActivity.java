package com.example.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.Iterator;
import java.util.List;

import adapters.SelectAdapter;
import beans.SelectBookBeans;
import inerfaces.ISelectBookCallback;
import presenters.SelectBookPresent;
import utils.LogUtil;
import views.UILoader;

public class SelectBookActivity extends AppCompatActivity implements ISelectBookCallback {

    private static final String TAG = "SelectBookActivity";
    private SelectBookPresent mSelectBookPresent;
    private SelectAdapter mAdapter;
    private FrameLayout mSelectContent;
    private UILoader mUiLoader;
    private View mSuccessView;
    private TwinklingRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_book);

        mSelectBookPresent = SelectBookPresent.getInstance();
        mSelectBookPresent.regestierSelectBookCallback(this);


        initView();

        initEvent();


    }

    private View createSuccessLayout(ViewGroup container) {
        mSuccessView = LayoutInflater.from(SelectBookActivity.this).inflate(R.layout.select_book_recycler_view, container, false);
        mRefreshLayout = mSuccessView.findViewById(R.id.select_twink);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableOverScroll(false);
        RecyclerView rvSuccess =  mSuccessView.findViewById(R.id.rv_select);
        rvSuccess.setLayoutManager(new LinearLayoutManager(this));
        rvSuccess.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom=8;
                outRect.top=5;
            }
        });

        mAdapter=new SelectAdapter();
        rvSuccess.setAdapter(mAdapter);
        return mSuccessView;
    }

    private void initEvent() {
        //获取数据
        mSelectBookPresent.getBook();
    }

    private void initView() { ;
        mSelectContent = findViewById(R.id.select_book_content);

        if (mUiLoader == null) {
            mUiLoader = new UILoader(SelectBookActivity.this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessLayout(container);
                }
            };
        }

        if (mUiLoader.getParent() instanceof ViewGroup){
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        mSelectContent.addView(mUiLoader);

    }

    @Override
    public void getBookList(List<SelectBookBeans.CatesBean> catesBeans,int random) {

        for (SelectBookBeans.CatesBean catesBean : catesBeans) {
            List<SelectBookBeans.CatesBean.BookListBean> bookList = catesBean.getBookList();

            SelectBookBeans.CatesBean catesBean1 = catesBeans.get(0);

            LogUtil.d(TAG,bookList.get(0).getSize()+"");
                //空数据的情况
                if (bookList!=null && bookList.size()==0){
                    mUiLoader.updateStatus(UILoader.UIStatus.EMPETY_DATA);
                }

                //成功加载的情况
                if (bookList != null && bookList.size()>0) {
                    mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
                    mAdapter.setData(bookList);
                }
        }


    }

    @Override
    public void onLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onNetworkError() {
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSelectBookPresent.unRegestierSelectBookCallback(this);
    }
}
