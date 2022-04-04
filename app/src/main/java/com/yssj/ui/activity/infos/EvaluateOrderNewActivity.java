package com.yssj.ui.activity.infos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.LoadingDialog;
import com.yssj.entity.EvaluateParam;
import com.yssj.entity.Order;
import com.yssj.entity.OrderShop;
import com.yssj.entity.ReturnInfo;
import com.yssj.model.ComModel2;
import com.yssj.ui.base.BasePager;
import com.yssj.ui.base.BasicActivity2;
import com.yssj.ui.fragment.orderinfo.EvaluateOrderFragment;
import com.yssj.ui.fragment.orderinfo.OrderDetailsActivity;
import com.yssj.ui.pager.EvaluateOrderPage;
import com.yssj.upyun.UpYunException;
import com.yssj.upyun.UpYunUtils;
import com.yssj.upyun.Uploader;
import com.yssj.utils.ToastUtil;

/****
 * 评价界面
 * 
 * @author Administrator
 * 
 */
public class EvaluateOrderNewActivity extends BasicActivity2 {

	private LinearLayout img_back,root,ll_st;
	private TextView tvTitle_base;
	
	private ViewPager content_pager;
	private com.yssj.custom.view.StickyScrollView sc_cc;
	private int currIndex = 0;// 当前页编号

	// private EditText et_content; // 评论内容

	// private List<Bitmap> listBitmap = new ArrayList<Bitmap>();// 上传的图片集合
	private List<String> listPicPath = new ArrayList<String>();// 上传的图片地址集合

	private List<String> listPicPathTemp = new ArrayList<String>(); // 临时存储图片路径

	private List<Boolean> isUploads = new ArrayList<Boolean>();// 是否上传的标识集合
	private List<String> listUploadedUrl = new ArrayList<String>();// 上传图片之后返回的URL

	private String content;

	private Order order;

	private List<BasePager> pageLists;
	private List<EvaluateOrderFragment> fList = new ArrayList<EvaluateOrderFragment>();
	private List<OrderShop> list = new ArrayList<OrderShop>();

	private List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();

	private List<EvaluateParam> listParam = new ArrayList<EvaluateParam>();
	private String jsonString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		order = (Order) getIntent().getSerializableExtra("order");
		if (order != null) {
			for(int i=0; i<order.getList().size(); i++){
				OrderShop os = order.getList().get(i);
				
				if(os.getChange() == 1){
					if(os.getStatus() == 1){//换货处理中
						continue;
					}
				}
				if(os.getChange() == 2){
					if(os.getStatus() == 1 || os.getStatus() == 3){//退货处理中，已成功
						continue;
					}
				}
				if(os.getChange() == 3){
					if(os.getStatus() == 1 || os.getStatus() == 3){//退款处理中，已成功
						continue;
					}
				}
				list.add(os);
			}
		}

		initView();
		initViewPager();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initView() {
		setContentView(R.layout.evaluate_order_new);
		root= (LinearLayout) findViewById(R.id.root);
		root.setBackgroundColor(Color.WHITE);
		ll_st = (LinearLayout) findViewById(R.id.ll_st);
		ll_st.setBackgroundColor(Color.WHITE);
		img_back = (LinearLayout) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		tvTitle_base = (TextView) findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("评价订单");
		sc_cc = (com.yssj.custom.view.StickyScrollView) findViewById(R.id.sc_cc);
		sc_cc.setBackgroundColor(Color.WHITE);
		content_pager = (ViewPager) findViewById(R.id.content_pager);

		findViewById(R.id.submit).setOnClickListener(this);

	}

	/** 初始化ViewPager */
	private void initViewPager() {
		pageLists = new ArrayList<BasePager>();
		for (int i = 0; i < list.size(); i++) {

			EvaluateOrderFragment f = new EvaluateOrderFragment();
			fList.add(f);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fl_content, f).commit();
			pageLists.add(new EvaluateOrderPage(this, list.get(i)));

		}

		pageLists.get(0).initData(); // 第一次进来时加载数据
		for (int i = 0; i < fList.size(); i++) {// 第一次进来显示第0个 Fragment 其他
			if (i == 0)
				getSupportFragmentManager().beginTransaction()
						.show(fList.get(i)).commit();
			else
				getSupportFragmentManager().beginTransaction()
						.hide(fList.get(i)).commit();
		}

		content_pager.setAdapter(new MyPagerAdapter(pageLists));

