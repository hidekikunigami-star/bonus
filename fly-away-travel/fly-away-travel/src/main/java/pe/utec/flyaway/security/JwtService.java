package pe.utec.flyaway.security;

import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {
    private final String secret;
    private final long expirationSeconds;

    public JwtService(org.springframework.core.env.Environment env) {
        this.secret = env.getProperty("APP_JWT_SECRET", "change-this-secret-key-for-development");
        this.expirationSeconds = Long.parseLong(env.getProperty("APP_JWT_EXPIRATION_SECONDS", "3600"));
    }

    public String generate(Long userId, String email) {
        long now = Instant.now().getEpochSecond();
        String header = base64Url("{"alg":"HS256","typ":"JWT"}");
        String payload = base64Url("{"sub":"" + userId + "","email":"" + escape(email) +
                "","iat":" + now + ","exp":" + (now + expirationSeconds) + "}");
        String unsigned = header + "." + payload;
        return unsigned + "." + sign(unsigned);
    }

    public Long validateAndGetUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3 || !sign(parts[0] + "." + parts[1]).equals(parts[2])) return null;
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            long exp = Long.parseLong(payload.replaceFirst(".*\"exp\":(\\d+).*", "$1"));
            if (Instant.now().getEpochSecond() >= exp) return null;
            String sub = payload.replaceFirst(".*\"sub\":\"(\\d+)\".*", "$1");
            return Long.valueOf(sub);
        } catch (Exception e) {
            return null;
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(
                    mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace(""", "\"");
    }
}
