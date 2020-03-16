/*
Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

        Date: 30 avril 2019
        Titre: Application de contact
        Description: Application qui permet d'Ajouter, de mettre a jour ou de supprimer des contacts
*/
package com.demo.contacts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;


import com.demo.contacts.RecyclerUtils.ContactRecyclerAdapter;
import com.demo.contacts.databinding.ContactEditActivityBinding;


public class EditContactActivity extends AppCompatActivity {
    DBHelper dbh;
    ContactEditActivityBinding binding;
    Contact contact;
    int previousActivity;
    private EditText prenom,nomFamille,email,noTelephone;
    private String prenom2,nomFamille2,email2,noTelephone2, prenom3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIds();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit_activity);

        binding = DataBindingUtil.setContentView(this, R.layout.contact_edit_activity);


        //pour differencer l'origine de l'intent
        previousActivity = getIntent().getIntExtra("activity", 0);

        //si = 1, on arrive de MainActivity, après avoir cliqué sur le bouton du topright menu
        //Création d'un nouveau contact
        if(previousActivity == 1){
            setTitle(R.string.new_contact);
            contact = new Contact();
        }
        //sinon on arrive du recyvlerView
        //On modifie un contact deja existant.
        else{
            setTitle(R.string.edit_contact);
            contact = (Contact) getIntent().getSerializableExtra("contact");
        }

        //bind entre la vue et le contact
        binding.setContact(contact);

        if (savedInstanceState != null) {
            prenom2 = savedInstanceState.getString("key1");
            nomFamille2 = savedInstanceState.getString("key2");
            email2 = savedInstanceState.getString("key3");
            noTelephone2 = savedInstanceState.getString("key4");

            setContentView(R.layout.contact_edit_activity);
            getIds();
            prenom.setText(prenom2);
            nomFamille.setText(nomFamille2);
            email.setText(email2);
            noTelephone.setText(noTelephone2);

        }

    }

    private void getIds() {
        prenom = findViewById(R.id.contact_edit_first_name);
        nomFamille = findViewById(R.id.contact_edit_last_name);
        email = findViewById(R.id.contact_edit_mail);
        noTelephone = findViewById(R.id.contact_edit_phone);
    }



    //gestion du bouton "SAVE"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_topright, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save_contact) {

            DBHelper dbh = new DBHelper(this);

            //si on est sur "New contact"
            if(previousActivity == 1){
                dbh.insertContact(binding.getContact());

            }
            //si on a cliqué sur le recyclerView
            else {
                dbh.modifContact(binding.getContact());
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getIds();

        outState.putString("key1", prenom.getText().toString());
        outState.putString("key2", nomFamille.getText().toString());
        outState.putString("key3", email.getText().toString());
        outState.putString("key4", noTelephone.getText().toString());

    }





}
