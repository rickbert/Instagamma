package com.instagamma.domain

import grails.converters.deep.JSON
import grails.converters.deep.XML
import org.springframework.dao.DataIntegrityViolationException

class BurstController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def list = Burst.list(params)
        def listObject = [burstInstanceList: list, burstInstanceTotal: Burst.count()]

        withFormat {
            html {
                listObject
            }
            json {
                render list as JSON
            }
            xml {
                render list as XML
            }
        }

        [burstInstanceList: Burst.list(params), burstInstanceTotal: Burst.count()]
    }

    def create() {
        [burstInstance: new Burst(params)]
    }

    def save() {
        def burstInstance = new Burst(params)
        if (!burstInstance.save(flush: true)) {
            render(view: "create", model: [burstInstance: burstInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'burst.label', default: 'Burst'), burstInstance.id])
        redirect(action: "show", id: burstInstance.id)
    }

    def show(Long id) {
        def burstInstance = Burst.get(id)
        if (!burstInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'burst.label', default: 'Burst'), id])
            redirect(action: "list")
            return
        }

        [burstInstance: burstInstance]
    }

    def edit(Long id) {
        def burstInstance = Burst.get(id)
        if (!burstInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'burst.label', default: 'Burst'), id])
            redirect(action: "list")
            return
        }

        [burstInstance: burstInstance]
    }

    def update(Long id, Long version) {
        def burstInstance = Burst.get(id)
        if (!burstInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'burst.label', default: 'Burst'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (burstInstance.version > version) {
                burstInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'burst.label', default: 'Burst')] as Object[],
                          "Another user has updated this Burst while you were editing")
                render(view: "edit", model: [burstInstance: burstInstance])
                return
            }
        }

        burstInstance.properties = params

        if (!burstInstance.save(flush: true)) {
            render(view: "edit", model: [burstInstance: burstInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'burst.label', default: 'Burst'), burstInstance.id])
        redirect(action: "show", id: burstInstance.id)
    }

    def delete(Long id) {
        def burstInstance = Burst.get(id)
        if (!burstInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'burst.label', default: 'Burst'), id])
            redirect(action: "list")
            return
        }

        try {
            burstInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'burst.label', default: 'Burst'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'burst.label', default: 'Burst'), id])
            redirect(action: "show", id: id)
        }
    }
}
