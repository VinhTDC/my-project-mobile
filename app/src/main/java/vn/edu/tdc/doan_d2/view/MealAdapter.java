package vn.edu.tdc.doan_d2.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.FragmentMealItemBinding;
import vn.edu.tdc.doan_d2.fragment.OnMealClickListener;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.meal.Meal;

interface DataProviderMeal {
    BaseCategory getItem(int position);
}

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> implements DataProviderMeal {
    private Context context;
    private ArrayList<BaseCategory> data;

    private LayoutInflater inflater;
    private OnMealClickListener onMealClickListener;

    public MealAdapter(Context context, ArrayList<BaseCategory> data, OnMealClickListener listener) {
        this.context = context;
        this.data = data;
        this.onMealClickListener = listener;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentMealItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_meal_item, parent, false);
        return new MyViewHolder(binding, this, onMealClickListener);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentMealItemBinding fragmentMealItemBinding;
        private final DataProviderMeal dataProvider;
        private final OnMealClickListener listener;
        private WeakReference<ImageView> imageViewWeakReference;

        public MyViewHolder(FragmentMealItemBinding fragmentMealItemBinding, DataProviderMeal dataProvider, OnMealClickListener listener) {
            super(fragmentMealItemBinding.getRoot());
            this.fragmentMealItemBinding = fragmentMealItemBinding;
            this.imageViewWeakReference = new WeakReference<>(fragmentMealItemBinding.imageMeal);
            this.dataProvider = dataProvider;
            this.listener = listener;

            fragmentMealItemBinding.getRoot().setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                BaseCategory item = dataProvider.getItem(position);

                if (position != RecyclerView.NO_POSITION && listener != null && item != null) {
                    listener.onMealClick(item);
                    Log.d("Log.d", item.getId() + "" + position);
                }
            });

        }


        private void bindMeal(Meal meal) {
            String imageUrl = meal.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadImageFromFirebase(imageUrl);
            // Đặt tên danh mục
            fragmentMealItemBinding.nameMeal.setText(meal.getName());
        }

        public void bind(BaseCategory baseCategory) {
            if (baseCategory instanceof Meal) {
                bindMeal((Meal) baseCategory);
            }
        }

        private void loadImageFromFirebase(String imageUrl) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageUrl != null && !imageUrl.isEmpty() && imageView != null && imageView.getContext() instanceof Activity && !((Activity) imageView.getContext()).isDestroyed()) {
                Glide.with(imageView.getContext())
                        .asGif() // Thiết lập tải dưới dạng GIF
                        .load(R.drawable.loadding1) // Đặt tên file loading.gif
                        .into(fragmentMealItemBinding.imageMeal);
                // Tiếp tục xử lý chỉ khi imageUrl không null và không rỗng
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("imgmeal/"+imageUrl);

                // Kiểm tra xem tệp tồn tại trong Firebase Storage
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    if (imageView != null && imageView.getContext() instanceof Activity && !((Activity) imageView.getContext()).isDestroyed()) {
                        Glide.with(imageView.getContext())
                                .load(uri)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                        if (imageView.getContext() instanceof Activity && ((Activity) imageView.getContext()).isDestroyed()) {
                                            Glide.with(imageView.getContext()).clear(imageView);
                                            return true;
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, com.bumptech.glide.request.target.Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                        if (imageView.getContext() instanceof Activity && ((Activity) imageView.getContext()).isDestroyed()) {
                                            // Nếu Activity đã bị hủy, hủy yêu cầu Glide
                                            Glide.with(imageView.getContext()).clear(imageView);
                                            return true; // Đã xử lý yêu cầu
                                        }
                                        return false; // Cho phép Glide hiển thị ảnh
                                    }

                                })
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof StorageException) {
                            StorageException storageException = (StorageException) exception;
                            int errorCode = storageException.getErrorCode();
                            String errorMessage = storageException.getMessage();

                            Glide.with(fragmentMealItemBinding.imageMeal.getContext())
                                    .load(R.drawable.img)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(fragmentMealItemBinding.imageMeal);
                            Glide.with(fragmentMealItemBinding.imageMeal.getContext()).resumeRequests();
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
                Glide.with(fragmentMealItemBinding.imageMeal.getContext())
                        .load(R.drawable.img)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(fragmentMealItemBinding.imageMeal);
                Glide.with(fragmentMealItemBinding.imageMeal.getContext()).resumeRequests();
            }

        }


    }

    public void setData(ArrayList<BaseCategory> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(getData(), newData));
        data.clear();
        data.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.itemView.getContext()).clear(holder.fragmentMealItemBinding.imageMeal);
    }

    public ArrayList<BaseCategory> getData() {
        return data;
    }
}
