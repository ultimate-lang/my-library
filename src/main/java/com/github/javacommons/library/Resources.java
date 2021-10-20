import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import java.io.IOException;
import java.net.URL;

public class MyResources {

    public static String asString(String resource) {
        URL url = com.google.common.io.Resources.getResource(resource);
        try {
            return com.google.common.io.Resources.toString(url, Charsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static CharSource asCharSource(String resource) {
        URL url = com.google.common.io.Resources.getResource(resource);
        return com.google.common.io.Resources.asCharSource(url, Charsets.UTF_8);
    }

    public static byte[] asByteArray(String resource) {
        URL url = com.google.common.io.Resources.getResource(resource);
        try {
            return com.google.common.io.Resources.toByteArray(url);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ByteSource asByteSource(String resource) {
        URL url = com.google.common.io.Resources.getResource(resource);
        return com.google.common.io.Resources.asByteSource(url);
    }

}
