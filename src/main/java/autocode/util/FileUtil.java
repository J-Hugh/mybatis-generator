/**
 * $Id: FileUtil.java,v 1.1 2012/09/11 06:36:24 chenhua Exp $
 */
package autocode.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 文件操作工具类
 * </p>
 * 
 * @author ResourceOne BizFoundation Team: zhaoqy
 * @version 1.0
 * @since 4.2
 */
public class FileUtil {
	static Logger log = LoggerFactory.getLogger(FileUtil.class);
    public static String fileSeparator =  "/";
    /**
     * <p>
     * 删除指定路径的内容，路径可以是文件或者目录
     * </p>
     * 
     * <pre>
     * Example:
     * 删除D:/Resource/xml 文件夹以及下面的所有内容：
     * File path = new File(&quot;D:/Resource/xml/&quot;);
     * FileUtil.rmdirs(path);
     * </pre>
     * 
     * @param path 路径
     */
    public static void rmdirs(File path) {
        try {
            if (path.isFile()) {
                path.delete();
                return;
            }
            File[] subs = path.listFiles();
            if (subs.length == 0) {
                path.delete();
            } else {
                for (int i = 0; i < subs.length; i++) {
                    rmdirs(subs[i]);
                }
                path.delete();
            }
        } catch (Exception ex) {
        }
    }

    /**
     * <p>
     * 复制目录（文件夹）下的所有内容到指定的目录。
     * </p>
     * <p>
     * 1、如果目标目录不存在，会按照源目录创建。
     * </p>
     * <p>
     * 2、如果目标目录已经存在，会覆盖目标目录下的同名文件。
     * </p>
     * 
     * <pre>
     * Example:
     * 复制D:/Resource/xml文件夹的内容到D:/Resource/xml_new
     * File src = new File(&quot;D:/Resource/xml/&quot;);
     * File destine = new File(&quot;D:/Resource/xml_new/&quot;);
     * FileUtil.rmdirs(src,destine);
     * </pre>
     * 
     * @param src 源目录
     * @param destine 目标目录
     */
    public static void xcopy(File src, File destine) {
        try {
            if (!src.exists() || src.getCanonicalPath().equals(destine.getCanonicalPath()))
                return;
        } catch (IOException ex) {
        }
        File[] chs = src.listFiles();
        for (int i = 0; i < chs.length; i++) {
            if (chs[i].isFile()) {
                File destineFile = new File(destine, chs[i].getName());
                copy(chs[i], destineFile);
            } else {
                File destineDir = new File(destine, chs[i].getName());
                destineDir.mkdirs();
                xcopy(chs[i], destineDir);
            }
        }
    }

    /**
     * <p>
     * 移动目录,将源目录移动到指定的目录下，移动完毕后，将原目录删除。
     * <p>
     * 
     * @param src 要移动的目录
     * @param destine 目标目录
     */
    public static void move(File src, File destine) {
        try {
            if (!src.exists() || src.getCanonicalPath().equals(destine.getCanonicalPath()))
                return;
        } catch (IOException ex) {
        }
        copy(src, destine);
        src.delete();
    }

    /**
     * <p>
     * 递归删除一个目录,会删除给定目录下所有的文件(夹) 如果删除失败，会抛出异常。
     * </p>
     * 
     * @param directory 要删除的目录
     * @throws IOException 删除失败抛出的异常
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * <p>
     * 创建目录。 如果文件名称已经存在，该目录不能创建，然后抛出一个异常。
     * </p>
     * 
     * <pre>
     * Example:
     * File file = new File(&quot;D:/Workspace&quot;);
     * FileUtil.forceMkdir(file);
     * </pre>
     * 
     * @param directory 要创建的目录
     * @throws IOException 如果不能创建，抛出的异常
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (directory.isFile()) {
                String message = "File " + directory + " exists and is "
                        + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (false == directory.mkdirs()) {
                String message = "Unable to create directory " + directory;
                throw new IOException(message);
            }
        }
    }

    /**
     * <p>
     * 复制文件
     * </p>
     * 
     * @param src
     * @param destine
     */
    public static void copy(File src, File destine) {
        try {
            if (!src.exists() || src.getCanonicalPath().equals(destine.getCanonicalPath()))
                return;
            FileInputStream fins = new FileInputStream(src);
            copy(fins, destine);
            destine.setLastModified(src.lastModified());
        } catch (Exception e) {
        }
    }

