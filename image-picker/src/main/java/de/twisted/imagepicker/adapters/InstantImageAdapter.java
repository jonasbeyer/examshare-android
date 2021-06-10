package de.twisted.imagepicker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import de.twisted.imagepicker.R;
import de.twisted.imagepicker.interfaces.OnSelectionListener;
import de.twisted.imagepicker.modals.Img;
import de.twisted.imagepicker.utility.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class InstantImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Img> list;
    private OnSelectionListener onSelectionListener;
    private RequestManager glide;
    private RequestOptions options;

    public InstantImageAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();

        glide = Glide.with(context);
        options = new RequestOptions().override(256).transform(new CenterCrop()).transform(new FitCenter());
    }

    public void addOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    public InstantImageAdapter addImage(Img image) {
        list.add(image);
        notifyDataSetChanged();
        return this;
    }

    public InstantImageAdapter addImageFirst(Img image) {
        list.add(0, image);
        notifyDataSetChanged();
        return this;
    }

    public ArrayList<Img> getItemList() {
        return list;
    }

    public void addImageList(ArrayList<Img> images) {
        list.addAll(images);
        notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
    }

    public void removeImage(String path) {
        Iterator<Img> iterator = list.iterator();
        while (iterator.hasNext()) {
            Img img = iterator.next();
            if (img.getUrl().equals(path)) {
                iterator.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void select(boolean selection, int pos) {
        if (pos < 100) {
            list.get(pos).setSelected(selection);
            notifyItemChanged(pos);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MainImageAdapter.HEADER) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.inital_image, parent, false);
            return new HolderNone(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.inital_image, parent, false);
            return new Holder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Img image = list.get(position);
        return (image.getContentUrl().isEmpty()) ? MainImageAdapter.HEADER : MainImageAdapter.ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Img image = list.get(position);
        if (holder instanceof Holder) {
            Holder imageHolder = (Holder) holder;
            int margin = 2;
            float size = Utility.convertDpToPixel(72, context) - 2;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) size, (int) size);
            layoutParams.setMargins(margin, margin, margin, margin);
            imageHolder.itemView.setLayoutParams(layoutParams);
            int padding = (int) (size / 3.5);
            imageHolder.selection.setPadding(padding, padding, padding, padding);
            imageHolder.preview.setLayoutParams(layoutParams);

            File file = new File(image.getUrl());
            if (!file.exists()) {
                list.remove(position);
                return;
            }

            glide.load(image.getContentUrl()).apply(options).into(imageHolder.preview);
            imageHolder.selection.setVisibility(image.getSelected() ? View.VISIBLE : View.GONE);
        } else {
            HolderNone noneHolder = (HolderNone) holder;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0, 0);
            noneHolder.itemView.setLayoutParams(layoutParams);
            noneHolder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView preview;
        ImageView selection;

        Holder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            selection = itemView.findViewById(R.id.selection);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = this.getLayoutPosition();
            onSelectionListener.OnClick(list.get(id), view, id);
        }
    }

    public class HolderNone extends RecyclerView.ViewHolder {
        HolderNone(View itemView) {
            super(itemView);
        }
    }
}
