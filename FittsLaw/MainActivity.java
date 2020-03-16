//Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)
package com.example.tp2_fitts;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    Button button;
    int size;
    Random random = new Random();
    int tries = 0;
    int nbTries ;
    RelativeLayout.LayoutParams params;
    int width = 0;
    int height = 0;
    double debut =0;
    double fin;
    int newX;
    int newY;
    double indDiff;
    boolean started = false;
    float x;
    float y;
    int statusBarHeight = 0;
    int navigationBarHeight = 0;
    int actionBarHeight = 0;

    //Tableau contenant les résultats des essais
    ArrayList<Double> listIndDiff = new ArrayList<Double>();
    ArrayList<Double> listTemps = new ArrayList<Double>();

    //Objet LinearRegression, pour calculer les valeur de a, b et r2. permet également d'envoyer les informations à ResultsActivity
    LinearRegression regression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nbTries = getResources().getInteger(R.integer.nbTries);

        button  = findViewById(R.id.button);
        button.setOnTouchListener(buttonListener);

        //recuperation des dimension de l'appareil
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        adjustHeight();

        //pour recuperer paremetre du bouton
        params = (RelativeLayout.LayoutParams)button.getLayoutParams();

    }



    //pour ajuster la hauteur de l'ecran (pour le positionnement du bouton)
    public void adjustHeight(){

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            height -= statusBarHeight;
        }

        resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
            height -= navigationBarHeight;
        }

        resourceId = getResources().getIdentifier("action_bar_height", "dimen", "android");
        if (resourceId > 0) {
            actionBarHeight = getResources().getDimensionPixelSize(resourceId);
            height -= actionBarHeight;
        }

    }



    View.OnTouchListener buttonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            //quand on pose le doigt sur le carré
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                x = motionEvent.getRawX();
                y = motionEvent.getRawY();

                buttonDown();

            }

            //quand on relève le doigt du carré
            //si on a atteint le nombre max d'essai, on execute la regression lineaire
            //et on affiche les resultats dans une nouvelle activité.
            //Sinon on set une nouvelle taille et position au carré
            //et on prend le temps de debut.
            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                if (tries == nbTries){

                    regression = new LinearRegression(listIndDiff, listTemps);
                    showResults();

                }
                else {

                    buttonUp();
                }

            }
            return true;
        }
    };


    //quand on pose le doigt sur le carré
    //on prend le temps de fin et incremente le nombre d'essai
    //on ajoute aux arraylist, le temps de reaction ainsi que l'indice de difficulté
    public void buttonDown(){
        if(started){

            tries++;
            fin = System.currentTimeMillis() - debut;
            listIndDiff.add(indDiff);
            listTemps.add(fin/1000);

        //pour le premier essai
        }else{
            started = true;
            size  = params.width;
        }
    }

    //On set une nouvelle taille et position au carré
    //et on prend le temps de debut.
    public void buttonUp(){
        setNewSizePosition();
        debut = System.currentTimeMillis();
    }




    //nouvelle largeur du bouton carré
    public int getRandomSize(){
        return random.nextInt(width/2) +  (width /25);
    }

    //change les dimensions et la position du bouton
    public void setNewSizePosition(){

        //nouvelle taille du carré
        size = getRandomSize();


        //on créer les nouvelles coordonnées du carré
        newX = random.nextInt(width - size) ;
        newY = random.nextInt(height - size);

        //fait le calcul de l'indice de difficulté
        getIndDiff();


        //on set la nouvelle taille et position au carré
        params.leftMargin=  newX;
        params.topMargin =  newY;

        params.width = size;
        params.height = size;
        button.setLayoutParams(params);
        button.setText("");


    }


    //prend en parametre l'ancienne position du carré
    // calcule la distance A avec la formule :    racine(deltaX^2 + deltaY^2)
    //puis l'indice de difficulté avec la formule du cours log(A/W +1 ) avec W qui est la largeur du carré
    public void getIndDiff(){

        float deltaX = (newX + 1/2*size) - x;
        float deltaY = (newY + 1/2*size) - y;

        double A =  Math.sqrt( Math.pow(deltaX, 2) + Math.pow(deltaY, 2)  ) ;

        indDiff = Math.log(A/size + 1 ) / Math.log(2);


    }


//Envoit les résultats à l'activité de présentation des résultats
    public void showResults() {


        //intent pour partager avec l'autre activité
        Intent intent = new Intent(this, ResultsActivity.class);

        intent.putExtra(getString(R.string.regression), regression);

        startActivity(intent);


    }


}
