package com.example.Eksamen

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit


@RestController
class SecondaryController(@Autowired private var meterRegistry: MeterRegistry) {

    //////////////////influx

    @Autowired
    fun SecondaryController(meterRegistry: MeterRegistry?) {
        this.meterRegistry = meterRegistry!!
    }


    private val logger = LoggerFactory.getLogger(PathController::class.java.name)

    private val counter1 = Counter.builder("Home_counter").description("Counter for cards").register(meterRegistry)
    private val gauge = meterRegistry.gauge("Gauge for /", 3)




    @GetMapping(path = ["/"])
    fun home() : String{
        gauge
        counter1.increment()
        logger.info("Fetching homepage")
        return "Welcome to home page, will this be visible?"
    }




    @GetMapping(path = ["/page1"])
    fun page1() : String{

        logger.info("Fetching page1")
        return meterRegistry.more().longTaskTimer("long.task.timer.get.card").recordCallable {
            TimeUnit.MILLISECONDS.sleep(3500)
            return@recordCallable "Does page 1 work?"
        }

    }

    @GetMapping(path = ["/page2"])
    fun page2() : String{
        logger.info("Fetching page2")
        return "Does page 2 work?"
    }

    @GetMapping(path = ["/page3"])
    fun page3() : String{
        logger.info("Fetching page3")
        return "Does page 3 work?"
    }

    @GetMapping(path = ["/page4"])
    fun page4() : String{
        logger.info("Fetching page4")
        return "Does page 1 work?"
    }
}