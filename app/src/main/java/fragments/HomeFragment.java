package fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.englishapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import inerfaces.IHomeCallback;
import presenters.HomePresent;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements IHomeCallback {

    private ImageView mRefresh;
    private HomePresent mPresent;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mPresent = HomePresent.getPresent();

        mPresent.regesiterHomeCallback(this);

        mPresent.getSingleWords();

        initView(rootView);
        initEvent();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initEvent() {
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击了刷新", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initView(View rootView) {
        mRefresh = rootView.findViewById(R.id.home_refresh_iv);
    }

    @Override
    public void showSingleWords() {



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.unRegesiterHomeCallback(this);
    }
}
