package com.shawn.ftpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	/**
	 * Description: 从FTP服务器下载文件 @Version1.0 Jul 27, 2008 5:32:36 PM by
	 * 崔红保（cuihongbao@d-heaven.com）创建
	 *
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */

	// 下载文件(直接获取文件保存到本地)
	public boolean downFile(String url, int port, String username, String password, String remotePath, String fileName,
			String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// ftp.connect(url);
			ftp.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();

			System.out.println(reply);

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {// 遍历ftp远程路径中的文件,匹配文件名的根据localFile输出到本地路径
				if (ff.getName().equals(fileName)) {

					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);// 将获取到的流存盘到本地，完成到下载的操作
					is.close();
				}
			}
			ftp.logout();// ftp退出登录
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();// 操作完成了关闭ftp
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	// 下载ftp文件,获取流，用于上传到nuxeo
	public FileOutputStream getFileStream(String url, int port, String username, String password, String remotePath,
			String fileName, String localPath) throws IOException {

		FileOutputStream fos = null;
		FTPClient ftp = new FTPClient();
		ftp.connect(url, port);// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.login(username, password);// 登录
		int reply = ftp.getReplyCode();// 返回码

		System.out.println(reply);// 打印返回码

		if (!FTPReply.isPositiveCompletion(reply)) {// 若失败
			ftp.disconnect();
			return null;// 返回空
		}
		// 若成功
		ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
		FTPFile[] fs = ftp.listFiles();
		for (FTPFile ff : fs) {// 遍历ftp远程路径中的文件,匹配文件名的根据localFile输出到本地路径
			if (ff.getName().equals(fileName)) {
				File localFile = new File(localPath + "/" + ff.getName());
				fos = new FileOutputStream(localFile);
			}
		}
		ftp.logout();// ftp退出登录
		if (ftp.isConnected()) {//检查ftp连接状态，关闭
			ftp.disconnect();
		}
		return fos;
	}

	//上传到nuxeo
	public boolean uploadToNuxeo(){
		return false;
	}

}
