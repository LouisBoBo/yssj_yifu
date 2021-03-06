package com.yssj.ui.fragment.orderinfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.MyListView;
import com.yssj.custom.view.PayPasswordCustomDialog;
import com.yssj.custom.view.PayPasswordCustomDialog.InputDialogListener;
import com.yssj.entity.CheckPwdInfo;
import com.yssj.entity.DJKdetail;
import com.yssj.entity.Order;
import com.yssj.entity.OrderShop;
import com.yssj.entity.RemainShipInfo;
import com.yssj.entity.ReturnInfo;
import com.yssj.eventbus.MessageEvent;
import com.yssj.huanxin.activity.ChatAllHistoryActivity;
import com.yssj.model.ComModel;
import com.yssj.model.ComModel2;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.activity.infos.AppendEvaluateOrderNewActivity;
import com.yssj.ui.activity.infos.EvaluateOrderNewActivity;
import com.yssj.ui.activity.infos.LogisticsInfoActivity;
import com.yssj.ui.activity.infos.SetMyPayPassActivity;
import com.yssj.ui.activity.payback.PaybackCommonFragmentActivity;
import com.yssj.ui.activity.shopdetails.OrderPaymentActivity;
import com.yssj.ui.activity.shopdetails.ShaiDanActivity;
import com.yssj.ui.base.BasicActivity;
import com.yssj.utils.CommonUtils;
import com.yssj.utils.DateUtil;
import com.yssj.utils.DialogUtils;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.SingleChoicePopupWindow;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.WXminiAppUtil;
import com.yssj.utils.YCache;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*****
 * ??????????????????
 *
 * @author Administrator
 *
 */
public class OrderDetailsActivity extends BasicActivity implements OnClickListener {
    private MyListView listview;

    private Order order; // ??????
    private OrderListSonAdapter adapter;

    private TextView tvTitle_base;

    private TextView tv_order_status, tv_order_amount, tv_order_no, tv_order_time, tv_receiver, tv_phone,
            tv_detail_address, tv_money;

    private TextView tv_logistic_info, tv_add_time, tv_postage;
    private View logistic_divider;
    private RelativeLayout rel_logistic, main;

    private RelativeLayout rel_prepay, rel_prereceive, rel_prejudge;// ??????????????????????????????????????????????????????

    private RelativeLayout rel_pre_diliver, rel_pre_receive_btns, rel_finish;// ???????????????????????????????????????
    // ????????????????????????
    private LinearLayout rel_appendjudge;

    private RelativeLayout rel_dateout; // ??????????????????

    private RelativeLayout rel_extends_receive;// ??????????????????
    private List<HashMap<String, Object>> mapLogistic;

    private PayPasswordCustomDialog customDialog;

    private InputDialogListener inputDialogListener;

    private Button btn_payback;
    private ImageView img_right_icon;
    protected AlertDialog dialog;
    private List<String> cancleOrderDatas = new ArrayList<String>(); // ????????????

    public static OrderDetailsActivity instance;
    private String shop_code;// ??????????????????????????????
    private String sign_huodong;
    private boolean paySuccess;
    private Context context;

    private void getCancleOrderDatas() {
        cancleOrderDatas.add("????????????");
        cancleOrderDatas.add("???????????????????????????");
        cancleOrderDatas.add("???????????????");
        cancleOrderDatas.add("??????????????????");
        cancleOrderDatas.add("?????????");
        cancleOrderDatas.add("????????????");
    }

    private Date date;
    private long timeout;
    private Long now;
    private TextView mRemainningTime;
    private long recLen;
    Timer timer = new Timer();
    private String TAG = "OrderDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detalis);
        context = this;
        EventBus.getDefault().register(this);//??????

//		aBar.hide();
        Bundle b = getIntent().getExtras();
        order = (Order) b.getSerializable("order");
        sign_huodong = getIntent().getStringExtra("sign_huodong");
        shop_code = getIntent().getStringExtra("shop_code");
        date = order.getLast_time();
        paySuccess = getIntent().getBooleanExtra("paySuccess", false);
        timeout = date.getTime();
        now = getIntent().getLongExtra("nowTime", System.currentTimeMillis());
        LogYiFu.e(TAG, now + "iiiiiiiii");
        // now = order.getNowss();
        // now = System.currentTimeMillis();
        // System.out.println("//___" + null == order.getNowss().toString());
        // recLen = timeout - Long.parseLong(order.getNowss().toString());
        recLen = timeout - now;
        listChecked.add(order);

