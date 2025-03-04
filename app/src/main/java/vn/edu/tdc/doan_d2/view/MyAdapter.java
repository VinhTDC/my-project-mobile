package vn.edu.tdc.doan_d2.view;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import vn.edu.tdc.doan_d2.R;

import vn.edu.tdc.doan_d2.databinding.FragmentCategoryItemBinding;
import vn.edu.tdc.doan_d2.fragment.OnCategoryClickListener;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.model.meal.Meal;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;


interface DataProvider {
    BaseCategory getItem(int position);
}

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements DataProvider {

    private Context context;
    private ArrayList<BaseCategory> data;

    private LayoutInflater inflater;

    private OnCategoryClickListener onCategoryClickListener;

    public MyAdapter(Context context, ArrayList<BaseCategory> data, OnCategoryClickListener listener) {
        this.context = context;
        this.data = data;
        this.onCategoryClickListener = listener;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(false);
    }

    @Override
    public long getItemId(int position) {
        BaseCategory item = data.get(position);
        return item.getId().hashCode(); // Giả sử BaseCategory có thuộc tính id
    }
    @Override
    public BaseCategory getItem(int position) {
        if (position >= 0 && position < data.size()) { // Check for valid position
            return data.get(position);
        } else {
            return null; // Or throw an IllegalArgumentException
        }
    }
    @Override
    public int getItemViewType(int position) {
        BaseCategory item = data.get(position);
        if (item instanceof Category) {
            return 1; // Category
        } else if (item instanceof Cuisine) {
            return 2; // Cuisine
        }else{
            return 0; // Mặc định hoặc loại không xác định
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentCategoryItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_category_item, parent, false);
        return new MyViewHolder(binding, onCategoryClickListener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BaseCategory item = data.get(position);
        if (data != null && !data.isEmpty() && position >= 0 && position < data.size()) {
            if (item instanceof Category) {
                Category baseCategory = (Category) data.get(position);
                Glide.get(context).clearMemory();
                if (baseCategory.getName() != null && !baseCategory.getName().isEmpty()) {
                    String capitalizedName = baseCategory.getName().substring(0, 1).toUpperCase() +
                            baseCategory.getName().substring(1);
                    baseCategory.setName(capitalizedName);
                }
                holder.bind(baseCategory);
            } else if(item instanceof Cuisine) {
                Cuisine baseCategory = (Cuisine) data.get(position);
                Glide.get(context).clearMemory();
                if (baseCategory.getName() != null && !baseCategory.getName().isEmpty()) {
                    String capitalizedName = baseCategory.getName().substring(0, 1).toUpperCase() +
                            baseCategory.getName().substring(1);
                    baseCategory.setName(capitalizedName);
                }
                holder.bind(baseCategory);
            } else {
            }
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentCategoryItemBinding categoryListItemBinding;
        private final DataProvider dataProvider;
        private final OnCategoryClickListener listener;
        private WeakReference<ImageView> imageViewWeakReference;


        public MyViewHolder(FragmentCategoryItemBinding categoryListItemBinding, OnCategoryClickListener listener, DataProvider dataProvider) {
            super(categoryListItemBinding.getRoot());
            this.dataProvider = dataProvider;
            this.listener = listener;
            this.categoryListItemBinding = categoryListItemBinding;
            this.imageViewWeakReference = new WeakReference<>(categoryListItemBinding.imageCategory);


            categoryListItemBinding.getRoot().setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                BaseCategory item = dataProvider.getItem(position);
                if (position != RecyclerView.NO_POSITION && listener != null && item != null) {
                    listener.onCategoryClick(item);
                    Log.d("item" , item.getName());
                }
            });
        }


        public interface OnItemClickListener {
            void onItemClick(BaseCategory category);
        }


        private void bindCategory(Category category) {
            String imageUrl = category.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadImageFromFirebase(imageUrl);
            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(category.getName());
        }

        private void bindCuisine(Cuisine cuisine) {
            String imageUrl = cuisine.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadImageFromFirebase(imageUrl);
            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(cuisine.getName());
        }


        public void bind(BaseCategory baseCategory) {
            if (baseCategory instanceof Category) {
                bindCategory((Category) baseCategory);
            } else if (baseCategory instanceof Cuisine) {
                bindCuisine((Cuisine) baseCategory);
            }
        }

        private void loadImageFromFirebase(String imageUrl) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageUrl != null && !imageUrl.isEmpty() && imageView != null) {

                Glide.with(categoryListItemBinding.imageCategory.getContext())
                        .asGif() // Thiết lập tải dưới dạng GIF
                        .load(R.drawable.loadding1) // Đặt tên file loading.gif
                        .into(categoryListItemBinding.imageCategory);
                // Tiếp tục xử lý chỉ khi imageUrl không null và không rỗng
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("categories/" + imageUrl);

                // Kiểm tra xem tệp tồn tại trong Firebase Storage
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(categoryListItemBinding.imageCategory.getContext())
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(categoryListItemBinding.imageCategory);
                        Glide.with(categoryListItemBinding.imageCategory.getContext()).resumeRequests();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof StorageException) {
                            StorageException storageException = (StorageException) exception;
                            int errorCode = storageException.getErrorCode();
                            String errorMessage = storageException.getMessage();

                            Glide.with(categoryListItemBinding.imageCategory.getContext())
                                    .load(R.drawable.img)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(categoryListItemBinding.imageCategory);
                            Glide.with(categoryListItemBinding.imageCategory.getContext()).resumeRequests();
                            // Xử lý dựa trên mã lỗi và thông điệp
                            switch (errorCode) {
                                case StorageException.ERROR_OBJECT_NOT_FOUND:
                                    // Tệp không tồn tại, xử lý tương ứng
                                    Log.e("FirebaseStorage1", "File does not exist: " + errorMessage);
                                    break;
                                default:
                                    // Xử lý mặc định hoặc thông báo lỗi
                                    break;
                            }
                        } else {

                            // Xử lý các loại ngoại lệ khác
                            Log.e("FirebaseStorage2", "Error: " + exception.getMessage());
                        }
                    }
                });
            } else {
                Glide.with(categoryListItemBinding.imageCategory.getContext())
                        .load(R.drawable.img)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(categoryListItemBinding.imageCategory);
                Glide.with(categoryListItemBinding.imageCategory.getContext()).resumeRequests();
            }
        }
    }
        public void setData(ArrayList<BaseCategory> newData) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(getData(), newData));
            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }

        public ArrayList<BaseCategory> getData() {
            return data;
        }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.itemView.getContext()).clear(holder.categoryListItemBinding.imageCategory);
    }
}


