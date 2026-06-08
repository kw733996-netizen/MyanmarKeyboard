package com.myanmarkeyboard
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.MotionEvent
import android.widget.*
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
class MyanmarKeyboardService : InputMethodService() {
    private var isMM = false
    private var isShift = false
    private var isCaps = false
    private val mmRows = arrayOf(
        arrayOf("ဆ","ဒ","ဂ","မ","တ","ယ","ဥ","ိ","ာ","ျ"),
        arrayOf("ဝ","သ","ရ","န","ခ","ပ","လ","ှ","ေ","း"),
        arrayOf("ဇ","ထ","ဖ","ဗ","ဘ","ညာ","ော","ု","ူ")
    )
    private val enRows = arrayOf(
        arrayOf("q","w","e","r","t","y","u","i","o","p"),
        arrayOf("a","s","d","f","g","h","j","k","l"),
        arrayOf("z","x","c","v","b","n","m")
    )
    private val numRows = arrayOf(
        arrayOf("၁","၂","၃","၄","၅","၆","၇","၈","၉","၀"),
        arrayOf("1","2","3","4","5","6","7","8","9","0"),
        arrayOf("-","/",":",";","!","?",",",".")
    )
    private var isNum = false
    override fun onCreateInputView(): View = buildKB()
    private fun buildKB(): LinearLayout {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#282828"))
            setPadding(4,4,4,8)
        }
        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(8,4,8,4)
        }
        val langBtn = btn(if(isMM)"မြန်မာ" else "English","#4285f4",110,36)
        langBtn.setOnClickListener { isMM=!isMM; isShift=false; isCaps=false; setInputView(buildKB()) }
        val numBtn = btn(if(isNum)"ABC" else "123","#3a3a3a",80,36)
        numBtn.setOnClickListener { isNum=!isNum; setInputView(buildKB()) }
        topBar.addView(langBtn)
        topBar.addView(View(this).apply { layoutParams=LinearLayout.LayoutParams(0,1,1f) })
        topBar.addView(numBtn)
        root.addView(topBar)
        val rows = if(isNum) numRows else if(isMM) mmRows else enRows
        rows.forEachIndexed { ri, row ->
            val rl = LinearLayout(this).apply {
                orientation=LinearLayout.HORIZONTAL
                gravity=android.view.Gravity.CENTER
                setPadding(4,0,4,0)
                layoutParams=LinearLayout.LayoutParams(-1,-2).apply{setMargins(0,3,0,3)}
            }
            if(ri==2 && !isNum){
                val sh=btn(if(isCaps)"⇪" else "⇧",if(isCaps)"#4285f4" else "#4a4a4a",46,44)
                sh.setOnClickListener{
                    if(isCaps){isCaps=false;isShift=false}
                    else if(isShift){isCaps=true}
                    else{isShift=true}
                    setInputView(buildKB())
                }
                rl.addView(sh)
            }
            row.forEach { k ->
                val kb=Button(this).apply{
                    text=if(!isMM&&(isShift||isCaps))k.uppercase() else k
                    setTextColor(Color.WHITE); textSize=14f
                    setPadding(0,0,0,0)
                    layoutParams=LinearLayout.LayoutParams(0,dpToPx(44),1f).apply{setMargins(3,0,3,0)}
                    background=GradientDrawable().apply{setColor(Color.parseColor("#3a3a3a"));cornerRadius=dpToPx(6).toFloat()}
                }
                kb.setOnClickListener{
                    val out=if(!isMM&&(isShift||isCaps))k.uppercase() else k
                    currentInputConnection?.commitText(out,1)
                    if(isShift&&!isCaps){isShift=false;setInputView(buildKB())}
                }
                rl.addView(kb)
            }
            if(ri==2){
                val bs=btn("⌫","#4a4a4a",46,44)
                bs.setOnClickListener{currentInputConnection?.deleteSurroundingText(1,0)}
                bs.setOnLongClickListener{currentInputConnection?.deleteSurroundingText(10,0);true}
                rl.addView(bs)
            }
            root.addView(rl)
        }
        val bl=LinearLayout(this).apply{
            orientation=LinearLayout.HORIZONTAL
            gravity=android.view.Gravity.CENTER
            setPadding(4,3,4,3)
        }
        val sp=Button(this).apply{
            text="space (ဖိ=ဘာသာပြောင်း)"
            setTextColor(Color.parseColor("#aaaaaa")); textSize=12f
            layoutParams=LinearLayout.LayoutParams(0,dpToPx(44),1f).apply{setMargins(6,0,6,0)}
            background=GradientDrawable().apply{setColor(Color.parseColor("#4a4a4a"));cornerRadius=dpToPx(6).toFloat()}
        }
        val h=Handler(Looper.getMainLooper()); var lr:Runnable?=null
        sp.setOnTouchListener{_,e->
            when(e.action){
                MotionEvent.ACTION_DOWN->{lr=Runnable{isMM=!isMM;isShift=false;isCaps=false;setInputView(buildKB())};h.postDelayed(lr!!,500)}
                MotionEvent.ACTION_UP->{if(lr!=null){h.removeCallbacks(lr!!);currentInputConnection?.commitText(" ",1);lr=null}}
                MotionEvent.ACTION_CANCEL->{lr?.let{h.removeCallbacks(it)};lr=null}
            };true
        }
        val en=btn("↵","#3a3a3a",70,44)
        en.setTextColor(Color.parseColor("#8ab4f8"))
        en.setOnClickListener{currentInputConnection?.performEditorAction(android.view.inputmethod.EditorInfo.IME_ACTION_DONE)}
        bl.addView(sp); bl.addView(en)
        root.addView(bl)
        return root
    }
    private fun btn(t:String,c:String,w:Int,h:Int)=Button(this).apply{
        text=t; setTextColor(Color.WHITE); textSize=12f; setPadding(0,0,0,0)
        layoutParams=LinearLayout.LayoutParams(dpToPx(w),dpToPx(h)).apply{setMargins(3,0,3,0)}
        background=GradientDrawable().apply{setColor(Color.parseColor(c));cornerRadius=dpToPx(6).toFloat()}
    }
    private fun dpToPx(dp:Int)=(dp*resources.displayMetrics.density).toInt()
}
