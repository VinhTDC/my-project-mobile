package vn.edu.tdc.doan_d2.fragment;

public interface PaginationInterface {
    public void goToPreviousPage();
    public void goToNextPage();
    public void onPageChanged(int currentPage);
}
