package app.fiance.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import app.fiance.my.R;

public class ActividadAniadirIngresoGasto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_aniadir_ingreso_gasto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void gestionMensualidad(View view){
        float cantidad = 0;
        String nombre = ((EditText) findViewById(R.id.nombreMens)).getText().toString(); // get strings of inputs
        String euros = ((EditText)findViewById(R.id.eurMensualidad)).getText().toString();
        if (!(euros.isEmpty())) {
            cantidad = Float.parseFloat(euros);   // get the float from the string
        }
        aniadirMensualidad(cantidad,nombre); // add the monthly
        regresar(); // return to last activity
    }

    public void aniadirMensualidad(float elementoAniadido, String elementoNombre){
        float mensualidadActual = SimpleFinance.getMensualidad();// total monthly
        float balnaceActual = SimpleFinance.getBalance();

        SimpleFinance.setMensualidades(mensualidadActual + elementoAniadido); // add the new element

        SimpleFinance.setBalance(balnaceActual + elementoAniadido); // also add the element to the balance

        if (elementoAniadido >= 0) { // add element to the correct list
            SimpleFinance.setAddElementF("listaIngresos",elementoNombre,elementoAniadido);
        }else{
            SimpleFinance.setAddElementF("listaGastos",elementoNombre,elementoAniadido);
        }
    }

    public void regresar(){ // return for everyone
        Intent actividadMensuales = new Intent(this,MainActivity2.class);
        startActivity(actividadMensuales);
    }


}