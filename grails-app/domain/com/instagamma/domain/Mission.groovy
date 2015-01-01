package com.instagamma.domain

class Mission {

    String name

    static hasMany = [bursts: Burst]

    static constraints = {
        name(nullable: false, blank: false)
    }
}
