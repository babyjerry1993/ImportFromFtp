package com.shawn.ftpUtil;

import org.junit.Test;

public class FtpUtilTest {

	//测试下载ftp文件到本地c盘
	@Test
	public void testDownFile() {

		FtpUtil ftpUtil = new FtpUtil();

		String url = "169.254.90.39";
		int port = 21;
		String username = "shawn";
		String password = "shawn";
		String remotePath = "/py";
		String fileName = "123.txt";
		String localPath = "C:/test";

		ftpUtil.downFile(url, port, username, password, remotePath, fileName, localPath);

	}
}
