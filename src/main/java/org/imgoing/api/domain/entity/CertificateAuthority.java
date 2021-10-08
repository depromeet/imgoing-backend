package org.imgoing.api.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.imgoing.api.domain.vo.TokenPayload;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

public class CertificateAuthority {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private static final ObjectMapper mapper = new ObjectMapper();

    public String makeToken(TokenPayload payload) throws JsonProcessingException {
        SignatureAlgorithm  signatureAlgorithm= SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setSubject(mapper.writeValueAsString(payload))
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    public TokenPayload decrypt (String token) throws JsonProcessingException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();

        TokenPayload payload = mapper.readValue(claims.getSubject(), TokenPayload.class);
        return payload;
    }
}
