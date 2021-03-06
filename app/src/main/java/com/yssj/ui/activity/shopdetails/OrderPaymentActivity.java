package com.yssj.ui.activity.shopdetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yssj.YConstance;
import com.yssj.YConstance.Config;
import com.yssj.YConstance.Pref;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.activity.wxapi.WXPayEntryActivity;
import com.yssj.activity.wxapi.WXPayEntryActivity.OnWxpayFinish;
import com.yssj.alipay.PayResult;
import com.yssj.alipay.PayUtil;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.PayPasswordCustomDialog;
import com.yssj.custom.view.PayPasswordCustomDialog.InputDialogListener;
import com.yssj.entity.CheckPwdInfo;
import com.yssj.entity.Order;
import com.yssj.entity.ReturnInfo;
import com.yssj.model.ComModel;
import com.yssj.model.ComModel2;
import com.yssj.ui.HomeWatcherReceiver;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.GuideActivity;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.activity.WithdrawalLimitActivity;
import com.yssj.ui.activity.infos.SetMyPayPassActivity;
import com.yssj.ui.base.BasicActivity;
import com.yssj.ui.dialog.PayErrorDialog;
import com.yssj.ui.fragment.orderinfo.OrderDetailsActivity;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.MD5Tools;
import com.yssj.utils.QRCreateUtil;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.TongJiUtils;
import com.yssj.utils.YunYingTongJi;
import com.yssj.wxpay.WxPayUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ???????????????
 *
 * @author Administrator
 *
 */
public class OrderPaymentActivity extends BasicActivity implements OnClickListener, OnWxpayFinish {
	private LinearLayout llWalletPay;
	private LinearLayout llQuickPay;
	private LinearLayout llWXinPay;
	private LinearLayout llAliPay;
	private LinearLayout llUnionPay, btnPay;
	private LinearLayout llMydeayPay;
	public static MealSubmitOrderActivity instance;
	private ImageView ivWalletPay, ivQuickPay, ivWXinPay, ivAliPay, ivUnionPay, ivMydearPay;
	private TextView tvTotalAccount;
	private final int SDK_PAY_FLAG = 1;
	private ImageView imgCancle;
	private HashMap<Integer, Boolean> payTypeMap;
	private ArrayList<ImageView> imgSelectList;
	private IWXAPI msgApi;
	private String orderNo;
	// private List<ShopCart> listGoods;
	private HashMap<String, Object> resultMap;
	private double totalAccount;
	private PayPasswordCustomDialog customDialog;
	private InputDialogListener inputDialogListener;
	// private YJApplication yjapp;
	private boolean isMulti;
	private List<Order> listOrder;
	private Order order;
	private String signShopDetail;
	private int signType;// ?????? ????????????
	private boolean isDuobao;
	private List<Order> shareOrders;
	private RelativeLayout root;
	private TimeCount timeCount;
	private RelativeLayout rel_pay_success, rel_nomal;
	private TextView tv_price;
	private String CanYunumber;// ???????????????????????????
	// private int id;
	// private int nextID;
	private int now_type_id;
	private int now_type_id_value;
	private int next_type_id;
	private int next_type_id_value;

	private String mSingle;
	private String order_common;
	private String order_special;
	private String mPayCount;
	private boolean mIsQulification;// ?????????????????????????????????
	private int mTwofoldness;// ?????????????????????
	private long mEndDate;// ??????????????????????????????
	private int mIsOpen;// 0,????????????;1???????????????

	private TextView tshengyuTime;
	private LinearLayout mLlFailture;// ?????????????????????
	private TextView mTvPayTimes;// ???????????????
	private TextView mTvUsefulMoney;// ??????????????????
	private RelativeLayout mRlNeedPay;// ????????????
	private TextView mTvNeedMoney;
	private RelativeLayout ll_zhifujine;// ?????????????????????
	private double mMyMoney = 0;// ????????????
	private double tfn_money;// ???????????????????????????
	private int is_wx = -1;
	private boolean pay_wx = false;// true?????????????????????????????????false???????????????
	private long deadLine = 0;// ??????????????????
	private long deadLine_guid=0;//??????????????????
	private Date date;
	private long timeout = 0;// ???????????????
	private long nowTime = 0;
	private boolean flagSpecial = false;
	private boolean mealflag = false;// true?????????????????????
	private boolean isSecondClick;//?????????????????????????????????
	private boolean mIsTwoGroup=false;//????????????????????????????????????
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
//		aBar.hide();
		setContentView(R.layout.activity_payment);
		context = this;

		WXPayEntryActivity.setOnWxpayFinish(this);// ???????????????????????????????????????????????????
		msgApi = WXAPIFactory.createWXAPI(this, null);
		deadLine_guid=Long.parseLong(SharedPreferencesUtil.getStringData(this,YConstance.Pref.YIDOU_HALVE_END_TIMES,"0"));
		mIsQulification = SharedPreferencesUtil.getBooleanData(this, Pref.IS_QULIFICATION, false);
		mIsOpen = Integer.parseInt(SharedPreferencesUtil.getStringData(this, Pref.IS_OPEN, "0"));
		mTwofoldness = Integer.parseInt(SharedPreferencesUtil.getStringData(this, Pref.TWOFOLDNESS, "0"));
		msgApi.registerApp(WxPayUtil.APP_ID);
		resultMap = (HashMap<String, Object>) getIntent().getSerializableExtra("result");
		// listGoods = (List<ShopCart>) getIntent().getSerializableExtra(
		// "listGoods");
		totalAccount = getIntent().getDoubleExtra("totlaAccount", 0);
		mSingle = getIntent().getStringExtra("single");
		order_common = getIntent().getStringExtra("order_common");
		order_special = getIntent().getStringExtra("order_special");
		mPayCount = getIntent().getStringExtra("paycount");
		if (null != resultMap) {
			orderNo = (String) resultMap.get("order_code");
		} else {
			orderNo = getIntent().getStringExtra("order_code");
		}
		timeCount = new TimeCount(1500, 500);
		// ??????????????????????????????
		isMulti = getIntent().getBooleanExtra("isMulti", false);
		listOrder = (List<Order>) getIntent().getSerializableExtra("listOrder");
		for (int i = 0; i < listOrder.size(); i++) {
			Order order2 = listOrder.get(i);
			date = order2.getLast_time();
			timeout = date.getTime();
			if (i == 0) {
				deadLine = timeout;
			}
			if (timeout < deadLine) {
				deadLine = timeout;
			}
		}
		order = (Order) getIntent().getSerializableExtra("order");
		shareOrders = new ArrayList<Order>();
		for (int i = 0; i < listOrder.size(); i++) {
			if (listOrder.get(i).getShop_from() != 1) {
				shareOrders.add(listOrder.get(i));
			}
		}
		isDuobao = getIntent().getBooleanExtra("isDuobao", false);
		signShopDetail = getIntent().getStringExtra("signShopDetail");
		signType = getIntent().getIntExtra("signType", 0);
		// id = Integer.valueOf(SharedPreferencesUtil.getStringData(context,
		// "Signidd", "0"));
		// nextID = Integer.valueOf(SharedPreferencesUtil.getStringData(context,
		// "SignnextID", "0"));
		now_type_id = Integer.valueOf(SharedPreferencesUtil.getStringData(context, "now_type_id", "0"));
		now_type_id_value = Integer.valueOf(SharedPreferencesUtil.getStringData(context, "now_type_id_value", "0"));
		next_type_id = Integer.valueOf(SharedPreferencesUtil.getStringData(context, "next_type_id", "0"));
		next_type_id_value = Integer.valueOf(SharedPreferencesUtil.getStringData(context, "next_type_id_value", "0"));

		initview();
		getPayMap();
		getSystemTime();
		// ??????????????????
		// queryMyMomeny();
		for (int i = 0; i < listOrder.size(); i++) {
			Order order = listOrder.get(i);
			if (order.getShop_from() == 1||order.getShop_from() == 3||order.getShop_from() == 4||order.getShop_from() == 5) {
				mealflag = true;
				break;
			}
		}

