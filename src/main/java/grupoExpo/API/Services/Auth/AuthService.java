package grupoExpo.API.Services.Auth;

import grupoExpo.API.Config.Crypto.Argon2Password;
import grupoExpo.API.Entities.User.UserEntity;
import grupoExpo.API.Repositories.User.UserRepo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthService {

@Autowired
private UserRepo repo;
    public boolean Login( String correoUsuario,  String contrasenaUsuario) {
        Argon2Password objHash = new Argon2Password();
        Optional<UserEntity> list = repo.findBycorreoUsuario(correoUsuario).stream().findFirst();
        if (list.isPresent()) {
            UserEntity usuario = list.get();
            String nombreTipoUsuario = usuario.getTipoUsuario().getNombreRol();
            System.out.println("Usuario encontrado ID: " + usuario.getId() +
                    ", email: " + usuario.getCorreoUsuario() +
                    ", rol: " + nombreTipoUsuario);
            String HashDB = usuario.getContrasena();
            boolean verificado = objHash.VerifyPassword(HashDB, contrasenaUsuario);
            return verificado;
        }
           return  false;
    }

    public Optional<UserEntity> obtenerUsuario(String correoUsuario) {
            // Buscar usuario completo en la base de datos
            Optional<UserEntity> userOpt = repo.findBycorreoUsuario(correoUsuario);
            return (userOpt != null) ? userOpt : null;
        }
    }
