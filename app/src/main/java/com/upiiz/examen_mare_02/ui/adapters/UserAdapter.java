package com.upiiz.examen_mare_02.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.entities.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> {
    public interface OnUserClick { void onClick(User u); }

    private final List<User> data;
    private final long currentUserId;
    private final OnUserClick listener;

    public UserAdapter(List<User> data, long currentUserId, OnUserClick listener) {
        this.data = data; this.currentUserId = currentUserId; this.listener = listener;
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_user, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        User u = data.get(i);
        h.tvName.setText(u.username);
        h.tvSub.setText(u.id == currentUserId ? "Tú" : "Jugador");
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(u);
        });
    }


    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvSub;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvSub = itemView.findViewById(R.id.tvSub);
        }
    }
}
