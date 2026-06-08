package com.myanmarkeyboard
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.view.Gravity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1a1a2e"))
            setPadding(40,60,40,60)
        }
        root.addView(TextView(this).apply {
            text = "🇲🇲 Myanmar Keyboard"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0,0,0,30)
        })
        val btn1 = Button(this).apply {
            text = "Step 1: Enable Keyboard"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#4285f4"))
            layoutParams = LinearLayout.LayoutParams(-1,120).apply { setMargins(0,0,0,16) }
        }
        btn1.setOnClickListener { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) }
        val btn2 = Button(this).apply {
            text = "Step 2: Switch Keyboard"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#34a853"))
            layoutParams = LinearLayout.LayoutParams(-1,120).apply { setMargins(0,0,0,16) }
        }
        btn2.setOnClickListener {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
        }
        root.addView(btn1)
        root.addView(btn2)
        root.addView(TextView(this).apply {
            text = "💡 Space ဖိထား = မြန်မာ/English ပြောင်း"
            textSize = 14f
            setTextColor(Color.parseColor("#aaaaaa"))
            gravity = Gravity.CENTER
            setPadding(0,20,0,0)
        })
        setContentView(root)
    }
}
