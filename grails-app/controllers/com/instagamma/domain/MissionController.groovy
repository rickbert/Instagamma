package com.instagamma.domain

import org.springframework.dao.DataIntegrityViolationException

class MissionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [missionInstanceList: Mission.list(params), missionInstanceTotal: Mission.count()]
    }

    def create() {
        [missionInstance: new Mission(params)]
    }

    def save() {
        def missionInstance = new Mission(params)
        if (!missionInstance.save(flush: true)) {
            render(view: "create", model: [missionInstance: missionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'mission.label', default: 'Mission'), missionInstance.id])
        redirect(action: "show", id: missionInstance.id)
    }

    def show(Long id) {
        def missionInstance = Mission.get(id)
        if (!missionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mission.label', default: 'Mission'), id])
            redirect(action: "list")
            return
        }

        [missionInstance: missionInstance]
    }

    def edit(Long id) {
        def missionInstance = Mission.get(id)
        if (!missionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mission.label', default: 'Mission'), id])
            redirect(action: "list")
            return
        }

        [missionInstance: missionInstance]
    }

    def update(Long id, Long version) {
        def missionInstance = Mission.get(id)
        if (!missionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mission.label', default: 'Mission'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (missionInstance.version > version) {
                missionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'mission.label', default: 'Mission')] as Object[],
                          "Another user has updated this Mission while you were editing")
                render(view: "edit", model: [missionInstance: missionInstance])
                return
            }
        }

        missionInstance.properties = params

        if (!missionInstance.save(flush: true)) {
            render(view: "edit", model: [missionInstance: missionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'mission.label', default: 'Mission'), missionInstance.id])
        redirect(action: "show", id: missionInstance.id)
    }

    def delete(Long id) {
        def missionInstance = Mission.get(id)
        if (!missionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mission.label', default: 'Mission'), id])
            redirect(action: "list")
            return
        }

        try {
            missionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'mission.label', default: 'Mission'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'mission.label', default: 'Mission'), id])
            redirect(action: "show", id: id)
        }
    }
}
