package com.shawn.ftpUtil;

public class FtpInfo {

	/**
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
	 */

	private String url;
	private int port;
	private String username;
	private String password;
	private String remotePath;
	private String fileName;
	private String localPath;

	/*
	 * 构造方法 带参数和不带参 created by shawn
	 */
	public FtpInfo() {
	}

	public FtpInfo(String url, int port, String username, String password, String remotePath, String fileName,
			String localPath) {

		this.url = url;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remotePath = remotePath;
		this.fileName = fileName;
		this.localPath = localPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

}
