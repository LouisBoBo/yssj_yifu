package com.yssj.ui.fragment.contributions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.custom.view.CommonLoadingView;
import com.yssj.custom.view.ContributionsDialog;
import com.yssj.entity.Order;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.infos.FundDetailsActivity;
import com.yssj.ui.activity.infos.LogisticsInfoActivity;
import com.yssj.ui.dialog.ArticlenumberDialog;
import com.yssj.ui.dialog.DeliverGoodsDialog;
import com.yssj.ui.fragment.orderinfo.OrderInfoFragment;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;

import java.util.ArrayList;
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
    private TextView head_content3;
    private ImageView address_img;
    private TextView submit_tv2;
    private TextView submit_tv1;
    private View status_base;
    private View content_base;
    private View spack_base;
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

    private int contribution_status = 999;//????????????
    private String getContribution_flow = "";//????????????
    private String contribution_shop_num = "";//??????
    private String express_company = "";
    private String express_num = "";
    private int getExpress_id = 0;
    private DeliverGoodsDialog dialog;

    private ContributionStatusBean contributionStatusBean;
    private CommonLoadingView loading;

    private List<String> mLocationOption = new ArrayList<>();

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

        loading = new CommonLoadingView(getContext());

        head_img = v.findViewById(R.id.head_image);
        head_title = v.findViewById(R.id.head_title);
        head_content1 = v.findViewById(R.id.content1);
        head_content2 = v.findViewById(R.id.content2);
        head_content3 = v.findViewById(R.id.content3);
        address_img = v.findViewById(R.id.adress_img);
        submit_tv1 = v.findViewById(R.id.submit1);
        submit_tv2 = v.findViewById(R.id.submit2);
        status_base = v.findViewById(R.id.status_base);
        content_base = v.findViewById(R.id.base_content);
        bottom_base = v.findViewById(R.id.base_bottom);
        spack_base = v.findViewById(R.id.base_spack);
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
        jian_text.setText("???????????????");
        photo_text.setText("?????????");
        update_text.setText("????????????");
        piantuan_text.setText("?????????");
        success_text.setText("????????????");
        daba_text.setText("????????????");
        back_text.setText("????????????");
        yongjin_text.setText("????????????");
        end_text.setText("??????");

        update_s.setText("????????????");
        back_s.setText("????????????");
        yongjin_s.setText("????????????");

        status_end.findViewById(R.id.line).setVisibility(View.GONE);

        submit_tv2.setOnClickListener(this);
        submit_tv1.setOnClickListener(this);
        update_s.setOnClickListener(this);
        back_s.setOnClickListener(this);
        yongjin_s.setOnClickListener(this);

//        String status = SharedPreferencesUtil.getStringData(mContext,"contribution_status","");
//        contribution_status = Integer.parseInt(status);

