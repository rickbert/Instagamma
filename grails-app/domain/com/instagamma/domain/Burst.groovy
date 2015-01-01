package com.instagamma.domain

import org.joda.time.LocalDateTime

class Burst {

    String triggerId
    String grbId
    String rightAscension
    String declination
    String galacticLatitude
    String galacticLongitude
    LocalDateTime dateTime
    Constellation constellation
    Mission mission
    String opticalCounterpartMagnitude
    String lightCurveUrl
    String afterglowUrl
    String moreInfoUrl
    String redshift
    String description
    String latitude

    static constraints = {
        opticalCounterpartMagnitude(nullable: true)
        lightCurveUrl(nullable: true)
        afterglowUrl(nullable: true)
        moreInfoUrl(nullable: true)
        redshift(nullable: true)
        description(nullable: true, maxSize: 2048)
        latitude(nullable: true)
    }
}
