package com.example.Eksamen

import com.example.Eksamen.db.Card
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class SecondaryController {

    //////////////////influx
    @Autowired
    private val meterRegistry: MeterRegistry? = null

    @PostMapping(path = ["/tx"], consumes = ["application/json"], produces = ["application/json"])
    fun addMember(@RequestBody tx: Card) {
        meterRegistry!!.counter("txcount2", "currency", tx.name).increment()
        meterRegistry.gauge("txcount4", 3)
    }

    @GetMapping(path = ["/"])
    fun home() : String{
        meterRegistry?.gauge("txcount4", 3)
        return "Welcome to home page, will this be visible?"
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