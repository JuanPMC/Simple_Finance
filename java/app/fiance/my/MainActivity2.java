package app.fiance.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import app.fiance.my.R;

import java.text.DecimalFormat;

public class MainActivity2 extends AppCompatActivity {

    float x_original,x_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mesualidades");

        // show monthly
        mostrarMensualidades();
        mostrarMensualidad();

    }

    public void mostrarMensualidad(){
        DecimalFormat df = new DecimalFormat("0.00"); // the decimal format
        float mensualidad = SimpleFinance.getMensualidades(); // get the monthly
        TextView mensualidadTotal = (TextView) findViewById(R.id.mesualidadTotal); // get the text fild

        final String textoFinal = df.format(mensualidad) + " EUR"; // correctly formated text

        mensualidadTotal.setText(textoFinal);  // change the value of the text fild
    }

    public void mostrarMensualidades(){
        TextView ingresos = (TextView) findViewById(R.id.ingresosLabel); // get references to textviews
        TextView perdidas = (TextView) findViewById(R.id.gastosLabel);

        String sIngresos = SimpleFinance.getLista("listaIngresos"); // get the formated strings
        String sPerdidas = SimpleFinance.getLista("listaGastos");

        ingresos.setText(sIngresos);  // set the lists value
        perdidas.setText(sPerdidas);
    }

    public void aniadirDato(View view){
        Intent i = new Intent(this, ActividadAniadirIngresoGasto.class);
        startActivity(i);
    }
    public void quitarDato(View view){
        Intent i = new Intent(this,EliminarMensual.class);
        startActivity(i);
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent){ // get the swiping

        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:   // actionn started
                x_original = touchevent.getX();
                break;

            case MotionEvent.ACTION_UP:     // action ended
                x_final = touchevent.getX();
                if (x_original < x_final){  // detect left to right
                    finish();   // go to previous activity
                }
                break;
            default:
                break;
        }
        return false;
    }

}