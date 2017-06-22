package com.verizon.vnf.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.io.Files;

public class CompareAndCopy {

	File original = new File("D:\\dir1");
	File last = new File("D:\\dir2");
	public static void main(String ...args) 
	{
		CompareAndCopy test = new CompareAndCopy();
		try
		{
			test.updatePackageFromLastVersion(test.original,test.last);
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	public void updatePackageFromLastVersion(File lastVersion, File newPackage) throws IOException
	{
		File[] fileList1 = lastVersion.listFiles();
		File[] fileList2 = newPackage.listFiles();
		Arrays.sort(fileList1);
		Arrays.sort(fileList2);
		HashMap<String, File> map1;
			map1 = new HashMap<String, File>();
			for(int i=0;i<fileList2.length;i++)
			{
				map1.put(fileList2[i].getName(),fileList2[i]);
			}
			compareAndCopy(fileList1, map1);
	}
	
	public void compareAndCopy(File[] fileArr, HashMap<String, File> map) throws IOException
	{
		for(int i=0;i<fileArr.length;i++)
		{
			String fName = fileArr[i].getName();
			File fComp = map.get(fName);
			map.remove(fName);
			if(fComp!=null)
			{
				if(fComp.isDirectory())
				{
					updatePackageFromLastVersion(fileArr[i], fComp);
				}
				else
				{
					String cSum1 = checksumValidation(fileArr[i]);
					String cSum2 = checksumValidation(fComp);
					if(!cSum1.equals(cSum2))
					{
						System.out.println(fileArr[i].getName()+"\t\t"+ "different");
					}
					else
					{
						System.out.println(fileArr[i].getName()+"\t\t"+"identical");
					}
				}
			}
			else
			{
				File tempFile = fileArr[i];
				if(fileArr[i].isDirectory())
				{
					System.out.println(tempFile.getAbsolutePath());
					traverseDirectories(fileArr[i]);
				}
				else
				{
					System.out.println(fileArr[i].getName()+"\t\t"+"only in "+fileArr[i].getParent());
					String originalFilePath = fileArr[i].getAbsolutePath().toString();
					String targetPath = originalFilePath.replace(original.getAbsolutePath(),last.getAbsolutePath()).replace(fileArr[i].getName(), "")+fileArr[i].getName();
					if(!originalFilePath.equals(targetPath))
					Files.copy(fileArr[i], new File(targetPath));
				}
			}
		}
	}
	
	public void traverseDirectories(File dir)
	{
		File[] list = dir.listFiles();
		for(int k=0;k<list.length;k++)
		{
			if(list[k].isDirectory())
			{
				traverseDirectories(list[k]);
			}
			else
			{
				System.out.println(list[k].getName() +"\t\t"+"only in "+ list[k].getParent());
			}
		}
	}
	
	public String checksumValidation(File file) 
	{
		try 
		{
		    InputStream fin = new FileInputStream(file);
		    java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
		    byte[] buffer = new byte[1024];
		    int read;
		    do 
		    {
		    	read = fin.read(buffer);
		    	if (read > 0)
		    		md5er.update(buffer, 0, read);
		    } while (read != -1);
		    fin.close();
		    byte[] digest = md5er.digest();
		    if (digest == null)
		      return null;
		    String strDigest = "0x";
		    for (int i = 0; i < digest.length; i++) 
		    {
		    	strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
		    }
		    return strDigest;
		} 
		catch (Exception e) 
		{
		    return null;
		}
	}


}
