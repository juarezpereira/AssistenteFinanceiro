package com.example.assistentefinanceiro.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDCore extends SQLiteOpenHelper {

    private static final String NAME_BD = "AssistentFinanceiro";
    private static final int VERSION_BD = 1;

    public static final String TB_LANCAMENTO = "lancamentos";
    public static final String COLUM_ID = "id";
    public static final String COLUM_DATA = "data";
    public static final String COLUM_TIPO = "tipo";
    public static final String COLUM_DESC = "descricao";
    public static final String COLUM_VALOR = "valor";

    public BDCore(Context context) {
        super(context, NAME_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_LANCAMENTO + "(" +
                COLUM_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUM_DATA  + " DATE," +
                COLUM_DESC  + " VARCHAR," +
                COLUM_TIPO  + " VARCHAR," +
                COLUM_VALOR + " FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TB_LANCAMENTO);
        onCreate(db);
    }

}