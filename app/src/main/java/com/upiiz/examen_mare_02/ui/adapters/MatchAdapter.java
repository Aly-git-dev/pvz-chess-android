package com.upiiz.examen_mare_02.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.entities.Match;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.VH> {
    public interface OnMatchClick { void onClick(Match m); }

    private final List<Match> data;
    private final OnMatchClick listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

    public MatchAdapter(List<Match> data, OnMatchClick listener) {
        this.data = data; this.listener = listener;
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_match, p, false);
        return new VH(view);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int i) {
        Match m = data.get(i);
        h.tvTitle.setText("Partida " + m.id);
        String when = sdf.format(new Date(m.lastTurnStartTime));
        h.tvStatus.setText(m.status + " • " + when);
        h.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvStatus;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
