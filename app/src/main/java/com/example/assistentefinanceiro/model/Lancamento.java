package com.example.assistentefinanceiro.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Lancamento implements Parcelable {

    private int id;
    private Date date;
    private String descricao;
    private String tipo;
    private double valor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.descricao);
        dest.writeString(this.tipo);
        dest.writeDouble(this.valor);
    }

    public Lancamento() {
    }

    protected Lancamento(Parcel in) {
        this.id = in.readInt();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.descricao = in.readString();
        this.tipo = in.readString();
        this.valor = in.readDouble();
    }

    public static final Parcelable.Creator<Lancamento> CREATOR = new Parcelable.Creator<Lancamento>() {
        @Override
        public Lancamento createFromParcel(Parcel source) {
            return new Lancamento(source);
        }

        @Override
        public Lancamento[] newArray(int size) {
            return new Lancamento[size];
        }
    };

}