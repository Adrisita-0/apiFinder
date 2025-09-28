package grupoExpo.API.Controllers.User;

import grupoExpo.API.Exceptions.user.ExceptionUsuarioNoInsertado;
import grupoExpo.API.Models.DTO.Usuario.UserDTO;
import grupoExpo.API.Models.apiResponse.ApiResponse;
import grupoExpo.API.Services.Usuarios.UsuariosService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.script.Bindings;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UsuariosService service;

    /**
     * @param page
     * @param size
     * @return
     */

    @GetMapping("/getDataUsers")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> obtenerUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (size <= 0 || size > 50) {
            ResponseEntity.badRequest().body(Map.of(
                    "status", "La paginacion de datos debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<UserDTO> users = service.getAllUsers(page, size);
        if (users == null) {
            ResponseEntity.badRequest().body(Map.of(
                    "status", "Error al obtener los datos "
            ));
        }
        return ResponseEntity.ok(ApiResponse.success("Datos consultados correctamente", users));
    }

    /**
     * @param json
     * @return
     */
    @PostMapping("/nuevoUsuario")
    public ResponseEntity<ApiResponse<UserDTO>> insertarUsuario(@Valid @RequestBody UserDTO json) {
        if (json == null) {
            throw new ExceptionUsuarioNoInsertado("Error al recibir y procesar la infomación del usuario");
        }
        UserDTO usuarioGuardado = service.createUser(json);
        if (usuarioGuardado == null) {
            throw new ExceptionUsuarioNoInsertado("El usuario no pudo ser registrado debido a algun inconveniente con los datos");
        }
        return ResponseEntity.ok(ApiResponse.success("Usuario registrado exitosamente", usuarioGuardado));
    }

    @PutMapping("/actualizarUsuario/{id}")
    public ResponseEntity<?> actualizarUusario(
            @Valid
            @PathVariable Long id,
            @RequestBody UserDTO json,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);

        }
        try {
            UserDTO usuaroActualizado = service.updateUser(id, json);
            return ResponseEntity.ok(usuaroActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al modificar el usuario");
        }

    }
    @DeleteMapping("/EliminarUsuario/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(
            @PathVariable Long Id
    ){
        try{
            if (!service.deleteUser(Id)){
                //Eliminacion no se pudo realizar
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Error", "El usuario no encontrado")
                        .body(Map.of(
                           "Error", "NOT FOUND",
                                "Mensaje", "El usuario no fue encontrado",
                                "Fecha y hora", Instant.now().toString()
                        ));
            }
            //La iliminacion si se ejecuto correctamente
            return ResponseEntity.ok().body(Map.of(
               "status", "Proceso completado",
               "mensaje", "Usuario eliminado exitosamente"
            ));
        }catch(Exception e){
            //Si ocurre cualquier error inesperado, retorna 500 ( Internal server Error)
            return ResponseEntity.internalServerError().body(Map.of(
               "status","Error", //Indicador de error
               "message","Error al eliminar el usuario", //Mensaje general
               "detail",e.getMessage() //Detalles técnicos del error (para debugging)
            ));
        }
    }
    @PutMapping("/Actualizar/{id}/contraseña")
    private ResponseEntity<Map<String, Object>> resetPassword(@Valid @PathVariable Long id){
        try{

            boolean respuesta = service.resetPassword(id);
            if(respuesta){
                return ResponseEntity.ok().body(Map.of(
                   "Success","Proceso completado existosamente",
                        "Message","La contrasena fue restablecida correctamente"
                ));
            }
            return ResponseEntity.ok().body(Map.of(
               "Status","Error",
               "Message","El proceso no pudo ser completado"
            ));
        }catch (Exception e){
            return ResponseEntity.ok().body(Map.of(
               "Status","El proceso interrumpido",
               "Message","El proceso no pudo ser completado"
            ));
        }
    }
}
