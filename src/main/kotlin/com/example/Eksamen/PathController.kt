package com.example.Eksamen

import com.example.Eksamen.db.CardRepository
import com.example.Eksamen.db.CardService
import com.example.Eksamen.dto.CardCopyDto
import io.micrometer.core.instrument.*
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit



@RestController
class PathController(private val cardService: CardService, @Autowired private var meterRegistry: MeterRegistry, private val cardRepository: CardRepository) {


    //////////////////influx end
    private val logger = LoggerFactory.getLogger(PathController::class.java.name)

    private val counter1 = Counter.builder("Cards_counter").description("Counter for cards").register(meterRegistry)



    //@ApiOperation("Retrieve card collection information for a specific user")

    @RequestMapping(path = ["/allCards/{name}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @GetMapping(path = ["/allCards/{name}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCardInfo(@PathVariable("name") cardName: String) : ResponseEntity<Void>{



        val card = cardService.findByIdEager(cardName)
        if(card == null){
            logger.warn("Failed to get card with status: 400")
            return ResponseEntity.status(400).build()
        }


        logger.info("Successfully fetching card with status: 200")
        return ResponseEntity.status(200).build()

    }



    @GetMapping("/allCards")
    fun getAllCards() = cardRepository.findAll()
            .map { CardCopyDto(it.name) }
            .also {

                logger.info("Fetching all cards")
                //meterRegistry.gaugeCollectionSize("fetch.amount.of.cards", listOf(Tag.of("type", "collection")), it)
                meterRegistry.gauge("amount.of.cards.initialized", it.size)

                DistributionSummary.builder("amount.of.cards.added.after.init")
                        .publishPercentiles(.25, .5, .75)
                        .register(meterRegistry)
                        .record(cardRepository.count().toDouble())

            }
            .map { ok(it) }



    @PostMapping(path = ["/allCards/{name}"])
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


