package com.allen.money.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.money.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

/**
 * @author Else
 * @description:
 * @date :2019/9/2 17:15
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ImageViewHolder> {
    private Context mContext;
    private List<String> stringList;
    private HashMap<Integer, String> checkedPosition;

    public ImagePickerAdapter(Context mContext, List<String> stringList) {
        this.mContext = mContext;
        this.stringList = stringList;
        checkedPosition = new HashMap<>();
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.dialog_image_picker_item, null);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        Glide.with(mContext).load(stringList.get(position)).into(holder.imageView);
        if (checkedPosition.containsKey(position)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedPosition.containsKey(position)) {
                    checkedPosition.remove(position);
                    holder.checkBox.setChecked(false);
                    return;
                }
                if (checkedPosition.size() >= 2) {
                    Toast.makeText(mContext, "最多选两张彩超", Toast.LENGTH_LONG).show();
                    return;
                }

                checkedPosition.put(position, stringList.get(position));
                holder.checkBox.setChecked(true);

            }
        });

    }


    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private ImageView imageView;
        private CheckBox checkBox;
        private TextView textView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.image_picker_imageview);
            checkBox = itemView.findViewById(R.id.image_picker_checkBox);
            textView = itemView.findViewById(R.id.image_picker_textView);
        }
    }
}
