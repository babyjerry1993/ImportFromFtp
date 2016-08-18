package com.shawn.ftpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {

	// 下载文件(单个指定文件)
	public boolean downloadSingleFromFtp(FtpInfo info) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// ftp.connect(url);
			ftp.connect(info.getUrl(), info.getPort());
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(info.getUsername(), info.getPassword());// 登录
			reply = ftp.getReplyCode();// 返回码
			if (!FTPReply.isPositiveCompletion(reply)) {// reply >= 200 && reply
														// < 300
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(info.getRemotePath());// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {// 遍历ftp远程路径中的文件,匹配文件名的根据localFile输出到本地路径
				if (ff.getName().equals(info.getFileName())) {

					File localFile = new File(info.getLocalPath() + "/" + ff.getName());

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

	// 下载文件(批量)
	public boolean downloadFolderFromFtp(FtpInfo info) {
		boolean success = false;
		FTPClient ftp = new FTPClient();

		// 设置中文编码----
		ftp.setControlEncoding("GBK");
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		conf.setServerLanguageCode("zh");
		ftp.configure(conf);
		// -------------
		try {
			int reply;
			ftp.connect(info.getUrl(), info.getPort());
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(info.getUsername(), info.getPassword());// 登录
			reply = ftp.getReplyCode();// 返回码
			if (!FTPReply.isPositiveCompletion(reply)) {// reply >= 200 && reply
														// < 300
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(info.getRemotePath());// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {// 将ftp远程路径中的文件全部输出到本地路径
				if (ff.isFile()) {
					File localFile = new File(info.getLocalPath() + "/" + ff.getName());
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
	public FileOutputStream getFileStream(FtpInfo info) throws IOException {

		FileOutputStream fos = null;
		FTPClient ftp = new FTPClient();
		ftp.connect(info.getUrl(), info.getPort());// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.login(info.getUsername(), info.getPassword());// 登录
		int reply = ftp.getReplyCode();// 返回码

		System.out.println(reply);// 打印返回码

		if (!FTPReply.isPositiveCompletion(reply)) {// 若失败
			ftp.disconnect();
			return null;// 返回空
		}
		// 若成功
		ftp.changeWorkingDirectory(info.getRemotePath());// 转移到FTP服务器目录
		FTPFile[] fs = ftp.listFiles();
		for (FTPFile ff : fs) {// 遍历ftp远程路径中的文件,匹配文件名的根据localFile输出到本地路径
			if (ff.getName().equals(info.getFileName())) {
				File localFile = new File(info.getLocalPath() + "/" + ff.getName());
				fos = new FileOutputStream(localFile);
			}
		}
		ftp.logout();// ftp退出登录
		if (ftp.isConnected()) {// 检查ftp连接状态，关闭
			ftp.disconnect();
		}
		return fos;
	}

}
