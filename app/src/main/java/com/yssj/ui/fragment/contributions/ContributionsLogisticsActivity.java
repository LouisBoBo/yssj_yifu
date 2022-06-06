package com.yssj.ui.fragment.contributions;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.AppManager;
import com.yssj.eventbus.MessageEvent;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.base.BasicActivity;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContributionsLogisticsActivity extends BasicActivity implements View.OnClickListener {

    private View img_back;
    private TextView log_name;
    private TextView log_num;
    private RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    private Context mcontext;

    private String post_name;
    private String post_num;

    //收到消息回主UI刷新界面
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123:
                    rvTrace.setAdapter(adapter);
                    break;
            }

            super.handleMessage(msg);
        }
    };

    //获取物流信息回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEven(LogistticsBean bean) {
        initData(bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mcontext = this;
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_contribution_logis);

        Bundle bundle = this.getIntent().getExtras();
        post_name = bundle.getString("name");
        post_num = bundle.getString("num");

        initview();

        initLogistData();

        //查询物流信息
//        ExpressUtils.main(post_num,getLogisticType(post_name));
    }

    public void initview(){

        EventBus.getDefault().register(this);

        img_back = findViewById(R.id.img_back);
        log_name = findViewById(R.id.logic_name);
        log_num = findViewById(R.id.logic_num);
        rvTrace = (RecyclerView) findViewById(R.id.rvTrace);
        rvTrace.setLayoutManager(new LinearLayoutManager(mcontext));
        adapter = new TraceListAdapter(mcontext, traceList);

        log_name.setText("物流公司：" + post_name);
        log_num.setText("快递单号：" + post_num);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void initLogistData(){

        HashMap<String, String> pairsMap = new HashMap<>();
        pairsMap.put("nu",post_num);

        YConn.httpPost(mcontext, YUrl.SUPPLYMATERIAL_EXPQUERY, pairsMap, new HttpListener<SupplyLogistBean>() {
            @Override
            public void onSuccess(SupplyLogistBean result) {
                if(result != null && result.getData().size()>0){
                    setData(result.getData().get(0).getLastResult().getData());
                }
            }

            @Override
            public void onError() {

            }
        });
    }
    public void setData(List<SupplyLogistBean.DataDTOX.LastResultDTO.DataDTO> data){
        for (SupplyLogistBean.DataDTOX.LastResultDTO.DataDTO datum : data) {
            Trace trace = new Trace();
            trace.setAcceptTime(datum.getFtime());
            trace.setAcceptStation(datum.getContext());
            traceList.add(trace);
        }

        myHandler.sendEmptyMessage(123);
    }

    public void initData(LogistticsBean bean){

        for (LogistticsBean.DataDTO datum : bean.getData()) {
            Trace trace = new Trace();
            trace.setAcceptTime(datum.getFtime());
            trace.setAcceptStation(datum.getContext());
            traceList.add(trace);
        }

        myHandler.sendEmptyMessage(123);
    }

    //获取物流公司编码
    public String getLogisticType(String name){

        String logistic_type = "";
        if(name.contains("圆通")){
            logistic_type = "yuantong";
        }else if(name.contains("德邦")){
            logistic_type = "debangwuliu";
        }else if(name.contains("ems")){
            logistic_type = "ems";
        }else if(name.contains("申通")){
            logistic_type = "shentong";
        }else if(name.contains("顺丰")){
            logistic_type = "shunfeng";
        }else if(name.contains("天天快递")){
            logistic_type = "tiantian";
        }else if(name.contains("优速物流")){
            logistic_type = "youshuwuliu";
        }else if(name.contains("韵达快运")){
            logistic_type = "yunda";
        }else if(name.contains("中通速递")){
            logistic_type = "zhongtong";
        }

        return logistic_type;
    }
}
