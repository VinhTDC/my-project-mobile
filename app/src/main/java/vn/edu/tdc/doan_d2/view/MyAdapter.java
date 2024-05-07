package vn.edu.tdc.doan_d2.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.CategoryListItemBinding;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<String> categoriesName;
    private List<String> data;

    public MyAdapter(Context context, List<String> categoriesName) {
        this.context = context;
        this.categoriesName = categoriesName;
        this.data = categoriesName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.category_list_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String category = data.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CategoryListItemBinding categoryListItemBinding;

        public MyViewHolder(CategoryListItemBinding categoryListItemBinding) {
            super(categoryListItemBinding.getRoot());

            this.categoryListItemBinding = categoryListItemBinding;
            categoryListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        public void bind(String category) {
            // Gán dữ liệu vào các view trong ViewHolder
            categoryListItemBinding.nameCategory.setText(category);
        }
    }



}
