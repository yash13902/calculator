package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val map = mapOf(
        0 to "+",
        1 to "-",
        2 to "*",
        3 to "/",
    )

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewCalc.text = ""

        val numbers = arrayOf(
            num0,
            num1,
            num2,
            num3,
            num4,
            num5,
            num6,
            num7,
            num8,
            num9,
        ).forEachIndexed{index, textView ->
            textView.setOnClickListener{
                textViewCalc.text = textViewCalc.text.toString() + index.toString()
            }
        }



        val signs = arrayOf(
            plus,
            minus,
            multiply,
            divide,
        ).forEachIndexed{index, textView ->
            textView.setOnClickListener{
                val x = textViewCalc.text.toString()[textViewCalc.text.toString().length-1]
                if(x != '+' && x != '-' && x != '*' && x != '/') {
                    textViewCalc.text = textViewCalc.text.toString() + map[index]
                } else{
                    Toast.makeText(this, "cannot add operator please add number!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        clear.setOnClickListener{
            textViewCalc.text = ""
            textViewResult.text = "Result: "
        }

        equal.setOnClickListener{
            if(textViewCalc.text.toString().isNotEmpty()) {
                val x = textViewCalc.text.toString()[textViewCalc.text.toString().length - 1]
                if (x != '+' && x != '-' && x != '*' && x != '/') {
                    postfix(textViewCalc.text.toString())
                } else {
                    Toast.makeText(
                        this,
                        "there cannot be an operator at the end!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    this,
                    "Please give an expression",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun postfix(expression: String){
        val exp = "$expression "
        var p=0
        var str: String
        val arr =  ArrayList<String>()
        for (i in exp.indices){
            if (exp[i] == '+' || exp[i] == '-' || exp[i] == '*' || exp[i] == '/' || exp[i] == ' '){
                str = exp.substring(p, i)
                arr.add(str)
                arr.add(exp[i].toString())
                p = i+1
            }
        }
        arr.removeLast()
        val stack = mutableListOf<String>()
        val op = mutableListOf<String>(" ")
        var str1: String
        for(i in arr.indices) {
            if(arr[i] != "+" && arr[i] != "-" && arr[i] != "*" && arr[i] != "/"){
                stack.add(arr[i])
            }else {
                if(op.last() == " "){
                    op.add(arr[i])
                }else if(arr[i] == "*" || arr[i] == "/"){
                    while((op.last() != " ") && (op.last() != "-" && op.last() != "+")){
                        str1 = op.last()
                        op.removeLast()
                        stack.add(str1)
                    }
                    op.add(arr[i])
                } else if (arr[i] == "+" || arr[i] == "-"){
                    while(op.last() != " "){
                        str1 = op.last()
                        op.removeLast()
                        stack.add(str1)
                    }
                    op.add(arr[i])
                }
            }
        }
        while(op.last() != " "){
            str1 = op.last()
            op.removeLast()
            stack.add(str1)
        }
        Log.d(TAG, "stack: $stack")
        Log.d(TAG, "op: $op")

        evaluate(stack)
    }

    private fun evaluate(exp: List<String>){

        val l = exp.size
        var str1: String
        var str2: String
        val stack = mutableListOf<String>()
        var result: Double
        for(i in exp.indices){
            if(exp[i] != "+" && exp[i] != "-" && exp[i] != "*" && exp[i] != "/" ){
                stack.add(exp[i])
            } else{
                str1 = stack.last()
                stack.removeLast()
                str2 = stack.last()
                stack.removeLast()
                when(exp[i]){
                    "+" -> {
                        result = str2.toDouble() + str1.toDouble()
                        stack.add(result.toString())
                    }
                    "-" -> {
                        result = str2.toDouble() - str1.toDouble()
                        stack.add(result.toString())
                    }
                    "*" -> {
                        result = str2.toDouble() * str1.toDouble()
                        stack.add(result.toString())
                    }
                    "/" -> {
                        result = str2.toDouble() / str1.toDouble()
                        stack.add(result.toString())
                    }
                }
            }
        }
        Log.d(TAG, "evaluate: $stack")
        textViewResult.text = "Result: "
        textViewResult.text = textViewResult.text.toString() + "of "+ textViewCalc.text.toString()+"= "+ stack[0]
        textViewCalc.text = ""
    }

}