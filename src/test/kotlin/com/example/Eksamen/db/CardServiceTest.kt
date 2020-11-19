package com.example.Eksamen.db

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate

@ActiveProfiles("CardServiceTest,test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class CardServiceTest{

    @Autowired
    private lateinit var cardService: CardService

    @Autowired
    private lateinit var cardRepository: CardRepository

    @BeforeEach
    fun initTest(){
        cardRepository.deleteAll()
    }


    @Test
    fun testCreateCard(){
        val id = "foo"
        assertTrue(cardService.addNewCard(id))
        assertTrue(cardRepository.existsById(id))
    }

    @Test
    fun testFailCreateCardTwice(){
        val id = "foo"
        assertTrue(cardService.addNewCard(id))
        assertFalse(cardService.addNewCard(id))
    }



}