package com.example.nhadat_app.Model;

import android.net.Uri;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TinDang implements Serializable {
    private String idl;
    private String userName;
    private String danhMuc;
    private String tinh;
    private String huyen;
    private String xa;
    private int dienTich;
    private long gia;
    private String phapLy;
    private String huongNha;
    private String tieuDe;
    private String moTa;
    private int luotxem;
    private String date;
    private ArrayList<String> list=new ArrayList<>();

    public TinDang(String userName,
                   String danhMuc, String tinh, String huyen,
                   String xa, int dienTich, long gia, String phapLy,
                   String huongNha, String tieuDe, String moTa, int luotxem,
                   String date, ArrayList<String> list) {
        this.userName = userName;
        this.danhMuc = danhMuc;
        this.tinh = tinh;
        this.huyen = huyen;
        this.xa = xa;
        this.dienTich = dienTich;
        this.gia = gia;
        this.phapLy = phapLy;
        this.huongNha = huongNha;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.luotxem = luotxem;
        this.date = date;
        this.list = list;
    }

    public TinDang(String idl, String userName,
                   String danhMuc, String tinh, String huyen, String xa,
                   int dienTich, long gia, String phapLy, String huongNha, String tieuDe,
                   String moTa, int luotxem, String date) {
        this.idl = idl;
        this.userName = userName;
        this.danhMuc = danhMuc;
        this.tinh = tinh;
        this.huyen = huyen;
        this.xa = xa;
        this.dienTich = dienTich;
        this.gia = gia;
        this.phapLy = phapLy;
        this.huongNha = huongNha;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.luotxem = luotxem;
        this.date = date;
    }

    public TinDang(String userName, String danhMuc, String tinh, String huyen, String xa,
                   int dienTich, long gia, String phapLy, String huongNha, String tieuDe,
                   String moTa, int luotxem, String date) {
        this.userName = userName;
        this.danhMuc = danhMuc;
        this.tinh = tinh;
        this.huyen = huyen;
        this.xa = xa;
        this.dienTich = dienTich;
        this.gia = gia;
        this.phapLy = phapLy;
        this.huongNha = huongNha;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.luotxem = luotxem;
        this.date = date;
    }

    public TinDang(String idl, String userName, String danhMuc, String tinh,
                   String huyen, String xa, int dienTich, long gia, String phapLy, String huongNha,
                   String tieuDe, String moTa, int luotxem, ArrayList<String> list) {
        this.idl = idl;
        this.userName = userName;
        this.danhMuc = danhMuc;
        this.tinh = tinh;
        this.huyen = huyen;
        this.xa = xa;
        this.dienTich = dienTich;
        this.gia = gia;
        this.phapLy = phapLy;
        this.huongNha = huongNha;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.luotxem = luotxem;
        this.list = list;
    }

    public TinDang(String idl, String userName, String danhMuc, String tinh, String huyen,
                   String xa, int dienTich, long gia, String phapLy, String huongNha, String tieuDe,
                   String moTa, int luotxem, String date, ArrayList<String> list) {
        this.idl = idl;
        this.userName = userName;
        this.danhMuc = danhMuc;
        this.tinh = tinh;
        this.huyen = huyen;
        this.xa = xa;
        this.dienTich = dienTich;
        this.gia = gia;
        this.phapLy = phapLy;
        this.huongNha = huongNha;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.luotxem = luotxem;
        this.date = date;
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public String getIdl() {
        return idl;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public String getUserName() {
        return userName;
    }

    public String getTinh() {
        return tinh;
    }

    public String getHuyen() {
        return huyen;
    }

    public String getXa() {
        return xa;
    }

    public int getDienTich() {
        return dienTich;
    }

    public long getGia() {
        return gia;
    }

    public String getPhapLy() {
        return phapLy;
    }

    public String getHuongNha() {
        return huongNha;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getLuotxem() {
        return luotxem;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return idl+"noikho"+userName +"noikho"+danhMuc+"noikho"+tinh+"noikho"+huyen+"noikho"+xa+"noikho"+
                dienTich+"noikho"+gia+"noikho"+phapLy+"noikho"+huongNha+"noikho"+tieuDe+"noikho"+moTa
                +"noikho"+luotxem+"noikho"+date;
    }
}

