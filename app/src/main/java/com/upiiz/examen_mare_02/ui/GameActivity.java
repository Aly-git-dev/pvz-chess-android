package com.upiiz.examen_mare_02.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.AppDatabase;
import com.upiiz.examen_mare_02.data.entities.Match;
import com.upiiz.examen_mare_02.data.game.Board;
import com.upiiz.examen_mare_02.data.game.BoardSerializer;
import com.upiiz.examen_mare_02.data.game.Piece;
import com.upiiz.examen_mare_02.data.game.Side;

import java.util.List;
import java.util.concurrent.Executors;

public class GameActivity extends AppCompatActivity implements BoardView.OnCellTouchListener {

    private AppDatabase db;
    private long meId, matchId;
    private Match match;
    private Board board;

    private BoardView boardView;
    private TextView tvTurn, tvStatus, tvTimer;
    private Button btnEndTurn, btnCancelSel;

    // selección actual
    private int selR = -1, selC = -1;
    private List<int[]> currentHints;

    // cronómetro
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Runnable timerTick = new Runnable() {
        @Override public void run() {
            if (match == null) { timerHandler.postDelayed(this, 1000); return; }
            if (match.status.equals("FINALIZADA")) return;
            long secs = Math.max(0, (System.currentTimeMillis() - match.lastTurnStartTime)/1000);
            tvTimer.setText("Tiempo de turno: " + secs + " s");
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_game);
        db = AppDatabase.get(this);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(item -> MenuUtils.handle(this, item));

        SharedPreferences sp = getSharedPreferences("SESSION", MODE_PRIVATE);
        meId = sp.getLong("currentUserId", -1);
        matchId = getIntent().getLongExtra("matchId", -1);

        boardView = findViewById(R.id.boardView);
        tvTurn = findViewById(R.id.tvTurn);
        tvTimer = findViewById(R.id.tvTimer);
        tvStatus = findViewById(R.id.tvStatus);
        btnEndTurn = findViewById(R.id.btnEndTurn);
        btnCancelSel = findViewById(R.id.btnCancelSel);

        boardView.setOnCellTouchListener(this);

        // Si quieres forzar una imagen de tablero concreta:
        // boardView.setBoardFullImage(R.drawable.board_full);

        btnEndTurn.setOnClickListener(v -> endTurnWithoutMove());
        btnCancelSel.setOnClickListener(v -> clearSelection());

        loadMatch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerTick, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerTick);
    }

    private void loadMatch() {
        Executors.newSingleThreadExecutor().execute(() -> {
            match = db.matchDao().getById(matchId);

            if (match.boardState == null || match.boardState.isEmpty()) {
                // Si por alguna razón la partida no tenía estado, crea uno nuevo
                board = Board.initial();
                match.boardState = BoardSerializer.serialize(board);
                db.matchDao().update(match);
            } else {
                board = BoardSerializer.deserialize(match.boardState);
            }

            runOnUiThread(this::render);
        });
    }


    private void render() {
        boardView.setBoard(board);

        if (match.status.equals("FINALIZADA")) {
            tvTurn.setText("Partida finalizada");
        } else {
            Side turnSide = currentTurnSide();
            String who = (turnSide == Side.PLANT) ? "Plantas" : "Zombies";
            tvTurn.setText("Turno de: " + who);
        }

        tvStatus.setText(statusText());
        if (match.status.equals("FINALIZADA")) {
            btnEndTurn.setEnabled(false);
        }
    }

    private String statusText() {
        if (match.status.equals("FINALIZADA")) {
            Side w = winnerFromBoard();
            if (w == null) return "Finalizada";
            boolean iAmPlants = sideOf(meId) == Side.PLANT;
            boolean plantsWon = w == Side.PLANT;
            return plantsWon == iAmPlants ? "Ganaste 🎉" : "Perdiste";
        }
        return "Plantas: " + board.countPieces(Side.PLANT) +
                "  Zombies: " + board.countPieces(Side.ZOMBIE);
    }

    private Side sideOf(long playerId) { return playerId == match.player1Id ? Side.PLANT : Side.ZOMBIE; }

    private Side currentTurnSide() {
        return sideOf(match.currentTurnPlayerId);
    }


    private void clearSelection() {
        selR = selC = -1;
        boardView.clearSelection();
        boardView.clearHints();
        currentHints = null;
    }

    // Tap en el tablero
    @Override
    public void onCellTapped(int row, int col) {
        if (match.status.equals("FINALIZADA")) return;

        // YA NO BLOQUEAMOS POR USUARIO LOGUEADO
        Side turnSide = currentTurnSide();

        // 1) Sin selección previa: selecciona pieza del lado que tiene el turno
        if (selR == -1) {
            Piece p = board.get(row, col);
            if (p != null && p.getSide() == turnSide) {
                selR = row; selC = col;
                boardView.select(row, col);
                currentHints = board.validMoves(row, col);
                boardView.setHints(currentHints);
            } else {
                Toast.makeText(this, "Selecciona una pieza del lado que tiene el turno", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // 2) Tocar la misma casilla = cancelar selección
        if (row == selR && col == selC) {
            clearSelection();
            return;
        }

        // 3) Sólo permitir destinos que estaban en los hints
        boolean hinted = false;
        if (currentHints != null) {
            for (int[] h : currentHints) {
                if (h[0] == row && h[1] == col) {
                    hinted = true;
                    break;
                }
            }
        }
        if (!hinted) {
            Toast.makeText(this, "Destino inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = board.tryMove(selR, selC, row, col);
        if (!ok) {
            Toast.makeText(this, "Movimiento no permitido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Daño por tiempo al LADO que se tardó en su turno
        long now = System.currentTimeMillis();
        long elapsed = Math.min(30, (now - match.lastTurnStartTime) / 1000);
        board.applyTimeDamage(elapsed, turnSide);

        // ¿Ganó alguien?
        Side win = board.getWinnerOrNull();
        if (win != null) {
            match.status = "FINALIZADA";
        } else {
            // Cambiar el jugador / lado del turno
            match.currentTurnPlayerId = (match.currentTurnPlayerId == match.player1Id)
                    ? match.player2Id : match.player1Id;
        }
        match.lastTurnStartTime = now;
        match.boardState = BoardSerializer.serialize(board);

        clearSelection();
        saveAndRender();
    }


    private void endTurnWithoutMove() {
        if (match.status.equals("FINALIZADA")) return;

        Side turnSide = currentTurnSide();

        long now = System.currentTimeMillis();
        long elapsed = Math.min(30, (now - match.lastTurnStartTime) / 1000);
        board.applyTimeDamage(elapsed, turnSide);

        Side win = board.getWinnerOrNull();
        if (win != null) {
            match.status = "FINALIZADA";
        } else {
            match.currentTurnPlayerId = (match.currentTurnPlayerId == match.player1Id)
                    ? match.player2Id : match.player1Id;
        }
        match.lastTurnStartTime = now;
        match.boardState = BoardSerializer.serialize(board);

        clearSelection();
        saveAndRender();
    }


    private void saveAndRender() {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.matchDao().update(match);
            runOnUiThread(this::render);
        });
    }

    private Side winnerFromBoard() { return board.getWinnerOrNull(); }

    // ===== Menú global =====
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtils.inflate(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MenuUtils.handle(this, item)) return true;
        return super.onOptionsItemSelected(item);
    }
}
