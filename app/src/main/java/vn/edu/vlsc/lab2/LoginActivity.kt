package vn.edu.vlsc.lab2

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vn.edu.vlsc.lab2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener { handleLogin() }

        // Báo Toast khi bấm "Quên mật khẩu?" (Cấp 1)
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, R.string.msg_forgot_password_click, Toast.LENGTH_SHORT).show()
        }

        // Bật/tắt hiện mật khẩu qua CheckBox (Cấp 2)
        binding.cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            // Di chuyển con trỏ xuống cuối văn bản
            binding.edtPassword.setSelection(binding.edtPassword.text?.length ?: 0)
        }
    }

    private fun handleLogin() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        // Xóa thông báo lỗi của lần bấm trước
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        when {
            email.isEmpty() || password.isEmpty() -> {
                Toast.makeText(this, R.string.msg_missing_info, Toast.LENGTH_SHORT).show()
                binding.tvStatus.setText(R.string.status_missing)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                binding.tilEmail.error = getString(R.string.err_email_invalid)
            password.length < 6 ->
                binding.tilPassword.error = getString(R.string.err_password_short)
            else -> {
                binding.tvStatus.text = getString(R.string.status_login_ok, email)
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.EXTRA_EMAIL, email)
                startActivity(intent)
            }
        }
    }
}
