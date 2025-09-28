package grupoExpo.API.Controllers.Auth;

import grupoExpo.API.Entities.User.UserEntity;
import grupoExpo.API.Models.DTO.Usuario.UserDTO;
import grupoExpo.API.Services.Auth.AuthService;
import grupoExpo.API.Utils.JWT.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
private ResponseEntity<String> login (@RequestBody UserDTO data, HttpServletResponse response) {
     if (data.getCorreoUsuario() == null || data.getCorreoUsuario().isBlank() || data.getContrasenaUsuario() == null
    || data.getContrasenaUsuario().isBlank()){
         return ResponseEntity.status(401).body("Error:Credenciales incompletas");
     }
         if (service.Login(data.getCorreoUsuario(), data.getContrasenaUsuario())){
             addTokenCookie(response, data.getCorreoUsuario());//<- Pasar solo el correo
             return ResponseEntity.ok("Inicio de sesión exitoso");
         }
         return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

    /**
     *
     * @param response
     * @param
     */

    private void addTokenCookie(HttpServletResponse response, String correoUsuario) {
        Optional<UserEntity> userOpt = service.obtenerUsuario(correoUsuario);

        if (userOpt.isPresent()){
            UserEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getId()),
                    user.getCorreoUsuario(),
                    user.getTipoUsuario().getNombreRol()
            );
            //String cookieValue = String.format(
                  //  "authToke=%s; "+
                            //"Path=/;" +
                         //   "HttpOnly; " +
                          //  "Secure; " +
                           // "SameSite=None; " +
                           // "MaxAge=86400; "
                            //"",
                  //  token
          //  );
           // response.addHeader("set-Cookie", cookieValue);
            //response.addHeader("Access-Control-Expose-Headers","Set-Cookie");
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        try{
            if (authentication == null || !authentication.isAuthenticated()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "authenticated", false,
                                "message","No autenticado"
                        ));
            }
            //Se manjea de diferentesd tipos de  principal
            String username;
            Collection<? extends GrantedAuthority> authorities;

            if(authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            authorities = userDetails.getAuthorities();
            }else{
                username = authentication.getName();
                authorities = authentication.getAuthorities();
            }
            Optional<UserEntity> userOpt = service.obtenerUsuario(username);

            if (userOpt.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                           "authenticated", false,
                           "message", "Usuario no encontrado"
                        ));
            }
            UserEntity user = userOpt.get();

            return ResponseEntity.ok(Map.of(
               "authenticated",true,
               "user",Map.of(
                       "id", user.getId(),
                            "nombre", user.getNombre(),
                            "correo" ,user.getCorreoUsuario(),
                             "generoUsuario",user.getGeneroUsuario(),
                            "nombreRol", user.getTipoUsuario(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())

                    )
            ));
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                       "authenticated", false,
                       "message", "Error obteniendo datos de usuario"
                    ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        //crear cookie de expiracion con SameSite=None
       String  cookieValue = "authToken =; Path=; HttpOnly; Secure; SameSite=None; MaxAbe=0; Domian=";

       response.addHeader("Set-Cookie", cookieValue);
       response.addHeader("Access-Control-Expose-Headers", "Set-Cokkie");
       
       //Tambien se agrega los headers CORS para la respuesta
        String origin = request.getHeader("Origin");
        if(origin != null &&
                (origin.contains("localhost")|| origin.contains("herokuapp.com"))){
            response.setHeader("Access-Control-Allow-origin",origin);
        }
        return ResponseEntity.ok().body("Logout exitoso");
    }
}
