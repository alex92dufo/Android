//Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)
package com.example.tp2_fitts;

import java.io.Serializable;
import java.util.ArrayList;

public class LinearRegression implements Serializable {

    //indices difficulté = x
    private ArrayList<Double> listIndDiff;

    //temps = y
    private ArrayList<Double> listTemps;

    private double a,b,r=0.0;


    //Constructeur de la classe
    public LinearRegression( ArrayList<Double> listIndDiff, ArrayList<Double> listTemps){

        this.listIndDiff = listIndDiff;
        this.listTemps = listTemps;


    }

    //Calcul de la pente de la régression linéaire
    public double slope(ArrayList<Double> x, ArrayList<Double> y){
        checkSizes(x,y);
        b = (this.getXyBar()-(this.getUx()*this.getUy()))/(this.getX2Bar()-(Math.pow(this.getUx(),2)));
        return b;
    }

    //Calcul de la valeur à l'origine de la régression linéaire
    public double origin(){

        a = getUy()-(getB()*getUx());
        return a;
    }

    //Calcul de moyenne (pour ID)
    public double mean(ArrayList<Double> x){
        double moyenne=0.0;
        for (double i : x)
            moyenne += i;
        return moyenne/x.size();
    }

    //Calcul de XY BAR
    public double meanXY(ArrayList<Double> x, ArrayList<Double> y){
        double moyenne=0.0;
        checkSizes(x,y);
        for (int i=0;i<x.size();i++) { moyenne+= (x.get(i) * y.get(i)) ;

        }
        return moyenne/x.size();
    }

    //Moyenne au carré
    public double meanSquared(ArrayList<Double> x){
        double moyenne = 0.0;
        for (double i : x){
            moyenne += Math.pow(i,2);
        }
        return moyenne/x.size();
    }

    //Calcul du coefficient de détermination
    public double R2 (){
        r = 1- (getSsr()/getSst());
        return r;
    }

    //Sum of squares residuals
    public double ssr (ArrayList<Double> x, ArrayList<Double> y){
        double somme = 0.0;
        checkSizes(x,y);
        for (int i = 0; i< y.size(); i++){
            somme += Math.pow((y.get(i)- ((this.getB()*x.get(i))+ this.getA())),2);
        }
        return somme;
    }

    //Sum of squares total
    public double sst (ArrayList<Double>y){
        double somme = 0.0;
        if (y.size() == 0)
            throw new IllegalArgumentException();
        else{
            for (double i : y){
                somme += Math.pow((i - this.getUy()),2);
            }
        }
        return somme;
    }

    //Vérifie que les tableaux ont la même taille avant de procéder aux calculs
    public static void checkSizes(ArrayList<Double>x, ArrayList<Double> y){
        if ((x.size()!= y.size()) || (x.size() == 0 || (y.size() == 0))){
            throw new IllegalArgumentException();
        }
    }


    public ArrayList<Double> getListIndDiff() {
        return this.listIndDiff;
    }

    public ArrayList<Double> getListTemps() {
        return this.listTemps;
    }



    //---Getters---//

    public double getR(){
        return this.R2();
    }
    public double getB() {
        return this.slope(listIndDiff, listTemps);
    }
    public double getUx() {
        return this.mean(listIndDiff);
    }
    public double getUy() {
        return this.mean(listTemps);
    }
    public double getXyBar() {
        return this.meanXY(listIndDiff, listTemps);
    }
    public double getX2Bar() {
        return this.meanSquared(listIndDiff);
    }
    public double getA() {
        return this.origin();
    }
    public double getSsr(){
        return this.ssr(listIndDiff, listTemps);
    }
    public double getSst(){ return this.sst(listTemps); }

}
