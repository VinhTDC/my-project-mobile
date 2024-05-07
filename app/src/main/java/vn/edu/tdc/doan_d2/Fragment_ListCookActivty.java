package vn.edu.tdc.doan_d2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

public class Fragment_ListCookActivty extends ListFragment {
    //Door duw lieu
    ArrayList<MonAn> arrayMonAn;
    MonAn_Adapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        arrayMonAn = new ArrayList<>();
        //Add du lieu duoi vao
        AddArrayMonAn();
        adapter = new MonAn_Adapter(getActivity(), R.layout.row_monan, arrayMonAn);
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_listcook, container, false);

    }

    private void AddArrayMonAn() {
        arrayMonAn.add(new MonAn("Bun dau mam tom","Ha Noi", 20000F));
        arrayMonAn.add(new MonAn("Com Tam","Lam tu ca tuoi", 10000F));
        arrayMonAn.add(new MonAn("Bun dau mam tom","Ha Noi", 20000F));
        arrayMonAn.add(new MonAn("Trung cut xao me","Mon trinh thich nhat", 10000000F));
        arrayMonAn.add(new MonAn("Bun dau mam tom","Ha Noi", 20000F));
    }
}
