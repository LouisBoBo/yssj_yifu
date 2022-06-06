package com.yssj.ui.fragment.contributions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yssj.activity.R;
import com.yssj.app.AppManager;
import com.yssj.eventbus.MessageEvent;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.MainFragment;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.base.BasicActivity;

import org.greenrobot.eventbus.EventBus;

public class ContributionStatusActivity extends BasicActivity implements View.OnClickListener{
    private View img_back;
    private TextView tv_submit;
    private View base_view;
    private View bottom_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_contributition_status);

        initview();
    }

    public void initview(){
        base_view = findViewById(R.id.base_content);
        bottom_view = findViewById(R.id.base_bottom);
        img_back = findViewById(R.id.img_back);
        tv_submit = findViewById(R.id.submit2);

        base_view.setVisibility(View.VISIBLE);
        bottom_view.setVisibility(View.VISIBLE);
        tv_submit.setVisibility(View.VISIBLE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setMessage("审核返回");
                EventBus.getDefault().post(messageEvent);

                onBackPressed();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setMessage("审核返回");
                EventBus.getDefault().post(messageEvent);

                onBackPressed();
            }
        });
    }
}