		for (int i = 0; i < listOrder.size(); i++) {
			Order order = listOrder.get(i);
			if (order.getIs_roll()==1) {
				mIsTwoGroup = true;
				break;
			}
		}
//		if (mealflag) {// ?????????????????????????????????????????????????????????
//			llWXinPay.setOnClickListener(this);
//			llWalletPay.setVisibility(View.GONE);
//			for (int i = 0; i < 6; i++) {
//				payTypeMap.put(i, false);
//			}
//			ivWalletPay.setSelected(false);
//			ivWXinPay.setSelected(true);
//			payTypeMap.put(2, true);
//
//		}
		llWXinPay.setOnClickListener(this);
		llWalletPay.setVisibility(View.GONE);
		for (int i = 0; i < 6; i++) {
			payTypeMap.put(i, false);
		}
		ivWalletPay.setSelected(false);
		ivWXinPay.setSelected(true);
		payTypeMap.put(2, true);
//		getUserGradle();
		if (mTask2 != null) {
			mTask2.cancel();
			mTask2 = new MyTimerTask2();
		} else {
			mTask2 = new MyTimerTask2();
		}
//		timer2.schedule(mTask2, 0, 1000); // timeTask


		//3.7.2???????????????????????????
		llWXinPay.setVisibility(View.VISIBLE);

