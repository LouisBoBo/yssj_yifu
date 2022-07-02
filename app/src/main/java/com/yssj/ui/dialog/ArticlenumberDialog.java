package com.yssj.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yssj.activity.R;
import com.yssj.utils.ToastUtil;

public class ArticlenumberDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int requestCode;
    private String mnumber;

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
        mnumber = number;
        findViewById(R.id.deliver).setOnClickListener(this);
        findViewById(R.id.copy).setOnClickListener(this);
    }


    public void setRequestCode(int code){
        this.requestCode=code;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.copy){
            ToastUtil.showShortText2("复制成功");
            //添加到剪切板
            ClipboardManager clipboardManager =
                    (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            /**之前的应用过期的方法，clipboardManager.setText(copy);*/
            assert clipboardManager != null;
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null,mnumber));
            if (clipboardManager.hasPrimaryClip()){
                clipboardManager.getPrimaryClip().getItemAt(0).getText();
            }
        }

        dismiss();
    }
}
