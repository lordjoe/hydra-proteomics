package com.lordjoe.utilities;

import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 * com.lordjoe.utilities.LoggingClassLoader
 * User: Steve
 * Date: 11/21/13
 */
public class LoggingClassLoader extends ClassLoader {

    private List<AbstractLoggingClassLoader> classPath = new ArrayList<AbstractLoggingClassLoader>();
    private Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    private Set<File> usedJars = new HashSet<File>();
    private JarOutputStream loadedClassesStream;

    public LoggingClassLoader(String classppaths) {
        this(classppaths.split(System.getProperty("path.separator")));
    }

    public LoggingClassLoader(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            File f = new File(paths[i]);
            if (!f.exists())
                continue;
            if (f.isFile()) {
                classPath.add(new JarClassLoader(this, f));
            }
            else {
                classPath.add(new DirectoryClassLoader(this, f));

            }

        }
    }

    public JarOutputStream getLoadedClasses() {
        return loadedClassesStream;
    }

    public void setLoadedClasses(final OutputStream os) {
        try {
            loadedClassesStream = new JarOutputStream(os);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    protected void maybeSaveClass(String name,byte[] classBytes)  {
        JarOutputStream saver = getLoadedClasses();
        if(saver == null)
            return;
        JarEntry je  = new JarEntry(name);

    }

    public void setLoadedClasses(final  File f) {
        try {
            setLoadedClasses(new FileOutputStream(f));
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public Set<File> getUsedJars() {
        return new HashSet<File>(usedJars);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        return classes.get(name);
    }


    @Override
    protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {

        Class<?> ret = classes.get(name);
        if(ret != null)
            return ret;

        for (AbstractLoggingClassLoader classLoader : classPath) {
             ret = classLoader.loadClass(name, resolve);
             if(ret != null) {
                 classes.put(name, ret);
                 if(classLoader.isJarLoader())
                     usedJars.add(classLoader.getSource());
                 return ret;
             }
        }
        return super.loadClass(name, resolve);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        return super.loadClass(name, false);
    }

    protected abstract class AbstractLoggingClassLoader extends ClassLoader {
        private final File source;

        public abstract boolean isJarLoader();


        protected AbstractLoggingClassLoader(final ClassLoader parent, final File pSource) {
            super(parent);
            source = pSource;
        }


        protected AbstractLoggingClassLoader(final File pSource) {
            source = pSource;
        }

        protected abstract InputStream getInputStream(String className);


        protected File getSource() {
            return source;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            System.out.println("Trying to find");
            throw new ClassNotFoundException();
        }


        @Override
        protected synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
            try {
                System.out.println("Trying to load");
                System.out.println("Loading class in Child : " + className);
                byte classByte[];
                Class result;


                InputStream is = getInputStream(className);
                if (is == null)
                    return null;
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                int nextValue = is.read();
                while (-1 != nextValue) {
                    byteStream.write(nextValue);
                    nextValue = is.read();
                }

                classByte = byteStream.toByteArray();

                result = defineClass(className, classByte, 0, classByte.length, null);
                return result;
            }
            catch (IOException e) {
                return null;

            }
        }

    }


    protected class JarClassLoader extends AbstractLoggingClassLoader {


        public JarClassLoader(ClassLoader parent, File jf) {
            super(parent, jf);
        }

        @Override
        public boolean isJarLoader() {
            return true;
        }

        @Override
        protected InputStream getInputStream(final String className) {
            try {
                JarFile jarFile = new JarFile(getSource());
                JarEntry entry = jarFile.getJarEntry(className.replace(".", "/") + ".class");
                if (entry == null)
                    return null;
                return jarFile.getInputStream(entry);
            }
            catch (IOException e) {
                return null;

            }
        }
    }

    protected class DirectoryClassLoader extends AbstractLoggingClassLoader {
        public DirectoryClassLoader(ClassLoader parent, File jf) {
            super(parent, jf);
        }

        @Override
        public boolean isJarLoader() {
            return false;
        }

        @Override
        protected InputStream getInputStream(final String className) {
            File f = new File(getSource(), className.replace(".", "/") + ".class");
            if (!f.exists())
                return null;
            try {
                //noinspection UnnecessaryLocalVariable
                InputStream is = new FileInputStream(f);
                return is;
            }
            catch (FileNotFoundException e) {
                return null;

            }
        }

    }

}
