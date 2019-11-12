package com.ftofs.twant.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.ftofs.twant.log.SLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;

/**
 * 文件操作
 * @author zwm
 */
public class FileUtil {
    /*
    openFileOutput
    打開數據文件，适合放一些比较重要的数据，可在【系统-应用-清除数据】把数据清理掉

    Context.getCacheDir()
    這個目錄適合放置臨時數據，可以通过【系统-应用-清空缓存】可以把这些数据清除掉
     */
    /**
     * 獲取緩存文件
     * @param context
     * @param path 文件名： path前面不帶 / ，例如: a.txt 或 test/a.txt 或 test1/test2/a.txt
     * @return
     */
    public static File getCacheFile(Context context, String path) {
        File cacheDir = context.getCacheDir();
        String subDir = PathUtil.getDirectory(path);

        if (!StringUtil.isEmpty(subDir)) {
            cacheDir = new File(cacheDir, subDir);
            if (!cacheDir.exists()) {
                boolean success = cacheDir.mkdirs();
                if (!success) {
                    return null;
                }
            }
        }


        String filename = PathUtil.getFilename(path);
        return new File(cacheDir, filename);
    }

    /**
     * 獲取壓縮后的圖片文件(同步方式)
     * @param context
     * @param file
     * @return
     */
    public static File getCompressedImageFile(Context context, File file) {
        try {
            List<File> fileList = Luban.with(context).load(file).get();

            if (fileList != null && fileList.size() > 0) {
                return fileList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 根据Uri获取图片文件的绝对路径
     * @param context  一般传入Activity
     * @param uri
     * @return
     */
    public static String getRealFilePath(Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getFileProviderAuthority(Context context) {
        // 返回这个字符串  com.ftofs.twant.fileprovider
        return context.getPackageName() + ".fileprovider";
    }

    public static Uri getCompatUriFromFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
                /*
                从Android 7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，会抛出一个FileUriExposedException异常(参考《第一行代码(第2版)》第8章)
                FileProvider则是一种特殊的内容提供器，它使用了和内容提供器类似的机制来对数据进行保护，可以选择性地将封装过的Uri【共享给外部】，从而提高了应用的安全性。
                */
            String authority = getFileProviderAuthority(context);
            uri = FileProvider.getUriForFile(context, authority, file);
        }

        return uri;
    }

    /**
     * 讀取小文件
     * @param file
     * @return
     */
    public static byte[] readFile(File file) {
        if (!file.isFile()) {
            return null;
        }

        long size = file.length();
        byte[] buffer = new byte[(int) size];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(buffer);
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 创建目录，并添加nomedia文件
     * @param dir
     */
    public static boolean createDirNomedia(File dir) throws IOException {
        List<String> folderList = new ArrayList<>();
        // 遍历每一层，都要加上nomedia文件
        while (!dir.exists()) {
            folderList.add(dir.getName());
            dir = dir.getParentFile();
        }

        for (int i = folderList.size() - 1; i >= 0; --i) {
            String folderName = folderList.get(i);
            dir = new File(dir, folderName);
            // 创建目录
            if (!dir.mkdir()) {
                return false;
            }
            // 在目录中新建.nomedia文件
            File nomediaFile = new File(dir, ".nomedia");
            nomediaFile.createNewFile();
        }

        return true;
    }

    /**
     * 获取App数据路径
     * 类似 /storage/emulated/0/twant 的这种路径
     * @return
     */
    public static String getAppDataRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/twant";
    }

    public static String getImageRoot() {
        return getAppDataRoot() + "/DCIM";
    }

    /**
     * 獲取今天拍照的照片存放目錄(每天一個存放目錄)
     * @return
     */
    public static String getTodayImageRoot() {
        Jarbon jarbon = new Jarbon();
        return getImageRoot() + "/" + jarbon.format("Ymd");
    }


    /**
     * 如果指定的目錄不存在，則創建它
     * @param file 指定的目錄
     * @return
     */
    public static boolean createOrExistsDir(File file) {
        try {
            return file != null && (file.exists() ? file.isDirectory() : createDir(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建目录
     * @param dir
     */
    public static boolean createDir(File dir) throws IOException {
        List<String> folderList = new ArrayList<>();
        // 遍历每一层
        while (!dir.exists()) {
            folderList.add(dir.getName());
            dir = dir.getParentFile();
        }

        for (int i = folderList.size() - 1; i >= 0; --i) {
            String folderName = folderList.get(i);
            dir = new File(dir, folderName);
            // 创建目录
            if (!dir.mkdir()) {
                return false;
            }
        }

        return true;
    }

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a kilobyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a megabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);

    /**
     * The file copy buffer size (128 KB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = ONE_KB * 128;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * The number of bytes in a gigabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);

    /**
     * The number of bytes in a terabyte.
     */
    public static final long ONE_TB = ONE_KB * ONE_GB;

    /**
     * The number of bytes in a terabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);

    /**
     * The number of bytes in a petabyte.
     */
    public static final long ONE_PB = ONE_KB * ONE_TB;

    /**
     * The number of bytes in a petabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);

    /**
     * The number of bytes in an exabyte.
     */
    public static final long ONE_EB = ONE_KB * ONE_PB;

    /**
     * The number of bytes in an exabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);

    /**
     * The number of bytes in a zettabyte.
     */
    public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));

    /**
     * The number of bytes in a yottabyte.
     */
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);



    /**
     * Copies a file to a new location preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however
     * it is not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile  an existing file to copy, must not be {@code null}
     * @param destFile the new file, must not be {@code null}
     *
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @throws IOException          if the output file length is not the same as the input file length after the copy
     * completes
     * //@see #copyFileToDirectory(File, File)
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFile(final File srcFile, final File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    /**
     * Copies a file to a new location.
     * <p>
     * This method copies the contents of the specified source file
     * to the specified destination file.
     * The directory holding the destination file is created if it does not exist.
     * If the destination file exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the file's last modified
     * date/times using {@link File#setLastModified(long)}, however it is
     * not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile          an existing file to copy, must not be {@code null}
     * @param destFile         the new file, must not be {@code null}
     * @param preserveFileDate true if the file date of the copy
     *                         should be the same as the original
     *
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @throws IOException          if the output file length is not the same as the input file length after the copy
     * completes
     * //@see #copyFileToDirectory(File, File, boolean)
     * @see #doCopyFile(File, File, boolean)
     */
    public static void copyFile(final File srcFile, final File destFile,
                                final boolean preserveFileDate) throws IOException {
        checkFileRequirements(srcFile, destFile);
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        final File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!createOrExistsDir(parentFile) && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    /**
     * Internal copy file method.
     * This caches the original file length, and throws an IOException
     * if the output file length is different from the current input file length.
     * So it may fail if the file changes size.
     * It may also fail with "IllegalArgumentException: Negative size" if the input file is truncated part way
     * through copying the data and the new file size is less than the current position.
     *
     * @param srcFile          the validated source file, must not be {@code null}
     * @param destFile         the validated destination file, must not be {@code null}
     * @param preserveFileDate whether to preserve the file date
     * @throws IOException              if an error occurs
     * @throws IOException              if the output file length is not the same as the input file length after the
     * copy completes
     */
    private static void doCopyFile(final File srcFile, final File destFile, final boolean preserveFileDate)
            throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = new FileInputStream(srcFile);
        FileChannel input = fis.getChannel();
        FileOutputStream fos = new FileOutputStream(destFile);
        FileChannel output = fos.getChannel();
        final long size = input.size(); // TODO See IO-386
        long pos = 0;
        long count = 0;
        while (pos < size) {
            final long remain = size - pos;
            count = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
            final long bytesCopied = output.transferFrom(input, pos, count);
            if (bytesCopied == 0) { // IO-385 - can happen if file is truncated after caching the size
                break; // ensure we don't loop forever
            }
            pos += bytesCopied;
        }


        final long srcLen = srcFile.length(); // TODO See IO-386
        final long dstLen = destFile.length(); // TODO See IO-386
        if (srcLen != dstLen) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }


    /**
     * Moves a file.
     * <p>
     * When the destination file is on another file system, do a "copy and delete".
     *
     * @param srcFile  the file to be moved
     * @param destFile the destination file
     * @throws IOException  if the destination file exists
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs moving the file
     * @since 1.4
     */
    public static void moveFile(final File srcFile, final File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new IOException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        final boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                FileUtil.deleteFile(destFile);
                throw new IOException("Failed to delete original file '" + srcFile +
                        "' after copy to '" + destFile + "'");
            }
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise
     * {@code false}
     *
     * @since 1.4
     */
    public static boolean deleteFile(final File file) {
        if (file == null) {
            return false;
        }

        try {
            return file.delete();
        } catch (final Exception ignored) {
            return false;
        }
    }


    /**
     * checks requirements for file copy
     * @param src the source file
     * @param dest the destination
     * @throws FileNotFoundException if the destination does not exist
     */
    private static void checkFileRequirements(final File src, final File dest) throws FileNotFoundException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (dest == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!src.exists()) {
            throw new FileNotFoundException("Source '" + src + "' does not exist");
        }
    }



    /**
     * Writing content to file.
     * @param file
     * @param content
     */
    public static void saveContent(File file, String content) {
        try {
            FileWriter fileWriter;
            fileWriter = new FileWriter(file.getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
