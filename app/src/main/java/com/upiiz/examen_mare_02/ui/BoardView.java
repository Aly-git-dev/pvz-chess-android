package com.upiiz.examen_mare_02.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.game.Board;
import com.upiiz.examen_mare_02.data.game.Piece;
import com.upiiz.examen_mare_02.data.game.Side;
import com.upiiz.examen_mare_02.ui.skins.PieceSkins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Renderiza tablero + piezas con imágenes, conserva selección, hints y HP. */
public class BoardView extends View {

    public interface OnCellTouchListener { void onCellTapped(int row, int col); }

    private Board board;
    private OnCellTouchListener listener;

    // Pinturas UI
    private final Paint line = new Paint();
    private final Paint selPaint = new Paint();
    private final Paint hintPaint = new Paint();
    private final Paint hpBg  = new Paint();
    private final Paint hpFg  = new Paint();
    private final Paint hpText = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Selección / hints
    private int selRow = -1, selCol = -1;
    private List<int[]> hints = new ArrayList<>();

    // Fondo
    private @DrawableRes int boardFullRes = R.drawable.tablero; // puedes cambiarlo en setBoardFullImage
    private Bitmap boardFullBmp;   // imagen completa del tablero escalada
    private @DrawableRes int tileLightRes = 0, tileDarkRes = 0; // alternativo si usas tiles
    private Bitmap tileLightBmp, tileDarkBmp;

    // Piezas escaladas por celda (cache por typeId)
    private final Map<String, Bitmap> pieceCache = new HashMap<>();

    // Tamaño
    private float cellSize = 0f;

    public BoardView(Context c, @Nullable AttributeSet a) {
        super(c, a);

        // estilos UI
        line.setStrokeWidth(3f); line.setStyle(Paint.Style.STROKE);
        selPaint.setStyle(Paint.Style.STROKE); selPaint.setStrokeWidth(8f);
        hintPaint.setStyle(Paint.Style.FILL); hintPaint.setAlpha(90);
        hpBg.setStyle(Paint.Style.FILL);
        hpFg.setStyle(Paint.Style.FILL);
        hpText.setTextAlign(Paint.Align.CENTER); hpText.setTextSize(22f);
    }

    // --------- Config pública ---------
    public void setBoard(Board b) { this.board = b; invalidate(); }
    public void setOnCellTouchListener(OnCellTouchListener l) { this.listener = l; }
    public void select(int r, int c) { selRow = r; selCol = c; invalidate(); }
    public void clearSelection() { selRow = selCol = -1; invalidate(); }
    public void setHints(List<int[]> cells) { this.hints = cells==null? new ArrayList<>() : cells; invalidate(); }
    public void clearHints() { this.hints.clear(); invalidate(); }

    /** Usa fondo único a pantalla completa (recomendado si ya tienes "board_full.png"). */
    public void setBoardFullImage(@DrawableRes int resId) {
        this.boardFullRes = resId;
        this.tileLightRes = 0; this.tileDarkRes = 0; // desactiva tiles
        boardFullBmp = null; // forzar recarga
        invalidate();
    }

    /** Usa dos texturas de casilla alternadas (si prefieres patrón checker). */
    public void setBoardTiles(@DrawableRes int lightRes, @DrawableRes int darkRes) {
        this.tileLightRes = lightRes;
        this.tileDarkRes = darkRes;
        this.boardFullRes = 0; // desactiva fondo full
        tileLightBmp = null; tileDarkBmp = null; // forzar recarga
        invalidate();
    }

    // --------- Ciclo de medida/escala ---------
    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int n = Board.SIZE;
        cellSize = w / (float) n;

        // (A) Escalar fondo
        if (boardFullRes != 0) {
            Bitmap raw = BitmapFactory.decodeResource(getResources(), boardFullRes);
            if (raw != null) boardFullBmp = Bitmap.createScaledBitmap(raw, w, w, true);
        } else if (tileLightRes != 0 && tileDarkRes != 0) {
            Bitmap rawL = BitmapFactory.decodeResource(getResources(), tileLightRes);
            Bitmap rawD = BitmapFactory.decodeResource(getResources(), tileDarkRes);
            if (rawL != null) tileLightBmp = Bitmap.createScaledBitmap(rawL, (int)cellSize, (int)cellSize, true);
            if (rawD != null) tileDarkBmp = Bitmap.createScaledBitmap(rawD, (int)cellSize, (int)cellSize, true);
        }

