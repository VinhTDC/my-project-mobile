package vn.edu.tdc.doan_d2.view;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
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
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.meal.Meal;


public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<BaseCategory> data;

    private LayoutInflater inflater;

    public MealAdapter(Context context, ArrayList<BaseCategory> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentCategoryItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_category_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BaseCategory item = data.get(position);
        if (data != null && !data.isEmpty() && position >= 0 && position < data.size()) {
            if (item instanceof Meal) {
                Meal baseCategory = (Meal) data.get(position);
                Glide.get(context).clearMemory();
                if (baseCategory.getName() != null && !baseCategory.getName().isEmpty()) {
                    String capitalizedName = baseCategory.getName().substring(0, 1).toUpperCase() +
                            baseCategory.getName().substring(1);
                    baseCategory.setName(capitalizedName);
                }
                holder.bind(baseCategory);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentCategoryItemBinding categoryListItemBinding;
        private WeakReference<ImageView> imageViewWeakReference;

        public MyViewHolder(FragmentCategoryItemBinding categoryListItemBinding) {
            super(categoryListItemBinding.getRoot());
            this.categoryListItemBinding = categoryListItemBinding;
            this.imageViewWeakReference = new WeakReference<>(categoryListItemBinding.imageCategory);
            categoryListItemBinding.getRoot().setOnClickListener(v -> {

            });
        }


        public interface OnItemClickListener {
            void onItemClick(BaseCategory category);
        }


        private void bindMeal(Meal meal) {
            String imageUrl = meal.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadImageFromFirebase(imageUrl);
            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(meal.getName());
        }

        public void bind(BaseCategory baseCategory) {
            if (baseCategory instanceof Meal) {
                bindMeal((Meal) baseCategory);
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
}
