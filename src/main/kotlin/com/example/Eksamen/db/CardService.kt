package com.example.Eksamen.db

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@Repository
interface CardRepository : CrudRepository<Card, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.name = :id")
    fun lockedFind(@Param("id") name: String) : Card?

}


@Service
@Transactional
class CardService(
        private val cardRepository: CardRepository
) {

    companion object{
        const val CARDS_PER_PACK = 5
    }

    fun findByIdEager(cardId: String) : Card?{

        val card = cardRepository.findById(cardId).orElse(null)
        if(card != null){
            card.name
        }
        return card
    }

    fun addNewCard(cardId: String) : Boolean{

        if(cardRepository.existsById(cardId)){
            return false
        }

        val card = Card()
        card.name = cardId
        cardRepository.save(card)
        return true
    }


    private fun validateCard(cardId: String) {
        if (!cardRepository.existsById(cardId)) {
            throw IllegalArgumentException("Card $cardId does not exist")
        }
    }

    private fun validate(cardId: String) {
        validateCard(cardId)
    }



}