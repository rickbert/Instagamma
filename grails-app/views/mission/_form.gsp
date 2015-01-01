<%@ page import="com.instagamma.domain.Mission" %>



<div class="fieldcontain ${hasErrors(bean: missionInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="mission.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${missionInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: missionInstance, field: 'bursts', 'error')} ">
	<label for="bursts">
		<g:message code="mission.bursts.label" default="Bursts" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${missionInstance?.bursts?}" var="b">
    <li><g:link controller="burst" action="show" id="${b.id}">${b?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="burst" action="create" params="['mission.id': missionInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'burst.label', default: 'Burst')])}</g:link>
</li>
</ul>

</div>

