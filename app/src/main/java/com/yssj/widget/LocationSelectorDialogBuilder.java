package com.yssj.widget;

import com.yssj.activity.R;
import com.yssj.animation.Effectstype;
import com.yssj.utils.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;



public class LocationSelectorDialogBuilder extends NiftyDialogBuilder implements
		android.view.View.OnClickListener {

	private Context context;
	private RelativeLayout rlCustomLayout;
	private AreasWheel areasWheel;
	private OnSaveLocationLister saveLocationLister;
	private static LocationSelectorDialogBuilder instance;
	private static  int mOrientation=1;

	public interface OnSaveLocationLister {
		abstract void onSaveLocation(String location, String provinceId, String cityId);
	}

	public LocationSelectorDialogBuilder(Context context, int theme) {
		super(context, theme);
		this.context = context;
		initDialog();
	}

	public LocationSelectorDialogBuilder(Context context) {
		super(context);
		this.context = context;
		initDialog();
	}

	public static LocationSelectorDialogBuilder getInstance(Context context){

        int ort=context.getResources().getConfiguration().orientation;
        if (mOrientation!=ort){
            mOrientation=ort;
            instance=null;
        }

        if (instance == null||((Activity) context).isFinishing()) {
            synchronized (LocationSelectorDialogBuilder.class) {
                if (instance == null) {
                    instance = new LocationSelectorDialogBuilder(context,R.style.dialog_untran);
                }
            }
        }
        return instance;

    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(R.drawable.edit_dialog_coner);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE 
		| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		Window window = this.getWindow();
		lp.width = (int) (display.getWidth()); // ????????????
		lp.y = display.getHeight();
		window.setAttributes(lp);
		window.setWindowAnimations(R.style.mystyle2); // ????????????
	}
	
	private void initDialog() {
		rlCustomLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.location_selector_dialog_layout, null);
		areasWheel = (AreasWheel) rlCustomLayout
				.findViewById(R.id.aw_location_selector_wheel);
		setDialogProperties();
	}

	private void setDialogProperties() {
		int width = DateUtils.getScreenWidth(context) * 3 / 4;
		this.withDialogWindows(width, LayoutParams.WRAP_CONTENT)
				.withTitleColor("#919191").withTitle("????????????")
				.setDialogClick(this).withEffect(Effectstype.Slit)
				.withPreviousText("??????").withPreviousTextColor("#F43f8c")
				.withDuration(200).setPreviousLayoutClick(this)
				.withNextText("??????").withNextTextColor("#0CB2C5")
				.withMessageMiss(View.GONE).withNextTextColor("#f43f8c")
				.setNextLayoutClick(this)
				.setCustomView(rlCustomLayout, context);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_dialog_title_previous:
			dismiss();
			break;
		case R.id.fl_dialog_title_next:
			if (null != saveLocationLister) {
				saveLocationLister.onSaveLocation(areasWheel.getArea(), areasWheel.getProvinceId(), areasWheel.getCityId());
			}
			dismiss();
			break;
		}
	}
	/**
	 * ???????????????????????????
	 * @param saveLocationLister
	 */
	public void setOnSaveLocationLister(OnSaveLocationLister saveLocationLister) {
		this.saveLocationLister = saveLocationLister;
	}
	@Override
	public void dismiss() {
		super.dismiss();
		instance = null;
	}
}
