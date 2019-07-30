import com.airshop.auth.entity.UserInfo;
import com.airshop.auth.utils.JwtUtils;
import com.airshop.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 12:14
 */
public class JwtTest {

    private static final String pubKeyPath = "D:\\server\\airshop\\rsa.pub";

    private static final String priKeyPath = "D:\\server\\airshop\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "123");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = JwtUtils.generateToken(new UserInfo(33L, "air苏杉杉"), privateKey, 5);

        System.err.println(token);

        UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, publicKey);
        System.err.println(infoFromToken.getId());
        System.err.println(infoFromToken.getUsername());
    }

    @Test
    public void testToken() throws Exception {
//        String token = JwtUtils.generateToken(new UserInfo(33L, "air苏杉杉"), privateKey, 5);
//
//        System.err.println(token);

        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjgsInVzZXJuYW1lIjoiYWlyNDMzIiwiZXhwIjoxNTY3MDU3NzI2fQ.BH6NhMKc_r3p9N7IMR6pOsu-IRgeOAEWlm_1PvJMYBPIwBiyx9ABwBFODTiDOi6BB3BLdBvjvq8UQ7tVJfVyVyBZIwVfkRTp_oSRabC1hjM7BYobEpP-ZPxrN1f3hJeDPO7tk1IvltOJpYvDCsN4n7WdAIHyetpeWxB9JKFlmZc";
        UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, publicKey);
        System.err.println(infoFromToken.getId());
        System.err.println(infoFromToken.getUsername());
    }

}
