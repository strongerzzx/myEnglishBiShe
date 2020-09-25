package fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapp.CiKuActivity;
import com.example.englishapp.R;
import com.example.englishapp.SearchActivity;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import inerfaces.IHomeCallback;
import presenters.HomePresent;
import utils.LogUtil;
import views.LoadingDialog;
import views.SuggestPopWindow;
import views.UILoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements IHomeCallback {

    private static final String TAG = "HomeFragment";
    private ImageView mRefresh;
    private HomePresent mPresent;
    private TextView mShowSingleEnglish;
    private TextView mShowSingleChines;
    private UILoader mUiLoader;
    private View mSuccessView;
    private Button mBtnCiku;
    private Button mSearch;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mUiLoader == null) {
            mUiLoader = new UILoader(container.getContext()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
        }

        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        LogUtil.d(TAG, "onCreateView --->");

        return mUiLoader;
    }

    private View createSuccessView(ViewGroup container) {
        mSuccessView = LayoutInflater.from(container.getContext()).inflate(R.layout.home_success_view, container, false);
        mBtnCiku = mSuccessView.findViewById(R.id.home_btn_ciku);
        mRefresh = mSuccessView.findViewById(R.id.home_refresh_iv);
        mSearch = mSuccessView.findViewById(R.id.home_btn_search);


        mShowSingleEnglish = mSuccessView.findViewById(R.id.home_tv_english);
        mShowSingleChines = mSuccessView.findViewById(R.id.home_tv_chinese);

        return mSuccessView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresent = HomePresent.getPresent();
        mPresent.regesiterHomeCallback(this);
        mPresent.getSingleWords();


        //词库界面
        mBtnCiku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CiKuActivity.class);
                startActivity(intent);
            }
        });

        //搜索界面
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void showSingleWords(String singleEnglish, String singleChinese) {

        if (getActivity() != null && mRefresh != null) {
            mRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mShowSingleEnglish.setText(singleEnglish);
                    mShowSingleChines.setText(singleChinese);
                }
            });
        }
    }

    @Override
    public void onRequestLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onFinishLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        mPresent.unRegesiterHomeCallback(this);
    }
}
