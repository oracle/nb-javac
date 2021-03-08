import com.sun.tools.classfile.Attribute;
import com.sun.tools.classfile.Attributes;
import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.ClassWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class StripModuleHashes {

    public static void main(String[] args) throws Exception {
        try (InputStream in = new FileInputStream(args[0]);
             OutputStream out = new FileOutputStream(args[1])) {
            out.write(in.read());//J
            out.write(in.read());//M
            out.write(in.read());//major version
            out.write(in.read());//minor version
            try (JarInputStream jis = new JarInputStream(in);
                 JarOutputStream jos = new JarOutputStream(out)) {
                ZipEntry e;
                while ((e = jis.getNextEntry()) != null) {
                    jos.putNextEntry(e);

                    if (e.getName().endsWith("/module-info.class")) {
                        ClassFile cf = ClassFile.read(jis);
                        Attributes attrs = cf.attributes;
                        HashMap<String, Attribute> newAttrs = new HashMap<>(attrs.map);
                        newAttrs.remove(Attribute.ModuleHashes);
                        ClassFile newCF = new ClassFile(cf.magic, cf.minor_version, cf.major_version, cf.constant_pool, cf.access_flags, cf.this_class, cf.super_class, cf.interfaces, cf.fields, cf.methods, new Attributes(newAttrs));
                        new ClassWriter().write(newCF, jos);
                    } else {
                        int read;

                        while ((read = jis.read()) != (-1)) {
                            jos.write(read);
                        }
                    }
                }
            }
        }
    }

}
