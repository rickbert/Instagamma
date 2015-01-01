package com.instagamma

import com.instagamma.domain.Burst
import com.instagamma.domain.Constellation
import com.instagamma.domain.Mission
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

class AdminController {

    def index() { }

    def loadData() {
        def xml = new XmlParser().parse(new File("C:\\dev\\sandbox\\grb-data\\data\\grbs.xml"))

        def rows = xml.grb

        rows.each { row ->
            createBurstFromData(row)?.save()
        }

        render "${rows.size()} bursts loaded."
    }

    Burst createBurstFromData(def chunk) {
        Burst burst

        boolean skip = false

        if (chunk) {
            // create dateTime
            LocalDate date = LocalDate.parse(chunk.burst_date.text(), DateTimeFormat.forPattern("yyyy/MM/dd"))
            LocalTime time
            try {
                time = LocalTime.parse(chunk.burst_time.text(), DateTimeFormat.forPattern("HH:mm:ss"))
            } catch (IllegalArgumentException iae) {
                log.warn("Format for the time of burst seems malformed.  Attempting another format.")
                try {
                    time = LocalTime.parse(chunk.burst_time.text(), DateTimeFormat.forPattern("HH:mm:ss.ss"))
                } catch (IllegalArgumentException iae2) {
                    log.error("Second attempt at parsing time '${chunk.burst_time.text()}'has failed.  Skipping burst with id ${chunk.grb_id.text()}")
                    skip = true
                }
            }

            if (!skip) {
                LocalDateTime dateTime = new LocalDateTime(date.year, date.monthOfYear, date.dayOfMonth, time.hourOfDay, time.minuteOfHour, time.secondOfMinute)

                // create or get Constellation
                String constellationName = chunk.constellation.text()
                Constellation constellation = Constellation.findByName(constellationName)
                if (!constellation) {
                    constellation = new Constellation(name: constellationName).save()
                }

                // create or get Mission
                String missionName = chunk.detecting_mission.text()
                Mission mission = Mission.findByName(missionName)
                if (!mission) {
                    mission = new Mission(name: missionName).save()
                }

                burst = new Burst(
                        triggerId: chunk.trigger.text(),
                        grbId: chunk.grb_id.text(),
                        rightAscension: chunk.burst_ra.text(),
                        declination: chunk.burst_dec.text(),
                        galacticLatitude: chunk.galatic_lat.text(),
                        galacticLongitude: chunk.galatic_long.text(),
                        constellation: constellation,
                        dateTime: dateTime,
                        mission: mission,
                        opticalCounterpartMagnitude: chunk.optical_counterpart_magnitude.text(),
                        lightCurveUrl: chunk.light_curve_url.text(),
                        afterglowUrl: chunk.after_glow_url.text(),
                        moreInfoUrl: chunk.more_info_url.text(),
                        redshift: chunk.redshift.text(),
                        description: chunk.grb_description.text(),
                        latitude: chunk.lat.text()
                )
            }
        }

        burst
    }
}
