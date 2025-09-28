package grupoExpo.API.Entities.User;

import grupoExpo.API.Entities.UserType.UserTypeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@EqualsAndHashCode
@Table(name= "USUARIO")
public class UserEntity {

    @Id
    @GeneratedValue(strategy =GenerationType.SEQUENCE ,generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName =  "seq_usuario", allocationSize = 1)
    @Column(name = "IDUSUARIO")
    private Long id;

    @Column(name ="NOMBREUSUARIO")
    private String nombre;

   @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn( name ="IDTIPOUSUARIO")
    private UserTypeEntity tipoUsuario;

   @Column(name= "EMAIL")
    private String correoUsuario;

   @Column(name="CONTRASENA")
    private String contrasena;

   @Column(name= "SECURITYANSWERUSUARIO")
    private String SecurityAnswerUsuario;

   @Column(name="IMAGENUSUARIO")
    private String imagenUsuario;

   @Column(name= "GENEROUSUARIO")
    private String generoUsuario;

   @Override
    public String toString(){
       return "UserEntity{"+
               "nombre='" + nombre+ '\''+
               ",id="+ id +
               ",correoUsuario='" + correoUsuario + '\''+
               ",contrasena='" + contrasena +'\''+
               ",SecurityAnswerUsuario='" + SecurityAnswerUsuario +'\'' +
               ",imagenUsuario=" + imagenUsuario +
               ",generoUsuario='" + generoUsuario +'\''+
               ",tipoUsuario='" +tipoUsuario + '\''+
               '}';
   }

}
