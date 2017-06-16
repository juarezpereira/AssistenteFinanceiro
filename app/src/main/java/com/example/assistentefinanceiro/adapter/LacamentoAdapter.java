package com.example.assistentefinanceiro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.assistentefinanceiro.R;
import com.example.assistentefinanceiro.model.Lancamento;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class LacamentoAdapter extends BaseAdapter{

    private ArrayList<Lancamento> mlist;
    private LayoutInflater mLayoutInflater;

    public LacamentoAdapter(@NonNull Context context, ArrayList<Lancamento> list) {
        super();
        mlist = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Lancamento lancamento = mlist.get(position);
        View layoutLancamento;

        if (convertView == null){
            layoutLancamento = mLayoutInflater.inflate(R.layout.item_lancamento, parent, false);
        }else{
            layoutLancamento = convertView;
        }

        TextView tvTipo = (TextView) layoutLancamento.findViewById(R.id.tvTipo);
        TextView tvDescricao = (TextView) layoutLancamento.findViewById(R.id.tvDescricao);
        TextView tvValor = (TextView) layoutLancamento.findViewById(R.id.tvValor);
        TextView tvData = (TextView) layoutLancamento.findViewById(R.id.tvData);
        View viewFlag = layoutLancamento.findViewById(R.id.flag);

        if (lancamento.getTipo().equals("Despesa")){
            viewFlag.setBackgroundColor(Color.parseColor("#D32F2F"));
        }else if (lancamento.getTipo().equals("Investimento")){
            viewFlag.setBackgroundColor(Color.parseColor("#43a047"));
        }

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(2);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        tvTipo.setText(lancamento.getTipo());
        tvDescricao.setText(lancamento.getDescricao());
        tvValor.setText(numberFormat.format(lancamento.getValor()));
        tvData.setText(format.format(lancamento.getDate()));

        return layoutLancamento;
    }

}