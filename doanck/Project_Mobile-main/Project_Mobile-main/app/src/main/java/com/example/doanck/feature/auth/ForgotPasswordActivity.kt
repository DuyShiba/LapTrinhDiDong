package com.example.doanck.feature.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doanck.R
import com.example.doanck.data.remote.supabase.SupabaseAuthClient
import com.example.doanck.data.remote.supabase.SupabaseConfig
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSend: MaterialButton
    private lateinit var tvBack: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.etForgotEmail)
        btnSend = findViewById(R.id.btnSendReset)
        tvBack = findViewById(R.id.tvBackForgot)

        tvBack.setOnClickListener { finish() }
        btnSend.setOnClickListener { sendRecover() }
    }

    private fun sendRecover() {
        val email = etEmail.text?.toString()?.trim().orEmpty()
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Email không hợp lệ"; return
        }

        val body = mapOf(
            "email" to email,
            "redirect_to" to SupabaseConfig.RECOVER_REDIRECT_TO
        )

        SupabaseAuthClient.service.recover(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ForgotPasswordActivity, "Đã gửi email reset. Hãy mở email và bấm link.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, SupabaseAuthClient.parseError(response), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ForgotPasswordActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
