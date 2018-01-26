package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.widget.RoundedCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TuiFangApplyActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.order_userImg)
    RoundedCornerImageView orderUserImg;
    @BindView(R.id.order_userName)
    TextView orderUserName;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_tag)
    View orderTag;
    @BindView(R.id.order_userinfo)
    RelativeLayout orderUserinfo;
    @BindView(R.id.order_roomImg)
    ImageView orderRoomImg;
    @BindView(R.id.order_room_title)
    TextView orderRoomTitle;
    @BindView(R.id.order_room_description)
    TextView orderRoomDescription;
    @BindView(R.id.ruzhu_time)
    TextView ruzhuTime;
    @BindView(R.id.leave_time)
    TextView leaveTime;
    @BindView(R.id.location_address)
    TextView locationAddress;
    @BindView(R.id.total_day)
    TextView totalDay;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.submit)
    TextView submit;
    private String tokenId;
    private String order_id;

    @Override
    protected void processLogic() {
        MinSuApi.applyTuifangPage(this, 0x001, tokenId, Integer.parseInt(order_id), callBack);
    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);

        order_id = getIntent().getStringExtra("order_id");
        toolbarTitle.setText("退房申请");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order_cancel);
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String title = jsonObject1.getString("title");
                            String house_info = jsonObject1.getString("house_info");
                            String check_time = jsonObject1.getString("check_time");
                            String leave_time = jsonObject1.getString("leave_time");
                            int days = jsonObject1.getInt("days");
                            String house_img = jsonObject1.getString("house_img");
                            String province = jsonObject1.getString("province");
                            String city = jsonObject1.getString("city");
                            String district = jsonObject1.getString("district");
                            String town = jsonObject1.getString("town");
                            String order_sn = jsonObject1.getString("order_sn");
                            orderNumber.setText(order_sn);
                            orderRoomTitle.setText(title);
                            ruzhuTime.setText(check_time);
                            leaveTime.setText(leave_time);
                            orderRoomDescription.setText(house_info);
                            totalDay.setText("共" + days + "天");
                            locationAddress.setText(city + " " + district + " " + town);
                            Glide.with(TuiFangApplyActivity.this)
                                    .load(Constant.BASE2_URL + house_img)
                                    .into(orderRoomImg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
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
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                MinSuApi.submitTuifang(TuiFangApplyActivity.this, 0x002, tokenId, Integer.parseInt(order_id), "不想订了", "行程有变", callBack);
                break;
        }
    }
}
