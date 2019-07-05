import com.airshop.AirUploadService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author ouyanggang
 * @Date 2019/7/5 - 15:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirUploadService.class)
@Component
public class FdfsTest {

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Value("#{fastFDS.address}")
    private String address;

    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("D:\\yc.jpg");
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "png", null);

        System.out.println(storePath.getFullPath());
        System.err.println(storePath.getPath());
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());

        System.out.println(path);
    }

    @Test
    public void t1(){
        System.out.println(address);
    }
}
