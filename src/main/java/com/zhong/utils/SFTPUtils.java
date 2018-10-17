package com.zhong.utils;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

/**
 * @author 张中俊
 **/
public class SFTPUtils {

    private static Session sshSession;
    private static ChannelSftp sftp;

    static {
        File f = MyUtils.getFile("src/main/webapp/WEB-INF", "config.properties");
        String path = f.getAbsolutePath();
        Properties properties = MyUtils.getUpConfig(path);

        String username = properties.getProperty("sftp.username");
        String host = properties.getProperty("sftp.host");
        int port = Integer.parseInt(properties.getProperty("sftp.port"));
        String password = properties.getProperty("sftp.password");

        JSch jsch = new JSch();
        try {
            sshSession = jsch.getSession(username, host, port);

            System.out.println("Session created.");

            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();

            System.out.println("Session connected.");
            System.out.println("Opening Channel.");

            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;

            System.out.println("Connected to " + host + ".");
        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    public static void portForwardingL(int lport, String rhost, int rport) throws Exception {
        int assinged_port = sshSession.setPortForwardingL(lport, rhost, rport);
        System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
    }

    /**
     * 断开连接
     */
    public static void disconnect() {
        if (sftp != null) sftp.disconnect();
        if (sshSession != null) sshSession.disconnect();
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件的路径
     */
    public static void upload(String directory, String uploadFile) throws Exception {
        if (!exist(directory)) {
            sftp.mkdir(directory);
        }
        sftp.cd(directory);
        File file = new File(uploadFile);
        sftp.put(new FileInputStream(file), file.getName());
    }

    /**
     * 上传文件
     *
     * @param directory 上传的目录
     * @param file      要上传的文件的
     */
    public static void upload(String directory, File file) throws Exception {
        sftp.cd(directory);
        sftp.put(new FileInputStream(file), file.getName());
        System.out.println("upload file " + file.getAbsolutePath() + " to host " + sshSession.getHost());
    }

    /**
     * 将本地的一个目录src 原封不动地拷贝到sftp服务器中
     *
     * @param src 本地目录
     * @param dst 服务器上的目录
     * @throws Exception 异常
     */
    public static void uploadDir(File src, String dst) throws Exception {
        if (!exist(dst)) {
            sftp.mkdir(dst);
        }
        if (src.isFile()) {
            upload(dst, src);
        } else {
            for (File file : src.listFiles()) {
                if (file.isDirectory()) {
                    uploadDir(file, dst + "/" + file.getName());
                }
                upload(dst, file);
            }
        }
    }

    /**
     * 目录是否存在
     *
     * @param path 要检测的路径
     * @return 如果存在 返回true；否则返回false
     * @throws SftpException 异常
     */
    public static boolean exist(String path) throws SftpException {
        String pwd = sftp.pwd();
        try {
            sftp.cd(path);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            } else {
                throw e;
            }
        } finally {
            sftp.cd(pwd);
        }
        return true;
    }

    /**
     * 下载文件
     *
     * @param directory    要下载的文件所在的目录
     * @param downloadFile 要下载的文件的文件名
     * @param saveFile     要下载到哪里
     * @throws Exception 异常
     */
    public static void download(String directory, String downloadFile, String saveFile) throws Exception {
        sftp.cd(directory);
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     *
     * @param directory    要下载的文件所在的目录
     * @param downloadFile 要下载的文件的文件名
     * @param saveFile     要下载到哪里
     * @throws Exception 异常
     */
    public static void download(String directory, String downloadFile, File saveFile) throws Exception {
        sftp.cd(directory);
        sftp.get(downloadFile, new FileOutputStream(saveFile));
        System.out.println("download file " + directory + "/" + downloadFile + " from host " + sshSession.getHost());
    }

    /**
     * 将服务器上的整个目录原封不动地下载到本地
     *
     * @param src 服务器上的目录
     * @param dst 本地的目录
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public static void downloadDir(String src, File dst) throws Exception {
        try {
            sftp.cd(src);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dst.mkdirs();

        Vector<ChannelSftp.LsEntry> files = sftp.ls(src);
        for (ChannelSftp.LsEntry lsEntry : files) {
            if (lsEntry.getFilename().equals(".") || lsEntry.getFilename().equals("..")) {
                continue;
            }
            if (lsEntry.getLongname().startsWith("d")) {
                downloadDir(src + "/" + lsEntry.getFilename(), new File(dst, lsEntry.getFilename()));
            } else {
                download(src, lsEntry.getFilename(), new File(dst, lsEntry.getFilename()));
            }
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除的文件所在的目录
     * @param deleteFile 要删除的文件的文件名
     * @throws SftpException 异常
     */
    public static void delete(String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 目录
     * @return 该目录下的所有文件
     * @throws SftpException 异常
     */
    public static Vector listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 使用密码连接sftp服务器
     *
     * @param host     服务器名
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return sftp的连接
     * @throws Exception 异常
     */
    public ChannelSftp connect(String host, int port, String username, String password) throws Exception {

        JSch jsch = new JSch();
        sshSession = jsch.getSession(username, host, port);

        System.out.println("Session created.");

        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();

        System.out.println("Session connected.");
        System.out.println("Opening Channel.");

        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;

        System.out.println("Connected to " + host + ".");

        return sftp;
    }

    /**
     * 使用密钥连接sftp服务器
     *
     * @param host       服务器名
     * @param port       端口号
     * @param username   用户名
     * @param privateKey 密钥
     * @param passphrase 密钥的密码
     * @return sftp连接
     * @throws Exception 异常
     */
    public ChannelSftp connect(String host, int port, String username, String privateKey, String passphrase) throws Exception {
        JSch jsch = new JSch();

        //设置密钥和密码
        if (!privateKey.isEmpty()) {
            if (!passphrase.isEmpty()) {
                //设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }
        sshSession = jsch.getSession(username, host, port);

        System.out.println("Session created.");

        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();

        System.out.println("Session connected.");
        System.out.println("Opening Channel.");

        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;

        System.out.println("Connected to " + host + ".");

        return sftp;
    }

    public void myMkDir(String path) {

    }
}
