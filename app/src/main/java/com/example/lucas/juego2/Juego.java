package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Clase que hereda surfeceView que se encarga del control de escenas (de pantallas)
 * @author Lucas Alonso de San Segundo
 */
public class Juego  extends SurfaceView implements SurfaceHolder.Callback{
    // control de tiempo de la aplicación
    /**
     * Long para el control temporal
     */
    long last;

    /**
     * Long para el control temporal
     */
    long now;
    /**
     * Entero para el control temporal
     */
    int timeXFrame;
    /**
     * Entero para el control temporal
     */
    int maxFrames;

    /**
     * Objeto SurfaceHolder
     */
    private SurfaceHolder surfaceHolder;      // Interfaz abstracta para manejar la superficie de dibujado

    /**
     * Objeto contexto
     */
    private Context context;                  // Contexto de la aplicación

    /**
     * Objeto pantalla que representa la pantalla actual
     */
    public Pantalla pantallaActual;

    /**
     * Entero que representa el ancho de la pantalla
     */
    private int anchoPantalla=1;              // Ancho de la pantalla, su valor se actualiza en el método surfaceChanged

    /**
     * Entero que representa el alto de la pantalla
     */
    private int altoPantalla=1;               // Alto de la pantalla, su valor se actualiza en el método surfaceChanged

    /**
     * Hilo
     */
    private Hilo hilo;                        // Hilo encargado de dibujar y actualizar la física

    /**
     * Control del hilo
     */
    private boolean funcionando = false;      // Control del hilo

    /**
     * Constructor de la clase Juego
     * @param context Objeto contexto
     */
    public Juego(Context context) {
        super(context);
        // control de tiempo de la aplicación
        now=System.currentTimeMillis();
        last=System.currentTimeMillis();
        maxFrames=100;                 // Número máximo de frames por segundo
        timeXFrame=1000/maxFrames;    // Tasa de tiempo para dibujar un frame

        this.surfaceHolder = getHolder();       // Se obtiene el holder
        this.surfaceHolder.addCallback(this);   // Se indica donde van las funciones callback
        this.context = context;                 // Obtenemos el contexto
        hilo = new Hilo();                      // Inicializamos el hilo
        setFocusable(true);                     // Aseguramos que reciba eventos de toque
    }

    //GESTIONO LA PULSACIOON DE LA PANTALLA

    /**
     * Dependiendo del código de la pantalla, hago una cosa u otra. Si el codigo de mi pantalla actual es diferente al que recibo, genero una nueva pantallaactual.
     * @param event Objeto MotionEvent
     * @return Siempre devuelve true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (surfaceHolder) {
            int codigo=  pantallaActual.onTouchEvent(event);
            //si he cambiado de escena
            if(codigo!=pantallaActual.idPantalla){
                //veo a que pantalla voy
                switch (codigo){
                    case 0:
                        pantallaActual=new Menu(context,0,anchoPantalla,altoPantalla);
                        break;
                    case 1:
                        pantallaActual=new Gameplay(context,1,anchoPantalla,altoPantalla);
                        break;
                    case 2:
                        pantallaActual=new Opciones(context,2,anchoPantalla,altoPantalla);
                        break;
                    case 3:
                        pantallaActual=new Records(context,3,anchoPantalla,altoPantalla);
                        break;
                    case 4:
                        pantallaActual=new Ayuda(context,4,anchoPantalla,altoPantalla);
                        break;
                    case 6:
                        pantallaActual=new Gameplay(context,1,anchoPantalla,altoPantalla);
                        break;
                    case 5:
                        pantallaActual=new Creditos(context,5,anchoPantalla,altoPantalla);
                        break;

                }
            }
        }
        return true;
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    /**
     * Método que se ejecuta cuando "se destroza" el surface
     * @param surfaceHolder Objeto SurfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        hilo.setFuncionando(false);  // Se para el hilo
        try {
            hilo.join();   // Se espera a que finalize
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se ejcuta cuando "el surface cambia"
     * @param holder Objeto SurfaceHolder
     * @param format Entero que representa el formato
     * @param width Entero que representa el ancho de la pantalla
     * @param height Entero que representa el alto de la pantalla
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        anchoPantalla = width;               // se establece el nuevo ancho de pantalla
        altoPantalla = height;               // se establece el nuevo alto de pantalla
        //creo la primera pantalla!!!
        pantallaActual=new Menu(getContext(),0,anchoPantalla,altoPantalla);
        hilo.setSurfaceSize(width,height);   // se establece el nuevo ancho y alto de pantalla en el hilo
        hilo.setFuncionando(true); // Se le indica al hilo que puede arrancar
        if (hilo.getState() == Thread.State.NEW) hilo.start(); // si el hilo no ha sido creado se crea;
        if (hilo.getState() == Thread.State.TERMINATED) {      // si el hilo ha sido finalizado se crea de nuevo;
            hilo=new Hilo();
            hilo.start(); // se arranca el hilo
        }
    }
    // Clase Hilo en la cual implementamos el método de dibujo (y física) para que
    // se haga en paralelo con la gestión de la interfaz de usuario

    /**
     * Clase en la cual implemento el método dibujar y actualizar física
     */
    class Hilo extends Thread {
        public Hilo(){

        }