        instance = this;

        // System.out.println("order:" + order);
        // if (order == null || order.getList() == null) {
        // Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
        // finish();
        // return;
        // }
//        if (paySuccess) {
//            DialogUtils.paySuccessDialog(this, order.getList().get(0).getShop_price());
//
//        }

        if (order == null) {
            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initView();
        if (order.getShop_from() == 3 || order.getShop_from() == 4 || order.getShop_from() == 6) {
            listview.setSelector(R.drawable.sign_order_details_selector);
        }
        timer.schedule(task, 0, 1000); // timeTask

        //???????????????
        initFHK();
    }

    private void initFHK() {
        if(OrderListAdapter.sqfhClickEd){
            return;
        }

        if (order.getShop_from() == 13//????????????
                && order.getWhether_prize() == 2
                && order.getNew_free() == 1
                && OrderInfoFragment.send_num > 0
                && OrderInfoFragment.send_num < 3
                && !order.getStatus().equals("9")) {
            DialogUtils.showNewUserFirstOrderDialog(instance, order, OrderInfoFragment.send_num, 0);//???????????????????????????????????????????????????


        }


    }

    // ?????????
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen -= 1000;
                    String days;
                    String hours;
                    String minutes;
                    String seconds;
                    long minute = recLen / 60000;
                    long second = (recLen % 60000) / 1000;
                    if (minute >= 60) {
                        long hour = minute / 60;
                        minute = minute % 60;
                        if (hour >= 24) {
                            long day = hour / 24;
                            hour = hour % 24;
                            if (day < 10) {
                                days = "0" + day;
                            } else {
                                days = "" + day;
                            }
                            if (hour < 10) {
                                hours = "0" + hour;
                            } else {
                                hours = "" + hour;
                            }
                            if (minute < 10) {
                                minutes = "0" + minute;
                            } else {
                                minutes = "" + minute;
                            }
                            if (second < 10) {
                                seconds = "0" + second;
                            } else {
                                seconds = "" + second;
                            }
                            mRemainningTime.setText("" + days + "???" + hours + "???" + minutes + "???" + seconds + "???");
                        } else {
                            if (hour < 10) {
                                hours = "0" + hour;
                            } else {
                                hours = "" + hour;
                            }
                            if (minute < 10) {
                                minutes = "0" + minute;
                            } else {
                                minutes = "" + minute;
                            }
                            if (second < 10) {
                                seconds = "0" + second;
                            } else {
                                seconds = "" + second;
                            }
                            mRemainningTime.setText("" + hours + "???" + minutes + "???" + seconds + "???");
                        }
                    } else if (minute >= 10 && second >= 10) {
                        mRemainningTime.setText("" + minute + "???" + second + "???");
                    } else if (minute >= 10 && second < 10) {
                        mRemainningTime.setText("" + minute + "???0" + second + "???");
                    } else if (minute < 10 && second >= 10) {
                        mRemainningTime.setText("0" + minute + "???" + second + "???");
                    } else {
                        mRemainningTime.setText("0" + minute + "???0" + second + "???");
                    }
                    // mRemainningTime.setText("" + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        mRemainningTime.setText("??????????????????");
                        // mRemainningTime.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    private void initView() {

        findViewById(R.id.img_back).setOnClickListener(this);
        tvTitle_base = (TextView) findViewById(R.id.tvTitle_base);
        tvTitle_base.setText("????????????");

