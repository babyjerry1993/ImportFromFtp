package com.shawn.ftpUtil;

import java.io.File;
import org.junit.Test;

public class FtpUtilTest {

	// 测试文件下载
	@Test
	public void downloadSingleFromFtp() {

		FtpUtil fu = new FtpUtil();
		FtpInfo info = new FtpInfo("169.254.90.39", 21, "shawn", "shawn", "/py", "123.txt", "C:\\test");

		fu.downloadSingleFromFtp(info);
	}

	// 测试批量下载
	@Test
	public void downloadFolderFromFtp() {

		//创建一个文件夹-----------------------
        String sourcePath = "C:\\temp";
		File myFilePath = new File(sourcePath);
		if (!myFilePath.exists()) {
			myFilePath.mkdir();
		}
		//Ftp批量下载文件到sourcePath
		FtpUtil fu = new FtpUtil();
		FtpInfo info = new FtpInfo("169.254.90.39", 21, "shawn", "shawn", "/py", null, sourcePath);

		fu.downloadFolderFromFtp(info);
	}
}
