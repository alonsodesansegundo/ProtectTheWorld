package com.example.lucas.juego2;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Esta clase se encarga de dibujar el propio juego y gestionar su funcionalidad
 * @author Lucas Alonso de San Segundo
 */
public class Gameplay extends Pantalla {
    //------------------------PROPIEDADES GAMEPLAY------------------------
    //------------------------FONDO------------------------
    /**
     * Array de fondos para dar la sensación de movimiento
     */
    private Fondo[] fondo;
    
    /**
     * Bitmap (imagen) que será utilizada para el fondo del juego
     */
    private Bitmap bitmapFondo;
    
    /**
     * Entero que representa la probabilidad de disparo que tendrán los marcianos, será del 33%
     */
    private int probabilidadDisparoMarcianos;
    
    /**
     * Booleanas de control
     */
    private boolean empece, perdi, pausa,estoyJugando;
    
    /**
     * Entero que se utilizará para rellenar los marcianos según el nivel en el que estemos
     */
    private int nivel;
    
    /**
     * Entero que representa la cantidad de filas de marcianos que habrá
     */
    private int filas;
    
    /**
     * Entero que representa la cantidad de columnas de marcianos que habrá
     */
    private int columnas;
    
    /**
     * Entero que representa la puntuación del jugador
     */
    private int puntuacionGlobal;
    
    /**
     * Bitmap (imagen) que tendrán los marcianos de un impacto
     */
    private Bitmap imgMarciano1;
    
    /**
     * Bitmap (imagen) que tendrán los marcianos de dos impactos
     */
    private Bitmap imgMarciano2;
    
    /**
     * Bitmap (imagen) que tendrá nuestra nave
     */
    private Bitmap imgNave;
    
    /**
     * Bitmap (imagen) que tendrán los proyectiles o balas de los marcianos
     */
    private Bitmap proyectilMarciano;
    
    /**
     * Bitmap (imagen) que tendrá el proyectil o bala de nuestra nave
     */
    private Bitmap balaNave;
    
    /**
     * Bitmap (imagen) que se representará la explosión de nuestra nave en el momento que perdamos
     */
    private Bitmap  explosion;
    
    /**
     * Float que servirá para situar los marcianos respecto al eje X
     */
    private float primeraX;
    
    /**
     * Float que servirá para situar los marcianos respecto al eje Y
     */
    private float primeraY;
    
    /**
     * Float que representa el tamaño del texto de la puntuación
     */
    private float tamanhoPuntuacion;
    
    /**
     * Double que representa la velocidad de movimiento lateral de los marcianos
     */
    private double vMarciano;
    
    /**
     * Double que representa la velocidad de la bala de nuestra nave
     */
    private double vBala;
    
    /**
     * Double que representa la velocidad de movimiento de las balas de los marcianos
     */
    private double vBalaMarciano;
    
    /**
     * Double que representa la velocidad de movimiento del fondo
     */
    private double vFondo;
    
    /**
     * Array bidimensional donde estarán situados nuestros Marcianos
     */
    private Marciano marcianos[][];
    
    /**
     * Objeto nave que será con el que jugaremos
     */
    private Nave miNave;
    
    /**
     * Booleana que nos sirve para saber en que dirección se tienen que mover los marcianos
     */
    private boolean voyIzquierda;
    
    /**
     * Booleana que nos sirve para saber si los marcianos tienen que descender un nivel o no
     */
    private boolean voyAbajo;
    
    /**
     * Booleana para el movimiento de la nave a través de la pulsación en la pantalla
     */
    private boolean mueveNave;
    
    /**
     * ArrayList auxiliar que me servirá para que solamente puedan disparar los ultimos marcianos de cada columna
     */
    private ArrayList misColumnas;
    
    /**
     * ArrayList en el que estarán guardadas todas las balas de los marcianos
     */
    private ArrayList<BalaMarciano> balasMarcianos;
    
    /**
     * Paint que se utilizará para la puntuación del juegador
     */
    private Paint pPunutacion;
    
    /**
     * Entero que representa en milisegundos el tiempo que va a vibrar el dispositivo en el caso de haber perdido y que la vibración esté activada
     */
    private int tiempoVibracion;
    
    /**
     * Objeto botón que nos permite pausar la partida
     */
    private Boton btnPausa;
    
    /**
     * Objeto botón que nos permite reanudar la partida
     */
    private Boton btnReanudar;
    
    /**
     * Objeto botón que nos permite salir de la partida y volver al menu principal
     */
    private Boton btnSalir;
    
    /**
     * Objeto botón que nos permite activar la música en el caso de que esté desactivada, y viceversa
     */
    private Boton btnMusica;
    
    /**
     * Objeto botón que nos permite iniciar el juego
     */
    private Boton btnJugar;
    
    /**
     * Objeto botón que nos permite no iniciar el juego y volver al menú principal
     */
    private Boton btnNoJugar;
    
    /**
     * Objeto botón que nos permite repetir partida
     */
    private Boton btnSi;
    
    /**
     * Objeto botón que nos permite regresar al menú principal una vez hayamos acabado nuestra partida
     */
    private Boton btnNo;
    
    /**
     * Objeto botón auxiliar que nos servirá para establecer como no pulsado una vez levantamos el dedo de la pantalla
     */
    private Boton btnAux;
    
    /**
     * Bitmap (imagen) para el boton pausa, mientras estemos jugando
     */
    private Bitmap imgPausa;
    
    /**
     * Bitmap (imagen) para el boton pausa, mientras estemos en pausa
     */
    private Bitmap imgPlay;
    
    /**
     * Bitmap (imagen) para el boton de la musica, mientras la musica no está sonando
     */
    private Bitmap imgMusicaOn;
    
    /**
     * Bitmap (imagen) para el boton de la musica, mientras la musica está sonando
     */
    private Bitmap imgMusicaOff;
    
    /**
     * Entero que nos servirá para saber con que nave vamos a jugar
     */
    private int codNave;
    
    /**
     * Entero que representa el margen lateral que habrá la hora de dibujar diferentes "menus", como el menu de pausa
     */
    private int margenLateralPausa;
    
    /**
     * Entero que utilizaré para dibujar diferentes "menus", como por ejemplo el menu pausa
     */
    private int altoMenuPausa;
    
    /**
     * Entero en el que estará la puntuación más baja más reciente
     */
    private int ultimaPuntuacion;
    
    /**
     * Objeto SharedPreferences.Editor para poder cambiar el archivo de configuración
     */
    private SharedPreferences.Editor editorPreferencias;
    
    /**
     * Objeto SharedPreferences
     */
    private SharedPreferences preferencias;
    
    /**
     * Booleana de configuración, si está a true el dispositivo vibrará en el momento que perdamos, y al contario
     */
    private boolean vibracion;
    
    /**
     * Booleana de configuración que nos indicará cómo se podrá mover la nave, en el caso de estar a true se moverá a traves del sensor de gravedad, y de lo contrario, se moverá con el movimiento del dedo por la pantalla
     */
    private boolean gravedad;
    

    //efectos sonoros
    /**
     * Objeto soundPool para poder repdroducir el sonido de la explosión de la nave
     */
    private SoundPool efectos;
    
