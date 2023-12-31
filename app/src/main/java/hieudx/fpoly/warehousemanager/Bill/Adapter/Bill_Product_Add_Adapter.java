package hieudx.fpoly.warehousemanager.Bill.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hieudx.fpoly.warehousemanager.General;
import hieudx.fpoly.warehousemanager.Product.Model.Product;
import hieudx.fpoly.warehousemanager.R;
import hieudx.fpoly.warehousemanager.Supplier.Dao.Supplier_Dao;
import hieudx.fpoly.warehousemanager.databinding.ItemRcvProductBillBinding;

public class Bill_Product_Add_Adapter extends RecyclerView.Adapter<Bill_Product_Add_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> list;
    private ArrayList<Product> list_checked;
    private OnCheckedChangeListener listener;

    public Bill_Product_Add_Adapter(Context context, ArrayList<Product> list, OnCheckedChangeListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.list_checked = new ArrayList<>();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(ArrayList<Product> list_checked);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcvProductBillBinding binding = ItemRcvProductBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        Supplier_Dao supplier_dao = new Supplier_Dao(context);

        String nameSupplier = supplier_dao.getSupplierById(list.get(position).getId_supplier()).getName();
        holder.binding.tvNameProduct.setText(product.getName() + " - "+nameSupplier);
        holder.binding.tvQuantity.setText(product.getQuantity() + "");
        holder.binding.edPrice.setText(General.formatSumVND(product.getPrice()));
        if (list.get(position).getImg().isEmpty()) holder.binding.imgProduct.setImageResource(R.mipmap.ic_launcher_foreground);
        else Picasso.get().load(list.get(position).getImg()).into(holder.binding.imgProduct);
        holder.binding.btnPlus.setOnClickListener(view -> {
            holder.binding.tvQuantity.setText((product.getQuantity() + 1) + "");
            product.setQuantity(product.getQuantity() + 1);
            list.set(position, product);
        });

        holder.binding.btnMinus.setOnClickListener(view -> {
            if (product.getQuantity() == 1) {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            } else {
                holder.binding.tvQuantity.setText((product.getQuantity() - 1) + "");
                product.setQuantity(product.getQuantity() - 1);
                list.set(position, product);
            }
        });
        holder.binding.cbAddProduct.setOnCheckedChangeListener((compoundButton, b) -> {
            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION && list != null) {
                String price = holder.binding.edPrice.getText().toString().trim();
                if (b) {
                    if (price.contains(" ") || price.isEmpty()) {
                        Toast.makeText(context, "Giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    } else {
                        product.setPrice(Double.parseDouble(price));
                        if (!list_checked.contains(product)) {
                            list_checked.add(product);
                        }
//                        Log.d("tag_kiemTra", "onCheckedChanged: " + list_checked.size());
                    }
                } else {
                    list_checked.remove(product);
//                    Log.d("tag_kiemTra", "onCheckedChanged: " + list_checked.size());
                }
            }
            listener.onCheckedChanged(list_checked);
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRcvProductBillBinding binding;
        public ViewHolder(@NonNull ItemRcvProductBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
