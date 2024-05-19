package vn.edu.tdc.doan_d2.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.RecipeMealDetailLayoutBinding;
import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.comment.CommentDiffCallback;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;
import vn.edu.tdc.doan_d2.view.CommentAdapter;
import vn.edu.tdc.doan_d2.viewmodel.mealdetail.MealDetailViewModel;


public class MealDetailGeneralFragment extends Fragment {
    private RecipeMealDetailLayoutBinding binding;
    private String tagFragment = "RECIPE_FRAGMENT_TAG";

    private MutableLiveData<MealDetailData> mealDetailDataMutableLiveData;
    private MealDetailViewModel viewModel;
    private WeakReference<ImageView> imageViewWeakReference;
    private CommentAdapter adapter;
    private MealDetailResponsive mealDetailResponsive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecipeMealDetailLayoutBinding.inflate(inflater, container, false);
        binding.getRoot().setTag(tagFragment);
        viewModel = new ViewModelProvider(requireActivity()).get(MealDetailViewModel.class);
        mealDetailResponsive = new MealDetailResponsive(getActivity().getApplication(), viewModel);
        imageViewWeakReference = new WeakReference<>(binding.imageView);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = binding.commentEditText.getText().toString().trim();
                float rating = binding.ratingBar.getRating();

                if (!commentText.isEmpty()) {
                    // Gửi bình luận và rating lên Firebase
                    viewModel.getIdMeal().observe(getViewLifecycleOwner(), idMeal -> {
                        Comment comment = new Comment(idMeal, commentText, "Vinh", rating);
                        mealDetailResponsive.sendCommentToFirebase(comment, idMeal);
                        adapter.notifyDataSetChanged();
                    });
                    binding.commentEditText.setText(""); // Xóa nội dung EditText

                }
            }
        });
    }

    private void setupData() {
        viewModel.loadMealDetail(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<MealDetailData>() {
            @Override
            public void onChanged(MealDetailData mealDetailData) {
                binding.nameMeal.setText(mealDetailData.getName());
                binding.displayRatingBar.setRating(mealDetailData.getRating());
                binding.descriptionMeal.setText(mealDetailData.getDescription());
                loadImageFromFirebase(mealDetailData.getImgUrl());
                binding.category.setText(mealDetailData.getCategory());
                binding.cuisine.setText(mealDetailData.getCuisine());
                binding.totalTime.setText(mealDetailData.getTimeTotal());
                // set adapter cho recycleview
                viewModel.loadCommnet(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
                    @Override
                    public void onChanged(List<Comment> comments) {
                        if (comments != null) {
                            if (adapter == null) {
                                adapter = new CommentAdapter(requireContext(), comments);
                                binding.commentsRecyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommentDiffCallback((ArrayList<Comment>) adapter.getData(), (ArrayList<Comment>) comments));
                                adapter.setData(comments);
                                diffResult.dispatchUpdatesTo(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }
                });

            }
        });
    }


    private void loadImageFromFirebase(String imageUrl) {
        ImageView imageView = imageViewWeakReference.get();
        if (imageUrl != null && !imageUrl.isEmpty() && imageView != null) {

            Glide.with(binding.imageView.getContext())
                    .asGif() // Thiết lập tải dưới dạng GIF
                    .load(R.drawable.loadding1) // Đặt tên file loading.gif
                    .into(binding.imageView);
            // Tiếp tục xử lý chỉ khi imageUrl không null và không rỗng
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("categories/" + imageUrl);

            // Kiểm tra xem tệp tồn tại trong Firebase Storage
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(binding.imageView.getContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(binding.imageView);
                    Glide.with(binding.imageView.getContext()).resumeRequests();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof StorageException) {
                        StorageException storageException = (StorageException) exception;
                        int errorCode = storageException.getErrorCode();
                        String errorMessage = storageException.getMessage();

                        Glide.with(binding.imageView.getContext())
                                .load(R.drawable.img)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.imageView);
                        Glide.with(binding.imageView.getContext()).resumeRequests();
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
            Glide.with(binding.imageView.getContext())
                    .load(R.drawable.img)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageView);
            Glide.with(binding.imageView.getContext()).resumeRequests();
        }
    }
}

