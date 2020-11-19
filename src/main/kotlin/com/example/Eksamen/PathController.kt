package com.example.Eksamen

import com.example.Eksamen.db.CardRepository
import com.example.Eksamen.db.CardService
import com.example.Eksamen.dto.CardCopyDto
import com.example.Eksamen.restDto.RestResponseFactory
import com.example.Eksamen.restDto.WrappedResponse
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

    private val logger = LoggerFactory.getLogger(PathController::class.java.name)

    private val counter1 = Counter.builder("Cards_counter").description("Counter for cards").register(meterRegistry)


    @RequestMapping(path = ["/allCards/{name}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @GetMapping(path = ["/allCards/{name}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCardInfo(@PathVariable("name") cardName: String) : ResponseEntity<WrappedResponse<CardCopyDto>>{

        val card = cardService.findByIdEager(cardName)
        if(card == null){
            logger.error("Failed to get card with status: 400")
            return RestResponseFactory.notFound("User $cardName not found")
        }


        logger.info("Successfully fetching card with status: 200")
        return RestResponseFactory.payload(200, DtoConverter.transform(card))

    }



    @GetMapping("/allCards")
    fun getAllCards() = cardRepository.findAll()
            .map { CardCopyDto(it.name) }
            .also {

                logger.info("Fetching all cards")

                //Gauge for how many cards is present at initialization
                meterRegistry.gauge("amount.of.cards.initialized", it.size)

                //Distribution summary checking how many cards are added after initialization. Updates every time card is added
                DistributionSummary.builder("amount.of.cards.added.after.init")
                        .publishPercentiles(.25, .5, .75)
                        .register(meterRegistry)
                        .record(cardRepository.count().toDouble())

            }
            .map { ok(it) }



    @PostMapping(path = ["/allCards/{name}"])
    fun createCard(@PathVariable("name") cardName: String): ResponseEntity<WrappedResponse<Void>> {
        //Counter registering each time a card has been created
        counter1.increment()

        //Timer for checking the cards being created
        val timer = Timer.builder("Cards_timer").register(meterRegistry)
        timer.record(5000, TimeUnit.MILLISECONDS)
        val ok = cardService.addNewCard(cardName)
        return if (!ok){
            logger.error("Failed to create card with status: 400")
            ResponseEntity.status(400).build()
        }
        else{
            logger.info("Succesfully created card")
            ResponseEntity.status(201).build()
        }

    }

}


