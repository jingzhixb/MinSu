package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.RegexUtils;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBankCardActivity extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_card_name)
    EditText etCardName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_idcard)
    EditText etIdcard;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.quick_bind)
    TextView quickBind;
    private String tokenId;
    private MyCountDownTimer myCountDownTimer;
    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("添加银行卡");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhone = etPhone.getText().toString();
                if (RegexUtils.isMobileExact(mPhone)) {
                    MinSuApi.getSmsCode(0x002, mPhone, 3, callBack);
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                    myCountDownTimer.start();
                } else {
                    ToastManager.show("您输入的手机号格式不正确");
                }
            }
        });
        quickBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mCardNumber = etCardNumber.getText().toString();
                String mCardName = etCardName.getText().toString();
                String mName = etName.getText().toString();
                String mIdCard = etIdcard.getText().toString();
                String mPhone = etPhone.getText().toString();
                String mYanzhengma = etYanzhengma.getText().toString();
                MinSuApi.addbankCard(AddBankCardActivity.this, 0x001, tokenId, mYanzhengma, mCardNumber, mCardName, mName, mIdCard, mPhone, callBack);
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_add_bank_card);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                          ToastManager.show(msg);
                          finish();
                        } else if (code == 211) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            etPhone.removeTextChangedListener(mTextWatcher);
            tvSend.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
            tvSend.setClickable(false);
            tvSend.setText(l / 1000 + "s");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tvSend.setText("重新获取");
            //设置可点击
            etPhone.addTextChangedListener(mTextWatcher);
            tvSend.setClickable(true);
            tvSend.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
        }
    }
    //监听输入的手机号
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 11) {//改变验证码背景颜色
                tvSend.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
                tvSend.setEnabled(true);
                tvSend.setClickable(true);
            } else {
                tvSend.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
                tvSend.setClickable(false);
            }
        }
    };
}
