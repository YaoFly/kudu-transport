package toolsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.h58pic.H58pic_Kudu;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ooopic on 2017/2/21.
 */
public class FileUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    public static List<String> getFilePath(String filePath, boolean isRecursive) {
        List<String> list = new ArrayList<>();
        File f = new File(filePath);

        File fa[] = f.listFiles();
        assert fa != null;
        for (File fs : fa) {
            if (!fs.isDirectory()) {
                list.add(fs.getAbsolutePath());
                LOG.info("read file : "+fs.getAbsolutePath());
            } else if (isRecursive) {
                list.addAll(getFilePath(fs.getAbsolutePath(), true));
            }
        }
        return list;
    }

    public static byte[] readFile(String Path) {
        File imgFile = new File(Path);
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] readFile(InputStream in) {
        byte[] data = null;
        //读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public synchronized static void outPutFile(String content, String path, boolean append) {
        FileOutputStream opt = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.createNewFile();
            }
            opt = new FileOutputStream(file, append);
            opt.write(content.getBytes());
            opt.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (opt != null) {
                try {
                    opt.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
