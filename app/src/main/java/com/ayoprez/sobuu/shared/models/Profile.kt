package com.ayoprez.sobuu.shared.models

data class Profile(val id: String,
                   val giveUp: List<Book>,
                   val alreadyRead: List<Book>,
                   val firstName: String,
                   val following: List<Profile>,
                   val userShelves: List<Shelf>)