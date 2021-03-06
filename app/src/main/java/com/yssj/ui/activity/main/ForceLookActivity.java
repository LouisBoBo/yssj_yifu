package com.yssj.ui.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yssj.YConstance;
import com.yssj.YConstance.Pref;
import com.yssj.YJApplication;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.AppManager;
import com.yssj.app.SAsyncTask;
import com.yssj.data.YDBHelper;
import com.yssj.entity.VipDikouData;
import com.yssj.model.ComModel2;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.activity.ShopCartNewNewActivity;
import com.yssj.ui.adpter.ForceLookAdapter;
import com.yssj.ui.base.BasicActivity;
import com.yssj.ui.dialog.LeaveDialog;
import com.yssj.ui.dialog.XunBaoDialog;
import com.yssj.ui.fragment.circles.SignListAdapter;
import com.yssj.utils.CommonUtils;
import com.yssj.utils.PicassoUtils;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.YCache;
import com.yssj.utils.sqlite.ShopCartDao;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class ForceLookActivity extends BasicActivity {
	public static int click_num; // ??????????????????
	public static int num_tongji;// ???????????????????????????

	public static int now_type_id_value;
	public static int now_type_id;
	public static int next_type_id;
	public static int next_type_id_value;
	private String theme_id = "";
    private boolean isMoreShop;//???????????????????????????????????????

	private long recLen; // 30??? ???????????? ????????????
	Timer timer = new Timer();

	private PullToRefreshListView r_list_view;

	private ForceLookAdapter mAdapter;
	public static  ForceLookActivity instance;

	private int index = 1;

	private Boolean isTimeout = false;

	private int mType = 1;// 1?????????????????????2?????????????????????

	private HashMap<String, Object> map;
	private HashMap<String, String> mapItem;
	private String oldId, idSearch;
	private String id, fromName;

	private String pinJievalue = "";

	private LinearLayout llContaint, lv_kaiqifanbei;

	private HorizontalScrollView hsv_containt;

	// private String checkId;

	private int pageSize = 30;
	private RelativeLayout rl_yuefanbei;

	private boolean isComplete = false;// ?????????????????????????????? ??????????????? ????????? ???????????????

	private LinearLayout llNodata;
	// ??????????????????????????????<pageSize?????????
	private boolean isTuijian = false;
	private boolean isSignQiangzhiliulan = false; // ??????????????????5??????????????????

	private ImageButton imgbtn_left_icon_sign, imgbtn_left_icon;

//	private String is_new = null, order_by_price = null;
	private String is_hot = null, is_new = "is_new", order_by_price = null;
	private TextView tv_title, liulanTime, tv_cart_count_Force;
	private TextView mTitle;//
	private boolean notType = false;
	private TextView tvNew, tvHot, tvDesc, tvAsc;
	private View horizontal_title_ll;
	private String doIconId;
	private Context context;
	private VipDikouData vipDikouData;

	// isComplete???true
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		context = this;
//		SignListAdapter.doType = 19;
		// formatter = new SimpleDateFormat ("yyyy:MM:dd HH:mm:ss");
		// Date curDate = new Date(System.currentTimeMillis());//??????????????????
		// come_time = formatter.format(curDate);
		// System.out.println("???????????????="+come_time);
		instance = this;
		long come_time = System.currentTimeMillis();
		come_time_in = DateFormatUtils.format(Long.parseLong(come_time + ""), "yyyy.MM.dd HH:mm:ss");
		// System.out.println("???????????????="+come_time_in);

		random = Integer.valueOf((int) (Math.random() * 3 + 3));
		click_num = 1;
		num_tongji = 0;// ???????????????????????????

		id_new = getIntent().getIntExtra("now_type_id", -1); // ????????????
		nextID = getIntent().getIntExtra("now_type_id_value", -1);
		isMoreShop = getIntent().getBooleanExtra("isMoreShop", false);
		theme_id = getIntent().getStringExtra("themeId");
		type_id = getIntent().getIntExtra("next_type_id", -1);
		type_id_value = getIntent().getIntExtra("next_type_id_value", -1);

		now_type_id_value = nextID;
		now_type_id = id_new;
		next_type_id = type_id;
		next_type_id_value = type_id_value;

		// Boolean isfirst = SharedPreferencesUtil.getBooleanData(context,
		// "isfirst", true);
		// if (isfirst) {
		// explain();
		// }

		// SignExplain signExplain = new SignExplain(context);
		// signExplain.show();

//		aBar.hide();
		map = (HashMap<String, Object>) getIntent().getSerializableExtra("condition");
		mapItem = (HashMap<String, String>) getIntent().getSerializableExtra("item");
		if (mapItem != null) {
			idSearch = mapItem.get("_id");
			oldId = idSearch;
		}
		id = getIntent().getStringExtra("id");
		fromName = getIntent().getStringExtra("title");
		if(SignListAdapter.doType == 4||SignListAdapter.doType ==19){//???????????? ?????? ???????????????
			pinJievalue = getIntent().getStringExtra("pinJievalue");
			doIconId = getIntent().getStringExtra("doIconId");//????????????ID
		}else if(SignListAdapter.doType == 5){//???????????????
			pinJievalue = SignListAdapter.fenzhongDoValueMap.get(YConstance.SCAN_SHOP_TIME);
			doIconId =SignListAdapter.fenzhongIconID.get(YConstance.SCAN_SHOP_TIME);//????????????ID
		}else{
			pinJievalue = SignListAdapter.doValue.split(",")[0];
//			doIconId = getIntent().getStringExtra("doIconId");//????????????ID
		}

		isTuijian = getIntent().getBooleanExtra("isTuijian", false);
		isSignQiangzhiliulan = getIntent().getBooleanExtra("qiangZhiLiuLan", false);
//		isSignLiulan = getIntent().getBooleanExtra("isSignLiulan", false);
		// checkId = getIntent().getStringExtra("checkId");
		setContentView(R.layout.force_look);

		haisheng = (TextView) findViewById(R.id.haisheng);
		if (id_new == 11) {
			haisheng.setText("??????2???????????????????????????????????????????????????????????????~");
		}
		if (id_new == 12) {
			haisheng.setText("??????5???????????????????????????????????????????????????????????????~");
		}
		if (id_new == 15) {
			haisheng.setText("??????1???????????????????????????????????????????????????????????????~");
		}
		if (id_new == 20) {
			haisheng.setText("??????3???????????????????????????????????????????????????????????????~");
		}
		r_list_view = (PullToRefreshListView) findViewById(R.id.r_list_view);
		headView = LayoutInflater.from(this).inflate(R.layout.view_scan_header,null);
//		addHeadeViewBanner();//????????????????????????
		horizontal_title_ll = headView.findViewById(R.id.horizontal_title_ll);
		tvNew = (TextView) headView.findViewById(R.id.create_is_new);
		tvHot = (TextView) headView.findViewById(R.id.create_is_hot);
		tvDesc = (TextView) headView.findViewById(R.id.create_price_desc);
		tvAsc = (TextView) headView.findViewById(R.id.create_price_asc);
		tvHot.setOnClickListener(this);
		tvNew.setOnClickListener(this);
		tvDesc.setOnClickListener(this);
		tvAsc.setOnClickListener(this);

		if ("collection=browse_shop".equals(pinJievalue)){
			tvHot.setVisibility(View.GONE);
		}
		if (isMoreShop){
			horizontal_title_ll.setVisibility(View.GONE);
		}

		tv_shuoming = (TextView) findViewById(R.id.tv_shuoming);
		tv_shuoming.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.tv_forcelook_title);
		tv_title = (TextView) findViewById(R.id.tv_title);
		liulanTime = (TextView) findViewById(R.id.liulanTime);
		imgbtn_left_icon_sign = (ImageButton) findViewById(R.id.imgbtn_left_icon_sign);
		imgbtn_left_icon = (ImageButton) findViewById(R.id.imgbtn_left_icon);
		lv_kaiqifanbei = (LinearLayout) findViewById(R.id.lv_kaiqifanbei);

