package com.yssj.ui.fragment.payback.tk;

import java.io.File;
import java.util.ArrayList;
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
import com.yssj.entity.ReturnInfo;
import com.yssj.entity.ReturnShop;
import com.yssj.huanxin.activity.ChatAllHistoryActivity;
import com.yssj.model.ComModel;
import com.yssj.ui.activity.payback.PayBackDetailsActivity;
import com.yssj.ui.base.BaseFragment;
import com.yssj.ui.fragment.payback.hh.HHFragment;
import com.yssj.ui.fragment.payback.thtk.THFragment;
import com.yssj.upyun.UpYunException;
import com.yssj.upyun.UpYunUtils;
import com.yssj.upyun.Uploader;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.WXminiAppUtil;

/**
 * ??????Fragment
 * @author Administrator
 *
 */
@SuppressLint("NewApi") public class UpdateTKFragment extends BaseFragment implements onDeteleImgLintener{
	
	private static final int RESULT_OK = -1;
	private TextView tvTitle_base;
	private LinearLayout img_back;
	private ImageView img_right_icon;
	
	private EditText et_apply_service,et_wl_status,et_tk_exlpain;
	private Button btn_submit_apply;
	
	private String order_code;		// ????????????
	private String return_type = "";
	
	private PopupWindow popupWindow;
	private ListView listView;
	private List<String> listService = new ArrayList<String>();
	private List<String> listTK = new ArrayList<String>();
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
	private String order_price;
	private ReturnShop returnShop = new ReturnShop();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		
		Bundle bundle = getArguments();
		if(bundle != null){
			returnShop = (ReturnShop) bundle.getSerializable("returnShop");
			order_code  = returnShop.getOrder_code();	// ???????????????
			order_price = String.valueOf(returnShop.getMoney());
		}
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.activity_payback_tk, null);
		
		tvTitle_base = (TextView) view.findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("????????????");
		img_back = (LinearLayout) view.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		
		img_right_icon = (ImageView) view.findViewById(R.id.img_right_icon);
		img_right_icon.setVisibility(View.VISIBLE);
		img_right_icon.setImageResource(R.drawable.mine_message_center);
		img_right_icon.setOnClickListener(this);
		img_right_icon.setVisibility(View.GONE);
		
		et_apply_service = (EditText) view.findViewById(R.id.et_apply_service);		// ????????????
		et_apply_service.setOnClickListener(this);
		
		et_wl_status = (EditText) view.findViewById(R.id.et_wl_status);			// ????????????
		et_wl_status.setOnClickListener(this);
		if(returnShop.getOrder_shop_status() == 1){
			et_wl_status.setText("?????????");
		}else if(returnShop.getOrder_shop_status() == 2){
			et_wl_status.setText("?????????");
		}else if(returnShop.getOrder_shop_status() == 3){
			et_wl_status.setText("?????????");
		}
		
		et_tk_exlpain = (EditText) view.findViewById(R.id.et_tk_exlpain);		// ????????????
		et_tk_exlpain.setText(returnShop.getExplain());
		
		btn_submit_apply = (Button) view.findViewById(R.id.btn_submit_apply);
		btn_submit_apply.setOnClickListener(this);
		
		iv_upload_pz = (ImageView) view.findViewById(R.id.iv_upload_pz);
		iv_upload_pz.setOnClickListener(this);
		
		container = (LinearLayout) view.findViewById(R.id.container);
		
		return view;
	}

	@Override
	public void initData() {
	
		
		listService.add("????????????");
		listService.add("?????????");
		listService.add("??????");
		
		
		listTK.add("????????????");
		listTK.add("????????????");
		
		initListView();
	}
	
	private void initListView(){
		listView = new ListView(context);
		
		listView.setBackgroundResource(R.drawable.payback_hh_selectdown_bg);
		listView.setVerticalScrollBarEnabled(false);  // ??????ListView?????????
		listView.setDivider(null);
		
	}
	
	private void showSelectServiceDown(){
		
		mAdapter = new MyAdapter(listService, et_apply_service);
		listView.setAdapter(mAdapter);
		
		if(popupWindow == null){
			popupWindow = new PopupWindow(listView, et_apply_service.getWidth(),215);
		}
		
		// ????????????view??????????????????????????????true
		popupWindow.setFocusable(true);
		// ???????????????????????????????????????????????????
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ??????????????????
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.showAsDropDown(et_apply_service,0,0);
	}
	
	private void showSelectWLDown(){
		mAdapter = new MyAdapter(listTK, et_wl_status);
		listView.setAdapter(mAdapter);
		
		
		if(popupWindow == null){
			popupWindow = new PopupWindow(listView, et_wl_status.getWidth(),274);
		}
		
		// ????????????view??????????????????????????????true
		popupWindow.setFocusable(true);
		// ???????????????????????????????????????????????????
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ??????????????????
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.showAsDropDown(et_wl_status,0,0);
	}
	
	
	@Override
	public void onClick(View v) {
		Fragment mFragment;
		Bundle bundle = new Bundle();
		bundle.putSerializable("returnShop", returnShop);
		switch (v.getId()) {
		case R.id.img_back:
			mFragment = new TKDetailOnlyFragment();
			mFragment.setArguments(bundle);
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, mFragment).commit();
			break;
		case R.id.et_apply_service:		// ??????????????????????????????
			showSelectServiceDown();
			break;
		case R.id.et_wl_status:			// ??????????????????????????????
			showSelectWLDown();
			break;
		case R.id.iv_upload_pz:			// ??????????????????????????????
			doPickPhotoAction();
			break;
		case R.id.btn_submit_apply:		// ????????????
			submit(v);
			break;
		case R.id.img_right_icon:// ????????????
			WXminiAppUtil.jumpToWXmini(getActivity());

			break;
		}
	}
	
	
	class MyAdapter extends BaseAdapter{

		private List<String> list;
		private EditText et;
		public MyAdapter(List<String> list, EditText et){
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final View view = View.inflate(context, R.layout.payback_hh_selectdown_item, null);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(list.get(position));
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					et.setText(list.get(position));
					popupWindow.dismiss();
					
					Bundle bundle = new Bundle();
					bundle.putString("order_price", order_price);
					bundle.putString("order_code", order_code);
					if("??????".equals(list.get(position))){
						Fragment mFragment = new HHFragment();
						mFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, mFragment).commit();
						
					}else if("????????????".equals(list.get(position))){
						Fragment mFragment = new THFragment();
						mFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, mFragment).commit();
					}
				}
			});
			return view;
		}
		
	}
	
	
	/**
	 * ????????????
	 */
	private void submitApply(){
		
		
		String service = et_apply_service.getText().toString();
		
		if("????????????".equals(service)){
			return_type = "2" ;
		}else if("?????????".equals(service)){
			return_type = "3" ;
		}else if("??????".equals(service)){
			return_type = "1" ;
		}
		
		final String cause = et_wl_status.getText().toString();
		final String explain = et_tk_exlpain.getText().toString();
		
		if(explain.length() > 200){
			ToastUtil.showLongText(context, "????????????????????????????????????200???");
			return;
		}
		
		new SAsyncTask<Void, Void, ReturnInfo>((FragmentActivity)context, R.string.wait){

			@Override
			protected ReturnInfo doInBackground(FragmentActivity context,
					Void... params) throws Exception {
				
				StringBuffer sbPic = new StringBuffer();
				
				for (int i = 0; i < listUploadedUrl.size(); i++) {
//					sbPic.append(listUploadedUrl.get(i).split("/")[2]);
					sbPic.append(listUploadedUrl.get(i));
					if(i != listUploadedUrl.size() -1){
						sbPic.append(",");
					}
				}
				
				String pic_list = sbPic.toString();
				
				return ComModel.updateReturnShop(context, explain , pic_list,"", return_type, order_code);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}
			
			@Override
			protected void onPostExecute(FragmentActivity context,
					ReturnInfo result, Exception e) {
				super.onPostExecute(context, result, e);
				if(null == e){
				if (result != null && "1".equals(result.getStatus()) ) {
					
//					ToastUtil.showLongText(context, "??????????????????");
					getActivity().finish();
					
					Intent intent = new Intent(context, PayBackDetailsActivity.class);
					intent.putExtra("order_code", order_code);
					context.startActivity(intent);
					
				}else{
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
		
//		System.out.println("listPicPath:" + listPicPath);

		for (int k = 0; k < listPicPath.size(); k++) {
			boolean isUpload = false;
			isUploads.add(isUpload);
			new SAsyncTask<String, Void, String>((FragmentActivity)context, v) {

				private int aa = 0;

				@Override
				protected String doInBackground(FragmentActivity context,
						String... params) throws Exception {
					String string = null;

					try {
						// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						String SAVE_KEY = File.separator + "returnShop"
								+ File.separator + System.currentTimeMillis()
								+ ".jpg";

						// ??????base64????????????policy
						String policy = UpYunUtils.makePolicy(SAVE_KEY,
								Uploader.EXPIRATION, Uploader.BUCKET);

						// ????????????api???????????????policy????????????
						// ???????????????????????????????????????????????????????????????????????????http?????????????????????????????????
						String signature = UpYunUtils.signature(policy + "&"
								+ Uploader.TEST_API_KEY);

						// ????????????????????????bucket?????????
						string = Uploader.upload(policy, signature, Uploader.BUCKET,
								params[0]);
						aa = Integer.valueOf(params[1]);

					} catch (UpYunException e) {
						e.printStackTrace();
					}

					return string;
				}

				@Override
				protected void onPostExecute(FragmentActivity context,
						String result) {
					super.onPostExecute(context, result);
					if (result != null) {
						LogYiFu.e("result", result);
						listUploadedUrl.add(result);
						isUploads.set(aa, true);
						boolean isUploaded = false;
						for (int i = 0; i < isUploads.size(); i++) {
							isUploaded = false;
							if (isUploads.get(i)) {
								isUploaded = true;
							}
						}

						if (isUploaded) {
							// ??????????????????
							submitApply();
						}
					}

				}

			}.execute(listPicPath.get(k), k + "");
		}
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

			fileImageName = System.currentTimeMillis()+"";

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
//			ImageView imageView = new ImageView(context);
//			imageView.setLayoutParams(new LayoutParams(screenWidth / 3,
//					LayoutParams.WRAP_CONTENT));
//			imageView.setAdjustViewBounds(true);
//			imageView.setImageBitmap(listBitmap.get(i));
//			imageView.setScaleType(ScaleType.FIT_XY);
//			container.addView(imageView);
			PubImage img=new PubImage(context,this);
			img.setTag(i);
			img.setBitmap(listBitmap.get(i));
			container.addView(img);
		}
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
		String[] proj = { MediaStore.Images.Media.DATA};
		Cursor cursor = context.getContentResolver().query(originalUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
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
	public void deleteImg(int index) {
		listBitmap.remove(index);
		listPicPath.remove(index);
		addImageView(listBitmap);
	}

}
