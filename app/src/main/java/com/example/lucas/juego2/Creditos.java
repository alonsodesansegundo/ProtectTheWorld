package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de dibujar la pantalla de créditos y gestionar su funcionalidad
 *
 * @author Lucas Alonso de San Segundo
 */
public class Creditos extends Pantalla {
    /**
     * Booleana para detectar si hay algun dedo en la pantalla o no, si lo hay, no muevo el texto
     */
    private boolean hayDedo;

    /**
     * Entero que nos sirve para gestionar que texto debe aparecer en la pantalla
     */
    private int modo;

    /**
     * Entero con el que iremos moviendo el texto respecto al eje Y
     */
    private int posY;

    /**
     * Bitmap (imagen) para el boton que nos permite volver al menu principal
     */
    private Bitmap imgVolver;

    /**
     * Objeto boton que nos permite regresar al menu principal
     */
    private Boton back;

    /**
     * Cadena que representará la palabra créditos
     */
    private String txtCreditos;

    /**
     * Cadena que representará la palabra musica
     */
    private String txtMusica;

    /**
     * Cadena que representará la palabra fuente
     */
    private String txtFuente;

    /**
     * Cadena que representará la palabra imagenes
     */
    private String txtImagenes;

    /**
     * Cadena que representará el primer agradecimiento imagenes
     */
    private String txtImg;

    /**
     * Cadena que representará el segundo agradecimiento imagenes
     */
    private String txtImg2;

    /**
     * Cadena que representará el agradecimiento a la musica
     */
    private String txtMusic;

    /**
     * Cadena que representará el afradecimiento a la fuente
     */
    private String txtFont;

    /**
     * Cadena que representará la el agradecimiento al canal
     */
    private String txtCanal;

    /**
     * Cadena que representará la direccion de youtube
     */
    private String txtYotube;

    /**
     * Cadena que representará el texto Hecho y dirigido por
     */
    private String txtHecho;

    /**
     * Cadena que representará el texto Con la ayuda de
     */
    private String txtAyuda;

    /**
     * Objeto paint para escribir el texto del agradecimiento actual Imagenes,Musica
     */
    private Paint pTexto;

    /**
     * Objeto paint para escribir el texto de donde hemos sacado los diferentes datos; imagenes, musica...
     */
    private Paint pTexto2;

    /**
     * Constructor de la pantalla Creditos
     *
     * @param contexto      Objeto contexto
     * @param idPantalla    Entero que representa el id de esta pantalla
     * @param anchoPantalla Entero que representa el ancho de la pantalla
     * @param altoPantalla  Entero que representa el alto de la pantalla
     */
    public Creditos(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);

