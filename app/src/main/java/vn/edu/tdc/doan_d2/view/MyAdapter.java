package vn.edu.tdc.doan_d2.view;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.R;

import vn.edu.tdc.doan_d2.databinding.FragmentCategoryItemBinding;
import vn.edu.tdc.doan_d2.fragment.OnCategoryClickListener;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.model.meal.Meal;


interface DataProvider {
    BaseCategory getItem(int position);
}

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements DataProvider {

    private Context context;
    private ArrayList<?> data;


    private LayoutInflater inflater;

    private OnCategoryClickListener onCategoryClickListener;
    private List<?> Category;

    public MyAdapter(Context context, ArrayList<?> data, OnCategoryClickListener listener) {
        this.context = context;
        this.data = data;

        this.onCategoryClickListener = listener;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(false);
    }


    @Override
    public long getItemId(int position) {
        Object item = data.get(position);
        if (item instanceof Category) {
            return ((Category) item).getId().hashCode();
        } else if (item instanceof Cuisine) {
            return ((Cuisine) item).getId().hashCode();

        } else if (item instanceof Meal) {
            return ((Meal) item).getId().hashCode();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof Category) {
            return 1; // Category
        } else if (item instanceof Cuisine) {
            return 2; // Cuisine
        } else if (item instanceof Meal) {
            return 3; // Meal
        } else {
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
        Object item = data.get(position);
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
            } else if (item instanceof Cuisine) {
                Cuisine baseCategory = (Cuisine) data.get(position);
                Glide.get(context).clearMemory();
                if (baseCategory.getName() != null && !baseCategory.getName().isEmpty()) {
                    String capitalizedName = baseCategory.getName().substring(0, 1).toUpperCase() +
                            baseCategory.getName().substring(1);
                    baseCategory.setName(capitalizedName);
                }
                holder.bind(baseCategory);
            } else if (item instanceof Meal) {
                Meal baseMeal = (Meal) data.get(position);
                Glide.get(context).clearMemory();
                if (baseMeal.getName() != null && !baseMeal.getName().isEmpty()) {
                    String capitalizedName = baseMeal.getName().substring(0, 1).toUpperCase() +
                            baseMeal.getName().substring(1);
                    baseMeal.setName(capitalizedName);
                }
                holder.bind(baseMeal);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public BaseCategory getItem(int position) {
        if (position >= 0 && position < data.size()) {
            Object item = data.get(position);
            if (item instanceof BaseCategory) {
                return (BaseCategory) item;
            }
        }
        return null;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentCategoryItemBinding categoryListItemBinding;
        private final DataProvider dataProvider;
        private final OnCategoryClickListener listener;


        public MyViewHolder(FragmentCategoryItemBinding categoryListItemBinding, OnCategoryClickListener listener, DataProvider dataProvider) {
            super(categoryListItemBinding.getRoot());
            this.dataProvider = dataProvider;
            this.listener = listener;
            this.categoryListItemBinding = categoryListItemBinding;


            categoryListItemBinding.getRoot().setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                BaseCategory item = dataProvider.getItem(position);
                if (position != RecyclerView.NO_POSITION && listener != null && item != null) {
                    listener.onCategoryClick(item);
                }
            });
        }

        private void bindCategory(Category category) {
            String imageUrl = category.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadDataImgFromFirebase(imageUrl);
            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(category.getName());
        }

        private void bindCuisine(Cuisine cuisine) {
            String imageUrl = cuisine.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadDataImgFromFirebase(imageUrl);
            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(cuisine.getName());
        }

        private void bindMeal(Meal meal) {
            String imageUrl = meal.getImgUrl();
            // Kiểm tra xem imageUrl không null và không rỗng
            loadDataImgFromFirebase(imageUrl);
            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(meal.getName());
        }

        public void bind(Object item) {
            if (item instanceof Category) {
                bindCategory((Category) item);
            } else if (item instanceof Cuisine) {
                bindCuisine((Cuisine) item);
            } else if (item instanceof Meal) {
                bindMeal((Meal) item);
            }
        }

        private void loadDataImgFromFirebase(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {

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


    public void setData(List<?> newData) {

        if (data == null) {
            // Initial setup - no DiffUtil
            data = new ArrayList<>(newData);
            notifyDataSetChanged();
        } else {
            // Enhanced type checking before applying DiffUtil
            if (!newData.isEmpty()) {
                if (newData.get(0) instanceof BaseCategory) {
                    diffCallback = new CategoryDiffCallback((List<BaseCategory>) data, (List<BaseCategory>) newData);
                } else if (newData.get(0) instanceof BaseMeal) {
                    diffCallback = new MealDiffCallback((List<BaseMeal>) data, (List<BaseMeal>) newData);
                } else {
                    // Throw an exception or handle the unsupported data type gracefully
                    throw new IllegalArgumentException("Unsupported data type");
                }
            } else {
                // Handle the case where newData is empty
                Log.e("MyAdapter", "newData is empty. No updates to perform.");
                return; // Exit the method if newData is empty
            }

            // Continue with DiffUtil calculation and update if diffCallback is set
            if (diffCallback != null) {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
                data = new ArrayList<>(newData);
                diffResult.dispatchUpdatesTo(this);
            }
        }
    }

    public ArrayList<?> getData() {
        if (data.get(0) instanceof Category) {
            return data;
        } else if (data.get(0) instanceof Cuisine) {
            return data;
        } else if (data.get(0) instanceof Meal) {
            return data;
        }
        return null;
    }

}

