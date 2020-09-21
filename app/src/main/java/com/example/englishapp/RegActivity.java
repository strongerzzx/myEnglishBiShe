package com.example.englishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import beans.RegBeans;
import inerfaces.Api;
import inerfaces.IRegCallback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import presenters.RegPresent;
import presenters.RetrofitMannager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.LogUtil;

public class RegActivity extends AppCompatActivity implements IRegCallback {

    private static final String TAG = "RegActivity";
    private Button mConfirm;
    private EditText mPswd;
    private EditText mAccount;
    private RegPresent mPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        mPresent = RegPresent.getPresent();
        mPresent.regRegCallback(this);

        initView();

    }

    private void initView() {
        mAccount = findViewById(R.id.et_reg_account);
        mPswd = findViewById(R.id.et_reg_pswd);
        mConfirm = findViewById(R.id.btn_reg_confirm);


        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String account = mAccount.getText().toString();
                String pswd = mPswd.getText().toString();

                mPresent.getAccount(account);
                mPresent.getPswd(pswd);

                mPresent.requestReg();

                Intent intent=new Intent(RegActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.unRegRegCallback(this);
    }
}
