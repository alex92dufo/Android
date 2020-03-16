//Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

package com.example.tp2_fitts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;


public class ResultsActivity extends AppCompatActivity {

    RecyclerView rv;

    LinearRegression regression;

    TextView tvR, tvA, tvB;

    Button export, graph;

    //Définit le forme d'écrite des difficulté et du temps
    DecimalFormat diffFormat;
    DecimalFormat tempsFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        rv = findViewById(R.id.tries_rv);

        MyAdapter adapter = new MyAdapter();
        rv.setAdapter(adapter);

        //recuperation objet LinearRegression
        regression = (LinearRegression) getIntent().getSerializableExtra(getString(R.string.regression));

        //boutton d'exportation
        export = findViewById(R.id.export_button);
        export.setOnClickListener(export_listener);

        //boutton d'accès au grahique des résultat
        graph = findViewById(R.id.graph_button);
        graph.setOnClickListener(graph_listener);

        //affichage resultat a
        tvA = findViewById(R.id.a_tv);
        tvA.setText(String.format(getString(R.string.a), regression.getA()));

        //affichage resultat b
        tvB = findViewById(R.id.b_tv);
        tvB.setText(String.format(getString(R.string.b), regression.getB()));

        //affichage resultat r2
        tvR = findViewById(R.id.r2_tv);
        tvR.setText(String.format(getResources().getString(R.string.r2), regression.getR()));

        diffFormat = new DecimalFormat(getResources().getString(R.string.diffFormat));
        tempsFormat = new DecimalFormat(getResources().getString(R.string.tempsFormat));

    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder>{
        //Nombre d'essai
        final int n = getResources().getInteger(R.integer.nbTries);

        int nbr_essai = 0;


        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tries_row, parent, false);

            MyHolder vh = new MyHolder(v, nbr_essai);
            nbr_essai++;

            return vh;
        }

        //Éléments à répéter à chaque essai dans le recycler view
        @Override
        public void onBindViewHolder(@NonNull MyHolder vh, int i) {
            vh.difficulty.setText(getResources().getString(R.string.diff, diffFormat.format(regression.getListIndDiff().get(i))));
            vh.time.setText(getResources().getString(R.string.time, tempsFormat.format(regression.getListTemps().get(i))));
            vh.resultat.setText(getResources().getString(R.string.result, i+1));

        }

        @Override
        public int getItemCount() {
            return n;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView difficulty, time, resultat;
        int nbr_essai;

        MyHolder(View view, int essai_num){
            super(view);
            this.nbr_essai = essai_num;

            difficulty = view.findViewById(R.id.difficulty_tv);
            time = view.findViewById(R.id.time_tv);
            resultat = view.findViewById(R.id.tries_tv);
        }
    }

    //Exportation des données en format texte
    View.OnClickListener export_listener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            //String contenant toutes les données à exporter.
            String exportString = getString(R.string.exportFitts, regression.getA(), regression.getB(), regression.getR());

            for (int i = 0; i < getResources().getInteger(R.integer.nbTries); i++){
                exportString += getString(R.string.exportData, (i+1), diffFormat.format(regression.getListIndDiff().get(i)) ,tempsFormat.format(regression.getListTemps().get(i)));
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, exportString);
            startActivity(intent);

        }

    };

    //Demande d'affichage du graphique
    View.OnClickListener graph_listener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            showGraph();

        }

    };

    //Fonctione de transfert à l'activité GraphActivity avec tous les éléments de la régression linéaires.
    public void showGraph() {


        //intent pour partager avec l'autre activité
        Intent intent = new Intent(this, GraphActivity.class);

        intent.putExtra(getString(R.string.regression), regression);

        startActivity(intent);

    }

}


