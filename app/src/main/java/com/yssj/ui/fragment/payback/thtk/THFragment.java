package com.yssj.ui.fragment.payback.thtk;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yssj.YConstance;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.PubImage;
import com.yssj.custom.view.PubImage.onDeteleImgLintener;
import com.yssj.entity.OrderShop;
import com.yssj.entity.ReturnInfo;
import com.yssj.entity.ReturnShop;
import com.yssj.huanxin.activity.ChatAllHistoryActivity;
import com.yssj.model.ComModel;
import com.yssj.model.ComModel2;
import com.yssj.ui.activity.payback.PayBackDetailsActivity;
import com.yssj.ui.base.BaseFragment;
import com.yssj.ui.fragment.BackHandledFragment;
import com.yssj.ui.fragment.payback.IWantApplyFragment;
import com.yssj.ui.fragment.payback.PayBackChoiceServiceFragment;
import com.yssj.ui.fragment.payback.hh.HHFragment;
import com.yssj.ui.fragment.payback.tk.TKFragment;
import com.yssj.upyun.UpYunException;
import com.yssj.upyun.UpYunUtils;
import com.yssj.upyun.Uploader;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.WXminiAppUtil;

/**
 * ????????????fragment
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class THFragment extends BackHandledFragment implements
		onDeteleImgLintener {

	private boolean hadIntercept;
	private static final int RESULT_OK = -1;
	private TextView tvTitle_base;
	private LinearLayout img_back;
	private ImageView img_right_icon;

	private EditText et_tk_je, et_th_exlpain;
	private Button btn_submit_apply;

	private TextView tv_notice, et_apply_service, et_th_reason;

	// private String order_code; // ????????????
	private String return_type = "";

	private PopupWindow popupWindow;
	private ListView listView;
	private List<String> listService = new ArrayList<String>();
	private List<String> listhh = new ArrayList<String>();
	private MyAdapter mAdapter;

	private ImageView iv_upload_pz;

	private static int RESULT_LOAD_IMAGE = 3;
	private static int RESULT_LOAD_PICTURE = 4;

	private String fileImageName;
	private LinearLayout container;
	private int screenWidth, screenHeight;

	private List<Bitmap> listBitmap = new ArrayList<Bitmap>();// ?????????????????????
	private List<String> listPicPath = new ArrayList<String>();// ???????????????????????????
	private List<Boolean> isUploads = new ArrayList<Boolean>();// ???????????????????????????
	private List<String> listUploadedUrl = new ArrayList<String>();// ???????????????????????????URL
	private double mMealMoney;
	private double mCommonMoney;
	private double money;// ????????????
	// private String order_price;
	private String TAG = "THFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.activity_payback_th, null);
		view.setBackgroundColor(Color.WHITE);
		tvTitle_base = (TextView) view.findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("????????????");
		img_back = (LinearLayout) view.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		img_right_icon = (ImageView) view.findViewById(R.id.img_right_icon);
		img_right_icon.setVisibility(View.VISIBLE);
		img_right_icon.setImageResource(R.drawable.mine_message_center);
		img_right_icon.setOnClickListener(this);

		et_apply_service = (TextView) view.findViewById(R.id.et_apply_service); // ????????????
		et_apply_service.setOnClickListener(this);

		et_th_reason = (TextView) view.findViewById(R.id.et_th_reason); // ????????????
		et_th_reason.setOnClickListener(this);

		et_tk_je = (EditText) view.findViewById(R.id.et_tk_je); // ????????????

		et_th_exlpain = (EditText) view.findViewById(R.id.et_th_exlpain); // ????????????

		tv_notice = (TextView) view.findViewById(R.id.tv_notice);

		btn_submit_apply = (Button) view.findViewById(R.id.btn_submit_apply);
		btn_submit_apply.setOnClickListener(this);

		iv_upload_pz = (ImageView) view.findViewById(R.id.iv_upload_pz);
		iv_upload_pz.setOnClickListener(this);

		container = (LinearLayout) view.findViewById(R.id.container);

		bundle = getArguments();

		isMeal = bundle.getString("isMeal");
		isDuobao = bundle.getString("isDuobao");
		if (null == isMeal) {
			orderShop = (OrderShop) bundle.getSerializable("orderShop");
			getThInfo();
		} else {
			mMealMoney = Double.parseDouble(bundle.getString("order_price"));
//			et_tk_je.setHint("???????????????" + bundle.getString("order_price") + "???");
			et_tk_je.setText(bundle.getString("order_price"));
			tv_notice.setVisibility(View.GONE);
		}
		return view;
	}

	private String isMeal;
	private String isDuobao;
	private OrderShop orderShop;
	private Bundle bundle;

	private void getThInfo() {

		new SAsyncTask<Void, Void, HashMap<String, String>>(getActivity(),
				R.string.wait) {

			@Override
			protected HashMap<String, String> doInBackground(
					FragmentActivity context, Void... params) throws Exception {
				// TODO Auto-generated method stub
				return ComModel2.getThTkInfo(context, orderShop.getId(), "2");
			}

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context,
					HashMap<String, String> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (e == null) {
					mCommonMoney = Double.parseDouble(result.get("money"));
//					et_tk_je.setHint("???????????????" + result.get("money") + "???");
					et_tk_je.setText(result.get("money"));
					double useInegral = Double
							.valueOf(result.get("useInegral"));
					double useCounpon = Double.valueOf(result.get("useCoupon"));
					double money = Double.valueOf(result.get("money"));
					double sum = money + useInegral + useCounpon;
					tv_notice.setText("(??????:"
							+ new DecimalFormat("0.00").format(sum) + ",???????????????:"
							+ new DecimalFormat("0.00").format(useCounpon)
							+ ",????????????:"
							+ new DecimalFormat("0.00").format(useInegral)
							+ ")");
				}
			}

		}.execute();
	}

	@Override
	public void initData() {

		listService.add("????????????");
		// ?????????????????????????????????????????????????????????????????????????????????
		if (!SharedPreferencesUtil.getBooleanData(context, "daishouhuo", false)) {
			listService.add("?????????");
		}
		listService.add("??????");

		listhh.add("????????????/?????????/????????????");
		listhh.add("??????/??????/?????????????????????");
		listhh.add("????????????/??????/??????/??????");
		listhh.add("?????????????????????????????????");
		listhh.add("?????????????????????????????????");
		listhh.add("????????????");
//		listhh.add("????????????????????????");
		listhh.add("??????");

		initListView();
	}

	private void initListView() {
		listView = new ListView(context);

		// listView.setBackgroundResource(R.drawable.payback_hh_selectdown_bg);
		listView.setVerticalScrollBarEnabled(false); // ??????ListView?????????
		listView.setDivider(null);

	}

	private void showSelectServiceDown() {

		mAdapter = new MyAdapter(listService, et_apply_service);
		listView.setAdapter(mAdapter);

		if (popupWindow == null) {
			popupWindow = new PopupWindow(listView,
					et_apply_service.getWidth(), LayoutParams.WRAP_CONTENT);
		}

		// ????????????view??????????????????????????????true
		popupWindow.setFocusable(true);
		// ???????????????????????????????????????????????????
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ??????????????????
		popupWindow.setOutsideTouchable(true);

		popupWindow.showAsDropDown(et_apply_service, 0, 0);
	}

	private void showSelectTHDown() {
		mAdapter = new MyAdapter(listhh, et_th_reason);
		listView.setAdapter(mAdapter);

		if (popupWindow == null) {
			popupWindow = new PopupWindow(listView, et_th_reason.getWidth(),
					LayoutParams.WRAP_CONTENT);
		}

		// ????????????view??????????????????????????????true
		popupWindow.setFocusable(true);
		// ???????????????????????????????????????????????????
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ??????????????????
		popupWindow.setOutsideTouchable(true);

		popupWindow.showAsDropDown(et_th_reason, 0, 0);
	}

	@Override
	public void onClick(View v) {
		/*
		 * bundle.putString("order_price", order_price);
		 * bundle.putString("order_code", order_code);
		 */
		switch (v.getId()) {
		case R.id.img_back:
			/*
			 * mFragment = new PayBackChoiceServiceFragment();
			 * mFragment.setArguments(bundle);
			 * getActivity().getSupportFragmentManager().beginTransaction()
			 * .replace(R.id.fl_content, mFragment).commit();
			 */
			Fragment mFragment = new IWantApplyFragment();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_content, mFragment).addToBackStack(null)
					.commit();
			// getActivity().onBackPressed();
			break;
		case R.id.et_apply_service: // ??????????????????????????????
			showSelectServiceDown();
			break;
		case R.id.et_th_reason: // ??????????????????????????????
			showSelectTHDown();
			break;
		case R.id.iv_upload_pz: // ??????????????????????????????
			if (listPicPath.size() == 3) {
				ToastUtil.showShortText(getActivity(), "??????????????????????????????");
				return;
			}
			doPickPhotoAction();
			break;
		case R.id.btn_submit_apply: // ????????????
			submit(v);
			break;
		case R.id.img_right_icon:// ????????????
			WXminiAppUtil.jumpToWXmini(getActivity());

			break;

		}
	}

	class MyAdapter extends BaseAdapter {

		private List<String> list;
		private TextView et;

		public MyAdapter(List<String> list, TextView et) {
			this.list = list;
			this.et = et;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final View view = View.inflate(context,
					R.layout.payback_hh_selectdown_item, null);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(list.get(position));
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					et.setText(list.get(position));
					popupWindow.dismiss();
					/*
					 * bundle.putString("order_price", order_price);
					 * bundle.putString("order_code", order_code);
					 */
					if ("?????????".equals(list.get(position))) {
						Fragment mFragment = new TKFragment();
						mFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager()
								.beginTransaction()
								.replace(R.id.fl_content, mFragment).commit();

					} else if ("??????".equals(list.get(position))) {
						Fragment mFragment = new HHFragment();
						mFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager()
								.beginTransaction()
								.replace(R.id.fl_content, mFragment).commit();
					}
				}
			});
			return view;
		}

	}

	/**
	 * ????????????
	 */
	private void submitApply() {

		String service = et_apply_service.getText().toString();

		if ("????????????".equals(service)) {
			return_type = "2";
		} else if ("?????????".equals(service)) {
			return_type = "3";
		} else if ("??????".equals(service)) {
			return_type = "1";
		}

		final String cause = et_th_reason.getText().toString();
		String submitMoney = et_tk_je.getText().toString();
		LogYiFu.e(TAG, "submitMoney" + submitMoney);
		if (null == submitMoney || "".equals(submitMoney)
				|| ".".equals(submitMoney)) {
			ToastUtil.showLongText(context, "???????????????????????????");
			return;
		}
		if (isMeal == null) {
			if (Double.parseDouble(submitMoney) > mCommonMoney) {
				ToastUtil.showLongText(context, "????????????????????????????????????");
				return;
			} else {
				money = Double.parseDouble(submitMoney);
			}
		} else {
			if (Double.parseDouble(submitMoney) > mMealMoney) {
				ToastUtil.showLongText(context, "????????????????????????????????????");
				return;
			} else {
				money = Double.parseDouble(submitMoney);
			}
		}

		final String explain = et_th_exlpain.getText().toString();

		if (explain.length() > 200) {
			ToastUtil.showLongText(context, "????????????????????????????????????200???");
			return;
		}

		new SAsyncTask<Void, Void, HashMap<String, Object>>(
				(FragmentActivity) context, R.string.wait) {

			@Override
			protected HashMap<String, Object> doInBackground(
					FragmentActivity context, Void... params) throws Exception {

				StringBuffer sbPic = new StringBuffer();

				for (int i = 0; i < listUploadedUrl.size(); i++) {
					// sbPic.append(listUploadedUrl.get(i).split("/")[2]);
					sbPic.append(listUploadedUrl.get(i));
					if (i != listUploadedUrl.size() - 1) {
						sbPic.append(",");
					}
				}

				String pic_list = sbPic.toString();
				// return null;
				if (null == isMeal) {
					return ComModel.returnShopNewTh(context, explain, pic_list,
							return_type, cause, orderShop.getId() + "", "3",
							money);
				} else {

					if (isDuobao == null) {
						return ComModel.returnMealShopTh(context, explain,
								cause, getArguments().getString("order_code"),
								return_type, pic_list, money);
					} else {
						return ComModel.returnIndanaShop(context, explain,
								cause, getArguments().getString("order_code"),
								return_type,pic_list);
					}
				}
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context,
					HashMap<String, Object> result, Exception e) {
				super.onPostExecute(context, result, e);
				if (null == e) {
					if (result != null && "1".equals(result.get("status"))) {

						// ToastUtil.showLongText(context, "??????????????????");
						// getActivity().finish();
						// Intent intent = new Intent(context,
						// PayBackDetailsActivity.class);
						// intent.putExtra("order_code", order_code);
						// context.startActivity(intent);

						// getItemValue(result.get("order_code"));
						ReturnShop rs = (ReturnShop) result.get("returnShop");
						Intent intent = new Intent(context,
								PayBackDetailsActivity.class);
						intent.putExtra("returnShop", (Serializable) rs);
						context.startActivity(intent);
						getActivity().finish();

						// ToastUtil.showShortText(context,
						// result.getMessage());

					} else {
						ToastUtil.showLongText(context, "??????????????????~~~~");
					}
				}
			}

		}.execute();
	}

	/***
	 * ???????????????????????????????????????
	 */
	private void submit(final View v) {

		if (listPicPath == null || listPicPath.isEmpty()
				|| listPicPath.size() == 0) {
			submitApply();
			return;
		}

		new SAsyncTask<Void, Void, Void>((FragmentActivity) context,
				R.string.wait) {

			@Override
			protected boolean isHandleException() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected Void doInBackground(FragmentActivity context,
					Void... params) throws Exception {
				// TODO Auto-generated method stub
				for (int k = 0; k < listPicPath.size(); k++) {
					listUploadedUrl.add(uploadPic(listPicPath.get(k)));
				}
				return super.doInBackground(context, params);
			}

			@Override
			protected void onPostExecute(FragmentActivity context, Void result,
					Exception e) {
				// TODO Auto-generated method stub
				if (null == e) {
					submitApply();
				}
				super.onPostExecute(context, result, e);
			}

		}.execute();

	}

	private String uploadPic(String picPath) {
		String string = null;

		try {
			// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			String SAVE_KEY = File.separator + "returnShop" + File.separator
					+ System.currentTimeMillis() + ".jpg";

			// ??????base64????????????policy
			String policy = UpYunUtils.makePolicy(SAVE_KEY,
					Uploader.EXPIRATION, Uploader.BUCKET);

			// ????????????api???????????????policy????????????
			// ???????????????????????????????????????????????????????????????????????????http?????????????????????????????????
			String signature = UpYunUtils.signature(policy + "&"
					+ Uploader.TEST_API_KEY);

			// ????????????????????????bucket?????????
			string = Uploader.upload(policy, signature, Uploader.BUCKET,
					picPath);
		} catch (UpYunException e) {
			e.printStackTrace();
		}

		return string;
	}

	/**
	 * ??????????????????
	 */
	private void doPickPhotoAction() {
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light); // R.style.Dialog
		String[] choices;
		choices = new String[2];
		choices[0] = "??????"; // ??????
		choices[1] = "??????????????????"; // ??????????????????
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {// ???????????????SD???
								doTakePhoto();
							} else {
								ToastUtil.showLongText(context, "????????????");
							}
							break;
						}
						case 1:
							doPickPhotoFromGallery();
							break;
						}
					}
				});
		builder.create().show();
	}

	/**
	 * ??????????????????
	 * 
	 */
	protected void doTakePhoto() {
		try {
			File mCurrentPhotoFile = new File(YConstance.saveUploadPicPath);
			if (!mCurrentPhotoFile.exists()) {
				boolean iscreat = mCurrentPhotoFile.mkdirs();// ???????????????????????????
			}

			fileImageName = System.currentTimeMillis() + "";

			mCurrentPhotoFile = new File(mCurrentPhotoFile, fileImageName);
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, RESULT_LOAD_PICTURE);
		} catch (ActivityNotFoundException e) {
			ToastUtil.showLongText(context, "photoPickerNotFoundText");
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	private void addImageView(List<Bitmap> listBitmap) {
		container.removeAllViews();
		for (int i = 0; i < listBitmap.size(); i++) {
			// ImageView imageView = new ImageView(context);
			// imageView.setLayoutParams(new LayoutParams(screenWidth / 3,
			// LayoutParams.WRAP_CONTENT));
			// imageView.setAdjustViewBounds(true);
			// imageView.setImageBitmap(listBitmap.get(i));
			// imageView.setScaleType(ScaleType.FIT_XY);
			// container.addView(imageView);
			PubImage img = new PubImage(context, this);
			img.setTag(i);
			img.setBitmap(listBitmap.get(i));
			container.addView(img);
		}
	}

	@Override
	public void deleteImg(int index) {
		listBitmap.remove(index);
		listPicPath.remove(index);
		addImageView(listBitmap);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == RESULT_LOAD_IMAGE) {
				// BitmapFactory bf = BitmapFactory.decodeFile(pathName)
				// add_qx_pic.set

				final Uri originalUri = data.getData(); // ???????????????uri
				String path;
				if (originalUri.getScheme().equals("content")) {
					path = getRealPathFromURI(originalUri);
				} else {
					path = originalUri.getPath();
				}
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeFile(path, options);
				listBitmap.add(bm);
				listPicPath.add(path);
				addImageView(listBitmap);
				// new_parklot_pic_path = path;
				// add_parklot_pic.setImageBitmap(bm);
				// add_parklot_pic.setScaleType(ScaleType.FIT_XY);
				// newParkLotPicSize = new File(new_parklot_pic_path).length();

			} else if (requestCode == RESULT_LOAD_PICTURE) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				String path = YConstance.saveUploadPicPath + fileImageName;
				Bitmap bm = BitmapFactory.decodeFile(path, options);
				listBitmap.add(bm);
				addImageView(listBitmap);
				listPicPath.add(path);
			}
		}
	}

	private String getRealPathFromURI(Uri originalUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(originalUri, proj,
				null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(cursor
				.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();
		return path;
	}

	protected void doPickPhotoFromGallery() {
		try {
			Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, RESULT_LOAD_IMAGE);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		return intent;
	}

	@Override
	public boolean onBackPressed() {
		if (hadIntercept) {
			return false;
		} else {
			// Toast.makeText(getActivity(), "Click MyFragment",
			// Toast.LENGTH_SHORT).show();
			Fragment mFragment = new IWantApplyFragment();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_content, mFragment).addToBackStack(null)
					.commit();
			hadIntercept = true;
			return true;
		}
	}

	/*
	 * public void getItemValue(final String order_code) { new SAsyncTask<Void,
	 * Void, ReturnShop>((FragmentActivity) context, R.string.wait) {
	 * 
	 * @Override protected ReturnShop doInBackground(FragmentActivity context,
	 * Void... params) throws Exception { return ComModel2.checkPayback(context,
	 * order_code); }
	 * 
	 * @Override protected boolean isHandleException() { return true; }
	 * 
	 * @Override protected void onPostExecute(FragmentActivity context,
	 * ReturnShop result, Exception e) { super.onPostExecute(context, result,
	 * e); if (null == e) { if (result != null) { Intent intent = new
	 * Intent(context, PayBackDetailsActivity.class);
	 * intent.putExtra("returnShop", (Serializable) result);
	 * context.startActivity(intent); getActivity().finish(); } else {
	 * ToastUtil.showShortText(getActivity(), "??????????????????~~~"); } } }
	 * 
	 * }.execute(); }
	 */

}
