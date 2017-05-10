package com.ofz.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class ValidateZip {
	public ValidateZip() {
		try {
			ArrayList<String> set = new ArrayList<String>();
			set.add("ziptest/");
			//set.add("ziptest/test/test.txt");
			//set.add("ziptest/test/");
			set.add("alter_varchar_clob.sql");
			set.add("runtime_config.sql");
			String filename = "forZipTest";
			byte[] buf = new byte[1024];
			ZipInputStream zipinputstream = null;
			ZipEntry zipentry;
			zipinputstream = new ZipInputStream(new FileInputStream("C:\\Users\\v553175\\Downloads\\" + filename + ".zip"));
			ArrayList<String> zipNames = new ArrayList<String>();
			ArrayList<String> entryNames = new ArrayList<String>();
			while ((zipentry = zipinputstream.getNextEntry()) != null) {
				String entryName = zipentry.getName();
				if (entryName.equals(filename + "/"))
					continue;
				entryNames.add(entryName);
				String s0 = entryName.substring(entryName.indexOf("/") + 1);

				if (s0.indexOf("/") > -1) {
					s0 = s0.substring(0, s0.indexOf("/"));
					if (!zipNames.contains(s0))
						zipNames.add(s0);
				}
			}
			System.out.println("zipNames:" + zipNames);
			System.out.println("entryNames: " + entryNames);
			if (!validate(filename, set, zipNames, entryNames))
				System.out.println("Zip is bad");
			else
				System.out.println("Zip is good");
			zipinputstream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public boolean validate(String filename, ArrayList<String> set, ArrayList<String> zipNames,
			ArrayList<String> entryNames) {
		boolean b = true;
		for (String s : zipNames) {
			for (String s1 : set) {
				String s2 = s1.replaceAll("fileName", filename);
				String s3 = s2.replaceAll("zipName", s);
				System.out.println("s3: " + s3);
				if (!entryNames.contains(s3)) {
					System.out.println("file does not contain " + s3);
					return false;
				}
				entryNames.remove(s3);
			}
		}
		
		if (entryNames.size() > 0) {
			/*if (entryNames.size() != set.size()){
				System.out.println("Size is different");
				return false;
			}*/
			HashMap<String, String> map = new HashMap<String, String>();
		    for (String str : set) {
		        map.put(str, str);
		    }
		    for (String str : entryNames) {
		        if ( ! map.containsKey(str) ) {
		            return false;
		        }
		    }
		}
		return b;
	}
	public static void main(String[] args) {
		new ValidateZip();
	}
}
