package com.example.Eksamen

import com.example.Eksamen.db.Card
import com.example.Eksamen.dto.CardCopyDto

object DtoConverter {


    fun transform(card: Card) : CardCopyDto {
        return CardCopyDto().apply {
            cardId = card.name
        }
    }
}