    /**
     * <p>
     * 复制文件
     * </p>
     * 
     * @param fins
     * @param destine
     */
    public static void copy(InputStream fins, File destine) {
        try {
            if (fins == null)
                return;
            destine.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(destine);
            byte[] buf = new byte[1024];
            int readLen;
            while ((readLen = fins.read(buf, 0, buf.length)) > 0) {
                fos.write(buf, 0, readLen);
            }
            fos.flush();
            fos.close();
            fins.close();
        } catch (Exception ex) {
        }
    }

    /**
     * <p>
     * 返回指定目录的大小
     * </p>
     * <p>
     * Recursively count size of a directory (sum of the length of all files).
     * <p>
     * 
     * @param directory directory to inspect
     * @return size of directory in bytes.
     */
    public static long sizeOfDirectory(File directory) {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        long size = 0;

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                size += sizeOfDirectory(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    /**
     * <p>
     * 对比两个文件的创建时间，如果file是新创建的返回true
     * </p>
     * Tests if the specified <code>File</code> is newer than the reference
     * <code>File</code>.
     * 
     * @param file the <code>File</code> of which the modification date must be compared
     * @param reference the <code>File</code> of which the modification date is used
     *            like reference
     * @return true if the <code>File</code> exists and has been modified more recently
     *         than the reference <code>File</code>.
     */
    public static boolean isFileNewer(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '" + file + "' doesn't exist");
        }

        return isFileNewer(file, reference.lastModified());
    }

    /**
     * 
     * <p>
     * 与给定的时间（<code>Date</code>）相比，文件是否是新创建的。如果是返回true。
     * </p>
     * 
     * Tests if the specified <code>File</code> is newer than the specified
     * <code>Date</code>
     * 
     * @param file the <code>File</code> of which the modification date must be compared
     * @param date the date reference
     * @return true if the <code>File</code> exists and has been modified after the
     *         given <code>Date</code>.
     */
    public static boolean isFileNewer(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileNewer(file, date.getTime());
    }

    /**
     * <p>
     * 与给定的时间（毫秒数）相比，文件是否是新创建的。如果是返回true。
     * </p>
     * <p>
     * Tests if the specified <code>File</code> is newer than the specified time
     * reference.
     * </p>
     * 
     * @param file the <code>File</code> of which the modification date must be
     *            compared.
     * @param timeMillis the time reference measured in milliseconds since the epoch
     *            (00:00:00 GMT, January 1, 1970)
     * @return true if the <code>File</code> exists and has been modified after the
     *         given time reference.
     */
    public static boolean isFileNewer(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }

        return file.lastModified() > timeMillis;
    }

    /**
     * <p>
     * 将<code>URL</code>转换为<code>File</code>
     * </p>
     * Convert from a <code>URL</code> to a <code>File</code>.
     * 
     * @param url File URL.
     * @return The equivalent <code>File</code> object, or <code>null</code> if the
     *         URL's protocol is not <code>file</code>
     */
    public static File toFile(URL url) {
        if (url.getProtocol().equals("file") == false) {
            return null;
        } else {
            String filename = url.getFile().replace('/', File.separatorChar);
            return new File(filename);
        }
    }

    /**
     * <p>
     * 将<code>File</code>数组转换为<code>URL</code>数组
     * </p>
     * <p>
     * Convert the array of Files into a list of URLs.
     * </p>
     * 
     * @param files the array of files
     * @return the array of URLs
     * @throws IOException if an error occurs
     */
    public static URL[] toURLs(File[] files) throws IOException {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURL();
        }

