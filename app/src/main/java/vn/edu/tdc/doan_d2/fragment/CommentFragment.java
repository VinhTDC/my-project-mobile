package vn.edu.tdc.doan_d2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.FragmentCategoryBinding;
import vn.edu.tdc.doan_d2.databinding.FragmentCommentBinding;
import vn.edu.tdc.doan_d2.databinding.RecipeMealDetailLayoutBinding;
import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.comment.CommentDiffCallback;
import vn.edu.tdc.doan_d2.view.CommentAdapter;
import vn.edu.tdc.doan_d2.viewmodel.mealdetail.MealDetailViewModel;

public class CommentFragment extends Fragment {
    private FragmentCommentBinding binding;

    private MealDetailViewModel viewModel;
    private CommentAdapter adapter;
    private  List<Comment> data;

    // Constructor
    public CommentFragment( MealDetailViewModel viewModel) {
        // Khắc phục: Không gán lại binding trong onCreateView
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(MealDetailViewModel.class);
        adapter = new CommentAdapter(getContext(), new ArrayList<>());
        binding.commentsRecyclerView.setAdapter(adapter);
        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.loadCommnet(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                if (comments != null) {
                    if (adapter == null) {
                        adapter = new CommentAdapter(requireContext(), comments);
                        binding.commentsRecyclerView.setAdapter(adapter);
                        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        setData(comments);
                        adapter.notifyDataSetChanged();
                    } else {
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommentDiffCallback((ArrayList<Comment>) adapter.getData(), (ArrayList<Comment>) comments));
                        adapter.setData(comments);
                        diffResult.dispatchUpdatesTo(adapter);
                    }
                }

            }
        });

    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
