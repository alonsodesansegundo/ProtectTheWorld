package com.example.lucas.juego2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Esta clase se encarga de dibujar la pantalla de records y gestionar su funcionalidad
 *
 * @author Lucas Alonso de San Segundo
 */
public class Records extends Pantalla {
    /**
     * Bitmap (imagen) para el boton de volver al menu principal
     */
    private Bitmap imgVolver;

    /**
     * String (query) que realiza la consulta de todos las puntuaciones de la bd
     */
    private String consulta;

    /**
     * Objeto boton que nos permite regresar al menu principal
     */
    private Boton back;

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
     * Array de lineas que tengo que mostrar en la pantalla
     */
    private ArrayList<String> texto;

    /**
     * Cadena auxiliar en la que guardo el texto que añado al arraylist texto
     */
    private String aux;

    /**
     * Entero que sirve para mostrar la posicion de la puntuacion y siglas
     */
    private int contador;

    /**
     * Entero que representa el tamaño que tendrá el texto
     */
    private int altoTexto;

    /**
     * Entero que me sirve para dibujar los records respecto al eje Y
     */
    private int posY;

    /**
     * Objeto paint que uso para pintar los recors
     */
    private Paint pPuntuaciones;

    /**
     * String cadena que representa el texto Borrar records
     */
    private String txtRecords;

    /**
     * String cadena que representa el texto si
     */
    private String txtSi;

    /**
     * String cadena que representa el texto no
     */
    private String txtNo;

    /**
     * String cadena que representa el texto Estas seguro?
     */
    private String txtSeguro;

    /**
     * Bitmap(imagen) que representa la medalla de oro
     */
    private Bitmap oro;

    /**
     * Bitmap(imagen) que representa la medalla de plata
     */
    private Bitmap plata;

    /**
     * Bitmap(imagen) que representa la medalla de bronce
     */
    private Bitmap bronce;

    /**
     * Objeto boton que nos pregunta si queremos borrar los reccords
     */
    private Boton btnBorrarRecords;

    /**
     * Objeto boton que nos permite borrar los records
     */
    private Boton btnSi;

    /**
     * Objeto boton que nos permite cancelar el hecho de querer borrar los records
     */
    private Boton btnNo;

    /**
     * Booleana de control
     */
    private boolean submenu;

    /**
     * Objeto paint con el que pinto el submenu de quieres borrar...
     */
    private Paint pMenu;

    /**
     * Entero para dibujar el submenu respecto al eje x
     */
    private int altoMenu;

    /**
     * Entero para dibujar el submenu respecto al eje Y
     */
    private int margenMenu;

