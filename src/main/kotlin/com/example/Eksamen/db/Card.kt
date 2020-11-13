package com.example.Eksamen.db

import com.sun.istack.NotNull
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "card_data")
class Card (
        @get:Id
        @get:NotNull
        var name: String? = null

)