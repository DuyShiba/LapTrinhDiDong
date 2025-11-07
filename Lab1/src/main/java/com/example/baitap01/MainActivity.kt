package com.example.baitap01

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Khai báo các view (lateinit tránh null)
    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var resultTextView: TextView

    private lateinit var arrayInputEditText: EditText
    private lateinit var processArrayButton: Button
    private lateinit var evenNumbersTextView: TextView
    private lateinit var oddNumbersTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ẩn thanh tiêu đề + full màn hình
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Gắn layout
        setContentView(R.layout.activity_main)

        // Liên kết ID trong XML
        editText = findViewById(R.id.edit_text)
        button = findViewById(R.id.button)
        resultTextView = findViewById(R.id.result_text_view)

        arrayInputEditText = findViewById(R.id.array_input_edit_text)
        processArrayButton = findViewById(R.id.process_array_button)
        evenNumbersTextView = findViewById(R.id.even_numbers_text_view)
        oddNumbersTextView = findViewById(R.id.odd_numbers_text_view)

        // ===========================
        // 🔹 Yêu cầu 5: Đảo chuỗi
        // ===========================
        button.setOnClickListener {
            val input = editText.text.toString().trim()

            if (input.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập một chuỗi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reversed = input
                .split("\\s+".toRegex())
                .filter { it.isNotEmpty() }
                .reversed()
                .joinToString(" ")
                .uppercase()

            resultTextView.text = "Chuỗi đảo: $reversed"
            Toast.makeText(this, reversed, Toast.LENGTH_LONG).show()
        }

        // ===========================
        // 🔹 Yêu cầu 4: Xử lý dãy số
        // ===========================
        processArrayButton.setOnClickListener {
            val input = arrayInputEditText.text.toString().trim()

            if (input.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập dãy số!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val even = mutableListOf<Int>()
            val odd = mutableListOf<Int>()

            try {
                val numbers = input.split(",")
                for (numStr in numbers) {
                    val number = numStr.trim().toInt()
                    if (number % 2 == 0) even.add(number) else odd.add(number)
                }

                evenNumbersTextView.text = "Số chẵn: ${even.joinToString(", ")}"
                oddNumbersTextView.text = "Số lẻ: ${odd.joinToString(", ")}"

            } catch (e: Exception) {
                Toast.makeText(this, "Dãy số không hợp lệ! (vd: 1,2,3,4)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
