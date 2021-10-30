package com.example.firstgame

data class User(val name: String, val date: String, val time: String, val address: String){
    constructor() : this("", "", "", "")}