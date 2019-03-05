package com.example.lucas.juego2;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class Pantalla {
    private String fuente;
    SharedPreferences preferencias;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    boolean musica;
    Context contexto;
    int idPantalla, altoPantalla, anchoPantalla;
    Bitmap fondo;
    Boolean perdi, pausa;
    Paint pTitulo;
    Vibrator miVibrador;
private Typeface typeFace;
    public Pantalla(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        preferencias = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        miVibrador = (Vibrator) contexto.getSystemService(Context.VIBRATOR_SERVICE);
        fuente="fuentes/miFuente.ttf";
        typeFace= Typeface.createFromAsset(contexto.getAssets(),fuente);
        this.contexto = contexto;
        this.idPantalla = idPantalla;
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        pTitulo = new Paint();
        pTitulo.setTypeface(typeFace);
        pTitulo.setColor(Color.LTGRAY);
        pTitulo.setTextAlign(Paint.Align.CENTER);
        pTitulo.setTextSize(altoPantalla / 10);


    }

    public Typeface getTypeFace() {
        return typeFace;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    // Actualizamos la física de los elementos en pantalla
    public void actualizarFisica() {

    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo
    public void dibujar(Canvas c) {
        try {

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
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último

                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }

    public boolean pulsa(Rect boton, MotionEvent evento) {
        if (boton.contains((int) evento.getX(), (int) evento.getY())) {
            return true;
        } else {
            return false;
        }
    }

    public void configuraMusica(int cancion) {
        mediaPlayer = MediaPlayer.create(contexto, cancion);
        audioManager = (AudioManager) contexto.getSystemService(Context.AUDIO_SERVICE);
       // int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
       // mediaPlayer.setVolume(v / 2, v / 2);

        mediaPlayer.setLooping(true);
        suenaMusica();
        paraMusica();
    }

    public void suenaMusica() {
        mediaPlayer.start();
    }

    public void paraMusica() {
        mediaPlayer.pause();
    }

    public void acabaMusica() {
        mediaPlayer.release();
    }
}
