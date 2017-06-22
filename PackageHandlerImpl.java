/**
 * 
 */
package com.verizon.vnf.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author root
 *
 */
@Service
public class PackageHandlerImpl {

	@Autowired
	WorkflowHandlerImpl workflowHandler;
	
	/*public String validatePackage() {
		
		Path p4 = Paths.get("/home/invlab09/testupload/Package");
		p4.getFileName();
		
		Path file = new File("/home/invlab09/testupload/Package").toPath();
		boolean exists = Files.exists(file);        // Check if the file exists
		boolean isDirectory = Files.isDirectory(file);   // Check if it's a directory
		boolean isFile = Files.isRegularFile(file);
		return null;
	}*/
	
	
	public String uploadFile(File f, String checksumValue, String vnfId) throws IOException{
		String responseString = "";
		System.out.println("starting upload --------------------------");
		try {

			//Create checksum file
			String fileExtn = f.getName().substring(f.getName().lastIndexOf("."));
			String filename = f.getName().replace(fileExtn, "");
			//			String[] fileExtn = f.getName().split("\\.");
			//			String packageName = fileExtn[0]+vnfId+"."+fileExtn[1];
			//			String checksumFileName = fileExtn[0]+vnfId+".txt";
			String packageName = filename+vnfId+fileExtn;
			String checksumFileName = filename+vnfId+".txt";

			System.out.println(checksumFileName);
			File checksumFile = new File(checksumFileName);
			FileOutputStream is = new FileOutputStream(checksumFile);
			OutputStreamWriter osw = new OutputStreamWriter(is);    
			Writer w = new BufferedWriter(osw);
			w.write(checksumValue);
			w.close();

			boolean cfilecopyStatus = upload("10.75.14.22", "sftpuser", "Verizon1", checksumFile.getAbsolutePath(), "/home/sftpuser/VNF_Package_Repository/"+checksumFileName);
			//			boolean cfilecopyStatus = upload("10.76.110.110", "invlab09", "sdnnfv@123", checksumFile.getAbsolutePath(), "/home/invlab09/testupload/"+checksumFileName);
			System.out.println("filecopyStatus : " + cfilecopyStatus);

			boolean iscFileExists = exist("10.75.14.22", "sftpuser", "Verizon1", "/home/sftpuser/VNF_Package_Repository/"+checksumFileName);
			//			boolean iscFileExists = exist("10.76.110.110", "invlab09", "sdnnfv@123", "/home/invlab09/testupload/"+checksumFileName);
			System.out.println("isFileExists : " + iscFileExists);


			//Upload file
			System.out.println("filepath : "+f.getAbsolutePath());

			boolean filecopyStatus = upload("10.75.14.22", "sftpuser", "Verizon1", f.getAbsolutePath(), "/home/sftpuser/VNF_Package_Repository/"+packageName);
			//			boolean filecopyStatus = upload("10.76.110.110", "invlab09", "sdnnfv@123", f.getAbsolutePath(), "/home/invlab09/testupload/"+packageName);
			System.out.println("filecopyStatus : " + filecopyStatus);

			boolean isFileExists = exist("10.75.14.22", "sftpuser", "Verizon1", "/home/sftpuser/VNF_Package_Repository/"+packageName);
			//			boolean isFileExists = exist("10.76.110.110", "invlab09", "sdnnfv@123", "/home/invlab09/testupload/"+packageName);
			System.out.println("isFileExists : " + isFileExists);

			//Trigger Jenkins job
			workflowHandler.triggerJenkinsValidation(vnfId,packageName);

		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}

	public static boolean upload(String hostName, String username, String password, String localFilePath,
			String remoteFilePath) {
		boolean isFileCopySuccessful = false;
		File file = new File(localFilePath);
		if (!file.exists())
			throw new RuntimeException("Local file not found");

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			manager.init();

			FileObject localFile = manager.resolveFile(file.getAbsolutePath());
			FileObject remoteFile = manager.resolveFile(getURIString(hostName, username, password, remoteFilePath), createDefaultOptions());

			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
			isFileCopySuccessful = true;
			localFile.delete();
			System.out.println("File upload success");
		} catch (Exception e) {
			isFileCopySuccessful = false;
			throw new RuntimeException(e);
		} finally {
			manager.close();
		}
		return isFileCopySuccessful;
	}


	public static boolean exist(String hostName, String username, String password, String remoteFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			manager.init();
			FileObject remoteFile = manager.resolveFile(getURIString(hostName, username, password, remoteFilePath), createDefaultOptions());
			System.out.println("File exist: " + remoteFile.exists());
			return remoteFile.exists();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			manager.close();
		}
	}

	public static String getURIString(String hostName, String username, String password,
			String remoteFilePath) throws Exception {
		URI uri = null;
		try {
			uri = new URI("sftp", username + ":" + password, hostName, -1, remoteFilePath, null, null);
		} catch (URISyntaxException urise) {
			System.out.println("Exception while constructing URI : " + urise.getMessage());
			throw new Exception(urise);
		}
		return uri.toString();
	}


	public static FileSystemOptions createDefaultOptions() throws FileSystemException {
		FileSystemOptions opts = new FileSystemOptions();
		try {
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");

			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
			// Timeout is count by Milliseconds
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 300000);
		} catch (org.apache.commons.vfs2.FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opts;
	}


	
}
