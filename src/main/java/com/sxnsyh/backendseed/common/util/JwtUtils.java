package com.sxnsyh.backendseed.common.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建解析JWT工具类
 * @author wuqian
 */
public class JwtUtils {

    /**
     * 加密算法签名
     */
    private static final String SECRET = "org.sxnsh.secret";
    /**
     * 发行token人签名
     */
    private static final String ISSUER = "sxnsh";

    /**
     * token失效时间单位秒
     */
    private static final long DISABLE_TIME = 60*60;



    /**
     * 生成token
     *
     * @param claims token承载的数据
     * @return
     */
    public static String createToken(Map<String, String> claims) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER);
            claims.forEach(builder::withClaim);
            return builder.sign(algorithm);
        } catch (IllegalArgumentException  e) {
            throw new Exception("生成token失败");
        }
    }


    /**
     * 验证jwt，并返回数据
     */
    public static Map<String, String> verifyToken(String token) throws Exception {
        Algorithm algorithm;
        Map<String, Claim> map;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                                  .withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            map = jwt.getClaims();
        } catch (Exception e) {
            throw new Exception("鉴权失败");
        }
        Map<String, String> resultMap = new HashMap<>(map.size());
        map.forEach((k, v) -> resultMap.put(k, v.asString()));
        return resultMap;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("id", "10001");
        map.put("name", "zhangsan");
        String token = createToken(map);
        System.out.println(token);
        Map<String, String> res = verifyToken(token);
        System.out.println(JSON.toJSONString(res));

        String tokens = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzeG5zaCIsIm5hbWUiOiJ6aGFuZ3NhbiIsImlkIjoiMTAwMDEifQ.wObuzFoFYy2KjYTyFvGi8TfJWSXxfecX2_C9A3hH-qE";
        DecodedJWT jwt = JWT.decode(token);
        System.out.println(jwt);
        Map<String, String> ress = verifyToken(tokens);
        System.out.println(JSON.toJSONString(ress));




    }
}
