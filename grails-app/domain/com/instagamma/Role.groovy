package com.instagamma

class Role {

    final static String ROLE_ADMIN = "ROLE_ADMIN"

    String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
