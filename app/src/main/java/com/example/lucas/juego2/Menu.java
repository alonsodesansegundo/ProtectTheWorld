package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Menu extends Pantalla {
    private Boton jugar, ayuda, opciones, records,creditos;
    private int alto, ancho, espacio;
    private String txtCreditos,txtOpciones,txtAyuda,txtRecords;

    public Menu(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);

        //---------------------STRINGS-----------------
        txtCreditos=contexto.getString(R.string.creditos);
        txtOpciones=contexto.getString(R.string.opciones);
        txtRecords=contexto.getString(R.string.records);
        txtAyuda=contexto.getString(R.string.ayuda);

        //-----------------IMAGEN FONDO-----------------
        fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.fondo3);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        alto = altoPantalla / 10;
        ancho = anchoPantalla / 10;
        espacio = altoPantalla / 50;

        //-----------------BOTONES-----------------
        jugar = new Boton(ancho, alto, anchoPantalla-ancho, alto * 2, Color.TRANSPARENT);
        jugar.setTexto("PROTECT THE WORLD",altoPantalla / 20, Color.WHITE,getTypeFace());

        opciones = new Boton(ancho,
                alto * 2 + espacio,
                ancho * 5,
                alto * 2 + espacio + alto
                , Color.TRANSPARENT);
        opciones.setTexto(txtOpciones,altoPantalla/20, Color.WHITE,getTypeFace());
        records = new Boton(anchoPantalla/2,
                alto * 2 + espacio,
                anchoPantalla-ancho,
                alto * 2 + espacio + alto,
                Color.TRANSPARENT);
        records.setTexto(txtRecords,altoPantalla/20, Color.WHITE,getTypeFace());
        ayuda = new Boton(anchoPantalla/2,
                alto * 2 + 2 * espacio + alto,
                anchoPantalla-ancho,
                alto * 2 + 2 * espacio + 2 * alto,
                Color.BLACK);
        ayuda.setTexto(txtAyuda,altoPantalla/20, Color.WHITE,getTypeFace());

        creditos= new Boton(ancho,
                alto * 2 + 2 * espacio + alto,
                anchoPantalla/2,
                alto * 2 + 2 * espacio + 2 * alto,
                Color.BLUE);
        creditos.setTexto(txtCreditos,altoPantalla/20, Color.WHITE,getTypeFace());

        //-----------------MUSICA-----------------
        musica=preferencias.getBoolean("musica",true);
        configuraMusica(R.raw.musica);
        if(musica){
            suenaMusica();
        }else {
            paraMusica();
        }
    }

    // Actualizamos la física de los elementos en pantalla
    public void actualizarFisica() {
    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo
    public void dibujar(Canvas c) {
        try {
            //dibujo la imagen de fondo
            c.drawBitmap(fondo, 0, 0, null);
            //BOTON JUGAR
            //dibujo el boton jugar
            jugar.dibujar(c);

            //BOTON OPCIONES
            //dibujo el boton opciones
            opciones.dibujar(c);

            //BOTON RECORDS
            records.dibujar(c);

            //BOTON AYUDA
            ayuda.dibujar(c);

            creditos.dibujar(c);

        } catch (Exception e) {
            Log.i("Error al dibujar", e.getLocalizedMessage());
        }
    }

    public int onTouchEvent(MotionEvent event) {
       int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                if (pulsa(jugar.getRectangulo(), event)) {
                    jugar.setBandera(true);
                }
                if (pulsa(opciones.getRectangulo(), event)) {
                    opciones.setBandera(true);
                }
                if (pulsa(records.getRectangulo(), event)) {
                    records.setBandera(true);
                }
                if (pulsa(ayuda.getRectangulo(), event)) {
                    ayuda.setBandera(true);
                }
                if(pulsa(creditos.getRectangulo(),event)){
                    creditos.setBandera(true);
                }
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                //si pulso la opcion jugar
                if (pulsa(jugar.getRectangulo(), event) && jugar.getBandera()) {
                    acabaMusica();
                    return 1;
                }
                //si pulso la opcion opciones
                if (pulsa(opciones.getRectangulo(), event)&&opciones.getBandera()) {
                    acabaMusica();
                    return 2;
                }
                if (pulsa(records.getRectangulo(), event)&&records.getBandera()) {
                    acabaMusica();
                    return 3;
                }
                if (pulsa(ayuda.getRectangulo(), event)&&ayuda.getBandera()) {
                    acabaMusica();
                    return 4;
                }
                if(pulsa(creditos.getRectangulo(),event)&&creditos.getBandera()){
                    acabaMusica();
                    return 5;
                }
                //pongo todas las banderas de los botones a false
                jugar.setBandera(false);
                opciones.setBandera(false);
                ayuda.setBandera(false);
                records.setBandera(false);
                creditos.setBandera(false);
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último

                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos
                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }
}