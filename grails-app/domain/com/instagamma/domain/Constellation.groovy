package com.instagamma.domain

class Constellation {

    String name

    static hasMany = [bursts: Burst]

    static constraints = {
        name(nullable: false, blank: false)
    }
}
