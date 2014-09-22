/**{ file
 @name DependencyClassLoader.java
 @function - A classloader which reports Dependencies
 @author> Steven M. Lewis
 @copyright>
  ************************
  *  Copyright (c) 98
  *  Steven M. Lewis smlewis@lordjoe.co,
  *  www.LordJoe.com
 ************************
 @date> Mon Jun 22 21:48:24 PDT 1998
 @version> 1.0
 }*/
package com.lordjoe.utilities;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class DependencyClassLoader extends ClassLoader {
    private Map<String,Dependency> m_Dependencies;
    private Stack<Dependency> m_Classes;
    public static final Dependency[] NULL_ARRAY = {};


    public DependencyClassLoader() {
        super(java.lang.String.class.getClassLoader());
        m_Dependencies = new HashMap<String,Dependency>();
        m_Classes = new Stack<Dependency>();
    }

    public static Dependency getDependencies(String ClassName) {
        DependencyClassLoader TheLoader = new DependencyClassLoader();
        TheLoader.clearDependencies();
        try {
            TheLoader.loadClass(ClassName, true);
            return ((TheLoader.getClassDependencies(ClassName)));
        }
        catch (ClassNotFoundException ex) {
            return (null);
        }
    }

    public static void listDependencies(String ClassName, PrintStream out) {
        Dependency dep = getDependencies(ClassName);
        dep.list(out);

    }


    /**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     *
     * @param name the name of the class
     * @return the resulting class
     * @throws ClassNotFoundException if the class could not be found
     */
    @Override
    protected Class findClass(final String name)
            throws ClassNotFoundException {
        try {
            Class<?> aClass = super.findClass(name);
            return aClass;
        }
        catch (ClassNotFoundException e) {
            System.out.println("Cannot load class " + name);
            throw new RuntimeException(e);

        }
    }




    /**
     * Loads a given class from .class file just like
     * the default ClassLoader. This method could be
     * changed to load the class over network from some
     * other server or from the database.
     *
     * see   http://k2java.blogspot.com/2011/04/writing-custom-class-loader-in-java.html
     * @param name Fully qualified class name
     */
    protected Class<?> getClass(String name)
        throws ClassNotFoundException {
        // We are getting a name that looks like
        // com.vaani.ClassToLoad
        // and we have to convert it into the .class file name like
        // com/vaani/ClassToLoad.class
        String file = name.replace('.', File.separatorChar)
            + ".class";
        byte[] b = null;
        try {
            // This loads the byte code data from the file
            b = loadClassData(file);
            // defineClass is inherited from the ClassLoader class
            // and converts the byte array into a Class
            Class<?> c = defineClass(name, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Loads a given file (presumably .class) into a byte array.
     * The file should be accessible as a resource, for example
     * it could be located on the classpath.
     *
     * @param name File name to load
     * @return Byte array read from the file
     * @throws IOException Is thrown when there
     *               was some problem reading the file
     */
    protected byte[] loadClassData(String name) throws IOException {
        // Opening the file
        InputStream stream = getClass().getClassLoader()
            .getResourceAsStream(name);
        int size = stream.available();
        byte buff[] = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        // Reading the binary data
        in.readFully(buff);
        in.close();
        return buff;
    }


    /**
     * Loads the class with the specified name.  The default implementation of
     * this method searches for classes in the following order:<p>
     * <p/>
     * <ol>
     * <li> Call {@link #findLoadedClass(String)} to check if the class has
     * already been loaded. <p>
     * <li> Call the <code>loadClass</code> method on the parent class
     * loader.  If the parent is <code>null</code> the class loader
     * built-in to the virtual machine is used, instead. <p>
     * <li> Call the {@link #findClass(String)} method to find the class. <p>
     * </ol>
     * <p/>
     * If the class was found using the above steps, and the
     * <code>resolve</code> flag is true, this method will then call the
     * {@link #resolveClass(Class)} method on the resulting class object.
     * <p/>
     * From JDK1.2, subclasses of ClassLoader are encouraged to override
     * {@link #findClass(String)}, rather than this method.<p>
     *
     * @param name    the name of the class
     * @param resolve if <code>true</code> then resolve the class
     * @throws ClassNotFoundException if the class could not be found
     * @return the resulting <code>Class</code> object
     */
    @Override
    protected synchronized Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        Dependency thisDep = getClassDependencies(name);
        if (m_Classes.size() > 0) {
            (  m_Classes.peek()).addDependency(thisDep);
        }
        m_Classes.push(thisDep);
        Class ret = super.loadClass(name, resolve);
        m_Classes.pop();
        return (ret);
    }


    public void clearDependencies() {
        m_Dependencies.clear();
    }

    public Dependency[] getAllDependencies() {
        Collection values = m_Dependencies.values();
        Dependency[] ret = new Dependency[values.size()];
        values.toArray(ret);
        return (ret);
    }

    public Dependency getClassDependencies(String ClassName) {
        Dependency ret =  m_Dependencies.get(ClassName);
        if (ret == null) {
            ret = new Dependency(ClassName);
            m_Dependencies.put(ClassName, ret);
        }
        return (ret);
    }


    public static class Dependency {
        public String m_ClassName;
        public Dependency[] m_Dependencies;


        public Dependency(String name) {
            m_ClassName = name;
            m_Dependencies = NULL_ARRAY;
        }

        public synchronized void addDependency(Dependency added) {
            m_Dependencies = Util.addToArray(m_Dependencies, added);
        }

        public void list(PrintStream out) {
            list(out, 0);
        }

        protected void list(PrintStream out, int indent) {
            Util.indent(out, indent);
            out.println(m_ClassName);
            for (int i = 0; i < m_Dependencies.length; i++) {
                m_Dependencies[i].list(out, indent + 1);
            }
        }


    }


    /**
     * run the main method from class mainnClass using args except for arg1
     * @param mainClass   !null main class
     * @param args     !null args the first is the mainClassName - all othera are the real arguments
     */
    private static void runMainDroppingArg1( Class mainClass,String[] args) {
         // copy arguments into other args
        String[] otherArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            otherArgs[i - 1] = args[i];

        }
        runMain(mainClass, otherArgs);
    }
    /**
     * run the main method from class mainnClass using args
     * @param mainClass   !null main class
     * @param args     !null   the real arguments
     */
    public static void runMain(Class mainClass, String[] otherArgs) {
        try {
            Method met = mainClass.getMethod("main", String[].class);
            Object[] objArgs = {otherArgs};
            met.invoke(null, objArgs);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);

        }
    }
    public static void main(String[] args) throws Exception {
        String targetClass = args[0];
        ClassUtilities.validateClassPath();
            Class mainClass = Class.forName(targetClass);
          runMainDroppingArg1(mainClass,args);

    }

}
