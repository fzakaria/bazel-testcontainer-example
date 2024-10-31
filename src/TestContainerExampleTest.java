import com.google.devtools.build.runfiles.Runfiles;
import org.junit.Test;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class TestContainerExampleTest {

    @Test
    public void doNothingTest() throws Exception {
        String imageName = "pingservice:latest";
        Runfiles.Preloaded runfiles = Runfiles.preload();
        String tarballPath = runfiles.unmapped().rlocation(System.getenv("TARBALL_RUNFILE"));

        try (FileInputStream imageStream = new FileInputStream(tarballPath)) {
            DockerClientFactory.instance().client().loadImageCmd(imageStream).exec();
        }
        try (GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(imageName))
                .withExposedPorts(8080)
                .waitingFor(Wait.forHttp("/ping"));) {
            container.start();

            String address = "http://" + container.getHost() + ":" + container.getMappedPort(8080) + "/ping";
            HttpURLConnection connection = (HttpURLConnection) new URL(address).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            String responseMessage = new String(connection.getInputStream().readAllBytes());

            assertEquals(200, responseCode);
            assertEquals("pong", responseMessage);
        }

    }
}
