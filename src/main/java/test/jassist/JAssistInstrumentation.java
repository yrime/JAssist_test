package test.jassist;

import javassist.*;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JAssistInstrumentation {
    private JarFile jar;
    private URLClassLoader loader;
    private URL jarUrl;

    public int instr(String jarname){
        try {
            jarUrl = new URL("file:///" + jarname);
            loader = new URLClassLoader(new URL[]{jarUrl});
            jar = new JarFile(jarname);
            classReader();
        }catch(MalformedURLException e) {
            e.printStackTrace();;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    private int classReader(){
        JarEntry entry;
        String fileName, className;
        Class<?> c;
        //javaassist init
        ClassPool cp = new ClassPool();
        ClassPath ccp = new LoaderClassPath(loader);
        cp.insertClassPath(ccp);
        CtClass cc;

        for(Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ){
            entry = entries.nextElement();
            fileName = entry.getName();

            if(fileName.endsWith(".class")){
                className = fileName.replace('/', '.').substring(0, fileName.length() - 6); // 6 = (".class").length()
                try{
                    c = loader.loadClass(className);
                    printClassInformation(c); // print information about class

                    try {
                        cc = cp.get(className);//get class from pool class
                        injectCode(cc);
                    }catch (NotFoundException e){
                        e.printStackTrace();
                    }

                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    private void printClassInformation(Class<?> c){
        System.out.println("Class name: " + c.getName());
        System.out.println("Class metods: " + c.getDeclaredMethods());
        System.out.println("Class constructors: " + c.getDeclaredConstructors());
    }

    private void injectCode(CtClass cc) throws CannotCompileException, IOException {
        System.out.println("javassist inject code to class: " + cc.getName());
        CtMethod ctm[] = cc.getDeclaredMethods();
        String mName;
        for(int i = 0; i < ctm.length; ++i){
            System.out.println("javassist inject code to method: " + ctm[i].getName());
            mName = ctm[i].getName();
            CtMethod ctmBuffer = ctm[i];
            ctmBuffer.insertBefore(String.format(
                    "System.out.println(\" injected code to method %s \");", mName
            ));
        }
        System.out.println("code injected ");
        byte[] b = cc.toBytecode();
        cc.writeFile("instrumentedClasses");
    }
}
