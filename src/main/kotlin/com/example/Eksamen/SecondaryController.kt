package com.example.Eksamen

import com.example.Eksamen.db.Card
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class SecondaryController(@Autowired private var meterRegistry: MeterRegistry) {

    //////////////////influx

    @Autowired
    fun SecondaryController(meterRegistry: MeterRegistry?) {
        this.meterRegistry = meterRegistry!!
    }


    private val counter1 = Counter.builder("Home_counter").description("Counter for cards").register(meterRegistry)
    private val gauge = meterRegistry.gauge("Gauge for /", 3)



    @GetMapping(path = ["/"])
    fun home(@RequestBody card: Card) : String{
        gauge
        counter1.increment()
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