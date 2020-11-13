package com.example.Eksamen

import com.example.Eksamen.db.CardService
import com.example.Eksamen.dto.CardCopyDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.coyote.Response
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "/api/cards", description = "Operations on cards")
@RequestMapping(
        path = ["/api/cards"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class PathController (private val cardService: CardService) {

    @GetMapping(path = ["/"])
    fun home() : String{
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