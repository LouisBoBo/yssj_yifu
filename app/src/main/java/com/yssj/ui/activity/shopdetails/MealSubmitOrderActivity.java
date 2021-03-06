package com.yssj.ui.activity.shopdetails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.yssj.Constants;
import com.yssj.YConstance;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.data.DBService;
import com.yssj.entity.DeliveryAddress;
import com.yssj.entity.Shop;
import com.yssj.model.ComModel2;
import com.yssj.ui.activity.setting.ManMyDeliverAddr;
import com.yssj.ui.activity.setting.SetDeliverAddressActivity;
import com.yssj.ui.activity.setting.SettingCommonFragmentActivity;
import com.yssj.ui.base.BasicActivity;
import com.yssj.ui.receiver.TaskReceiver;
import com.yssj.utils.MD5Tools;
import com.yssj.utils.PicassoUtils;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.QRCreateUtil;
import com.yssj.utils.SetImageLoader;
import com.yssj.utils.ShareUtil;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.YCache;
import com.yssj.wxpay.WxPayUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ??????????????????
 */
public class MealSubmitOrderActivity extends BasicActivity {

	private TextView tv_name, tv_phone, tv_receiver_addr;

	private LinearLayout lin_receiver_addr;

	private DBService db = new DBService(this);

	private TextView tv_sum, tv_pro_name;
	private ImageView img_pro_pic;

	private TextView express_money;

	private Button btn_pay;
	private TextView total_account;

	private DeliveryAddress dAddress;
	private HashMap<String, String> addResult;
	private String message = "";
	private String orderNo;

	Map<String, String> resultunifiedorder;

	IWXAPI msgApi;// ??????api

	private int addressId = 0;

	private LinearLayout rel_set_addr;

	// title ??????
	private TextView tvTitle_base;
	private LinearLayout img_back;

	private LinearLayout lin_set_addr;
	private RelativeLayout rel_name_phone;
	private Boolean isClicked = false;

	private TextView tv_pro_former_price, tv_ProDiscount, tvTotalAccount;

	private TextView tv_useable_integral, tv_notice, meal;
	private EditText et_input_integral;

	public static MealSubmitOrderActivity instance;

	private String orderToken;

	private TaskReceiver receiver;

	private RelativeLayout rel_show_share;

	private ImageView img_count_down;

	private int[] countDownBg = { R.drawable.count_down_1, R.drawable.count_down_2, R.drawable.count_down_3,
			R.drawable.count_down_3 };
	private Intent intent;

	private List<Shop> list;

	private boolean isBindPhone = true, isLackofIntegral = false;

	private String signValue;// ??????????????????
	private String signShopDetail;
	private int signType;// ?????? ????????????

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		aBar.hide();
		instance = this;
		orderToken = YCache.getOrderToken(this);
		setContentView(R.layout.meal_submit_order);
		msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp(WxPayUtil.APP_ID);
//		aBar.hide();

		intent = ShareUtil.shareMultiplePictureToTimeLine(ShareUtil.getImage());

