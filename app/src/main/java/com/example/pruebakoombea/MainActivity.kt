package com.example.pruebakoombea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val words = "hola adios caballo"
        longestWord(words)

        println("$words en reversa: ${firstReverse(words)}")

        val a = "arrb6???4xxbl5???eee5"
        println("question marks de ${a} es " + questionMarks(a))

    }

    private fun longestWord(str: String) : String {
        val re = Regex("[^A-Za-z0-9 ]")
        val filterStr = re.replace(str, "")
        println("String sin carcateres especiales: $filterStr")
        var words = filterStr.split(" ")
        words = words.reversed()
        println("Nuevo orden: $words")
        println("Número de palabras: ${words.size}")
        var longest = ""
        for (word in words) {
            if(word.length > longest.length) {
                longest = word
            }
        }
        println("Palabra más larga: $longest")
        return longest
    }

    private fun firstReverse(str: String) : String {
        return str.reversed()
    }

    private fun questionMarks(str: String) : String {
        var numberOfQuestionMarks = 0
        var n1: Int? = null
        var n2: Int? = null
        for(i in str){
            println("i: $i")
            if(i.isDigit() && n1 == null){
                println("n1 = i es digito: $i")
                // toInt en un char NO convierte el número literal a int, sino que dveuelve el valor del char
                n1 = i.toString().toInt()
                println("n1 = $n1")
            }
            else if (i.isDigit() && n2 == null){
                println("n2 = i es digito: $i")
                n2 = i.toString().toInt()
                val n = n1?.plus(n2)
                println("n1 = $n1")
                println("n2 = $n2")
                println("n = $n")
                //println("Suma: $n1 + $n2 = $n")
                println("Question marks: $numberOfQuestionMarks")
                if(n!=10 || numberOfQuestionMarks!=3){
                    return "false"
                }
                n1 = null
                n2 = null
                numberOfQuestionMarks = 0
            }
            else if(i == '?'){
                numberOfQuestionMarks++
            }

        }
        return "true"
    }


}