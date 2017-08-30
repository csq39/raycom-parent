/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.common.utils.file;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.raycom.common.utils.string.StringUtils;
import io.raycom.system.framework.collection.RData;

/**
 * 文件操作工具类
 * 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 * @author caoshengquan
 * @version 2013-06-21
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
	
	private static Logger log = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 复制单个文件，如果目标文件存在，则不覆盖
	 * @param srcFileName 待复制的文件名
	 * @param descFileName 目标文件名
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return FileUtils.copyFileCover(srcFileName, descFileName, false);
	}

	/**
	 * 复制单个文件
	 * @param srcFileName 待复制的文件名
	 * @param descFileName 目标文件名
	 * @param coverlay 如果目标文件已存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName,
			String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				log.debug("目标文件已存在，准备删除!");
				if (!FileUtils.delFile(descFileName)) {
					log.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				log.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					log.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			log.debug("复制单个文件 " + srcFileName + " 到" + descFileName
					+ "成功!");
			return true;
		} catch (Exception e) {
			log.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制整个目录的内容，如果目标目录存在，则不覆盖
	 * @param srcDirName 源目录名
	 * @param descDirName 目标目录名
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String descDirName) {
		return FileUtils.copyDirectoryCover(srcDirName, descDirName,
				false);
	}

	/**
	 * 复制整个目录的内容 
	 * @param srcDirName 源目录名
	 * @param descDirName 目标目录名
	 * @param coverlay 如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectoryCover(String srcDirName,
			String descDirName, boolean coverlay) {
		File srcDir = new File(srcDirName);
		// 判断源目录是否存在
		if (!srcDir.exists()) {
			log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
			return false;
		}
		// 判断源目录是否是目录
		else if (!srcDir.isDirectory()) {
			log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
			return false;
		}
		// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		// 如果目标文件夹存在
		if (descDir.exists()) {
			if (coverlay) {
				// 允许覆盖目标目录
				log.debug("目标目录已存在，准备删除!");
				if (!FileUtils.delFile(descDirNames)) {
					log.debug("删除目录 " + descDirNames + " 失败!");
					return false;
				}
			} else {
				log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
				return false;
			}
		} else {
			// 创建目标目录
			log.debug("目标目录不存在，准备创建!");
			if (!descDir.mkdirs()) {
				log.debug("创建目标目录失败!");
				return false;
			}

		}

		boolean flag = true;
		// 列出源目录下的所有文件名和子目录名
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 如果是一个单个文件，则直接复制
			if (files[i].isFile()) {
				flag = FileUtils.copyFile(files[i].getAbsolutePath(),
						descDirName + files[i].getName());
				// 如果拷贝文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 如果是子目录，则继续复制目录
			if (files[i].isDirectory()) {
				flag = FileUtils.copyDirectory(files[i]
						.getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
			return false;
		}
		log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
		return true;

	}

	/**
	 * 
	 * 删除文件，可以删除单个文件或文件夹
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
 		File file = new File(fileName);
		if (!file.exists()) {
			log.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return FileUtils.deleteFile(fileName);
			} else {
				return FileUtils.deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 
	 * 删除单个文件
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.debug("删除文件 " + fileName + " 成功!");
				return true;
			} else {
				log.debug("删除文件 " + fileName + " 失败!");
				return false;
			}
		} else {
			log.debug(fileName + " 文件不存在!");
			return true;
		}
	}

	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName 被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			log.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtils.deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtils.deleteDirectory(files[i]
						.getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			log.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			log.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

	/**
	 * 创建单个文件
	 * @param descFileName 文件名，包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			log.debug("文件 " + descFileName + " 已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			log.debug(descFileName + " 为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				log.debug("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				log.debug(descFileName + " 文件创建成功!");
				return true;
			} else {
				log.debug(descFileName + " 文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(descFileName + " 文件创建失败!");
			return false;
		}

	}

	/**
	 * 创建目录
	 * @param descDirName 目录名,包含路径
	 * @return 如果创建成功，则返回创建的路径，否则返回""
	 */
	public static boolean createDirectory(String descDirName) {
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		if (descDir.exists()) {
			log.debug("目录 " + descDirNames + " 已存在!");
			return true;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			log.debug("目录 " + descDirNames + " 创建成功!");
			return true;
		} else {
			log.debug("目录 " + descDirNames + " 创建失败!");
			return false;
		}

	}
	
	public static String getDayDirectory(String descDirName) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//yyyyMMddHHmmss
		return df.format(new Date());
	}

	/**
	 * 写入文件
	 * @param file 要写入的文件
	 */
	public static void writeToFile(String fileName, String content, boolean append) {
		try {
			FileUtils.write(new File(fileName), content, "utf-8", append);
			log.debug("文件 " + fileName + " 写入成功!");
		} catch (IOException e) {
			log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
		}
	}

	/**
	 * 写入文件
	 * @param file 要写入的文件
	 */
	public static void writeToFile(String fileName, String content, String encoding, boolean append) {
		try {
			FileUtils.write(new File(fileName), content, encoding, append);
			log.debug("文件 " + fileName + " 写入成功!");
		} catch (IOException e) {
			log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
		}
	}
	


	/**
	 * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
	 * @param dirPat 目录名
	 * @param file entry文件名
	 * @return
	 */
	private static String getEntryName(String dirPath, File file) {
		String dirPaths = dirPath;
		if (!dirPaths.endsWith(File.separator)) {
			dirPaths = dirPaths + File.separator;
		}
		String filePath = file.getAbsolutePath();
		// 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
		if (file.isDirectory()) {
			filePath += "/";
		}
		int index = filePath.indexOf(dirPaths);

		return filePath.substring(index + dirPaths.length());
	}
	
	/**
	 * 
	 * @param rdata
	 * 			fileName:对应zip文件名
	 *         	fileList：list-->rdata对应附件地址
	 *                            dirPath：对应附件在zip内的路径
	 *                            file： 文件全路径，如果是目录，以"/"结尾
	 * @return
	 * @throws IOException 
	 */
	public static byte[] zipFilesToByteArray(RData rdata) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(baos);
		zipOut.setEncoding(System.getProperty("sun.jnu.encoding"));
		List<RData> fileList  = (List<RData>)rdata.get("fileList");
		for (RData file : fileList) {
			String fileDir = file.getString("file");
			if(fileDir.endsWith("/"))
				zipDirectoryToZipFileByTag(file.getString("dirPath"),zipOut);
			else
				zipFilesToZipFileByTag(file.getString("dirPath"),new File(fileDir),zipOut);
		}
		try {
			zipOut.finish();
			return baos.toByteArray(); 
		}finally {
			IOUtils.closeQuietly(zipOut);
			IOUtils.closeQuietly(baos);
		}
        
		
	}
	
	/**
	 * 将目录压缩到ZIP输出流
	 * @param dirPath 目录路径
	 * @param fileDir 文件信息
	 * @param zouts 输出流
	 */
	public static void zipDirectoryToZipFileByTag(String dirPath, ZipOutputStream zouts) {
		ZipEntry entry = new ZipEntry(dirPath);
		try {
			zouts.putNextEntry(entry);
			zouts.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 将文件压缩到ZIP输出流
	 * @param dirPath 目录路径
	 * @param file 文件
	 * @param zouts 输出流
	 */
	public static void zipFilesToZipFileByTag(String dirPath, File file, ZipOutputStream zouts) {
		FileInputStream fin = null;
		ZipEntry entry = null;
		// 创建复制缓冲区
		byte[] buf = new byte[4096];
		int readByte = 0;
		if (file.isFile()) {
			try {
				// 创建一个文件输入流
				fin = new FileInputStream(file);
				// 创建一个ZipEntry
				entry = new ZipEntry(dirPath);
				System.out.println("添加文件 " + dirPath);
				// 存储信息到压缩文件
				zouts.putNextEntry(entry);
				// 复制字节到压缩文件
				while ((readByte = fin.read(buf)) != -1) {
					System.out.println("添加文件 " + readByte);
					zouts.write(buf, 0, readByte);
				}
				zouts.closeEntry();
				fin.close();
				System.out
						.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	public static void zipFiles(String srcDirName, String fileName,
			String descFileName) {
		// 判断目录是否存在
		if (srcDirName == null) {
			return;
		}
		File fileDir = new File(srcDirName);
		if (!fileDir.exists() || !fileDir.isDirectory()) {
			return;
		}
		String dirPath = fileDir.getAbsolutePath();
		File descFile = new File(descFileName);
		try {
			ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(
					descFile));
			if ("*".equals(fileName) || "".equals(fileName)) {
				FileUtils.zipDirectoryToZipFile(dirPath, fileDir, zouts);
			} else {
				File file = new File(fileDir, fileName);
				if (file.isFile()) {
					FileUtils.zipFilesToZipFile(dirPath, file, zouts);
				} else {
					FileUtils
							.zipDirectoryToZipFile(dirPath, file, zouts);
				}
			}
			zouts.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 将目录压缩到ZIP输出流
	 * @param dirPath 目录路径
	 * @param fileDir 文件信息
	 * @param zouts 输出流
	 */
	public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			// 空的文件夹
			if (files.length == 0) {
				// 目录信息
				ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
				try {
					zouts.putNextEntry(entry);
					zouts.closeEntry();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}

			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					// 如果是文件，则调用文件压缩方法
					FileUtils
							.zipFilesToZipFile(dirPath, files[i], zouts);
				} else {
					// 如果是目录，则递归调用
					FileUtils.zipDirectoryToZipFile(dirPath, files[i],
							zouts);
				}
			}
		}
	}

	/**
	 * 将文件压缩到ZIP输出流
	 * @param dirPath 目录路径
	 * @param file 文件
	 * @param zouts 输出流
	 */
	public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
		FileInputStream fin = null;
		ZipEntry entry = null;
		// 创建复制缓冲区
		byte[] buf = new byte[4096];
		int readByte = 0;
		if (file.isFile()) {
			try {
				// 创建一个文件输入流
				fin = new FileInputStream(file);
				// 创建一个ZipEntry
				entry = new ZipEntry(getEntryName(dirPath, file));
				// 存储信息到压缩文件
				zouts.putNextEntry(entry);
				// 复制字节到压缩文件
				while ((readByte = fin.read(buf)) != -1) {
					zouts.write(buf, 0, readByte);
				}
				zouts.closeEntry();
				fin.close();
				System.out
						.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	
	/**
	 * 修复路径，将 \\ 或 / 等替换为 File.separator
	 * @param path
	 * @return
	 */
	public static String path(String path){
		String p = StringUtils.replace(path, "\\", File.separator);
		p=	StringUtils.replace(p, "/", File.separator);
		p = StringUtils.join(StringUtils.split(p, File.separator), File.separator);
		return p;
	}
	
	public static String getFileName(String path) {
		path = path.replace("\\", "/");
		return path.substring(path.lastIndexOf("/") + 1);
	}
	
	public static String getFilePath() {
		String path = getAppPath(FileUtil.class);
		path = path + File.separator + "ftl" + File.separator;
		path = path.replace("\\", "/");
		return path;
	}
	
	public static String getFtlPath() {
		String path = getAppPath(FileUtil.class);
		String webInfPath = "/WEB-INF/lib";
		if(path.endsWith(webInfPath))
			path = path.replace(webInfPath, "");
			path = path + File.separator + "static/ftl" + File.separator;
		path = path.replace("\\", "/");
		return path;
	}
	
	public static String getAppPath(Class<?> cls) {
		// 检查用户传入的参数是否为空
		if (cls == null)
			throw new java.lang.IllegalArgumentException("参数不能为空！");
		ClassLoader loader = cls.getClassLoader();
		// 获得类的全名，包括包名
		String clsName = cls.getName() + ".class";
		// 获得传入参数所在的包
		Package pack = cls.getPackage();
		String path = "";
		// 如果不是匿名包，将包名转化为路径
		if (pack != null) {
			String packName = pack.getName();
			// 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
			if (packName.startsWith("java.") || packName.startsWith("javax."))
				throw new java.lang.IllegalArgumentException("不要传送系统类！");
			// 在类的名称中，去掉包名的部分，获得类的文件名
			clsName = clsName.substring(packName.length() + 1);
			// 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
			if (packName.indexOf(".") < 0)
				path = packName + "/";
			else {// 否则按照包名的组成部分，将包名转换为路径
				int start = 0, end = 0;
				end = packName.indexOf(".");
				while (end != -1) {
					path = path + packName.substring(start, end) + "/";
					start = end + 1;
					end = packName.indexOf(".", start);
				}
				path = path + packName.substring(start) + "/";
			}
		}
		// 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
		java.net.URL url = loader.getResource(path + clsName);
		// 从URL对象中获取路径信息
		String realPath = url.getPath();
		// 去掉路径信息中的协议名"file:"
		int pos = realPath.indexOf("file:");
		if (pos > -1)
			realPath = realPath.substring(pos + 5);
		// 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
		pos = realPath.indexOf(path + clsName);
		realPath = realPath.substring(0, pos - 1);
		// 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
		if (realPath.endsWith("!"))
			realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		/*------------------------------------------------------------ 
		 ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径 
		  中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要 
		  的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的 
		  中文及空格路径 
		-------------------------------------------------------------*/
		try {
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("realPath----->" + realPath);
		return realPath;
	}
	
}
