package zwj.test.testASM;



import org.objectweb.asm.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by zhangwj on 16-4-6.
 * 本测试类用来对UDF jar进行安全性审查
 */
public class FunctionJarSandboxChecker {
    private static final boolean UDF_SAFE_CHECK_DISABLED = false;//安全检查开关

    /***
     * 不允许在UDF中使用的类集合
     */
    private static final Set<String> DISALLOWED_CLASSES = new HashSet<String>();

    static {
        DISALLOWED_CLASSES.add("java/lang/Class");
        DISALLOWED_CLASSES.add("java/lang/ClassLoader");
        DISALLOWED_CLASSES.add("java/lang/System");
        DISALLOWED_CLASSES.add("java/io/File");
        DISALLOWED_CLASSES.add("java/net/InetAddress");
        DISALLOWED_CLASSES.add("java/net/Inet4Address");
        DISALLOWED_CLASSES.add("java/net/Inet6Address");
        DISALLOWED_CLASSES.add("java/net/NetworkInterface");
        DISALLOWED_CLASSES.add("java/net/SocketException");
        DISALLOWED_CLASSES.add("java/lang/Thread");
    }



    /***
     * 不允许进行调用的方法，格式是类名 + "##" + 方法名
     */
    private static final Set<String> DISALLOWED_METHODS = new HashSet<String>();


    static {
        DISALLOWED_METHODS.add("java/lang/Object##clone");
        DISALLOWED_METHODS.add("java/lang/Object##finalize");
        DISALLOWED_METHODS.add("java/lang/Object##notify");
        DISALLOWED_METHODS.add("java/lang/Object##notifyAll");
        DISALLOWED_METHODS.add("java/lang/Object##wait");
    }

    /***
     * 允许在UDF调用的方法，用于过滤出允许调用的方法，而将不允许的方法添加到不允许集合中
     */
    private static final Set<String> ALLOWED_METHODS = new HashSet<String>();

    static {
        ALLOWED_METHODS.add("java/lang/System##currentTimeMillis");
        ALLOWED_METHODS.add("java/lang/System##nanoTime");
    }

    public Set<String> check(ClassLoader loader, List<String> jarFilePaths) throws Exception {
        Set<String> errors = new HashSet<String>();
        if (jarFilePaths == null || jarFilePaths.size() <= 0) {
            return errors;
        }
        for (String jarFilePath : jarFilePaths) {
            Set<String> partial = check(loader, new File(jarFilePath));
            if (partial != null && partial.size() > 0) {
                errors.addAll(partial);
            }
        }
        return errors;
    }


    /***
     * 该jarFile已经被指定的loader加载到classpath上
     *
     * @param loader
     * @param jarFile
     * @return 验证失败的校验信息
     */
    public Set<String> check(ClassLoader loader, File jarFile) throws Exception {
        final Set<String> errors = new HashSet<String>();
        /***
         * 如果不需要安全，则无需进行下面的检查
         */
        if (UDF_SAFE_CHECK_DISABLED) {
            return errors;
        }

        Map<String, byte[]> className2Bytes = className2ToBytes(loader, jarFile);
        Set<Map.Entry<String, byte[]>> entries = className2Bytes.entrySet();//把(key-value)作为一个整体放到集合中
        for (Map.Entry<String, byte[]> entry : entries) {
            byte[] classBytes = entry.getValue();
            ClassVisitor visitor = new ClassVisitor() {

                private void logError(String className, String methodName) {
                    errors.add(className + "##" + methodName);//自定义错误输出格式
                }

                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                }

                public void visitSource(String source, String debug) {

                }

                public void visitOuterClass(String owner, String name, String desc) {

                }

                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    return null;
                }

                public void visitAttribute(Attribute attr) {

                }

                public void visitInnerClass(String name, String outerName, String innerName, int access) {

                }

                public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                    return null;
                }

                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                        return new MethodVisitor() {    //参数为ASM版本号
                        public AnnotationVisitor visitAnnotationDefault() {
                            return null;
                        }

                        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                            return null;
                        }

                        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
                            return null;
                        }

                        public void visitAttribute(Attribute attr) {

                        }

                        public void visitCode() {

                        }

                        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {

                        }

                        public void visitInsn(int opcode) {

                        }

                        public void visitIntInsn(int opcode, int operand) {

                        }

                        public void visitVarInsn(int opcode, int var) {

                        }

                        public void visitTypeInsn(int opcode, String type) {

                        }

                        public void visitFieldInsn(int opcode, String owner, String name, String desc) {

                        }

                        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                            //会被调用，owner为class，name为method
                            //如果调用的方法在白名单中
                            if (ALLOWED_METHODS.contains(owner + "##" + name)) {
                                return;
                            }
                            if (DISALLOWED_CLASSES.contains(owner)) {
                                logError(owner, name);
                            } else if (DISALLOWED_METHODS.contains(owner + "##" + name)) {
                                logError(owner, name);
                            }
                        }

                        public void visitJumpInsn(int opcode, Label label) {

                        }

                        public void visitLabel(Label label) {

                        }

                        public void visitLdcInsn(Object cst) {

                        }

                        public void visitIincInsn(int var, int increment) {

                        }

                        public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {

                        }

                        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {

                        }

                        public void visitMultiANewArrayInsn(String desc, int dims) {

                        }

                        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {

                        }

                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {

                        }

                        public void visitLineNumber(int line, Label start) {

                        }

                        public void visitMaxs(int maxStack, int maxLocals) {

                        }

                        public void visitEnd() {

                        }
                    };
                }

                public void visitEnd() {

                }
            };

            ClassReader classReader = new ClassReader(classBytes);
            classReader.accept(visitor, ClassReader.SKIP_DEBUG);
        }


        return errors;
    }

    /***
     * 类名与字节码之间的映射关系
     *
     * @return
     */
    private Map<String, byte[]> className2ToBytes(ClassLoader loader, File jarFile) throws Exception {
        Map<String, byte[]> map = new HashMap<String, byte[]>();
        JarInputStream in = null;
        try {
            in = new JarInputStream(new FileInputStream(jarFile));
            JarEntry entry = null;

            while ((entry = in.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (name != null && name.endsWith("class")) {
                    InputStream resource = loader.getResourceAsStream(name);
                    byte[] classBytes = inputStream2Bytes(resource);
                    if (classBytes != null && classBytes.length > 0) {
                        map.put(name, classBytes);
                    }
                }
            }
            return map;
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    private byte[] inputStream2Bytes(InputStream in) throws Exception {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int read;
            while ((read = in.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return output.toByteArray();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
