package hieudx.fpoly.warehousemanager.Member.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import hieudx.fpoly.warehousemanager.General;
import hieudx.fpoly.warehousemanager.Member.Dao.User_Dao;
import hieudx.fpoly.warehousemanager.Member.Model.User;
import hieudx.fpoly.warehousemanager.databinding.FragmentAddMemberBinding;

public class Member_Add_Fragment extends Fragment {
    private FragmentAddMemberBinding binding;

    public Member_Add_Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMemberBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        General.onStateIconBack(getActivity(), actionBar, getParentFragmentManager(), false);

        validForm();
        onClickAddMember();
    }

    private void onClickAddMember() {
        binding.btnAdd.setOnClickListener(view -> {
            String avt = binding.edAvt.getText().toString().trim();
            String name = binding.edName.getText().toString().trim();
            String username = binding.edUsername.getText().toString().trim();
            String phone = binding.edPhone.getText().toString().trim();
            String email = binding.edEmail.getText().toString().trim();
            String pass = binding.edPass.getText().toString().trim();

            if (!TextUtils.isEmpty(avt)) General.isContainSpace(avt, binding.avt);
            General.isContainNumber(name, binding.name);
            General.isContainSpace(username, binding.username);
            General.isContainSpace(phone, binding.phone);
            General.validPhone(phone, binding.phone);
            General.validEmail(email, binding.email);
            General.isContainSpace(pass, binding.pass);

            if (TextUtils.isEmpty(username) ||
                    TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(phone) ||
                    TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), "Hãy nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
            } else if (binding.avt.getError() == null
                    && binding.name.getError() == null
                    && binding.username.getError() == null
                    && binding.phone.getError() == null
                    && binding.email.getError() == null
                    && binding.pass.getError() == null) {
                User_Dao user_dao = new User_Dao(getContext());
                User user = new User(username, pass, name, email, phone, 1, avt);
                switch (user_dao.insert(user)){
                    case 1:
                        Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                        break;
                    case 0:
                        Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(getContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        break;
                    default:break;
                }
            }
        });

        binding.btnClear.setOnClickListener(view -> {
            binding.edAvt.setText("");
            binding.edName.setText("");
            binding.edUsername.setText("");
            binding.edPhone.setText("");
            binding.edEmail.setText("");
            binding.edPass.setText("");
        });
    }

    private void validForm() {
        General.isEmptyValid(binding.edName, binding.name);
        General.isEmptyValid(binding.edUsername, binding.username);
        General.isEmptyValid(binding.edPhone, binding.phone);
        General.isEmptyValid(binding.edEmail, binding.email);
        General.isEmptyValid(binding.edPass, binding.pass);
    }
}