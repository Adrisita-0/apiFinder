package grupoExpo.API.Models.DTO.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UserDTO {

    private Long idusuario;
    private Long idRol;
    @NotBlank
    private String nombreUsuario;
    @Email(message = "Debe ser un correo valido")
    private String correoUsuario;
    @Size(min = 8, message = "La contraseña debe contener 8 al menos caracteres")
    private String contrasenaUsuario;
    @NotNull
    private String segurityAnswerUsuario;
    private byte imagenUsuario;
    private String generoUsuario;

    private String nombreRol;


    public Long getTipoUsuario() {
        return null;
    }
}
