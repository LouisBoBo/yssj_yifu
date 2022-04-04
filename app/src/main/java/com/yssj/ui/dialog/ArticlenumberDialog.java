package com.yssj.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yssj.activity.R;

public class ArticlenumberDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int requestCode;

    public ArticlenumberDialog.OnItemClick mOnItemClick;
    public interface OnItemClick {
        void click(String address ,String num ,String com);
    }
    public void setOnItemClick(ArticlenumberDialog.OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public ArticlenumberDialog(Context context, String number) {
        super(context, R.style.my_invate_dialog);
        this.context=context;
        setContentView(R.layout.article_number_dialog);
        this.getWindow().setWindowAnimations(R.style.my_dialog_anim_style);
        TextView content = (TextView)findViewById(R.id.tv_content);
        content.setText(number);
        findViewById(R.id.deliver).setOnClickListener(this);
    }


    public void setRequestCode(int code){
        this.requestCode=code;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
