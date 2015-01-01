package com.instagamma.domain



import org.junit.*
import grails.test.mixin.*

@TestFor(BurstController)
@Mock(Burst)
class BurstControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/burst/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.burstInstanceList.size() == 0
        assert model.burstInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.burstInstance != null
    }

    void testSave() {
        controller.save()

        assert model.burstInstance != null
        assert view == '/burst/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/burst/show/1'
        assert controller.flash.message != null
        assert Burst.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/burst/list'

        populateValidParams(params)
        def burst = new Burst(params)

        assert burst.save() != null

        params.id = burst.id

        def model = controller.show()

        assert model.burstInstance == burst
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/burst/list'

        populateValidParams(params)
        def burst = new Burst(params)

        assert burst.save() != null

        params.id = burst.id

        def model = controller.edit()

        assert model.burstInstance == burst
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/burst/list'

        response.reset()

        populateValidParams(params)
        def burst = new Burst(params)

        assert burst.save() != null

        // test invalid parameters in update
        params.id = burst.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/burst/edit"
        assert model.burstInstance != null

        burst.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/burst/show/$burst.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        burst.clearErrors()

        populateValidParams(params)
        params.id = burst.id
        params.version = -1
        controller.update()

        assert view == "/burst/edit"
        assert model.burstInstance != null
        assert model.burstInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/burst/list'

        response.reset()

        populateValidParams(params)
        def burst = new Burst(params)

        assert burst.save() != null
        assert Burst.count() == 1

        params.id = burst.id

        controller.delete()

        assert Burst.count() == 0
        assert Burst.get(burst.id) == null
        assert response.redirectedUrl == '/burst/list'
    }
}
