import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWT {

    private static Key apiKey  = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //Sample method to construct a JWT
    public static String createJWT(String username) {
        return Jwts.builder().setSubject(username).signWith(apiKey).compact();
    }

    //Sample method to validate and read the JWT
    public static String extractUsername(String jwt) {
        return Jwts.parserBuilder().setSigningKey(apiKey).build().parseClaimsJws(jwt).getBody().getSubject();
    }
}
