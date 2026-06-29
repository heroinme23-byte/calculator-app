package com.kalex.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context as RhinoContext
import org.mozilla.javascript.Scriptable

class CalculatorViewModel: ViewModel() {
    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData ("0")
    val resultText: LiveData<String> = _resultText
    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        _equationText.value?.let {
            if (btn == "AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }
            if (btn == "C") {
                if (it.isNotEmpty()) {
                    _equationText.value = it.substring(0, it.length - 1)
                }
                return
            }
            if (btn == "=") {
                _equationText.value = _resultText.value
                return
            }

            val textToAppend = if (btn == "sin" || btn == "cos" || btn == "tan" || btn == "nPr" || btn == "nCr") {
                "$btn("
            } else {
                btn
            }

            _equationText.value = it + textToAppend

            try {
                _resultText.value = calculateResult(_equationText.value.toString())
            } catch (_: Exception) {}
        }
    }




    fun calculateResult(equation: String): String {
        val context: RhinoContext = RhinoContext.enter()
        context.optimizationLevel = -1
        val scriptable: Scriptable = context.initStandardObjects()

        val setup = """
            function sin(deg) { return Math.sin(deg * Math.PI / 180); }
            function cos(deg) { return Math.cos(deg * Math.PI / 180); }
            function tan(deg) { return Math.tan(deg * Math.PI / 180); }
            
            function factorial(n) {
                if (n < 0) return NaN;
                if (n <= 1) return 1;
                var result = 1;
                for (var i = 2; i <= n; i++) {
                    result *= i;
                }
                return result;
            }
            
            function nPr(n, r) {
                return factorial(n) / factorial(n - r);
            }
            
            function nCr(n, r) {
                return factorial(n) / (factorial(r) * factorial(n - r));
            }
        """.trimIndent()

        context.evaluateString(scriptable, setup, "Setup", 1, null)

        // Convert "X%" into "(X/100)" before evaluating
        val processedEquation = equation.replace(Regex("(\\d+(\\.\\d+)?)%")) {
            "(${it.groupValues[1]}/100)"
        }

        val rawResult = context.evaluateString(scriptable, processedEquation, "Javascript", 1, null).toString()

        val finalResult = try {
            val number = rawResult.toDouble()
            "%.10f".format(number).trimEnd('0').trimEnd('.')
        } catch (_: Exception) {
            rawResult
        }

        return finalResult
    }



}