		content_pager.setCurrentItem(currIndex);
		content_pager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	/* 页卡切换监听 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			pageLists.get(arg0).initData();

			for (int i = 0; i < fList.size(); i++) {
				if (i == arg0)
					getSupportFragmentManager().beginTransaction()
							.show(fList.get(i)).commit();
				else
					getSupportFragmentManager().beginTransaction()
							.hide(fList.get(i)).commit();
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

	}

	class MyPagerAdapter extends PagerAdapter {
		private List<BasePager> pageLists;

		public MyPagerAdapter(List<BasePager> pageLists) {
			this.pageLists = pageLists;
		}

		@Override
		public int getCount() {
			return pageLists.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pageLists.get(position).getRootView());
			return pageLists.get(position).getRootView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_back: // 返回
			finish();
			break;

		case R.id.submit: // 发表评论

			submit(v);
			break;

		default:
			break;
		}
	}

	private void getData() {
		for (int i = 0; i < fList.size(); i++) {
			HashMap<String, Object> map = fList.get(i).getData();
			mapList.add(map);
		}
	}

	private void commentRequest(View v) {
		new SAsyncTask<Void, Void, ReturnInfo>(this, v, R.string.wait) {
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				LoadingDialog.show(EvaluateOrderNewActivity.this);
			}
			@Override
			protected ReturnInfo doInBackground(FragmentActivity context,
					Void... params) throws Exception {
				// method stub
				/*
				 * StringBuffer sbPic = new StringBuffer(); for (int i = 0; i <
				 * listUploadedUrl.size(); i++) {
				 * sbPic.append(listUploadedUrl.get(i).split("/")[2]); if (i !=
				 * listUploadedUrl.size() - 1) { sbPic.append(","); } }
				 */

