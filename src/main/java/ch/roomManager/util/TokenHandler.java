package ch.roomManager.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.NewCookie;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * utility for creating and reading JSON web token
 * <p>
 * M151 BookDB
 *
 * @author Marcel Suter
 */

public class TokenHandler {
    private final static String JWT_SECRET = "ThisIsAVeryGoodKeyLol";

    /**
     * encrypts the string
     *
     * @param strToEncrypt string to be encrypted
     * @return encrypted string
     * @author Lokesh Gupta (https://howtodoinjava.com/security/java-aes-encryption-example/)
     */
    private static String encrypt(String strToEncrypt, String secret) {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(
                cipher.doFinal(
                    strToEncrypt.getBytes(StandardCharsets.UTF_8)
                )
            );
        } catch (Exception ex) {
            System.out.println("Error while encrypting: " + ex.toString());
        }
        return null;
    }

    /**
     * decrypts the string
     *
     * @param strToDecrypt string to be dencrypted
     * @return decrypted string
     * @author Lokesh Gupta (https://howtodoinjava.com/security/java-aes-encryption-example/)
     */
    private static String decrypt(String strToDecrypt, String secret) {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception ex) {
            System.out.println("Error while decrypting: " + ex.toString());
        }
        return null;
    }

    /**
     * gets the jwtkey from the propierties
     *
     * @return the jwtKey
     */
    private static String getJwtEncrypt() {
        String rawKey = JWT_SECRET;
        int multi8 = rawKey.length() / 8;
        return rawKey.substring(0, (multi8 * 8));
    }

    /**
     * builds a NewCookie with the json web token
     *
     * @param claimMap a map with the token data
     * @return the NewCookie
     */
    public NewCookie buildCookie(Map<String, String> claimMap) {
        NewCookie newCookie;
        if (claimMap == null || claimMap.isEmpty()) {
            newCookie = new NewCookie("token", "Auth-Token");
        } else {
            newCookie = new NewCookie(
                "token",
                buildToken(claimMap, 10),
                "",
                "",
                "Auth-Token",
                60,
                false,
                false
            );
        }

        return newCookie;
    }

    /**
     * reads all claims from the token
     *
     * @param token JSON web token
     * @return claims as a map
     */
    public Map<String, String> readClaims(String token) {
        Map<String, String> claimMap = new HashMap<>();
        Jws<Claims> jwsClaims;
        byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        try {
            jwsClaims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
            claimMap.put(
                "userName",
                decrypt(
                    jwsClaims.getBody().get("userName").toString(),
                    getJwtEncrypt()
                )
            );
            claimMap.put(
                "userRole",
                decrypt(
                    jwsClaims.getBody().get("userRole").toString(),
                    getJwtEncrypt()
                )
            );

        } catch (JwtException ex) {
            ex.printStackTrace();
            System.out.println(ex.getCause());
        }
        return claimMap;
    }

    /**
     * builds the token
     *
     * @param claimMap a map with the token claims
     * @param duration the duration of this token in minutes)
     * @return JSON web token as String
     */
    private String buildToken(Map<String, String> claimMap, int duration) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + duration * 60000);

        JwtBuilder jwtBuilder = Jwts.builder()
            .setIssuer("BookDB")
            .setSubject("authentication")
            .setExpiration(expiration)
            .setIssuedAt(now);

        for (Map.Entry<String, String> entry : claimMap.entrySet()) {
            jwtBuilder = jwtBuilder.claim(
                entry.getKey(),
                encrypt(entry.getValue(), getJwtEncrypt())
            );
        }

        byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return jwtBuilder
            .signWith(secretKey)
            .compact();
    }
}
