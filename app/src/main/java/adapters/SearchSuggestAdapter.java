package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishapp.R;

import java.util.ArrayList;
import java.util.List;

import beans.SuggestBeans;

public class SearchSuggestAdapter extends RecyclerView.Adapter<SearchSuggestAdapter.InnerViewHolder> {

    private List<SuggestBeans> mSuggestList=new ArrayList<>();
    private onSuggestItemClickListener mOnSuggestItemClickListener;
    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View suggestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_suggest_rv, parent, false);
        return new InnerViewHolder(suggestView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        SuggestBeans suggestBeans = mSuggestList.get(position);


        String wordHead = suggestBeans.getWordHead();
        String ukphone = suggestBeans.getUkphone();
        String descCn = suggestBeans.getDescCn();
        String tranCn = suggestBeans.getTranCn();

        holder.suggestEnglish.setText(wordHead);
        holder.suggestFayin.setText(ukphone);
        holder.suggestChines.setText(descCn+","+tranCn);


        if (mOnSuggestItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSuggestItemClickListener.onSuggestItemClick(wordHead,ukphone,descCn,tranCn);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSuggestList==null?0:mSuggestList.size();
    }

    public void setData(List<SuggestBeans> suggestList) {
        mSuggestList.clear();
        if (suggestList != null) {
            mSuggestList.addAll(suggestList);
        }
        notifyDataSetChanged();
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestEnglish;
        private TextView suggestChines;
        private TextView suggestFayin;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestEnglish=itemView.findViewById(R.id.search_suggest_english);
            suggestChines=itemView.findViewById(R.id.search_suggest_chinese);
            suggestFayin=itemView.findViewById(R.id.search_suggest_fayin);
        }
    }

    public void setOnSuggestItemClickListener(onSuggestItemClickListener onSuggestItemClickListener) {
        mOnSuggestItemClickListener = onSuggestItemClickListener;
    }

    public interface onSuggestItemClickListener{
        void onSuggestItemClick(String wordHead, String ukphone, String descCn, String tranCn);
    }
}