				return ComModel2.addCommentList(context, order.getOrder_code(),
						jsonString);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context,
					ReturnInfo result, Exception e) {
				super.onPostExecute(context, result, e);
				LoadingDialog.hide(EvaluateOrderNewActivity.this);
				if (null == e) {
					if (result != null && "1".equals(result.getStatus())) {
						ToastUtil.showShortText(context, result.getMessage());
						/*Intent intent = new Intent(
								EvaluateOrderNewActivity.this,
								StatusInfoActivity.class);
						intent.putExtra("index", 4);
						startActivity(intent);*/
						if(null != OrderDetailsActivity.instance){
							OrderDetailsActivity.instance.finish();
						}
						finish();

					} else {
						ToastUtil.showShortText(context, result.getMessage());
					}
				}
			}

		}.execute();
	}

	/***
	 * 提交图片信息，提交文字信息
	 */
	private void submit(final View v) {
		mapList.clear();
		listParam.clear();
		listPicPath.clear();

		getData();
		if (mapList.size() != 0) {
			if (mapList.get(0).get("content").equals("")
					|| mapList.get(0).get("content").equals("neirong")
					|| mapList.get(0).get("content") == null) {
				ToastUtil.showLongText(context, "评论内容不能为空");
				return;
			}
			if (pageLists.size() != 1) {
				if (mapList.get(pageLists.size() - 1).get("content").equals("")
						|| mapList.get(pageLists.size() - 1).get("content")
								.equals("neirong")
						|| mapList.get(pageLists.size() - 1).get("content") == null) {
					ToastUtil.showLongText(context, "你还有商品没有评论");
					return;
				}
			}
		} else {
			ToastUtil.showLongText(context, "评论内容不能为空");
			return;
		}
		for (int i = 0; i < mapList.size(); i++) {
			EvaluateParam param = new EvaluateParam();
			param.setColor(mapList.get(i).get("color").toString());

			if ("1".equals(mapList.get(i).get("star").toString())
					|| "2".equals(mapList.get(i).get("star").toString())) {
				param.setComment_type("1");
				if (TextUtils.isEmpty(mapList.get(i).get("content").toString())) {
					ToastUtil.showShortText(context, "总评价小于两颗星，请先填写评价内容！");
					return;
				}
			} else if ("3".equals(mapList.get(i).get("star").toString())
					|| "4".equals(mapList.get(i).get("star").toString())) {
				param.setComment_type("2");
			} else if ("5".equals(mapList.get(i).get("star").toString())) {
				param.setComment_type("3");
			}

			param.setContent(mapList.get(i).get("content").toString());
			param.setCost(mapList.get(i).get("cost").toString());
			param.setId(list.get(i).getId().toString());
			// param.setPic(mapList.get(i).get("listPicPath").toString().replace("[",
			// "").replace("]", ""));
			param.setStar(mapList.get(i).get("star").toString());
			param.setType(mapList.get(i).get("type").toString());
			param.setWork(mapList.get(i).get("work").toString());

			listParam.add(param);

			// 获得所有订单图片一起上传
			listPicPathTemp = (List<String>) mapList.get(i).get("listPicPath");
			for (int j = 0; j < listPicPathTemp.size(); j++) {
				listPicPath.add(listPicPathTemp.get(j));
			}
		}

		jsonString = JSONObject.toJSONString(listParam);

		if (listPicPath == null || listPicPath.isEmpty()
				|| listPicPath.size() == 0) {
			commentRequest(v);
			return;
		}

		new SAsyncTask<Void, Void, Void>(this, v, R.string.wait) {
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				LoadingDialog.show(EvaluateOrderNewActivity.this);
			}

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected Void doInBackground(FragmentActivity context,
					Void... params) throws Exception {
				// String[] strPic = listParam.get(i).getPic().split(",");
				for (int j = 0; j < mapList.size(); j++) {
					final List<String> picList = (List<String>) mapList.get(j).get(
							"listPicPath");
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < picList.size(); i++) {
						try {
							// 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
							String SAVE_KEY = File.separator + "returnShop"
									+ File.separator
									+ System.currentTimeMillis() + ".jpg";

							// 取得base64编码后的policy
							String policy = UpYunUtils.makePolicy(SAVE_KEY,
									Uploader.EXPIRATION, Uploader.BUCKET);

							// 根据表单api签名密钥对policy进行签名
							// 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
							String signature = UpYunUtils.signature(policy
									+ "&" + Uploader.TEST_API_KEY);

							// 上传文件到对应的bucket中去。
							String string = Uploader.upload(policy, signature,
									Uploader.BUCKET, picList.get(i));
							// aa = Integer.valueOf(params[1]);
							sb.append(string);
							if(i != picList.size()){
								sb.append(",");
							}
						} catch (UpYunException e) {
							e.printStackTrace();
						}
					}
					listParam.get(j).setPic(sb.toString());
				}
				return super.doInBackground(context, params);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, Void result,
					Exception e) {
				super.onPostExecute(context, result, e);
				if(e == null){
					jsonString = JSONObject.toJSONString(listParam);
					commentRequest(v);
				}
			}

		}.execute();

		/*
		 * for (int k = 0; k < listPicPath.size(); k++) { boolean isUpload =
		 * false; isUploads.add(isUpload); new SAsyncTask<String, Void,
		 * String>(this, v, R.string.wait) {
		 * 
		 * private int aa = 0;
		 * 
		 * @Override protected String doInBackground(FragmentActivity context,
		 * String... params) throws Exception { String string = null;
		 * 
		 * try { // 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。 String SAVE_KEY
		 * = File.separator + "returnShop" + File.separator +
		 * System.currentTimeMillis() + ".jpg";
		 * 
		 * // 取得base64编码后的policy String policy = UpYunUtils.makePolicy(SAVE_KEY,
		 * Uploader.EXPIRATION, Uploader.BUCKET);
		 * 
		 * // 根据表单api签名密钥对policy进行签名 // 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
		 * String signature = UpYunUtils.signature(policy + "&" +
		 * Uploader.TEST_API_KEY);
		 * 
		 * // 上传文件到对应的bucket中去。 string = Uploader.upload(policy, signature,
		 * Uploader.BUCKET, params[0]); aa = Integer.valueOf(params[1]);
		 * 
		 * } catch (UpYunException e) { e.printStackTrace(); }
		 * 
		 * return string; }
		 * 
		 * @Override protected void onPostExecute(FragmentActivity context,
		 * String result) { super.onPostExecute(context, result); if (result !=
		 * null) { MyLogYiFu.e("result", result); listUploadedUrl.add(result);
		 * isUploads.set(aa, true); boolean isUploaded = false; for (int i = 0;
		 * i < isUploads.size(); i++) { isUploaded = false; if
		 * (isUploads.get(i)) { isUploaded = true; } }
		 * 
		 * if (isUploaded) { commentRequest(v); } }
		 * 
		 * }
		 * 
		 * }.execute(listPicPath.get(k), k + ""); }
		 */
	}

}
