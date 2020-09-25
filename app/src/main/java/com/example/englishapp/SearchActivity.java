package com.example.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapters.CiKuAdapter;
import adapters.SearchResultAdapter;
import adapters.SearchSuggestAdapter;
import beans.SuggestBeans;
import inerfaces.ISearchCallback;
import presenters.SearchPresent;
import utils.LogUtil;
import views.SuggestPopWindow;
import views.UILoader;

public class SearchActivity extends AppCompatActivity implements ISearchCallback {

    private static final String TAG = "SearchActivity";
    private InputMethodManager mIm;
    private ImageView mLeftBack;
    private EditText mEtInput;
    private ImageView mIvDelete;
    private TextView mSearchBtn;
    private SearchResultAdapter mSearchResultAdapter;
    private SearchPresent mSearchPresent;
    private UILoader mUiLoader;
    private FrameLayout mSearchContent;
    private RecyclerView mSearchResultRv;
    private SuggestPopWindow mSuggestPop;
    private LinearLayout mSearchHeader;
    private int mHeardHeight;
    private int mScrollY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mIm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mSearchPresent = SearchPresent.getPresent();
        mSearchPresent.regViewCallback(this);

        mSuggestPop = new SuggestPopWindow();


        initView();

        initEvent();
    }

    private View createSuccessView(ViewGroup container) {
        View successView = LayoutInflater.from(container.getContext()).inflate(R.layout.search_successful_view, container, false);

        //搜索结果的Rv
        mSearchResultRv = successView.findViewById(R.id.search_result_rv);
        mSearchResultRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultAdapter = new SearchResultAdapter();
        mSearchResultRv.setAdapter(mSearchResultAdapter);


        //联想词的Rv


        return successView;

    }

    private void initEvent() {

        mEtInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEtInput.requestFocus();
                mIm.showSoftInput(mEtInput, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEtInput.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空...", Toast.LENGTH_SHORT).show();
                } else {
                    if (mSearchPresent != null) {
                        mSearchPresent.doSearch(input.trim());
                        mIm.hideSoftInputFromWindow(mEtInput.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                        mSuggestPop.dismiss();
                        updatePopBg(1.0f);
                    }
                }
            }
        });


        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newInput = s.toString().replaceAll(" ", "");
                if (!TextUtils.isEmpty(newInput)) {
                    mIvDelete.setVisibility(View.VISIBLE);
                    mSearchPresent.doSuggestWords(newInput);
                    LogUtil.d(TAG, "new Input --> " + newInput);
                    //弹出popWindow并且window变灰
                    mSuggestPop.showAsDropDown(mEtInput, Gravity.LEFT, 0, 0);
                    updatePopBg(0.8f);
                } else {
                    mIvDelete.setVisibility(View.GONE);
                    mSuggestPop.dismiss();
                    updatePopBg(1.0f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtInput.setText("");
            }
        });

        mSuggestPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                updatePopBg(1.0f);
            }
        });

        mSearchResultRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mHeardHeight == 0) {
                    mHeardHeight = mSearchHeader.getHeight();
                }

                mScrollY += dy;
                if (mScrollY <= 0) {
                    mSearchHeader.setVisibility(View.VISIBLE);
                } else {
                    mSearchHeader.setVisibility(View.GONE);
                }

                LogUtil.d(TAG, "dy --> " + dy);
            }
        });

    }


    private void updatePopBg(float alpha) {
        WindowManager.LayoutParams windowAttr = getWindow().getAttributes();
        windowAttr.alpha = alpha;
        getWindow().setAttributes(windowAttr);
    }

    private void initView() {
        mSearchHeader = findViewById(R.id.search_header);
        mLeftBack = findViewById(R.id.search_left_arrow);
        mEtInput = findViewById(R.id.search_input);
        mIvDelete = findViewById(R.id.search_input_delete);
        mSearchBtn = findViewById(R.id.search_btn);
        mIvDelete.setVisibility(View.GONE);
        mSearchContent = findViewById(R.id.search_content_layout);

        if (mUiLoader == null) {
            mUiLoader = new UILoader(SearchActivity.this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
        }

        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        mSearchContent.addView(mUiLoader);
    }

    @Override
    public void onSearchResult(List<SuggestBeans> mHeardWordsList) {
        mSearchResultRv.setVisibility(View.VISIBLE);
        if (mHeardWordsList != null && mHeardWordsList.size() > 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);

            mSearchResultAdapter.setData(mHeardWordsList);
        }

    }

    //大小限定为10 String
    @Override
    public void onSuggestResult(List<SuggestBeans> suggestList) {
        if (suggestList != null) {
            mSuggestPop.setList(suggestList);
        }
        LogUtil.d(TAG, "suggesList size --> " + suggestList.size());
    }

    @Override
    public void onEmpty() {
        mUiLoader.updateStatus(UILoader.UIStatus.EMPETY_DATA);
        LogUtil.d(TAG, "空数据");
    }

    @Override
    public void onLoading() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresent != null) {
            mSearchPresent.unRegViewCallback(this);
        }
    }
}
