package hieudx.fpoly.warehousemanager.Login_SignUp_Forget_Reset.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hieudx.fpoly.warehousemanager.General;
import hieudx.fpoly.warehousemanager.MainActivity;
import hieudx.fpoly.warehousemanager.Member.Dao.User_Dao;
import hieudx.fpoly.warehousemanager.databinding.FragmentLoginTabBinding;
import hieudx.fpoly.warehousemanager.Member.Model.User;
import hieudx.fpoly.warehousemanager.Login_SignUp_Forget_Reset.Activity.Forgot_Reset_Pass_Activity;


public class Login_Tab_Fragment extends Fragment {
    private FragmentLoginTabBinding binding;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public Login_Tab_Fragment() {
    }

    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginTabBinding.inflate(inflater, container, false);

        checkRemember();
        animation();

        binding.tvForgotPass.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), Forgot_Reset_Pass_Activity.class));
        });

        binding.btnLogin.setOnClickListener(view -> {
            String username = binding.edUsername.getText().toString();
            String pass = binding.edPass.getText().toString();
            if (username.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getContext(), "Hãy nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
            } else {
                User_Dao user_dao = new User_Dao(getContext());
                if (user_dao.checkLogin(username, md5(pass))) {
                    User user = user_dao.getUserByUsernameAndPassword(username,md5(pass));
                    if (user.getRole() == -1){
                        General.showFailurePopup(getContext(), "Thất bại", "Tài khoản của bạn đã bị khóa", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(android.app.Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
                    } else {
                        if (binding.cbRemember.isChecked()) {
                            editor.putString("username", username);
                            editor.putString("password", pass);
                            editor.putBoolean("isChecked", binding.cbRemember.isChecked());
                            editor.apply();
                        } else {
                            editor.clear();
                            editor.apply();
                        }
                        binding.edUsername.setText("");
                        binding.edPass.setText("");
                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }
                } else {
                    General.showFailurePopup(getContext(), "Thất bại", "Tài khoản hoặc mật khẩu không đúng", new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(android.app.Dialog dialog) {
                            super.onDismissClicked(dialog);
                        }
                    });
                }
            }
        });
        return binding.getRoot();
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
                    sb.append("0"+hex);
                } else {
                    sb.append(hex);
                }
            }
            Log.i("Chuoi md5: ",sb.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void checkRemember() {
        shared = requireActivity().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        editor = shared.edit(); // gọi dòng trên và edit vào nó
        boolean isCheck = shared.getBoolean("isChecked", false);
        if (isCheck) {
            binding.edUsername.setText(shared.getString("username", ""));
            binding.edPass.setText(shared.getString("password", ""));
            binding.cbRemember.setChecked(isCheck);
        }
    }

    private void animation() {
        binding.edUsername.setTranslationX(800);
        binding.pass.setTranslationX(800);
        binding.cbRemember.setTranslationX(800);
        binding.tvForgotPass.setTranslationX(800);
        binding.btnLogin.setTranslationX(800);
//
        binding.edUsername.setAlpha(0);
        binding.pass.setAlpha(0);
        binding.cbRemember.setAlpha(0);
        binding.tvForgotPass.setAlpha(0);
        binding.btnLogin.setAlpha(0);

        binding.edUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.cbRemember.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.tvForgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        binding.btnLogin.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }
}