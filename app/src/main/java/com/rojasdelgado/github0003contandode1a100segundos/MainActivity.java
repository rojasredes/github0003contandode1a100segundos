package com.rojasdelgado.github0003contandode1a100segundos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final int contarhasta=5; //cuento hasta 5, porque contar hasta 100 me hace esperar 100 segundos sin ver nada
    TextView etiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etiqueta=(TextView)findViewById(R.id.etiqueta);
        etiqueta.setText("Vamos a contar de 1 a 100");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        etiqueta.setText("Empezamos...");

        for(int i=1;i<=contarhasta;i++)
        {
            etiqueta.setText(Integer.valueOf(i).toString()); //esto no se ve
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        etiqueta.setText("Terminamos.");
    }
}
