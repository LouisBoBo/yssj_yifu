package com.yssj.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.yssj.YConstance.Pref;
import com.yssj.YJApplication;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.entity.MyInfoCenterDate;
import com.yssj.entity.MycenterCount;
import com.yssj.entity.UserInfo;
import com.yssj.entity.VipInfo;
import com.yssj.model.ComModel2;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.activity.MessageCenterActivity;
import com.yssj.ui.activity.MyPicActivity;
import com.yssj.ui.activity.MyYJactivity;
import com.yssj.ui.activity.infos.HelpCenterActivity;
import com.yssj.ui.activity.infos.MyCouponsActivity;
import com.yssj.ui.activity.infos.MyFavorActivity;
import com.yssj.ui.activity.infos.MyFootPrintActivity;
import com.yssj.ui.activity.infos.MyInfoActivity;
import com.yssj.ui.activity.infos.MyInfoActivity.onUserInfoUpdate;
import com.yssj.ui.activity.infos.MyWalletActivity;
import com.yssj.ui.activity.infos.StatusInfoActivity;
import com.yssj.ui.activity.payback.PaybackCommonFragmentActivity;
import com.yssj.ui.activity.setting.InviteFriendActivity;
import com.yssj.ui.activity.setting.SettingActivity;
import com.yssj.ui.activity.vip.MyVipListActivity;
import com.yssj.ui.base.BasicActivity;
import com.yssj.utils.BlurTransformation;
import com.yssj.utils.PicassoUtils;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.SignCompleteDialogUtil;
import com.yssj.utils.StringUtils;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.YCache;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/***
 * ??????
 *
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class MineIfoFragment extends Fragment implements OnClickListener, onUserInfoUpdate {

    // static {
    // System.loadLibrary("base64encode");
    // }
    //
    // public native String Base64Encode(byte[] cs); // ??????????????????

    // private ImageView iv_mtopimg;
    private ImageView img_user_button;
    private RelativeLayout rel_user_info;
    private TextView tv_location;
    // private TextView tv_exit;


    ClipboardManager clipboardManager;
    String tempStr;
    CharSequence cs;

    private RelativeLayout rel_wait_pay, rel_wait_deliver, rel_wait_receipt, rel_wait_judge, rel_wait_payback,
            rel_mydata;
    private RelativeLayout rel_my_order,tv_shouchang1 ;

    private LinearLayout ll_myqianbao, ll_mycard, ll_vip;

    private TextView tv_wait_pay_count, tv_wait_deliver_count, tv_wait_receipt_count, tv_wait_judge_count,
            tv_wait_payback_count, tv_haoyou, tv_miyou, tv_chuanda, tv_zuiai, tv_zuji, my_qianming, tv_zhaopian,
            tv_shouchang, tv_haoyoujiangli,ll_xiaoxi;

    private static ImageView ll_top;
    private TextView tv_wallet_red, tv_coupons_red, tv_vip;

    private TextView my_yqm;
    // private ImageView img_premium_services;

    // private LinearLayout lin_login;

    // private View barView;
    private RelativeLayout btn_setting;
    // private ImageButton imgbtn_left_icon;

    // private static YDBHelper dbHelper;

    private static final String TAB = "tab5";

    private UserInfo info;

    private TextView unreadLabel;// ????????????

    private MycenterCount mycenterCount = new MycenterCount();
    private static Context mContext;

    private String like_count, myStepCount, store_shop_count;

    private RelativeLayout rel_title;

    //    private PullToRefreshScrollView scroll;
    private int huaTiCount;// ????????????
    private int systemCount = 0;// ????????????

    private float argY;
    private ImageView iv_vvv;

    public static MyInfoCenterDate.VipDataBean vipData;//????????????
    private TextView tv_icon_newuser;

    // private ImageView iv_to_myinfo;

    public static MineIfoFragment newInstance(String title, Context context) {
        MineIfoFragment fragment = new MineIfoFragment();
        Bundle args = new Bundle();
        args.putString(TAB, title);
        mContext = context;
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myinfo_fragment, container, false);
        initView(v);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        // cShow = (CallbackShow) activity;
        // ((MainMenuActivity)
        // (activity)).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActivity().getActionBar().hide();

//        EMChatManager.getInstance().registerEventListener(new EMEventListener() {
//
//            @Override
//            public void onEvent(EMNotifierEvent event) {
//
//                switch (event.getEvent()) {
//                    case EventNewMessage: // ???????????????
//                    {
//                        event.getData();
//                        // ???????????????
//                        updateUnreadLabel();
//                        break;
//                    }
//                    case EventDeliveryAck: {// ?????????????????????
//                        // ???????????????
//
//                        updateUnreadLabel();
//                        break;
//                    }
//
//                    case EventNewCMDMessage: {// ??????????????????
//
//                        updateUnreadLabel();
//                        break;
//                    }
//
//                    case EventReadAck: {// ??????????????????
//
//                        updateUnreadLabel();
//                        break;
//                    }
//
//                    case EventOfflineMessage: {// ??????????????????
//                        event.getData();
//
//                        updateUnreadLabel();
//                        break;
//                    }
//
//                    case EventConversationListChanged: {// ????????????????????????event?????????????????????????????????SDK????????????????????????????????????????????????
//
//                        updateUnreadLabel();
//                        break;
//                    }
//
//                    default:
//                        break;
//                }
//            }
//
//        });
    }

    /*
     * ?????????????????????????????????UI
     */
    private void getMainInfoCount() {
        HashMap<String, String> pairsMap = new HashMap<>();


        YConn.httpPost(mContext, YUrl.PERSON_CENTER_COUNT, pairsMap, new HttpListener<MyInfoCenterDate>() {


            @Override
            public void onSuccess(MyInfoCenterDate result) {
                if(result == null){
                    return;
                }

                if(result.getLike_count() != null){
                    like_count = result.getLike_count();
                }

                if (Integer.parseInt(result.getBalance_show()) == 1) {

                } else {

                }

                vipData = result.getVipData();
                if (null != vipData && !StringUtils.isEmpty(vipData.getVip_name())) {


                    PicassoUtils.initImage(vipData.getHead_url(), iv_vvv, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_vvv.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });


                    if (vipData.getVip_type() < 0) {
                        my_qianming.setText("???????????????");

                    } else {
                        my_qianming.setText(vipData.getVip_name());

                    }
                    iv_vvv.setVisibility(View.VISIBLE);

                } else {
                    my_qianming.setText("????????????");
                    iv_vvv.setVisibility(View.GONE);
                }


                java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
                if (YCache.getCacheUser(mContext).getReviewers() == 1) {
                    if(result.getCoupon_sum() != null) {
//                        tv_wallet_red.setText("?? " + df.format(Double.parseDouble("" + result.getCoupon_sum())));
                        tv_wallet_red.setText("???" + result.getCoupon_sum());
                    }

                } else {
                    if(result.getShowExtMoney() != null) {
//                        tv_wallet_red.setText("?? " + df.format(Double.parseDouble("" + result.getShowExtMoney())));
                        tv_wallet_red.setText("???" + result.getShowExtMoney());
                    }
                }

                if(result.getCoupon_sum() != null) {
//                    tv_coupons_red.setText("?? " + df.format(Double.parseDouble("" + result.getCoupon_sum())));
                    tv_coupons_red.setText("???" + result.getCoupon_sum());
                }

                if(result.getMySteps_count() != null) {
                    myStepCount = result.getMySteps_count();
                }

                if(result.getStore_shop_count() != null) {
                    store_shop_count = result.getStore_shop_count();
                }

                initRedPointView(result.getPay_count(), result.getSend_count(),
                        result.getFurl_count(), result.getAss_count() + "",
                        result.getRefund_count(), "0");
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setData();

        }
    }


    // private int mWidth;
    // private int mHeight;
    // private int statusBarHeight;

    private void initView(View v) {



        // ????????????
//        getKefu();

        ll_xiaoxi = v.findViewById(R.id.ll_xiaoxi);
//        tv_haoyou = (TextView) v.findViewById(R.id.tv_haoyou);
        tv_miyou = (TextView) v.findViewById(R.id.tv_miyou);
        tv_shouchang = (TextView) v.findViewById(R.id.tv_shouchang);
        tv_chuanda = (TextView) v.findViewById(R.id.tv_chuanda);
        tv_zuiai = (TextView) v.findViewById(R.id.tv_zuiai);
        tv_zuji = (TextView) v.findViewById(R.id.tv_zuji);
        tv_zhaopian = (TextView) v.findViewById(R.id.tv_zhaopian);
        my_qianming = (TextView) v.findViewById(R.id.my_qianming);
        my_yqm = (TextView) v.findViewById(R.id.my_yqm);
        tv_haoyoujiangli = (TextView) v.findViewById(R.id.tv_haoyoujiangli);
        ll_vip = (LinearLayout) v.findViewById(R.id.ll_vip);
        tv_vip = v.findViewById(R.id.tv_vip);
        tv_vip.setOnClickListener(this);

        tv_chuanda.setVisibility(View.INVISIBLE);
        tv_miyou.setVisibility(View.INVISIBLE);
        tv_shouchang.setVisibility(View.INVISIBLE);



        iv_vvv = (ImageView) v.findViewById(R.id.iv_vvv);
        tv_shouchang1 = v.findViewById(R.id.tv_shouchang1);
        tv_shouchang1 .setOnClickListener(this);
        ll_xiaoxi.setOnClickListener(this);
//        tv_haoyou.setOnClickListener(this);
        tv_miyou.setOnClickListener(this);
        tv_shouchang.setOnClickListener(this);
        tv_chuanda.setOnClickListener(this);
        tv_zuiai.setOnClickListener(this);
        tv_zuji.setOnClickListener(this);
        tv_zhaopian.setOnClickListener(this);
        tv_haoyoujiangli.setOnClickListener(this);
        ll_vip.setOnClickListener(this);


        // ????????????
        rel_mydata = (RelativeLayout) v.findViewById(R.id.rel_mydata);
        rel_mydata.setOnClickListener(this);

        // iv_to_myinfo = (ImageView) v.findViewById(R.id.iv_to_myinfo);
        // iv_to_myinfo.setOnClickListener(this);
        rl_to_myinfo = (RelativeLayout) v.findViewById(R.id.rl_to_myinfo);
        rl_to_myinfo.setOnClickListener(this);
        ll_myqianbao = (LinearLayout) v.findViewById(R.id.ll_myqianbao);
        ll_myqianbao.setOnClickListener(this);
        ll_mycard = (LinearLayout) v.findViewById(R.id.ll_mycard);
        ll_mycard.setOnClickListener(this);

        img_user_button = (ImageView) v.findViewById(R.id.img_user_button);
        img_user_button.setOnClickListener(this);

        tv_wallet_red = (TextView) v.findViewById(R.id.tv_wallet_red);

        btn_setting = (RelativeLayout) v.findViewById(R.id.btn_setting); // ??????????????????
        btn_setting.setOnClickListener(this);

        rel_user_info = (RelativeLayout) v.findViewById(R.id.rel_user_info);// ????????????
        rel_user_info.setOnClickListener(this);
        tv_location = (TextView) v.findViewById(R.id.tv_location);// ????????????

        rel_wait_pay = (RelativeLayout) v.findViewById(R.id.rel_wait_pay);// ?????????
        rel_wait_pay.setOnClickListener(this);
        rel_wait_deliver = (RelativeLayout) v.findViewById(R.id.rel_wait_deliver);// ?????????
        rel_wait_deliver.setOnClickListener(this);
        rel_wait_receipt = (RelativeLayout) v.findViewById(R.id.rel_wait_receipt);// ?????????
        rel_wait_receipt.setOnClickListener(this);
        rel_wait_judge = (RelativeLayout) v.findViewById(R.id.rel_wait_judge);// ?????????
        rel_wait_judge.setOnClickListener(this);
        rel_wait_payback = (RelativeLayout) v.findViewById(R.id.rel_wait_payback);// ?????????
        rel_wait_payback.setOnClickListener(this);

        rel_my_order = (RelativeLayout) v.findViewById(R.id.rel_my_order);// ????????????
        rel_my_order.setOnClickListener(this);
        unreadLabel = (TextView) v.findViewById(R.id.unread_msg_number);

        tv_coupons_red = (TextView) v.findViewById(R.id.tv_coupons_red);
        tv_icon_newuser = (TextView) v.findViewById(R.id.tv_icon_newuser);

        tv_wait_pay_count = (TextView) v.findViewById(R.id.tv_wait_pay_count);
        tv_wait_deliver_count = (TextView) v.findViewById(R.id.tv_wait_deliver_count);
        tv_wait_receipt_count = (TextView) v.findViewById(R.id.tv_wait_receipt_count);
        tv_wait_judge_count = (TextView) v.findViewById(R.id.tv_wait_judge_count);
        tv_wait_payback_count = (TextView) v.findViewById(R.id.tv_wait_payback_count);
        rel_title = (RelativeLayout) v.findViewById(R.id.rel_title);
        rel_title.setBackgroundColor(Color.rgb(255, 82, 171));
        rel_title.getBackground().setAlpha(0);
//        scroll = (PullToRefreshScrollView) v.findViewById(R.id.scroll);
//        scroll.setMode(Mode.BOTH);
        // scroll.setScrollViewListener(this);
//        argY = scroll.getScaleY();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // mWidth = metrics.widthPixels;
        // mHeight = metrics.heightPixels;
        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // statusBarHeight = frame.top;

        ll_top = (ImageView) v.findViewById(R.id.ll_top);// ???????????????????????????

        setData();
    }

    static String mUrl;

    private void setTopBg(ImageView ll_top2, String url) {
        if (!TextUtils.isEmpty(url) && url.contains("http://")) {
            mUrl = url;
        } else if (!TextUtils.isEmpty(url) && url.contains("https://")) {
            mUrl = url;
        } else {
            mUrl = YUrl.imgurl + url;
        }
//        // ?????????????????????????????????
//        new Thread(networkTask).start();

        Activity activity = getActivity();
        if (isDestroy(activity)) {
            return;
        }

        Glide.with(activity)
                .load(mUrl)
                .error(R.drawable.image_default)
                .bitmapTransform(new BlurTransformation(mContext, 20, 1)) // ???23?????????????????????(???0.0???25.0??????)????????????25";"4":??????????????????,?????????1??????
                .into(ll_top2);

    }

    /**
     * ??????Activity??????Destroy
     *
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    static Bitmap map = null;

    private String kefu;

//    private void getKefu() {
//
//        new SAsyncTask<Void, Void, HashMap<String, String>>(getActivity(), 0) {
//
//            @Override
//            protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
//                    throws Exception {
//                return ComModel2.choiceKefu(getActivity(), 1);
//            }
//
//            protected boolean isHandleException() {
//                return true;
//            }
//
//            ;
//
//            @Override
//            protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
//                super.onPostExecute(context, result, e);
//                if (e == null && result != null) {
//                    kefu = (String) result.get("id");
//                    LogYiFu.e("IDD", kefu);
//                    SharedPreferencesUtil.saveStringData(context, "kefuNB", kefu);
//                }
//            }
//
//        }.execute();
//    }

    /**
     * pay_count:???????????????send_count??????????????????furl_count??? ???????????? ass_count??? ????????????
     * refund_count????????????
     */
    private void initRedPointView(String pay_count, String send_count, String furl_count, String ass_count,
                                  String refund_count, String finCount) {
        if (!pay_count.equals("0")) {//?????????
            tv_wait_pay_count.setText(pay_count + "");
            tv_wait_pay_count.setVisibility(View.VISIBLE);
        } else {
            tv_wait_pay_count.setVisibility(View.GONE);
        }

        if (!send_count.equals("0")) {//?????????
            tv_wait_deliver_count.setText(send_count + "");
            tv_wait_deliver_count.setVisibility(View.VISIBLE);
        } else {
            tv_wait_deliver_count.setVisibility(View.GONE);
        }

        if (!furl_count.equals("0")) {
            tv_wait_receipt_count.setText(furl_count + "");
            tv_wait_receipt_count.setVisibility(View.VISIBLE);
        } else {
            tv_wait_receipt_count.setVisibility(View.GONE);
        }

        if (!ass_count.equals("0")) {
            tv_wait_judge_count.setText(ass_count + "");
            tv_wait_judge_count.setVisibility(View.VISIBLE);
        } else {
            tv_wait_judge_count.setVisibility(View.GONE);
        }

        if (!refund_count.equals("0")) {
            // tv_wait_payback_count.setText(refund_count + "");
            tv_wait_payback_count.setVisibility(View.GONE);
        } else {
            tv_wait_payback_count.setVisibility(View.GONE);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * ??????????????????????????????
     */
//    static Runnable networkTask = new Runnable() {
//        @SuppressLint("NewApi")
//        @Override
//        public void run() {
//            try {
//                URL url = new URL(mUrl);
//                URLConnection conn = url.openConnection();
//                conn.connect();
//                InputStream in;
//                in = conn.getInputStream();
//                map = BitmapFactory.decodeStream(in);
//
//                Bitmap sentBitmap = map.copy(Bitmap.Config.ARGB_8888, true);
//                /**
//                 * ??????????????????
//                 * ??????scaleRatio?????????????????????????????????bitmap????????????????????????????????????????????????????????????????????????????????????
//                 * ??????blurRadius??????????????????????????????????????????????????????CPU??????intensive
//                 */
//
//                int scaleRatio = 5;
//                int blurRadius = 8;
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(sentBitmap, sentBitmap.getWidth() / scaleRatio,
//                        sentBitmap.getHeight() / scaleRatio, false);
//                final Bitmap blurBitmap = FastBlur.doBlur(scaledBitmap, blurRadius, true);
//                // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // ll_top.setBackground(new
//                        // BitmapDrawable(mContext.getResources(), blurBitmap));
//                        ll_top.setImageBitmap(blurBitmap);
//                    }
//                });
//
//                // TODO Auto-generated catch block
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    };
    private void setData() {
        getMainInfoCount(); // ????????????????????????


        try {
            info = YCache.getCacheUserSafe(mContext);
        } catch (Exception e) {
            new UserInfo();
            UserInfo info = YCache.getCacheUserSafe(mContext);
            this.info = info;
        }

        if (info.getReviewers() == 1) {
//            my_qianming.setVisibility(View.GONE);
//            tv_zhaopian.setVisibility(View.VISIBLE);
//            tv_vip.setVisibility(View.GONE);
//
//
//            tv_haoyoujiangli.setVisibility(View.GONE);
//            tv_shouchang.setVisibility(View.INVISIBLE);
//            tv_shouchang1.setVisibility(View.VISIBLE);



            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            my_yqm.setLayoutParams(lp);


        } else {

//            tv_haoyoujiangli.setVisibility(View.VISIBLE);
//            tv_shouchang.setVisibility(View.VISIBLE);
//            tv_shouchang1.setVisibility(View.GONE);
//
//
//            my_qianming.setVisibility(View.VISIBLE);
//            tv_zhaopian.setVisibility(View.GONE);
//            tv_vip.setVisibility(View.GONE);
        }

        // ????????????????????????
        updateUnreadLabel();
        if (YCache.getCacheUserSafe(mContext) == null) {
            // lin_login.setVisibility(View.VISIBLE);
            rel_user_info.setVisibility(View.GONE);
            // iv_mtopimg.setVisibility(View.GONE);
            img_user_button.setVisibility(View.GONE);
            // imgbtn_left_icon.setImageResource(R.drawable.mine_return_icon);
        } else {
            // lin_login.setVisibility(View.GONE);
            rel_user_info.setVisibility(View.VISIBLE);
            // iv_mtopimg.setVisibility(View.VISIBLE);
            img_user_button.setVisibility(View.VISIBLE);
            // imgbtn_left_icon.setImageResource(R.drawable.mine_return_icon);

            if (info != null) {
                // SetImageLoader.initImageLoader(mContext, img_user_button,
                // info.getPic(), "");


                PicassoUtils.initImage(mContext, info.getPic(), img_user_button);
                my_yqm.setText(info.getUser_id() + "");
                my_yqm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringUtils.copyString(mContext, info.getUser_id() + "");
                        ToastUtil.showShortText2("?????????????????????~");
                    }
                });


//                GlideUtils.initRoundImage(Glide.with(mContext), mContext, info.getPic(), img_user_button);


                String v_ident = info.getV_ident() + "";
                int v = 0;
                try {
                    v = Integer.parseInt(v_ident);
                } catch (Exception e) {
                }

//                switch (v) {
//                    case 0:
//                        iv_vvv.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        iv_vvv.setImageResource(R.drawable.v_red_big);
//                        iv_vvv.setVisibility(View.VISIBLE);
//
//                        break;
//                    case 2:
//                        iv_vvv.setImageResource(R.drawable.v_blue_big);
//                        iv_vvv.setVisibility(View.VISIBLE);
//
//                        break;
//
//                    default:
//                        break;
//                }

                // new Thread(networkTask).start();
                setTopBg(ll_top, info.getPic());

                tv_location.setText(info.getNickname());


                String add_date = "";
                try {
                    add_date = YCache.getCacheUser(mContext).getAdd_date().split(" ")[0];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String currentDate = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");
                if (add_date.equals(currentDate)) {
                    tv_icon_newuser.setVisibility(View.VISIBLE);
                } else {
                    tv_icon_newuser.setVisibility(View.GONE);
                }


//                if (null == info.getIs_member() || "0".equals(info.getIs_member()) || "1".equals(info.getIs_member())) {
//                    // rel_super_vip.setVisibility(View.VISIBLE);
//                    // line6.setVisibility(View.VISIBLE);
//                } else if ("3".equals(info.getIs_member())) {
//                } else if ("2".equals(info.getIs_member()) || "4".equals(info.getIs_member())
//                        || "5".equals(info.getIs_member())) {
//                    // rel_super_vip.setVisibility(View.VISIBLE);
//                    // line6.setVisibility(View.VISIBLE);
//                }

            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(mContext).clearDiskCache();

            }
        }).start();

        Glide.get(mContext).clearMemory();

//         MobclickAgent.onPageStart("MineInfoFragment"); // ????????????
        MyInfoActivity.setOnUserInfoUpdate(this);
        if (this.isVisible() == false) {
            return;
        }

        if (mTask != null) {
            mTask.cancel();
            mTask = new MyTimerTask();
        } else {
            mTask = new MyTimerTask();
        }

        if (YJApplication.instance.isLoginSucess()) {
            timer.schedule(mTask, 0, 1000); // 1s?????????task,??????1s????????????
            setData();
        }


    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // MobclickAgent.onPageEnd("MineInfoFragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_xiaoxi:// ??????
                intent = new Intent(mContext, MessageCenterActivity.class);
                startActivity(intent);
                // ??????????????? ???????????????????????????
                SharedPreferencesUtil.saveStringData(mContext, Pref.ALL_MESSAGE, "0");
                SharedPreferencesUtil.saveStringData(mContext, Pref.ALL_MESSAGE_COUNT, "0");
                SharedPreferencesUtil.saveStringData(mContext, Pref.ALL_MESSAGE_CHAT, "0");
                SharedPreferencesUtil.saveStringData(mContext, Pref.ALL_MESSAGE_COUNT_CHAT, "0");
                break;
            case R.id.tv_haoyoujiangli:// ??????


//                IWXAPI api = WXAPIFactory.createWXAPI(mContext, WxPayUtil.APP_ID);
//                api.registerApp(WxPayUtil.APP_ID);
//
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = YUrl.WX_MINIAPP_ORIGINAL_ID; // ??????????????????id
//                req.path = "/pages/mine/AppMessage/AppMessage";                  ////??????????????????????????????????????????????????????????????????????????????????????????????????????????????? query ????????????????????????????????????????????? "?foo=bar"???
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;// ???????????? ?????????????????????????????????
//                api.sendReq(req);

                if (YCache.getCacheUser(mContext).getReviewers() != 1) {
                    intent = new Intent(mContext, MyYJactivity.class);
                    startActivity(intent);
                }


//                    startActivity(new Intent(mContext, TestActivity.class));


//
//
//			ToastUtil.showShortText(mContext,"????????????????????????????????????~");


//                ((Activity) mContext).runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        clipboardManager = (ClipboardManager) (mContext.getSystemService(Context.CLIPBOARD_SERVICE));
//
//                        if (clipboardManager.hasPrimaryClip()) {
//
//                            CharSequence cs = clipboardManager.getPrimaryClip().getItemAt(0).getText();
//                            if (cs == null) {
//                                ToastUtil.showShortText(mContext, "??????????????????");
//
//                            } else if ("".equals(cs)) {
//                                ToastUtil.showShortText(mContext, "??????????????????");
//
//                            } else {
//                                tempStr = cs.toString();
//                                ToastUtil.showShortText(mContext, "??????????????????" + tempStr);
//                                Intent intent = new Intent(mContext, ShopDetailsActivity.class);
//                                intent.putExtra("code", tempStr);
//                                ((FragmentActivity) mContext).startActivity(intent);
//                                ((FragmentActivity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//
//
//                                clipboardManager.setPrimaryClip(ClipData.newPlainText("text", ""));
//                            }
//                        } else {
//                            ToastUtil.showShortText(mContext, "??????????????????");
//
//
//                        }
//
//
//                    }
//                });


//			SharedPreferencesUtil.saveStringData(mContext, "commonactivityfrom", "sign");
//
//			intent = new Intent(mContext, CommonActivity.class);
//			intent.putExtra("isTastComplete",true);
//			startActivity(intent);
//			((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);


//			Intent intentYao = new Intent(mContext, YaoQingFrendsActivity.class);
//			intentYao.putExtra("isFabu",true);
//
//			startActivity(intentYao);
//			((FragmentActivity) mContext).overridePendingTransition(
//					R.anim.slide_left_in, R.anim.slide_match);
//


                // intent = new Intent(mContext, MyFriendsAllListActivity.class);
                // intent.putExtra("type", "1");
                // startActivity(intent);
                //????????????????????????
//			intent = new Intent(mContext, YaoQingFrendsActivity.class);
//			intent.putExtra("jumpFrom", "shareTieZi");

//			startActivity(intent);
//			((FragmentActivity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);

                break;
            case R.id.tv_miyou:// ??????

                ToastUtil.showShortText(mContext, "????????????????????????????????????~");


//			startActivity(new Intent(mContext, ShopPageActivity.class));
//			((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);


//                ToastUtil.showDialog(new SignFengZhongCompleteDialog(YJApplication.diolgContext, R.style.DialogQuheijiao, "bankuailiulanwancheng", SignFragment.signFragment));


                // intent = new Intent(mContext, PullToRefreshSampleActivity.class);
                // startActivity(intent);
                //????????????????????????
//			intent = new Intent(mContext, YaoQingFrendsActivity.class);
//			intent.putExtra("jumpFrom", "shareShop");
//			startActivity(intent);
//			((FragmentActivity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);

                // SharedPreferencesUtil.saveStringData(mContext,
                // "commonactivityfrom", "pubuliu_sign");
                // startActivity(new Intent(mContext, CommonActivity.class));
                //
                // intent = new Intent(mContext, SampleActivity.class);
                // startActivity(intent);

                // intent = new Intent(mContext, MyFriendsAllListActivity.class);
                // intent.putExtra("type", "2");
                // startActivity(intent);

                break;
            case R.id.tv_shouchang:// ??????
            case R.id.tv_shouchang1:
                SharedPreferencesUtil.saveStringData(mContext, "commonactivityfrom", "choucangliebiao");
                intent = new Intent(mContext, CommonActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_chuanda:// ??????
                SharedPreferencesUtil.saveStringData(mContext, "commonactivityfrom", "chuandaliebiao");
                intent = new Intent(mContext, CommonActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_zuiai:// ??????
                if (null != mycenterCount && like_count != null) {
                    intent = new Intent(mContext, MyFavorActivity.class);
                    intent.putExtra("count", Integer.parseInt(like_count));
                    startActivity(intent);
                }
                break;
            case R.id.tv_zuji:// ??????
                if (null != mycenterCount && myStepCount != null) {
                    intent = new Intent(mContext, MyFootPrintActivity.class);
                    intent.putExtra("myStepCount", Integer.parseInt(myStepCount));
                    startActivityForResult(intent, 10001);
                }

                break;
            case R.id.ll_vip:// ??????

                if (null != vipData && !StringUtils.isEmpty(vipData.getVip_name())) {
                    BasicActivity.goToGuideVipOrToMyVipList(mContext, vipData.getVip_type());
                } else {
                    BasicActivity.goToGuideVipOrToMyVipList(mContext, 0);
                }

                break;

            case R.id.tv_vip:
                mContext.startActivity(new Intent(mContext, MyVipListActivity.class)
                );
                break;


            case R.id.tv_zhaopian:// ??????


                intent = new Intent(mContext, MyPicActivity.class);
                startActivity(intent);

//                intent = new Intent(mContext, MyVipListActivity.class);
//                startActivity(intent);


                break;
            case R.id.ll_mycard:// ????????????

                intent = new Intent(mContext, MyCouponsActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_myqianbao:// ????????????


                //??????????????????
//                HashMap<String, String> pairsMap = new HashMap<>();
//                YConn.httpPost(mContext, YUrl.QUERY_VIP_INFO2, pairsMap
//                        , new HttpListener<VipInfo>() {
//                            @Override
//                            public void onSuccess(VipInfo vipInfo) {
//                                if (null != vipInfo) {

                if (YCache.getCacheUser(mContext).getReviewers() == 1) {
                    intent = new Intent(mContext, MyCouponsActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, MyWalletActivity.class));

                }

//                                }
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });


                break;

            case R.id.img_user_button:// ????????????
                intent = new Intent(mContext, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_to_myinfo:// ????????????
                intent = new Intent(mContext, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_mydata:// ????????????
                intent = new Intent(mContext, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_to_myinfo:// ????????????
                intent = new Intent(mContext, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_wait_pay:// ?????????
                intent = new Intent(mContext, StatusInfoActivity.class);
                intent.putExtra("index", 1);
                getActivity().startActivityForResult(intent, 10002);
                break;
            case R.id.rel_wait_deliver:// ?????????
                intent = new Intent(mContext, StatusInfoActivity.class);
                intent.putExtra("index", 2);
                getActivity().startActivityForResult(intent, 10002);
                break;
            case R.id.rel_wait_receipt:// ?????????
                intent = new Intent(mContext, StatusInfoActivity.class);
                intent.putExtra("index", 3);
                getActivity().startActivityForResult(intent, 10002);
                break;
            case R.id.rel_wait_judge:// ?????????
                intent = new Intent(mContext, StatusInfoActivity.class);
                intent.putExtra("index", 4);
                startActivityForResult(intent, 10002);
                break;
            case R.id.rel_wait_payback:// ????????????
                // intent = new Intent(getActivity(), PayBackActivity.class);
                // startActivity(intent);
                intent = new Intent(mContext, PaybackCommonFragmentActivity.class);
                intent.putExtra("flag", "payBackListFragment");
                startActivityForResult(intent, 10002);
                break;
            case R.id.rel_my_order:// ????????????
                intent = new Intent(mContext, StatusInfoActivity.class);
                intent.putExtra("index", 0);
                getActivity().startActivityForResult(intent, 10002);
                break;

            case R.id.rel_my_coupons:// ????????????
                intent = new Intent(mContext, MyCouponsActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_my_integral:// ????????????
                // intent = new Intent(mContext, DailySignActivity.class);
                // startActivity(intent);
                break;
            case R.id.rel_help_center:// ????????????
                intent = new Intent(mContext, HelpCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_super_vip:// ??????????????????
                // if (null == YCache.getCacheUserSafe(mContext).getIs_member()
                // || YCache.getCacheUserSafe(mContext).getIs_member().equals("0"))
                // {
                // intent = new Intent(mContext, MemberVerifyActivity.class);
                // startActivity(intent);
                // } else {// ??????98???????????????
                // intent = new Intent(mContext, MembersGoodsListActivity.class);
                // startActivity(intent);
                // }
                break;
            case R.id.rel_union_biz:// ???????????????
                // checkMerchaseSubmitInfo(v);
                // intent = new Intent(mContext, LeagueBusinessHomeActivity.class);
                // Bundle bundle = new Bundle();
                // bundle.putSerializable("mapObj", null);
                // ;
                // intent.putExtras(bundle);
                // startActivity(intent);

                break;
            case R.id.btn_setting: // ??????
                intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
            // case R.id.rel_red_bag: // ????????????
            // intent = new Intent(mContext, RedPacketsActivity.class);
            // startActivity(intent);
            // break;

            case R.id.rel_invite_friend: // ????????????
                intent = new Intent(mContext, InviteFriendActivity.class);
                intent.putExtra("userheard", info.getPic());
                startActivity(intent);
                break;
            // case R.id.rel_invite_member: // ????????????
            // if (null != mycenterCount && myStepCount != null) {
            // intent = new Intent(mContext, MyFootPrintActivity.class);
            // intent.putExtra("myStepCount", Integer.parseInt(myStepCount));
            // startActivityForResult(intent, 10001);
            // }
            // break;
            // case R.id.imgbtn_left_icon:// ????????????
            // intent = new Intent(mContext, ChatAllHistoryActivity.class);
            // startActivity(intent);
            // break;
            // case R.id.rel_shop_cart: // ?????????
            // intent = new Intent(mContext, ShopCartNewNewActivity.class);
            // startActivityForResult(intent, 10002);
            // break;
            default:
                break;
        }

        ((Activity) mContext).

                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);

    }

    // private RelativeLayout rel_invite_member;

    private RelativeLayout rl_to_myinfo;

    /**
     * ?????????????????????
     */
    private void updateUnreadLabel() {

        MainMenuActivity.instances.runOnUiThread(new Runnable() {
            public void run() {

                // ??????bottom bar???????????????---????????????
//                int count = getUnreadMsgCountTotal();
//                // ??????+??????
//                int countAll = count + huaTiCount + systemCount;
//                if (countAll > 0) {
//                    unreadLabel.setText(countAll + "");
//                    unreadLabel.setVisibility(View.VISIBLE);
//                } else {
                    unreadLabel.setVisibility(View.GONE);
//                }

            }
        });

    }

    /**
     * ?????????????????????
     *
     * @return
     */
//    public int getUnreadMsgCountTotal() {
//        int unreadMsgCountTotal = 0;
//        int chatroomUnreadMsgCount = 0;
//        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
//        /*
//         * for(EMConversation
//         * conversation:EMChatManager.getInstance().getAllConversations
//         * ().values()){ if(conversation.getType() ==
//         * EMConversationType.ChatRoom)
//         * chatroomUnreadMsgCount=chatroomUnreadMsgCount
//         * +conversation.getUnreadMsgCount(); }
//         */
//        return unreadMsgCountTotal - chatroomUnreadMsgCount;
//    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        info = YCache.getCacheUserSafe(mContext);
        // SetImageLoader.initImageLoader(mContext, img_user_button,
        // info.getPic(), "");
        PicassoUtils.initImage(mContext, info.getPic(), img_user_button);

    }

    // @Override
    // public void onScrollChanged(ObservableScrollView scrollView, int x, int
    // y, int oldx, int oldy) {
    // // TODO Auto-generated method stub
    // if (scrollView.getScrollY() > argY) {
    // rel_title.getBackground().setAlpha(255 * 50 / 100);
    // } else {
    // rel_title.getBackground().setAlpha(255 * 0 / 100);
    // }
    //
    // }

    public void deleteWalletRed() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    ComModel2.deleteWalletRed(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }

            ;

        }.execute();
    }

    public void deleteCouponsRed() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    ComModel2.deleteCouponsRed(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }

            ;

        }.execute();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                if (YJApplication.instance.isLoginSucess()) {
                    huaTiCount = Integer.parseInt(SharedPreferencesUtil.getStringData(mContext,
                            Pref.TOPIC_MESSAGE + YCache.getCacheUser(mContext).getUser_id(), "0"));
//				systemCount=Integer.parseInt(SharedPreferencesUtil.getStringData(mContext,
//						Pref.SYSTEM_MESSAGE + YCache.getCacheUser(mContext).getUser_id(), "0"));
                }


            }
            super.handleMessage(msg);
        }

        ;
    };

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // ???????????????:????????????
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    ;

    private MyTimerTask mTask;
    Timer timer = new Timer();
}
