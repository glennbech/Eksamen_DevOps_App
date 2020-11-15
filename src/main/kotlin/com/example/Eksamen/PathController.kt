package com.example.Eksamen

import com.example.Eksamen.db.Card
import com.example.Eksamen.db.CardService
import io.micrometer.core.instrument.MeterRegistry
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
class PathController(private val cardService: CardService) {


    //@ApiOperation("Retrieve card collection information for a specific user")
    @GetMapping(path = ["/{name}"])
    fun getCardInfo(@PathVariable("name") cardName: String) : ResponseEntity<Void>{

        val card = cardService.findByIdEager(cardName)
        if(card == null){
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(200).build()
    }


    @PutMapping(path = ["/{name}"])
    fun createMonster(@PathVariable("name") cardName: String): ResponseEntity<Void> {

        val ok = cardService.addNewCard(cardName)
        return if (!ok) ResponseEntity.status(400).build()
        else ResponseEntity.status(201).build()
    }


}