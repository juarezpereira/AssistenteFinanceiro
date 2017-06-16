package com.example.assistentefinanceiro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.assistentefinanceiro.adapter.LacamentoAdapter;
import com.example.assistentefinanceiro.model.Lancamento;
import com.example.assistentefinanceiro.utils.DataBase;

import java.util.ArrayList;

public class LancamentosActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener{

    public static final String LANCAMENTO = "LANCAMENTO";
    private static final int REQUEST_CODE_REGISTRO = 100;
    private static final String LANCAMENTOS = "LANCAMENTOS";

    private ArrayList<Lancamento> listLancamento;
    private LacamentoAdapter adapterLancamento;

    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamentos);

        db = new DataBase(this);

        listLancamento = new ArrayList<>();

        adapterLancamento = new LacamentoAdapter(this, listLancamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.lstLancamento);
        listView.setAdapter(adapterLancamento);
        listView.setOnItemClickListener(this);

        if (savedInstanceState != null){
            listLancamento = savedInstanceState.getParcelableArrayList(LANCAMENTOS);
            adapterLancamento.notifyDataSetChanged();
        }else{
            listLancamento.addAll(db.listar());
            adapterLancamento.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_REGISTRO){
            if (resultCode == RESULT_OK){
                listLancamento.clear();
                listLancamento.addAll(db.listar());
                adapterLancamento.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lancamento, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_evento){
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REGISTRO);
            return true;
        }else if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelableArrayList(LANCAMENTOS, listLancamento);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Lancamento lancamento = (Lancamento) parent.getItemAtPosition(position);

        AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LancamentosActivity.this, CadastroActivity.class);
                        intent.putExtra(LANCAMENTO, lancamento);
                        startActivityForResult(intent, REQUEST_CODE_REGISTRO);
                    }
                })
                .setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (db.deletar(lancamento)){
                            listLancamento.remove(position);
                            adapterLancamento.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getBaseContext(), "Não foi possivel excluir",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();
        mAlertDialog.show();
    }

}