    /**
     * Entero que representa el sonido que se tiene que reproducir cuando hemos sido eliminados
     */
    private int  sonidoMuereNave;
    
    /**
     * Entero que representa el numero maximo de efectos sonoros que puede haber
     */
    private int maxSonidosSimultaneos;

    //para las siglas
    /**
     * Entero auxiliar que utilizaremos para desplazar una sigla
     */
    private int pos;
    
    /**
     * Entero que representa el tamaño de las siglas
     */
    private int tamanhoSiglas;
    
    /**
     * ArrayList en el que estarán todas las letras del abecedario
     */
    private ArrayList<Character> abecedario;
    /**
     * Booleanas de control
     */
    private boolean pideSiglas, tengoSiglas, hiceInsert;
    
    /**
     * Array de char en el que guardaré las siglas del usuario
     */
    private char[] siglas;
    
    /**
     * Entero utilizado para dibujar el menú iniciales
     */
    private int altoMenuIniciales;
    
    /**
     * Objeto boton superior de la primera sigla
     */
    private Boton btnSiglaArriba;
    
    /**
     * Objeto boton inferior de la primera sigla
     */
    private Boton btnSiglaAbajo;
    
    /**
     * Objeto boton superior de la segunda sigla
     */
    private Boton btnSigla2Arriba;
    
    /**
     * Objeto boton inferior de la segunda sigla
     */
    private Boton btnSigla2Abajo;
    
    /**
     * Objeto boton superior de la tercera sigla
     */
    private Boton btnSigla3Arriba;
    
    /**
     * Objeto boton inferior de la tercera sigla
     */
    private Boton btnSigla3Abajo;
    
    /**
     * Objeto boton con el que poder realizar la inserccion de nuestras siglas a la base de datos, como poder eliminar la puntuacion mas baja mas reciente
     */
    private Boton btnEnviar;

    /**
     * Bitmap (imagen) utiziada para los botones que estan situados en la parte superior de las siglas
     */
    private Bitmap trianguloArriba;

    /**
     * Bitmap (imagen) utiziada para los botones que estan situados en la parte inferior de las siglas
     */
    private Bitmap trianguloAbajo;
    

    //sensor de gravedad
    /**
     * Objeto sensor que será el sensor de gravedad en el caso de haber seleccionado jugar moviendo el movl
     */
    private Sensor sensorGravedad;

    /**
     * Objeto sensor manager
     */
    private SensorManager sensorManager;

    /**
     * Objeto SensorEventListener del sensor de gravedad
     */
    private SensorEventListener escuchaGravedad;

    //para la bd
    /**
     * Entero que representa el id maximo antes de realizar la inserccion y el delete
     */
    private int ultimoId;

    /**
     * Cadena (query) para obtener los datos de la puntuación más baja más reciente
     */
    private String consultaUltima;

    /**
     * Cadena (query) con la que obtengo el id mas alto antes de realizar el delete y el insert
     */
    private String consultaId;

    /**
     * Objeto BaseDeDatos
     */
    private BaseDeDatos bd;

    /**
     * Objeto SQLiteDatabase
     */
    private SQLiteDatabase db;

    /**
     * Objeto Cursor
     */
    private Cursor c;

    /**
     * Cadena que representará la palabra continuar
     */
    private String txtContinuar;

    /**
     * Cadena que representará la palabra salir
     */
    private String txtSalir;

    /**
     * Cadena que representará la palabra accion
     */
    private String txtAccion;

    /**
     * Cadena que representará el texto ¿Estás listo?
     */
    private String txtEmpezar;

    /**
     * Cadena que representará la palabra Si
     */
    private String txtSi;

    /**
     * Cadena que representará el texto ¿Otra partida?
     */
    private String txtRepetir;

    /**
     * Cadena que representará la palabra No
     */
    private String txtNo;

    /**
     * Cadena que representará el texto Introduce tu siglas
     */
    private String txtSiglas;

    /**
     * Cadena que representará la palabra Enviar
     */
    private String txtEnviar;
    //timer para disparo marcianos

    /**
     * Objeto TimerTask para el disparo de los marcianos
     */
    private TimerTask task;

    /**
     * Objeto Timer para el disparo de los marcianos
     */
    private Timer miTimer;

    //------------------------CONSTRUCTOR------------------------

