package vn.edu.tdc.doan_d2.model.comment;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class CommentDiffCallback  extends DiffUtil.Callback{
    private ArrayList<Comment> oldComment;
    private  ArrayList<Comment> newComment;

    public CommentDiffCallback(ArrayList<Comment> oldComment, ArrayList<Comment> newComment) {
        this.oldComment = oldComment;
        this.newComment = newComment;
    }

    @Override
    public int getOldListSize() {
        return oldComment.size();
    }

    @Override
    public int getNewListSize() {
        return newComment.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Comment oldItem = oldComment.get(oldItemPosition);
        Comment newItem = newComment.get(newItemPosition);

        // Kiểm tra xem 2 item có cùng ID không
        return oldItem.getUserName().equals(newItem.getUserName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Comment oldItem = oldComment.get(oldItemPosition);
        Comment newItem = newComment.get(newItemPosition);
        // So sánh các thuộc tính của BaseCategory
        // Chú ý: Bạn cần đảm bảo rằng các lớp Category và Cuisine đều override phương thức equals()
        return oldItem.equals(newItem);
    }
}
