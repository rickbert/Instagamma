class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')

        name api0: "/api/$controller/$id"(parseRequest:true){
             action = [GET: "show", PUT: "update", DELETE: "delete"]
             constraints {
                 id(matches:/\d+/)
             }
         }

         name api1: "/api/$controller"(parseRequest:true){
             action = [GET: "list", POST: "save"]
         }

	}


}
