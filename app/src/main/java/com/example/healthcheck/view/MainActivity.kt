package com.example.healthcheck.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.databinding.ActivityMainBinding
import com.example.healthcheck.util.Constants
import java.util.Calendar

////////██╗░░██╗███████╗░█████╗░██╗░░░░░████████╗██╗░░██╗////////░█████╗░██╗░░██╗███████╗░█████╗░██╗░░██╗
////////██║░░██║██╔════╝██╔══██╗██║░░░░░╚══██╔══╝██║░░██║////////██╔══██╗██║░░██║██╔════╝██╔══██╗██║░██╔╝
////////███████║█████╗░░███████║██║░░░░░░░░██║░░░███████║////////██║░░╚═╝███████║█████╗░░██║░░╚═╝█████═╝░
////////██╔══██║██╔══╝░░██╔══██║██║░░░░░░░░██║░░░██╔══██║////////██║░░██╗██╔══██║██╔══╝░░██║░░██╗██╔═██╗░
////////██║░░██║███████╗██║░░██║███████╗░░░██║░░░██║░░██║////////╚█████╔╝██║░░██║███████╗╚█████╔╝██║░╚██╗
////////╚═╝░░╚═╝╚══════╝╚═╝░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝////////░╚════╝░╚═╝░░╚═╝╚══════╝░╚════╝░╚═╝░░╚═╝
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }

}