package com.example.Eksamen

import com.example.Eksamen.db.CardService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "/api/cards", description = "Operations on cards")
@RequestMapping(
        path = ["/api/cards"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class PathController(private val cardService: CardService, @Autowired private var meterRegistry: MeterRegistry) {

    @Autowired
    fun PathController(meterRegistry: MeterRegistry?) {
        this.meterRegistry = meterRegistry!!
    }

    private val counter1 = Counter.builder("Cards_counter").description("Counter for cards").register(meterRegistry)
    private val timer = Timer.builder("Timer_for_API").register(meterRegistry)


    //@ApiOperation("Retrieve card collection information for a specific user")

    @GetMapping(path = ["/{name}"])
    fun getCardInfo(@PathVariable("name") cardName: String) : ResponseEntity<Void>{

        val sample: Timer.Sample = Timer.start(meterRegistry)

        val card = cardService.findByIdEager(cardName)
        if(card == null){
            return ResponseEntity.status(400).build()
        }

        counter1.increment()
        sample.stop(timer)

        return ResponseEntity.status(200).build()

    }



    @PostMapping(path = ["/{name}"])
    fun createCard(@PathVariable("name") cardName: String): ResponseEntity<Void> {
        //counter1.increment()
        val ok = cardService.addNewCard(cardName)
        return if (!ok) ResponseEntity.status(400).build()
        else ResponseEntity.status(201).build()
    }




}