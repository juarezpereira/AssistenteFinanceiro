package com.example.assistentefinanceiro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.assistentefinanceiro.utils.DataBase;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TextView despesa;
    private TextView investimento;
    private TextView receita;

    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        despesa = (TextView) findViewById(R.id.tvDespesa);
        investimento = (TextView) findViewById(R.id.tvInvestimento);
        receita = (TextView) findViewById(R.id.tvReceita);

        db = new DataBase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int despesas = db.despesas();
        int investimentos = db.investimentos();
        int receitas = investimentos - despesas;

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(2);

        despesa.setText(numberFormat.format(despesas));
        investimento.setText(numberFormat.format(investimentos));
        receita.setText(numberFormat.format(receitas));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_lancamentos){
            Intent intent = new Intent(this, LancamentosActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}