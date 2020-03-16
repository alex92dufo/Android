//Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)
package com.example.colorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.picked_color);

        view.setBackgroundColor(Color.BLACK);

        ColorPickerDialog dialog = new ColorPickerDialog(this);
        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());

        dialog.setOnColorPickedListener((colorPickerDialog, color) ->
                view.setBackgroundColor(colorPickerDialog.getColor()));
    }

}
