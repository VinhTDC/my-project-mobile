package vn.edu.tdc.doan_d2.view;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.CommentItemBinding;
import vn.edu.tdc.doan_d2.databinding.RecipeMealDetailLayoutBinding;
import vn.edu.tdc.doan_d2.model.comment.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private Context context;
    private List<Comment> data;

    public CommentAdapter(Context context, List<Comment> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.comment_item, parent, false);
        return new CommentAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment item = data.get(position);
        holder.bind(item);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CommentItemBinding commentItemBinding;
        private WeakReference<ImageView> imageViewWeakReference;

        public MyViewHolder(CommentItemBinding commentItemBinding) {
            super(commentItemBinding.getRoot());
            this.commentItemBinding = commentItemBinding;
            this.imageViewWeakReference = new WeakReference<>(commentItemBinding.userAvatarImageView);
        }

        public void bind(Comment comment) {
            commentItemBinding.commentContentTextView.setText(comment.getComment());
            commentItemBinding.userRatingBar.setRating(comment.getRating());
            commentItemBinding.userNameTextView.setText(comment.getUserName());
            loadImageFromFirebase("");
        }
        private void loadImageFromFirebase(String imageUrl) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageUrl != null && !imageUrl.isEmpty() && imageView != null) {

                Glide.with(commentItemBinding.userAvatarImageView.getContext())
                        .asGif() // Thiết lập tải dưới dạng GIF
                        .load(R.drawable.loadding1) // Đặt tên file loading.gif
                        .into(commentItemBinding.userAvatarImageView);
                // Tiếp tục xử lý chỉ khi imageUrl không null và không rỗng
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("categories/" + imageUrl);

                // Kiểm tra xem tệp tồn tại trong Firebase Storage
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(commentItemBinding.userAvatarImageView.getContext())
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(commentItemBinding.userAvatarImageView);
                        Glide.with(commentItemBinding.userAvatarImageView.getContext()).resumeRequests();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof StorageException) {
                            StorageException storageException = (StorageException) exception;
                            int errorCode = storageException.getErrorCode();
                            String errorMessage = storageException.getMessage();

                            Glide.with(commentItemBinding.userAvatarImageView.getContext())
                                    .load(R.drawable.img)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(commentItemBinding.userAvatarImageView);
                            Glide.with(commentItemBinding.userAvatarImageView.getContext()).resumeRequests();
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
                Glide.with(commentItemBinding.userAvatarImageView.getContext())
                        .load(R.drawable.btn_login)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(commentItemBinding.userAvatarImageView);
                Glide.with(commentItemBinding.userAvatarImageView.getContext()).resumeRequests();
            }
        }
    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
