package com.example.Eksamen

import com.example.Eksamen.db.Card
import com.example.Eksamen.db.CardRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class EksamenApplication{

    @Autowired
    private var cardsRepository: CardRepository? = null

    //Initializing database with some default data at start
    @Bean
    open fun sendDatabase(): InitializingBean? {
        return InitializingBean {
            cardsRepository?.save(Card("Pikachu"))
            cardsRepository?.save(Card("Bulbasaur"))
            cardsRepository?.save(Card("Charmander"))
            cardsRepository?.save(Card("Squirtle"))
            cardsRepository?.save(Card("Onyx"))
        }
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(EksamenApplication::class.java, *args)
}

