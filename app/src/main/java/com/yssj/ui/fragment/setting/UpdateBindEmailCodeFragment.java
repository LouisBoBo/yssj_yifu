package com.yssj.ui.fragment.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.entity.ReturnInfo;
import com.yssj.model.ComModel;
import com.yssj.ui.activity.setting.AccountSecureActivity;
import com.yssj.ui.base.BaseFragment;
import com.yssj.utils.ToastUtil;

public class UpdateBindEmailCodeFragment extends BaseFragment implements OnClickListener {
	
	private TextView tvTitle_base;
	private LinearLayout img_back;
	private Button btn_right;
	private Button btn_get_identify_code;
	
	private EditText et_identify_code;
	private Button btn_next_step;

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.activity_setting_update_email_code, null);
		view.setBackgroundColor(Color.WHITE);
		tvTitle_base = (TextView) view.findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("更换绑定邮箱");
		img_back = (LinearLayout) view.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		btn_right = (Button) view.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText("关闭");
		btn_right.setOnClickListener(this);
		
		btn_get_identify_code = (Button) view.findViewById(R.id.btn_get_identify_code);
		btn_get_identify_code.setOnClickListener(this);
		
		et_identify_code = (EditText) view.findViewById(R.id.et_identify_code);
		
		btn_next_step = (Button) view.findViewById(R.id.btn_next_step);
		btn_next_step.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void initData() {
		
	}
	
	private void nextStep(){
		
		final String code = et_identify_code.getText().toString();
		if(TextUtils.isEmpty(code)){
			ToastUtil.showShortText(context, "请输入验证码");
			return;
		}
		
		new SAsyncTask<Void, Void, ReturnInfo>((FragmentActivity)context, R.string.wait){

			@Override
			protected ReturnInfo doInBackground(FragmentActivity context,
					Void... params) throws Exception {
				
				return ComModel.checkEmailCode(context, code);
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
				if (result != null && "1".equals(result.getStatus())) {
					// 往下一步跳转
					Fragment mFragment = new SuccessBindEmailFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, mFragment).commit();
					
				}else {
					ToastUtil.showLongText(context, result.getMessage());
				}
				}
			}
			
		}.execute();
	}

	@Override
	public void onClick(View v) {
		Fragment mFragment;
		switch (v.getId()) {
		case R.id.img_back:
			mFragment = new UpdateBindEmailFragment();
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, mFragment).commit();
			break;
			
		case R.id.btn_get_identify_code:
			getCode();
			break;
		case R.id.btn_next_step:
			nextStep();
			break;
		case R.id.btn_right:	// 关闭
			startActivity(new Intent(getActivity(), AccountSecureActivity.class));
			getActivity().finish();
			break;
			

		}
	}
	
	
	private void getCode(){
		
		final String email = getArguments().getString("email");
		if(TextUtils.isEmpty(email)){
			ToastUtil.showLongText(context, "手机号不能为空");
		}
		new SAsyncTask<Void, Void, ReturnInfo>((FragmentActivity)context, R.string.wait){

			@Override
			protected ReturnInfo doInBackground(FragmentActivity context,
					Void... params) throws Exception {
				
				return ComModel.sendEmailVerifyCode(context, email,7);
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
				if(result != null && "1".equals(result.getStatus())){
					new CountDownTimer(Long.parseLong(getResources().getString(R.string.identify_code)), 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							btn_get_identify_code.setText(String.valueOf(millisUntilFinished / 1000) + "秒重发");
						}

						@Override
						public void onFinish() {
							btn_get_identify_code.setText("重新发送");
						}
					}.start();
					
				}else{
					btn_get_identify_code.setText("重新发送");
				}
				ToastUtil.showShortText(context, result.getMessage());
			}
			}
		}.execute();
	}	


}
