package com.example.assistentefinanceiro.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.assistentefinanceiro.model.Lancamento;

import java.util.ArrayList;
import java.util.Date;

public class DataBase {

    private SQLiteDatabase db;

    public DataBase(Context context){
        BDCore db = new BDCore(context);
        this.db = db.getWritableDatabase();
    }

    public boolean inserir(Lancamento lancamento){
        ContentValues values = new ContentValues();
        values.put(BDCore.COLUM_DATA, lancamento.getDate().getTime());
        values.put(BDCore.COLUM_DESC, lancamento.getDescricao());
        values.put(BDCore.COLUM_TIPO, lancamento.getTipo());
        values.put(BDCore.COLUM_VALOR, lancamento.getValor());

        return db.insert("lancamentos", null, values) > 0;
    }

    public boolean atualizar(Lancamento lancamento){
        ContentValues values = new ContentValues();
        values.put("data", lancamento.getDate().getTime());
        values.put("tipo", lancamento.getTipo());
        values.put("descricao", lancamento.getDescricao());
        values.put("valor", lancamento.getValor());

        return db.update("lancamentos", values, "id = ?", new String[]{""+lancamento.getId()}) > 0;
    }

    public boolean deletar(Lancamento lancamento){
        return db.delete("lancamentos", "id = ?", new String[]{""+lancamento.getId()}) > 0;
    }

    public ArrayList<Lancamento> listar(){
        String[] colums = new String[]{
                BDCore.COLUM_ID,
                BDCore.COLUM_DESC,
                BDCore.COLUM_DATA,
                BDCore.COLUM_VALOR,
                BDCore.COLUM_TIPO};


        Cursor cursor = db.query(BDCore.TB_LANCAMENTO, colums, null, null, null, null, "data ASC");

        ArrayList<Lancamento> lancamentos = new ArrayList<>();

        if (cursor.getCount() > 0){
            cursor.moveToFirst();

            do {
                Lancamento lancamento = new Lancamento();
                lancamento.setId(cursor.getInt(0));
                lancamento.setDescricao(cursor.getString(1));
                lancamento.setDate(new Date(cursor.getLong(2)));
                lancamento.setValor(cursor.getFloat(3));
                lancamento.setTipo(cursor.getString(4));
                lancamentos.add(lancamento);
            }while (cursor.moveToNext());
        }

        return lancamentos;
    }

    public float despesas(){
        float despesa;

        String query = "SELECT SUM(valor) FROM lancamentos WHERE tipo = ?;";
        Cursor cursor = db.rawQuery(query, new String[]{"Despesa"});

        if (cursor.moveToFirst()){
            despesa = cursor.getFloat(0);
        }else{
            despesa = 0;
        }

        return despesa;
    }

    public float investimentos(){
        float investimento;

        String query = "SELECT SUM(valor) FROM lancamentos WHERE tipo = ?;";
        Cursor cursor = db.rawQuery(query, new String[]{"Investimento"});

        if (cursor.moveToFirst()){
            investimento = cursor.getFloat(0);
        }else{
            investimento = 0;
        }

        return investimento;
    }

}