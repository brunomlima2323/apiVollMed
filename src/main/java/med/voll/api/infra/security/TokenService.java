package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.api.domain.usuario.Usuario;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;

	public String gerarToken(Usuario usuario) {
		System.out.println(secret);
		
		try {
		    var algoritimo = Algorithm.HMAC256(secret);
		    return JWT.create()
		        .withIssuer("API Voll.med")
		        .withSubject(usuario.getLogin())
//		        .withClaim("id", usuario.getId()) //Qualquer informação adicional que deseje guardar utilizando chave e valor
		        .withExpiresAt(dataExpiracao())
		        .sign(algoritimo);
		} catch (JWTCreationException exception){
		    // Invalid Signing configuration / Couldn't convert Claims.
			throw new RuntimeException("Erro ao gerar token jwt!", exception);
		}
	}
	
	public String getSubject(String tokenJWT) {
		try {
			var algoritimo = Algorithm.HMAC256(secret);
		    return JWT.require(algoritimo)
		        .withIssuer("API Voll.med")
		        .build()
		        .verify(tokenJWT)
		        .getSubject();
		    
		} catch (JWTVerificationException exception){
			throw new RuntimeException("Token jwt inválido ou expirado!", exception);
		}
	}

	private Instant dataExpiracao() {
		// TODO Auto-generated method stub
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
	
}
