package com.example.Eksamen

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(EksamenApplication::class)], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecondaryControllerTest {

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

//    @Test
//    fun testGet400OnUnknownAPI(){
//        RestAssured.given().get("/doesNotExist")
//                .then()
//                .statusCode(400)
//    }
}