package hieudx.fpoly.warehousemanager.Member.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hieudx.fpoly.warehousemanager.General;
import hieudx.fpoly.warehousemanager.Member.Dao.User_Dao;
import hieudx.fpoly.warehousemanager.Member.Model.User;
import hieudx.fpoly.warehousemanager.Member.account.Edit_UserLogin;
import hieudx.fpoly.warehousemanager.R;
import hieudx.fpoly.warehousemanager.databinding.ItemDialogDetailMemberBinding;
import hieudx.fpoly.warehousemanager.databinding.ItemRycMemberBinding;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.Viewholer> implements Filterable {
    private final Context context;
    private ArrayList<User> list;
    private ArrayList<User> mlist;
    private User_Dao userDao;

    public User_Adapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        this.mlist = list;
        userDao = new User_Dao(context);
    }

    @NonNull
    @Override
    public Viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRycMemberBinding binding = ItemRycMemberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholer(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholer holder, int position) {
        if (list.get(position).getAvatar().isEmpty())
            holder.binding.imgMember.setImageResource(R.mipmap.ic_launcher_foreground);
        else Picasso.get().load(list.get(position).getAvatar()).into(holder.binding.imgMember);
        holder.binding.nameMember.setText(list.get(position).getName());
        holder.binding.phoneMember.setText(list.get(position).getPhone());
        holder.binding.emailMember.setText(list.get(position).getEmail());

//        int role = list.get(position).getRole();
//        if (role == -1)holder.binding.switchLimit.setChecked(true);
//        else holder.binding.switchLimit.setChecked(false);

        holder.binding.switchLimit.setChecked(list.get(position).getRole() == 1);
        holder.binding.switchLimit.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                userDao.updateUserRoleById(list.get(position).getId(), 1);
                General.showSuccessPopup(context, "Thành công", "Bạn đã mở khóa "+list.get(position).getName()+ " thành công", new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
            } else {
                userDao.updateUserRoleById(list.get(position).getId(), -1);
                General.showSuccessPopup(context, "Thành công", "Bạn đã khóa "+list.get(position).getName()+ " thành công", new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
            }
        });
        holder.binding.cardViewMember.setOnClickListener(view -> OnClickGoToDetail(list.get(position)));
    }

    private void OnClickGoToDetail(User user) {
        ItemDialogDetailMemberBinding binding = ItemDialogDetailMemberBinding.inflate(LayoutInflater.from(context));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (user.getAvatar().isEmpty())
            binding.imgAvatar.setImageResource(R.mipmap.ic_launcher_foreground);
        else Picasso.get().load(user.getAvatar()).into(binding.imgAvatar);
        binding.edName.setText(user.getName());
        binding.edEmail.setText(user.getEmail());
        binding.edPhone.setText(user.getPhone());

        binding.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setCancelable(false);
            builder1.setTitle("Thông báo");
            builder1.setMessage("Bạn có chắc chắn muốn xóa?");
            builder1.setNegativeButton("Có", (dialog1, which) -> {
                switch (userDao.deleteUser(user.getId())) {
                    case 1:
                        list.clear();
                        list.addAll(userDao.getAllUser());
                        notifyDataSetChanged();
                        dialog1.dismiss();
                        dialog.dismiss();
                        General.showSuccessPopup(context, "Thành công", "Bạn đã xóa thành viên thành công", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
                        break;
                    case 0:
                        General.showFailurePopup(context, "Thất bại", "Xóa thành viên thất bại", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
                        break;
                    case -1:
                        General.showFailurePopup(context, "Thất bại", "Không thể xóa thành viên này", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
                        break;
                }
            });
            builder1.setPositiveButton("Không", null);
            builder1.show();
        });

        binding.imgClose.setOnClickListener(view -> dialog.dismiss());
    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if (TextUtils.isEmpty(search)) {
                    list = mlist;
                } else {
                    ArrayList<User> listFilter = new ArrayList<>();
                    for (User user : mlist) {
                        if (user.getName().toUpperCase().contains(search.toUpperCase())) {
                            listFilter.add(user);
                        }
                    }
                    list = listFilter;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Viewholer extends RecyclerView.ViewHolder {
        private ItemRycMemberBinding binding;

        public Viewholer(ItemRycMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
