import com.airshop.order.AirOrderService;
import com.airshop.order.utils.PayHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 18:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirOrderService.class)
public class PayTest {

    @Autowired
    private PayHelper payHelper;

    @Test
    public void createOrder(){
        String payUrl = payHelper.createPayUrl(12314323L, "air", 1L);
        System.err.println(payUrl);
    }
}
