package com.yssj.huanxin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.yssj.Constants;
import com.yssj.Json;
import com.yssj.YConstance.Pref;
import com.yssj.YJApplication;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.entity.QueryPhoneInfo;
import com.yssj.entity.UserInfo;
import com.yssj.eventbus.MessageEvent;
import com.yssj.model.ComModel;
import com.yssj.model.ComModel2;
import com.yssj.model.ComModelL;
import com.yssj.model.ModQingfeng;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.activity.MineLikeActivity;
import com.yssj.ui.activity.SignDrawalLimitActivity;
import com.yssj.ui.activity.WithdrawalLimitActivity;
import com.yssj.ui.activity.logins.LoginActivity;
import com.yssj.ui.activity.logins.PhoneRegisterActivity;
import com.yssj.ui.activity.setting.SettingCommonFragmentActivity;
import com.yssj.ui.activity.shopdetails.ShopDetailsActivity;
import com.yssj.ui.activity.vip.MyVipListActivity;
import com.yssj.utils.CheckStrUtil;
import com.yssj.utils.DP2SPUtil;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.WXcheckUtil;
import com.yssj.utils.YCache;

import org.apache.commons.lang.time.DateFormatUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PublicUtil {


    public static void compLeteLogin(final Activity activity){

        new  Thread(new Runnable() {
            @Override
            public void run() {
                MessageEvent messageEvent = new MessageEvent();
                EventBus.getDefault().post(messageEvent);

                int homePageSwitchLoginTo = activity.getIntent().getIntExtra("homePageSwitchLoginTo",0);
                switch (homePageSwitchLoginTo){
                    case MainMenuActivity.loginToSign:

                        //?????????????????????????????????????????????????????????
                        if(YJApplication.instance.isLoginSucess() && YCache.getCacheUser(activity).getReviewers() ==1 ){
                            //??????????????????????????????
                            long serviceTime = System.currentTimeMillis()+ YJApplication.serviceDifferenceTime;
                            String add_date = "";
                            try {
                                add_date = YCache.getCacheUser(activity).getAdd_date().split(" ")[0];
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String currentDate = DateFormatUtils.format(serviceTime, "yyyy-MM-dd");
                            if (add_date.equals(currentDate)) {
                                SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                                activity.startActivity(new Intent(activity, CommonActivity.class));
                            }
                        }else{
                            SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                            activity.startActivity(new Intent(activity, CommonActivity.class));
                        }

                        break;
                    case MainMenuActivity.loginToWithdrawal:
                        activity.startActivity(new Intent(activity,WithdrawalLimitActivity.class));
                        break;
                    case MainMenuActivity.loginToVip:
                        activity.startActivity(new Intent(activity, MyVipListActivity.class)
                                .putExtra("guide_vipType", 4));
                        break;
                    default:
                        break;

                }
                if(homePageSwitchLoginTo > 0){
                    if (LoginActivity.instances != null) {
                        LoginActivity.instances.finish();
                    }
                    return;
                }

                if(activity.getIntent().getBooleanExtra("isVirtual",false)){
                    activity.setResult(SignDrawalLimitActivity.LOGIN_SUCCESS);
                    ShopDetailsActivity.everyDayTask1_2 = 0;
                    ShopDetailsActivity.shopTask = 0;
                    if (LoginActivity.instances != null) {
                        LoginActivity.instances.finish();
                    }

                    return;
                }

                activity.setResult(-1);
                ShopDetailsActivity.everyDayTask1_2 = 0;
                ShopDetailsActivity.shopTask = 0;


                if (LoginActivity.isSign) {

                    if (LoginActivity.instances != null) {
                        LoginActivity.instances.finish();
                    }


                    //????????????
                    SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                    activity.startActivity(new Intent(activity, CommonActivity.class));


                }
                if (activity instanceof PhoneRegisterActivity && LoginActivity.instances != null) {
                    LoginActivity.instances.finish();
                }
                ((Activity) activity).finish();

            }
        }).start();
    }




    public static void registerHuanxin(final Activity activity,
                                       final UserInfo result, final Boolean isPhoneRegister) {
        YCache.setCacheUser(activity, result);// ???????????????????????????
        ComModel.saveLoginFlag(activity);
        compLeteLogin(activity);

    }




    public static void registerHuanxinForLogin(final Activity activity,
                                               final UserInfo result, final String pwd, final boolean bindPhoneFlag, final boolean isQudao) {

        if (bindPhoneFlag || isQudao) {//????????? ??????????????? ????????????????????????  ?????????????????????????????? ?????????????????????????????????
            YCache.setCacheUser(activity, result);// ???????????????????????????
            ComModel.saveLoginFlag(activity);
        }

        new Thread(new Runnable() {

            public void run() {
                SharedPreferences sp = activity.getSharedPreferences("YSSJ_yf", Context.MODE_PRIVATE);
                Editor edit = sp.edit();
                edit.putInt("paycount", 0);
                edit.commit();
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        MessageEvent messageEvent = new MessageEvent();
                        EventBus.getDefault().post(messageEvent);

                        int homePageSwitchLoginTo = activity.getIntent().getIntExtra("homePageSwitchLoginTo",0);
                        switch (homePageSwitchLoginTo){
                            case MainMenuActivity.loginToSign:
                                //?????????????????????????????????????????????????????????
                                if(YJApplication.instance.isLoginSucess() && YCache.getCacheUser(activity).getReviewers() ==1 ){
                                    //??????????????????????????????
                                    long serviceTime = System.currentTimeMillis()+ YJApplication.serviceDifferenceTime;
                                    String add_date = "";
                                    try {
                                        add_date = YCache.getCacheUser(activity).getAdd_date().split(" ")[0];
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String currentDate = DateFormatUtils.format(serviceTime, "yyyy-MM-dd");
                                    if (add_date.equals(currentDate)) {
                                        SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                                        activity.startActivity(new Intent(activity, CommonActivity.class));
                                    }
                                }else{
                                    SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                                    activity.startActivity(new Intent(activity, CommonActivity.class));
                                }

                                break;
                            case MainMenuActivity.loginToWithdrawal:
                                activity.startActivity(new Intent(activity,WithdrawalLimitActivity.class));
                                break;
                            case MainMenuActivity.loginToVip:
                                activity.startActivity(new Intent(activity, MyVipListActivity.class)
                                        .putExtra("guide_vipType", 4));
                                break;
                            default:
                                break;

                        }
                        if(homePageSwitchLoginTo > 0){
                            if (LoginActivity.instances != null) {
                                LoginActivity.instances.finish();
                            }
                            return;
                        }



                        if(activity.getIntent().getBooleanExtra("isVirtual",false)){
                            activity.setResult(SignDrawalLimitActivity.LOGIN_SUCCESS);
                            ShopDetailsActivity.everyDayTask1_2 = 0;
                            ShopDetailsActivity.shopTask = 0;
                            if (LoginActivity.instances != null) {
                                LoginActivity.instances.finish();
                            }

                            return;
                        }


                        activity.setResult(-1);
                        ShopDetailsActivity.everyDayTask1_2 = 0;
                        ShopDetailsActivity.shopTask = 0;
                        YJApplication.instance.setLoginSucess(true);
                        ((Activity) activity).finish();
                    }
                });
            }

        }).start();
    }

    public static void openId(final SHARE_MEDIA platform, final int type, final Activity activity) {

        final UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);

        mController.doOauthVerify(activity, platform,
                new UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA platform) {

                    }

                    @Override
                    public void onError(SocializeException e,
                                        SHARE_MEDIA platform) {
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {

                        mController.getPlatformInfo(activity, platform, new UMDataListener() {

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onComplete(int status, Map<String, Object> info) {
                                if (info != null) {
                                    if (type == 0) {// ??????
                                        if (status == 200 && info != null) {
                                            final String unionid = info.get("unionid").toString();
                                            if (TextUtils.isEmpty(unionid)) {
                                                return;
                                            }
                                            new Thread() {
                                                public void run() {
                                                    try {
                                                        ComModel2.registerOpenId(unionid, activity);
                                                    } catch (Exception e) {

                                                    }
                                                }

                                                ;
                                            }.start();
                                        }
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {

                    }
                });

    }


    public static void registerHuanxin1(final Activity activity,  //????????????????????????
                                        final UserInfo result, final String pwd) {
        activity.getSharedPreferences(Pref.pd, Context.MODE_PRIVATE).edit().putString(Pref.pd, pwd).commit();
        new Thread(new Runnable() {

            public void run() {
                SharedPreferences sp = activity.getSharedPreferences("YSSJ_yf", Context.MODE_PRIVATE);
                Editor edit = sp.edit();
                edit.putInt("paycount", 0);
                edit.commit();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        MessageEvent messageEvent = new MessageEvent();
                        EventBus.getDefault().post(messageEvent);
                        int homePageSwitchLoginTo = activity.getIntent().getIntExtra("homePageSwitchLoginTo",0);
                        switch (homePageSwitchLoginTo){
                            case MainMenuActivity.loginToSign:
                                //?????????????????????????????????????????????????????????
                                if(YJApplication.instance.isLoginSucess() && YCache.getCacheUser(activity).getReviewers() ==1 ){
                                    //??????????????????????????????
                                    long serviceTime = System.currentTimeMillis()+ YJApplication.serviceDifferenceTime;
                                    String add_date = "";
                                    try {
                                        add_date = YCache.getCacheUser(activity).getAdd_date().split(" ")[0];
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String currentDate = DateFormatUtils.format(serviceTime, "yyyy-MM-dd");
                                    if (add_date.equals(currentDate)) {
                                        SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                                        activity.startActivity(new Intent(activity, CommonActivity.class));
                                    }
                                }else{
                                    SharedPreferencesUtil.saveStringData(activity, "commonactivityfrom", "sign");
                                    activity.startActivity(new Intent(activity, CommonActivity.class));
                                }

                                break;
                            case MainMenuActivity.loginToWithdrawal:
                                activity.startActivity(new Intent(activity,WithdrawalLimitActivity.class));
                                break;
                            case MainMenuActivity.loginToVip:
                                activity.startActivity(new Intent(activity, MyVipListActivity.class)
                                        .putExtra("guide_vipType", 4));
                                break;
                            default:
                                break;

                        }
                        if(homePageSwitchLoginTo > 0){
                            if (LoginActivity.instances != null) {
                                LoginActivity.instances.finish();
                            }
                            return;
                        }






                        if(activity.getIntent().getBooleanExtra("isVirtual",false)){
                            activity.setResult(SignDrawalLimitActivity.LOGIN_SUCCESS);
                            ShopDetailsActivity.everyDayTask1_2 = 0;
                            ShopDetailsActivity.shopTask = 0;
                            if (LoginActivity.instances != null) {
                                LoginActivity.instances.finish();
                            }

                            return;
                        }

                        Intent intent = new Intent(activity,
                                MineLikeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userinfo", result);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        ((Activity) activity).finish();

                    }
                });
            }

        }).start();
    }



    /**
     * ?????????????????????????????????
     *
     * @param context
     * @param im_iv
     * @param phone
     */
    public static void setVCode(Context context, ImageView im_iv, String phone) {
        String mUrl = "";
        try {
            mUrl = ComModelL.getPhoneCode(context, phone, CheckStrUtil.getImei(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getPicture(context, im_iv, mUrl);
    }

    /**
     * ????????????????????????????????? ????????????????????????
     *
     * @param context
     * @param im_iv
     */
    public static void setVCodePwd(Context context, ImageView im_iv) {
        String mUrl = "";
        try {
            mUrl = ComModelL.getPhoneCodePWD(context, CheckStrUtil.getImei(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getPicture(context, im_iv, mUrl);
    }

    public static void getPicture(final Context context, final ImageView im_iv, final String mUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(mUrl);
//                URL url = new URL("http://www.52yifu.wang/cloud-api/vcode/getVcode?version=V1.31&phone=13333333333&imei=866479024413139");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(10000);

                    if (conn.getResponseCode() == 200) {
                        InputStream fis = conn.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length = fis.read(bytes)) != -1) {
                            bos.write(bytes, 0, length);
                        }
                        final byte[] picBytes = bos.toByteArray();
                        bos.close();
                        fis.close();

//						Message message = new Message();
//						message.what = 99;
//						handle.sendMessage(message);
                        final String type = conn.getContentType();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (picBytes != null) {
                                    LogYiFu.e("posturl", new String(picBytes).toString());
                                    int rd = new Random().nextInt(90000000) + 1;
                                    //                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
                                    //                    mIvRule.getLayoutParams().height = width * bitmap.getHeight() / bitmap.getWidth();
                                    //                    mIvRule.setImageBitmap(bitmap);
                                    if ("image/gif".equals(type)) {
                                        Glide.with(context)
                                                .load(picBytes)
                                                .asGif()
                                                .diskCacheStrategy(DiskCacheStrategy.SOURCE).signature(new StringSignature(rd + "")).into(im_iv);
                                    } else if ("image/png".equals(type)) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
                                        im_iv.setImageBitmap(bitmap);
                                    } else {
                                        String str = picBytes.toString();
                                        JSONObject j = null;
                                        try {
                                            j = new JSONObject(new String(picBytes));
                                            String message3 = j.has("message") ? j.getString(Json.RetInfo.message) : "";
                                            ToastUtil.showMyToast(context, message3, 1000);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                }
                            }
                        });

                    } else {
//						ToastUtil.showMyToast(context, "??????????????????",1000);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//	public static Handler handle = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what == 99) {
//				if (picBytes != null) {
//					int rd = new Random().nextInt(90000000) + 1;
////                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
////                    mIvRule.getLayoutParams().height = width * bitmap.getHeight() / bitmap.getWidth();
////                    mIvRule.setImageBitmap(bitmap);
//					if ("image/gif".equals(type)) {
//						Glide.with(vcontext)
//								.load(picBytes)
//								.asGif()
//								.diskCacheStrategy(DiskCacheStrategy.SOURCE).signature(new StringSignature(rd + "")).into(v_im_iv);
//					} else if ("image/png".equals(type)) {
//						Bitmap bitmap = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
//						v_im_iv.setImageBitmap(bitmap);
//					} else {
//						String str = picBytes.toString();
//						JSONObject j = null;
//						try {
//							j = new JSONObject(new String(picBytes));
//							String message3 = j.has("message") ? j.getString(Json.RetInfo.message) : "";
//							ToastUtil.showMyToast(vcontext, message3,1000);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//
//					}
//
//				}
//			}
//		}
//	};
//	private static String type;
//	public static Runnable runnables = new Runnable() {
//		@Override
//		public void run() {
//			try {
//
//
////				String phone = "13333333333";
//				String mUrl = "";
//				try {
//					mUrl = ComModelL.getPhoneCode(vcontext, vphone, CheckStrUtil.getImei(vcontext));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//
//				URL url = new URL(mUrl);
////                URL url = new URL("http://www.52yifu.wang/cloud-api/vcode/getVcode?version=V1.31&phone=13333333333&imei=866479024413139");
//				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//				conn.setRequestMethod("GET");
//				conn.setReadTimeout(10000);
//
//				if (conn.getResponseCode() == 200) {
//					InputStream fis = conn.getInputStream();
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					byte[] bytes = new byte[1024];
//					int length = -1;
//					while ((length = fis.read(bytes)) != -1) {
//						bos.write(bytes, 0, length);
//					}
//					picBytes = bos.toByteArray();
//					bos.close();
//					fis.close();
//
//					Message message = new Message();
//					message.what = 99;
//					handle.sendMessage(message);
//					type = conn.getContentType();
//
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	};


    /**
     * ????????????????????????????????? ??????
     * ???????????????????????????????????????
     */
    public static void getBalanceNum(Context mContext, View ivBalanceLottory, boolean isSign) {

        balanceNum(mContext, ivBalanceLottory, isSign);
    }
//
//    /**
//     * ????????????????????????????????? ??????
//     * ???????????????????????????????????????
//     * ????????????????????????????????????
//     */
//    public static void getShopDetailsBalanceNum(Context mContext,View ivBalanceLottory,TextView tvBuyNow) {
//
//        balanceNum(mContext, ivBalanceLottory, tvBuyNow,false,true);
//    }

    /**
     * @param mContext
     * @param ivBalanceLottory ??????????????????
     * @param isSign           ??????????????????
     */
    private static void balanceNum(final Context mContext, final View ivBalanceLottory,
                                   final boolean isSign) {


        boolean mWxInstallFlag = false;


        try {
            // // ?????????????????????

            if (WXcheckUtil.isWeChatAppInstalled(mContext)) {
                mWxInstallFlag = true;
            } else {
                mWxInstallFlag = false;
            }
        } catch (Exception e) {
        }


        if (SharedPreferencesUtil.getBooleanData(mContext, Pref.IS_AREADY_BUY
                + YCache.getCacheUser(mContext).getUser_id(), false)) {
            if (!isSign) {
                ivBalanceLottory.setVisibility(View.GONE);
            }
            return; //?????????????????? ????????????
        }

//		setBalanceInit();//??????????????? ??????????????????
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String balanceLotteryTimes = SharedPreferencesUtil.getStringData(mContext, Pref.BALANCE_LOTTERY_DATE, "0");
        long balanceLotteryTimesLong = Long.parseLong(balanceLotteryTimes);
        if ("0".equals(balanceLotteryTimes) || !df.format(new Date()).equals(df.format(new Date(balanceLotteryTimesLong)))) {
            SharedPreferencesUtil.saveStringData(mContext, Pref.BALANCE_LOTTERY_SUM_COUNT, "0");
            SharedPreferencesUtil.saveStringData(mContext, Pref.BALANCE_LOTTERY, "0");
            SharedPreferencesUtil.saveStringData(mContext, Pref.BALANCE_LOTTERY_SUM_VALUE
                    + YCache.getCacheUser(mContext).getUser_id(), "0");
        }

        final boolean finalMWxInstallFlag = mWxInstallFlag;
        new SAsyncTask<Void, Void, HashMap<String, String>>((FragmentActivity) mContext, 0) {
            @Override
            protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
                    throws Exception {

                return ComModelL.getBalanceLuckNum(context);
            }

            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(final FragmentActivity context, HashMap<String, String> result, Exception e) {
                super.onPostExecute(context, result, e);
                if (e == null && result != null && "1".equals(result.get("status"))) {
                    String balanceLottery = result.get("data");
                    if ("-1".equals(balanceLottery)) {
                        //??????????????????
                        if (!isSign) {
                            ivBalanceLottory.setVisibility(View.GONE);
//                            if(isShopDetails){
//                                tvBuyNow.setText("????????????");
//                            }
                        }
                        SharedPreferencesUtil.saveBooleanData(context, Pref.IS_AREADY_BUY
                                        + YCache.getCacheUser(context).getUser_id()
                                , true);//??????????????????


                    } else {
                        if (isSign) {
                            //????????????????????????5????????????????????????????????????
                            Intent intent = new Intent(mContext, WithdrawalLimitActivity.class);
                            intent.putExtra("isBalanceLottery", true);
                            intent.putExtra("isFromSignBalanceLottery", isSign);
                            mContext.startActivity(intent);
                        } else {
                            ivBalanceLottory.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

//??????????????????--------------------------------------------------------------------------------------------------------------------------------------
                                    new SAsyncTask<Void, Void, QueryPhoneInfo>((FragmentActivity) mContext) {

                                        @Override
                                        protected QueryPhoneInfo doInBackground(FragmentActivity context,
                                                                                Void... params) throws Exception {

                                            return ComModel.bindPhone(context);
                                        }

                                        @Override
                                        protected boolean isHandleException() {
                                            return true;
                                        }

                                        @Override
                                        protected void onPostExecute(FragmentActivity context,
                                                                     QueryPhoneInfo result, Exception e) {
                                            super.onPostExecute(context, result, e);
                                            if (null == e) {
                                                if (result != null && "1".equals(result.getStatus())) {
                                                    Boolean phoneboolll = result.isBool();
                                                    if (phoneboolll == false) {// ???????????????
                                                        customDialog(mContext);
                                                        return;
                                                    } else {
//??????????????????---------------------------------------------------------------------------------------------
                                                        String uuid = YCache.getCacheUserSafe(mContext).getUuid();

                                                        if (null == uuid || uuid.equals("null")
                                                                || uuid.equals("")) {
                                                            //??????????????????-----?????????

                                                            if (!finalMWxInstallFlag) {

                                                                ToastUtil.showShortText(mContext, "??????????????????????????????????????????~");

                                                            } else {

                                                                ToastUtil.showShortText(mContext, "?????????????????????~");

//
//                                                                UMWXHandler wxHandler = new UMWXHandler(mContext, WxPayUtil.APP_ID,
//                                                                        WxPayUtil.APP_SECRET);
//                                                                wxHandler.addToSocialSDK();
//
//
//                                                                //????????????
//                                                                final UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
//
//                                                                mController.doOauthVerify(context, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
//
//                                                                    @Override
//                                                                    public void onStart(SHARE_MEDIA platform) {
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onError(SocializeException e, SHARE_MEDIA platform) {
//                                                                        ToastUtil.showShortText(mContext, "??????????????????");
//
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
//
//                                                                        final String openid = value.getString("openid");
//
//
//                                                                        mController.getPlatformInfo(mContext, platform, new SocializeListeners.UMDataListener() {
//
//                                                                            @Override
//                                                                            public void onStart() {
//
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onComplete(int status, Map<String, Object> info) {
//                                                                                if (info != null) {
//
//
//                                                                                    final String unionid = info.get("unionid").toString();
//                                                                                    if (TextUtils.isEmpty(unionid)) {
//                                                                                        return;
//                                                                                    }
//
//                                                                                    //??????unionid
//                                                                                    new SAsyncTask<String, Void, UserInfo>((FragmentActivity) mContext, R.string.wait) {
//
//                                                                                        @Override
//                                                                                        protected UserInfo doInBackground(FragmentActivity context, String... params)
//                                                                                                throws Exception {
//                                                                                            return ComModel2.updateUuid(mContext, unionid, openid);
//                                                                                        }
//
//                                                                                        @Override
//                                                                                        protected boolean isHandleException() {
//                                                                                            return true;
//                                                                                        }
//
//                                                                                        @Override
//                                                                                        protected void onPostExecute(FragmentActivity context, UserInfo result, Exception e) {
//                                                                                            super.onPostExecute(context, result, e);
//                                                                                            if (null == e) {
//
//
//                                                                                            }
//                                                                                        }
//
//                                                                                    }.execute();
//
//
//                                                                                }
//                                                                            }
//                                                                        });
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onCancel(SHARE_MEDIA platform) {
//                                                                        ToastUtil.showShortText(mContext, "?????????????????????~");
//
//                                                                    }
//                                                                });

                                                            }

                                                            return;
                                                        }


                                                    }

                                                    //????????????????????????5????????????????????????????????????
                                                    Intent intent = new Intent(mContext, WithdrawalLimitActivity.class);
                                                    intent.putExtra("isBalanceLottery", true);
                                                    mContext.startActivity(intent);


                                                } else {
                                                    ToastUtil.showLongText(context, "??????????????????~~~");
                                                }
                                            }
                                        }

                                    }.execute();


                                }
                            });


                            getSignHasHongbao(ivBalanceLottory, mContext);


//                            ivBalanceLottory.setVisibility(View.VISIBLE);


//                            if(isShopDetails){
//                                tvBuyNow.setText("????????????????????????");
//                            }


                        }


                        SharedPreferencesUtil.saveStringData(context, Pref.BALANCE_LOTTERY, balanceLottery);//????????????


                        //?????????
                        SharedPreferencesUtil.saveStringData(context, Pref.BALANCE_LOTTERY_SUM_COUNT, result.get("n"));//?????????

                        //????????????????????????
                        SharedPreferencesUtil.saveStringData(mContext, Pref.BALANCE_LOTTERY_SUM_VALUE
                                + YCache.getCacheUser(mContext).getUser_id(), result.get("money"));

                    }

                    SharedPreferencesUtil.saveStringData(context, Pref.BALANCE_LOTTERY_DATE, System.currentTimeMillis() + "");


                } else {
                    if (!isSign) {
                        ivBalanceLottory.setVisibility(View.GONE);
                    }
                }

            }
        }.execute();
    }

    private static void getSignHasHongbao(final View ivBalanceLottory, final Context mContext) {


        new SAsyncTask<Void, Void, Boolean>((FragmentActivity) mContext, 0) {

            @Override
            protected Boolean doInBackground(FragmentActivity context,
                                             Void... params) throws Exception {
                return ModQingfeng.getHasHongbaoTask(mContext);
            }

            protected boolean isHandleException() {
                return true;
            }

            ;

            @Override
            protected void onPostExecute(FragmentActivity context,
                                         Boolean result, Exception e) {
                super.onPostExecute(context, result, e);
                if (null != result) {
                    if (result) {
                        ivBalanceLottory.setVisibility(View.VISIBLE);

                    }
                }


            }

        }.execute();


    }

//	private void setBalanceInit() {
//
//	}

    public static AlertDialog dialog;


    public static void customDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Theme_Transparent);
        // ???????????????????????????
        View view = View.inflate(mContext, R.layout.payback_esc_apply_dialog, null);
        view.setBackgroundResource(R.drawable.redquanbg);
        TextView tv_des = (TextView) view.findViewById(R.id.tv_des);
        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setTextColor(Color.parseColor("#ffffff"));
        ok.setBackgroundResource(R.drawable.bg_red_ok);
        ok.setWidth(DP2SPUtil.dp2px(mContext, 90));
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setWidth(DP2SPUtil.dp2px(mContext, 90));
        cancel.setTextColor(Color.parseColor("#ff3f8b"));
        cancel.setBackgroundResource(R.drawable.bg_tans_cancel);


        tv_des.setText("?????????????????????????????????????????????");
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ???????????????????????????
                dialog.dismiss();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                            bangDingPhoneType = id;
//                    Intent intent = new Intent(mContext, BindPhoneActivity.class);
//                    startActivityForResult(intent, 1305);


                Intent intent = new Intent(mContext, SettingCommonFragmentActivity.class);
                intent.putExtra("flag", "bindPhoneFragment");
                intent.putExtra("bool", false);
                intent.putExtra("isChanal", MainMenuActivity.noUserRegist); // ????????????true,?????????false
                // ???????????????????????????true
                intent.putExtra("phoneNum", "");
                intent.putExtra("wallet", "account");
                intent.putExtra("bindSelf", true);

                intent.putExtra("thirdparty", "thirdparty");
                intent.putExtra("tishiBind", true);

                mContext.startActivity(intent);

                ((FragmentActivity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);


                dialog.dismiss();
            }
        });


        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }
}
