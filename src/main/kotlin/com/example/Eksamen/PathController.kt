package com.example.Eksamen

import com.example.Eksamen.db.CardRepository
import com.example.Eksamen.db.CardService
import com.example.Eksamen.dto.CardCopyDto
import io.micrometer.core.instrument.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

//@Api(value = "/api/cards", description = "Operations on cards")
//@RequestMapping(
//        path = ["/api/cards"],
//        produces = [(MediaType.APPLICATION_JSON_VALUE)]
//)
@RestController
class PathController(private val cardService: CardService, @Autowired private var meterRegistry: MeterRegistry, private val cardRepository: CardRepository) {


    //////////////////influx end
    private val logger = LoggerFactory.getLogger(PathController::class.java.name)

    private val counter1 = Counter.builder("Cards_counter").description("Counter for cards").register(meterRegistry)



    //@ApiOperation("Retrieve card collection information for a specific user")

    @GetMapping(path = ["/{name}"])
    fun getCardInfo(@PathVariable("name") cardName: String) : ResponseEntity<Void>{

//        val longTaskTimer = LongTaskTimer.builder("long.task.timer").tags("get", "card").register(meterRegistry)
//        longTaskTimer.activeTasks()

        val card = cardService.findByIdEager(cardName)
        if(card == null){
            logger.warn("Failed to get card with status: 400")
            return ResponseEntity.status(400).build()
        }

        //counter1.increment()

        logger.info("Successfully fetching card with status: 200")
        return ResponseEntity.status(200).build()

    }


    @GetMapping("/allCards")

    fun getAllCards() = cardRepository.findAll()
            .map { CardCopyDto(it.name) }
            .also {

                logger.info("Fetching all cards")
                meterRegistry.gaugeCollectionSize("fetch.amount.of.cards", listOf(Tag.of("type", "collection")), it)

                DistributionSummary.builder("retrieved.measurements.values")
                        .publishPercentiles(.25, .5, .75)
                        .register(meterRegistry)
                        .record(cardRepository.count().toDouble())

            }
            .map { ok(it) }



    @PostMapping(path = ["/{name}"])
    fun createCard(@PathVariable("name") cardName: String): ResponseEntity<Void> {
        counter1.increment()
        val timer = Timer.builder("Cards_timer").register(meterRegistry)
        timer.record(5000, TimeUnit.MILLISECONDS)
        val ok = cardService.addNewCard(cardName)
        return if (!ok){
            logger.warn("Failed to create card with status: 400")
            ResponseEntity.status(400).build()
        }
        else{
            logger.info("Succesfully created card")
            ResponseEntity.status(201).build()
        }

    }

}


