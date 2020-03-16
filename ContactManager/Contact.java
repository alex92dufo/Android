package com.demo.contacts;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import java.io.Serializable;


public class Contact  extends BaseObservable implements Serializable {

    private int id;
    private String nom, prenom, nomComplet, initials, courriel, telephone;
    private boolean favoris;

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {

        this.nom = nom;
        notifyPropertyChanged(BR.nom);
    }

    @Bindable
    public String getPrenom() {
        return prenom;

    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
        notifyPropertyChanged(BR.prenom);
    }

    @Bindable
    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
        notifyPropertyChanged(BR.courriel);
    }

    @Bindable
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
        notifyPropertyChanged(BR.telephone);
    }

    @Bindable
    public boolean getFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
        notifyPropertyChanged(BR.favoris);
    }


    @Bindable
    public String getNomComplet() {
        return nomComplet;

    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet= nomComplet;
        notifyPropertyChanged(BR.nomComplet);
    }


    @Bindable
    public String getInitials() {
        return initials;

    }

    public void setInitials(String initials) {
        this.initials= initials;
        notifyPropertyChanged(BR.initials);
    }

}
