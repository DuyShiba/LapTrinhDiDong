package com.example.doanck.feature.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doanck.R
import com.example.doanck.data.remote.supabase.AuthStore
import com.example.doanck.data.remote.supabase.SignUpResponse
import com.example.doanck.data.remote.supabase.SupabaseAuthClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFirst: TextInputEditText
    private lateinit var etLast: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPass: TextInputEditText
    private lateinit var etConfirm: TextInputEditText
    private lateinit var etAvatar: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvBack: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirst = findViewById(R.id.etFirstName)
        etLast = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etRegisterEmail)
        etPass = findViewById(R.id.etRegisterPassword)
        etConfirm = findViewById(R.id.etRegisterConfirm)
        etAvatar = findViewById(R.id.etAvatarUrl)
        btnRegister = findViewById(R.id.btnRegister)
        tvBack = findViewById(R.id.tvBackToLogin)

        tvBack.setOnClickListener { finish() }
        btnRegister.setOnClickListener { doRegister() }
    }

    private fun doRegister() {
        val first = etFirst.text?.toString()?.trim().orEmpty()
        val last = etLast.text?.toString()?.trim().orEmpty()
        val email = etEmail.text?.toString()?.trim().orEmpty()
        val pass = etPass.text?.toString()?.trim().orEmpty()
        val confirm = etConfirm.text?.toString()?.trim().orEmpty()
        val avatar = etAvatar.text?.toString()?.trim().orEmpty()

        if (first.isBlank() || last.isBlank() || email.isBlank() || pass.isBlank() || confirm.isBlank()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show(); return
        }
        if (pass != confirm) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show(); return
        }

        val data = mutableMapOf<String, Any>(
            "first_name" to first,
            "last_name" to last
        )
        if (avatar.isNotBlank()) data["avatar_url"] = avatar

        val body = mapOf(
            "email" to email,
            "password" to pass,
            "data" to data
        )

        SupabaseAuthClient.service.signUp(body).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val r = response.body()!!
                    val session = r.session
                    if (!session?.accessToken.isNullOrBlank()) {
                        AuthStore.save(this@RegisterActivity, session!!.accessToken, session.refreshToken, r.user?.id, r.user?.email ?: email)
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công! Kiểm tra email để xác minh.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, SupabaseAuthClient.parseError(response), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
