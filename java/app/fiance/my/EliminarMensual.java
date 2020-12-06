package app.fiance.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import app.fiance.my.R;

public class EliminarMensual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_mensual);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaSetup();
    }

    public void regresar(){
        Intent i = new Intent(this,MainActivity2.class);
        startActivity(i);
    }
    public void eliminarMEnsualidad(String datoAEliminar){
        float valor, totalValor;

        valor = SimpleFinance.getElementF("listaIngresos",datoAEliminar); // the value of the dato
        valor += SimpleFinance.getElementF("listaGastos",datoAEliminar);

        SimpleFinance.eliminarElemento("listaIngresos",datoAEliminar);  // delete the dato
        SimpleFinance.eliminarElemento("listaGastos",datoAEliminar);

        totalValor = SimpleFinance.getMensualidad();    // get the total value of monthly
        totalValor -= valor;                            // calculate the actual value
        SimpleFinance.setMensualidades(totalValor);     // set the value to the actual value

    }
    public void listaSetup(){
        ListView lb = (ListView) findViewById(R.id.listaEle); // reference the list
        SimpleFinance.getFullList(lb);  // get the list

        lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {   // set up listeners
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                eliminarMEnsualidad(adapterView.getItemAtPosition(i).toString());
                regresar();
            }
        });

    }

}