    /**
     * Constructor de la pantalla Records
     *
     * @param contexto      Objeto contexto
     * @param idPantalla    Entero que representa el id de esta pantalla
     * @param anchoPantalla Entero que representa el ancho de la pantalla
     * @param altoPantalla  Entero que representa el alto de la pantalla
     */
    public Records(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        //----------------STRINGS----------------
        txtRecords = contexto.getString(R.string.noRecords);
        txtNo = contexto.getString(R.string.no);
        txtSi = contexto.getString(R.string.si);
        txtSeguro = contexto.getString(R.string.seguro);

        margenMenu = anchoPantalla / 20;
        altoMenu = altoPantalla / 4;
        submenu = false;

        //----------------BTN SI----------------
        btnSi = new Boton(margenMenu * 2, altoPantalla / 2,
                anchoPantalla / 2 - margenMenu,
                altoPantalla / 2 + altoMenu / 2 - margenMenu, Color.GREEN);
        btnSi.setTexto(txtSi, altoPantalla / 30, Color.BLACK, getTypeFace());

        //----------------BTN NO----------------
        btnNo = new Boton(anchoPantalla / 2 + margenMenu, altoPantalla / 2,
                anchoPantalla - margenMenu * 2,
                altoPantalla / 2 + altoMenu / 2 - margenMenu, Color.RED);
        btnNo.setTexto(txtNo, altoPantalla / 30, Color.BLACK, getTypeFace());

        fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.fondo2);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);

        btnBorrarRecords = new Boton(anchoPantalla / 2 - anchoPantalla / 5,
                altoPantalla - altoPantalla / 10,
                anchoPantalla / 2 + anchoPantalla / 5,
                altoPantalla - altoPantalla / 100, Color.TRANSPARENT);
        btnBorrarRecords.setTexto(txtRecords, altoPantalla / 40, Color.WHITE, getTypeFace());
        contador = 0;
        altoTexto = altoPantalla / 20;
        //--------------IMAGENES--------------
        oro = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.oro);
        oro = Bitmap.createScaledBitmap(oro, altoTexto, altoTexto, true);

        plata = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.plata);
        plata = Bitmap.createScaledBitmap(plata, altoTexto, altoTexto, true);

        bronce = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.bronce);
        bronce = Bitmap.createScaledBitmap(bronce, altoTexto, altoTexto, true);
        //--------------MUSICA--------------
        musica = preferencias.getBoolean("musica", true);
        configuraMusica(R.raw.submenus);
        if (musica) {
            suenaMusica();
        }

        pPuntuaciones = new Paint();
        pPuntuaciones.setTypeface(getTypeFace());
        pPuntuaciones.setTextSize(altoTexto);
        pPuntuaciones.setColor(Color.WHITE);
        txtRecords = contexto.getString(R.string.records);
        posY = altoPantalla / 4;

        back = new Boton(anchoPantalla - anchoPantalla / 10, 0, anchoPantalla, anchoPantalla / 10, Color.TRANSPARENT);
        imgVolver = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla / 10, anchoPantalla / 10, true);
        back.setImg(imgVolver);
        actualizaArrayPuntuaciones();
    }

    /**
     * Método que se encarga de dibujar la pantalla de récords
     *
     * @param c Objeto Canvas para poder dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        try {
            c.drawBitmap(fondo, 0, 0, null);
            c.drawText(txtRecords, anchoPantalla / 2, altoPantalla / 8, pTitulo);
            back.dibujar(c);
            btnBorrarRecords.dibujar(c);
            for (int i = 0; i < texto.size(); i++) {
                switch (i) {
                    case 0:
                        //dibujo la medalla de oro
                        c.drawBitmap(oro, anchoPantalla / 4, posY - altoTexto, null);
                        c.drawText(texto.get(i), anchoPantalla / 4 + oro.getWidth(), posY, pPuntuaciones);
                        break;
                    case 1:
                        //dibujo la medalla de plata
                        c.drawBitmap(plata, anchoPantalla / 4, posY - altoTexto, null);
                        c.drawText(texto.get(i), anchoPantalla / 4 + plata.getWidth(), posY, pPuntuaciones);
                        break;
                    case 2:
                        //dibujo la medalla de bronce
                        c.drawBitmap(bronce, anchoPantalla / 4, posY - altoTexto, null);
                        c.drawText(texto.get(i), anchoPantalla / 4 + bronce.getWidth(), posY, pPuntuaciones);
                        break;
                    default:
                        c.drawText(texto.get(i), anchoPantalla / 4, posY, pPuntuaciones);
                        break;
                }


                //espacio entre records
                posY += altoPantalla / 15;
            }
            //vuelvo a la posY inicial
            posY = altoPantalla / 4;

            if (submenu) {
                dibujaConfirmacion(c);
            }
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
                if (submenu) {
                    if (pulsa(btnSi.getRectangulo(), event)) {
                        btnSi.setBandera(true);
                    }
                    if (pulsa(btnNo.getRectangulo(), event)) {
                        btnNo.setBandera(true);
                    }
                } else {
                    if (pulsa(back.getRectangulo(), event)) {
                        back.setBandera(true);
                    }

                    if (pulsa(btnBorrarRecords.getRectangulo(), event)) {
                        btnBorrarRecords.setBandera(true);
                    }
                }


            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo

                if (submenu) {
                    if (pulsa(btnSi.getRectangulo(), event) && btnSi.getBandera()) {
                        borroRecords();
                        submenu = false;
                    }
                    if (pulsa(btnNo.getRectangulo(), event) && btnNo.getBandera()) {
                        submenu = false;
                    }
                } else {
                    //si pulso la opcion volver
                    if (pulsa(back.getRectangulo(), event) && back.getBandera()) {
                        //vuelvo al menu
                        acabaMusica();
                        return 0;
                    }

                    if (pulsa(btnBorrarRecords.getRectangulo(), event) && btnBorrarRecords.getBandera()) {
                        submenu = true;
                    }

                    back.setBandera(false);
                    btnBorrarRecords.setBandera(false);
                }
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último
                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }

    /**
     * Método que elimina todos los récords de la base de datos, es decir, borra la base de datos
     */
    public void borroRecords() {
        bd = new BaseDeDatos(contexto, "puntuacionesJuego", null, 1);
        db = bd.getWritableDatabase();
        contexto.deleteDatabase("puntuacionesJuego");
        db.close();
        actualizaArrayPuntuaciones();
    }

    /**
     * Método que guarda las siglas y las puntuaciones de las 10 mejores puntuaciones en el arraylist texto
     */
    public void actualizaArrayPuntuaciones() {
        texto = new ArrayList<String>();
        bd = new BaseDeDatos(contexto, "puntuacionesJuego", null, 1);
        db = bd.getWritableDatabase();
        //ORDENO LAS PUNTUACIONES DE MAS A MENOS PUNTOS, Y DE MENOS ANTIGUO A MAS EN EL CASO DE COINCIDIR LA PUNTUACION
        consulta = "SELECT * FROM puntuaciones ORDER BY 3 desc, 1 ";
        //ejecuto la consulta
        c = db.rawQuery(consulta, null);
        contador = 0;
        if (c.moveToFirst()) {
            do {
                contador++;
                if (contador == 1 || contador == 2 || contador == 3) {
                    aux = c.getString(1) + " " + c.getInt(2);
                    texto.add(aux);
                } else {
                    aux = contador + ".- " + c.getString(1) + " " + c.getInt(2);
                    texto.add(aux);
                }

            } while (c.moveToNext());
        }
        c.close();
        db.close();
    }

    /**
     * Método que se encarga de dibujar el submenú de confirmación para eliminar la base de datos
     *
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujaConfirmacion(Canvas c) {

        //fondo
        pMenu = new Paint();
        pMenu.setTypeface(getTypeFace());
        pMenu.setColor(Color.WHITE);
        c.drawRect(margenMenu, altoPantalla / 2 - altoMenu / 2,
                anchoPantalla - margenMenu, altoPantalla / 2 + altoMenu / 2, pMenu);

        //dibujo los botones reanudar y salir
        btnSi.dibujar(c);
        btnNo.dibujar(c);

        //dibujo la pregunta
        pMenu.setColor(Color.BLACK);
        pMenu.setTextSize(altoPantalla / 20);
        pMenu.setTextAlign(Paint.Align.CENTER);
        c.drawText(txtSeguro, anchoPantalla / 2, altoPantalla / 2 - altoMenu / 2 + altoPantalla / 20 + margenMenu, pMenu);
    }
}
