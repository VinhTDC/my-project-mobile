package vn.edu.tdc.doan_d2;

public class MonAn {
    private String tenMonAn;
    private String moTa;
    private Float gia;

    public MonAn(String tenMonAn, String moTa, Float gia) {
        this.tenMonAn = tenMonAn;
        this.moTa = moTa;
        this.gia = gia;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Float getGia() {
        return gia;
    }

    public void setGia(Float gia) {
        this.gia = gia;
    }
}
