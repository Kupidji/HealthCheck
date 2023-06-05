package com.example.healthcheck.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //Устанавливает белую тему
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //TODO прописать логику для темы
    }
}