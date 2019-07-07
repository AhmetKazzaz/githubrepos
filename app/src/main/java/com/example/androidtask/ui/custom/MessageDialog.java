package com.example.androidtask.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.androidtask.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MessageDialog extends Dialog {

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.btnClose)
    Button btnClose;

    @BindView(R.id.content)
    TextView tvContent;

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    private Disposable disposable;


    public MessageDialog(@NonNull Context context) {
        super(context, R.style.Theme_Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_dialog);
        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    @OnClick(R.id.btnClose)
    public void onButtonClicked() {
        dismiss();
    }


}
