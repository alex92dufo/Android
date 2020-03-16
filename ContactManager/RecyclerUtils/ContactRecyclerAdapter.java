package com.demo.contacts.RecyclerUtils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.contacts.Contact;
import com.demo.contacts.DBHelper;
import com.demo.contacts.EditContactActivity;
import com.demo.contacts.MainActivity;
import com.demo.contacts.R;
import com.demo.contacts.databinding.ContactListItemBinding;


public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    Cursor contacts;
    int nom_col_id, prenom_col_id, courriel_col_id, telephone_col_id, favoris_col_id, id_col_id ;



    public ContactRecyclerAdapter(Cursor contacts){
        super();
        this.contacts = contacts;

        //recupération des indices des colonnes de la table "contacts"
        id_col_id  = contacts.getColumnIndex(DBHelper.C_ID);
        nom_col_id  = contacts.getColumnIndex(DBHelper.C_NOM);
        prenom_col_id = contacts.getColumnIndex(DBHelper.C_PRENOM);
        courriel_col_id = contacts.getColumnIndex(DBHelper.C_COURRIEL);
        telephone_col_id = contacts.getColumnIndex(DBHelper.C_TEL);
        favoris_col_id = contacts.getColumnIndex(DBHelper.C_FAV);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ContactListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.contact_list_item, parent, false);

        return new ContactViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        contacts.moveToPosition(position);

        int id = contacts.getInt(id_col_id);
        String nom = contacts.getString(nom_col_id);
        String prenom = contacts.getString(prenom_col_id);
        String courriel = contacts.getString(courriel_col_id);
        String tel = contacts.getString(telephone_col_id);
        int fav = contacts.getInt(favoris_col_id);

        //Les boolean n'existe pas avec SQLite.
        //Conversion de int vers boolean pour pouvoir s'adapter au Switch "Favorites"
        // true = 1, false = 0
        boolean favBool = false;
        if(fav == 1) favBool = true;


        //Création du contact qu'on va bind avec la view d'édition de contact et le list_contact_item
        Contact contact = new Contact();
        contact.setId(id);
        contact.setNom(nom);
        contact.setPrenom(prenom);
        contact.setCourriel(courriel);
        contact.setTelephone(tel);
        contact.setFavoris(favBool);
        contact.setInitials(contact.getPrenom().substring(0,1).toUpperCase() + contact.getNom().substring(0,1).toUpperCase());
        contact.setNomComplet(prenom + " "+ nom);

        //bind avec la view list_contact_item
        holder.bind(contact);


        //short click pour passer un appel avec le numero du contact
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ holder.binding.getContact().getTelephone() ));
                context.startActivity(intent);
            }
        });

        //long click pour éditer le contact
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Context context = v.getContext();

                //passe le contact en extra, pour pouvoir le bind avec la classe d'édition de contact
                Intent intent = new Intent(context, EditContactActivity.class);
                intent.putExtra("contact", holder.binding.getContact());
                context.startActivity(intent);
                notifyDataSetChanged();
                return true;
            }
        });
    }


    public void delete(int position) {

        contacts.moveToPosition(position);
        int id = contacts.getInt(id_col_id);
        DBHelper.deleteContact(id);

        notifyItemRemoved(position);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return contacts.getCount();
    }
}
