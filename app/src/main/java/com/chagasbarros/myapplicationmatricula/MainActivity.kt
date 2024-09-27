package com.chagasbarros.myapplicationmatricula

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Aluno(
    val nome: String,
    val turma: String,
    val notas: List<Double>,
    val media: Double,
    val situacao: String
)

class SistemaMatricula {
    val alunos = mutableListOf<Aluno>()

    fun matricularAluno(nome: String, turma: String, notas: List<Double>) {
        if (nome.isBlank() || turma.isBlank()) {
            println("Erro: Nome do aluno ou turma não podem estar vazios.")
            return
        }

        if (notas.size != 4) {
            println("Erro: O aluno deve ter exatamente 4 notas")
            return
        }

        val media = calcularMedia(notas)
        val situacao = if (media >= 7.0) "Aprovado" else "Reprovado"

        val aluno =  Aluno(nome, turma, notas, media, situacao)
        alunos.add(aluno)
        println("Aluno $nome matriculado com sucesso!")

    }

    fun calcularMedia(notas: List<Double>): Double {
        return notas.average().toDouble()
    }


    companion object {
        fun consultarAluno(nome: String, sistemaMatricula: SistemaMatricula): String {
            val alunoConsulta = sistemaMatricula.alunos.find {it.nome.equals(nome, ignoreCase = true) }
            return if (alunoConsulta != null) {
                "${alunoConsulta.nome} está na turma ${alunoConsulta.turma} com média ${alunoConsulta.media} e está ${alunoConsulta.situacao}."
            } else {
                "Erro: Aluno $nome não encontrado."
            }
        }
    }
}

class MainActivity : AppCompatActivity() {

    val sistemaMatricula = SistemaMatricula()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val editNomeConsulta = findViewById<EditText>(R.id.EditNomeConsulta)
        val buttonConsultar = findViewById<Button>(R.id.buttonConsultar)
        val textSaidaConsulta = findViewById<TextView>(R.id.textSaidaConsulta)
        val textNomeMatricula = findViewById<EditText>(R.id.textNomeMatricula)
        val textNota1 = findViewById<EditText>(R.id.textNota1)
        val textNota2 = findViewById<EditText>(R.id.textNota2)
        val textNota3 = findViewById<EditText>(R.id.textNota3)
        val textNota4 = findViewById<EditText>(R.id.textNota4)
        val spinnerTurma = findViewById<Spinner>(R.id.spinnerTurma)
        val buttonMatricular = findViewById<Button>(R.id.buttonMatricular)
        val textSaidaMatricula = findViewById<TextView>(R.id.textMatricula)


        buttonConsultar.setOnClickListener {
            val nome = editNomeConsulta.text.toString()
            val resultado = SistemaMatricula.consultarAluno(nome, sistemaMatricula)
            textSaidaConsulta.text = resultado
        }

        buttonMatricular.setOnClickListener{
            val nomeCompleto = textNomeMatricula.text.toString()
            val turma = spinnerTurma.selectedItem.toString()
            val nota = listOf(
                textNota1.text.toString().toDoubleOrNull(),
                textNota2.text.toString().toDoubleOrNull(),
                textNota3.text.toString().toDoubleOrNull(),
                textNota4.text.toString().toDoubleOrNull()
            )

            val notasValidas = nota.all { it != null && it in 0.0..10.0}

            if (nomeCompleto.isNotBlank() && notasValidas) {
                sistemaMatricula.matricularAluno(nomeCompleto, turma, nota.map{it!!})
                textSaidaMatricula.text = "Aluno $nomeCompleto matriculado na $turma com sucesso! "
            }else {
                textSaidaMatricula.text = "Preencha todos os campos correntamente!"
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}