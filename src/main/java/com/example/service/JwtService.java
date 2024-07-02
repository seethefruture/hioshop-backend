package com.example.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 校验token并解析token
     */
    public Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error("token解码异常", e);
            //解码异常则抛出异常
            return null;
        }
        return jwt.getClaims();
    }

    /**
     * 根据token解析获取用户id
     */
    public String getUserId(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("解析token失败！");
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token).getClaim("user_id").asString();
    }

    /**
     * 根据用户信息创建token
     */
    public String create(Map<String, Object> userInfo) {

        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + expiration * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                // 添加头部
                .withHeader(map)
                //可以将基本信息放到claims中
                //userId
                .withClaim("user_id", userInfo.get("user_id").toString())
                //userName
                .withClaim("openid", userInfo.get("openid").toString())
                //超时设置,设置过期的日期
                .withExpiresAt(expireDate)
                //签发时间
                .withIssuedAt(new Date())
                //SECRET加密
                .sign(Algorithm.HMAC256(secret));
    }


    /**
     * 根据token验证token的有效性
     */
//    public boolean verify(String token) {
//        Claims claims = parse(token);
//        return claims != null;
//    }
}