//        initView();

        initContributionStatusData();

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //     * ???????????? status???0??????????????? 1???????????? 2???????????? 3????????????????????? 4??????????????? 5???????????? 6??????????????? 7.?????????????????? 8.???????????????????????????????????????9.??????????????????????????????????????????????????????????????? ??? ????????????????????????10.????????????????????????
    public void initView(){
        status_base.setVisibility(View.GONE);
        content_base.setVisibility(View.GONE);
        bottom_base.setVisibility(View.GONE);

        getCompanyList();

        if(contribution_status == 0){//?????????

            status_base.setVisibility(View.VISIBLE);
            jian_img.setImageResource(R.drawable.status_jian_normal);
            jian_text.setTextColor(Color.parseColor("#ff3f8b"));

        }else if(contribution_status == 1){//????????????

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);
            head_content2.setVisibility(View.VISIBLE);
            head_content3.setVisibility(View.VISIBLE);
            address_img.setVisibility(View.VISIBLE);

            head_title.setText("????????????");
            head_img.setImageResource(R.drawable.shenhe_success_status);
            head_content1.setText("???????????????????????????????????????");
            head_content2.setText(contributionStatusBean.getDataddress().getAddress());
            head_content3.setText("????????????" + contributionStatusBean.getDataddress().getConsignee() + "  " + "???????????????" +contributionStatusBean.getDataddress().getPhone());
            submit_tv2.setText("??????????????????");


        }else if(contribution_status == 2){//????????????

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);

            head_title.setText("????????????");
            head_img.setImageResource(R.drawable.shenhe_jujue_status);
            head_content1.setText("???????????????????????????????????????????????????????????????");


        }else if(contribution_status == 3){//?????????????????????

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);

            head_title.setText("?????????????????????????????????");
            head_content2.setVisibility(View.VISIBLE);
            head_img.setImageResource(R.drawable.shenhe_jujue_status);
            head_content1.setText("???????????????"+contributionStatusBean.getData().getRefuse());
            head_content2.setText("???????????????????????????????????????");
            submit_tv2.setText("???????????????");

        }else if(contribution_status == 4){//?????????

            content_base.setVisibility(View.VISIBLE);
            bottom_base.setVisibility(View.VISIBLE);
            address_img.setVisibility(View.GONE);

            head_title.setText("???????????????");
            submit_tv1.setVisibility(View.VISIBLE);
            head_content1.setVisibility(View.GONE);
            head_content2.setVisibility(View.GONE);
            head_content3.setVisibility(View.GONE);
            address_img.setVisibility(View.GONE);

            submit_tv1.setText("????????????");
            submit_tv2.setText("????????????");
            head_img.setImageResource(R.drawable.shenhezhong_status);
        }else {

            jian_img.setImageResource(R.drawable.status_jian_normal);
            jian_text.setTextColor(Color.parseColor("#ff3f8b"));

            if(getContribution_flow.equals("?????????")){
                if(contribution_status == 5 || contribution_status == 8 || contribution_status == 9 || contribution_status == 18 || contribution_status == 14 || contribution_status == 15 || contribution_status == 16 || contribution_status == 99 || contribution_status == -1){//?????????
                    status_base.setVisibility(View.VISIBLE);

                    switch (contribution_status){
                        case 8:
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
                        case 18:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            photo_img.setImageResource(R.drawable.status_photo_success);
                            update_img.setImageResource(R.drawable.status_update_success);
                            pingtuan_img.setImageResource(R.drawable.status_pingtuan_success);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            photo_text.setTextColor(Color.parseColor("#ff3f8b"));
                            update_text.setTextColor(Color.parseColor("#ff3f8b"));
                            piantuan_text.setTextColor(Color.parseColor("#ff3f8b"));

                            update_s.setVisibility(View.VISIBLE);

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

                            update_s.setVisibility(View.VISIBLE);

                            break;
                        case 15:
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

                            update_s.setVisibility(View.VISIBLE);

                            break;
                        case 16:
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

                            update_s.setVisibility(View.VISIBLE);
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

                            update_s.setVisibility(View.VISIBLE);
                            back_s.setVisibility(View.VISIBLE);
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

                            update_s.setVisibility(View.VISIBLE);
                            back_s.setVisibility(View.VISIBLE);
                            yongjin_s.setVisibility(View.VISIBLE);
                            bottom_base.setVisibility(View.VISIBLE);
                            spack_base.setVisibility(View.VISIBLE);

                            submit_tv2.setText("????????????");

                            break;
                    }
                }
            }else if(getContribution_flow.equals("????????????")){
                if(contribution_status == 5 || contribution_status == 6 || contribution_status == 17 || contribution_status == -1){//????????????

                    status_base.setVisibility(View.VISIBLE);

                    status_update.setVisibility(View.GONE);
                    status_pintuan.setVisibility(View.GONE);
                    status_photo.setVisibility(View.GONE);
                    status_daban.setVisibility(View.GONE);
                    status_yongjin.setVisibility(View.GONE);

                    success_text.setText("????????????");
                    success_img.setImageResource(R.drawable.status_fail_normal);

                    switch (contribution_status){

                        case 6:
                            jian_img.setImageResource(R.drawable.status_jian_normal);
                            success_img.setImageResource(R.drawable.status_fail);

                            jian_text.setTextColor(Color.parseColor("#ff3f8b"));
                            success_text.setTextColor(Color.parseColor("#ff3f8b"));
                            break;
                        case 17:
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


                            bottom_base.setVisibility(View.VISIBLE);
                            back_s.setVisibility(View.VISIBLE);

                            submit_tv2.setText("????????????");
                            break;

                    }

                }
            }else if(getContribution_flow.equals("????????????")){
                if(contribution_status == 5 || contribution_status == 8 || contribution_status == 9 || contribution_status == 18 ||contribution_status == 13 || contribution_status == 12 || contribution_status == -1){//????????????

                    status_base.setVisibility(View.VISIBLE);

                    status_daban.setVisibility(View.GONE);
                    status_yongjin.setVisibility(View.GONE);

                    success_text.setText("????????????");
                    success_img.setImageResource(R.drawable.status_fail_normal);

                    switch (contribution_status){
                        case 8:
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
                        case 18:
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

                            back_s.setVisibility(View.VISIBLE);
                            bottom_base.setVisibility(View.VISIBLE);
                            spack_base.setVisibility(View.VISIBLE);

                            submit_tv2.setText("????????????");
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
        if(view == submit_tv2 && submit_tv2.getText().toString().contains("???????????????")){
            Intent intent = new Intent(mContext, CommitContributionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("contribution_status",contribution_status);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(view == submit_tv2 && submit_tv2.getText().toString().contains("??????????????????")){
            showDeliverDialog();
        }else if(view == submit_tv2 && submit_tv2.getText().toString().contains("????????????")){
            showDeliverDialog();
        }else if(view == submit_tv2 && submit_tv2.getText().toString().contains("????????????")){
            if(contribution_status == 2 || contribution_status == -1){//????????????????????????
//                Intent intent = new Intent(mContext, CommitContributionsActivity.class);
//                startActivity(intent);

                SharedPreferencesUtil.saveStringData(getActivity(), "commonactivityfrom", "contributions");
                SharedPreferencesUtil.saveStringData(getActivity(),"contribution_history_status",String.valueOf(-1));
                Intent intentSign = new Intent(getActivity(), CommonActivity.class);
                getActivity().startActivity(intentSign);
                getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);

            }else
                getActivity().finish();
        }else if(view == submit_tv1 && submit_tv1.getText().toString().contains("????????????")){

            if(express_company.length()>0 && express_num.length()>0)
            {
                Intent intent = new Intent(mContext, ContributionsLogisticsActivity.class);
                Bundle bundleSimple = new Bundle();
                bundleSimple.putString("name", express_company);
                bundleSimple.putString("num", express_num);
                intent.putExtras(bundleSimple);
                startActivity(intent);
            }
        }else if(view == update_s){//????????????

            showArticleDialog();

        }else if(view == back_s){//????????????
            if(express_company.length()>0 && express_num.length()>0) {
                Intent intent = new Intent(mContext, ContributionsLogisticsActivity.class);
                Bundle bundleSimple = new Bundle();
                bundleSimple.putString("name", express_company);
                bundleSimple.putString("num", express_num);
                intent.putExtras(bundleSimple);
                startActivity(intent);
            }
        }else if(view == yongjin_s){//????????????
            Intent intent = new Intent(mContext, FundDetailsActivity.class);
            startActivity(intent);
        }
    }

    public FragmentManager getSupportFragmentManager() {
        return this.getSupportFragmentManager();
    }

    //????????????
    public void showDeliverDialog(){
        dialog = new DeliverGoodsDialog(mContext);
        dialog.show();

        dialog.setOnItemClick(new DeliverGoodsDialog.OnItemClick() {
            @Override
            public void click(String address, String num, String com) {
                if(num.equals("")){
                    ToastUtil.showShortText2("?????????????????????");
                    return;
                }
                if(com.equals("")){
                    ToastUtil.showShortText2("?????????????????????");
                    return;
                }

                if(contribution_status == 4)//????????????
                {
                    echangeData(com , num);
                }else
                    submitData(com , num);
            }
        });

        dialog.setmSelectClick(new DeliverGoodsDialog.SelectClick() {
            @Override
            public void click(TextView com) {
                showPickView(com);
            }
        });
    }

    //????????????
    public void showArticleDialog(){
        ArticlenumberDialog dialog = new ArticlenumberDialog(mContext,contribution_shop_num);
        dialog.show();
    }

    //??????????????????
    public void submitData(String com ,String num){

        String id = SharedPreferencesUtil.getStringData(getActivity(),"id","");

        HashMap<String, String> pairsMap = new HashMap<>();
        pairsMap.put("expressName",com);
        pairsMap.put("expressNum",num);
        pairsMap.put("id",id);
        pairsMap.put("company",getLogisticType(com));

        YConn.httpPost(getActivity(), YUrl.SUPPLYMATERIAL_EXPRESS_ACTION, pairsMap, new HttpListener<ContributionStatusBean>() {
            @Override
            public void onSuccess(ContributionStatusBean result) {

                ToastUtil.showShortText2("????????????");

                submit_tv1.setVisibility(View.VISIBLE);
                head_content1.setVisibility(View.GONE);
                head_content2.setVisibility(View.GONE);
                head_content3.setVisibility(View.GONE);
                address_img.setVisibility(View.GONE);

                submit_tv1.setText("????????????");
                submit_tv2.setText("????????????");

                head_img.setImageResource(R.drawable.shenhezhong_status);
                head_title.setText("???????????????");

                initContributionStatusData();
            }

            @Override
            public void onError() {

            }
        });
    }

    //????????????
    public void echangeData(String com ,String num){
        String id = SharedPreferencesUtil.getStringData(getActivity(),"id","");

        HashMap<String, String> pairsMap = new HashMap<>();
        pairsMap.put("expressName",com);
        pairsMap.put("expressNum",num);
        pairsMap.put("id",String.valueOf(getExpress_id));
        pairsMap.put("company",getLogisticType(com));

        YConn.httpPost(getActivity(), YUrl.SUPPLYMATERIAL_UPDATEEXPRESS, pairsMap, new HttpListener<ContributionStatusBean>() {
            @Override
            public void onSuccess(ContributionStatusBean result) {
                ToastUtil.showShortText2("????????????");
            }

            @Override
            public void onError() {

            }
        });
    }

    //??????????????????
    public void initContributionStatusData(){

        HashMap<String, String> pairsMap = new HashMap<>();

        loading.show();
        YConn.httpPost(getActivity(), YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_FINDSUPPLY, pairsMap, new HttpListener<ContributionStatusBean>() {

            @Override
            public void onSuccess(ContributionStatusBean result) {
                loading.dismiss();

                SharedPreferencesUtil.saveStringData(getActivity(), "id", result.getData().getId()+"");

                contributionStatusBean = result;
                if(result.getData() != null){
                    contribution_status = result.getData().getStatus();
                    contribution_shop_num = result.getData().getShop_num();
//                    contribution_status = 9;//?????????
                }

                if(result.getSupplyMaterialExpress() != null){
                    express_company = result.getSupplyMaterialExpress().getExpress_company();
                    express_num = result.getSupplyMaterialExpress().getExpress_num();
                    getExpress_id = result.getSupplyMaterialExpress().getId();
                }

                getContribution_flow = result.getFlow();
//                getContribution_flow = "?????????";//?????????

                initView();
            }

            @Override
            public void onError() {
                loading.dismiss();
            }
        });


    }



    //?????????????????????
    public void showPickView(final TextView companytv){
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String company = mLocationOption.get(options1);
                companytv.setText(company);
                dialog.show();
                //??????????????????????????????
            }
        })
                .setTitleText("??????????????????")
                .setContentTextSize(16)
                .build();

        pvOptions.setPicker(mLocationOption);
        pvOptions.show();

    }

    //??????????????????
    public void getCompanyList(){
        mLocationOption.clear();

        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("??????????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("EMS");
        mLocationOption.add("????????????");
        mLocationOption.add("??????????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("??????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("EWE????????????");
        mLocationOption.add("????????????");
        mLocationOption.add("????????????");
    }

    //????????????????????????
    public String getLogisticType(String name){

        String logistic_type = "";

        if(name.equals("????????????")){
            logistic_type = "yuantong";
        }else if(name.contains("????????????")){
            logistic_type = "yunda";
        }else if(name.contains("????????????")){
            logistic_type = "zhongtong";
        }else if(name.contains("????????????")){
            logistic_type = "shentong";
        }else if(name.contains("????????????")){
            logistic_type = "jtexpress";
        }else if(name.contains("??????????????????")){
            logistic_type = "youzhengguonei";
        }else if(name.contains("????????????")){
            logistic_type = "shunfeng";
        }else if(name.contains("EMS")){
            logistic_type = "ems";
        }else if(name.contains("????????????")){
            logistic_type = "jd";
        }else if(name.contains("??????????????????")){
            logistic_type = "youzhengbk";
        }else if(name.contains("????????????")){
            logistic_type = "debangkuaidi";
        }else if(name.contains("??????")){
            logistic_type = "debangwuliu";
        }else if(name.contains("????????????")){
            logistic_type = "huitongkuaidi";
        }else if(name.contains("????????????")){
            logistic_type = "fengwang";
        }else if(name.contains("????????????")){
            logistic_type = "zhongtongguoji";
        }else if(name.contains("????????????")){
            logistic_type = "zhongtongkuaiyun";
        }else if(name.contains("????????????")){
            logistic_type = "koreapostcn";
        }else if(name.contains("????????????")){
            logistic_type = "annengwuliu";
        }else if(name.contains("????????????")){
            logistic_type = "jingdongkuaiyun";
        }else if(name.contains("EWE????????????")){
            logistic_type = "ewe";
        }else if(name.contains("????????????")){
            logistic_type = "yundakuaiyun";
        }else if(name.contains("????????????")){
            logistic_type = "youshuwuliu";
        }

        return logistic_type;
    }
}