    /**
     * Constructor de la pantalla Gameplay
     * @param contexto      Objeto contexto
     * @param idPantalla    Entero que representa el id de esta pantalla
     * @param anchoPantalla Entero que representa el ancho de la pantalla
     * @param altoPantalla  Entero que representa el alto de la pantalla
     */
    public Gameplay(Context contexto, int idPantalla, final int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        perdi = false;
        bitmapFondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.universe2);
        bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, anchoPantalla, bitmapFondo.getHeight(), true);
        fondo = new Fondo[2];
        fondo[0] = new Fondo(bitmapFondo, altoPantalla);
        fondo[1] = new Fondo(bitmapFondo, 0, fondo[0].posicion.y - bitmapFondo.getHeight());


        vFondo = altoPantalla * 0.001;
        vBala = altoPantalla * 0.01;
        vBalaMarciano = vBala / 3;
        //efectos sonoros
        maxSonidosSimultaneos = 10;
        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(maxSonidosSimultaneos);
            this.efectos = spb.build();
        } else {
            this.efectos = new SoundPool(maxSonidosSimultaneos, AudioManager.STREAM_MUSIC, 0);
        }

        sonidoMuereNave = efectos.load(contexto, R.raw.muerenave, 1);
        estoyJugando = false;
        miTimer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (estoyJugando) {
                    disparanMarcianos();
                }
            }
        };
        // Empezamos dentro de 10ms y luego lanzamos la tarea cada 1000ms
        miTimer.schedule(task, 2000, 2000);

        probabilidadDisparoMarcianos = 33;
        empece = false;
        pausa = false;
        perdi = false;
        pideSiglas = false;
        hiceInsert = false;
        //----------------STRINGS----------------
        txtContinuar = contexto.getString(R.string.continuar);
        txtSalir = contexto.getString(R.string.salir);
        txtAccion = contexto.getString(R.string.accion);
        txtEmpezar = contexto.getString(R.string.listo);
        txtSi = contexto.getString(R.string.si);
        txtRepetir = contexto.getString(R.string.repetir);
        txtNo = contexto.getString(R.string.no);
        txtSiglas = contexto.getString(R.string.siglas);
        txtEnviar = contexto.getString(R.string.enviar);

        //----------------ABECEDARIO----------------
        abecedario = new ArrayList<Character>();
        for (int i = 0; i < 26; i++) {
            abecedario.add((char) ('A' + i));
        }

        //----------------SIGLAS INICIALES----------------
        siglas = new char[3];
        siglas[0] = abecedario.get(0);
        siglas[1] = abecedario.get(0);
        siglas[2] = abecedario.get(0);

        tamanhoSiglas = altoPantalla / 12;
        //----------------BOTONES SIGLAS----------------

        //----------------IMAGENES TRIANGULOS----------------
        trianguloAbajo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.triangulodown);
        trianguloAbajo = Bitmap.createScaledBitmap(trianguloAbajo, anchoPantalla / 10, anchoPantalla / 10, true);

        trianguloArriba = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.trianguloup);
        trianguloArriba = Bitmap.createScaledBitmap(trianguloArriba, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------BOTONES SIGLA 1----------------
        btnSiglaArriba = new Boton(anchoPantalla / 20 + trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 - trianguloArriba.getHeight() / 2,
                anchoPantalla / 20 + trianguloArriba.getWidth() + trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 + trianguloArriba.getHeight() / 2, Color.TRANSPARENT);
        btnSiglaArriba.setImg(trianguloArriba);

        btnSiglaAbajo = new Boton(anchoPantalla / 20 + trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100 - trianguloArriba.getHeight(),
                anchoPantalla / 20 + trianguloArriba.getWidth() + trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100, Color.TRANSPARENT);
        btnSiglaAbajo.setImg(trianguloAbajo);

        //----------------BOTONES SIGLA 2----------------
        btnSigla2Abajo = new Boton(anchoPantalla / 2 - trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100 - trianguloArriba.getHeight(),
                anchoPantalla / 2 + trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100, Color.TRANSPARENT);
        btnSigla2Abajo.setImg(trianguloAbajo);

        btnSigla2Arriba = new Boton(anchoPantalla / 2 - trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 - trianguloArriba.getHeight() / 2,
                anchoPantalla / 2 + trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 + trianguloArriba.getHeight() / 2, Color.TRANSPARENT);
        btnSigla2Arriba.setImg(trianguloArriba);

        //----------------BOTONES SIGLA 3----------------
        btnSigla3Arriba = new Boton(anchoPantalla - trianguloArriba.getWidth() * 2,
                altoPantalla / 2 - altoPantalla / 50 - trianguloArriba.getHeight() / 2,
                anchoPantalla - trianguloArriba.getWidth(),
                altoPantalla / 2 - altoPantalla / 50 + trianguloArriba.getHeight() / 2, Color.TRANSPARENT);
        btnSigla3Arriba.setImg(trianguloArriba);

        btnSigla3Abajo = new Boton(anchoPantalla - trianguloArriba.getWidth() * 2,
                altoPantalla / 3 * 2 - altoPantalla / 100 - trianguloArriba.getHeight(),
                anchoPantalla - trianguloArriba.getWidth(),
                altoPantalla / 3 * 2 - altoPantalla / 100, Color.TRANSPARENT);
        btnSigla3Abajo.setImg(trianguloAbajo);

        altoMenuIniciales = altoPantalla / 3;

        //----------------BTN ENVIAR----------------
        btnEnviar = new Boton(anchoPantalla / 2 - anchoPantalla / 10,
                altoPantalla / 3 * 2 + altoPantalla / 20,
                anchoPantalla / 2 + anchoPantalla / 10,
                altoPantalla / 3 * 2 + altoPantalla / 20 * 2, Color.GREEN);

        btnEnviar.setTexto(txtEnviar, altoPantalla / 40, Color.BLACK, getTypeFace());
        //-----------------MENU PAUSA----------------
        margenLateralPausa = anchoPantalla / 20;
        altoMenuPausa = altoPantalla / 4;

        //----------------ARCHIVO CONFIGURACIÓN--------------
        preferencias = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        editorPreferencias = preferencias.edit();
        //--------------BOOLEAN gravedad--------------
        gravedad = preferencias.getBoolean("gravedad", false);
        if (gravedad) {
            sensorManager = (SensorManager) contexto.getSystemService(Context.SENSOR_SERVICE);

            sensorGravedad = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            escuchaGravedad = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // More code goes here
                    if (estoyJugando) {
                        float nuevaPos = anchoPantalla / 2 - sensorEvent.values[0] * 100;
                        if (nuevaPos >= 0 && nuevaPos <= anchoPantalla) {
                            miNave.moverNave(nuevaPos);
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
            // Register the listener
            sensorManager.registerListener(escuchaGravedad,
                    sensorGravedad, SensorManager.SENSOR_DELAY_NORMAL);
        }

        //--------------BOOLEAN VIBRACION--------------
        vibracion = preferencias.getBoolean("vibracion", true);
        //----------------BOTON PAUSA----------------
        btnPausa = new Boton(anchoPantalla - anchoPantalla / 10, 0,
                anchoPantalla, anchoPantalla / 10, Color.TRANSPARENT);

        //----------------IMAGEN PAUSA----------------
        imgPausa = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.pause);
        imgPausa = Bitmap.createScaledBitmap(imgPausa, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------SET IMAGEN PAUSA----------------
        btnPausa.setImg(imgPausa);

        //----------------IMAGEN PLAY----------------
        imgPlay = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.play);
        imgPlay = Bitmap.createScaledBitmap(imgPlay, anchoPantalla / 10, anchoPantalla / 10, true);


        //----------------BOTON MÚSICA----------------
        btnMusica = new Boton(anchoPantalla - anchoPantalla / 10 * 2, 0,
                anchoPantalla - anchoPantalla / 10, anchoPantalla / 10, Color.TRANSPARENT);

        //----------------IMAGEN MUSICA ON----------------
        imgMusicaOn = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.musica);
        imgMusicaOn = Bitmap.createScaledBitmap(imgMusicaOn, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------IMAGEN MUSICA OFF----------------
        imgMusicaOff = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.musicano);
        imgMusicaOff = Bitmap.createScaledBitmap(imgMusicaOff, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------MUSICA----------------
        musica = preferencias.getBoolean("musica", true);
        configuraMusica(R.raw.game);
        if (musica) {
            btnMusica.setImg(imgMusicaOff);
            suenaMusica();
        } else {
            btnMusica.setImg(imgMusicaOn);
            paraMusica();
        }
        //----------------MILISEGUNDOS VIBRACIÓN----------------
        tiempoVibracion = 1000;

        //----------------ALTO DE LA PUNTUACIÓN----------------
        tamanhoPuntuacion = altoPantalla / 15;
        //-----------------PAINT PUNTUACIÓN-----------------
        pPunutacion = new Paint();
        pPunutacion.setTypeface(getTypeFace());
        pPunutacion.setColor(Color.WHITE);
        pPunutacion.setTextAlign(Paint.Align.CENTER);
        pPunutacion.setTextSize(tamanhoPuntuacion);

        //-----------------PUNTUACIÓN GLOBAL-----------------
        puntuacionGlobal = 0;

        //-----------------POSICIÓN PRIMER MARCIANO-----------------
        primeraX = 0;
        primeraY = 0;
        //al comienzo los marcianos se moverán hacia la derecha
        voyIzquierda = false;
        //al comienzo los marcianos no irán hacia abajo ya
        voyAbajo = false;
        //al comienzo la nave todavía no se mueve
        mueveNave = false;
        //-----------------NIVEL INICIAL-----------------
        nivel = 0;
        //-----------------FILAS Y COLUMNAS DE MARCIANOS-----------------
        filas = 5;
        columnas = 6;
        marcianos = new Marciano[filas][columnas];  //cinco filas seis columnas de marcianos
        //velocidad de movimiento lateral de los marcianos al comienzo
        vMarciano = anchoPantalla * 0.001;
        //imagen marcianos impacto 1
//        imgMarciano1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.marciano1);
//        imgMarciano1 = Bitmap.createScaledBitmap(imgMarciano1, anchoPantalla / 20, altoPantalla / 30, true);

        imgMarciano1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.mio1);
        imgMarciano1 = Bitmap.createScaledBitmap(imgMarciano1, anchoPantalla / 20, altoPantalla / 30, true);


        //imagen marcianos impacto 2
//        imgMarciano2 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.marciano2);
//        imgMarciano2 = Bitmap.createScaledBitmap(imgMarciano2, anchoPantalla / 20, altoPantalla / 30, true);

        imgMarciano2 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.mio2);
        imgMarciano2 = Bitmap.createScaledBitmap(imgMarciano2, anchoPantalla / 20, altoPantalla / 30, true);


        proyectilMarciano = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.bombamarciano);
        proyectilMarciano = Bitmap.createScaledBitmap(proyectilMarciano, anchoPantalla / 20, altoPantalla / 30, true);


        balaNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.proyectilnave);
        balaNave = Bitmap.createScaledBitmap(balaNave, anchoPantalla / 30, altoPantalla / 20, true);
        //llenar de marcianos el array bidimensional
        rellenaMarcianos();

        //imagen de la nave
        codNave = preferencias.getInt("idNave", 1);
        switch (codNave) {
            case 0:
                imgNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave);
                imgNave = Bitmap.createScaledBitmap(imgNave, anchoPantalla / 10, altoPantalla / 15, true);
                break;
            case 1:
                imgNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave1);
                imgNave = Bitmap.createScaledBitmap(imgNave, anchoPantalla / 10, altoPantalla / 15, true);
                break;
            case 2:
                imgNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave2);
                imgNave = Bitmap.createScaledBitmap(imgNave, anchoPantalla / 10, altoPantalla / 15, true);
        }

        explosion = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.explosion);
        explosion = Bitmap.createScaledBitmap(explosion, anchoPantalla / 10, altoPantalla / 15, true);
        //creo el objeto nave
        miNave = new Nave(imgNave, anchoPantalla / 2 - imgNave.getWidth() / 2, altoPantalla - imgNave.getHeight(),
                vBala, balaNave);

        //arraylist con las balas generadas por los marcianos
        balasMarcianos = new ArrayList<BalaMarciano>();

        //arraylist auxiliar para que disparen solamente los ultimos marcianos de cada columna
        misColumnas = new ArrayList();

        //----------------BTN REANUDAR----------------
        btnReanudar = new Boton(margenLateralPausa * 2, altoPantalla / 2,
                anchoPantalla / 2 - margenLateralPausa,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.GREEN);
        btnReanudar.setTexto(txtContinuar, altoPantalla / 30, Color.BLACK, getTypeFace());

        //----------------BTN SALIR----------------
        btnSalir = new Boton(anchoPantalla / 2 + margenLateralPausa, altoPantalla / 2,
                anchoPantalla - margenLateralPausa * 2,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.RED);
        btnSalir.setTexto(txtSalir, altoPantalla / 30, Color.BLACK, getTypeFace());

        //----------------BTN PARA EMPEZAR A JUGAR----------------
        btnJugar = new Boton(anchoPantalla / 2 - anchoPantalla / 4, altoPantalla / 2,
                anchoPantalla / 2, altoPantalla / 2 + altoPantalla / 11, Color.GREEN);
        btnJugar.setTexto(txtSi, altoPantalla / 15, Color.BLACK, getTypeFace());

        btnNoJugar = new Boton(anchoPantalla / 2, altoPantalla / 2,
                anchoPantalla / 2 + anchoPantalla / 4, altoPantalla / 2 + altoPantalla / 11, Color.RED);
        btnNoJugar.setTexto(txtNo, altoPantalla / 15, Color.BLACK, getTypeFace());

        //----------------BTN PARA REPETIR O NO----------------
        btnSi = new Boton(margenLateralPausa * 2, altoPantalla / 2,
                anchoPantalla / 2 - margenLateralPausa,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.GREEN);
        btnSi.setTexto(txtSi, altoPantalla / 30, Color.BLACK, getTypeFace());


        btnNo = new Boton(anchoPantalla / 2 + margenLateralPausa, altoPantalla / 2,
                anchoPantalla - margenLateralPausa * 2,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.RED);
        btnNo.setTexto(txtNo, altoPantalla / 30, Color.BLACK, getTypeFace());
    }

    /**
     * Método encargado de gestionar la física de los elementos de la pantalla. Es decir, mover los marcianos, las balas...
     */
    public void actualizarFisica() {
        mueveFondo();

        //si no he pausado, el gameplay continua
        if (estoyJugando) {
            //------------------------DISPARO DE LA NAVE------------------------
            disparaNave();

            //------------------------DISPARO DE LOS MARCIANOS------------------------
            //lo hago a traves del timer en el constructor


            //------------------------MOVER BALAS MARCIANOS (ARRAYLIST)------------------------
            actualizaBalasMarcianos();

            //------------------------MOVIMIENTO VERTICAL Y HORIZONTAL DE LOS MARCIANOS------------------------

            //VEO EN QUE DIRECCIÓN SE TIENEN QUE MOVER Y SI DESCIENDEN UN NIVEL O NO Y ACTUALIZO LAS BANDERAS
            actualizaBanderasMovimiento();

            //MUEVO LOS MARCIANOS SEGÚN LAS BANDERAS
            mueveMarcianos();
        }

        if (tengoSiglas && !hiceInsert) {
            insertPuntuacion();
            perdi = true;
        }
    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo

    /**
     * Método encarga de dibujar los diferentes elementos con los que tiene el juego, dependiendo de la situacion actual
     *
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujar(Canvas c) {
        try {
            dibujaFondo(c);
            //si he empezado a jugar
            if (estoyJugando) {
                dibujaJuego(c);
            } else {
                //si aun no he empezado a jugar
                dibujaInicio(c);
            }
            //si he pulsado el boton de pausa
            if (pausa) {
                dibujaJuego(c);
                dibujaPausa(c);
            }
            //si he perdido
            if (perdi) {
                dibujaJuego(c);
                dibujaPerdi(c);
            }
            if (pideSiglas && !tengoSiglas) {
                dibujaJuego(c);
                dibujaPideSiglas(c);
            }
        } catch (Exception e) {
            Log.i("Error al dibujar", e.getLocalizedMessage());
        }
    }

    /**
     * Método que será llamado cuando no haya más marcianos en el arraybidimensional. Incrementa el nivel y se encarga de rellenar de marcianos el array bidimensional dependiendo del nivel en el que estemos. También se encarga de aumentar la velocidad de los marcianos en el momento adecuado
     */
    public void rellenaMarcianos() {
        //incremento el nivel
        nivel++;
        //recorro las filas
        for (int i = 0; i < marcianos.length; i++) {
            //incremento la pos y
            primeraY += imgMarciano1.getHeight() + altoPantalla / 20;
            //recorro las columnas
            for (int j = 0; j < marcianos[0].length; j++) {
                //dependiendo del nivel y de la fila en la que esté
                //pongo un marciano nivel 1 o marciano nivel 2
                //por ejemplo, si estoy en la ultima fila y en el nivel 2-1, sera de marcianos de dos impactos
                if (i >= marcianos.length - (nivel - 1)) {
                    marcianos[i][j] = new Marciano(imgMarciano2, primeraX, primeraY, 2, vMarciano, 25);
                } else {
                    marcianos[i][j] = new Marciano(imgMarciano1, primeraX, primeraY, 1, vMarciano, 10);
                }
                //aumento la posX
                primeraX += imgMarciano1.getWidth() + anchoPantalla / 10;
            }
            //una vez recorro todas las columnas de la fila actual
            primeraX = 0;
        }
        //una vez recorro todas las filas y columnas
        primeraY = 0;
        //si el nivel - 1 es igual al numero de filas, es decir, he llegado a cubrir la pantalla de marcianos de dos impactos
        if (nivel - 1 == filas) {
            nivel = 0;
            vMarciano = vMarciano * 2;
        }
    }

    //devuelvo true si encuentro algun marciano, devuelvo false si no hay 7ningun marciano

    /**
     * Método que utilizaremos para saber si todavía hay marcianos en el arraybidimensional o no
     *
     * @return True si todavía hay algún marciano. False en el caso de que no haya ningún marciano.
     */
    public boolean hayMarcianos() {
        for (int i = 0; i < marcianos.length; i++) {
            for (int j = 0; j < marcianos[0].length; j++) {
                if (marcianos[i][j] != null) {
                    return true;
                }
            }
        }
        //si no he encontrado ningun null
        return false;
    }

    /**
     * Método que se encarga de vacíar el array en el que están las balas de los marcianos. Será llamado una vez no haya marcianos en el array bidimensional
     */
    public void vaciaBalas() {
        balasMarcianos.clear();
    }

    /**
     * Método encargado de mover el fondo mientras estemos jugando
     */
    public void mueveFondo() {
        if (estoyJugando) {
            // Movemos
            fondo[0].mover(vFondo);
            fondo[1].mover(vFondo);
// Comprobamos que se sobrepase la pantalla y reiniciamos
            if (fondo[0].posicion.y > altoPantalla) {
                fondo[0].posicion.y = fondo[1].posicion.y - fondo[0].imagen.getHeight();
            }
            if (fondo[1].posicion.y > altoPantalla) {
                fondo[1].posicion.y = fondo[0].posicion.y - fondo[1].imagen.getHeight();
            }
        }
    }
    //------------------------DISPARO DE LA NAVE------------------------

    /**
     * Método encargado de la bala de la nave. Se encarga de generarla y hacerla desaparecer, o bien porque ha salido de la pantalla, porque ha chocado con una bala de un marciano o porque ha chocado con un marciano.
     */
    public void disparaNave() {
        //solo habrá una bala de la nave en la pantalla
        //si hay bala, la muevo
        if (miNave.getHayBala()) {
            //acutalizo las posiciones del proyectil
            miNave.actualizaProyectil();
            //recorro los marcianos, para ver si alguno choca, en el caso de ser asi miNave.hayBala = false
            for (int i = 0; i < marcianos.length; i++) {
                for (int j = 0; j < marcianos[0].length; j++) {
                    //si hay un marciano
                    if (marcianos[i][j] != null) {
                        // si la bala impacta con un marciano
                        if (marcianos[i][j].colisiona(miNave.getBala())) {
                            //le resto uno de salud al marciano
                            marcianos[i][j].setSalud(marcianos[i][j].getSalud() - 1);
                            //si salud es cero
                            if (marcianos[i][j].getSalud() == 0) {

                                //sumo a mi puntuacion global los puntos del marciano
                                puntuacionGlobal += marcianos[i][j].getPuntuacion();
                                //elimino el marciano
                                marcianos[i][j] = null;
                            } else {
                                //si continua con salud
                                //cambio la imagen del marciano por una de marciano nivel 1
                                marcianos[i][j].setImagen(imgMarciano1);
                            }
                            //quito la bala
                            miNave.setHayBala(false);

                            //si no hay marcianos
                            //relleno el array segun el nivel, de ello se encarga rellena marcianos
                            if (!hayMarcianos()) {
                                vaciaBalas();
                                rellenaMarcianos();
                            }
                            //salgo del bucle porque no hace falta seguir recorriendo todos los marcianos, ya que solo es posible que haya un impacto
                            break;
                        }
                    }
                }
            }
        } else {
            //si no hay bala, la genero
            miNave.disparar();

        }
    }

    //------------------------SONIDOS DISPARO NAVE Y MUERE NAVE------------------------

    /**
     * Método que se encarga de reproducir el sonido de explosión en el momento que somos eliminados
     */
    public void suenaMuereNave() {
        int v = getAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC);
        efectos.play(sonidoMuereNave, v, v, 1, 0, 1);
    }

    //------------------------DISPARO DE LOS MARCIANOS------------------------

    /**
     * Método que se encarga de que los marcianos puedan disparan. Podrán disparar los marcianos situados más abajo de cada columna.
     */
    public void disparanMarcianos() {
        //solo podrán disparar los últimos marcianos de cada columna
        //recorro las filas de abajo a arriba
        for (int i = marcianos.length - 1; i >= 0; i--) {
            //recorro las columnas de izq a drch
            for (int j = 0; j < marcianos[0].length; j++) {
                //si esa columna no está en mi arraylist de columnas y en la posicion actual hay un marciano
                if (misColumnas.indexOf(j) == -1 && marcianos[i][j] != null) {
                    //añado la columna a mi arraylist
                    misColumnas.add(j);
                    //si se decide que el marciano dispare (porque sale x probabilidad)
                    if (marcianos[i][j].dispara(probabilidadDisparoMarcianos)) {
                        //genero una nueva bala marciano que añado a su array
                        balasMarcianos.add(new BalaMarciano((int) marcianos[i][j].getContenedor().centerX() -
                                proyectilMarciano.getWidth() / 2,
                                (int) marcianos[i][j].getPos().y + marcianos[i][j].getImagen().getHeight(),
                                proyectilMarciano.getWidth(),
                                proyectilMarciano.getHeight(), proyectilMarciano, vBalaMarciano));
                    }
                }
            }
        }
        //limpio el arraylist que uso de contenedor de las columnas
        misColumnas.clear();
    }

    //------------------------MOVER BALAS MARCIANOS (ARRAYLIST)------------------------

    /**
     * Método encargado de mover las balas de los marcianos hacia abajo. Se encarga de ver si algun proyectil choca con la nave o no
     */
    public void actualizaBalasMarcianos() {
        //además de mover las balas, gestiono si chocan o no con la nave, y si desaparecen de la pantalla las elimino
        //de atrás alante para no tener problemas al eliminar
        for (int i = balasMarcianos.size() - 1; i >= 0; i--) {
            //muevo la bala actual hacia abajo
            balasMarcianos.get(i).bajar();
            //si choca con la nave
            if (balasMarcianos.get(i).getContenedor().intersect(miNave.getContenedor())) {

                miNave.setImagen(explosion);
                //vibra el dispositivo
                if (vibracion) {

                    vibrar();
                }
                //perdi
                if (musica) {
                    //sonido explosion nave
                    suenaMuereNave();
                }
                estoyJugando = false;
                mueveNave = false;
                acabaMusica();
                if (mejoraPuntuacion()) {
                    pideSiglas = true;
                } else {
                    perdi = true;
                }
            } else {
                //si no ha chocado con la nave
                //veo si ha chocado o no con la bala de la nave, si es asi, elimino ambas balas
                if (balasMarcianos.get(i).getContenedor().intersect(miNave.getBala())) {

                    //elimino ambas balas
                    //elimino la bala marciano
                    balasMarcianos.remove(i);

                    //elimino la bala de la nave
                    miNave.setHayBala(false);

                    //si no choca con la nave ni con la bala de la nave
                } else {
                    //veo si desaparece de la pantalla, si es así
                    if (balasMarcianos.get(i).getContenedor().top >= altoPantalla) {
                        //la elimino para no mover balas que no se ven
                        balasMarcianos.remove(i);
                    }
                }

            }
        }
    }

    //------------------------MOVIMIENTO VERTICAL Y HORIZONTAL DE LOS MARCIANOS------------------------

    /**
     * Método que se encarga de ver en que dirección tienen que moverse los marcianos, y si tienen que descender un nivel o no
     */
    public void actualizaBanderasMovimiento() {
        //aqui veré en que dirección tienen que ir los marcianos (izq o drch) -> bandera voyIzquierda
        //también veré si descienden un nivel o no -> bandera voy abajo
        //recorro las filas de marcianos
        for (int i = 0; i < marcianos.length; i++) {
            //recorro las columnas
            for (int j = 0; j < marcianos[0].length; j++) {
                //si hay un marciano
                if (marcianos[i][j] != null) {
                    //si un marciano llega al limite de la derecha
                    if (marcianos[i][j].limiteDerecha(anchoPantalla)) {
                        //bandera voy abajo a true
                        voyAbajo = true;
                        //pongo la bandera voyIzquierda a true
                        voyIzquierda = true;
                        //salgo del for porque uno de ellos a llegado al limite
                        break;
                    } else {
                        //si no llegue al limite por la derecha, miro si llegue al limite por la izquierda
                        if (marcianos[i][j].limiteIzquierda()) {
                            //bandera voy abajo a true
                            voyAbajo = true;
                            //pongo la bandera voyIzquierda a false
                            voyIzquierda = false;
                            //salgo del for porque uno de ellos a llegado al limite
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que se encarga de mover los marcianos según las banderas de dirección. Es decir, si tienen que ir a la derecha o a la izquierda, o si tienen que descender un nivel o no
     */
    public void mueveMarcianos() {
        //recorro las filas
        for (int i = 0; i < marcianos.length; i++) {
            //recorro las columnas
            for (int j = 0; j < marcianos[0].length; j++) {
                //si hay un marciano
                if (marcianos[i][j] != null) {
                    //lo muevo de manera lateral segun en que dirección tengo que ir
                    marcianos[i][j].moverLateral(voyIzquierda);
                    //en caso de tener que descender un nivel, lo hace
                    marcianos[i][j].moverAbajo(voyAbajo);
                    if (marcianos[i][j].limiteAbajo(altoPantalla - miNave.getImagen().getHeight())) {
                        //hago que el dispositivo vibre
                        vibrar();
                        if (mejoraPuntuacion()) {
                            pideSiglas = true;
                        } else {
                            perdi = true;
                        }
                        estoyJugando = false;
                    }
                }
            }
        }
        //después de mover todos los marcianos
        //pongo la bandera voyAbajo a false
        voyAbajo = false;
    }

    //------------------------VIBRACIÓN DEL DISPOSITIVO------------------------

    /**
     * Método que será llamado en el caso de que hayamos perdido y la vibración esté a true, en dicho caso, hará vibrar el dispositivo móvil
     */
    public void vibrar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            miVibrador.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            miVibrador.vibrate(tiempoVibracion);
        }
    }

    //------------------------CUANDO PULSO LA PANTALLA (PUEDO PULSAR UN BOTON O MOVER LA NAVE)------------------------

    /**
     * Este método se encarga de gestionar los movimientos que se producen en dicha pantalla
     * @param event Evento según el tipo de pulsación o movimiento en la pantalla
     * @return Devuelve un entero. En el caso de pulsar el boton de si salir, devuelve el entero que representa la pantalla de inicio, es decir, devuelve 0; si hemos pulsado el boton que nos permite repetir partida devuelve un entero que genera una nueva pantalla Gameplay.  De haber pulsado cualquier otra cosa que no fuera el boton de salir o repetir partida, devuelve el entero de la pantalla actual.
     */
    public int onTouchEvent(MotionEvent event) {
        //cuando el dedo esté en la pantalla, muevo la nave con respecto al eje x!!!!!!!!!
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación


        switch (accion) {
            case MotionEvent.ACTION_DOWN:// Primer dedo toca
                //si estoy jugando con la opcion del gravedad desactivada, y pulso en la pos x donde está la nave, puedo mover la nave
                if (!gravedad && pointerID == 0 && estoyJugando && (event.getX() >= miNave.getContenedor().left && event.getX() <= miNave.getContenedor().right) || mueveNave) {
                    mueveNave = true;
                }
                if (estoyJugando) {
                    //si pulso el btn musica
                    if (pulsa(btnMusica.getRectangulo(), event)) {
                        btnMusica.setBandera(true);
                        btnAux = btnMusica;
                    }
                    //si pulso el btn pausa
                    if (pulsa(btnPausa.getRectangulo(), event)) {
                        btnPausa.setBandera(true);
                        btnAux = btnPausa;
                    }
                }
                if (!empece) {
                    //si no empece y pulso el btn si a jugar
                    if (pulsa(btnJugar.getRectangulo(), event)) {
                        btnJugar.setBandera(true);
                        btnAux = btnJugar;
                    }
                    if (pulsa(btnNoJugar.getRectangulo(), event)) {
                        btnNoJugar.setBandera(true);
                        btnAux = btnNoJugar;
                    }

                }
                //si estoy en pausa
                if (pausa) {
                    //si pulso el btn salir
                    if (pulsa(btnSalir.getRectangulo(), event)) {
                        btnSalir.setBandera(true);
                        btnAux = btnSalir;
                    }
                    //si pulso el btn reanudar
                    if (pulsa(btnReanudar.getRectangulo(), event)) {
                        btnReanudar.setBandera(true);
                        btnAux = btnReanudar;
                    }

                    if (pulsa(btnPausa.getRectangulo(), event)) {
                        btnPausa.setBandera(true);
                        btnAux = btnPausa;
                    }
                }
                if (pideSiglas) {
                    if (pulsa(btnSiglaArriba.getRectangulo(), event)) {
                        btnSiglaArriba.setBandera(true);
                        btnAux = btnSiglaArriba;
                    }
                    if (pulsa(btnSigla2Arriba.getRectangulo(), event)) {
                        btnSigla2Arriba.setBandera(true);
                        btnAux = btnSigla2Arriba;
                    }
                    if (pulsa(btnSigla3Arriba.getRectangulo(), event)) {
                        btnSigla3Arriba.setBandera(true);
                        btnAux = btnSigla3Arriba;
                    }

                    if (pulsa(btnSiglaAbajo.getRectangulo(), event)) {
                        btnSiglaAbajo.setBandera(true);
                        btnAux = btnSiglaAbajo;
                    }
                    if (pulsa(btnSigla2Abajo.getRectangulo(), event)) {
                        btnSigla2Abajo.setBandera(true);
                        btnAux = btnSigla2Abajo;
                    }
                    if (pulsa(btnSigla3Abajo.getRectangulo(), event)) {
                        btnSigla3Abajo.setBandera(true);
                        btnAux = btnSigla3Abajo;
                    }
                    if (pulsa(btnEnviar.getRectangulo(), event)) {
                        btnEnviar.setBandera(true);
                        btnAux = btnEnviar;
                    }
                }
                if (perdi) {
                    if (pulsa(btnSi.getRectangulo(), event)) {
                        btnSi.setBandera(true);
                        btnAux = btnSi;
                    }
                    if (pulsa(btnNo.getRectangulo(), event)) {
                        btnNo.setBandera(true);
                        btnAux = btnNo;
                    }
                }


            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;
            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                if (mueveNave) {
                    mueveNave = false;
                }
                //mientras estoy jugando
                if (estoyJugando) {
                    //si levanto el dedo en el btn musica
                    if (pulsa(btnMusica.getRectangulo(), event) && btnMusica.getBandera()) {
                        cambiaBtnMusica();
                    }
                    //si pulso la opcion pausa
                    if (pulsa(btnPausa.getRectangulo(), event) && btnPausa.getBandera()) {

                        //muestro pantallaPausa, reanudar o salir
                        pausa = true;
                        estoyJugando = false;

                        paraMusica();
                        btnPausa.setImg(imgPlay);

                    }
                } else {
                    if (pulsa(btnJugar.getRectangulo(), event) && btnJugar.getBandera()) {
                        empece = true;
                        estoyJugando = true;
                    }
                    if (pulsa(btnNoJugar.getRectangulo(), event) && btnNoJugar.getBandera()) {
                        //vuelvo al menu
                        acabaMusica();
                        return 0;
                    }
                    if (pausa) {

                        //si he pulsado la opcion salir
                        if (pulsa(btnSalir.getRectangulo(), event) && btnSalir.getBandera()) {
                            //vuelvo al menu
                            acabaMusica();
                            return 0;
                        }
                        if (pulsa(btnReanudar.getRectangulo(), event) && btnReanudar.getBandera()) {
                            //reanudo el gameplay
                            reanudaGame();
                        }
                        if (pulsa(btnPausa.getRectangulo(), event) && btnPausa.getBandera()) {
                            //reanudo el gameplay
                            reanudaGame();

                        }
                    }

                }

                if (pideSiglas) {
                    if (pulsa(btnSiglaArriba.getRectangulo(), event) && btnSiglaArriba.getBandera()) {
                        siglas[0] = retrocede(siglas[0]);
                    }
                    if (pulsa(btnSigla2Arriba.getRectangulo(), event) && btnSigla2Arriba.getBandera()) {
                        siglas[1] = retrocede(siglas[1]);
                    }
                    if (pulsa(btnSigla3Arriba.getRectangulo(), event) && btnSigla3Arriba.getBandera()) {
                        siglas[2] = retrocede(siglas[2]);
                    }

                    if (pulsa(btnSiglaAbajo.getRectangulo(), event) && btnSiglaAbajo.getBandera()) {
                        siglas[0] = avanza(siglas[0]);
                    }
                    if (pulsa(btnSigla2Abajo.getRectangulo(), event) && btnSigla2Abajo.getBandera()) {
                        siglas[1] = avanza(siglas[1]);
                    }
                    if (pulsa(btnSigla3Abajo.getRectangulo(), event) && btnSigla3Abajo.getBandera()) {
                        siglas[2] = avanza(siglas[2]);
                    }
                    if (pulsa(btnEnviar.getRectangulo(), event) && btnEnviar.getBandera()) {
                        tengoSiglas = true;
                    }
                }
                if (perdi) {
                    if (pulsa(btnSi.getRectangulo(), event) && btnSi.getBandera()) {
                        return 6;
                    }
                    if (pulsa(btnNo.getRectangulo(), event) && btnNo.getBandera()) {
                        //vuelvo al menu
                        acabaMusica();
                        return 0;
                    }

                }
                //pongo la bandera del btn que anteriormente puse a true a false
                if (btnAux != null) {
                    btnAux.setBandera(false);
                }

            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último
                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                //solo puedo poner mueve nave a true, cuando gravedad está a false entre una de las condiciones
                if (mueveNave && pointerID == 0) {
                    miNave.moverNave(event.getX());
                }
                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }


    //------------------------CUANDO PULSO EL BTN DE LA MÚSICA EN EL GAMEPLAY------------------------

    /**
     * Método que será llamado en el momento que pulse el boton de la música mientras estoy jugando. Se encarga de activar o desactivar la música, y guardar dicha booleana en el archivo Shared Preferences
     */
    public void cambiaBtnMusica() {
        musica = !musica;
        if (musica) {
            suenaMusica();
            btnMusica.setImg(imgMusicaOff);
        } else {
            paraMusica();
            btnMusica.setImg(imgMusicaOn);
        }
        editorPreferencias.putBoolean("musica", musica);
        editorPreferencias.commit();
    }

    //------------------------DIBUJAR LA PANTALLA------------------------

    /**
     * Método encargado de dibujar el fondo, dependiendo de si estoy jugando o no, dibujo de una manera u otra
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaFondo(Canvas c) {
        if (!estoyJugando) c.drawBitmap(bitmapFondo, 0, 0, null); // Dibujamos el fondo
        else {
            c.drawBitmap(fondo[0].imagen, fondo[0].posicion.x, fondo[0].posicion.y, null);
            c.drawBitmap(fondo[1].imagen, fondo[1].posicion.x, fondo[1].posicion.y, null);
        }
    }

    /**
     * Método encargado de dibujar la pantalla de inicio del juego
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaInicio(Canvas c) {
        c.drawText(txtEmpezar, anchoPantalla / 2, altoPantalla / 2 - tamanhoPuntuacion / 2, pPunutacion);
        btnJugar.dibujar(c);
        btnNoJugar.dibujar(c);
    }

    /**
     * Método encargado de dibujar el propio juego. Marcianos, nave, boton musica, boton pausa, puntuacion y balas.
     *
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaJuego(Canvas c) {
        //dibujo el btnPausa
        btnPausa.dibujar(c);

        //dibujo el btn sonido
        btnMusica.dibujar(c);
        //dibujo los marcianos del array bidimensional (marcianos)
        for (int i = 0; i < marcianos.length; i++) {
            for (int j = 0; j < marcianos[0].length; j++) {
                if (marcianos[i][j] != null) {
                    //dibujo a los marcianos y su contenedor
                    marcianos[i][j].dibujar(c);
                }
            }
        }
        //dibujo todas las balas marcianos
        for (int i = 0; i < balasMarcianos.size(); i++) {
            balasMarcianos.get(i).dibujar(c);
        }

        //dibujo la nave y el proyectil que genera
        miNave.dibujar(c);

        //dibujo la puntuacion
        c.drawText(Integer.toString(puntuacionGlobal), anchoPantalla / 2, altoPantalla / 20, pPunutacion);
    }

    /**
     * Método encargado de dibujar el submenu pausa
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaPausa(Canvas c) {
        //fondo pausa
        Paint a = new Paint();
        a.setTypeface(getTypeFace());
        a.setColor(Color.LTGRAY);
        a.setAlpha(125);
        c.drawRect(0, 0, anchoPantalla, altoPantalla, a);
        a.setColor(Color.WHITE);
        c.drawRect(margenLateralPausa, altoPantalla / 2 - altoMenuPausa / 2,
                anchoPantalla - margenLateralPausa, altoPantalla / 2 + altoMenuPausa / 2, a);

        //dibujo los botones reanudar y salir
        btnSalir.dibujar(c);
        btnReanudar.dibujar(c);

        //dibujo la pregunta
        a.setColor(Color.BLACK);
        a.setTextSize(altoPantalla / 20);
        a.setTextAlign(Paint.Align.CENTER);
        c.drawText(txtAccion, anchoPantalla / 2, altoPantalla / 2 - altoMenuPausa / 2 + altoPantalla / 20 + margenLateralPausa, a);
    }

    /**
     * Método encargado de dibujar el submenú cuando pierdes, preguntando si deseas repetir o no
     *
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaPerdi(Canvas c) {
        //fondo
        Paint a = new Paint();
        a.setTypeface(getTypeFace());
        a.setColor(Color.LTGRAY);
        a.setAlpha(125);
        c.drawRect(0, 0, anchoPantalla, altoPantalla, a);
        a.setColor(Color.WHITE);
        c.drawRect(margenLateralPausa, altoPantalla / 2 - altoMenuPausa / 2,
                anchoPantalla - margenLateralPausa, altoPantalla / 2 + altoMenuPausa / 2, a);

        //dibujo los botones si y no
        btnNo.dibujar(c);
        btnSi.dibujar(c);
        //dibujo la pregunta
        a.setColor(Color.BLACK);
        a.setTextSize(altoPantalla / 20);
        a.setTextAlign(Paint.Align.CENTER);
        c.drawText(txtRepetir, anchoPantalla / 2, altoPantalla / 2 - altoMenuPausa / 2 + altoPantalla / 20 + margenLateralPausa, a);
    }

    /**
     * Método encargado de dibujar el submenu con el que podemos introducir nuestras siglas
     *
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaPideSiglas(Canvas c) {
        //fondo
        Paint a = new Paint();
        a.setTypeface(getTypeFace());
        a.setColor(Color.LTGRAY);
        a.setAlpha(125);
        c.drawRect(0, 0, anchoPantalla, altoPantalla, a);
        a.setColor(Color.WHITE);
        c.drawRect(margenLateralPausa, altoPantalla / 2 - altoMenuIniciales / 2,
                anchoPantalla - margenLateralPausa, altoPantalla / 2 + altoMenuIniciales / 2, a);

        //dibujo los botones
        btnSiglaArriba.dibujar(c);
        btnSiglaAbajo.dibujar(c);
        btnSigla2Arriba.dibujar(c);
        btnSigla2Abajo.dibujar(c);
        btnSigla3Arriba.dibujar(c);
        btnSigla3Abajo.dibujar(c);
        btnEnviar.dibujar(c);
        //dibujo las siglas
        a.setColor(Color.BLACK);
        a.setTextSize(tamanhoSiglas);
        a.setTextAlign(Paint.Align.CENTER);
        c.drawText(siglas[0] + "", margenLateralPausa * 3, altoPantalla / 2 + tamanhoSiglas, a);
        c.drawText(siglas[1] + "", anchoPantalla / 2, altoPantalla / 2 + altoPantalla / 12, a);
        c.drawText(siglas[2] + "", anchoPantalla - margenLateralPausa * 3, altoPantalla / 2 + tamanhoSiglas, a);

        //dibujo la pregunta
        a.setTextSize(altoPantalla / 20);
        c.drawText(txtSiglas, anchoPantalla / 2,
                altoPantalla / 2 - altoMenuIniciales / 2 + altoPantalla / 20 + margenLateralPausa, a);

    }

    /**
     * Método que comprueba si nuestra puntuación mejora la puntuación más baja que hay en la base de datos
     *
     * @return True en el caso de que mi puntuación es mayor que la puntuación más baja. False si mi puntuación no es mayor que la puntuación más baja de la base de datos
     */
    public boolean mejoraPuntuacion() {
        bd = new BaseDeDatos(contexto, "puntuacionesJuego", null, 1);
        db = bd.getWritableDatabase();
        consultaUltima = "SELECT min(puntuacion) FROM puntuaciones";
        //ejecuto la consultaUltima que me devuelve la ultima punutacion y la guardo en, ultimaPuntuacion
        c = db.rawQuery(consultaUltima, null);
        if (c.moveToFirst()) {
            do {
                ultimaPuntuacion = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        //si mi puntuacion es mayor que la ultima
        if (puntuacionGlobal > ultimaPuntuacion) {
            return true;
        }
        return false;
    }

    /**
     * Método que se encarga de introducir en la base de datos nuestra puntuación junto a nuestras siglas. También elimino la puntuación más baja más reciente.
     */
    public void insertPuntuacion() {
        bd = new BaseDeDatos(contexto, "puntuacionesJuego", null, 1);
        db = bd.getWritableDatabase();
        //obtengo el ultimo id para el orden de antiguedad
        consultaId = "SELECT max(id )FROM puntuaciones";
        c = db.rawQuery(consultaId, null);
        if (c.moveToFirst()) {
            do {
                ultimoId = c.getInt(0);
            } while (c.moveToNext());
        }
        //ejecuto la consulta borrar
        //BORRO LA MENOR PUNTUACION MAS NUEVA, CON EL ID MAS ALTO
        db.delete("puntuaciones", "id=(SELECT id FROM puntuaciones WHERE puntuacion=" +
                "(SELECT min(puntuacion) FROM puntuaciones) ORDER BY id DESC LIMIT 1)", null);

        //ejecuto el insert
        ContentValues fila = new ContentValues();
        fila.put("siglas", Character.toString(siglas[0]) + Character.toString(siglas[1]) + Character.toString(siglas[2]));
        fila.put("id", ultimoId + 1);
        fila.put("puntuacion", puntuacionGlobal);
        db.insert("puntuaciones", null, fila);
        c.close();
        hiceInsert = true;
        perdi = true;
    }

    /**
     * Método que se encarga de avanzar las siglas
     *
     * @param letra Char que representa la sigla actual
     * @return Char que representa la sigla siguiente
     */
    public char avanza(char letra) {
        pos = abecedario.indexOf(letra);
        if (pos == abecedario.size() - 1) {
            pos = 0;
        } else {
            pos++;
        }
        letra = abecedario.get(pos);
        return letra;
    }

    /**
     * Método que se encarga de retroceder las siglas
     * @param letra Char que representa la sigla actual
     * @return Char que representa la sigla anterior
     */
    public char retrocede(char letra) {
        pos = abecedario.indexOf(letra);
        if (pos == 0) {
            pos = abecedario.size() - 1;
        } else {
            pos--;
        }
        letra = abecedario.get(pos);
        return letra;
    }

    /**
     * Método que se encarga de reanudar el juego, y la música en el caso de estar activada
     */
    public void reanudaGame() {
        //reanudo el gameplay
        estoyJugando = true;
        if (musica) {
            suenaMusica();
        }
        btnPausa.setImg(imgPausa);
        pausa = false;
    }
}