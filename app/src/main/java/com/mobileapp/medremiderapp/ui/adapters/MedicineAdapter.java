package com.mobileapp.medremiderapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.model.Medicine;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<Medicine> medicines;
    private OnItemClickListener listener;
    private int selectedPosition = -1;
    private boolean useOverlayApproach = true;

    public interface OnItemClickListener {
        void onItemClick(Medicine medicine, boolean isSelected);
    }

    public MedicineAdapter(List<Medicine> medicines, OnItemClickListener listener) {
        this.medicines = medicines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new MedicineViewHolder(view, useOverlayApproach);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        boolean isSelected = position == selectedPosition;
        holder.bind(medicine, isSelected);

        holder.itemView.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                // Toggle selection
                if (selectedPosition == clickedPosition) {
                    // Clicked on already selected item - deselect
                    selectedPosition = -1;
                } else {
                    // Select new item
                    int oldPosition = selectedPosition;
                    selectedPosition = clickedPosition;
                    if (oldPosition != -1) notifyItemChanged(oldPosition);
                }
                notifyItemChanged(clickedPosition);

                if (listener != null) {
                    listener.onItemClick(medicines.get(clickedPosition), selectedPosition == clickedPosition);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        if (oldPosition != -1) notifyItemChanged(oldPosition);
        if (position != -1) notifyItemChanged(position);
    }

    public void clearSelection() {
        int oldPosition = selectedPosition;
        selectedPosition = -1;
        if (oldPosition != -1) notifyItemChanged(oldPosition);
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDose, tvStock, tvDescription;
        private View overlay;

        public MedicineViewHolder(@NonNull View itemView, boolean useOverlay) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDose = itemView.findViewById(R.id.tvDose);
            tvStock = itemView.findViewById(R.id.tvStockQuantity);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            overlay = itemView.findViewById(R.id.overlay);
        }

        public void bind(Medicine medicine, boolean isSelected) {
            tvName.setText(medicine.getName());
            tvDose.setText(medicine.getDose());
            tvStock.setText(String.valueOf(medicine.getStockQuantity()));
            tvDescription.setText(medicine.getDescription());

            overlay.setVisibility(isSelected ? View.VISIBLE : View.GONE);

            overlay.animate()
                    .alpha(isSelected ? 0.6f : 0f)
                    .setDuration(200)
                    .start();
        }
    }
}