		ivWXinPay.setSelected(true);
		setSelectStatus(2);
//		ivAliPay.setSelected(true);
//		setSelectStatus(3);

	}

	// ????????????map?????? ????????????????????????
	private void getPayMap() {
		payTypeMap = new HashMap<Integer, Boolean>();
		int select = 0;
		if (ivWalletPay.isSelected()) {

		} else if (ivWXinPay.isSelected()) {
			select = 2;
		}
		payTypeMap.put(select, true);
		for (int i = 0; i < 6; i++) {
			if (i != select) {
				payTypeMap.put(i, false);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isSecondClick=false;
		// MobclickAgent.onPageStart("PaymentActivity");
		// MobclickAgent.onResume(this);
		HomeWatcherReceiver.registerHomeKeyReceiver(this);
		SharedPreferencesUtil.saveStringData(this, Pref.TONGJI_TYPE, "1055");
		TongJiUtils.TongJi(this, 12+"");
		LogYiFu.e("TongJiNew", 12+"");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("PaymentActivity");
		// MobclickAgent.onPause(this);
		HomeWatcherReceiver.unregisterHomeKeyReceiver(this);
		TongJiUtils.TongJi(this, 112+"");
		LogYiFu.e("TongJiNew", 112+"");

	}

	private void initview() {
		ll_zhifujine = (RelativeLayout) findViewById(R.id.ll_zhifujine);
		mLlFailture = (LinearLayout) findViewById(R.id.payment_ll_pay_failture);
		mTvPayTimes = (TextView) findViewById(R.id.tv_pay_times);
		mTvUsefulMoney = (TextView) findViewById(R.id.tv_useful_money);
		mRlNeedPay = (RelativeLayout) findViewById(R.id.rl_need_pay);
		mTvNeedMoney = (TextView) findViewById(R.id.tv_need_money);
		tshengyuTime = (TextView) findViewById(R.id.tshengyuTime);

		tv_price = (TextView) findViewById(R.id.tv_price);
		rel_pay_success = (RelativeLayout) findViewById(R.id.rel_pay_success);
		rel_nomal = (RelativeLayout) findViewById(R.id.rel_nomal);
		root = (RelativeLayout) findViewById(R.id.root);
		root.setBackgroundColor(Color.WHITE);
		llWalletPay = (LinearLayout) findViewById(R.id.ll_wallet);
		llWalletPay.setVisibility(View.GONE);
		llQuickPay = (LinearLayout) findViewById(R.id.ll_quick_pay);
		llWXinPay = (LinearLayout) findViewById(R.id.ll_wxin_pay);
		llAliPay = (LinearLayout) findViewById(R.id.ll_ali_pay);
		llUnionPay = (LinearLayout) findViewById(R.id.ll_union_pay);
		llMydeayPay = (LinearLayout) findViewById(R.id.ll_mydear_pay);
		ivWalletPay = (ImageView) findViewById(R.id.iv_wallet);

		ivQuickPay = (ImageView) findViewById(R.id.iv_quick_pay);
		ivWXinPay = (ImageView) findViewById(R.id.iv_wxin_pay);
		ivAliPay = (ImageView) findViewById(R.id.iv_ali_pay);
		ivUnionPay = (ImageView) findViewById(R.id.iv_union_pay);
		ivMydearPay = (ImageView) findViewById(R.id.iv_mydear_pay);
		if ("SignShopDetail".equals(signShopDetail) || isDuobao) {
			llWalletPay.setVisibility(View.GONE);
			ivWXinPay.setSelected(true);
		} else {
			ivWalletPay.setSelected(true);
		}
		// ??????????????????
		imgSelectList = new ArrayList<ImageView>();
		imgSelectList.add(ivWalletPay);
		imgSelectList.add(ivQuickPay);
		imgSelectList.add(ivWXinPay);
		imgSelectList.add(ivAliPay);
		imgSelectList.add(ivUnionPay);
		imgSelectList.add(ivMydearPay);

		tvTotalAccount = (TextView) findViewById(R.id.tv_total_account);// ????????????
		String price = new java.text.DecimalFormat("#0.00").format(totalAccount);
		tv_price.setText("??" + price);
		tvTotalAccount.setText("??????:??" + price);
		btnPay = (LinearLayout) findViewById(R.id.btn_pay); // ??????
		imgCancle = (ImageView) findViewById(R.id.iv_cancle);// ????????????

		setOnclick();
	}

	// ??????????????????
	private void setOnclick() {
		// llWalletPay.setOnClickListener(this);
		llQuickPay.setOnClickListener(this);
		// llWXinPay.setOnClickListener(this);
		llAliPay.setOnClickListener(this);
		llUnionPay.setOnClickListener(this);
		llMydeayPay.setOnClickListener(this);
		// ivWalletPay.setOnClickListener(this);
		ivQuickPay.setOnClickListener(this);
		// ivWXinPay.setOnClickListener(this);
		ivAliPay.setOnClickListener(this);
		ivUnionPay.setOnClickListener(this);
		ivMydearPay.setOnClickListener(this);
		btnPay.setOnClickListener(this);
		imgCancle.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_wallet:
		case R.id.iv_wallet:
			setSelectStatus(0);
			break;
		case R.id.ll_quick_pay:
		case R.id.iv_quick_pay:
			setSelectStatus(1);
			break;
		case R.id.ll_wxin_pay:
		case R.id.iv_wxin_pay:
			setSelectStatus(2);
			break;
		case R.id.ll_ali_pay:
		case R.id.iv_ali_pay:
			setSelectStatus(3);
			break;
		case R.id.ll_union_pay:
		case R.id.iv_union_pay:
			setSelectStatus(4);
			break;
		case R.id.ll_mydear_pay:
		case R.id.iv_mydear_pay:
			setSelectStatus(5);
			break;
		case R.id.btn_pay:// ?????????
			// ????????????????????????????????????
			// if (!isMulti) {
			// ShareUtil.getPicPath(listOrder.get(0).getList().get(0)
			// .getShop_code(), null, this);
			// }
			for (int i = 0; i < listOrder.size(); i++) {
				Order order = listOrder.get(i);
				if (order.getShop_from() == 0) {// ???????????????????????????????????????
					YunYingTongJi.yunYingTongJi(this, 113);
					break;
				}
			}
			Iterator<Entry<Integer, Boolean>> iterator = payTypeMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Boolean> entry = iterator.next();
				if (entry.getValue()) {
					gotoPay(entry.getKey(), view);
					break;
				}
			}
			break;
		case R.id.iv_cancle:// ???????????? ???????????????
			noticeDialog();
			break;
		default:
			break;
		}
	}

	private AlertDialog dialog;

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
				Intent intent = new Intent(OrderPaymentActivity.this, SetMyPayPassActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void checkMyWalletPayPass(View v) {
		new SAsyncTask<Void, Void, CheckPwdInfo>(OrderPaymentActivity.this, v, R.string.wait) {

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
				isSecondClick = false;
				if (null == e) {
					if (result != null && "1".equals(result.getStatus()) && "1".equals(result.getFlag())) {
						customDialog();
					} else if (result != null && "1".equals(result.getStatus()) && "2".equals(result.getFlag())) {
						// Toast.makeText(this, "???????????? ",
						// Toast.LENGTH_LONG).show();
						customDialog = new PayPasswordCustomDialog(OrderPaymentActivity.this, R.style.mystyle,
								R.layout.pay_customdialog, "?????????????????????",
								new java.text.DecimalFormat("#0.00").format(totalAccount));
						inputDialogListener = new InputDialogListener() {

							@Override
							public void onOK(String pwd) {
								// ??????????????????????????????
								// ToastUtil.showLongText(context, "??????????????????" +
								// text);
								isSecondClick = true;
								walletPayOrder(orderNo, pwd, isMulti);
							}

							@Override
							public void onCancel() {
								OrderPaymentActivity.this.finish();
							}

						};
						// TextView tvAccount = (TextView) customDialog
						// .findViewById(R.id.tv_account);
						// tvAccount.setText(totalAccount + "???");.
						customDialog.setListener(inputDialogListener);
						customDialog.show();
					} else {
						ToastUtil.showLongText(context, "??????????????????~~~");
					}
				}
			}

		}.execute((Void[]) null);
	}

	// ??????
	private void gotoPay(Integer position, View v) {
		switch (position) {
		case 0: // ???????????? ????????????
			if(isSecondClick){
				break;
			}
			isSecondClick = true;
			checkMyWalletPayPass(v);
			break;
		case 1: // ????????????

			break;
		case 2: // ????????????
			/*
			 * if (null != listGoods) { if (listGoods.size() > 1) {
			 * getPrepayId(YUrl.WX_PAY_MULTI); } else {
			 * getPrepayId(YUrl.WX_PAY_SINGLE); } } else {
			 */
			ToastUtil.showShortText(OrderPaymentActivity.this, "?????????...");
			if (isMulti) {
				getPrepayId(YUrl.WX_PAY_MULTI);
			} else {
//				getPrepayId(YUrl.WX_PAY_SINGLE);
				getPrepayId(YUrl.WX_PAY_MULTI);

			}
			// }
			break;
		case 3: // ???????????????
			getAliParam();
			/*
			 * if (isMulti) { aliPay(null, YUrl.ALI_NOTIFY_URL_MULTI, orderNo);
			 * } else { aliPay(null, YUrl.ALI_NOTIFY_URL_SINGLE, orderNo); }
			 */
			// }
			break;
		case 4: // ????????????

			break;
		case 5: // ??????????????????

			break;

		default:
			break;
		}
	}

	/** ??????????????????????????? */
	private void getAliParam() {
		new SAsyncTask<Void, Void, HashMap<String, String>>(this, R.string.wait) {

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				return ComModel2.getAliParam(context, orderNo);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
				if (e == null) {
					if (result.get("status").equals("1")) {
						if (isMulti) {
							aliPay(null, result.get("pay_url") + YUrl.ALI_NOTIFY_URL_MULTI, orderNo,
									result.get("partner"), result.get("seller"), result.get("sign_type"),
									result.get("private_key"), result.get("price"));
						} else {
							aliPay(null, result.get("pay_url") + YUrl.ALI_NOTIFY_URL_SINGLE, orderNo,
									result.get("partner"), result.get("seller"), result.get("sign_type"),
									result.get("private_key"), result.get("price"));
						}
					}
				}
				super.onPostExecute(context, result, e);
			}

		}.execute();
	}

	// ??????????????????
	private void setSelectStatus(int i) {
		for (int j = 0; j < 6; j++) {
			if (j == i) {
				payTypeMap.put(j, true);
				imgSelectList.get(j).setSelected(true);
			} else {
				payTypeMap.put(j, false);
				imgSelectList.get(j).setSelected(false);
			}
		}
		// TODO:
		// &&!isDuobao&&!("signShopDetail".equals("signShopDetail"))
		if (tfn_money > 0 && "single".equals(mSingle) && !isDuobao && !("signShopDetail".equals(signShopDetail))
				|| ((grade != 1&&grade!=0) && flagSpecial)) {// ?????????????????????
			imgSelectList.get(0).setSelected(true);
		}
	}

	/**
	 * ???????????????
	 *
	 * @param v
	 * @param order_code
	 * @param partner
	 *            :??????id
	 * @param seller
	 *            :????????????
	 * @param sign_type
	 *            :????????????
	 * @param private_key
	 *            :??????
	 */
	private void aliPay(View v, String notify_url, String order_code, String partner, String seller, String sign_type,
			String private_key, String totalMoney) {

		StringBuffer sb = new StringBuffer();
		/*
		 * if (null != listGoods) { for (int i = 0; i < listGoods.size(); i++) {
		 * ShopCart sc = listGoods.get(i); sb.append(sc.getShop_name()).append(
		 * " "); } } else {
		 */
		for (int i = 0; i < listOrder.size(); i++) {
			Order order = listOrder.get(i);
			sb.append(order.getOrder_name()).append(" ");
		}
		// }
		String subject = Config.pay_subject;
		String content = sb.toString();
		// String price = sum + "";
		// shop.getShop_se_price() + "";
		// TODO:
		// ??????
		String orderInfo = PayUtil.getOrderInfo(subject, content, order_code, totalMoney + "", notify_url, partner,
				seller);

		// ????????????RSA ??????
		String sign = PayUtil.sign(orderInfo, private_key);
		try {
			// ?????????sign ???URL??????
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// ???????????????????????????????????????????????????
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + PayUtil.getSignType(sign_type);

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ??????PayTask ??????
				PayTask alipay = new PayTask(OrderPaymentActivity.this);
				// ???????????????????????????????????????
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// ??????????????????
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// ??????resultStatus ??????9000???????????????????????????????????????????????????????????????????????????
				if (TextUtils.equals(resultStatus, "9000")) {
					SharedPreferencesUtil.saveBooleanData(context, "signDATAneedRefresh", true);
					// Toast.makeText(PaymentActivity.this, "????????????",
					// Toast.LENGTH_SHORT).show();
					// showShareDialog();
					updatePayStatusList(orderNo, 1 + "");
					if (shareOrders.size() == 0) {// ??????????????????
													// 0????????????????????????cut???
						OrderPaymentActivity.this.finish();
						return;
					}
					// if("SignShopDetail".equals(signShopDetail)){
					// for (int i = 0; i < listOrder.size(); i++) {
					// Order order = listOrder.get(i);
					// if((order.getStatus()==1&&order.getShop_from()==3)){
					// //????????????????????????????????????????????????????????????
					// order.setShop_from(0);//????????????????????????
					// }
					// }
					// }

					if ("SignShopDetail".equals(signShopDetail) || isDuobao) {

					} else {
						timeCount.start();
					}
					rel_pay_success.setVisibility(View.VISIBLE);
					TongJiUtils.TongJi(context, 13+"");//???????????? ??????????????????
					LogYiFu.e("TongJiNew", 13+"");
					for (int i = 0; i < listOrder.size(); i++) {
						Order order = listOrder.get(i);
						if (order.getShop_from() == 0) {// ???????????????????????????????????????
							YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 114);
							break;
						}
					}
//					YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 114);
					SharedPreferencesUtil.saveStringData(OrderPaymentActivity.this, Pref.TONGJI_TYPE, "1056");
					rel_nomal.setVisibility(View.GONE);

					if (("SignShopDetail".equals(signShopDetail) || isDuobao) && !Order.signCompleteFlag) {
						if (isDuobao) {
							if (isMulti) {
								getMultiDuobaoNumber(orderNo, "1");
							} else {
								getDuobaoNumber(orderNo, "1");
							}
						} else {
							SignComplete();
						}

					} else if (isDuobao && Order.signCompleteFlag) {
						if (isMulti) {
							getMultiDuobaoNumber(orderNo, "1");
						} else {
							getDuobaoNumber(orderNo, "1");
						}
					}

					// if (!isMulti) {
					// getShopLink();
					// } else {
					// Intent intent = new Intent(OrderPaymentActivity.this,
					// OrderPaymentSuccessActivity.class);
					// intent.putExtra("listOrder", (Serializable) shareOrders);
					// intent.putExtra("order_code", orderNo);
					// startActivity(intent);
					// OrderPaymentActivity.this.finish();
					// }

					if (null != OrderDetailsActivity.instance) {
						OrderDetailsActivity.instance.finish();
					}
					// OrderPaymentActivity.this.setResult(1);
					// finish();
				} else {
					// ??????resultStatus ?????????9000??????????????????????????????
					// ???8000???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderPaymentActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();

					} else {

						mLlFailture.setVisibility(View.VISIBLE);
						mTvPayTimes.setVisibility(View.GONE);
						// noticeDialog();
						// if (mTask2 != null) {
						// mTask2.cancel();
						// mTask2 = new MyTimerTask2();
						// } else {
						// mTask2 = new MyTimerTask2();
						// }
						// timer.schedule(mTask, 0, 1000); // timeTask
						// ??????????????????????????????????????????????????????????????????????????????????????????????????????
						// Toast.makeText(OrderPaymentActivity.this, "????????????",
						// Toast.LENGTH_SHORT).show();
						// OrderPaymentActivity.this.finish();
					}
				}
				break;
			}
			}
		};
	};

	/****
	 * ???????????????????????????
	 *
	 * @param order_code
	 * @param pwd
	 */
	private void walletPayOrder(String order_code, String pwd, final boolean isMulti) {
		new SAsyncTask<String, Void, ReturnInfo>(OrderPaymentActivity.this, null, R.string.wait) {
			@Override
			protected ReturnInfo doInBackground(FragmentActivity context, String... params) throws Exception {

				if (!isMulti) {
					return ComModel.walletPayOrder(context, params[0], params[1]);
				} else {
					return ComModel.walletPayMultiOrder(context, params[0], params[1]);
				}
			}

			@Override
			protected void onPostExecute(FragmentActivity context, ReturnInfo r, Exception e) {

				if (e != null) {// ????????????
					isSecondClick = false;
					LogYiFu.e("?????? -----", context.getString(R.string.ss));
				} else {// ???????????????????????????????????????

					int pwdflag = r.getPwdflag();

					if (pwdflag == 0) { // ????????????
						SharedPreferencesUtil.saveBooleanData(context, "signDATAneedRefresh", true);
						ToastUtil.showShortText(OrderPaymentActivity.this, "????????????");

						if (shareOrders.size() == 0) {// ?????????????????? 0????????????????????????cut???
							OrderPaymentActivity.this.finish();
							return;
						}
						timeCount.start();
						rel_pay_success.setVisibility(View.VISIBLE);
//						TongJiUtils.TongJi(context, 13+"");//???????????? ??????????????????
						LogYiFu.e("TongJiNew", 13+"");
						for (int i = 0; i < listOrder.size(); i++) {
							Order order = listOrder.get(i);
							if (order.getShop_from() == 0) {// ???????????????????????????????????????
								YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 114);
								break;
							}
						}
//						YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 114);
						SharedPreferencesUtil.saveStringData(OrderPaymentActivity.this, Pref.TONGJI_TYPE, "1056");
						rel_nomal.setVisibility(View.GONE);
						// if (!isMulti) {
						// if (listOrder.get(0).getList().size() == 1) {
						//
						// getShopLink();
						// } else {
						// Intent intent = new Intent(
						// OrderPaymentActivity.this,
						// OrderPaymentSuccessActivity.class);
						// intent.putExtra("listOrder",
						// (Serializable) shareOrders);
						// intent.putExtra("order_code", orderNo);
						// startActivity(intent);
						// OrderPaymentActivity.this.finish();
						// }
						// } else {
						// Intent intent = new Intent(OrderPaymentActivity.this,
						// OrderPaymentSuccessActivity.class);
						// intent.putExtra("listOrder", (Serializable)
						// shareOrders);
						// intent.putExtra("order_code", orderNo);
						// startActivity(intent);
						// OrderPaymentActivity.this.finish();
						// }
						// OrderPaymentActivity.this.setResult(1);
						// finish();
						if (null != OrderDetailsActivity.instance) {
							OrderDetailsActivity.instance.finish();
						}

						// MealPaymentActivity.this.finish();
						// paySuccessTo();
						// timeCount.start();
						// rel_pay_success.setVisibility(View.VISIBLE);
						// rel_nomal.setVisibility(View.GONE);
					} else if (pwdflag == 1 || pwdflag == 2 || pwdflag == 3) { // ??????????????????
						isSecondClick = false;
						String message = r.getMessage();
						customDialog.dismiss();
						PayErrorDialog dialog = new PayErrorDialog(context, R.style.DialogStyle1, pwdflag, message);
						dialog.show();

					}

					// if(shareOrders.size() == 0){//?????????????????? 0????????????????????????cut???
					// OrderPaymentActivity.this.finish();
					// return;
					// }
					//
					// if (!isMulti) {
					// if (listOrder.get(0).getList().size() == 1) {
					//
					// getShopLink();
					// } else {
					// Intent intent = new Intent(
					// OrderPaymentActivity.this,
					// OrderPaymentSuccessActivity.class);
					// intent.putExtra("listOrder",
					// (Serializable) shareOrders);
					// intent.putExtra("order_code", orderNo);
					// startActivity(intent);
					// OrderPaymentActivity.this.finish();
					// }
					// } else {
					// Intent intent = new Intent(OrderPaymentActivity.this,
					// OrderPaymentSuccessActivity.class);
					// intent.putExtra("listOrder", (Serializable) shareOrders);
					// intent.putExtra("order_code", orderNo);
					// startActivity(intent);
					// OrderPaymentActivity.this.finish();
					// }
					// // OrderPaymentActivity.this.setResult(1);
					// // finish();
					// if(null != OrderDetailsActivity.instance){
					// OrderDetailsActivity.instance.finish();
					// }
				}

			};

			@Override
			protected boolean isHandleException() {
				return true;
			};
		}.execute(order_code, pwd);
	}

	// ??????????????????
	private void getShopLink() {
		new SAsyncTask<String, Void, HashMap<String, String>>(OrderPaymentActivity.this, R.string.wait) {

			@Override
			protected HashMap<String, String> doInBackground(FragmentActivity context, String... params)
					throws Exception {
				return ComModel2.getShopLink(params[0], context, "true");
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
				super.onPostExecute(context, result, e);

				if (null == e) {
					if (result.get("status").equals("1")) {
						String[] picList = result.get("shop_pic").split(",");
						if (!TextUtils.isEmpty(result.get("four_pic")) && !result.get("four_pic").equals("null")) {
							download(null, picList, listOrder.get(0).getList().get(0).getShop_code(), result);
						} else {
							ToastUtil.showShortText(context, "????????????");
							OrderPaymentActivity.this.finish();
						}
					} else if (result.get("status").equals("1050")) {// ??????
						startActivity(new Intent(OrderPaymentActivity.this, NoShareActivity.class)); // ?????????????????????
						// ToastUtil.showShortText(context, "?????????????????????????????????"); //
						// 4?????????
						OrderPaymentActivity.this.finish();
					}
				}

			}

		}.execute(listOrder.get(0).getList().get(0).getShop_code());// ?????????????????????????????????
	}

	private void download(View v, final String[] picList, final String shop_code,
			final HashMap<String, String> mapInfos) {
		new SAsyncTask<Void, Void, Void>(OrderPaymentActivity.this, v, R.string.wait) {

			@Override
			protected Void doInBackground(Void... params) {
				File fileDirec = new File(YConstance.savePicPath);
				if (!fileDirec.exists()) {
					fileDirec.mkdir();
				}
				File[] listFiles = new File(YConstance.savePicPath).listFiles();
				if (listFiles.length != 0) {
					LogYiFu.e("TAG", "??????????????? ?????????????????????");
					for (File file : listFiles) {
						file.delete();
					}
				}
				// LogYiFu.i("TAG", "piclist=" + picList.length);
				List<String> pics = new ArrayList<String>();
				for (int j = 0; j < picList.length; j++) {
					if (!picList[j].contains("reveal_") && !picList[j].contains("detail_")
							&& !picList[j].contains("real_")) {
						pics.add(picList[j]);
					}
				}
				int j = pics.size() + 1;
				if (pics.size() > 8) {
					j = 9;
				}
				for (int i = 0; i < j; i++) {
					if (i == j - 1) {
						/*
						 * ComModel2.saveQRCode(PaymentSuccessActivity.this,
						 * shop_code);
						 */
						// downloadPic(mapInfos.get("qr_pic"), 9);
						Bitmap bm = QRCreateUtil.createImage(mapInfos.get("QrLink"), 500, 700,
								mapInfos.get("shop_se_price") + "", OrderPaymentActivity.this);// ?????????????????????
						QRCreateUtil.saveBitmap(bm, YConstance.savePicPath, MD5Tools.md5(String.valueOf(9)) + ".jpg");// ?????????????????????
						break;
					}
					downloadPic(shop_code.substring(1, 4) + "/" + shop_code + "/" + pics.get(i) + "!450", i);
				}
				return super.doInBackground(params);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, Void result) {
				// showShareDialog();
				super.onPostExecute(context, result);
				// getShopLink();
				Intent intent = new Intent(OrderPaymentActivity.this, ShowShareActivity.class);
				intent.putExtra("shop_link", mapInfos.get("link"));
				intent.putExtra("content", mapInfos.get("content"));
				intent.putExtra("four_pic", mapInfos.get("four_pic"));
				intent.putExtra("order_code", orderNo);
				Bundle bundle = new Bundle();
				// bundle.putSerializable("listGoods", (Serializable)
				// listGoods);
				intent.putExtras(bundle);
				bundle = new Bundle();
				bundle.putSerializable("listOrder", (Serializable) listOrder);
				intent.putExtras(bundle);

				startActivity(intent);
				OrderPaymentActivity.this.finish();
			}

		}.execute();
	}

	private void downloadPic(String picPath, int i) {
		try {
			URL url = new URL(YUrl.imgurl + picPath);
			// ????????????
			URLConnection con = url.openConnection();
			// ?????????????????????
			int contentLength = con.getContentLength();
			// System.out.println("?????? :" + contentLength);
			// ?????????
			InputStream is = con.getInputStream();
			// 1K???????????????
			byte[] bs = new byte[8192];
			// ????????????????????????
			int len;
			// ?????????????????? /sdcard/yssj/
			File file = new File(YConstance.savePicPath, MD5Tools.md5(String.valueOf(i)) + ".jpg");
			if (file.exists()) {
				file.delete();
			}
			LogYiFu.e("TAG", "??????????????????????????????????????????");
			OutputStream os = new FileOutputStream(file);
			// ????????????
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);

			}
			LogYiFu.i("TAG", "?????????????????????file=" + file.toString());
			// ???????????????????????????
			os.close();
			is.close();
		} catch (Exception e) {
			LogYiFu.e("TAG", "????????????");
			e.printStackTrace();
		}
	}

	/***
	 * ????????????
	 *
	 * @author Administrator
	 *
	 */
	private void getPrepayId(String wxPayUrl) {
		new SAsyncTask<String, Void, Map<String, String>>(OrderPaymentActivity.this, R.string.wait) {

			@Override
			protected Map<String, String> doInBackground(FragmentActivity context, String... params) throws Exception {
				Map<String, String> mapReturn = ComModel2.getPrepayId(context, orderNo, "test", params[0]);
				return mapReturn;
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, Map<String, String> result, Exception e) {
				super.onPostExecute(context, result, e);

				if (null == e) {
					WxPayUtil.sendPayReq(msgApi, WxPayUtil.genPayReq(result), result.get("appid"));
				}
				// finish();
			}

		}.execute(wxPayUrl);
	}

	@Override
	public void onWxpaySuccess() {
		updatePayStatusList(orderNo, 2 + "");
		if (shareOrders.size() == 0) {// ?????????????????? 0????????????????????????cut???
			OrderPaymentActivity.this.finish();
			return;
		}
		// if("SignShopDetail".equals(signShopDetail)){
		// for (int i = 0; i < listOrder.size(); i++) {
		// Order order = listOrder.get(i);
		// if((order.getStatus()==1&&order.getShop_from()==3)){
		// //????????????????????????????????????????????????????????????
		// order.setShop_from(0);//????????????????????????
		// }
		// }
		// }
		if ("SignShopDetail".equals(signShopDetail) || isDuobao) {

		} else {
			timeCount.start();
		}
		rel_pay_success.setVisibility(View.VISIBLE);
		TongJiUtils.TongJi(context, 13+"");//???????????? ??????????????????
		LogYiFu.e("TongJiNew", 13+"");
		for (int i = 0; i < listOrder.size(); i++) {
			Order order = listOrder.get(i);
			if (order.getShop_from() == 0) {// ???????????????????????????????????????
				YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 114);
				break;
			}
		}