        return urls;
    }

    /**
     * <p>
     * 清空指定的目录，改操作不会删除目录，只会清空
     * </p>
     * Clean a directory without deleting it.
     * 
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        IOException exception = null;

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * <p>
     * 强行删除file对象，如果是目录对象则递归删除子目录。如果删除失败则显式地抛出IOException
     * </p>
     * <p>
     * Delete a file. If file is a directory, delete it and all sub-directories.
     * </p>
     * <p>
     * The difference between File.delete() and this method are:
     * </p>
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted. (java.io.File
     * methods returns a boolean)</li>
     * </ul>
     * 
     * @param file file or directory to delete.
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            if (!file.delete()) {
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * <p>
     * 当JVM退出时，把file对象删除。如果是目录对象则递归删除子目录
     * </p>
     * 
     * <p>
     * Schedule a file to be deleted when JVM exits. If file is directory delete it and
     * all sub-directories.
     * </p>
     * 
     * @param file file or directory to delete.
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * <p>
     * 当JVM退出时，把目录删除。如果是目录对象则递归删除子目录
     * </p>
     * 
     * Recursively schedule directory for deletion on JVM exit.
     * 
     * @param directory directory to delete.
     * @throws IOException in case deletion is unsuccessful
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectoryOnExit(directory);
        directory.deleteOnExit();
    }

    /**
     * <p>清空目录</p>
     * Clean a directory without deleting it.
     * 
     * @param directory directory to clean.
     * @throws IOException in case cleaning is unsuccessful
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        IOException exception = null;

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    
    /**
     * 把文本text用encode编码写到filePath文件里
     * 
     * @param filePath
     * @param text
     * @param encode
     * @throws RuntimeException
     */
    public static void toFile(String filePath, String text) throws RuntimeException {
        toFile(filePath,text,null);
    }
    /**
     * 把文本text用encode编码写到fileName文件里，并创建filePath文件
     * 
     * @param filePath
     * @param text
     * @param encode
     * @throws RuntimeException
     */
    public static void toFile(String filePath, String text, String encode) throws RuntimeException {
        OutputStreamWriter filewriter = null;
        try {
            createFile(filePath);
            if (StringUtil.isNotBlank(encode)) {
                filewriter = new OutputStreamWriter(new FileOutputStream(filePath), encode);
            } else {
                filewriter = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
            }
            filewriter.write(text);
            filewriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            if (filewriter != null) {
                try {
                    filewriter.close();
                } catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
    }
    
    /**
     * 通过包名和目录路径生成对应的文件路径
     * <pre>
     * 输入：  packageName=com.csi.samppleapp.base.model 
     *      dirPath=d:/tmp
     * 输出：d:/tmp/com/csi/samppleapp/base/model
     * </pre>
     * 
     * @param packageName 包名
     * @param baseDirPath 目录路径
     * @return
     */
    public static String getDirPath(String packageName, String baseDirPath) {
        String filePath = ""; 
        if (StringUtil.isNotBlank(packageName)) {
            filePath = StringUtil.package2Path(packageName);
        }
        if (StringUtil.isBlank(baseDirPath)) {
            return filePath; 
        } 
        return  baseDirPath + "/" + filePath; 
    }

    /**
     * <p>通过文件路径（包括文件名）,获得目录和文件名数组</p>
     * <pre>
     * 输入：filePath=d:/tmp/ganjp.txt
     * 输出：String[]{"d:/tmp","ganjp.txt"}
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String [] getDirPathAndFileName(String filePath) {
        String tmpFilePath = filePath.replace('/',File.separatorChar);
        if (tmpFilePath.indexOf(File.separatorChar)==-1) {
            log.error(filePath + "不是合法的路径,请输入路径如：d:/tmp/ganjp.txt");
            throw new RuntimeException(filePath + "不是合法的路径,请输入路径如：d:/tmp/ganjp.txt");
        }
        String dirPath = filePath.substring(0,tmpFilePath.lastIndexOf(File.separatorChar));
        String fileName = filePath.substring(tmpFilePath.lastIndexOf(File.separatorChar)+1, filePath.length());
        return new String[]{dirPath, fileName};
    }
    
    /**
     * 创建文件
     * 
     * @param filePath
     * @throws RuntimeException
     */
    public static File createFile(String filePath) throws RuntimeException {
        try {
            File file = new File(filePath);
            if(!file.exists()){
                String tmpFile = filePath.replace('/',File.separatorChar);
                String dir = filePath.substring(0,tmpFile.lastIndexOf(File.separatorChar));
                File dirFile = new File(dir);
                if(!dirFile.exists()){
                    dirFile.mkdirs();
                }
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }    
    }
    
    /**
     * 读取文件内容,并把内容以字符串形式返回
     * 
     * @param filePath 文件路径
     * @param encode   编码
     * @return
     */
    public static String readFile(String filePath)  {
        return readFile(filePath, null);
    }
    
    /**
     * 读取文件内容,并把内容以字符串形式返回
     * 
     * @param filePath 文件路径
     * @param encode   编码
     * @return
     */
    public static String readFile(String filePath, String encode) {
        InputStreamReader fileReader = null;
        try {
            if (StringUtil.isNotBlank(encode)) {
                fileReader = new InputStreamReader(new FileInputStream(filePath), encode);
            } else {
                fileReader = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            }
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuffer strBuff = new StringBuffer();
            String str = reader.readLine();
            while (str != null) {
                if(strBuff.length() >0){
                    strBuff.append("\r\n" + str);
                }else {
                    strBuff.append(str);
                }
                str = reader.readLine();
            }
            return strBuff.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
    }
    
    /**
     * 如果文件filePath里存在着existStr字符串，那么用regexExist规则替换成replaceStrExist字符串，否则使用regexNoExist字符串替换成replaceStrNoExit
     * 
     * @param filePath
     * @param existStr
     * @param regexExist
     * @param replaceStrExist
     * @param regexNoExist
     * @param replaceStrNoExit
     * 
     */
    public static void writeOrReplaceText(String filePath, String existStr, String regexExist, String replaceStrExist, String regexNoExist,
            String replaceStrNoExit) {
        String fileStr = readFile(filePath);
        if (fileStr.indexOf(existStr)==-1) {
            fileStr = fileStr.replaceAll(regexNoExist, replaceStrNoExit);
        } else {
            fileStr = fileStr.replaceAll(regexExist, replaceStrExist);
        }
        toFile(filePath, fileStr);
    }
    
    /**
     * 将文本内容(text)替换或者追加到文件中
     * @param filePath 文件路径
     * @param existStr 是否存在的字符串标识，如果文件中存在该标识字符串则替换，否则追加
     * @param regexExist 内容替换的正则表达式
     * @param text 文本内容
     */
    public static void replaceOrAppenderText(String filePath,String existStr,String regexExist,String text){
        String fileStr = readFile(filePath);
        if(fileStr.indexOf(existStr) == -1){
            fileStr += "\r\n" + text;
        }else {
            fileStr = fileStr.replaceAll(regexExist, text);
        }
        toFile(filePath,fileStr);
    }

    /**
     * 将文本内容(text)替换或者追加到文件中
     * @param filePath 文件路径
     * @param existStr 是否存在的字符串标识，如果文件中存在该标识字符串则替换，否则追加
     * @param regexExist 内容替换的正则表达式
     * @param text 文本内容
     */
    public static void replaceOrAppenderAsciiText(String filePath,String existStr,String regexExist,String text){
        String fileStr = readFile(filePath);
        if(fileStr.indexOf(existStr) == -1){
            fileStr += "\r\n" + text;
        }else {
            fileStr = fileStr.replaceAll(regexExist, text);
        }
        fileStr = StringUtil.native2Ascii(fileStr);
        toFile(filePath,fileStr);
    }
    /**
     * 判断文件是否存在
     * @param filePath
     * @return
     */
    public static boolean exist(String filePath){
    	if(filePath == null){
    		return false;
    	}
    	File file = new File(filePath);
    	return file.exists();
    }
    
    public static void main(String[] args) {
        try {
            //toFile("tmp/com/Demo.java", "public class Demo {}");
            String filePath = "modules/base/resources/spring/applicationContext-base.xml";
            
            String existStr = "<!--%s1-start-->";
            String regexExist = "<!--%s1-start-->(?s:.)*<!--%s1-end-->";            
            String replaceStrExist = "<!--%s1-start-->\r\n        <bean name=\"%s1\" class=\"%s2\" />\r\n        <!--%s1-end-->";
            String regexNoExist = "</beans>";
            String replaceStrNoExit = "        " + replaceStrExist + "\r\n</beans>";
            
            writeOrReplaceText(filePath, existStr, regexExist, replaceStrExist, regexNoExist, replaceStrNoExit);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
