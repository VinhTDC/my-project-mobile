package vn.edu.tdc.doan_d2.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

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
    public CommentAdapter(Context context) {
        this.context = context;

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

        public MyViewHolder(CommentItemBinding commentItemBinding) {
            super(commentItemBinding.getRoot());
            this.commentItemBinding = commentItemBinding;
        }

        public void bind(Comment comment) {
            commentItemBinding.commentContentTextView.setText(comment.getComment());
            commentItemBinding.userRatingBar.setRating(comment.getRating());
            commentItemBinding.userNameTextView.setText(comment.getUserName());
        }
    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