//		shop_cart = (ImageButton) findViewById(R.id.shop_cart);

		findViewById(R.id.img_back).setOnClickListener(this);
//		img_btn_filter = (ImageButton) findViewById(R.id.img_btn_filter);
//		img_btn_filter.setOnClickListener(this);
		// ??????????????????
		// llContaint = (LinearLayout) findViewById(R.id.ll_containt);
		// addView(llContaint, map);

		rl_yuefanbei = (RelativeLayout) findViewById(R.id.rl_yuefanbei);
		tv_cart_count_Force = (TextView) findViewById(R.id.tv_cart_count_Force);


		llNodata = (LinearLayout) findViewById(R.id.ll_nodata);
		findViewById(R.id.btn_view_allcircle).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_no_join)).setText("????????????");
		r_list_view.setMode(Mode.BOTH);



		int shopCartCount = new ShopCartDao(context).queryCartCount(context);

		if (shopCartCount > 0) {
			tv_cart_count_Force.setText(shopCartCount + "");
			tv_cart_count_Force.setVisibility(View.VISIBLE);
		} else {
			tv_cart_count_Force.setVisibility(View.GONE);
		}

		mAdapter = new ForceLookAdapter(this,isMoreShop);
		r_list_view.setAdapter(mAdapter);

		// ??????????????????????????????????????????

		if (isSignQiangzhiliulan) {
			tv_title.setText("???????????????");
			imgbtn_left_icon_sign.setVisibility(View.VISIBLE);
//			shop_cart.setVisibility(View.VISIBLE);
			rl_yuefanbei.setVisibility(View.VISIBLE);

			imgbtn_left_icon.setVisibility(View.GONE);
//			img_btn_filter.setVisibility(View.GONE);
		}
		if (SignListAdapter.doType == 5 || SignListAdapter.doType == 6 ||
				SignListAdapter.doType == 24) {// ???????????? ??????(?????????????????????)
			mTitle.setText(fromName);
			rl_yuefanbei.setVisibility(View.GONE);
//			img_btn_filter.setVisibility(View.GONE);
			tv_cart_count_Force.setVisibility(View.GONE);
//			shop_cart.setVisibility(View.GONE);
		}
		setListViewRefresh();

		if (YJApplication.instance.isLoginSucess()) {
			//????????????
			HashMap<String, String> pairsMap = new HashMap<>();
			YConn.httpPost(ForceLookActivity.this, YUrl.GETALLDIKOU, pairsMap, new HttpListener<VipDikouData>() {
				@Override
				public void onSuccess(VipDikouData result) {

					vipDikouData = result;
					initData(index + "", is_new, order_by_price);

				}

				@Override
				public void onError() {

				}
			});


		} else {
			initData(index + "", is_new, order_by_price);

		}












		// if(SharedPreferencesUtil.getStringData(context,
		// "tags","").equals("11")||SharedPreferencesUtil.getStringData(context,
		// "tags","").equals("10")){
		// new VersionChangDialog(context, R.style.DialogStyle1).show();
		// }

		// // ?????????????????????
		// if (mTask != null) {
		// mTask.cancel();
		// mTask = new MyTimerTask();
		// } else {
		// mTask = new MyTimerTask();
		// }
		// recLen=120*1000;
		// timer.schedule(mTask, 0, 1000); // timeTask
		if (SignListAdapter.doType == 4&&!isMoreShop) {// ??????????????????
			if (!SignListAdapter.isSignComplete) {
				showDialog();
			}

			// ??????????????????????????????????????????
			String value = SignListAdapter.doValue;
			String values[] = value.split(",");
			if (values.length > 1) {
				singvalue = values[1];
				if (!Pattern.compile("^\\+?[1-9][0-9]*$").matcher(singvalue).find()) {
					singvalue = "" + SignListAdapter.doNum;
				}

			} else {
				singvalue = "" + SignListAdapter.doNum;
			}
		}
        if (SignListAdapter.doType == 19&&!isMoreShop) {// ???????????????????????? ??????????????????

            // ??????????????????????????????????????????
            String value = SignListAdapter.doValue;
            String values[] = value.split(",");
            if (values.length > 1) {
                singvalue = values[1];
				if (!Pattern.compile("^\\+?[1-9][0-9]*$").matcher(singvalue).find()) {
					singvalue = "10";
				}
            }
			else {
				singvalue = "10";
			}
            haisheng.setText("?????????"+singvalue+"??????????????????"+SignListAdapter.jiangliValue+"????????????????????????????????????~");
			if (!SignListAdapter.isSignComplete) {
				showTXDialog();
			}
			if(TextUtils.isEmpty(SignListAdapter.doNeedCount)||"-1".equals(SignListAdapter.doNeedCount)
					||"null".equals(SignListAdapter.doNeedCount)){
				//??????????????????????????? ???????????????????????????
			}else{
				int forcelookLimitNum = Integer
						.parseInt(
								SharedPreferencesUtil
										.getStringData(ForceLookActivity.this,
												SignListAdapter.signIndex + Pref.ISFORCELOOKLIMITNUM
														+ YCache.getCacheUser(context).getUser_id(),
												"0"));
				int forceLookLimitNumTemp = ((SignListAdapter.doNum-Integer.parseInt(SignListAdapter.doNeedCount))*Integer.parseInt(singvalue)
				+forcelookLimitNum%Integer.parseInt(singvalue));//??????????????????????????????????????????????????? ????????? ?????????????????????????????????????????????????????????
				SharedPreferencesUtil.saveStringData(ForceLookActivity.this,
								SignListAdapter.signIndex + Pref.ISFORCELOOKLIMITNUM
										+ YCache.getCacheUser(context).getUser_id(),
								"" + forceLookLimitNumTemp);
			}
        }
		
		
		
		if(isMoreShop){
			mTitle.setText(fromName);
			rl_yuefanbei.setVisibility(View.GONE);
//			img_btn_filter.setVisibility(View.GONE);
			tv_cart_count_Force.setVisibility(View.GONE);
		}
		
	}

	private void setListViewRefresh() {
		r_list_view.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				mType = 1;
				index = 1;
				if (isTuijian) {
					initData(index + "",  is_new, order_by_price);
					return;
				}
				initData(index + "", is_new, order_by_price);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// if (!isComplete) {// ???????????????????????? ????????????????????????
				// if (SignListAdapter.doType!=4) {
				index++;
				mType = 2;
				if (isTuijian) {
					initData(index + "", is_new, order_by_price);
				} else {
					initData(index + "",  is_new, order_by_price);
				}
				// }else{
				// r_list_view.onRefreshComplete();//?????????????????? ??????????????????????????????
				// }
			}
		});

	}

	/**
	 * ????????????????????????????????? ?????????????????????
	 */
	private void showDialog() {
		SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String forceLook = SharedPreferencesUtil.getStringData(context, Pref.FORCELOOK, "0");
		long forceLookTime = Long.parseLong(forceLook);
		if ("0".equals(forceLook) || !df.format(new Date()).equals(df.format(new Date(forceLookTime)))) {
			new XunBaoDialog(context).show();
			SharedPreferencesUtil.saveStringData(context, Pref.FORCELOOK, System.currentTimeMillis() + "");
		}

	}
	/**
	 * ????????????????????????????????? ?????????????????????(???????????????)
	 */
	private void showTXDialog() {
		SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String forceLookTX = SharedPreferencesUtil.getStringData(context, Pref.FORCELOOKTX, "0");
		long forceLookTimeTX = Long.parseLong(forceLookTX);
		if ("0".equals(forceLookTX) || !df.format(new Date()).equals(df.format(new Date(forceLookTimeTX)))) {
			XunBaoDialog dialog = new XunBaoDialog(context);
			dialog.show();
			dialog.setContentText("?????????"+singvalue+"??????????????????"+SignListAdapter.jiangliValue+"??????????????????????????????????????????????????????????????????~");
			SharedPreferencesUtil.saveStringData(context, Pref.FORCELOOKTX, System.currentTimeMillis() + "");
		}

	}
	private MyTimerTask mTask;

	private void initData(final String index,  final String is_new,
			final String order_by_price) {

		// ??????????????????
		final int pageSize = 10;
		new SAsyncTask<String, Void, List<HashMap<String, Object>>>(this, 0) {
			@Override
			protected List<HashMap<String, Object>> doInBackground(FragmentActivity context, String... params)
					throws Exception {
				if(isMoreShop){//????????????--??????????????????
					return ComModel2.getMoreShop(context, pageSize, index,theme_id);
				}else{
					//????????????????????????????????????
					if ("collection=browse_shop".equals(pinJievalue)){
						return ComModel2.getForceLook(context, pageSize, index,is_new,is_hot,order_by_price);
					}else{
						//??????????????????
						return ComModel2.getSignShop(context, index, pinJievalue,is_new,is_hot,order_by_price);
					}

				}










//				return ComModel2.getForceLook(context, pageSize, index);
//				if (SignListAdapter.doType == 4) {// ???????????????
//					if ("collection=browse_shop".equals(SignListAdapter.doValue.split(",")[0])) {// ????????????
//						// ????????????
//						return ComModel2.getForceLook(context, pageSize, index);
//					} else if (mapItem != null) {
//						return ComModel2.getProductListBySearch(context, params[0], idSearch, "2", pageSize, is_new,
//								order_by_price, notType, oldId);
//					} else {
//						return ComModel2.getFilterProductList2(title, id, context, params[0], map, true + "", pageSize,
//								is_new, order_by_price, false);
//					}
//				} else {// ??????????????????
//					return ComModel2.getProductList1(context, index, id + "", "1", title + "", pageSize, false);
//				}
			}

			@Override
			protected boolean isHandleException() {

				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, List<HashMap<String, Object>> result, Exception e) {
				// TODO Auto-generated method stub
				super.onPostExecute(context, result, e);

				if (e == null) {
					if (mType == 1) {
						if (result == null || result.size() == 0) {
							llNodata.setVisibility(View.VISIBLE);
							r_list_view.setVisibility(View.GONE);
						} else {
							llNodata.setVisibility(View.GONE);
							r_list_view.setVisibility(View.VISIBLE);
						}
						if (null != vipDikouData && vipDikouData.getIsVip() > 0) {
							mAdapter.setDataVip(result, true, vipDikouData);

						} else {
//                            mAdapter.setData(result);
							mAdapter.setDataVip(result, false, vipDikouData);


						}
					} else if (mType == 2) {
						if (result == null || result.size() == 0) {
							ToastUtil.showShortText(context, "???????????????????????????~");
						}else{
							mAdapter.addItemLast(result);
							r_list_view.getRefreshableView().smoothScrollBy(100, 20);
						}
					}
				}
				r_list_view.onRefreshComplete();
			}

		}.execute(index);
	}

	private String singvalue;

	// // ?????????????????????
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if(SignListAdapter.doType==4){
	// countTaskBack();
	// }else if(SignListAdapter.doType==5){
	// timeTaskBack();
	// }else{
	// onBackPressed();
	// }
	// return true;
	//
	// }

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.img_back: // ??????
			onBackPressed();
			break;
		case R.id.img_btn_filter:

			Intent intent = new Intent(this, ShopCartNewNewActivity.class);
			intent.putExtra("where", "0");
			startActivity(intent);
			break;

		case R.id.tv_shuoming:
			if(SignListAdapter.doType==19){
				XunBaoDialog dialog = new XunBaoDialog(context);
				dialog.show();
				dialog.setContentText("?????????"+singvalue+"??????????????????"+SignListAdapter.jiangliValue+"??????????????????????????????????????????????????????????????????~");
			}else {
				new XunBaoDialog(context).show();
			}
			break;
			
		case R.id.create_is_new:// ??????
			tvNew.setTextColor(Color.parseColor("#FF3F8B"));
			tvHot.setTextColor(Color.parseColor("#3E3E3E"));
			tvDesc.setTextColor(Color.parseColor("#3E3E3E"));
			tvAsc.setTextColor(Color.parseColor("#3E3E3E"));
			is_new = "is_new";
			is_hot = null;
			order_by_price = null;
			mType = 1;
			index = 1;
			initData(index + "", is_new, order_by_price);
			break;
		case R.id.create_is_hot:// ??????
			tvHot.setTextColor(Color.parseColor("#FF3F8B"));
			tvNew.setTextColor(Color.parseColor("#3E3E3E"));
			tvDesc.setTextColor(Color.parseColor("#3E3E3E"));
			tvAsc.setTextColor(Color.parseColor("#3E3E3E"));
			is_hot = "is_hot";
			is_new = null;
			order_by_price = null;
			mType = 1;
			index = 1;
			initData(index + "",  is_new, order_by_price);
			break;
		case R.id.create_price_asc:// ??????
			tvAsc.setTextColor(Color.parseColor("#FF3F8B"));
			tvNew.setTextColor(Color.parseColor("#3E3E3E"));
			tvHot.setTextColor(Color.parseColor("#3E3E3E"));
			tvDesc.setTextColor(Color.parseColor("#3E3E3E"));
			is_new = null;
			is_hot = null;
			order_by_price = "asc";
			mType = 1;
			index = 1;
			initData(index + "",  is_new, order_by_price);
			break;
		case R.id.create_price_desc:// ??????
			tvDesc.setTextColor(Color.parseColor("#FF3F8B"));
			tvNew.setTextColor(Color.parseColor("#3E3E3E"));
			tvHot.setTextColor(Color.parseColor("#3E3E3E"));
			tvAsc.setTextColor(Color.parseColor("#3E3E3E"));
			is_new = null;
			is_hot = null;
			order_by_price = "desc";
			mType = 1;
			index = 1;
			initData(index + "",  is_new, order_by_price);
			break;
		default:
			break;
		}
	}

	// private void explain() {
	// // TODO Auto-generated method stub
	// SignExplain signExplain = new SignExplain(this, id_new);
	// signExplain.show();
	// SharedPreferencesUtil.saveBooleanData(context, "isfirst", false);
	// }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private boolean isLou = true;

	private TextView tv_shuoming;

	public static int random;

	private int id_new;

	private int nextID;

	private TextView haisheng;
	private SimpleDateFormat formatter;
	private int type_id;
	private int type_id_value;
	private String come_time;
	private String come_time_in;

	/** ??????????????????????????????popupwindow */
	private class OrderByPopupWindow extends PopupWindow implements OnClickListener {
		private Context mContext;
		private LinearLayout create_is_new, create_price_desc, create_price_asc;
		private String[] names;

		private TextView is_lou;

		public OrderByPopupWindow(Context context) {
			super(context);
			mContext = context;
			init();
		}

		private void init() {
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			ColorDrawable dw = new ColorDrawable(0x00);
			setBackgroundDrawable(dw);
			View view = LayoutInflater.from(mContext).inflate(R.layout.popup_order, null);
			create_is_new = (LinearLayout) view.findViewById(R.id.create_is_new);
			create_price_desc = (LinearLayout) view.findViewById(R.id.create_price_desc);
			create_price_asc = (LinearLayout) view.findViewById(R.id.create_price_asc);

			create_is_new.setOnClickListener(this);
			create_price_desc.setOnClickListener(this);
			create_price_asc.setOnClickListener(this);
			setContentView(view);
			setAnimationStyle(R.style.PopupWindowAnimation);
			setOutsideTouchable(true);
			setFocusable(true);
		}

		@Override
		public void onClick(View v) {
			// Intent intent = new Intent();
			// int enterAnimID = R.anim.slide_right_in;
			// int exitAnimID = R.anim.slide_left_out;
			int id = v.getId();
//			if (id == R.id.create_is_new) {
//				is_new = "is_new";
//				order_by_price = null;
//			} else if (id == R.id.create_price_desc) {
//				is_new = null;
//				order_by_price = "desc";
//			} else if (id == R.id.create_price_asc) {
//				is_new = null;
//				order_by_price = "asc";
//			}
			notType = true;
			mType = 1;
			index = 1;
			// initData(index + "", SearchResultActivity.this.id,
			// level,is_new,order_by_price, notType);
			initData(index + "",  is_new, order_by_price);
			// enterAnimID = R.anim.slide_down_in;
			// exitAnimID = R.anim.slide_top_in;
			dismiss();
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 101 && arg1 == 102) {
			CommonUtils.finishActivity(MainMenuActivity.instances);

			Intent intent = new Intent(this, MainMenuActivity.class);
			intent.putExtra("index", 3);
			startActivity(intent);
			finish();
		}
	}

	// private long recLen = (1 * 1000 * 60) / 2; // 30??? ???????????? ????????????
	// Timer timer = new Timer();

	// ?????????
	class MyTimerTask extends TimerTask {

		@Override
		public void run() {

			((Activity) context).runOnUiThread(new Runnable() { // UI thread
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
							liulanTime.setText("" + days + "???" + hours + ":" + minutes + ":" + seconds);
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
							liulanTime.setText("" + hours + ":" + minutes + ":" + seconds);
						}
					} else if (minute >= 10 && second >= 10) {
						liulanTime.setText("" + minute + ":" + second);
					} else if (minute >= 10 && second < 10) {
						liulanTime.setText("" + minute + ":0" + second);
					} else if (minute < 10 && second >= 10) {
						liulanTime.setText("0" + minute + ":" + second);
					} else {
						liulanTime.setText("0" + minute + ":0" + second);
					}
					// tv_time.setText("" + recLen);
					if (recLen <= 0) {
						timer.cancel();

					}

					String time = (String) liulanTime.getText();

					if (recLen <= 0) { // ????????????????????????????????????????????????
						isTimeout = true;
					}

				}
			});
		}

	}

	private String tongJiTime() {
		Date leave_time = new Date(System.currentTimeMillis());// ??????????????????
		String str_leave_time = formatter.format(leave_time);
		// System.out.println("????????????="+str_leave_time);
		// TODO ????????? ??????????????????????????????
		return str_leave_time;
	}

	private void yunYunTongJi(final String shop_code, final int type, final int tab_type) {
		new SAsyncTask<Void, Void, HashMap<String, String>>((FragmentActivity) this, R.string.wait) {

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
					throws Exception {

				return ComModel2.getOperator(context, shop_code, type, tab_type);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
				// TODO Auto-generated method stub
				super.onPostExecute(context, result, e);
				// System.out.println("???????????????????????????");
			}

		}.execute();
	}

	private void countTaskBack() {
		int forcelookNum=0;
		int forcelookLimitNum=0;
		SimpleDateFormat dfNum = new SimpleDateFormat("yyyy-MM-dd");
		if (SignListAdapter.doType ==4) {
			String nowTimeForcelook = SharedPreferencesUtil.getStringData(ForceLookActivity.this,
                    "forcelookNowTime" + YCache.getCacheUser(context).getUser_id(), "");
			forcelookNum = Integer.parseInt(SharedPreferencesUtil.getStringData(ForceLookActivity.this,
                    SignListAdapter.signIndex + "forcelookNum" + YCache.getCacheUser(context).getUser_id(), "0"));
			if (!dfNum.format(new Date()).equals(nowTimeForcelook)) {
                forcelookNum = 0;// ????????????????????????????????? ??????????????????????????? ??? ??????????????????????????????????????? ??????????????????
            }
		} else if (SignListAdapter.doType ==19){
			String nowTimeForcelookLimit = SharedPreferencesUtil.getStringData(
					ForceLookActivity.this,
					"nowTimeForcelookLimit" + YCache.getCacheUser(context).getUser_id(), "");
			forcelookLimitNum = Integer
					.parseInt(
							SharedPreferencesUtil
									.getStringData(ForceLookActivity.this,
											SignListAdapter.signIndex + Pref.ISFORCELOOKLIMITNUM
													+ YCache.getCacheUser(context).getUser_id(),
											"0"));
			if (!dfNum.format(new Date()).equals(nowTimeForcelookLimit)) {
				forcelookLimitNum = 0;// ?????????????????????????????????
				// ??????????????????????????? ???
				// ???????????????????????????????????????
				// ?????????????????????????????????
			}
		}
		if (/* isTimeout || */ (SignListAdapter.doType ==4 && forcelookNum >= Integer.parseInt(singvalue))
				||(SignListAdapter.doType ==19 && forcelookLimitNum/Integer.parseInt(singvalue) +1 > SignListAdapter.doNum)){
			// Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
			long time = System.currentTimeMillis();
			String leave_time = DateFormatUtils.format(Long.parseLong(time + ""), "yyyy.MM.dd HH:mm:ss");

			DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			try {
				Date d1 = df.parse(leave_time + "");
				Date d2 = df.parse(come_time_in);
				long diff = d1.getTime() - d2.getTime();// ????????????????????????????????????
				long days = diff / (1000 * 60 * 60 * 24);
				long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
				long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

				long time_stay = diff / 1000; // ????????????

				StringBuffer sb = new StringBuffer();
				sb.append(time_stay + ",");
				sb.append(num_tongji);
				String string = sb.toString();

				yunYunTongJi(string + "", 400, 5);

			} catch (Exception e) {

			}

			////
			finish();
			overridePendingTransition(R.anim.slide_match, R.anim.slide_left_out);
		} else {
			// Toast.makeText(context, "???????????????????????????????????????",
			// Toast.LENGTH_SHORT).show();
			final LeaveDialog leaveDialog = new LeaveDialog(this);
			leaveDialog.show();

			View btn_left = leaveDialog.findViewById(R.id.btn_left);
			btn_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// ?????????????????? ????????????????????????????????????

					////

					long time = System.currentTimeMillis();
					String leave_time = DateFormatUtils.format(Long.parseLong(time + ""), "yyyy.MM.dd HH:mm:ss");

					DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
					try {
						Date d1 = df.parse(leave_time + "");
						Date d2 = df.parse(come_time_in);
						long diff = d1.getTime() - d2.getTime();// ????????????????????????????????????
						long days = diff / (1000 * 60 * 60 * 24);
						long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
						long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

						long time_stay = diff / 1000; // ????????????

						StringBuffer sb = new StringBuffer();
						sb.append(time_stay + ",");
						sb.append(num_tongji);
						String string = sb.toString();

						yunYunTongJi(string + "", 400, 5);

					} catch (Exception e) {

					}

					////
					if(leaveDialog!=null){
						leaveDialog.dismiss();
					}
					finish();
					overridePendingTransition(R.anim.slide_match, R.anim.slide_left_out);
				}
			});

		}
	}

	/**
	 * ??????????????????????????????????????????
	 */
	private void timeTaskBack() {
		if (SignListAdapter.isForceLookTimeOut) {// ?????????????????????
			finish();
			overridePendingTransition(R.anim.slide_match, R.anim.slide_left_out);
		} else {
			final LeaveDialog leaveDialog = new LeaveDialog(this);
			leaveDialog.show();
			leaveDialog.setContentText("???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????~");
			leaveDialog.setButtonText("???????????????", "????????????");
			View btn_left = leaveDialog.findViewById(R.id.btn_left);
			btn_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(leaveDialog!=null){
						leaveDialog.dismiss();
					}
					finish();
					overridePendingTransition(R.anim.slide_match, R.anim.slide_left_out);
				}
			});
			View btn_right = leaveDialog.findViewById(R.id.btn_right);
			btn_right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent((Activity) context, MainMenuActivity.class);
					intent.putExtra("toYf", "toYf");
					context.startActivity(intent);
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		if(isMoreShop){
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_match, R.anim.slide_left_out);
			return;
		}
		if ((SignListAdapter.doType == 4||SignListAdapter.doType == 19)&& !SignListAdapter.isSignComplete) {
			countTaskBack();
		} else if (SignListAdapter.doType == 5 && !SignListAdapter.isSignComplete) {
			timeTaskBack();
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_match, R.anim.slide_left_out);
		}
	}



	private int width;
	private  View  headView;
	private ImageView imgBanner;
	private YDBHelper dbHelp;
	private List<HashMap<String,String>> listData;
	private void  addHeadeViewBanner(){
		if(isMoreShop){
			return;
		}
		r_list_view.getRefreshableView().addHeaderView(headView);
		imgBanner = (ImageView) headView.findViewById(R.id.img_head);
		imgBanner.setVisibility(View.GONE);
		if(SignListAdapter.doType==6||SignListAdapter.doType==24){
			return;
		}
		dbHelp = new YDBHelper(context);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		ViewGroup.LayoutParams lp = imgBanner.getLayoutParams();
		lp.width =width;
		lp.height =lp.width*200/340;
		imgBanner.setLayoutParams(lp);
//		imgBanner.setImageResource(R.drawable.bg_group_shop);
		listData= new ArrayList<HashMap<String,String>>();
		String sql = "select * from shop_group_list where _id = "+doIconId;
		listData =  dbHelp.query(sql);
		if(listData!=null&&listData.size()>0){
			String banner = listData.get(0).get("banner");
			if("null".equals(banner)||TextUtils.isEmpty(banner)) {
				imgBanner.setVisibility(View.GONE);
			}else{
				PicassoUtils.initImage(this,banner+"!560",imgBanner);
				imgBanner.setVisibility(View.VISIBLE);
			}
		}

	}

}
