package hieudx.fpoly.warehousemanager.Login_SignUp_Forget_Reset.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hieudx.fpoly.warehousemanager.General;
import hieudx.fpoly.warehousemanager.Member.Dao.User_Dao;
import hieudx.fpoly.warehousemanager.databinding.FragmentResetBinding;
import hieudx.fpoly.warehousemanager.Member.Model.User;
import hieudx.fpoly.warehousemanager.Login_SignUp_Forget_Reset.Activity.Login_SignUp_Activity;

public class Reset_Fragment extends Fragment {
    private FragmentResetBinding binding;
    private User_Dao userDao;
    private User user;
    int userId;
    String newPassword, confirmNewPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Reset_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetBinding.inflate(inflater, container, false);

        //lay du lieu trong budle
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("userID", -1);
        }

        binding.imgBack.setOnClickListener(view -> {
            //FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.btnResetPass.setOnClickListener(view -> {
            resetPass();
        });
        return binding.getRoot();
    }

    private void resetPass() {
        newPassword = binding.edNewPass.getText().toString().trim();
        confirmNewPassword = binding.edReNewPass.getText().toString().trim();

        validationNewPass();
        validationReNewPass();

        if (binding.NewPass.getError() == null && binding.ReNewPass.getError() == null) {
            userDao = new User_Dao(getActivity());
            if (userDao.updatePasswordUser(userId, md5(newPassword))) {
                General.showSuccessPopup(getContext(), "Thành công", "Bạn đã đổi mật khẩu thành công", new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                        startActivity(new Intent(requireActivity(), Login_SignUp_Activity.class));
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String md5(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff; // add salt
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            Log.i("Chuoi md5: ", sb.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void validationNewPass() {
        int passwordLength = newPassword.length();
        User_Dao userDao = new User_Dao(getActivity());
        User user = userDao.getUserById(userId);
        if (newPassword.isEmpty()) {
            binding.NewPass.setError("Vui lòng nhập mật khẩu");
        } else if (newPassword.contains(" ")) {
            binding.NewPass.setError("Mật khẩu không được chứa khoảng trắng");
        } else if (passwordLength < 5 || passwordLength >= 10) {
            binding.NewPass.setError("Mật khẩu phải có ít nhất 5 ký tự và nhỏ hơn 10 ký tự");
        } else if (md5(newPassword).equals(user.getPassword())) {
            binding.NewPass.setError("Mật khẩu mới không được trùng với mật khẩu cũ");
        } else {
            binding.NewPass.setError(null);
        }
    }

    private void validationReNewPass() {
        if (confirmNewPassword.isEmpty()) {
            binding.ReNewPass.setError("Vui lòng nhập lại mật khẩu");
        } else if (!confirmNewPassword.equals(newPassword)) {
            binding.ReNewPass.setError("Mật khẩu nhập lại không khớp");
        } else {
            binding.ReNewPass.setError(null);
        }
    }
}