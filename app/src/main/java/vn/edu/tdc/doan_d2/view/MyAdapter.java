package vn.edu.tdc.doan_d2.view;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import vn.edu.tdc.doan_d2.R;

import vn.edu.tdc.doan_d2.databinding.FragmentCategoryItemBinding;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Category> data;
    private static final int IMAGE_LOADING = 1;
    private static final int IMAGE_LOADED = 2;
    private static final int IMAGE_ERROR = 3;


    public MyAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.data = categories;
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

        if (data != null && position >= 0 && position < data.size()) {
            Category category = data.get(position);
            Glide.get(context).clearMemory();
            String capitalizedName = category.getName().substring(0, 1).toUpperCase() + category.getName().substring(1);
            category.setName(capitalizedName);
            holder.bind(category);
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentCategoryItemBinding categoryListItemBinding;

        public MyViewHolder(FragmentCategoryItemBinding categoryListItemBinding) {
            super(categoryListItemBinding.getRoot());

            this.categoryListItemBinding = categoryListItemBinding;
            categoryListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("testClick", "Called");
                }
            });
        }

        public void bind(Category category) {
            // Lấy hình ảnh từ Firebase Storage
            String imageUrl = category.getImgUrl();

            // Kiểm tra xem imageUrl không null và không rỗng
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Log.d("testimg", imageUrl);

                Glide.with(categoryListItemBinding.imageCategory.getContext())
                        .asGif() // Thiết lập tải dưới dạng GIF
                        .load(R.drawable.loadding1) // Đặt tên file loading.gif
                        .into(categoryListItemBinding.imageCategory);
                // Tiếp tục xử lý chỉ khi imageUrl không null và không rỗng
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child(imageUrl);

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

                            // Xử lý dựa trên mã lỗi và thông điệp
                            switch (errorCode) {
                                case StorageException.ERROR_OBJECT_NOT_FOUND:
                                    // Tệp không tồn tại, xử lý tương ứng
                                    Log.e("FirebaseStorage", "File does not exist: " + errorMessage);
                                    break;
                                default:
                                    // Xử lý mặc định hoặc thông báo lỗi
                                    break;
                            }
                        } else {

                            // Xử lý các loại ngoại lệ khác
                            Log.e("FirebaseStorage", "Error: " + exception.getMessage());
                        }
                        Log.e("FirebaseStorage", "File does not exist: " + exception.getMessage());
                    }
                });
            } else {
                // Xử lý trường hợp imageUrl là null hoặc rỗng
                Log.e("testimg", "Image URL is null or empty");
            }

            // Đặt tên danh mục
            categoryListItemBinding.nameCategory.setText(category.getName());
        }
    }

    public void setData(ArrayList<Category> newData) {
        CategoryDiffCallback diffCallback = new CategoryDiffCallback(data, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        data.clear();
        data.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }
    public ArrayList<Category> getData(){
        return data;
    }

}
