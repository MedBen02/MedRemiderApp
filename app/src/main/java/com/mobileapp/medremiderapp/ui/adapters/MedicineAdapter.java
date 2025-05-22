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

    public MedicineAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.bind(medicine);
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDose, tvStock, tvDescription;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDose = itemView.findViewById(R.id.tvDose);
            tvStock = itemView.findViewById(R.id.tvStockQuantity);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Medicine medicine) {
            tvName.setText(medicine.getName());
            tvDose.setText(medicine.getDose());
            tvStock.setText(String.valueOf(medicine.getStockQuantity()));
            tvDescription.setText(medicine.getDescription());
        }
    }
}