        hayDedo = false;
        modo = 0;
        posY = altoPantalla / 4;
        pTexto = new Paint();
        pTexto.setTypeface(getTypeFace());
        pTexto.setColor(Color.WHITE);
        pTexto.setTextAlign(Paint.Align.CENTER);
        pTexto.setTextSize(altoPantalla / 20);
        pTexto2 = new Paint();
        pTexto2.setTypeface(getTypeFace());
        pTexto2.setColor(Color.WHITE);
        pTexto2.setTextAlign(Paint.Align.CENTER);
        pTexto2.setTextSize(altoPantalla / 30);
        //--------------STRINGS--------------
        txtCreditos = contexto.getString(R.string.creditos);
        txtMusica = contexto.getString(R.string.musica);
        txtFuente = contexto.getString(R.string.fuente);
        txtImagenes = contexto.getString(R.string.img);
        txtImg = contexto.getString(R.string.img1);
        txtImg2 = contexto.getString(R.string.img2);
        txtMusic = contexto.getString(R.string.music1);
        txtFont = contexto.getString(R.string.font1);
        txtHecho = contexto.getString(R.string.hecho);
        txtAyuda = contexto.getString(R.string.conAyuda);
        txtYotube = contexto.getString(R.string.youtube);
        txtCanal = contexto.getString(R.string.canalYT);
        //-------------FONDO-------------
        fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.fondo2);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        //--------------MUSICA--------------
        musica = preferencias.getBoolean("musica", true);
        configuraMusica(R.raw.submenus);
        if (musica) {
            suenaMusica();
        }
        back = new Boton(anchoPantalla - anchoPantalla / 10, 0, anchoPantalla, anchoPantalla / 10, Color.TRANSPARENT);
        imgVolver = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla / 10, anchoPantalla / 10, true);
        back.setImg(imgVolver);


    }

    /**
     * Este método se encarga de dibujar los creditos relacionados con las imagenes
     *
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujaImagenes(Canvas c) {
        //texto de agradecimientos
        c.drawText(txtImagenes, anchoPantalla / 2, posY, pTexto);
        c.drawText(txtImg, anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);
        c.drawText(txtImg2, anchoPantalla / 2, posY + pTexto.getTextSize() * 2, pTexto2);
    }

    /**
     * Este método se encarga de dibujar los creditos relacionados con la música
     *
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujaMusica(Canvas c) {
        //texto de agradecimientos
        c.drawText(txtMusica, anchoPantalla / 2, posY, pTexto);
        c.drawText(txtMusic, anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);
        c.drawText(txtYotube, anchoPantalla / 2, posY + pTexto.getTextSize() + pTexto2.getTextSize(), pTexto2);
        c.drawText(txtCanal, anchoPantalla / 2, posY + pTexto.getTextSize() + pTexto2.getTextSize() * 2, pTexto2);
    }

    /**
     * Este método se encarga de dibujar los creditos relacionados con la fuente
     *
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujaFuente(Canvas c) {
//texto de agradecimientos
        c.drawText(txtFuente, anchoPantalla / 2, posY, pTexto);
        c.drawText(txtFont, anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);
    }

    /**
     * Este método se encarga de dibujar los creditos relacionados con las personas que han ayudado a la realización de dicho juego
     *
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujaUltimo(Canvas c) {
        //dibujo el texto
        c.drawText(txtAyuda, anchoPantalla / 2, posY, pTexto);
        c.drawText("Javier Conde", anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);

        c.drawText(txtHecho, anchoPantalla / 2, posY + pTexto.getTextSize() * 3, pTexto);
        c.drawText("Lucas Alonso", anchoPantalla / 2, posY + pTexto.getTextSize() * 4, pTexto2);
    }

    /**
     * Método que se encarga de ir dibujando los creditos. Se encarga de gestionar que creditos hay que dibujar y tambien da la sensación de movimiento hacia abajo.
     *
     * @param c Objeto canvas para poder dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        try {
            //fondo
            c.drawBitmap(fondo, 0, 0, null);
            //titulo
            c.drawText(txtCreditos, anchoPantalla / 2, altoPantalla / 8, pTitulo);

            switch (modo) {
                case 0:
                    dibujaImagenes(c);
                    break;
                case 1:
                    dibujaMusica(c);
                    break;
                case 2:
                    dibujaFuente(c);
                    break;
                case 3:
                    dibujaUltimo(c);
                    break;
            }

            if (!hayDedo) {
                posY += altoPantalla / 200;
                if (posY > altoPantalla + pTexto.getTextSize()) {
                    posY = altoPantalla / 4;
                    modo++;
                    if (modo == 4) {
                        modo = 0;
                    }
                }
            }
            //boton volver
            back.dibujar(c);
        } catch (Exception e) {

        }
    }

    /**
     * Este método se encarga de gestionar los movimientos que se producen en dicha pantalla
     *
     * @param event Evento según el tipo de pulsación o movimiento en la pantalla
     * @return Devuelve un entero. En el caso de pulsar el boton de volver, devuelve el entero que representa la pantalla de inicio, es decir, devuelve 0. De haber pulsado cualquier otra cosa que no fuera el boton de volver, devuelve el entero de la pantalla actual.
     */
    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                if (pulsa(back.getRectangulo(), event)) {
                    back.setBandera(true);
                }
                hayDedo = true;
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                //si pulso la opcion volver
                if (pulsa(back.getRectangulo(), event) && back.getBandera()) {
                    acabaMusica();
                    return 0;
                }
                hayDedo = false;
                back.setBandera(false);
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

