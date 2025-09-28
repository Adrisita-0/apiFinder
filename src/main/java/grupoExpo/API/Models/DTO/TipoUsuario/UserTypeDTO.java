package grupoExpo.API.Models.DTO.TipoUsuario;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString @EqualsAndHashCode
public class UserTypeDTO {

    private Long IdRol;
    private String nombreRol;
}