		initData();
		preSubmitOrder();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isClicked = false;
		// MobclickAgent.onPageStart("MealSubmitOrderActivity");
		// MobclickAgent.onResume(this);
		receiver = new TaskReceiver(this);
		TaskReceiver.regiserReceiver(this, receiver);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("MealSubmitOrderActivity");
		// MobclickAgent.onPause(this);
		unregisterReceiver(receiver);
	}

	private void initData() {
		final RelativeLayout myView = (RelativeLayout) findViewById(R.id.myview);
		myView.setBackgroundColor(Color.WHITE);
		myView.setVisibility(View.GONE);
		new SAsyncTask<Void, Void, HashMap<String, String>>(this, myView, R.string.wait) {

			@Override
			protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				// TODO Auto-generated method stub
				return ComModel2.getDefaultDeliverAddr(context);
			}

			protected boolean isHandleException() {
				return true;
			};

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
				super.onPostExecute(context, result);
				// TODO Auto-generated method stub
				// if (null != result) {
				// if (e == null) {
				myView.setVisibility(View.VISIBLE);
				initView(result);
				// }
				// }
			}

		}.execute();

		packageCode = getIntent().getStringExtra("packageCode");
		stockType = getIntent().getStringExtra("stockType");
		shop_num = getIntent().getIntExtra("package_num", 1);
		pic = getIntent().getStringExtra("pic");

		shop_price = getIntent().getDoubleExtra("shop_price", 0.00);
		shop_se_price = getIntent().getDoubleExtra("shop_se_price", 0.00);

		postage = getIntent().getStringExtra("postage");
		name = getIntent().getStringExtra("name");

		pos = getIntent().getStringExtra("pos");
		list = (List<Shop>) getIntent().getSerializableExtra("list");

		signShopDetail = getIntent().getStringExtra("signShopDetail");
		// signValue = getIntent().getStringExtra("signValue");
		signType = getIntent().getIntExtra("signType", 0);
	}

	private String pos;
	private String packageCode, stockType, pic;
	private double shop_price, shop_se_price;
	private int shop_num;
	private String postage;
	private String name;

	double sum;

	TimeCount time;

	private void initView(HashMap<String, String> result) {

		// tvTitle_base = (TextView) findViewById(R.id.tvTitle_base);
		// tvTitle_base.setText("????????????");
		// findViewById(R.id.imgbtn_left_icon).setOnClickListener(this);

		// tv_pro_price = (TextView) findViewById(R.id.tv_pro_price);
		// tv_pro_price.setText("??: " + price);

		img_back = (LinearLayout) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		tvTitle_base = (TextView) findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("????????????");
		findViewById(R.id.rl_containt).setOnClickListener(this);

		tv_pro_former_price = (TextView) findViewById(R.id.tv_pro_former_price);
		tv_ProDiscount = (TextView) findViewById(R.id.tv_pro_price);// ?????????
		// ????????????
		tv_ProDiscount.setText("??" + new java.text.DecimalFormat("#0.00").format(shop_se_price));

		if (!(shop_se_price + "").equals(shop_price)) {
			// ToastUtil.addStrikeSpan(tv_pro_former_price, "??:" + shop_price);
			tv_pro_former_price.setVisibility(View.VISIBLE);
			tv_pro_former_price.setText("??" + new java.text.DecimalFormat("#0.00").format(shop_price));
			tv_pro_former_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}

		tv_name = (TextView) findViewById(R.id.tv_name);// ?????????
		tv_phone = (TextView) findViewById(R.id.tv_phone);// ???????????????
		tv_receiver_addr = (TextView) findViewById(R.id.tv_receiver_addr);// ????????????
		lin_receiver_addr = (LinearLayout) findViewById(R.id.lin_receiver_addr);
		lin_receiver_addr.setOnClickListener(this);
		lin_set_addr = (LinearLayout) findViewById(R.id.lin_set_addr);
		lin_set_addr.setOnClickListener(this);

		tvTotalAccount = (TextView) findViewById(R.id.tv_total_account);

		total_account = (TextView) findViewById(R.id.total_account);// ???????????????????????????

		rel_name_phone = (RelativeLayout) findViewById(R.id.rel_name_phone);

		express_money = (TextView) findViewById(R.id.express_money);

		express_money.setText(Html.fromHtml(getString(R.string.tv_mail_fee, postage)));
		this.addResult = result;
		// dAddress = result.get(0);
		setDeliverAddress(result, null);

		tv_sum = (TextView) findViewById(R.id.tv_sum);// ????????????x????????????
		tv_pro_name = (TextView) findViewById(R.id.tv_pro_name);// ????????????
		img_pro_pic = (ImageView) findViewById(R.id.img_pro_pic);// ????????????
		btn_pay = (Button) findViewById(R.id.btn_pay);// ????????????
		btn_pay.setOnClickListener(this);

		sum = Double.valueOf(postage) + shop_se_price;

		// ????????????????????????
		total_account.setText("??" + new java.text.DecimalFormat("#0.00").format(sum));
		tvTotalAccount.setText("???????????" + new java.text.DecimalFormat("#0.00").format(sum));

		tv_sum.setText("x" + shop_num);
		tv_pro_name.setText(name);

		// String shopPic = shop.getShop_code().substring(1,
		// 4)+"/"+shop.getShop_code()+"/" + pic;
//		SetImageLoader.initImageLoader(this, img_pro_pic, pic, "");
		PicassoUtils.initImage(this, pic, img_pro_pic);

		tv_useable_integral = (TextView) findViewById(R.id.tv_useable_integral);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		et_input_integral = (EditText) findViewById(R.id.et_input_integral);
		et_input_integral.setEnabled(false);

		rel_show_share = (RelativeLayout) findViewById(R.id.rel_show_share);

		img_count_down = (ImageView) findViewById(R.id.img_count_down);

		meal = (TextView) findViewById(R.id.meal);
		if (null != list) {
			if (list.size() == 1)
				meal.setText("????????????");
			else
				meal.setText("????????????");
		}
	}

	private void preSubmitOrder() {
		if ("SignShopDetail".equals(signShopDetail)) {
			return;
		}
		new SAsyncTask<Void, Void, HashMap<String, Object>>(this, R.string.wait) {

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				// TODO Auto-generated method stub
				return ComModel2.getPreSubmit(context, packageCode);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					isBindPhone = true;
					if ((Integer) result.get("status") == 1) {

						tv_useable_integral.setText("(????????????????????????" + result.get("integral") + ")");

						if ((Integer) result.get("isOneBuy") == 3) {// ??????????????????
							ToastUtil.showShortText(context, "?????????????????????");
							return;
						}
						if ((Integer) result.get("isOneBuy") == 5) {// ???????????????
							// ToastUtil.showShortText(context, "???????????????");
							isBindPhone = false;
							showBindPhoneDialog();
						} else {
							if ((Integer) result.get("isOneBuy") == 0) {// ?????????????????????
								if ((Integer) result.get("needIntegral") > (Integer) result.get("integral")) {
									ToastUtil.showShortText(context, "????????????");
									tv_notice.setText("???????????????" + (Integer) result.get("needIntegral") + "??????,????????????");
									lackOfIntegral();
									isLackofIntegral = true;
								} else {// ???????????? ????????????
									btn_pay.setClickable(true);
									if ((Integer) result.get("integral") != 0)
										tv_notice.setText("???????????????" + (Integer) result.get("needIntegral") + "??????");
									else
										tv_notice.setText("????????????????????????" + 0 + ",??????" + 0 + "???");
									et_input_integral.setText((Integer) result.get("needIntegral") + "");
									isLackofIntegral = false;
								}
							} else {// ??????????????????
								btn_pay.setClickable(true);
								tv_notice.setText("????????????????????????" + 0 + ",??????" + 0 + "???");
								et_input_integral.setText("0");
							}
						}

					}
				}
			}

		}.execute();
	}

	private void lackOfIntegral() {// ????????????????????????????????????
		sendBroadcast(new Intent(TaskReceiver.newMemberTask_8));
	}

	private AlertDialog dialog;

	private void showBindPhoneDialog() {
		AlertDialog.Builder builder = new Builder(this);
		// ???????????????????????????
		View view = View.inflate(this, R.layout.payback_esc_apply_dialog, null);
		TextView tv_des = (TextView) view.findViewById(R.id.tv_des);
		tv_des.setText("??????0??????????????????????????????");

		Button ok = (Button) view.findViewById(R.id.ok);
		ok.setBackgroundResource(R.drawable.payback_esc_apply_esc);
		Button cancel = (Button) view.findViewById(R.id.cancel);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ???????????????????????????
				dialog.dismiss();
				isBindPhone = false;
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MealSubmitOrderActivity.this, SettingCommonFragmentActivity.class);
				intent.putExtra("flag", "bindPhoneFragment");
				intent.putExtra("buy0", "buy0");
				startActivityForResult(intent, 1);// (intent);
				dialog.dismiss();
				isBindPhone = false;
			}
		});

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void setDeliverAddress(HashMap<String, String> mapRet, DeliveryAddress dAddress) {
		if (null == mapRet && dAddress != null) {
			tv_name.setText("????????????" + dAddress.getConsignee());
			tv_phone.setText(dAddress.getPhone());
			String province = db.queryAreaNameById(dAddress.getProvince());
			String city = db.queryAreaNameById(dAddress.getCity());
			String county = db.queryAreaNameById(dAddress.getArea());
			String street = "";
			if (null != dAddress.getStreet() && 0 != dAddress.getStreet()) {
				street = db.queryAreaNameById(dAddress.getStreet());
			}
			tv_receiver_addr.setText("???????????????" + province + city + county + street + dAddress.getAddress());// ????????????
			lin_receiver_addr.setVisibility(View.VISIBLE);
			rel_name_phone.setVisibility(View.VISIBLE);
			lin_set_addr.setVisibility(View.GONE);
		} else if (null != mapRet && dAddress == null) {
			tv_name.setText("????????????" + mapRet.get("consignee"));
			tv_phone.setText(mapRet.get("phone"));
			tv_receiver_addr.setText("???????????????" + mapRet.get("address"));// ????????????
			lin_receiver_addr.setVisibility(View.VISIBLE);
			rel_name_phone.setVisibility(View.VISIBLE);
			lin_set_addr.setVisibility(View.GONE);
		} else if (null == mapRet && null == dAddress) {
			lin_receiver_addr.setVisibility(View.GONE);
			rel_name_phone.setVisibility(View.GONE);
			lin_set_addr.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * ????????????????????????
	 */
	private void submitZeroOrder(final View v) {

		new SAsyncTask<Void, Void, HashMap<String, Object>>(this, null, R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				return ComModel2.submitZeroOrder(context, message, packageCode, stockType, shop_num, addressId,
						orderToken);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					orderNo = (String) result.get("order_code");

					/*
					 * ToastUtil.showShortText(context, (String)
					 * result.get("message"));
					 */
					LogYiFu.e("SpicEnd", System.currentTimeMillis() + "");

					// ????????????????????????
					Intent intent = new Intent(MealSubmitOrderActivity.this, MealPaymentActivity.class);
					LogYiFu.e("TAG", "??????????????????");
					int url = (Integer) result.get("url");
					Bundle bundle = new Bundle();
					bundle.putSerializable("result", result);
					intent.putExtras(bundle);
					intent.putExtra("isMulti", false);
					// orderNo = getIntent().getStringExtra("order_code");
					intent.putExtra("order_code", orderNo);
					intent.putExtra("totlaAccount", sum);
					intent.putExtra("pos", pos);
					if (url > 1) {
						intent.putExtra("isMulti", true);
					}
					startActivityForResult(intent, 1003);
				}
			}

		}.execute((Void[]) null);
	}

	/**
	 * ?????? ??????????????????
	 */
	private void submitSignOrder(final View v) {
		new SAsyncTask<Void, Void, HashMap<String, Object>>(this, null, R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				packageCode = list.get(0).getShop_code();// ?????? ???????????? ????????????
				return ComModel2.submitZeroOrderSign(context, message, packageCode, stockType, addressId);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					orderNo = (String) result.get("order_code");
					/*
					 * ToastUtil.showShortText(context, (String)
					 * result.get("message"));
					 */

					// ????????????????????????
					Intent intent = new Intent(MealSubmitOrderActivity.this, MealPaymentActivity.class);
					LogYiFu.e("TAG", "??????????????????");
					int url = (Integer) result.get("url");
					Bundle bundle = new Bundle();
					bundle.putSerializable("result", result);
					intent.putExtras(bundle);
					intent.putExtra("isMulti", false);
					intent.putExtra("totlaAccount", sum);

					intent.putExtra("signShopDetail", signShopDetail);
					intent.putExtra("signType", signType);
					intent.putExtra("signValue", signValue);
					// intent.putExtra("pos", pos);
					if (url > 1) {
						intent.putExtra("isMulti", true);
					}
					startActivityForResult(intent, 1003);
				}
			}

		}.execute((Void[]) null);
	}

	/**
	 * ????????????
	 */
	private void submitZeroOrderNoShare() {

		new SAsyncTask<Void, Void, HashMap<String, Object>>(this, null, R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				return ComModel2.submitZeroOrder(context, message, packageCode, stockType, shop_num, addressId,
						orderToken);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					orderNo = (String) result.get("order_code");

					/*
					 * ToastUtil.showShortText(context, (String)
					 * result.get("message"));
					 */

					// ????????????????????????
					Intent intent = new Intent(MealSubmitOrderActivity.this, MealPaymentActivity.class);
					LogYiFu.e("TAG", "??????????????????");
					int url = (Integer) result.get("url");
					Bundle bundle = new Bundle();
					bundle.putSerializable("result", result);
					intent.putExtras(bundle);
					intent.putExtra("isMulti", false);
					intent.putExtra("totlaAccount", sum);
					intent.putExtra("pos", pos);
					if (url > 1) {
						intent.putExtra("isMulti", true);
					}
					startActivityForResult(intent, 1003);
				}
			}

		}.execute((Void[]) null);
	}

	/**
	 * ????????????
	 */
	private void getPshareShop() {
		new SAsyncTask<String, Void, HashMap<String, Object>>(this, R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, String... params)
					throws Exception {
				// TODO Auto-generated method stub
				return ComModel2.getPshopLink(params[0], context, "true");
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				// TODO Auto-generated method stub
				super.onPostExecute(context, result, e);
				if (null == e) {
					if (result.get("status").equals("1")) {
						LogYiFu.e("pic", (String) result.get("shop_pic"));
						String[] picList = ((String) result.get("shop_pic")).split(",");
						String link = (String) result.get("link");
						download(null, picList, packageCode, result, link);
					} else if (result.get("status").equals("1050")) {// ??????
						Intent intent = new Intent(context, NoShareActivity.class);
						intent.putExtra("isNomal", true);
						context.startActivity(intent); // ?????????????????????

					}
				}
			}

		}.execute(packageCode);
	}

	/** ????????????????????? */
	private void download(View v, final String[] picList, final String shop_code,
			final HashMap<String, Object> mapInfos, final String link) {
		new SAsyncTask<Void, Void, Void>((FragmentActivity) MealSubmitOrderActivity.this, v, R.string.wait) {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
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
						Bitmap bm = QRCreateUtil.createZeroImage(link, 500, 700, (String) mapInfos.get("shop_se_price"),
								MealSubmitOrderActivity.this);// ?????????????????????
						QRCreateUtil.saveBitmap(bm, YConstance.savePicPath, MD5Tools.md5(String.valueOf(9)) + ".jpg");// ?????????????????????
						// downloadPic(mapInfos.get("qr_pic"), 9);
						break;
					}
					downloadPic(pics.get(i) + "!450", i);
				}
				return super.doInBackground(params);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, Void result) {
				// TODO Auto-generated method stub
				// showShareDialog();
				/*
				 * ShareUtil.configPlatforms(context); UMImage umImage = new
				 * UMImage(context, R.drawable.ic_launcher);
				 * ShareUtil.setShareContent(context, umImage,
				 * shop.getShop_name(), link);
				 */
				// ShareUtil.share(ShopDetailsActivity.this);
				/*
				 * myPopupwindow = new MyPopupwindow(context, 0);
				 * myPopupwindow.showAtLocation(
				 * context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
				 */

				rel_show_share.setVisibility(View.VISIBLE);
				time = new TimeCount(4000, 1000);
				time.start();

				super.onPostExecute(context, result);

			}

		}.execute();
	}

	private int requestCode = 100;

	private void onceShare(Intent intent, String perform) {
		if (ShareUtil.intentIsAvailable(this, intent)) {
			this.startActivityForResult(intent, requestCode);
		} else {
			/*
			 * Toast.makeText(this, "????????????" + perform + "?????????",
			 * Toast.LENGTH_SHORT).show();
			 */
			ToastUtil.showShortText(this, "????????????0????????????????????????????????????????????????????????????~");
			// submitZeroOrder(null);
		}
	}

	private void downloadPic(String picPath, int i) {
		try {
			URL url = new URL(YUrl.imgurl + picPath);
			// ????????????
			URLConnection con = url.openConnection();
			// ?????????????????????
			int contentLength = con.getContentLength();
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

	/**
	 * ?????????????????????
	 **/
	private void getShareShop(final View v) {
		new SAsyncTask<Void, Void, HashMap<String, Object>>(this, v, R.string.wait) {

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
					throws Exception {
				// TODO Auto-generated method stub
				return ComModel2.getSharePShopInfoDduobao(context, packageCode, true);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					if ((Integer) result.get("status") == 1) {
						// ?????????900 X 900 ?????????

						createSharePic((String) result.get("link") + "&post=true", (String) result.get("four_pic"),
								(String) result.get("price"), (String) result.get("shop_code"), v);
					}
					// submitZeroOrder(v);
				}
			}

		}.execute();
	}

	private void createSharePic(final String link, final String picPath, final String price, final String shop_code,
			final View v) {
		new SAsyncTask<Void, Void, Void>(this, R.string.wait) {

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected Void doInBackground(FragmentActivity context, Void... params) throws Exception {
				// TODO Auto-generated method stub
				Bitmap bmQr = QRCreateUtil.createQrImage(link, 160, 160);// ?????????????????????
				String pic = shop_code.substring(1, 4) + "/" + shop_code + "/" + picPath;
				Bitmap bmBg = downloadPic(pic);

				Bitmap bmNew = QRCreateUtil.drawNewBitmap1(context, bmBg, bmQr, price, "2");//??????????????????????????????

				QRCreateUtil.saveBitmap(bmNew, YConstance.savePicPath, MD5Tools.md5(String.valueOf(9)) + ".jpg");// ?????????????????????
				return super.doInBackground(context, params);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, Void result, Exception e) {
				// TODO Auto-generated method stub
				super.onPostExecute(context, result, e);
				if (null == e) {
					/*
					 * File file = new File(YConstance.savePicPath,
					 * MD5Tools.md5(String.valueOf(9)) + ".jpg"); share(file,
					 * v);
					 */
					rel_show_share.setVisibility(View.VISIBLE);
					ToastUtil.showShortText(context, "????????????????????????~");
					time = new TimeCount(4000, 1000);
					time.start();
				}
			}

		}.execute();
	}

	public UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR_SHARE);

	private String flag = "";

	private void share(File file, final View v) {

		if (file == null) {
			Toast.makeText(this, "??????????????????????????????~~", Toast.LENGTH_SHORT).show();
			return;
		}

		UMImage umImage = new UMImage(this, file);
		// UMImage umImage = new UMImage(this, R.drawable.huo_dong);
		// UMImage umImage = new UMImage(this, R.drawable.huodong_new);
		ShareUtil.configPlatforms(this);
		ShareUtil.shareShop(this, umImage);

		mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {

					String showText1 = platform.toString();
					showText1 = "";
					// submitZeroOrder(v);
					submitSignOrder(v);
				} else {
					/*
					 * showText += "??????????????????";
					 * Toast.makeText(MealSubmitOrderActivity.this, showText,
					 * Toast.LENGTH_SHORT).show();
					 */
				}
			}
		});

	}

	private Bitmap downloadPic(String picPath) {
		try {
			URL url = new URL(YUrl.imgurl + picPath);
			// ????????????
			URLConnection con = url.openConnection();
			// ?????????????????????
			int contentLength = con.getContentLength();
//			System.out.println("?????? :" + contentLength);
			// ?????????
			InputStream is = con.getInputStream();
			// 1K???????????????
			byte[] bs = new byte[8192];
			// ????????????????????????
			int len;
			BitmapDrawable bmpDraw = new BitmapDrawable(is);

			// ???????????????????????????
			is.close();
			return bmpDraw.getBitmap();
		} catch (Exception e) {
			LogYiFu.e("TAG", "????????????");

			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (null != time) {
			time.cancel();
		}
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_pay:
			// payMoney(v);
			if (!isBindPhone) {
				// ToastUtil.showShortText(this, "?????????????????????");
				showBindPhoneDialog();
				return;
			}
			if (isLackofIntegral) {
				preSubmitOrder();
				return;
			}
			if (addResult == null && dAddress == null) {
				ToastUtil.showShortText(this, "?????????????????????");
				return;
			}
			// getPshareShop();

			// if()
			if ("SignShopDetail".equals(signShopDetail)) {

				// ?????????????????????????????????
				new SAsyncTask<Void, Void, HashMap<String, Object>>(this) {

					@Override
					protected boolean isHandleException() {
						return true;
					}

					@Override
					protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
							throws Exception {
						return ComModel2.checkBaoyou(context);
					}

					@Override
					protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result,
							Exception e) {
						super.onPostExecute(context, result, e);

						if (e == null && result != null) {
							flag = (String) result.get("flag");

							if (flag.equals("0")) {// ?????????????????????????????????
									

								if (!isClicked) {
									
//									ToastUtil.showShortText(context, "?????????????????????????????????~");
//									getShareShop(v); // ????????????
									//????????????
									submitSignOrder(v);
									
									
									isClicked = true;
								}
								
							} else { // ?????????
								ToastUtil.showShortText(context, "??????????????????????????????~");
								return;
							}

						}

					}

				}.execute();
				
				
				
				
				

			} else {
				submitZeroOrderNoShare(); // ???????????? ?????????????????????????????????

				// MyLogYiFu.e("MealType",ShopDetailsActivity.MealType);
				// if(Integer.parseInt(ShopDetailsActivity.MealType) == 1){
				// getShareShop(v); // ????????????
				// }else{
				// submitZeroOrderNoShare(); //????????????
				// }
			}

			//
			// submitZeroOrderNoShare();//????????????
			// submitZeroOrder(v); ?????????
			break;
		case R.id.lin_receiver_addr:
			// intent = new Intent(this, ChooseMyDeliverAddr.class);
			intent = new Intent(this, ManMyDeliverAddr.class);
			intent.putExtra("flag", "submitmultishop");
			startActivityForResult(intent, 1001);
			break;
		case R.id.lin_set_addr:
			intent = new Intent(this, SetDeliverAddressActivity.class);
			startActivityForResult(intent, 1002);
			break;
		case R.id.img_back:
			customDialog();
			break;
		case R.id.imgbtn_left_icon:
			this.finish();
			break;
		case R.id.rl_discount_coupon:
			/*
			 * intent = new Intent(this, UsefulCouponsActivity.class);
			 * intent.putExtra("amount", sum); startActivityForResult(intent,
			 * 1005);
			 */
			break;
		case R.id.rl_containt:

			if ("SignShopDetail".equals(signShopDetail)) {
				return;
			} else {
				intent = new Intent(this, ShopDetailsActivity.class);
				intent.putExtra("isMeal", true);
				intent.putExtra("code", packageCode);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}

			break;

		default:
			break;
		}
		super.onClick(v);
	}

	private void customDialog() {
		final Dialog dialog = new Dialog(this, R.style.invate_dialog_style);
		View view = View.inflate(this, R.layout.cancle_order_dialog, null);
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText("??????????????????,???????????????????");
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ???????????????????????????
				dialog.dismiss();
			}
		});
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != time) {
					time.cancel();
				}
				finish();
				dialog.dismiss();
			}
		});

		// ?????????????????????dialog
		dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 1001) {
			if (null != intent) {
				dAddress = (DeliveryAddress) intent.getSerializableExtra("item");
				addressId = dAddress.getId();
				setDeliverAddress(null, dAddress);
			}
		} else if (requestCode == 1002) {
			initData();
		} else if (requestCode == 1003 && resultCode == 1) {
			finish();
		} else if (requestCode == 1005 && resultCode == 2001) {
		} else if (requestCode == 1) {
			preSubmitOrder();
		} else if (requestCode == 100) {
			if ("SignShopDetail".equals(signShopDetail)) {
				submitSignOrder(null);
			} else
				submitZeroOrder(null);
		}
	}

	// ?????????
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ????????????????????????,????????????????????????

		}

		@Override
		public void onFinish() {// ?????????????????????
			try {
				File file = new File(YConstance.savePicPath, MD5Tools.md5(String.valueOf(9)) + ".jpg");
				LogYiFu.e("picEnd", System.currentTimeMillis() + "");
				share(file, null);
				// onceShare(intent, "??????");

				rel_show_share.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {// ??????????????????
			try {
				// btn.setEnabled(false);
				// btn.setBackgroundResource(R.color.time_count);
				// text.setText(millisUntilFinished / 1000 + "???????????????????????????");
				int i = Integer.valueOf(millisUntilFinished / 1000 + "") - 1;
				img_count_down.setImageResource(countDownBg[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// ?????????????????????

			customDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
