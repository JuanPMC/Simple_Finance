package app.fiance.my;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class SimpleFinance {

    public static Context context;

    public static float getMensualidades(){
        SharedPreferences sp = context.getSharedPreferences("mensualidad", Context.MODE_PRIVATE); // refetence to sp

        return sp.getFloat("mensualidad",0);    // get the monthly
    }

    public static Map<String,Float> getMap(String tipoDeLista) {
        SharedPreferences sp2 = context.getSharedPreferences(tipoDeLista,Context.MODE_PRIVATE); // get reference to sp
        return (Map<String,Float>)sp2.getAll();
    }

    public static String getLista(String tipoDeLista){
        Map<String,Float> mapaMensual = getMap(tipoDeLista);   // get all values as a map
        String listaValores = "";   // string to store formated values

        for (Map.Entry<String, Float> valor : mapaMensual.entrySet()) { // loop throw the map
            listaValores += valor.getKey() + ": " + valor.getValue() + "\n"; // format each value
        }

        return listaValores; //return formated values
    }
    public static void getFullList(ListView viewAModificar){
        ArrayList<String> listaS = new ArrayList<String>(); // instance an array list for the values
        Map<String,Float> mapaMensualIngresos = getMap("listaIngresos");   // get all values as a map
        Map<String,Float> mapaMensualGastos = getMap("listaGastos");   // get all values as a map

        for (Map.Entry<String, Float> valor : mapaMensualIngresos.entrySet()) { // loop throw the maps and add them to the list
            listaS.add(valor.getKey());
        }
        for (Map.Entry<String, Float> valor : mapaMensualGastos.entrySet()) {
            listaS.add(valor.getKey());
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(context,android.R.layout.simple_list_item_activated_1,listaS); // create an adapter

        viewAModificar.setAdapter(adaptador);   // change the list given
    }

    // deletion methods
    public static void clear(){
        SharedPreferences sp = context.getSharedPreferences("mensualidad",Context.MODE_PRIVATE); // get sp
        SharedPreferences sp2 = context.getSharedPreferences("listaIngresos",Context.MODE_PRIVATE);
        SharedPreferences sp3 = context.getSharedPreferences("listaGastos",Context.MODE_PRIVATE);

        SharedPreferences.Editor ed; // define an editor

        ed = sp.edit(); // clear all the sp
        ed.clear();
        ed.commit();

        ed = sp2.edit();
        ed.clear();
        ed.commit();

        ed = sp3.edit();
        ed.clear();
        ed.commit();

    }
    public static void eliminarElemento(String file,String name){
        SharedPreferences sp = context.getSharedPreferences(file,Context.MODE_PRIVATE); // get sp
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(name);
        ed.commit();
    }
    public static void eliminarIngreso(String name){
        eliminarElemento("listaIngresos",name);
    }
    public static void eliminarGasto(String name){
        eliminarElemento("listaGastos",name);
    }
    // generic getters and setters
    public static void setAnyB(String file, boolean value){
        SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putBoolean(file,value);
        ed.commit();
    }
    public static boolean getAnyB(String file){ // get any float
        SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getBoolean(file,false);
    }
    public static void setAddElementF(String file, String name,float value){
        if (!(name.isEmpty())) {
            SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();

            ed.putFloat(name, value);
            ed.commit();
        }
    }
    public static float getElementF(String file,String name){
        SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getFloat(name,0);
    }
    // Setters and getters
    public static void setMensualidades(float input){
        setAddElementF("mensualidad","mensualidad",input);
    }
    public static float getMensualidad(){
        return getElementF("mensualidad","mensualidad");
    }
    public static void setBalance(float balance){
        setAddElementF("dinero","dinero",balance);
    }
    public static float getBalance(){
        return getElementF("dinero","dinero");
    }
    public static boolean getMonthFlag(){
        return getAnyB("pagadasMensualidades");
    }
    public static void setMonthFlag(boolean value){
        setAnyB("pagadasMensualidades",value);
    }
}