//		YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 114);
		SharedPreferencesUtil.saveBooleanData(context, "signDATAneedRefresh", true);
		SharedPreferencesUtil.saveStringData(OrderPaymentActivity.this, Pref.TONGJI_TYPE, "1056");
		rel_nomal.setVisibility(View.GONE);
		LogYiFu.e("12121", isDuobao + "");
		if (("SignShopDetail".equals(signShopDetail) || isDuobao) && !Order.signCompleteFlag) {
			if (isDuobao) {
				if (isMulti) {
					getMultiDuobaoNumber(orderNo, "2");
				} else {
					getDuobaoNumber(orderNo, "2");
				}
			} else {
				SignComplete();
			}
		} else if (isDuobao && Order.signCompleteFlag) {
			if (isMulti) {
				getMultiDuobaoNumber(orderNo, "2");
			} else {
				getDuobaoNumber(orderNo, "2");
			}
		}

		// if (!isMulti) {
		// getShopLink();
		// } else {
		// Intent intent = new Intent(OrderPaymentActivity.this,
		// OrderPaymentSuccessActivity.class);
		// intent.putExtra("listOrder", (Serializable) shareOrders);
		// intent.putExtra("order_code", orderNo);
		// startActivity(intent);
		// OrderPaymentActivity.this.finish();
		// }
		// if(null != OrderDetailsActivity.instance){
		// OrderDetailsActivity.instance.finish();
		// }
		// OrderPaymentActivity.this.setResult(1);
		// finish();
	}

	@Override
	public void onWxpayFailed() {
		if (WXPayEntryActivity.instance != null)
			WXPayEntryActivity.instance.finish();
		// OrderPaymentActivity.this.finish();
		mLlFailture.setVisibility(View.VISIBLE);
		mTvPayTimes.setVisibility(View.GONE);
	}

	private void updatePayStatusList(final String gcode, final String buy_type) {
		new SAsyncTask<Void, Void, ReturnInfo>(OrderPaymentActivity.this) {

			@Override
			protected ReturnInfo doInBackground(FragmentActivity context, Void... params) throws Exception {
				return ComModel.updatePayStatusList(context, gcode, buy_type);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, ReturnInfo result, Exception e) {
				super.onPostExecute(context, result, e);
			}

		}.execute();
	}

	private void SignComplete() {
//		Intent intent = new Intent(OrderPaymentActivity.this, MainMenuActivity.class);
//		intent.putExtra("signType", signType + "");
//		intent.putExtra("signShopDetail", signShopDetail);
//		intent.putExtra("isDuobao", isDuobao);
//		intent.putExtra("CanYunumber", CanYunumber);
//		// intent.putExtra("Signidd", id);
//		// intent.putExtra("SignnextID", nextID);
//		intent.putExtra("now_type_id", now_type_id);
//		intent.putExtra("now_type_id_value", now_type_id_value);
//		intent.putExtra("next_type_id", next_type_id);
//		intent.putExtra("next_type_id_value", next_type_id_value);



		if(null !=CommonActivity.instance){
			CommonActivity.instance.finish();
		}
		if(null !=ShopDetailsIndianaActivity.instance){
			ShopDetailsIndianaActivity.instance.finish();
		}



			// ????????????

			SharedPreferencesUtil.saveStringData(context, "commonactivityfrom", "sign");
			Intent intent = new Intent(OrderPaymentActivity.this,
					CommonActivity.class);
			intent.putExtra("signType", signType + "");
			intent.putExtra("signShopDetail", signShopDetail);
			intent.putExtra("isDuobao", isDuobao);
			intent.putExtra("CanYunumber", CanYunumber);
//		intent.putExtra("Signidd", id);
//		intent.putExtra("SignnextID", nextID);
			intent.putExtra("now_type_id", now_type_id);
			intent.putExtra("now_type_id_value", now_type_id_value);
			intent.putExtra("next_type_id", next_type_id);
			intent.putExtra("next_type_id_value", next_type_id_value);
			startActivity(intent);



//		startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);






		finish();

//		Order.signCompleteFlag = true;
	}

	/**
	 * ???????????? ??????????????????
	 *
	 * @return
	 */
	private void getDuobaoNumber(String order_code, String pay_type) {
		new SAsyncTask<String, Void, HashMap<String, String>>((FragmentActivity) OrderPaymentActivity.this, 0) {
			@Override
			protected HashMap<String, String> doInBackground(FragmentActivity context, String... params)
					throws Exception {
				return ComModel2.getDuobaoNumber(OrderPaymentActivity.this, params[0], Integer.valueOf(params[1]));
			}

			protected boolean isHandleException() {
				return true;
			};

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (e == null && result != null) {
					CanYunumber = result.get("data");
					LogYiFu.e("OrderPaymentActivity", "???????????????????????????" + CanYunumber);
					SignComplete();
				}
			}
		}.execute(order_code, pay_type);
	}

	/**
	 * ?????????????????? ????????????
	 *
	 * @return
	 */
	private void getMultiDuobaoNumber(String g_code, String pay_type) {
		new SAsyncTask<String, Void, HashMap<String, String>>((FragmentActivity) OrderPaymentActivity.this, 0) {
			@Override
			protected HashMap<String, String> doInBackground(FragmentActivity context, String... params)
					throws Exception {
				return ComModel2.getMultiDuobaoNumber(OrderPaymentActivity.this, params[0], Integer.valueOf(params[1]));
			}

			protected boolean isHandleException() {
				return true;
			};

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (e == null && result != null) {
					CanYunumber = result.get("data");
					LogYiFu.e("OrderPaymentActivity", "???????????????????????????" + CanYunumber);
					SignComplete();
				}
			}
		}.execute(g_code, pay_type);
	}

	// ?????????
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ????????????????????????,????????????????????????

		}

		@Override
		public void onFinish() {// ?????????????????????
			TongJiUtils.TongJi(context, 113+"");//???????????? ??????????????????
			LogYiFu.e("TongJiNew", 113+"");
			// if (ShopCartNewNewActivity.instance != null) {
			// ShopCartNewNewActivity.instance = null;
			// }
			// Intent intent = new Intent(PaymentActivity.this,
			// ShopCartNewNewActivity.class);
			// startActivity(intent);
			if (null != ShopDetailsActivity.instance) {
				ShopDetailsActivity.instance.finish();
			}

			boolean isMad = SharedPreferencesUtil.getBooleanData(context,Pref.ISMADMONDAY, false);
			if(isMad){
				//??????????????? ???????????????????????? ??????
				int chouJiangNum = 0 ;
				//?????????????????????????????????????????? ?????????
				if(isMulti){
					for (int i = 0; i <listOrder.size() ; i++) {
						double remainMoney = listOrder.get(i).getRemain_money();
						chouJiangNum += (int)(remainMoney%5==0?remainMoney/5:remainMoney/5+1);
					}
				}else{
					chouJiangNum = (int)(totalAccount%5==0?totalAccount/5:totalAccount/5+1);
				}
				SharedPreferencesUtil.saveStringData(context,Pref.PAYSUCCESSDIALOG,""+totalAccount);
				Intent intent = new Intent(context, WithdrawalLimitActivity.class);
				intent.putExtra("isFromPaySuccess", true);
				intent.putExtra("payLotteryNumber", chouJiangNum);//??????????????????  ?????????????????????????????????
				startActivity(intent);
				OrderPaymentActivity.this.finish();

			}else{
//				if (MainMenuActivity.instances != null)
//					MainMenuActivity.instances.finish();
//				Intent intent = new Intent(context, MainMenuActivity.class);
//				intent.putExtra("toYf", "toYf");
//				intent.putExtra("mIsTwoGroup", mIsTwoGroup);
//				startActivity(intent);
				// ???????????????????????? ?????? ????????????????????????
//                int yiDouNum = 0 ;
//                //?????????????????????????????????????????????
//				if(isMulti){
//					for (int i = 0; i <listOrder.size() ; i++) {
//						double remainMoney = listOrder.get(i).getRemain_money();
//						yiDouNum += (int)(Math.ceil(remainMoney));
//					}
//				}else{
//					yiDouNum = (int)(Math.ceil(totalAccount));
//				}
//				SharedPreferencesUtil.saveStringData(context,Pref.PAYSUCCESSDIALOG,""+totalAccount);
//				Intent intent = new Intent(context, WithdrawalLimitActivity.class);
//				if(recLen_guide>0){
//					intent.putExtra("is_guidPay",true);
//				}else{
//					intent.putExtra("is_guidPay",false);
//				}
//				intent.putExtra("isFromPaySuccess", true);
//				intent.putExtra("payYiDouNumber", yiDouNum);//??????????????????  ?????????????????????
//				startActivity(intent);
				//?????????????????????????????????????????????
				goToOrderDetail();


			}
		}

		@Override
		public void onTick(long millisUntilFinished) {// ??????????????????

		}
	}


	private void goToOrderDetail() {

		new SAsyncTask<Void, Void, Order>(this, R.string.wait) {

			@Override
			protected Order doInBackground(FragmentActivity context, Void... params)
					throws Exception {

				return ComModel2.getMyOrderPaysuccss(context, orderNo);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(final FragmentActivity context, final Order result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {


					Intent intent = new Intent(context,
							OrderDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("order", result);
					intent.putExtras(bundle);
					intent.putExtra("paySuccess",true);
					startActivity(intent);
					OrderPaymentActivity.this.finish();


				}
			}

		}.execute();


	}


	// TODO:NEW JOIN

	// private long recLen = 30 * 1000 * 60;
	private long recLen = 10000;
	Timer timer = new Timer();
	private MyTimerTask mTask;

	// ?????????
	public class MyTimerTask extends TimerTask {

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
							tshengyuTime.setText("" + days + "???" + hours + ":" + minutes + ":" + seconds);
							// mTvPayTimes.setText("" + days + "???" + hours + ":"
							// + minutes + ":" + seconds);
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
							tshengyuTime.setText("" + hours + ":" + minutes + ":" + seconds);
						}
					} else if (minute >= 10 && second >= 10) {
						tshengyuTime.setText("" + minute + ":" + second);
						// mTvPayTimes.setText("" + minute + ":" + second);
					} else if (minute >= 10 && second < 10) {
						tshengyuTime.setText("" + minute + ":0" + second);
						// mTvPayTimes.setText("" + minute + ":0" + second);
					} else if (minute < 10 && second >= 10) {
						tshengyuTime.setText("0" + minute + ":" + second);
						// mTvPayTimes.setText("0" + minute + ":" + second);
					} else {
						tshengyuTime.setText("0" + minute + ":0" + second);
						// mTvPayTimes.setText("0" + minute + ":0" + second);
					}
					// tv_time.setText("" + recLen);
					if (recLen == 0) {
						timer.cancel();
						// tv_time.setText("???????????????");
						btnPay.setBackgroundColor(Color.parseColor("#a8a8a8"));
						btnPay.setClickable(false);

					}
				}
			});
		}

	}
	private long recLen_normal=24 * 60 * 60 * 1000;//????????????????????????
	private long recLen_guide=0;//???????????????????????????
	private long recLen2 = 0;
	Timer timer2 = new Timer();
	private MyTimerTask2 mTask2;

	// ?????????
	public class MyTimerTask2 extends TimerTask {

		@Override
		public void run() {

			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
//					recLen2 -=1000;
					recLen_normal-=1000;
					if(recLen_guide>0){
						recLen_guide-=1000;
					}
					if(recLen_guide>0&&mTvPayTimes.getVisibility()==View.VISIBLE){
						recLen2=recLen_guide;
					}else{
						recLen2=recLen_normal;
					}
					String days;
					String hours;
					String minutes;
					String seconds;
					long minute = recLen2 / 60000;
					long second = (recLen2 % 60000) / 1000;
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
							tshengyuTime.setText("" + days + "???" + hours + ":" + minutes + ":" + seconds);
							mTvPayTimes.setText("" + days + "???" + hours + ":" + minutes + ":" + seconds);
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
							tshengyuTime.setText("" + hours + ":" + minutes + ":" + seconds);
							mTvPayTimes.setText("" + hours + ":" + minutes + ":" + seconds);
						}
					} else if (minute >= 10 && second >= 10) {
						tshengyuTime.setText("" + minute + ":" + second);
						mTvPayTimes.setText("" + minute + ":" + second);
					} else if (minute >= 10 && second < 10) {
						tshengyuTime.setText("" + minute + ":0" + second);
						mTvPayTimes.setText("" + minute + ":0" + second);
					} else if (minute < 10 && second >= 10) {
						tshengyuTime.setText("0" + minute + ":" + second);
						mTvPayTimes.setText("0" + minute + ":" + second);
					} else {
						tshengyuTime.setText("0" + minute + ":0" + second);
						mTvPayTimes.setText("0" + minute + ":0" + second);
					}
					// tv_time.setText("" + recLen);
					if (recLen2 <= 0&&recLen_normal<=0) {
						timer2.cancel();
						OrderPaymentActivity.this.finish();
						// tv_time.setText("???????????????");
						// btn_pay.setBackgroundColor(Color.parseColor("#a8a8a8"));
						// btn_pay.setClickable(false);

					}
				}
			});
		}

	}

	// TODO:
	public void noticeDialog() {
		final Dialog dialog = new Dialog(context, R.style.invate_dialog_style);
//		View view = View.inflate(context, R.layout.payment_notice__dialog, null);
		View view = View.inflate(context, R.layout.dialog_pay_back, null);
		TextView mConfrim = (TextView) view.findViewById(R.id.confrim);
		TextView mDismiss = (TextView) view.findViewById(R.id.dismiss);

		mDismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ???????????????????????????
				dialog.dismiss();
				for (int i = 0; i < listOrder.size(); i++) {
					Order order = listOrder.get(i);
					if (order.getShop_from() == 0) {// ???????????????????????????????????????
						YunYingTongJi.yunYingTongJi(OrderPaymentActivity.this, 113);
						break;
					}
				}
				Iterator<Entry<Integer, Boolean>> iterator = payTypeMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Boolean> entry = iterator.next();
					if (entry.getValue()) {
						gotoPay(entry.getKey(), null);
						break;
					}
				}
			}
		});
		mConfrim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderPaymentActivity.this.finish();

			}
		});

		// ?????????????????????dialog
		dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * ????????????????????????????????? TODO:
	 */

	private void queryMyMomeny() {

		new SAsyncTask<Void, Void, String[]>(OrderPaymentActivity.this, R.string.wait) {

			@Override
			protected String[] doInBackground(FragmentActivity context, Void... params) throws Exception {
				return ComModel2.myWalletInfo(context);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, String[] result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					if (result != null && result.length > 0) {
						String balance = result[0];// ????????????
						mMyMoney = Double.parseDouble(balance);
						LogYiFu.e("payment", "??????????????????");
						if(mealflag){
							llWXinPay.setOnClickListener(OrderPaymentActivity.this);
							llWalletPay.setVisibility(View.GONE);
							for (int i = 0; i < 6; i++) {
								payTypeMap.put(i, false);
							}
							ivWalletPay.setSelected(false);
							ivWXinPay.setSelected(true);
							payTypeMap.put(2, true);
						}else
						if (("order_special".equals(order_special) && !("paycount".equals(mPayCount)))
								|| (listOrder.size() == 1 && listOrder.get(0).getShop_from() == 1)) {// ?????????????????????
							llWalletPay.setVisibility(View.VISIBLE);
							llWXinPay.setOnClickListener(OrderPaymentActivity.this);
							if (mIsQulification && mIsOpen == 1) {
								mTvUsefulMoney.setText(
										"????????????" + new java.text.DecimalFormat("#0.00").format(mMyMoney * mTwofoldness));
								mTvUsefulMoney.setTextColor(Color.parseColor("#c5c5c5"));
								llWalletPay.setClickable(false);
								ivWalletPay.setSelected(false);
								ivWXinPay.setSelected(true);
								for (int i = 0; i < 6; i++) {
									payTypeMap.put(i, false);
								}
								payTypeMap.put(2, true);
							} else {
								if (mMyMoney >= totalAccount) {// ????????????????????????????????????????????????
									mRlNeedPay.setVisibility(View.GONE);
									mTvUsefulMoney.setText(
											"????????????" + new java.text.DecimalFormat("#0.00").format(mMyMoney) + "???");
									llWalletPay.setClickable(true);
									ivWalletPay.setSelected(true);
									llWalletPay.setOnClickListener(OrderPaymentActivity.this);
								} else {// ??????????????????????????????????????????????????????

									mRlNeedPay.setVisibility(View.GONE);
									if (mMyMoney == 0) {
										mTvUsefulMoney.setText("????????????" + 0.00 + "???");
										mTvUsefulMoney.setTextColor(Color.parseColor("#c5c5c5"));
										llWalletPay.setClickable(false);
										ivWalletPay.setSelected(false);
										ivWXinPay.setSelected(true);
									} else {
										mTvUsefulMoney.setText("????????????"
												+ new java.text.DecimalFormat("#0.00").format(mMyMoney) + "???(????????????)");
										mTvUsefulMoney.setTextColor(Color.parseColor("#c5c5c5"));
										llWalletPay.setClickable(false);
										ivWalletPay.setSelected(false);
										ivWXinPay.setSelected(true);
									}
									for (int i = 0; i < 6; i++) {
										payTypeMap.put(i, false);
									}
									payTypeMap.put(2, true);
								}
								// mTvUsefulMoney.setText("????????????" + balance +
								// "???");
								// if(mMyMoney<totalAccount){
								// mTvUsefulMoney.setTextColor(Color.parseColor("#c5c5c5"));
								// mTvUsefulMoney.setText("????????????" + balance +
								// "???(????????????)");
								// }
							}
							if (mealflag) {// ???????????????????????????????????????
								mRlNeedPay.setVisibility(View.GONE);
								llWXinPay.setOnClickListener(OrderPaymentActivity.this);
								llWalletPay.setVisibility(View.GONE);
								for (int i = 0; i < 6; i++) {
									payTypeMap.put(i, false);
								}
								ivWalletPay.setSelected(false);
								ivWXinPay.setSelected(true);
								payTypeMap.put(2, true);

							}
						} else {// ??????????????????????????????????????????
								// && "order_common".equals(order_common)
							if ("single".equals(mSingle) && !isDuobao && !("signShopDetail".equals(signShopDetail))) {
								llWalletPay.setVisibility(View.VISIBLE);
								// ???????????????????????????????????????
								LogYiFu.e("payment", "??????????????????");
								double mHaveMoney = mMyMoney * mTwofoldness;
								for (int i = 0; i < listOrder.size(); i++) {
									Order order = listOrder.get(i);
									if (order.getTfn_money() <= 0) {
										if (mIsQulification && mIsOpen == 1) {// ?????????????????????>=6;??????????????????
											double mMoney = 0;
											if (grade == 1||grade==0) {
												if (order.getShop_from() == 0) {
													if (auseTwofold * order.getShop_num() <= mHaveMoney) {
														mMoney = auseTwofold * order.getShop_num();

													} else {
														mMoney = mHaveMoney;
													}
													mHaveMoney -= mMoney;// ?????????????????????
													tfn_money += mMoney;
												}
											} else {
												if (order.getShop_from() == 0) {
													if (buseTwofold * order.getShop_num() <= mHaveMoney) {
														mMoney = buseTwofold * order.getShop_num();

													} else {
														mMoney = mHaveMoney;
													}
													mHaveMoney -= mMoney;// ?????????????????????
													tfn_money += mMoney;
												}
											}
										}
									} else {
										tfn_money += order.getTfn_money();
										if (listOrder.size() >= 2) {
											totalAccount += order.getTfn_money();
										}

									}

									if (tfn_money > 0) {
										if (order.getIs_wx() == 1 && order.getWx_price() != order.getRemain_money()) {// ??????????????????
											pay_wx = true;
										}
									}
								}
								// tvTotalAccount.setText("??????:??" + new
								// java.text.DecimalFormat("#0.0").format(totalAccount));
								// Order orderSingle = listOrder.get(0);
								// tfn_money = orderSingle.getTfn_money();//
								// ?????????????????????
								// is_wx=orderSingle.getIs_wx();
								if (tfn_money > 0) {// ?????????????????????
									mTvUsefulMoney.setText(
											"????????????" + new java.text.DecimalFormat("#0.00").format(tfn_money) + "???");
									// mRlNeedPay.setVisibility(View.VISIBLE);
									mTvNeedMoney.setText(
											"" + new java.text.DecimalFormat("#0.00").format(totalAccount - tfn_money));
									llWalletPay.setClickable(false);
									ivWalletPay.setSelected(true);
									ivWXinPay.setSelected(true);
									for (int i = 0; i < 6; i++) {
										payTypeMap.put(i, false);
									}
									payTypeMap.put(2, true);
									if ("paycount".equals(mPayCount)) {// ????????????
																		// ,??????????????????
										llWXinPay.setClickable(true);
										llWXinPay.setOnClickListener(OrderPaymentActivity.this);
									} else if (pay_wx) {// ?????????????????????????????????
										llWXinPay.setClickable(false);
									} else {
										llWXinPay.setClickable(true);
										llWXinPay.setOnClickListener(OrderPaymentActivity.this);
									}
									llWalletPay.setVisibility(View.GONE);
									tvTotalAccount.setText("??????:??"
											+ new java.text.DecimalFormat("#0.00").format(totalAccount - tfn_money));
								} else if (mIsQulification && mIsOpen == 1) {// ???????????????????????????????????????????????????
									boolean flag = false;
									for (int i = 0; i < listOrder.size(); i++) {
										if (listOrder.get(i).getShop_from() != 0) {
											flag = false;
										} else {
											flag = true;
											return;
										}
									}
									if (!flag) {
										llWXinPay.setClickable(true);
										llWXinPay.setOnClickListener(OrderPaymentActivity.this);
										llWalletPay.setVisibility(View.GONE);
										for (int i = 0; i < 6; i++) {
											payTypeMap.put(i, false);
										}
										payTypeMap.put(2, true);
									}
								} else if (grade != 1&&grade!=0) {// ???A??????????????????????????????

									for (int i = 0; i < listOrder.size(); i++) {
										if (listOrder.get(i).getShop_from() != 1) {
											flagSpecial = true;
											break;
										}
									}
									llWXinPay.setOnClickListener(OrderPaymentActivity.this);
									if (flagSpecial) {
										double mMoney = 0;
										for (int i = 0; i < listOrder.size(); i++) {
											double mTempMoney = 0;
											Order order = listOrder.get(i);
											if (order.getShop_from() == 0) {

												if (buseTwofold * order.getShop_num() <= mMyMoney) {
													mTempMoney = buseTwofold * order.getShop_num();
												} else {
													mTempMoney = mMyMoney;
												}

											} else if (order.getShop_from() == 1 || order.getShop_from() == 2) {
												mTempMoney = order.getOrder_price();
											}
											mMoney += mTempMoney;
										}
										mTvUsefulMoney.setText(
												"????????????" + new java.text.DecimalFormat("#0.00").format(mMoney) + "???");
										// mRlNeedPay.setVisibility(View.VISIBLE);
										llWalletPay.setClickable(false);
										ivWalletPay.setSelected(false);
										ivWXinPay.setSelected(true);

										// mTvNeedMoney.setText(
										// "" + new
										// java.text.DecimalFormat("#0.0").format(totalAccount
										// - mMoney));
										// llWalletPay.setClickable(false);
										// if (mMoney > 0) {
										// ivWalletPay.setSelected(true);
										// } else {
										// ivWalletPay.setSelected(false);
										// }
										llWalletPay.setVisibility(View.GONE);
										ivWXinPay.setSelected(true);
										for (int i = 0; i < 6; i++) {
											payTypeMap.put(i, false);
										}
										payTypeMap.put(2, true);
										// llWalletPay.setVisibility(View.GONE);
										tvTotalAccount
												.setText("" + new DecimalFormat("#0.00").format(totalAccount) + "???");
									} else {
										llWalletPay.setClickable(true);
										ivWalletPay.setSelected(true);
										llWalletPay.setOnClickListener(OrderPaymentActivity.this);
										mTvUsefulMoney.setText(
												"????????????" + new java.text.DecimalFormat("#0.00").format(mMyMoney) + "???");
										tvTotalAccount
												.setText("" + new DecimalFormat("#0.00").format(totalAccount) + "???");
									}

								} else if (mMyMoney >= totalAccount) {// A?????????
																		// ????????????????????????????????????????????????
									LogYiFu.e("payment", "????????????");
									mRlNeedPay.setVisibility(View.GONE);
									mTvUsefulMoney.setText(
											"????????????" + new java.text.DecimalFormat("#0.00").format(mMyMoney) + "???");
									llWalletPay.setClickable(true);
									ivWalletPay.setSelected(true);
									llWalletPay.setOnClickListener(OrderPaymentActivity.this);
									llWXinPay.setClickable(true);
									llWXinPay.setOnClickListener(OrderPaymentActivity.this);
								} else {// A????????? ??????????????????????????????????????????????????????
									LogYiFu.e("payment", "????????????");
									llWXinPay.setClickable(true);
									llWXinPay.setOnClickListener(OrderPaymentActivity.this);
									mRlNeedPay.setVisibility(View.GONE);
									if (mMyMoney == 0) {
										mTvUsefulMoney.setText("????????????" + 0.00 + "???");
										mTvUsefulMoney.setTextColor(Color.parseColor("#c5c5c5"));
										llWalletPay.setClickable(false);
										ivWalletPay.setSelected(false);
										ivWXinPay.setSelected(true);
									} else {
										mTvUsefulMoney.setText("????????????"
												+ new java.text.DecimalFormat("#0.00").format(mMyMoney) + "???(????????????)");
										mTvUsefulMoney.setTextColor(Color.parseColor("#c5c5c5"));
										llWalletPay.setClickable(false);
										ivWalletPay.setSelected(false);
										ivWXinPay.setSelected(true);
									}
									for (int i = 0; i < 6; i++) {
										payTypeMap.put(i, false);
									}
									payTypeMap.put(2, true);
								}
								if (mealflag) {// ???????????????????????????????????????
									mRlNeedPay.setVisibility(View.GONE);
									llWXinPay.setOnClickListener(OrderPaymentActivity.this);
									llWalletPay.setVisibility(View.GONE);
									for (int i = 0; i < 6; i++) {
										payTypeMap.put(i, false);
									}
									ivWalletPay.setSelected(false);
									ivWXinPay.setSelected(true);
									payTypeMap.put(2, true);

								}
							} else {// ???????????????????????????????????????????????????????????????????????????
								llWalletPay.setVisibility(View.VISIBLE);
								llWXinPay.setClickable(true);
								llWalletPay.setClickable(true);
								llWalletPay.setOnClickListener(OrderPaymentActivity.this);
								llWXinPay.setOnClickListener(OrderPaymentActivity.this);
							}
						}
					}
				}
			}

		}.execute((Void[]) null);

	}

	private int grade = 1;
	private int buseTwofold = 6;
	private int auseTwofold = 6;

	/**
	 * ??????????????????
	 */
	private void getUserGradle() {
		new SAsyncTask<Void, Void, HashMap<String, Object>>((FragmentActivity) OrderPaymentActivity.this,
				R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				return ComModel2.getUserGradle(context);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);

				if (null == e && result != null) {
					grade = (Integer) result.get("grade");
					buseTwofold = (Integer) result.get("buseTwofold");
					auseTwofold = (Integer) result.get("auseTwofold");
					queryMyMomeny();
				}
			}

		}.execute();
	}

	/**
	 * ??????????????????
	 */
	private void getSystemTime() {
		new SAsyncTask<Void, Void, HashMap<String, Object>>((FragmentActivity) OrderPaymentActivity.this,
				R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				return ComModel2.getSystemTime(context);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);

				if (null == e && result != null) {
					nowTime = (Long) result.get("now");

					recLen_normal = deadLine - nowTime;
					recLen_guide=deadLine_guid-nowTime;
//					recLen_guide=1*60*1000;
					mTvPayTimes.setVisibility(View.VISIBLE);
				}else{
					recLen_normal=0;
					recLen_guide=0;
				}
				timer2.schedule(mTask2, 0, 1000); // timeTask
			}

		}.execute();
	}
}
