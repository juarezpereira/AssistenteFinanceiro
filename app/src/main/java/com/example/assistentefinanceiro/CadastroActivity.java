package com.example.assistentefinanceiro;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assistentefinanceiro.model.Lancamento;
import com.example.assistentefinanceiro.utils.DataBase;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CadastroActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener{

    private EditText edtData;
    private EditText edtDescricao;
    private EditText edtValor;

    private String tipo;
    private DataBase db;

    private Lancamento lancamento;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = new DataBase(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtData = (EditText) findViewById(R.id.edtData);
        edtValor = (EditText) findViewById(R.id.edtValor);
        Spinner spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);

        edtData.setOnClickListener(new DateListener());
        edtData.setOnFocusChangeListener(new DateListener());

        edtValor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    onClick(v);
                    return true;
                }
                return false;
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.lancamentos_tipos, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(spinnerAdapter);
        spinnerTipo.setOnItemSelectedListener(this);

        Button btn = (Button) findViewById(R.id.btnRegister);
        btn.setOnClickListener(this);

        if (getIntent().getParcelableExtra(LancamentosActivity.LANCAMENTO) != null){
            lancamento = getIntent().getParcelableExtra(LancamentosActivity.LANCAMENTO);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

            edtValor.setText(numberFormat.format(lancamento.getValor()));
            edtDescricao.setText(lancamento.getDescricao());
            edtData.setText(dateFormat.format(lancamento.getDate()));

            if (lancamento.getTipo().equals("Investimento")){
                spinnerTipo.setSelection(2);
            }else{
                spinnerTipo.setSelection(1);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tipo = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        AlertDialog mAlertDialog;

        if (tipo.equals("Evento") || tipo == null){
            Toast.makeText(CadastroActivity.this, "Escolha o tipo de lançamento.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validaCampo(edtDescricao) && validaCampo(edtData) && validaCampo(edtValor)){
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            Number valor;
            try {
                valor = numberFormat.parse(edtValor.getText().toString());
            } catch (ParseException e) {
                valor = 0;
            }

            if (lancamento != null){
                lancamento.setTipo(tipo);
                lancamento.setValor(valor.doubleValue());
                lancamento.setDescricao(edtDescricao.getText().toString().trim());

                try {
                    lancamento.setDate(dateFormat.parse(edtData.getText().toString()));
                } catch (ParseException e) {
                    lancamento.setDate(Calendar.getInstance().getTime());
                }

                if (db.atualizar(lancamento)){
                    setResult(RESULT_OK);

                    mAlertDialog = new AlertDialog.Builder(CadastroActivity.this)
                            .setTitle("Lançamento")
                            .setMessage("Atualizado com sucesso!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    mAlertDialog.show();
                }else{
                    Toast.makeText(CadastroActivity.this, "Não foi possivel atualizar.", Toast.LENGTH_SHORT).show();
                }
            }else{
                lancamento = new Lancamento();
                lancamento.setTipo(tipo);
                lancamento.setValor(Integer.valueOf(edtValor.getText().toString().trim()));
                lancamento.setDescricao(edtDescricao.getText().toString().trim());

                try {
                    lancamento.setDate(dateFormat.parse(edtData.getText().toString()));
                } catch (ParseException e) {
                    lancamento.setDate(Calendar.getInstance().getTime());
                }

                if (db.inserir(lancamento)){
                    setResult(RESULT_OK);

                    mAlertDialog = new AlertDialog.Builder(CadastroActivity.this)
                            .setTitle("Lançamento")
                            .setMessage("Registrado com sucesso!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    mAlertDialog.show();
                }else{
                    Toast.makeText(CadastroActivity.this, "Não foi possivel registrar.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public boolean validaCampo(EditText editText){
        editText.setError(null);
        if (editText.getText().toString().isEmpty()){
            editText.setError("Preencha esse campo");
            return false;
        }
        return true;
    }

    private int ano, mes, dia;

    private void initDate(){
        if (ano == 0){
            Calendar calendar = Calendar.getInstance();
            ano = calendar.get(Calendar.YEAR);
            mes = calendar.get(Calendar.MONTH);
            dia = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }

    public void showDatePicker(){
        initDate();

        Calendar ca = Calendar.getInstance();
        ca.set(ano, mes, dia);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, this,
                ca.get(Calendar.YEAR),
                ca.get(Calendar.MONTH),
                ca.get(Calendar.DAY_OF_MONTH));

        mDatePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ano = year;
        mes = month;
        dia = dayOfMonth;

        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes, dia);

        String dateFormated = dateFormat.format(calendar.getTime());

        edtData.setText(dateFormated);
    }

    private class DateListener implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            showDatePicker();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                showDatePicker();
        }

    }

}