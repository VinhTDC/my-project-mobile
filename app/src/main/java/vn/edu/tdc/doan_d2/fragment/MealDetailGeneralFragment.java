package vn.edu.tdc.doan_d2.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.FragmentCommentBinding;
import vn.edu.tdc.doan_d2.databinding.RecipeMealDetailLayoutBinding;
import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.comment.CommentDiffCallback;
import vn.edu.tdc.doan_d2.model.comment.Rating;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;
import vn.edu.tdc.doan_d2.view.CommentAdapter;
import vn.edu.tdc.doan_d2.viewmodel.mealdetail.MealDetailViewModel;


public class MealDetailGeneralFragment extends Fragment {
    private RecipeMealDetailLayoutBinding binding;
    private CommentFragment  fragment;
    private String tagFragment = "RECIPE_FRAGMENT_TAG";

    private MealDetailViewModel viewModel;
    private WeakReference<ImageView> imageViewWeakReference;
    private CommentAdapter adapter;
    private MealDetailResponsive mealDetailResponsive;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final String tagFragmnetCommnet = "FRAGMNET_COMMENT";
    private String idMealC;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecipeMealDetailLayoutBinding.inflate(inflater, container, false);
        binding.getRoot().setTag(tagFragment);
        viewModel = new ViewModelProvider(requireActivity()).get(MealDetailViewModel.class);
        mealDetailResponsive = new MealDetailResponsive(getActivity().getApplication(), viewModel);
        imageViewWeakReference = new WeakReference<>(binding.imageView);
        fragment = new CommentFragment(viewModel);
        // Thêm CommentFragment vào FragmentContainerViews
        swipeRefreshLayout = binding.swipeLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_comment,fragment,tagFragmnetCommnet);
        transaction.commit();

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        adapter = new CommentAdapter(getContext(),fragment.getData());
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
                        idMealC = idMeal;
                        mealDetailResponsive.getComment(idMeal).observe(getViewLifecycleOwner(), commentList -> {
                            // Cập nhật dữ liệu trong adapter
                            adapter.setData(commentList);
                            adapter.notifyDataSetChanged();

                            // Cập nhật danh sách bình luận trong CommentFragment
                            fragment.updateCommnetList(commentList);
                        });
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
                binding.descriptionMeal.setText(mealDetailData.getDescription());
                loadImageFromFirebase(mealDetailData.getImgUrl());
                binding.category.setText(mealDetailData.getCategory());
                binding.cuisine.setText(mealDetailData.getCuisine());
                binding.totalTime.setText(mealDetailData.getTimeTotal());
                viewModel.loadRating(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<Rating>() {
                    @Override
                    public void onChanged(Rating rating) {
                        binding.displayRatingBar.setRating(rating.getRating());
                    }
                });
                
            }
        });
    }


    private void loadImageFromFirebase(String imageUrl) {
        ImageView imageView = imageViewWeakReference.get(); // Get ImageView from WeakReference

        if (imageView != null && imageUrl != null && !imageUrl.isEmpty() && getViewLifecycleOwner().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            // Load loading GIF and start Glide request only if the Fragment's view is at least in the STARTED state

            Glide.with(requireContext())
                    .asGif()
                    .load(R.drawable.loadding1)
                    .into(imageView);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(imageUrl);

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    // Load the image only if the Fragment's view is at least in the STARTED state
                    Glide.with(requireContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            }).addOnFailureListener(exception -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    // Load the error image only if the Fragment's view is at least in the STARTED state
                    Glide.with(requireContext())
                            .load(R.drawable.img)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);

                    if (exception instanceof StorageException) {
                        StorageException storageException = (StorageException) exception;
                        int errorCode = storageException.getErrorCode();
                        String errorMessage = storageException.getMessage();
                        Log.e("FirebaseStorage", "Error: " + errorMessage + " (Code: " + errorCode + ")"); // Log both error message and code
                    } else {
                        Log.e("FirebaseStorage", "Error: " + exception.getMessage());
                    }
                }
            });
        } else {
            // Load the default error image if ImageView, imageUrl, or lifecycle conditions are not met
            if(imageView != null) {
                Glide.with(requireContext())
                        .load(R.drawable.img)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.with(this).onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.with(this).onStop();
    }


}

