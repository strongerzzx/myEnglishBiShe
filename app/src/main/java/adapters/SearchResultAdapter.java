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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.InnerViewHolder> {

    List<SuggestBeans> mHeardWordsList=new ArrayList<>();

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View searchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_rv_item_view, parent, false);
        return new InnerViewHolder(searchView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        SuggestBeans suggestBeans = mHeardWordsList.get(position);
        holder.searchEnglish.setText(suggestBeans.getWordHead());
    }

    @Override
    public int getItemCount() {
        return mHeardWordsList==null?0:mHeardWordsList.size();
    }

    public void setData(List<SuggestBeans> wordBeanList) {
        mHeardWordsList.clear();
        if (wordBeanList != null) {
            mHeardWordsList.addAll(wordBeanList);
        }
        notifyDataSetChanged();
    }


    public class InnerViewHolder extends RecyclerView.ViewHolder {

        private TextView searchEnglish;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);

            searchEnglish=itemView.findViewById(R.id.search_item_english);
        }
    }
}
