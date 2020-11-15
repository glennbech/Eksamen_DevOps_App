package com.example.Eksamen

import com.example.Eksamen.db.Card
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class SecondaryController(@Autowired private var meterRegistry: MeterRegistry) {

    //////////////////influx

    private val counterRegistry = Counter.builder("Counter for page 1").description("Is this working?").register(meterRegistry)
    private val gauge = meterRegistry.gauge("Gauge for /", 3)



    @GetMapping(path = ["/"])
    fun home() : String{
        gauge
        return "Welcome to home page, will this be visible?"
    }

    @GetMapping(path = ["/page1"])
    fun page1(@RequestBody card: Card) : String{
        meterRegistry.counter("txcount2", "currency", card.name).increment()
        return "Does page 1 work?"
    }

    @GetMapping(path = ["/page2"])
    fun page2() : String{
        counterRegistry.increment()
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