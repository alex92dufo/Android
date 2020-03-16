package com.example.colorpicker.Views;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class HSeekBar extends AppCompatSeekBar {

    public ShapeDrawable.ShaderFactory sf;


    public HSeekBar(Context context) {
        super(context);
        init();
    }

    public HSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){


        sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient rbGradient = new LinearGradient(0, 0, width, height,
                        new int[] {
                                0xFFff0000,
                                0xFFffff00,
                                0xFF00ffff,
                                0xFF0000ff,
                                0xFFff0000}, //substitute the correct colors for these
                        new float[] {
                                0, 0.25f, 0.50f,0.75f, 1 },
                        Shader.TileMode.REPEAT);
                return rbGradient;
            }
        };

        PaintDrawable color = new PaintDrawable();
        color.setShape(new RectShape());
        color.setShaderFactory(sf);

        setMax(360);
        this.setProgressDrawable(color);
        setOnSeekBarChangeListener(listener);

    }

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            System.out.println("progress = " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

}

