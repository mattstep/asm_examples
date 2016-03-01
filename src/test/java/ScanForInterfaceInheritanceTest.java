import com.google.common.base.*;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by mattstep on 3/1/16.
 */

public class ScanForInterfaceInheritanceTest {

    public List<InputStream> getClassDefinitions(String jarPath) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            Throwables.propagate(e);
        }

        List<InputStream> classDefinitions = new LinkedList<InputStream>();

        Enumeration<JarEntry> classesInJar = jarFile.entries();

        while(classesInJar.hasMoreElements()) {
            try {
                JarEntry next = classesInJar.nextElement();
                if(next.getName().endsWith(".class")) {
                    classDefinitions.add(jarFile.getInputStream(next));
                }
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
        return classDefinitions;
    }

    @Test
    public void scanSampleJar() throws IOException {

        for(InputStream stream : getClassDefinitions("src/test/resources/appengine-tools-sdk-1.9.32.jar")) {

            ClassReader reader = new ClassReader(stream);

            if(Iterables.any(Arrays.asList(reader.getInterfaces()), Predicates.containsPattern("com/google/apphosting"))) {
                System.out.println(reader.getClassName() + " : " + Joiner.on(",").join(reader.getInterfaces()));
            }

        }
    }
}


