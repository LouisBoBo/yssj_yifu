package com.yssj.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.entity.BaseData;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.SignDrawalLimitActivity;
import com.yssj.ui.activity.main.ForceLookActivity;
import com.yssj.ui.activity.main.SignActiveShopActivity;
import com.yssj.ui.fragment.circles.SignListAdapter;

import java.util.HashMap;

/**
 * Created by Administrator on 2020/3/14 0014.
 */

public class SignCompleteDialogUtil {

    public interface DoSingBackToSignListener {
        void signCompleteRefresh();
    }


    public static void SignIng(final Context mContext, final DoSingBackToSignListener doSingBackToSignListener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("index_id", SignListAdapter.signIndex);
        map.put("day", EntityFactory.signDay);
        map.put("share", "false");
        map.put("mac", CheckStrUtil.getMac(mContext));
        SharedPreferencesUtil.saveBooleanData(mContext, "signDATAneedRefresh", true);


        YConn.httpPost(mContext, YUrl.SIGN_QIANDAO, map, new HttpListener<BaseData>() {
            @Override
            public void onSuccess(BaseData result) {

                if (result.getStatus().equals("1")) {
                    doSingBackToSignListener.signCompleteRefresh();
                    if (result.getIsNewbie01() == 1) {
                        firstClickInGoToZP(mContext);
                        return;
                    }
                    showSignComplete(mContext, SignListAdapter.jiangliValue);
                }

            }

            @Override
            public void onError() {

            }
        });


    }


    public static void showSignComplete(Context mContext, String money) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        final Dialog dialog = new Dialog(mContext, R.style.invate_dialog_style);
        View view = mInflater.inflate(R.layout.dialog_sign_complete_common, null);


        ImageView iv_close = view.findViewById(R.id.iv_close);
        TextView tv = view.findViewById(R.id.tv);
        tv.setText("???????????????????????????" + money + "??????????????????????????????????????????????????????????????????");
        view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        ToastUtil.showDialog(dialog);


    }


    public static void showSignCompleteLiuLanCount(final Context mContext, String money) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        final Dialog dialog = new Dialog(mContext, R.style.invate_dialog_style);
        View view = mInflater.inflate(R.layout.dialog_sign_liulan_count_complete_common, null);


        ImageView iv_close = view.findViewById(R.id.iv_close);
        TextView tv = view.findViewById(R.id.tv);
        tv.setText("???????????????????????????" + money + "??????????????????????????????????????????????????????????????????");
        view.findViewById(R.id.bt_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != ForceLookActivity.instance) {
                    ForceLookActivity.instance.finish();
                }
                dialog.dismiss();

            }
        });

        view.findViewById(R.id.bt_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


                if (null != ForceLookActivity.instance) {
                    ForceLookActivity.instance.finish();
                }

                if (null != SignActiveShopActivity.instance) {
                    SignActiveShopActivity.instance.finish();
                }

                if (null != CommonActivity.instance) {
                    CommonActivity.instance.finish();
                }


                SharedPreferencesUtil.saveStringData(mContext, "commonactivityfrom", "sign");
                Intent intent = new Intent(mContext, CommonActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
                ((Activity) mContext).finish();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        ToastUtil.showDialog(dialog);


    }


    public static void firstClickInGoToZP(final Context mContext) {


        if (null != SignDrawalLimitActivity.instance) {
            SignDrawalLimitActivity.instance.finish();
        }
        if (YCache.getCacheUser(mContext).getReviewers() == 1) {
            return;
        }

        Intent intent = new Intent(mContext, SignDrawalLimitActivity.class)
                .putExtra("type", 1);
        mContext.startActivity(intent);
        ((FragmentActivity) mContext).overridePendingTransition(
                R.anim.slide_left_in, R.anim.slide_match);


//
//        LayoutInflater mInflater = LayoutInflater.from(mContext);
//        final Dialog dialog = new Dialog(mContext, R.style.invate_dialog_style);
//        View view = mInflater.inflate(R.layout.dialog_common_one_button, null);
//
//
//        TextView tag_name = view.findViewById(R.id.tag_name);
//        TextView tag_name1 = view.findViewById(R.id.tag_name1);
//        TextView tv_title = view.findViewById(R.id.tv_title);
//        TextView btn_ok = view.findViewById(R.id.btn_ok);
//        tv_title.setText("????????????");
//        btn_ok.setText("???????????????");
//        tag_name.setText(Html.fromHtml("??????????????????????????????????????????????????????????????????????????????<font color='#ff3f8b'><b><big>??????15????????????100?????????</big></b></font>????????????????????????"));
//        tag_name1.setText(Html.fromHtml("????????????<font color='#ff3f8b'><b><big>???????????????</big></b></font>??????????????????????????????????????????<font color='#ff3f8b'><b><big>?????????????????????</big></b></font>???"));
//        tag_name1.setVisibility(View.VISIBLE);
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if(null != SignDrawalLimitActivity.instance){
//                    SignDrawalLimitActivity.instance.finish();
//                }
//                Intent intent = new Intent(mContext, SignDrawalLimitActivity.class)
//                        .putExtra("type", 1);
//                mContext.startActivity(intent);
//                ((FragmentActivity) mContext).overridePendingTransition(
//                        R.anim.slide_left_in, R.anim.slide_match);
//
//            }
//        });
//        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//
//            }
//        });
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//
//        ToastUtil.showDialog(dialog);

    }


}
