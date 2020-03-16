/*
Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

        Date: 30 avril 2019
        Titre: Application de contact
        Description: Application qui permet d'Ajouter, de mettre a jour ou de supprimer des contacts
*/
package com.demo.contacts.RecyclerUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.demo.contacts.DBHelper;
import com.demo.contacts.R;

public class ContactSwipeCallback extends ItemTouchHelper.SimpleCallback {
    private Drawable icon;
    private int iconWidth;
    private int iconHeight;
    private DBHelper db;
    private ContactRecyclerAdapter myAdapter;
    private RecyclerItemTouchHelperListener listener;

    public ContactSwipeCallback(ContactRecyclerAdapter adapter, Context context, RecyclerItemTouchHelperListener listener) {
        // À implémenter. Vous avez le droit de changer la signature du constructeur.
        super(0, ItemTouchHelper.LEFT);

        myAdapter = adapter;
        icon = context.getDrawable(R.drawable.ic_delete);
        iconWidth = icon.getIntrinsicWidth();
        iconHeight = icon.getIntrinsicHeight();
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
      /*  int position = viewHolder.getAdapterPosition();
        myAdapter.delete(position);
*/
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // Cette méthode gère l'affichace de l'icône "poubelle" lorsqu'on glisse le contach à
        // gauche. IL N'EST PAS NÉCESSAIRE DE LA MODIFIER.

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getBottom() - itemView.getTop();

        ColorDrawable background = new ColorDrawable(Color.LTGRAY);
        background.setBounds(
                itemView.getRight() + (int) dX,
                itemView.getTop(),
                itemView.getRight(),
                itemView.getBottom()
        );
        background.draw(canvas);

        if(isCurrentlyActive) {
            // Calculate position of delete icon
            int iconTop = itemView.getTop() + (itemHeight - iconHeight) / 2;
            int iconBottom = iconTop + iconHeight;
            int iconMargin = (itemHeight - iconHeight) / 2;

            int iconRight = itemView.getRight() - iconMargin;
            int iconLeft = Math.max(itemView.getRight()
                    + (int) dX, itemView.getRight() - iconMargin - iconWidth);
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.draw(canvas);
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