        /*
         * ??????????????????
         */
        main = (RelativeLayout) findViewById(R.id.main);
        main.setBackgroundColor(Color.WHITE);
        img_right_icon = (ImageView) findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.VISIBLE);
        img_right_icon.setImageResource(R.drawable.mine_message_center);
        img_right_icon.setOnClickListener(this);
        img_right_icon.setVisibility(View.GONE);
        findViewById(R.id.lin_contact_seller).setOnClickListener(this);// ????????????
        findViewById(R.id.lin_call).setOnClickListener(this);// ????????????

        tv_order_status = (TextView) findViewById(R.id.tv_order_status);// ????????????
        tv_order_amount = (TextView) findViewById(R.id.tv_order_amount);// ???????????????
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);// ????????????
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);// ??????????????????
        tv_receiver = (TextView) findViewById(R.id.tv_receiver);// ?????????
        tv_phone = (TextView) findViewById(R.id.tv_phone);// ????????????
        tv_detail_address = (TextView) findViewById(R.id.tv_detail_address);// ????????????

        tv_money = (TextView) findViewById(R.id.tv_money);// ????????????

        rel_logistic = (RelativeLayout) findViewById(R.id.rel_logistic);
        rel_logistic.setOnClickListener(this);

        tv_logistic_info = (TextView) findViewById(R.id.tv_logistic_info);
        tv_add_time = (TextView) findViewById(R.id.tv_add_time);

        logistic_divider = findViewById(R.id.logistic_divider);
        rel_prepay = (RelativeLayout) findViewById(R.id.rel_prepay);
        findViewById(R.id.btn_cancel_order).setOnClickListener(this);// ????????????
        findViewById(R.id.btn_pay).setOnClickListener(this);// ??????
        mRemainningTime = (TextView) findViewById(R.id.remainning_time);// ?????????
        rel_prereceive = (RelativeLayout) findViewById(R.id.rel_prereceive);
        findViewById(R.id.btn_ck_logistic).setOnClickListener(this);// ????????????
        findViewById(R.id.btn_yc_sh).setOnClickListener(this);// ????????????
        findViewById(R.id.btn_qrsh).setOnClickListener(this);// ????????????
        rel_prejudge = (RelativeLayout) findViewById(R.id.rel_prejudge);
        findViewById(R.id.btn_judge).setOnClickListener(this);// ????????????

        findViewById(R.id.btn_prejudge_delete).setOnClickListener(this);// ????????????

        rel_appendjudge = (LinearLayout) findViewById(R.id.rel_appendjudge); // ??????????????????

        Button btn_appendjudge = (Button) findViewById(R.id.btn_appendjudge);
        findViewById(R.id.btn_appendjudge).setOnClickListener(this);// ????????????
        findViewById(R.id.btn_appendjudge_delete).setOnClickListener(this);// ????????????

        rel_dateout = (RelativeLayout) findViewById(R.id.rel_dateout);
        findViewById(R.id.btn_delete_dateout).setOnClickListener(this); // ??????????????????

        rel_pre_diliver = (RelativeLayout) findViewById(R.id.rel_pre_diliver);
        findViewById(R.id.btn_diliver_remind).setOnClickListener(this);// ????????????

        rel_pre_receive_btns = (RelativeLayout) findViewById(R.id.rel_pre_receive_btns);
        btn_payback = (Button) findViewById(R.id.btn_payback);
        btn_payback.setOnClickListener(this);// ??????
        btn_payback.setVisibility(View.GONE);

        rel_extends_receive = (RelativeLayout) findViewById(R.id.rel_extends_receive);
        findViewById(R.id.btn_ck_logistic7).setOnClickListener(this);
        findViewById(R.id.btn_qrsh7).setOnClickListener(this);

        rel_finish = (RelativeLayout) findViewById(R.id.rel_finish);
        findViewById(R.id.btn_service).setOnClickListener(this);// ????????????

        listview = (MyListView) findViewById(R.id.listview);// ????????????
        if ("sign_huodong".equals(sign_huodong)) {
            adapter = new OrderListSonAdapter(this, order, false, true, "" + shop_code);
        } else {
            adapter = new OrderListSonAdapter(this, order, false, false, "");
        }

        listview.setAdapter(adapter);

        tv_postage = (TextView) findViewById(R.id.tv_postage);

        if (order.getShop_from() == 4 || order.getShop_from() == 6) {
            btn_appendjudge.setVisibility(View.GONE);
        }

        refreshView();
    }

    /****
     * ????????????
     */
    private void refreshView() {
        adapter.notifyDataSetChanged();
        int status = Integer.parseInt(order.getStatus().toString());
        switch (status) {
            case 1:
                if (order.getLast_time().before(new Date(now))) {
                    tv_order_status.setText("?????????");
                    rel_logistic.setVisibility(View.GONE);
                    rel_dateout.setVisibility(View.VISIBLE);
                } else {
                    if (order.getPay_status() == 1) {
                        tv_order_status.setText("?????????");
                    } else {
                        tv_order_status.setText("?????????");
                    }
                    logistic_divider.setVisibility(View.GONE);
                    rel_logistic.setVisibility(View.GONE);
                    rel_prepay.setVisibility(View.VISIBLE);
                }

                break;
            case 2:


                if (order.getWhether_prize() == 2 || order.getNew_free() == 1) {
                    tv_order_status.setText("???????????????");
                } else if (order.getWhether_prize() == 3) {
                    tv_order_status.setText("?????????");
                } else {
                    tv_order_status.setText("?????????");

                }


                logistic_divider.setVisibility(View.GONE);
                rel_logistic.setVisibility(View.GONE);
                rel_pre_diliver.setVisibility(View.VISIBLE);
                break;
            case 3:
                getLogistics(order);
                tv_order_status.setText("?????????");
                rel_prereceive.setVisibility(View.VISIBLE);
                // rel_pre_receive_btns.setVisibility(View.VISIBLE);
                break;
            case 4:
                getLogistics(order);
                tv_order_status.setText("?????????");
                rel_prejudge.setVisibility(View.VISIBLE);
                break;
            case 5:
                getLogistics(order);
                tv_order_status.setText("?????????");
                rel_appendjudge.setVisibility(View.VISIBLE);
                break;
            case 6:
                getLogistics(order);
                tv_order_status.setText("?????????");
                // rel_finish.setVisibility(View.VISIBLE);
                break;

            case 7:
                getLogistics(order);
                tv_order_status.setText("????????????");
                rel_extends_receive.setVisibility(View.VISIBLE);
                rel_pre_receive_btns.setVisibility(View.VISIBLE);
                break;
            case 9:
                // getLogistics(order);
                tv_order_status.setText("????????????");
                rel_logistic.setVisibility(View.GONE);
                break;
            case 14:

                // getLogistics(order);
                tv_order_status.setText("???????????????");
                rel_logistic.setVisibility(View.GONE);
                break;
            case 17:

                // getLogistics(order);
                tv_order_status.setText("???????????????");
                rel_logistic.setVisibility(View.GONE);
                break;
            case 21:

                // getLogistics(order);
                tv_order_status.setText("?????????");
                rel_logistic.setVisibility(View.GONE);
                break;

        }
        String addTime = DateUtil.FormatMillisecond(order.getAdd_time());
        if (!TextUtils.isEmpty(addTime)) {
            tv_order_time.setText("???????????????" + addTime);
        }
        String order_price = "" + order.getOrder_price();
//        if (!TextUtils.isEmpty(order_price)) {
//
//            // ????????????
//            tv_order_amount.setText("????????????(??????): " + new java.text.DecimalFormat("#0.00").format(order.getOrder_price()));
//
//            // ??????
//            tv_money.setText("??" + new java.text.DecimalFormat("#0.00").format(order.getPay_money()));
//        }

        tv_money.setText("??" + new java.text.DecimalFormat("#0.00").format(order.getPay_money()));
        tv_order_amount.setText("????????????(??????): ??" + new java.text.DecimalFormat("#0.00").format(order.getPay_money()));

        String consignee = order.getConsignee();
        if (!TextUtils.isEmpty(consignee)) {
            tv_receiver.setText("???????????? " + consignee);
        }
        String phone = order.getPhone();
        if (!TextUtils.isEmpty(phone)) {
            tv_phone.setText("????????? " + phone);
        }

        String address = order.getAddress();
        if (!TextUtils.isEmpty(address)) {
            tv_detail_address.setText("?????????????????? " + address);
        }
        String order_code = order.getOrder_code();
        if (!TextUtils.isEmpty(order_code)) {
            tv_order_no.setText("????????? ??? " + order_code);
        }
        if (null != order.getPostage())
            tv_postage.setText("??" + new java.text.DecimalFormat("#0.00").format(order.getPostage()));
    }

    // ?????????100???????????????
    private void getLogistics(final Order order) {

        if (order.getShop_from() == 10 || order.getLogi_code()==null) {
            rel_logistic.setVisibility(View.GONE);
            return;
        }

        new SAsyncTask<Void, Void, List<HashMap<String, Object>>>(this, R.string.wait) {

            @Override
            protected List<HashMap<String, Object>> doInBackground(FragmentActivity context, Void... params)
                    throws Exception {
                return ComModel2.getLogistics(context, order.getLogi_code());
            }

            @Override
            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context, List<HashMap<String, Object>> result, Exception e) {
                super.onPostExecute(context, result, e);
                if (null == e) {

                    if (null != result && 0 != result.size()) {
                        tv_logistic_info.setText((CharSequence) result.get(0).get("context"));
                        tv_add_time.setText((CharSequence) result.get(0).get("time"));
                        logistic_divider.setVisibility(View.VISIBLE);
                        rel_logistic.setVisibility(View.VISIBLE);
                    } else {
                        rel_logistic.setVisibility(View.GONE);
                    }
                    OrderDetailsActivity.this.mapLogistic = result;
                }
            }

        }.execute();
    }

    private void callPhone(View v) {
        new SAsyncTask<Integer, Void, String>(OrderDetailsActivity.this, v, R.string.wait) {

            @Override
            protected boolean isHandleException() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            protected String doInBackground(FragmentActivity context, Integer... params) throws Exception {
                // TODO Auto-generated method stub
                return ComModel2.getSuppPhone(context, params[0]);
            }

            @Override
            protected void onPostExecute(FragmentActivity context, String result, Exception e) {
                super.onPostExecute(context, result, e);
                // if (null == e) {
                // if (null != result && !"null".equals(result)) {
                // // ???intent??????????????????
                // Intent intent = new Intent(Intent.ACTION_DIAL,
                // Uri.parse("tel:" + result));
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);
                // } else {
                String number = "4008884224"; // ????????????
                // ???intent??????????????????
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            // }
            // }

        }.execute(order.getSupp_id());
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.btn_cancel_order:// ????????????
                if (order.getPay_status() == 1) {
                    Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                cancleOrder();
                break;
            case R.id.img_back:// ????????????

                // MealPaymentActivity.instance.finish();
                OrderDetailsActivity.instance.finish();

                break;
            case R.id.img_right_icon:// ????????????
                WXminiAppUtil.jumpToWXmini(this);

                break;
            case R.id.btn_pay:// ??????
                // goToPay(order);
//			long nowTimes = new Date().getTime();
//			long issueEndTime = order.getIssue_endtime();
//			if ((order.getShop_from() == 4||order.getShop_from() == 6) && issueEndTime != 0 && nowTimes >= issueEndTime) {
//				ToastUtil.showShortText(context, "???????????????????????????");
//				return;
//			}
                if (order.getPay_status() == 1) {
                    Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                intentTOPayment(listChecked.get(0).getOrder_code(), listChecked.get(0).getOrder_price(), false);
                break;
            case R.id.lin_call:// ????????????
                callPhone(v);
                break;
            case R.id.btn_ck_logistic7:// ????????????-????????????
            case R.id.rel_logistic:// ????????????
            case R.id.btn_ck_logistic:// ?????????-????????????
                intent = new Intent(this, LogisticsInfoActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("result", (Serializable) mapLogistic);
                intent.putExtras(bundle);
                bundle = new Bundle();
                bundle.putSerializable("order", order);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_yc_sh:// ?????????-????????????
                customDialog(1, 0);
                break;
            case R.id.btn_qrsh7:
            case R.id.btn_qrsh:// ?????????-????????????
                affirmOrder(v, order);
                break;
            case R.id.btn_judge:// ?????????-????????????
                boolean isIn = false;

                if (order.getShop_from() == 4 || order.getShop_from() == 6) {

                    Intent intent2 = new Intent(context, ShaiDanActivity.class);
                    intent2.putExtra("in_code", order.getParticipation_code());
                    intent2.putExtra("shop_code", order.getBak());
                    intent2.putExtra("shop_name", order.getOrder_name());
                    intent2.putExtra("issue_code", order.getIssue_code());
                    context.startActivity(intent2);
                } else {

                    for (int i = 0; i < order.getList().size(); i++) {
                        OrderShop os = order.getList().get(i);
                        if (os.getChange() == 1) {
                            if (os.getStatus() == 1) {// ???????????????
                                continue;
                            }
                        }
                        if (os.getChange() == 2) {
                            if (os.getStatus() == 1 || os.getStatus() == 3) {// ???????????????????????????
                                continue;
                            }
                        }
                        if (os.getChange() == 3) {
                            if (os.getStatus() == 1 || os.getStatus() == 3) {// ???????????????????????????
                                continue;
                            }
                        }
                        isIn = true;
                    }
                    if (!isIn) {
                        ToastUtil.showShortText(context, "????????????????????????????????????");
                        return;
                    }
                    intent = new Intent(this, EvaluateOrderNewActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    // finish();
                }
                break;
            case R.id.btn_prejudge_delete:// ?????????-????????????
                // escOrder(order.getOrder_code());
                customDialog(2, 1);
                break;
            case R.id.btn_service:// ????????????
                /*
                 * intent = new Intent(this, ThhActivity.class); bundle = new
                 * Bundle(); bundle.putSerializable("order", order);
                 * intent.putExtras(bundle); startActivity(intent);
                 */
                intent = new Intent(context, PaybackCommonFragmentActivity.class);
                intent.putExtra("order_code", order.getOrder_code());
                intent.putExtra("flag", "payBackChoiceServiceFragment");
                intent.putExtra("order_price", order.getOrder_price() + "");
                if ("4".equals(order.getStatus()) || "5".equals(order.getStatus()) || "6".equals(order.getStatus())) {
                    if (order.getIs_kick() == 1) {
                        intent.putExtra("isHH", true);
                    }

                }
                startActivity(intent);

                break;
            case R.id.btn_payback:// ??????
                /*
                 * intent = new Intent(this, ThhActivity.class); bundle = new
                 * Bundle(); bundle.putSerializable("order", order);
                 * intent.putExtras(bundle); startActivity(intent);
                 */

                intent = new Intent(context, PaybackCommonFragmentActivity.class);
                intent.putExtra("order_code", order.getOrder_code());
                intent.putExtra("flag", "tKFragment");
                intent.putExtra("order_price", order.getOrder_price() + "");
                startActivity(intent);

                break;
            case R.id.lin_contact_seller:// ????????????
//                intent = new Intent(OrderDetailsActivity.this, ChatActivity.class);
//                intent.putExtra("userId", SharedPreferencesUtil.getStringData(context, "kefuNB", "0"));
//                intent.putExtra("sell", "buyyer");
//                startActivity(intent);
                WXminiAppUtil.jumpToWXmini(this);

                break;
            case R.id.btn_diliver_remind:// ????????????
                // intent = new Intent(OrderDetailsActivity.this,
                // ChatActivity.class);
                // intent.putExtra("userId", String.valueOf(order.getSupp_id()));
                // startActivity(intent);

                remainOrdershop(order.getOrder_code());

                break;

            case R.id.btn_appendjudge: // ????????????
                intent = new Intent(OrderDetailsActivity.this, AppendEvaluateOrderNewActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("order", order);
                intent.putExtras(bundle2);
                startActivity(intent);
                break;
            case R.id.btn_appendjudge_delete: // ????????????
                customDialog(2, 2);
                break;
            case R.id.btn_delete_dateout: // ??????????????????
                customDialog(2, 2);
                break;
        }
    }

    /**
     * ????????????????????????????????????????????????
     */
    private void cancleOrder() {
        cancleOrderDatas.clear();
        getCancleOrderDatas();
        // ?????????????????????-----------???????????????????????????
        final SingleChoicePopupWindow menuWindow;
        menuWindow = new SingleChoicePopupWindow(context, cancleOrderDatas);
        menuWindow.setTitle("????????????");

        // ????????????
        menuWindow.showAtLocation(OrderDetailsActivity.this.findViewById(R.id.main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // ??????layout???PopupWindow??????????????????

        menuWindow.setOnOKButtonListener(new OnClickListener() {

            public void onClick(View v) {
                menuWindow.dismiss();
                String selItem = menuWindow.getSelectItem();
                escOrder(order.getOrder_code(), selItem);

            }
        });
    }

    // ????????????????????????????????????
    private void customDialog(int type, final int flag) {
        AlertDialog.Builder builder = new Builder(OrderDetailsActivity.this);
        // ???????????????????????????
        View view = View.inflate(OrderDetailsActivity.this, R.layout.payback_esc_apply_dialog, null);
        TextView tv_des = (TextView) view.findViewById(R.id.tv_des);

        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setBackgroundResource(R.drawable.payback_esc_apply_esc);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ???????????????????????????
                dialog.dismiss();
            }
        });

        switch (type) {
            case 1:
                tv_des.setText("  ??????????????????????????????\n?????????????????????????????????");
                ok.setOnClickListener(new OnClickListener() { // ????????????

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        extensionOrdershop(order.getOrder_code());
                    }
                });
                break;
            case 2:
                tv_des.setText("????????????????????????");
                ok.setOnClickListener(new OnClickListener() { // ????????????

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        returnShop(order.getOrder_code(), flag);
                    }
                });
                break;

        }

        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    /******
     * ????????????
     *
     * @param order_code
     */
    private void extensionOrdershop(String order_code) {
        new SAsyncTask<String, Void, ReturnInfo>(this, null, R.string.wait) {
            @Override
            protected ReturnInfo doInBackground(FragmentActivity context, String... params) throws Exception {

                return ComModel.extensionOrdershop(context, YCache.getCacheToken(context), params[0]);
            }

            @Override
            protected void onPostExecute(FragmentActivity context, ReturnInfo r, Exception e) {

                if (e != null) {// ????????????
                    LogYiFu.e("?????? -----", context.getString(R.string.ss));
                    Toast.makeText(context, R.string.ee, Toast.LENGTH_SHORT).show();
                } else {// ???????????????????????????????????????
                    Toast.makeText(context, "????????????????????????", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }

            }

            ;

            @Override
            protected boolean isHandleException() {
                return true;
            }

            ;
        }.execute(order_code);

    }

    /******
     * ????????????
     *
     * @param order_code
     */
    private void remainOrdershop(String order_code) {
        new SAsyncTask<String, Void, RemainShipInfo>(this, null, R.string.wait) {
            @Override
            protected RemainShipInfo doInBackground(FragmentActivity context, String... params) throws Exception {
                return ComModel.urgesuppShipments(context, params[0]);
            }

            @Override
            protected void onPostExecute(FragmentActivity context, RemainShipInfo info, Exception e) {

                if (e != null) {// ????????????
                    LogYiFu.e("?????? -----", context.getString(R.string.ss));
                    Toast.makeText(context, R.string.fh, Toast.LENGTH_SHORT).show();
                } else {// ??????????????????
                    if (info != null && "1".equals(info.getStatus()) && 1 == info.getFalg()) {
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
                    } else if (info != null && "1".equals(info.getStatus()) && 2 == info.getFalg()) {
                        Toast.makeText(context, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            ;

            @Override
            protected boolean isHandleException() {
                return true;
            }

            ;
        }.execute(order_code);

    }

    // ???????????????????????????
    private void affirmOrder(View v, final Order order) {
        // WalletDialog dlgDialog = new WalletDialog(this, R.style.DialogStyle);
        // dlgDialog.show();
        // dlgDialog.listener = new WalletDialog.OnCallBackPayListener() {
        //
        // @Override
        // public void selectPwd(String pwd) {
        // affirmOrder(order, pwd);
        //
        // }
        // };

        // ?????????????????????????????????

        new SAsyncTask<Void, Void, CheckPwdInfo>(this, v, R.string.wait) {

            @Override
            protected CheckPwdInfo doInBackground(FragmentActivity context, Void... params) throws Exception {
                return ComModel.checkPWD(context);
            }

            @Override
            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context, CheckPwdInfo result, Exception e) {
                super.onPostExecute(context, result, e);

                if (null == e) {
                    if (result != null && "1".equals(result.getStatus()) && "1".equals(result.getFlag())) {

                        // ?????????????????????
                        customDialog();

                    } else if (result != null && "1".equals(result.getStatus()) && "2".equals(result.getFlag())) {
                        // ????????????????????????

                        customDialog = new PayPasswordCustomDialog(context, R.style.mystyle, R.layout.pay_customdialog,
                                "????????????", null);

                        inputDialogListener = new InputDialogListener() {

                            @Override
                            public void onOK(String pwd) {
                                // ??????????????????????????????
                                // ToastUtil.showLongText(context, "??????????????????" +
                                // text);
                                affirmOrder(order, pwd);
                            }

                            @Override
                            public void onCancel() {
                                // TODO Auto-generated method stub

                            }

                        };
                        customDialog.setListener(inputDialogListener);
                        customDialog.show();

                    } else {
                        ToastUtil.showLongText(context, "??????????????????~~~");
                    }
                }
            }

        }.execute((Void[]) null);

        // TextView tvAccount = (TextView) customDialog
        // .findViewById(R.id.tv_account);
        // tvAccount.setText(totalAccount + "???");
    }

    // ????????????
    private void affirmOrder(final Order order, final String pwd) {
        new SAsyncTask<Void, Void, ReturnInfo>(this, R.string.wait) {

            @Override
            protected ReturnInfo doInBackground(FragmentActivity context, Void... params) throws Exception {
                // TODO Auto-generated method stub
                return ComModel2.affirmOrder(context, order.getOrder_code(), pwd, 0);
            }

            @Override
            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context, ReturnInfo result, Exception e) {
                // TODO Auto-generated method stub
                super.onPostExecute(context, result, e);
                if (null == e) {
                    if (result != null && "1".equals(result.getStatus())) {
                        ToastUtil.showShortText(context, result.getMessage());
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtil.showShortText(context, "??????????????????~~~");
                    }
                }
            }

        }.execute();
    }

    /****
     * ????????????
     *
     * @param order_code
     */

    private void escOrder(String order_code, final String reason) {

        new SAsyncTask<String, Void, ReturnInfo>(OrderDetailsActivity.this, null, R.string.wait) {
            @Override
            protected ReturnInfo doInBackground(FragmentActivity context, String... params) throws Exception {

                return ComModel.escOrder(context, YCache.getCacheToken(context), params[0], reason);
            }

            @Override
            protected void onPostExecute(FragmentActivity context, ReturnInfo r, Exception e) {

                if (e != null) {// ????????????
                    LogYiFu.e("?????? -----", context.getString(R.string.ss));//
                    Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
                } else {// ???????????????????????????????????????
                    Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveBooleanData(context, "signDATAneedRefresh", true);
                    setResult(RESULT_OK);
//                    if (YJApplication.isLogined || YJApplication.instance.isLoginSucess()) {
//                        // ??????????????????????????????
//                        GetJinBiJinQuanUtils.getJinBi(context);
//                        GetJinBiJinQuanUtils.getJinQuan(context);
//                    }
                    finish();
                }

            }

            ;

            @Override
            protected boolean isHandleException() {
                return true;
            }

            ;
        }.execute(order_code);
    }

    private List<Order> listChecked = new ArrayList<Order>();

    private void intentTOPayment(String order_code, double price, boolean isMulti) {
        Intent intent = new Intent(this, OrderPaymentActivity.class);
        if (order.getShop_from() == 3) {
            intent.putExtra("signShopDetail", "SignShopDetail");
            intent.putExtra("signType", order.getSignType());
        } else if (order.getShop_from() == 4 || order.getShop_from() == 6) {
            intent.putExtra("isDuobao", true);
            intent.putExtra("signType", order.getSignType());
        }
        intent.putExtra("single", "single");
        intent.putExtra("order_code", order_code);
        intent.putExtra("totlaAccount", price);
        intent.putExtra("isMulti", isMulti);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listOrder", (Serializable) listChecked);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /****
     * ????????????
     *
     * @param order_code
     */
    private void returnShop(String order_code, final int type) {
        new SAsyncTask<String, Void, ReturnInfo>(this, null, R.string.wait) {
            @Override
            protected ReturnInfo doInBackground(FragmentActivity context, String... params) throws Exception {

                return ComModel.returnShop(context, YCache.getCacheToken(context), params[0]);
            }

            @Override
            protected void onPostExecute(FragmentActivity context, ReturnInfo r, Exception e) {

                if (e != null) {// ????????????
                    LogYiFu.e("?????? -----", context.getString(R.string.ss));
                    Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                } else {// ???????????????????????????????????????
                    Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();

                    Intent intent = getIntent();
                    if (type == 1) {
                        intent.putExtra("index", 4);
                    } else if (type == 2) {
                        intent.putExtra("index", 0);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

            ;

            @Override
            protected boolean isHandleException() {
                return true;
            }

            ;
        }.execute(order_code);

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 1001) { // ????????????
            setResult(1);
        } else if (arg0 == 1002 && arg1 == 102) { // ??????????????????????????????
            CommonUtils.finishActivity(MainMenuActivity.instances);

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("index", 3);
            startActivity(intent);
            finish();
        }
    }

    private void customDialog() {
        AlertDialog.Builder builder = new Builder(this);
        // ???????????????????????????
        View view = View.inflate(this, R.layout.payback_esc_apply_dialog, null);
        TextView tv_des = (TextView) view.findViewById(R.id.tv_des);
        tv_des.setText("???????????????????????????????????????????????????");

        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setBackgroundResource(R.drawable.payback_esc_apply_esc);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ???????????????????????????
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this, SetMyPayPassActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//????????????
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BuyVipSucEvent(MessageEvent messageEvent) {
        if (messageEvent.getEventBuyVipSucVipType() == 8) {

            HashMap<String, String> map = new HashMap<>();
            map.put("order_code", order.getOrder_code());
            YConn.httpPost(context, YUrl.QUERY_DJK_DETAIL, map, new HttpListener<DJKdetail>() {
                @Override
                public void onSuccess(DJKdetail djKDetail) {
                    if (djKDetail.getIsDeliver() == 1) {//???????????????????????????

                        tv_order_status.setText("?????????");
                        rel_pre_diliver.setVisibility(View.VISIBLE);

                    } else {
                        DialogUtils.showNewUserFirstOrderDialog(instance, order, OrderInfoFragment.send_num, 0);//???????????????????????????????????????????????????

                    }

                }

                @Override
                public void onError() {

                }
            });


        }
    }


}
