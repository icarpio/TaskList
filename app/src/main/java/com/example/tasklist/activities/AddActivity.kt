package com.example.tasklist.activities

import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tasklist.R
import com.example.tasklist.data.Category
import com.example.tasklist.data.CategoryDAO
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityAddBinding
import com.example.tasklist.databinding.AddCategoryBinding
import com.example.tasklist.databinding.DialogEditBinding
import com.example.tasklist.network.RetrofitInstance
import com.example.tasklist.utils.DateTimePickerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var taskDAO: TaskDAO
    private lateinit var catDAO:CategoryDAO
    private var dateMax: LocalDateTime? = null
    private lateinit var dateTimePickerHelper: DateTimePickerHelper
    private lateinit var categories:List<Category>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateTimePickerHelper = DateTimePickerHelper(this)

        taskDAO = TaskDAO(this)
        catDAO = CategoryDAO(this)

        setupSpinner()

        binding.saveButton.setOnClickListener {
            val taskName = binding.nameEditText.text.toString()
            val selectedCategoryPosition = binding.categorySpinner.selectedItemPosition
            val selectedCategoryId = categories[selectedCategoryPosition].id
            // Verificar si taskName no está vacío
            if (taskName.isNotEmpty() && dateMax != null) {
                val dateFormat = dateMax!!.format(DateTimeFormatter.ISO_DATE_TIME)

                val task = Task(-1, taskName, false, dateFormat,selectedCategoryId)
                taskDAO.insertTask(task)
                Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show()
                // Finalizar la actividad después de guardar la tarea
                finish()
            } else {
                // Mostrar mensaje de error usando Toast si algún campo está vacío
                Toast.makeText(this, "Por favor, completa todos los campos y selecciona una fecha.", Toast.LENGTH_LONG).show()
            }
        }
        binding.selectDateMaxButton.setOnClickListener {
            showDatePickerDialog()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.phraseApiService.getPhrase()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val phrase = response.body()
                    binding.todayPhraseTextView.text = "Frase del dia:" + phrase?.phrase
                } else {
                    Log.e("RETROFIT", "Error en la solicitud: ${response.code()}")
                }
            }
        }
        binding.addCategoryButton.setOnClickListener{
            addCategory()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        dateTimePickerHelper.showDatePickerDialog { selectedDateTime ->
            dateMax = selectedDateTime
            binding.dateMaxSelectedTextView.text = dateMax.toString()
        }
    }

    private fun addCategory() {
        val categoryBinding = AddCategoryBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Añadir Categoria")
            .setView(categoryBinding.root)
            .setPositiveButton("Guardar") { dialog, which ->
                val categoryName = categoryBinding.categoryTextName.text.toString()
                val category = Category(-1,categoryName)
                catDAO.insertCategory(category)
                setupSpinner()
                Toast.makeText(this, "Categoria guardada correctamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    // Configurar el Spinner
    private fun setupSpinner() {
        categories = catDAO.getAllCategories()
        val categoryNames = categories.map { it.name }
        Log.d("CATEGORIAS", categoryNames.toString())
        val spinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = spinner
    }


}