package com.example.Eksamen

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.util.concurrent.TimeUnit


@RestController
class SecondaryController(@Autowired private var meterRegistry: MeterRegistry) {

    //////////////////influx

    @Autowired
    fun SecondaryController(meterRegistry: MeterRegistry?) {
        this.meterRegistry = meterRegistry!!
    }




    private val logger = LoggerFactory.getLogger(PathController::class.java.name)

    //private val counter1 = Counter.builder("Home_counter").description("Counter for cards").register(meterRegistry)





    @GetMapping(path = ["/"])
    fun welcome() : ModelAndView{
        meterRegistry.counter("home.get.requests", "uri", "/", "method", HttpMethod.GET.toString()).increment()

        logger.info("Welcome to homepage")
        //Displaying a basic html page
        val modelAndView = ModelAndView()
        modelAndView.viewName = "index.html"
        return modelAndView
    }



    @GetMapping(path = ["/page1"])
    fun page1() : String{

        logger.info("Fetching page1")

        //Adding a long task timer, which will record the time it takes to return page1. Added sleep() to make it seem like a longer task
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