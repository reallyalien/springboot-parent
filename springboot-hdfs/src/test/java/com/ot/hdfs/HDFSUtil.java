package com.ot.hdfs;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.io.compress.snappy.SnappyDecompressor;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class HDFSUtil {

    public static boolean testConnection(String url, String user) {
        Configuration configuration = new Configuration();
        boolean f = true;
        Path path = new Path("/" + UUID.randomUUID().toString());
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(url), configuration, user);
            fs.mkdirs(path);
        } catch (Exception e) {
            f = false;
        } finally {
            if (f) {
                try {
                    fs.delete(path, true);
                } catch (IOException e) {
                }
            }
        }
        return f;
    }

    /**
     * 读取hdfs指定路径的内容
     */
    public static List<Map<String, Object>> getHDFSData(String hDFSPath, FileSystem fileSystem) {
        String result = "";
        if (StringUtils.isNotEmpty(hDFSPath)) {
            Path path = new Path(hDFSPath);
            FSDataInputStream fsDataInputStream = null;
            BufferedReader br = null;
            // 定义一个字符串用来存储文件内容
            try {
                fsDataInputStream = fileSystem.open(path);
                br = new BufferedReader(new InputStreamReader(fsDataInputStream));
                String str2;
                while ((str2 = br.readLine()) != null) {
                    // 遍历抓取到的每一行并将其存储到result里面
                    result += str2 + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fsDataInputStream != null) {
                    try {
                        fsDataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileSystem != null) {
                    try {
                        fileSystem.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.info("HDFS文件路径：【{}】,文件内容: 【{}】", hDFSPath, result);
        }
        String[] split = result.split("\n");
        List<String> list = Arrays.asList(split);
        return null;
    }


    public static void decompres(String filename, FileSystem fileSystem) throws FileNotFoundException, IOException {

        Configuration conf = new Configuration();
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);

        // 1 获取文件的压缩方法
        Path path = new Path(filename);
        CompressionCodec codec = factory.getCodec(path);

        // 2 判断该压缩方法是否存在
        if (null == codec) {
            System.out.println("Cannot find codec for file " + filename);
            return;
        }
        // 3 创建压缩文件的输入流
        FSDataInputStream open = fileSystem.open(path);

        int compressedSize = (int) fileSystem.getFileStatus(path).getLen();
        InputStream cin = codec.createInputStream(open, new SnappyDecompressor(compressedSize));


        SnappyDecompressor snappyDecompressor = new SnappyDecompressor();
//        byte[] buffer = new byte[1024];
//        int len;
//        while ((len = cin.read(buffer)) != -1) {
//            System.out.write(buffer, 0, len);
//        }

        // 4 创建解压缩文件的输出流
        File fout = new File("d:/1.txt");
        OutputStream out = new FileOutputStream(fout, true);

        // 5 流对接
        IOUtils.copyBytes(cin, out, 1024 * 1024 * 5, false);

        // 6 关闭资源
        open.close();
        out.close();
    }

    public static void decompres1(String filename, FileSystem fs) throws FileNotFoundException, IOException {
        // 创建 SnappyCodec 对象
        CompressionCodec codec = new SnappyCodec();

        // 读取压缩数据并创建输入流
        Path path = new Path("/path/to/file.snappy");
        FSDataInputStream inputStream = fs.open(path);

        // 获取压缩数据块的大小
        long compressedSize = fs.getFileStatus(path).getLen();

//        // 解压数据
//        CompressionInputStream in = codec.createInputStream(inputStream, compressedSize);
//        byte[] buffer = new byte[1024];
//        int len;
//        while ((len = in.read(buffer)) != -1) {
//            System.out.write(buffer, 0, len);
//        }
//        in.close();
//        inputStream.close();
    }

    public static void append(String hdfsPath) throws FileNotFoundException, IOException {
        Configuration conf = new Configuration();
        conf.setBoolean("dfs.support.append", true);

        String inpath = "d:/1.txt";
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(hdfsPath), conf);
            //要追加的文件流，inpath为文件
            InputStream in = new
                    BufferedInputStream(new FileInputStream(inpath));
            OutputStream out = fs.append(new Path(hdfsPath));
            IOUtils.copyBytes(in, out, 4096, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
