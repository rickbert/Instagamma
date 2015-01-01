import com.instagamma.Role
import com.instagamma.User
import com.instagamma.UserRole
import com.instagamma.domain.Mission

class BootStrap {

    def init = { servletContext ->
        User rick = new User(username: 'rick', password: 'instagammarawks', enabled: true)?.save()
        User serge = new User(username: 'serge', password: 's3rg3r00lz', enabled: true)?.save()

        Role adminRole = new Role(authority: Role.ROLE_ADMIN)?.save()

        UserRole userRole = UserRole.create(rick, adminRole, true)?.save()
        userRole = UserRole.create(serge, adminRole, true)?.save()

    }

    def destroy = {
    }
}
