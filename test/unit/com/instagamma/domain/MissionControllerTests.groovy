package com.instagamma.domain



import org.junit.*
import grails.test.mixin.*

@TestFor(MissionController)
@Mock(Mission)
class MissionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/mission/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.missionInstanceList.size() == 0
        assert model.missionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.missionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.missionInstance != null
        assert view == '/mission/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/mission/show/1'
        assert controller.flash.message != null
        assert Mission.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/mission/list'

        populateValidParams(params)
        def mission = new Mission(params)

        assert mission.save() != null

        params.id = mission.id

        def model = controller.show()

        assert model.missionInstance == mission
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/mission/list'

        populateValidParams(params)
        def mission = new Mission(params)

        assert mission.save() != null

        params.id = mission.id

        def model = controller.edit()

        assert model.missionInstance == mission
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/mission/list'

        response.reset()

        populateValidParams(params)
        def mission = new Mission(params)

        assert mission.save() != null

        // test invalid parameters in update
        params.id = mission.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/mission/edit"
        assert model.missionInstance != null

        mission.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/mission/show/$mission.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        mission.clearErrors()

        populateValidParams(params)
        params.id = mission.id
        params.version = -1
        controller.update()

        assert view == "/mission/edit"
        assert model.missionInstance != null
        assert model.missionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/mission/list'

        response.reset()

        populateValidParams(params)
        def mission = new Mission(params)

        assert mission.save() != null
        assert Mission.count() == 1

        params.id = mission.id

        controller.delete()

        assert Mission.count() == 0
        assert Mission.get(mission.id) == null
        assert response.redirectedUrl == '/mission/list'
    }
}
