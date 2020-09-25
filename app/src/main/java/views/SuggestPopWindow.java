package views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishapp.CiKuDetailActivity;
import com.example.englishapp.R;

import java.util.ArrayList;
import java.util.List;

import adapters.SearchSuggestAdapter;
import bases.MyApplication;
import beans.SuggestBeans;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SuggestPopWindow extends PopupWindow {

    private final View mSuggestView;
    private RecyclerView mSuggestRv;
    private SearchSuggestAdapter mAdapter;
   // private List<String> mCurrentList=new ArrayList<>();
    private List<SuggestBeans> mCurrentList=new ArrayList<>();
    private TextView mPopEmpty;

    public SuggestPopWindow() {
        mSuggestView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.pop_suggest_window, null);
        setContentView(mSuggestView);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //外界可点击取消
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);

        initChildView();

        initEvent();
    }

    private void initEvent() {
        mAdapter.setOnSuggestItemClickListener(new SearchSuggestAdapter.onSuggestItemClickListener() {
            @Override
            public void onSuggestItemClick(String wordHead, String ukphone, String descCn, String tranCn) {
                Intent intent=new Intent(MyApplication.getContext(), CiKuDetailActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("headWord",wordHead);
                bundle.putString("tranCn",tranCn);
                bundle.putString("tranCn1",descCn);
                bundle.putString("ukphone",ukphone);
                intent.putExtras(bundle);

                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            }
        });
    }

    private void initChildView() {
        mSuggestRv = mSuggestView.findViewById(R.id.pop_suggest);
        mPopEmpty = mSuggestView.findViewById(R.id.pop_empty_data);
        mSuggestRv.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        mAdapter = new SearchSuggestAdapter();
        mSuggestRv.setAdapter(mAdapter);
    }

    //String
    public void setList(List<SuggestBeans> suggestList) {

        if (suggestList != null) {
            mAdapter.setData(suggestList);
            this.mCurrentList=suggestList;
        }
    }
}