        // (B) Escalar todas las piezas a cellSize (cache por typeId)
        pieceCache.clear(); // reescalar por si cambió tamaño
        // Nota: el caché se construye lazy en draw: la primera vez que ve un typeId lo carga/escala.
    }

    private Bitmap pieceBitmapFor(Piece p) {
        String key = p.getTypeId();
        Bitmap bmp = pieceCache.get(key);
        if (bmp != null) return bmp;
        int resId = PieceSkins.drawableFor(p);
        if (resId == 0) return null;
        Bitmap raw = BitmapFactory.decodeResource(getResources(), resId);
        if (raw == null) return null;
        int sz = (int)(cellSize * 0.9f); // deja margen
        Bitmap scaled = Bitmap.createScaledBitmap(raw, sz, sz, true);
        pieceCache.put(key, scaled);
        return scaled;
    }

    // --------- Render ---------
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = Board.SIZE;
        float w = getWidth();

        // Fondo
        if (boardFullBmp != null) {
            canvas.drawBitmap(boardFullBmp, 0, 0, null);
        } else if (tileLightBmp != null && tileDarkBmp != null) {
            for (int r=0;r<n;r++) for (int c=0;c<n;c++) {
                Bitmap tile = ((r+c)%2==0) ? tileLightBmp : tileDarkBmp;
                canvas.drawBitmap(tile, c*cellSize, r*cellSize, null);
            }
        } else {
            // fallback: sin imágenes, dibuja líneas
            for (int i=0;i<=n;i++){
                canvas.drawLine(0,i*cellSize,w,i*cellSize,line);
                canvas.drawLine(i*cellSize,0,i*cellSize,w,line);
            }
        }

        // Hints
        for (int[] h : hints) {
            float cx = h[1]*cellSize + cellSize/2f, cy = h[0]*cellSize + cellSize/2f;
            canvas.drawCircle(cx, cy, cellSize*0.12f, hintPaint);
        }

        // Selección
        if (selRow>=0 && selCol>=0) {
            RectF r = new RectF(selCol*cellSize, selRow*cellSize, (selCol+1)*cellSize, (selRow+1)*cellSize);
            canvas.drawRect(r, selPaint);
        }

        // Piezas
        if (board == null) return;
        for (int r=0;r<n;r++) for (int c=0;c<n;c++) {
            Piece p = board.get(r,c);
            if (p == null || !p.isAlive()) continue;

            // Dibuja sprite
            Bitmap sprite = pieceBitmapFor(p);
            float left = c*cellSize + (cellSize - (sprite!=null? sprite.getWidth(): cellSize*0.66f))/2f;
            float top  = r*cellSize + (cellSize - (sprite!=null? sprite.getHeight(): cellSize*0.66f))/2f;
            if (sprite != null) {
                canvas.drawBitmap(sprite, left, top, null);
            } else {
                // fallback por si falta un recurso: círculo
                canvas.drawCircle(c*cellSize+cellSize/2f, r*cellSize+cellSize/2f, cellSize*0.33f,
                        (p.getSide()==Side.PLANT)? hpFg : hpBg);
            }

            // Barra de HP
            float cx = c*cellSize + cellSize/2f, cy = r*cellSize + cellSize/2f;
            float wBar = cellSize*0.62f, hBar = cellSize*0.12f;
            float leftBar = cx - wBar/2f, topBar = r*cellSize + cellSize*0.82f;
            canvas.drawRect(leftBar, topBar, leftBar+wBar, topBar+hBar, hpBg);
            float pct = Math.max(0f, (float)p.getHp()/p.getMaxHp());
            canvas.drawRect(leftBar, topBar, leftBar+wBar*pct, topBar+hBar, hpFg);

            // Número HP
            canvas.drawText(String.valueOf(p.getHp()), cx, topBar - 4f, hpText);
        }
    }

    // --------- Input ---------
    @Override public boolean onTouchEvent(MotionEvent e) {
        if (board == null) return false;
        if (e.getAction() == MotionEvent.ACTION_UP) {
            int n = Board.SIZE;
            int col = (int)(e.getX() / cellSize);
            int row = (int)(e.getY() / cellSize);
            if (Board.in(row,col) && listener != null) listener.onCellTapped(row, col);
            return true;
        }
        return true;
    }
}
