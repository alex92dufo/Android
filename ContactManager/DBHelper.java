/*
Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

        Date: 30 avril 2019
        Titre: Application de contact
        Description: Application qui permet d'Ajouter, de mettre a jour ou de supprimer des contacts
*/
package com.demo.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "contacts.db";
    final static int VERSION = R.integer.version;

    public final static String TABLE_CONTACTS = "contacts";
    public final static String C_ID = "_id";
    public final static String C_NOM = "nom";
    public final static String C_PRENOM = "prenom";
    public final static String C_COURRIEL = "courriel";
    public final static String C_TEL = "title";
    public final static String C_FAV = "favoris";

    private static SQLiteDatabase db = null;

    public DBHelper(Context context) {

        super(context, DB_NAME, null, VERSION);

        if(db == null){
            db = getWritableDatabase();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table " + TABLE_CONTACTS + " ( "
                + C_ID + " integer primary key, "
                + C_NOM + " text unique, "
                + C_PRENOM  + " text, "
                + C_COURRIEL + " text, "
                + C_TEL + " text, "
                + C_FAV + " integer ) ";
        Log.d("SQL", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = R.string.drop_table + TABLE_CONTACTS;
        Log.d("SQL", sql);
        db.execSQL(sql);

        onCreate(db);
    }


    //modification d'un contact dans la BD
    public void modifContact(Contact contact){

        ContentValues cv = new ContentValues();
        cv.put(C_ID, contact.getId());
        cv.put(C_NOM, contact.getNom());
        cv.put(C_PRENOM, contact.getPrenom());
        cv.put(C_COURRIEL,contact.getCourriel());
        cv.put(C_TEL, contact.getTelephone());

        //conversion de la valeur booleenne sur switch en int pour insertion dans la table "contacts"
        int fav=0;
        if(contact.getFavoris() == true)
            fav = 1;

        cv.put(C_FAV, fav);

        db.replaceOrThrow(TABLE_CONTACTS, null, cv);
    }

    //insertion d'un nouveau contact dans la BD
    public void insertContact(Contact contact){

        ContentValues cv = new ContentValues();
        cv.put(C_NOM, contact.getNom());
        cv.put(C_PRENOM, contact.getPrenom());
        cv.put(C_COURRIEL,contact.getCourriel());
        cv.put(C_TEL, contact.getTelephone());

        //conversion de la valeur booleenne sur switch en int pour insertion dans la table "contacts"
        int fav=0;
        if(contact.getFavoris() == true)
            fav = 1;

        cv.put(C_FAV, fav);

        db.insert(TABLE_CONTACTS, null, cv);
    }

    //retourne un curseur avec tous les contacts favoris ou non
    Cursor getContacts(){
        return db.rawQuery("Select * from " + TABLE_CONTACTS + " ORDER BY " + C_PRENOM + " ASC", null);
    }

    //retourne un curseur avec tous les contacts favoris
    Cursor getFavoritesContacts(){
        return db.rawQuery("Select * from " + TABLE_CONTACTS + " WHERE " + C_FAV + " = 1 ORDER BY " + C_PRENOM + " ASC" , null);
    }

    public static void deleteContact(int id){
        db.delete(TABLE_CONTACTS, C_ID + "=?", new String[]{Integer.toString(id)});
    }

}
