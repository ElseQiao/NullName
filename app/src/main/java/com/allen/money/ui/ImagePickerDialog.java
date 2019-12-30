package com.allen.money.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.allen.money.R;

import java.util.List;

/**
 * @author Else
 * @description:
 * @date :2019/9/2 17:01
 */
public class ImagePickerDialog extends Dialog {
    private RecyclerView recyclerView;
    private List<String> paths;
    public ImagePickerDialog(Context context,List<String> imagePaths) {
        super(context);
        paths=imagePaths;
    }

    public ImagePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ImagePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_image_picker_layout);
        recyclerView=findViewById(R.id.image_picker_recycle);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        ImagePickerAdapter imagePickerAdapter=new ImagePickerAdapter(getContext(),paths);
        recyclerView.setAdapter(imagePickerAdapter);
    }
}
