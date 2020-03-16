//Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)
package com.example.tp2_fitts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

//Importation externe pour affichage du graphique ref: http://www.android-graphview.org/

//Librairie externe au SDK Android permettant l'affichage de graphique

public class GraphActivity extends AppCompatActivity {
    GraphView gv;
    LinearRegression regression;
    ArrayList<Double> indiceSort;
    ArrayList<Double> timeSort;
    PointsGraphSeries<DataPoint> seriesPoints;
    LineGraphSeries<DataPoint> seriesLine;
    Button results;

    //Les coordonnées des 2 points pour créer la regression lineaire
    Double[] regPointX;
    Double[] regPointY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //boutton de retour au résultats (pour grarder en mémoire les données de l'expérience.
        results = findViewById(R.id.backResults);
        results.setOnClickListener(backResults_listener);

        //Récupération des données de la régression
        regression = (LinearRegression) getIntent().getSerializableExtra(getResources().getString(R.string.regression));

        gv = findViewById(R.id.gv);

        // Réglage Manuel de l'axe abscisse
        gv.getViewport().setXAxisBoundsManual(true);
        gv.getViewport().setMinX(0.0);
        gv.getViewport().setMaxX(5);

        // Réglage ordonné de l'axe ordonné
        gv.getViewport().setYAxisBoundsManual(true);
        gv.getViewport().setMinY(0.0);
        gv.getViewport().setMaxY(5);

        //Initialisation des series de nuages de points et de points pour la regression lineaire
        seriesPoints = new PointsGraphSeries<DataPoint>();
        seriesLine = new LineGraphSeries<DataPoint>();

        //Initialisation des tableaux de points pour la régression
        regPointX = new Double[]{0.0, 5.0};
        regPointY = new Double [2];

        //Copie des listes d'indice de difficulté et de temps pour les ordonner pour le graph view
        // La liste de temps est ajuster en correspondance pour ne pas mélanger les données.
        indiceSort = new ArrayList(regression.getListIndDiff());
        timeSort = new ArrayList(regression.getListTemps());
        sort(indiceSort, timeSort);

        //Création des données pour la régression linéaire
        createLinearRegression(regression.getA(), regression.getB());

        //Ajout du nuage de points
        for (int i = 0; i< indiceSort.size();i++){

            seriesPoints.appendData(new DataPoint(indiceSort.get(i), timeSort.get(i)), true , indiceSort.size());
        }
        gv.addSeries(seriesPoints);

        //Ajout de la régression linéaire
        for (int i = 0; i< regPointX.length;i++){

            seriesLine.appendData(new DataPoint(regPointX[i], regPointY[i]), true , regPointY.length);
        }
        gv.addSeries(seriesLine);


    }

//Fonction qui ordonne en ordre croissant un tableau et déplace les données du second tableau en concordance
    public void sort(ArrayList<Double> indiceSort, ArrayList<Double> timeSort){

        for (int i = 1; i < indiceSort.size(); i++) {
            int j = i;
            Double B = indiceSort.get(i);
            Double D = timeSort.get(i);
            while ((j > 0) && (indiceSort.get(j-1) > B)) {
                indiceSort.set(j, indiceSort.get(j-1));
                timeSort.set(j,timeSort.get(j-1));
                j--;
            }
            indiceSort.set(j, B);
            timeSort.set(j, D);
        }

    }

    //Création de la régression linéaire utilisant 2 points avec la formule de la régression linéaire
    public void createLinearRegression(Double a, Double b){

        for(int i = 0; i < 2; i++){
            regPointY[i] = a + (b*regPointX[i]);
        }
    }

    //Renvoit à la page des résultats
    View.OnClickListener backResults_listener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            showGraph();

        }

    };

    //Crée et envoit l'intention de retourner à la page de résultats
    public void showGraph() {

        //intent pour partager avec l'autre activité
        Intent intent = new Intent(this, ResultsActivity.class);

        intent.putExtra(getString(R.string.regression), regression);

        startActivity(intent);

    }
}
