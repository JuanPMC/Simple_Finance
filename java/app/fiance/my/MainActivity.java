package app.fiance.my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.fiance.my.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    float balance; // traking balance

    float x_original,x_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SimpleFinance.context = this; // set the context for sp
        balance = SimpleFinance.getBalance(); // sets the variable balance to the total balance

        // show the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((EditText) findViewById(R.id.editText1)).setVisibility(View.GONE);  // hide test fild
        ((EditText) findViewById(R.id.editText2)).setVisibility(View.GONE);

        setUpKeyboard();

        configurar_mensualidades_boton();

        aplicarMensualidades();// Activitys main function.
        mostrarBalance();
        setupMonthBar();
        setupDineroProgressBar();
        porcentageDineraGastado();
        textSubmitSetUp();
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent){    // override to swipe event

        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:   // action start
                x_original = touchevent.getX();
                break;

            case MotionEvent.ACTION_UP: //action end
                x_final = touchevent.getX();
                if (x_original > x_final){  // get right to left
                    showMonthly();  //  show the monthly activity
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void configurar_mensualidades_boton(){
        Button button_configurar_mensual = (Button) findViewById(R.id.presupuesto_mensual); // Get the monthly elements
        ProgressBar direroProgresBar = (ProgressBar) findViewById(R.id.direroProgresBar);
        TextView dineroMesPorcentage = (TextView) findViewById(R.id.dineroMesPorcentage);

        if (SimpleFinance.getMensualidad() != 0){   // show the correct elements
            button_configurar_mensual.setVisibility(View.GONE);
            direroProgresBar.setVisibility(View.VISIBLE);
            dineroMesPorcentage.setVisibility(View.VISIBLE);
        }else{
            button_configurar_mensual.setVisibility(View.VISIBLE);
            direroProgresBar.setVisibility(View.GONE);
            dineroMesPorcentage.setVisibility(View.GONE);
        }

        button_configurar_mensual.setOnClickListener(new View.OnClickListener() { // setup the listener for the button
            @Override
            public void onClick(View view) {
                showMonthly();
            }
        });

    }
    public void aplicarMensualidades(){
        Calendar cal = Calendar.getInstance(); // instance of calendar
        final boolean dia1 = (cal.get(Calendar.DAY_OF_MONTH) == 1); // Get if is the first of the month
        boolean pagoMensualRealizado = SimpleFinance.getMonthFlag(); // get the pay flag


        if (dia1){   // pay and set pay flag
            if(!pagoMensualRealizado){
                sumarMensualidadMensual();
                SimpleFinance.setMonthFlag(true);
                }
        }else {
            SimpleFinance.setMonthFlag(false);
        }
    }

    // Procces the input data and adds the balance
    private void actualizarValores(){
        final EditText input1 = (EditText)findViewById(R.id.editText1); // references to inputs
        final EditText input2 = (EditText)findViewById(R.id.editText2);
        String input1Str = input1.getText().toString();   // strings from the imputs
        String input2Str = input2.getText().toString();
        final float valorIngrso, valorGasto;    // temp values of gastos and ingresos filds

        if (input1Str.isEmpty()){   // place 0 on empty filds
            input1Str = "0";
        }else if (input2Str.isEmpty()){
            input2Str = "0";
        }

        valorIngrso = Float.parseFloat(input1Str);  // get the value of the filds
        valorGasto = Float.parseFloat(input2Str);

        balance += valorIngrso - valorGasto; // calculate and add to balance

        SimpleFinance.setBalance(balance);   // save total money on sp
        mostrarBalance();   // show the balance

        setupDineroProgressBar();   // update the spending progress bar
        porcentageDineraGastado();

        input1.setText(""); // reset input values
        input2.setText("");
    }

    private void mostrarBalance(){
        final int ORANGE_DARK = getResources().getColor(R.color.colorPrimaryDark); // define colors
        final int ORANGE_LIGHT = getResources().getColor(R.color.colorAccent);

        balance = SimpleFinance.getBalance(); // sets the variable balance to the total balance

        DecimalFormat df = new DecimalFormat("0.00"); // define deciamal format
        TextView balanceDinero = ((TextView) findViewById(R.id.label1)); // reference the balance label

        balanceDinero.setText( df.format(balance) + " EUR"); // write the balance

        if (balance > 0) { // choose the color for the balance
            balanceDinero.setTextColor(ORANGE_DARK);
        }else if (balance < 0){
            balanceDinero.setTextColor(Color.RED);
        }else{
            balanceDinero.setTextColor(ORANGE_LIGHT);
        }
    }

    private void sumarMensualidadMensual(){ // add monthly to balance and save to sp
        balance += SimpleFinance.getMensualidad();
        SimpleFinance.setBalance(balance);
    }


    public void showMonthly(){  // starts the monthly activity
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(i);
    }

    public void setupMonthBar(){ // sets up the month bar
        Calendar cal = Calendar.getInstance(); // Instance of calendar
        ProgressBar monthProgress = (ProgressBar) findViewById(R.id.progressBar1); // reference the progressbar

        setupMonthDate(cal.get(Calendar.DAY_OF_MONTH)); // display the day

        monthProgress.setMax(30);
        monthProgress.setProgress(cal.get(Calendar.DAY_OF_MONTH));
    }

    public void sumarBal(View view){
        editarInput(R.id.editText1);
    }
    public void restarBal(View view){
        editarInput(R.id.editText2);
    }

    public void editarInput(int id){    // eenables user edit input
        EditText input = ((EditText) findViewById(id)); // get references to the elements
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        input.setVisibility(View.VISIBLE); // show input

        input.requestFocus();// Show keyboard
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        input.setText(""); // Delete the data from input
    }

    public void textSubmitSetUp(){
        EditText editText1 = (EditText) findViewById(R.id.editText1); // get references
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        editText1.setOnClickListener(new View.OnClickListener(){ // set up listeners
            @Override
            public void onClick(View view) {
                submitAndHideKeyboard(R.id.editText1);
            }
        });
        editText2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                submitAndHideKeyboard(R.id.editText2);
            }
        });
    }

    public void submitAndHideKeyboard(int id){ // submits the input
        EditText input = (EditText) findViewById(id); // get reference

        actualizarValores(); // submit the content

        input.setVisibility(View.GONE); // hide the input

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // Hide keyboard
        imm.hideSoftInputFromWindow(input.getWindowToken(),0);
    }

    public void setupMonthDate(int dayOfTheMonth){
        TextView monthText = (TextView) findViewById(R.id.diaDelMes);

        monthText.setText("Dia " + dayOfTheMonth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.monthlyBut:
                showMonthly();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupDineroProgressBar(){
        ProgressBar pgb = (ProgressBar) findViewById(R.id.direroProgresBar);
        final float dineroTotalDelMes = SimpleFinance.getMensualidad();

        pgb.setMax((int)dineroTotalDelMes);
        pgb.setProgress((int)(dineroTotalDelMes - balance));

    }
    public void porcentageDineraGastado(){
        TextView tv = (TextView)findViewById(R.id.dineroMesPorcentage);
        final float dineroAgastar = SimpleFinance.getMensualidad();
        final float porcentageDeGasto = (dineroAgastar - balance) * 100 / dineroAgastar;

        tv.setText("Gasto " + (int)porcentageDeGasto + "%");
    }
    public void setUpKeyboard(){ // TODO ...
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if (!b){
                    ((EditText) findViewById(R.id.editText1)).setVisibility(View.GONE);  // hide test fild
                    ((EditText) findViewById(R.id.editText2)).setVisibility(View.GONE);
                }
            }
        });
    }
}