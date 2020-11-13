package com.example.Eksamen

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(EksamenApplication::class.java, "--spring.profiles.active=test")
}