        @Override
        public void run() {
            long tiempoDormido = 0; //Tiempo que va a dormir el hilo
            final int FPS = 50; // Nuestro objetivo
            final int TPS = 1000000000; //Ticks en un segundo para la función usada nanoTime()
            final int FRAGMENTO_TEMPORAL = TPS / FPS; // Espacio de tiempo en el que haremos todo de forma repetida
            // Tomamos un tiempo de referencia actual en nanosegundos más preciso que currenTimeMillis()
            long tiempoReferencia = System.nanoTime();

            while (funcionando) {
                now=System.currentTimeMillis();
                if (now-last>=timeXFrame){ // si ya paso el tiempo necesario, dibujo. Control de FramesxSegundo en funcion del tiempo
                    last=now;
                    Canvas c=null; //Necesario repintar _todo el lienzo
                    try {
                        if (!surfaceHolder.getSurface().isValid()) continue; // si la superficie no está preparada repetimos
                        c=surfaceHolder.lockCanvas(); // Obtenemos y bloqueamos el lienzo. La  sincronización es necesaria por ser recurso común
                        synchronized (surfaceHolder) {
                            pantallaActual.actualizarFisica(); // solo actualizo la fisica si no hay un modal por encima
                            pantallaActual.dibujar(c);
                        }
                    }catch (Exception e){
                        e.getStackTrace();
                    } finally { //haya o no excepción, hay que liberar el lienzo
                        if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                    }
                } else {
                    try {
                        Thread.sleep((long)timeXFrame - (now - last)); // duermo el tiempo necesario para el siguiente frame
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Calculamos el siguiente instante temporal donde volveremos a actualizar y pintar
                tiempoReferencia += FRAGMENTO_TEMPORAL;
                // El tiempo que duerme será el siguiente menos el actual (Ya ha terminado de pintar y actualizar)
                tiempoDormido = tiempoReferencia - System.nanoTime();
                //Si tarda mucho, dormimos.
                if (tiempoDormido > 0) {
                    try {
                        Thread.sleep(tiempoDormido / 1000000); //Convertimos a ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            System.exit(0);
//        @Override
//        public void run() {
//            while (funcionando) {
//                Canvas c = null; //Necesario repintar todo el lienzo
//                try {
//                    if (!surfaceHolder.getSurface().isValid()) continue; // si la superficie no está preparada repetimos
//                    c = surfaceHolder.lockCanvas(); // Obtenemos el lienzo.  La sincronización es necesaria por ser recurso común
//                    synchronized (surfaceHolder) {
//
//                        pantallaActual.actualizarFisica();  // Movimiento de los elementos
//                        pantallaActual.dibujar(c);              // Dibujamos los elementos
//                    }
//                } finally {  // Haya o no excepción, hay que liberar el lienzo
//                    if (c != null) {
//                        surfaceHolder.unlockCanvasAndPost(c);
//                    }
//                }
//            }
        }

        // Activa o desactiva el funcionamiento del hilo
        void setFuncionando(boolean flag) {
            funcionando = flag;
        }

        // Función es llamada si cambia el tamaño de la pantall o la orientación
        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {  // Se recomienda realizarlo de forma atómica

            }
        }
    }
}

