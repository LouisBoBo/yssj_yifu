package com.yssj.ui.fragment.contributions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.custom.view.ContributionsDialog;
import com.yssj.entity.Order;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.infos.FundDetailsActivity;
import com.yssj.ui.activity.infos.LogisticsInfoActivity;
import com.yssj.ui.dialog.ArticlenumberDialog;
import com.yssj.ui.dialog.DeliverGoodsDialog;
import com.yssj.ui.fragment.orderinfo.OrderInfoFragment;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ContributionStatusFragment extends Fragment implements View.OnClickListener{

    private static Context mContext;
    private List<HashMap<String, Object>> mListDatas;
    private static String mJumpFrom;
    private ImageView head_img;
    private TextView head_title;
    private TextView head_content1;
    private TextView head_content2;
    private ImageView address_img;
    private TextView submit_tv2;
    private TextView submit_tv1;
    private View status_base;
    private View content_base;
    private View bottom_base;
    private View status_jian;
    private View status_photo;
    private View status_update;
    private View status_pintuan;
    private View status_success;
    private View status_daban;
    private View status_back;
    private View status_yongjin;
    private View status_end;

    private ImageView jian_img;
    private ImageView photo_img;
    private ImageView update_img;
    private ImageView pingtuan_img;
    private ImageView success_img;
    private ImageView daban_img;
    private ImageView yongjin_img;
    private ImageView back_img;
    private ImageView end_img;

    private TextView jian_text;
    private TextView photo_text;
    private TextView update_text;
    private TextView piantuan_text;
    private TextView success_text;
    private TextView daba_text;
    private TextView yongjin_text;
    private TextView back_text;
    private TextView end_text;

    private TextView jian_s;
    private TextView photo_s;
    private TextView update_s;
    private TextView pintuan_s;
    private TextView success_s;
    private TextView daban_s;
    private TextView yongjin_s;
    private TextView back_s;
    private TextView end_s;

    private int contribution_status = 999;
    private String getContribution_flow = "";
    private String express_company = "";
    private String express_num = "";
    private int getExpress_id = 0;

    public static ContributionStatusFragment newInstances(Context context, String jumpFrom) {
        mJumpFrom = jumpFrom;
        mContext = context;

        ContributionStatusFragment fragment = new ContributionStatusFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_contributition_status, container, false);

        View headview = v.findViewById(R.id.ll_title);
        headview.setVisibility(View.GONE);

//        String status = SharedPreferencesUtil.getStringData(mContext,"contirbution_status","");
//        contribution_status = Integer.parseInt(status);

        head_img = v.findViewById(R.id.head_image);
        head_title = v.findViewById(R.id.head_title);
        head_content1 = v.findViewById(R.id.content1);
        head_content2 = v.findViewById(R.id.content2);
        address_img = v.findViewById(R.id.adress_img);
        submit_tv1 = v.findViewById(R.id.submit1);
        submit_tv2 = v.findViewById(R.id.submit2);
        status_base = v.findViewById(R.id.status_base);
        content_base = v.findViewById(R.id.base_content);
        bottom_base = v.findViewById(R.id.base_bottom);
        status_jian = v.findViewById(R.id.status_jian);
        status_update = v.findViewById(R.id.status_update);
        status_pintuan = v.findViewById(R.id.status_pintuan);
        status_success = v.findViewById(R.id.status_pintuan_success);
        status_daban = v.findViewById(R.id.status_daban);
        status_back = v.findViewById(R.id.status_back);
        status_yongjin = v.findViewById(R.id.stauts_yongjin);
        status_end = v.findViewById(R.id.status_end);
        status_photo = v.findViewById(R.id.status_photo);

        jian_img = status_jian.findViewById(R.id.item_img);
        photo_img = status_photo.findViewById(R.id.item_img);
        update_img = status_update.findViewById(R.id.item_img);
        pingtuan_img = status_pintuan.findViewById(R.id.item_img);
        success_img = status_success.findViewById(R.id.item_img);
        daban_img = status_daban.findViewById(R.id.item_img);
        back_img = status_back.findViewById(R.id.item_img);
        yongjin_img = status_yongjin.findViewById(R.id.item_img);
        end_img = status_end.findViewById(R.id.item_img);

        jian_text = status_jian.findViewById(R.id.item_content);
        photo_text = status_photo.findViewById(R.id.item_content);
        update_text = status_update.findViewById(R.id.item_content);
        piantuan_text = status_pintuan.findViewById(R.id.item_content);
        success_text = status_success.findViewById(R.id.item_content);
        daba_text = status_daban.findViewById(R.id.item_content);
        back_text = status_back.findViewById(R.id.item_content);
        yongjin_text = status_yongjin.findViewById(R.id.item_content);
        end_text = status_end.findViewById(R.id.item_content);

        jian_s = status_jian.findViewById(R.id.item_status);
        photo_s = status_photo.findViewById(R.id.item_status);
        update_s = status_update.findViewById(R.id.item_status);
        pintuan_s = status_pintuan.findViewById(R.id.item_status);
        success_s = status_success.findViewById(R.id.item_status);
        daban_s = status_daban.findViewById(R.id.item_status);
        back_s = status_back.findViewById(R.id.item_status);
        yongjin_s = status_yongjin.findViewById(R.id.item_status);
        end_s = status_end.findViewById(R.id.item_status);

        jian_img.setImageResource(R.drawable.status_jian_normal);
        photo_img.setImageResource(R.drawable.status_photo_normal);
        update_img.setImageResource(R.drawable.status_update_normal);
        pingtuan_img.setImageResource(R.drawable.status_pingtuan_normal);
        success_img.setImageResource(R.drawable.status_success_normal);
        daban_img.setImageResource(R.drawable.status_daban_normal);
        back_img.setImageResource(R.drawable.status_back_normal);
        yongjin_img.setImageResource(R.drawable.status_yongjin_normal);
        end_img.setImageResource(R.drawable.status_end_normal);

        jian_text.setTextColor(Color.parseColor("#ff3f8b"));
        jian_text.setText("设计师验衣");
        photo_text.setText("拍摄中");
        update_text.setText("上传平台");
        piantuan_text.setText("拼团中");
        success_text.setText("拼团成功");
        daba_text.setText("工厂打板");
        back_text.setText("返还样衣");
        yongjin_text.setText("结算佣金");
        end_text.setText("结束");

        update_s.setText("查看货号");
        back_s.setText("查看物流");
        yongjin_s.setText("查看明细");

        status_end.findViewById(R.id.line).setVisibility(View.GONE);

        submit_tv2.setOnClickListener(this);
        submit_tv1.setOnClickListener(this);
        update_s.setOnClickListener(this);
        back_s.setOnClickListener(this);
        yongjin_s.setOnClickListener(this);

//        initView();

        initContributionStatusData();

        return v;

    }

    //     * 供款状态 status：0已申请供货 1审核通过 2整体拒绝 3图片不合格拒绝 4，样衣发货 5验衣通过 6验衣不通过 7.核验货号通过 8.已确认货号信息（生产工艺）9.上架（生产工艺确认工价单、上传样衣贴货号图 与 上传工艺单完成）10.待上架（上传中）
    public void initView(){
        status_base.setVisibility(View.GONE);
        content_base.setVisibility(View.GONE);
        bottom_base.setVisibility(View.GONE);

        if(contribution_status == 0){//审核中
//            content_base.setVisibility(View.VISIBLE);
//            bottom_base.setVisibility(View.VISIBLE);
//
//            head_title.setText("样衣审核中");
//            head_img.setImageResource(R.drawable.shenhezhong_status);
//            head_content1.setText("审核时间一般为一个工作日");

            status_base.setVisibility(View.VISIBLE);
            jian_img.setImageResource(R.drawable.status_jian_normal);
            jian_text.setTextColor(Color.parseColor("#ff3f8b"));

        }else if(contribution_status == 1){//审核成功

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);

            head_title.setText("审核成功");
            head_content2.setVisibility(View.VISIBLE);
            head_img.setImageResource(R.drawable.shenhe_success_status);
            head_content1.setText("请将您的样衣快递到以下地址");
            head_content2.setText("深圳市 南区区登良路恒裕中心B座408");
            address_img.setVisibility(View.VISIBLE);
            submit_tv2.setText("立即去发样衣");


        }else if(contribution_status == 2){//审核拒绝

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);
            head_title.setText("审核拒绝");
            head_img.setImageResource(R.drawable.shenhe_jujue_status);
            head_content1.setText("您的申请不通过，您可以更换别的样衣再次申请");


        }else if(contribution_status == 3){//样衣图片不合格

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);

            head_title.setText("您的样衣图片上传不合格");
            head_content2.setVisibility(View.VISIBLE);
            head_img.setImageResource(R.drawable.shenhe_jujue_status);
            head_content1.setText("具体原因：面料和辅料图拍糊了，请重新上传");
            head_content2.setText("请您修改不合格图片再次上传");
            submit_tv2.setText("修改样衣图");

        }else if(contribution_status == 4){//已发货

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);

            head_title.setText("物流运输中");
            submit_tv1.setVisibility(View.VISIBLE);
            head_content1.setVisibility(View.GONE);
            head_content2.setVisibility(View.GONE);
            submit_tv1.setText("查看物流");
            submit_tv2.setText("修改物流");
            head_img.setImageResource(R.drawable.shenhezhong_status);
        }else {
            if(getContribution_flow.equals("全成功")){
                if(contribution_status == 0 || contribution_status == 5 || contribution_status == 9 || contribution_status == 11 || contribution_status == 14 || contribution_status == 98 || contribution_status == 12 || contribution_status == 99 || contribution_status == -1){//全成功
                    status_base.setVisibility(View.VISIBLE);

                    switch (contribution_status){
                        case 0:
                            jian_img.setImageResource(R.drawable.status_jian_normal);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 5:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 9:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));

                            update_s.setVisibility(View.VISIBLE);
                            break;
                        case 11:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));

                            break;
                        case 14:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));

                            break;
                        case 98:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_success);
                            daban_img.setImageResource(R.drawable.status_daban_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            daba_text.setTextColor(Color.parseColor("#ff3f8b"));

                            break;
                        case 12:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_success);
                            daban_img.setImageResource(R.drawable.status_daban_success);
                            back_img.setImageResource(R.drawable.status_back_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            daba_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));

                            back_s.setVisibility(View.VISIBLE);

                            break;
                        case 99:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_success);
                            daban_img.setImageResource(R.drawable.status_daban_success);
                            back_img.setImageResource(R.drawable.status_back_success);
                            yongjin_img.setImageResource(R.drawable.status_yongjin_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            daba_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));
                            yongjin_text.setTextColor(Color.parseColor("#ff3f8b"));

                            yongjin_s.setVisibility(View.VISIBLE);
                            break;

                        case -1:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_success);
                            daban_img.setImageResource(R.drawable.status_daban_success);
                            back_img.setImageResource(R.drawable.status_back_success);
                            yongjin_img.setImageResource(R.drawable.status_yongjin_success);
                            end_img.setImageResource(R.drawable.status_end_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            daba_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));
                            yongjin_text.setTextColor(Color.parseColor("#ff3f8b"));
                            end_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                    }
                }
            }else if(getContribution_flow.equals("验衣失败")){
                if(contribution_status == 0 || contribution_status == 6 || contribution_status == 12 || contribution_status == -1){//验衣失败

                    status_base.setVisibility(View.VISIBLE);

                    status_update.setVisibility(View.GONE);
                    status_pintuan.setVisibility(View.GONE);
                    status_photo.setVisibility(View.GONE);
                    status_daban.setVisibility(View.GONE);
                    status_yongjin.setVisibility(View.GONE);

                    success_text.setText("验衣失败");
                    success_img.setImageResource(R.drawable.status_fail_normal);

                    switch (contribution_status){
                        case 0:
                            jian_img.setImageResource(R.drawable.status_jian_normal);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 6:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            success_img.setImageResource(R.drawable.status_fail);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 12:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            success_img.setImageResource(R.drawable.status_fail);
                            back_img.setImageResource(R.drawable.status_back_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));

                            back_s.setVisibility(View.VISIBLE);
                            break;
                        case -1:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            success_img.setImageResource(R.drawable.status_fail);
                            back_img.setImageResource(R.drawable.status_back_success);
                            end_img.setImageResource(R.drawable.status_end_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));
                            end_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;

                    }

                }
            }else if(getContribution_flow.equals("拼单失败")){
                if(contribution_status == 0 || contribution_status == 5 || contribution_status == 9 || contribution_status == 11 ||contribution_status == 13 || contribution_status == 12 || contribution_status == -1){//拼团失败

                    status_base.setVisibility(View.VISIBLE);

                    status_daban.setVisibility(View.GONE);
                    status_yongjin.setVisibility(View.GONE);

                    success_text.setText("拼团失败");
                    success_img.setImageResource(R.drawable.status_fail_normal);

                    switch (contribution_status){
                        case 0:
                            jian_img.setImageResource(R.drawable.status_jian_normal);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));

                            break;
                        case 5:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 9:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));

                            update_s.setVisibility(View.VISIBLE);
                            break;
                        case 11:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 13:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_fail);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));

                            break;

                        case 12:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_fail);
                            back_img.setImageResource(R.drawable.status_back_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));

                            back_s.setVisibility(View.VISIBLE);

                            break;

                        case -1:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);
                            success_img.setImageResource(R.drawable.status_fail);
                            back_img.setImageResource(R.drawable.status_back_success);
                            end_img.setImageResource(R.drawable.status_end_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            back_text.setTextColor(Color.parseColor("#ff3f8b"));
                            end_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                    }
                }
            }

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(final View view) {
        if(view == submit_tv2 && submit_tv2.getText().toString().contains("修改样衣图")){
            Intent intent = new Intent(mContext, CommitContributionsActivity.class);
            startActivity(intent);
        }else if(view == submit_tv2 && submit_tv2.getText().toString().contains("立即去发样衣")){
            showDeliverDialog();
        }else if(view == submit_tv2 && submit_tv2.getText().toString().contains("修改物流")){
            showDeliverDialog();
        }else if(view == submit_tv2 && submit_tv2.getText().toString().contains("我知道了")){
            if(contribution_status == 2){//审核拒绝重新审核
                Intent intent = new Intent(mContext, CommitContributionsActivity.class);
                startActivity(intent);
            }else
                getActivity().finish();
        }else if(view == submit_tv1 && submit_tv1.getText().toString().contains("查看物流")){

            if(express_company.length()>0 && express_num.length()>0) {
                Intent intent = new Intent(mContext, ContributionsLogisticsActivity.class);
                Bundle bundleSimple = new Bundle();
                bundleSimple.putString("name", express_company);
                bundleSimple.putString("num", express_num);
                intent.putExtras(bundleSimple);
                startActivity(intent);
            }
        }else if(view == update_s){//查看货号

            showArticleDialog();

        }else if(view == back_s){//查看物流
            if(express_company.length()>0 && express_num.length()>0) {
                Intent intent = new Intent(mContext, ContributionsLogisticsActivity.class);
                Bundle bundleSimple = new Bundle();
                bundleSimple.putString("name", express_company);
                bundleSimple.putString("num", express_num);
                intent.putExtras(bundleSimple);
                startActivity(intent);
            }
        }else if(view == yongjin_s){//查看明细
            Intent intent = new Intent(mContext, FundDetailsActivity.class);
            startActivity(intent);
        }
    }

    public FragmentManager getSupportFragmentManager() {
        return this.getSupportFragmentManager();
    }

    //输入物流
    public void showDeliverDialog(){
        DeliverGoodsDialog dialog = new DeliverGoodsDialog(mContext);
        dialog.show();

        dialog.setOnItemClick(new DeliverGoodsDialog.OnItemClick() {
            @Override
            public void click(String address, String num, String com) {
                if(num.equals("")){
                    ToastUtil.showShortText2("请输入物流单号");
                    return;
                }
                if(com.equals("")){
                    ToastUtil.showShortText2("请输入物流公司");
                    return;
                }

                if(contribution_status == 4)//修改物流
                {
                    echangeData(com , num);
                }else
                    submitData(com , num);
            }
        });
    }

    //查看货号
    public void showArticleDialog(){
        ArticlenumberDialog dialog = new ArticlenumberDialog(mContext,express_num);
        dialog.show();
    }

    //添加物流信息
    public void submitData(String com ,String num){

        String id = SharedPreferencesUtil.getStringData(getActivity(),"id","");

        HashMap<String, String> pairsMap = new HashMap<>();
        pairsMap.put("expressName",com);
        pairsMap.put("expressNum",num);
        pairsMap.put("id",id);

        submit_tv1.setVisibility(View.VISIBLE);
        head_content1.setVisibility(View.GONE);
        head_content2.setVisibility(View.GONE);

        submit_tv1.setText("查看物流");
        submit_tv2.setText("修改物流");

        head_img.setImageResource(R.drawable.shenhezhong_status);
        head_title.setText("物流运输中");

        YConn.httpPost(getActivity(), YUrl.SUPPLYMATERIAL_EXPRESS_ACTION, pairsMap, new HttpListener<ContributionStatusBean>() {
            @Override
            public void onSuccess(ContributionStatusBean result) {

                ToastUtil.showShortText2("操作成功");

                submit_tv1.setVisibility(View.VISIBLE);
                head_content1.setVisibility(View.GONE);
                head_content2.setVisibility(View.GONE);

                submit_tv1.setText("查看物流");
                submit_tv2.setText("修改物流");

                head_img.setImageResource(R.drawable.shenhezhong_status);
                head_title.setText("物流运输中");

                initContributionStatusData();
            }

            @Override
            public void onError() {

            }
        });
    }

    //修改物流
    public void echangeData(String com ,String num){
        String id = SharedPreferencesUtil.getStringData(getActivity(),"id","");

        HashMap<String, String> pairsMap = new HashMap<>();
        pairsMap.put("expressName",com);
        pairsMap.put("expressNum",num);
        pairsMap.put("id",String.valueOf(getExpress_id));

        YConn.httpPost(getActivity(), YUrl.SUPPLYMATERIAL_UPDATEEXPRESS, pairsMap, new HttpListener<ContributionStatusBean>() {
            @Override
            public void onSuccess(ContributionStatusBean result) {
                ToastUtil.showShortText2("修改成功");
            }

            @Override
            public void onError() {

            }
        });
    }

    //获取供款状态
    public void initContributionStatusData(){

        HashMap<String, String> pairsMap = new HashMap<>();

        YConn.httpPost(getActivity(), YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_FINDSUPPLY, pairsMap, new HttpListener<ContributionStatusBean>() {
            @Override
            public void onSuccess(ContributionStatusBean result) {
                SharedPreferencesUtil.saveStringData(getActivity(), "id", result.getData().getId()+"");

                if(result.getData() != null){
                    contribution_status = result.getData().getStatus();
                }

                if(result.getSupplyMaterialExpress() != null){
                    express_company = result.getSupplyMaterialExpress().getExpress_company();
                    express_num = result.getSupplyMaterialExpress().getExpress_num();
                    getExpress_id = result.getSupplyMaterialExpress().getId();
                }

                getContribution_flow = result.getFlow();

                initView();
            }

            @Override
            public void onError() {

            }
        });
    }
}
