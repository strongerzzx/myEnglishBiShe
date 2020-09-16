package adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.englishapp.R;

import java.util.ArrayList;
import java.util.List;

import bases.MyApplication;
import beans.SelectBookBeans;
import presenters.HomePresent;
import utils.LogUtil;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.InnerViewHolder> {

    private static final String TAG = "SelectAdapter";
   // List<SelectBookBeans.CatesBean.BookListBean> mBeanList=new ArrayList<>();
   List<SelectBookBeans.CatesBean.BookListBean> mBeanList=new ArrayList<>();
   private onItemClickListener mOnItemClickListener;
    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.select_book_item, null);
        return new InnerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        SelectBookBeans.CatesBean.BookListBean bookListBean = mBeanList.get(position);
        String originName = bookListBean.getBookOrigin().getOriginName();//来源

        String title = bookListBean.getTitle();//书名
        String cover = bookListBean.getCover();//图片
        int size = bookListBean.getWordNum();//词量

        holder.tvWordSum.setText(String.valueOf(size));
        holder.tvSrcName.setText(originName);
        holder.tvTitle.setText(title);

        RequestOptions options=new RequestOptions().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(MyApplication.getContext()).load(cover).apply(options).into(holder.ivTitle);

        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position,mBeanList);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mBeanList==null?0:mBeanList.size();
    }

    public void setData(List<SelectBookBeans.CatesBean.BookListBean> bookList) {
        if (bookList != null) {
            mBeanList.addAll(bookList);
            notifyDataSetChanged();
        }
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTitle;
        private TextView tvTitle;
        private TextView tvSrcName;
        private TextView tvWordSum;
        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTitle=itemView.findViewById(R.id.select_iv);
            tvTitle=itemView.findViewById(R.id.select_tv_title);
            tvSrcName=itemView.findViewById(R.id.select_tv_src_name);
            tvWordSum=itemView.findViewById(R.id.select_tv_word_count);
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener{
        //ACT点击item的时候 获取list和pos --> 才能获取让下一个页面获取到对应的URL
        void onItemClick(int position,List<SelectBookBeans.CatesBean.BookListBean> mBeanList);
    }
}
