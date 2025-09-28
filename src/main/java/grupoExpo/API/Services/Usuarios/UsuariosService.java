package grupoExpo.API.Services.Usuarios;

import grupoExpo.API.Config.Crypto.Argon2Password;
import grupoExpo.API.Entities.User.UserEntity;
import grupoExpo.API.Entities.UserType.UserTypeEntity;
import grupoExpo.API.Exceptions.UserType.TipoUsuarioNotFound;
import grupoExpo.API.Exceptions.user.UserNotFoundException;
import grupoExpo.API.Exceptions.user.UsuarioCorreoDuplicadoException;
import grupoExpo.API.Models.DTO.Usuario.UserDTO;
import grupoExpo.API.Utils.JWT.PasswordGenerator;
import grupoExpo.API.Repositories.User.UserRepo;
import grupoExpo.API.Repositories.UserType.UserTypeRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuariosService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private UserTypeRepository repoUserType;

    @Autowired
    private Argon2Password argon2;

    /**
     *
     * @param page
     * @param size
     * @return retorna los datos paginados conforme al tamano establecido por pagina
     */

    public Page<UserDTO>getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }
    /**
     *
     * @param  json
     * @return
     */
    public UserDTO createUser(@Valid UserDTO json){
        if (verificarExistenciaUsuario(json.getCorreoUsuario())){
            throw new UsuarioCorreoDuplicadoException("El correo ya esta registrado en la base de datos");

        }
        UserEntity objEntity = ConvertirAEntity(json);
        UserEntity savedUser = repo.save(objEntity);
        return ConvertirADTO (savedUser);
    }



    /**
     *
     * @param id
     * @param json
     * @return
     */
    public UserDTO updateUser(@Valid Long id, UserDTO json){
        //1.Se verifica si existe
        UserEntity existencia = repo.findById(id).orElseThrow(()-> new UserNotFoundException("Usuario no econtrado"));
        if (!existencia.getCorreoUsuario().equals(json.getCorreoUsuario())){
            if(verificarExistenciaUsuario(json.getCorreoUsuario())){
                throw new UsuarioCorreoDuplicadoException("El correo que pretende registrar ya existe en la base de datos");
            }
        }
        //2.Actualizar valores
        existencia.setNombre(json.getNombreUsuario());
        existencia.setCorreoUsuario(json.getCorreoUsuario());
        if(json.getIdRol() != null){
            UserTypeEntity tipoUsuario = repoUserType.findById(json.getIdRol())
                    .orElseThrow(()-> new TipoUsuarioNotFound("Tipo de usuario no encontrado"));
            existencia.setTipoUsuario(tipoUsuario);
        }else {
            existencia.setTipoUsuario(null);
        }
        UserEntity usuarioAcutalizado= repo.save(existencia);
        return ConvertirADTO(usuarioAcutalizado);
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteUser(Long id){
        UserEntity existente = repo.findById(id).orElse(null);
        if(existente!=null){
            repo.deleteById(id);
            return true;
        }else{
            log.error("Usuario no encontrado");
            return false;
        }
    }
    /**
     *
     * @param id
     * @return
     */
    public boolean resetPassword(@Valid Long id) {
        UserEntity existente = repo.findById(id).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        if (existente != null){
            String newPassword = PasswordGenerator.generateSecurePassword(12);
            existente.setContrasena(argon2.EncryptPassword(newPassword));
            UserEntity usuarioActualizado = repo.save(existente);
            return true;
        }
        return false;
    }

    //METODOS COMPLEMENTARIOS
    private boolean verificarExistenciaUsuario( String email) {
    boolean data = repo.existsByCorreo(email);
    if(data)
        return true;
    else
        return false;
    }
    /**
     * El metodo recibira un objeto entity y pasara a cada uno de los valores de Entity -> Dto
     * @param userEntity
     * @return valores en un tipo de objeto DTO
     */

    private UserDTO ConvertirADTO(UserEntity userEntity){
        UserDTO dto = new UserDTO();
        dto.setIdusuario(userEntity.getId());
        dto.setNombreUsuario(userEntity.getGeneroUsuario());
        dto.setCorreoUsuario(userEntity.getCorreoUsuario());
        dto.setContrasenaUsuario(userEntity.getContrasena());
        dto.setGeneroUsuario(userEntity.getGeneroUsuario());
        dto.setSegurityAnswerUsuario(userEntity.getSecurityAnswerUsuario());
        if (userEntity.getTipoUsuario()!= null){
            dto.setNombreRol(userEntity.getTipoUsuario().getNombreRol());
            dto.setIdRol(userEntity.getTipoUsuario().getIdRol());


        }
        return dto;
    }

    /**
     * El metodo recibira un objeto dto el cual se llamara json y pasara a cada uno de los valores de
     * DTO-> Entity
     * @param json
     * @return los valores en un tipo de objeto entity
     */
    private UserEntity ConvertirAEntity(@Valid UserDTO json) {
        Argon2Password objHash = new Argon2Password();
        UserEntity entity = new UserEntity();
        entity.setNombre(json.getNombreUsuario());
        entity.setCorreoUsuario(json.getCorreoUsuario());
        entity.setContrasena(argon2.EncryptPassword(json.getContrasenaUsuario()));
        entity.setGeneroUsuario(json.getGeneroUsuario());
        if(json.getTipoUsuario() != null){
            UserTypeEntity entityType = repoUserType.findById(json.getTipoUsuario())
                    .orElseThrow(()-> new TipoUsuarioNotFound("ID de Tipo Uusario no encontrado"));
            entity.setTipoUsuario(entityType);
        }
        return entity;
    }
}
