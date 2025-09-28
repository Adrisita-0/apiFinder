package grupoExpo.API.Utils.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTUtils {

    @Value("${security.jwt.secret}")
    private String jwtSecreto;                  // 32 caracteres por seguridad
    @Value("${security.jwt.issuer}")
    private String issuer;                      // Firma del token
    @Value("${security.jwt.expiration}")
    private long expiracionMs; //Tiempo de expiración

    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * Metodo para crear JWT
     * @param id
     * @param correoUsuario
     * @return
     */

    public String create(String id , String correoUsuario, String nombreRol) {
        //Se descodifica el secreto y crea una clave HMA-SHA segura
        SecretKey signinKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto));

        //Se obtiene la fecha actual y calcula la fecha de expiracion
        Date now = new Date();
        Date expiration = new Date(now.getTime()+ expiracionMs);

        //Construye el token con sus componentes
        return Jwts.builder()
                .setId(id)//ID unico (JWT ID)
                .setIssuedAt(now)//fecha de emision
                .setSubject(correoUsuario) //Sujeto (usuario
                .claim("id", id)
                .claim("rol", nombreRol)
                .setIssuer(issuer) //Emisor del token
                .setExpiration(expiracionMs>= 0? expiration: null) //Expiracion ( si es >= 0)
                .signWith(signinKey, SignatureAlgorithm.HS256) // Firma con algoritmo HS256
                .compact();// Convierte a String compacto
    }

    public String extractRol(String token) {
        Claims claims = parseToken(token);
        return  claims.get("rol",String.class);
    }
    /**
     * Obtine el ID del JWT
     * @param jwt
     * @return
     */
    public String getKey(String jwt){
        // Parsea los claims y devuelve el ID
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    /**
     * Obtiene el subjct ( nombre ) del JWT
     * @param jwt Token JWT como string
     * @return String con el subject del token
     */

    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }
    /**
     * Validación del token
     * @param token
     * @return
     */
    public boolean validate(String token){
        try{
            parseClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }
    /**
     * Metodo privado para parsear los claims de un JWT
     * @param jwt Token a parsear
     * @return Claims del token
     */
    private Claims parseClaims(String jwt) {
        //Configura el parse con la clave de firma y parsea el token
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto)))  // Clave de firma
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }


}
