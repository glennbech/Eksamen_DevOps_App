package com.example.Eksamen

import com.example.Eksamen.db.CardRepository
import com.example.Eksamen.db.CardService
import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(EksamenApplication::class)], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PathControllerTest {

    @LocalServerPort
    protected var port = 8080

    @Autowired
    private lateinit var cardService: CardService

    @Autowired
    private lateinit var cardRepository: CardRepository

    @PostConstruct
    fun init(){
        //RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/cards"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @BeforeEach
    fun initTest(){
        cardRepository.deleteAll()
    }

    @Test
    fun testGetCard(){
        val id = "foo";
        cardService.addNewCard(id)

        RestAssured.given().get("/$id")
                .then()
                .statusCode(200)
    }

    @Test
    fun testCreateCard(){
        val id = "foo";
        //cardService.addNewCard(id)

        RestAssured.given().put("/$id")
                .then()
                .statusCode(201)

        assertTrue(cardRepository.existsById(id))
    }

    @Test
    fun testGet200Page1(){
        RestAssured.given().get("/page1")
                .then()
                .statusCode(200)
    }

    @Test
    fun testGet200Page2(){
        RestAssured.given().get("/page2")
                .then()
                .statusCode(200)
    }

    @Test
    fun testGet200Page3(){
        RestAssured.given().get("/page3")
                .then()
                .statusCode(200)
    }

    @Test
    fun testGet200Page4(){
        RestAssured.given().get("/page4")
                .then()
                .statusCode(200)
    }

    @Test
    fun testGet400OnUnknownAPI(){
        RestAssured.given().get("/doesNotExist")
                .then()
                .statusCode(400)
    }


}