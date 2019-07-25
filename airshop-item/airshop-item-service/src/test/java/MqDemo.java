import com.airshop.AirItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author ouyanggang
 * @Date 2019/7/24 - 17:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirItemService.class)
public class MqDemo {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSend() throws InterruptedException {
        String msg = "hello, spring boot amqp";
//        this.amqpTemplate.convertAndSend("spring.test.exchange", "a.b", msg);
        this.amqpTemplate.convertAndSend("item.D", 11);
        Thread.sleep(10000);
    }
}
