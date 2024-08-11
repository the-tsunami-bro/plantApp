package com.example.demo.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "your_secret_key_your_secret_key_your_secret_key";//应该使用更安全的密钥
    private final long JWT_EXPIRATION = 604800000L;// 7天有效期
    private final Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes()); // 从字符串生成Key对象

    // 生成JWT
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key) // 使用新的签名方式
                .compact();
    }

    // 从JWT中获取用户名
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //// 验证JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


}



