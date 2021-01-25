package com.ot.springboot.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * 1.文件写入流程
 * a)客户端请求上传文件，hdfs的namenode获取到当前datanode的信息返回给客户端
 * b)客户端根据文件的大小，划分为块，然后根据datanode的地址信息，按顺序写入到每一个datanode当中
 * 2.文件读取
 */

public class FileSystemTest {


    /**
     * 为了使用自定义的Schema，需要设置URLStreamHandlerFactory，这个操作一个JVM只能进行一次，多次操作会导致不可用，通常在
     * 静态代码块当中完成
     */
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    public static void main(String[] args) {
        InputStream in = null;
        try {
            in = new URL(args[0]).openStream();
            IOUtils.copyBytes(in, System.out, 4096, false);
        } catch (Exception e) {
            IOUtils.closeStream(in);
        }
    }

    /**
     *
     *  public static FileSystem get(Configuration conf) throws IOException
     *  public static FileSystem get(URI uri , Configuration conf) throws IOException
     *  public static FileSystem get(URI uri , Configuration conf，String user) throws IOException
     *  1.如果是本地文件，使用
     *      public static LocalFileSystem getLocal(Configuration conf) throws IOException
     *  2.调用open（）获取输入流,默认是4096，4kb，可以自行设置
     *   public FSDataInputStream open(Path f) throws IOException
     *   public abstarct FSDataInputStream open(Path f , int bufferSize) throws IOException
     *  3.使用FSDataInputStream进行数据操作，FSDataInputStream是java.io.DataInputStream的特殊实现，在其基础上增加了随机读取、
     *  部分读取的能力
     */


    /**
     * 1.写文件
     * 在HDFS中，文件使用FileSystem类的create方法及其重载形式来创建，create方法返回一个输出流FSDataOutputStream，可以调用返回
     * 输出流的getPos方法查看当前文件的位移，但是不能进行seek操作，HDFS仅支持追加操作。
     * <p>
     * 创建时，可以传递一个回调接口Peofressable，获取进度信息
     * <p>
     * append(Path f)方法用于追加内容到已有文件，但是并不是所有的实现都提供该方法，例如Amazon的文件实现就没有提供追加功能。
     */

    public void write() throws IOException {
        String localSrc = "";
        String dst = "";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(localSrc));
        FileSystem fs = FileSystem.get(URI.create(dst), new Configuration());
        FSDataOutputStream fsDataOutputStream = fs.create(new Path(dst), new Progressable() {
            @Override
            public void progress() {

            }
        });
        IOUtils.copyBytes(bis, fsDataOutputStream, 4096, true);

    }
    /**
     * 目录操作
     * 使用mkdir（）会自动创建没有的上级目录
     * HDFS元数据封装在FileStatus当中，包括长度、block size，replications，修改时间、所有者、权限等信息。
     * 使用FileSystem提供的getFileStatus方法获取FileStatus。exists()方法判断文件或者目录是否存在；
     *
     * public abstract FileStatus[] listStatus(Path var1) throws FileNotFoundException, IOException;
     * 当path是文件的时候，返回长度为1的数组,FileUtil提供的stat2Paths方法用于将FileStatus转化为Path对象。
     *
     * globStatus则使用通配符对文件路径进行匹配：
     *
     * public FileStatus[] globStatus(Path pathPattern) throws IOException
     */

    /**
     * 删除数据
     *public boolean delete(Path f , boolean recursive) throws IOException;
     * recursive在f是文件的时候被忽略，如果是目录，并且recursive是true则删除整个目录，否则抛出异常
     */

    //===========================================================================================================
    /**
     * 读文件
     * 1）客户端传递一个文件Path给FileSystem的open方法
     *
     * 2）DFS采用RPC远程获取文件最开始的几个block的datanode地址。Namenode会根据网络拓扑结构决定返回哪些节点（前提是节点有block副本），
     * 如果客户端本身是Datanode并且节点上刚好有block副本，直接从本地读取。
     *
     * 3）客户端使用open方法返回的FSDataInputStream对象读取数据（调用read方法）
     *
     * 4）DFSInputStream（FSDataInputStream实现了改类）连接持有第一个block的、最近的节点，反复调用read方法读取数据
     *
     * 5）第一个block读取完毕之后，寻找下一个block的最佳datanode，读取数据。如果有必要，DFSInputStream会联系Namenode获取下一批Block
     * 的节点信息(存放于内存，不持久化），这些寻址过程对客户端都是不可见的。
     *
     * 6）数据读取完毕，客户端调用close方法关闭流对象
     *
     * 在读数据过程中，如果与Datanode的通信发生错误，DFSInputStream对象会尝试从下一个最佳节点读取数据，并且记住该失败节点， 后续Block的
     * 读取不会再连接该节点
     * 读取一个Block之后，DFSInputStram会进行检验和验证，如果Block损坏，尝试从其他节点读取数据，并且将损坏的block汇报给Namenode。
     * 客户端连接哪个datanode获取数据，是由namenode来指导的，这样可以支持大量并发的客户端请求，namenode尽可能将流量均匀分布到整个集群。
     * Block的位置信息是存储在namenode的内存中，因此相应位置请求非常高效，不会成为瓶颈。
     */

    /**
     * 写文件
     * 步骤分解
     * 1）客户端调用DistributedFileSystem的create方法
     *
     * 2）DistributedFileSystem远程RPC调用Namenode在文件系统的命名空间中创建一个新文件，此时该文件没有关联到任何block。
     * 这个过程中，Namenode会做很多校验工作，例如是否已经存在同名文件，是否有权限，如果验证通过，返回一个FSDataOutputStream对象。
     * 如果验证不通过，抛出异常到客户端。
     *
     * 3）客户端写入数据的时候，DFSOutputStream分解为packets（数据包），并写入到一个数据队列中，该队列由DataStreamer消费。
     *
     * 4）DateStreamer负责请求Namenode分配新的block存放的数据节点。这些节点存放同一个Block的副本，构成一个管道。
     * DataStreamer将packet写入到管道的第一个节点，第一个节点存放好packet之后，转发给下一个节点，下一个节点存放 之后继续往下传递。
     *
     * 5）DFSOutputStream同时维护一个ack queue队列，等待来自datanode确认消息。当管道上的所有datanode都确认之后，packet从ack队列中移除。
     *
     * 6）数据写入完毕，客户端close输出流。将所有的packet刷新到管道中，然后安心等待来自datanode的确认消息。全部得到确认之后告知Namenode
     * 文件是完整的。 Namenode此时已经知道文件的所有Block信息（因为DataStreamer是请求Namenode分配block的），只需等待达到最小副本数要求，
     * 然后返回成功信息给客户端。
     */


}
