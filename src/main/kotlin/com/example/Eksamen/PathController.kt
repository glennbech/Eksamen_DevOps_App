package com.example.Eksamen

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PathController {
    @GetMapping(path = ["/"])
    fun home() : String{
        return "Welcome to home page"
    }

    @GetMapping(path = ["/page1"])
    fun page1() : String{
        return "Does page 1 work?"
    }

    @GetMapping(path = ["/page2"])
    fun page2() : String{
        return "Does page 2 work?"
    }

    @GetMapping(path = ["/page3"])
    fun page3() : String{
        return "Does page 3 work?"
    }

    @GetMapping(path = ["/page4"])
    fun page4() : String{
        return "Does page 1 work?"
    }
}