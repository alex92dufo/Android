/*
Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

        Date: 30 avril 2019
        Titre: Application de contact
        Description: Application qui permet d'Ajouter, de mettre a jour ou de supprimer des contacts
*/
package com.demo.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import com.demo.contacts.RecyclerUtils.ContactRecyclerAdapter;
import com.demo.contacts.RecyclerUtils.ContactSwipeCallback;

public class MainActivity extends AppCompatActivity implements ContactSwipeCallback.RecyclerItemTouchHelperListener {

    private DBHelper dbh;
    private RecyclerView contactRecyler;
    private BottomNavigationView nav;
    private ContactRecyclerAdapter adapter;

    public MainActivity(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //creation/ouverture DB
        dbh = new DBHelper(this);
        ContactRecyclerAdapter adapter = afficherContacts();

        //Action de swipe pour détruire le contact
        ItemTouchHelper.SimpleCallback myItemTouch = new ContactSwipeCallback(adapter, this, this);
        new ItemTouchHelper(myItemTouch).attachToRecyclerView(contactRecyler);

        //Gestion du bottom menu, "Favorites" et "All"
        BottomNavigationView nav = findViewById(R.id.navigation);

        //Preselection de "All"
        nav.getMenu().getItem(1).setChecked(true);

        //listenner pour filtrer la liste, favoris ou tous les contacts
        nav.setOnNavigationItemSelectedListener((item) -> {
            switch(item.getItemId()){
                case R.id.favorites:
                    ContactRecyclerAdapter adapterFav = new ContactRecyclerAdapter(dbh.getFavoritesContacts());
                    contactRecyler.setAdapter(adapterFav);
                    return true;
                case R.id.all:
                    ContactRecyclerAdapter adapterAll = new ContactRecyclerAdapter(dbh.getContacts());
                    contactRecyler.setAdapter(adapterAll);
                    return true;
            }
            return false;
        });


    }

    private ContactRecyclerAdapter afficherContacts() {
        //Affichage de tous les contacts dans le RecyclerView
        contactRecyler = findViewById(R.id.contact_list);
        adapter = new ContactRecyclerAdapter(dbh.getContacts());
        contactRecyler.setAdapter(adapter);
        return adapter;
    }


    //Top right menu, pour la creation d'un nouveau contact
    //Ouvre l'activité "EditContactActivity"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_topright, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.new_contact) {

            Intent intent = new Intent(this, EditContactActivity.class);

            //extra pour differencer la provenance de l'intent
            //pour que la view d'edition de contact serve à creer un nouveau contace
            intent.putExtra("activity", 1);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Mise à jour du recycler view au retour d'ajout de contact ou de modification de contact
    @Override
    protected void onResume() {
        super.onResume();
        afficherContacts();
        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.getMenu().getItem(1).setChecked(true);
    }

    //Met à jour le recycler view apres avoir deleter un contact
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(adapter != null)
            adapter.delete(viewHolder.getAdapterPosition());
        afficherContacts();
    }
}
