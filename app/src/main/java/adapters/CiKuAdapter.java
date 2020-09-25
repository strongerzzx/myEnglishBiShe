package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishapp.R;

import org.xml.sax.helpers.LocatorImpl;

import java.util.ArrayList;
import java.util.List;

import beans.ZipBeans;
import presenters.CollectionPresent;
import utils.LogUtil;

public class CiKuAdapter extends RecyclerView.Adapter<CiKuAdapter.InnerViewHolder>  {
    private static final String TAG = "CiKuAdapter";
    private List<ZipBeans> mList =new ArrayList<>();
    private onCiKuItemClickListener mOnCiKuItemClickListener;
    private CollectionPresent mCollPresent;


    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.ci_ku_recyler_item, parent, false);
        return new InnerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        ZipBeans beans = mList.get(position);
        String ukphone = beans.getContent().getWord().getContent().getUkphone();//音标
        String headWord = mList.get(position).getHeadWord();
        String tranCn = beans.getContent().getWord().getContent().getTrans().get(0).getTranCn();
        String tranCn1 = beans.getContent().getWord().getContent().getTrans().get(0).getDescCn();
        holder.tv2Chinese.setText(position+"-"+tranCn+","+tranCn1);
        holder.tv2English.setText(headWord);

        if (mOnCiKuItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCiKuItemClickListener.onCiKuClickListener(headWord,tranCn,tranCn1,ukphone);
                }
            });
        }

        holder.ivCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollPresent = CollectionPresent.getPresent();
                mCollPresent.setData(position,tranCn,tranCn1,headWord);
                mCollPresent.doCollection();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public void setData(List<ZipBeans> beansList) {
      //  mList.clear();
        if (beansList != null) {
            mList.addAll(beansList);
            notifyDataSetChanged();
        }
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv2English;
        private TextView tv2Chinese;
        private ImageView ivCollection;
        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv2Chinese=itemView.findViewById(R.id.ciku_item_chinese);
            tv2English=itemView.findViewById(R.id.ciku_item_english);
            ivCollection=itemView.findViewById(R.id.ciku_item_collection_iv);
        }
    }

    public void setOnCiKuItemClickListener(onCiKuItemClickListener onCiKuItemClickListener) {
        mOnCiKuItemClickListener = onCiKuItemClickListener;
    }

    public interface onCiKuItemClickListener{
        //item点击 --> 详情页
        void onCiKuClickListener(String headWord, String tranCn, String tranCn1, String ukphone);

    }
}
