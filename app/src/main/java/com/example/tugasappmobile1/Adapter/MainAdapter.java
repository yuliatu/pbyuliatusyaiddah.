package com.example.tugasappmobile1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasappmobile1.Model.Catatan;
import com.example.tugasappmobile1.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.NoteViewHolder> {
    private List<Catatan> noteList;

    public MainAdapter(List<Catatan> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Catatan note = noteList.get(position);
        holder.cardTitle.setText(note.note);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setNotes(List<Catatan> newNotes) {
        this.noteList = newNotes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
        }
    }
}
