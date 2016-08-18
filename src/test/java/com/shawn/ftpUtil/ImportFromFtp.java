package com.shawn.ftpUtil;

import org.junit.Test;

import com.shawn.controller.HttpFileImporterExecutor;
import com.shawn.ftpUtil.FtpUtil;

//定义一个类继承HttpFileImporterExecutor，使得该类有了一切import的方法和权限
public class ImportFromFtp extends HttpFileImporterExecutor {

	@Test
	public void importFromFtp() {

		FtpUtil ftpUtil = new FtpUtil();
		FtpInfo info = new FtpInfo("ftp://169.254.90.39", 21, "shawn", "shawn", "/py", "123.txt", "c:\test");

		ftpUtil.downloadSingleFromFtp(info);

		String destinationPath = info.getLocalPath();
		String sourcePath = "/";
		boolean skipRootContainerCreation = false;
		int batchSize = 5;
		int noImportingThreads = 5;
		boolean interactive = false;

		getImporterService().importDocuments(this, destinationPath, sourcePath, skipRootContainerCreation, batchSize,
				noImportingThreads, interactive);

	}

}
