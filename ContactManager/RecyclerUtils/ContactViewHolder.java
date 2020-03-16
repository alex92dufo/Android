/*
Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

        Date: 30 avril 2019
        Titre: Application de contact
        Description: Application qui permet d'Ajouter, de mettre a jour ou de supprimer des contacts
*/
package com.demo.contacts.RecyclerUtils;


import android.support.v7.widget.RecyclerView;


import com.demo.contacts.BR;
import com.demo.contacts.Contact;

import com.demo.contacts.databinding.ContactListItemBinding;

class ContactViewHolder extends RecyclerView.ViewHolder {

    ContactListItemBinding binding;

    //Le constructeur recupere le binding
    //On recupere la view avec binding.getRoot()
    ContactViewHolder(ContactListItemBinding binding) {

        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Contact contact) {

        binding.setVariable(BR.contact, contact);
        binding.executePendingBindings();
    }




}
