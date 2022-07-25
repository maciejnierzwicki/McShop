package pl.maciejnierzwicki.mcshop.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JarUtils {
	
	  /**
	   * Returns the jar file used to load class clazz, or defaultJar if clazz was not loaded from a
	   * jar.
	   */
	  public static JarFile jarForClass(Class<?> clazz, JarFile defaultJar) {
	    String path = "/" + clazz.getName().replace('.', '/') + ".class";
	    URL jarUrl = clazz.getResource(path);
	    if (jarUrl == null) {
	      return defaultJar;
	    }

	    String url = jarUrl.toString();
	    int bang = url.indexOf("!");
	    String JAR_URI_PREFIX = "jar:file:";
	    if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
	      try {
	        return new JarFile(url.substring(JAR_URI_PREFIX.length(), bang));
	      } catch (IOException e) {
	        throw new IllegalStateException("Error loading jar file.", e);
	      }
	    } else {
	      return defaultJar;
	    }
	  }

	  /**
	   * Copies a directory from a jar file to an external directory.
	   */
	  public static void copyResourcesToDirectory(JarFile sourceJar, String jarDir, String destDir)
	      throws IOException {
		  log.debug("fromJar: " + sourceJar.getName());
		  String prefix = jarDir + "/";
	    for (Enumeration<JarEntry> entries = sourceJar.entries(); entries.hasMoreElements();) {
	      JarEntry entry = entries.nextElement();
	      prepareDestinationDirAndCopyEntryContentIfEntryNameStartsWithPrefix(sourceJar, entry, prefix, jarDir, destDir);
	    }
	    
	  }
	  
	  private static void prepareDestinationDirAndCopyEntryContentIfEntryNameStartsWithPrefix(JarFile sourceJar, JarEntry entry, String prefix, String jarDir, String destDirPath) throws FileNotFoundException, IOException {
	      if (entry.getName().startsWith(prefix) && !entry.isDirectory()) {
	    	  log.debug("MATCH: " + entry.getName());
	    	  String currentPath = new File(".").getAbsolutePath();
	    	  String dirPath = currentPath + destDirPath + "/" + entry.getName().substring(jarDir.length() + 1);
	    	  createDirIfNotExists(dirPath);
	    	  File destDir = new File(dirPath);
	    	  copyEntryContentFromSourceJarToDestinationDir(sourceJar, entry, destDir);
	      }
	  }
	  
	  private static void copyEntryContentFromSourceJarToDestinationDir(JarFile sourceJar, JarEntry entry, File destDir) throws FileNotFoundException, IOException {
	        
		  try(FileOutputStream out = new FileOutputStream(destDir)) {
		        InputStream in = sourceJar.getInputStream(entry);
		        try {
		          byte[] buffer = new byte[8 * 1024];
		          int s = 0;
		          while ((s = in.read(buffer)) > 0) {
		            out.write(buffer, 0, s);
		          }
		        } catch (IOException e) {
		          throw new IOException("Could not copy asset from jar file", e);
		        } finally {
		          try {
		            in.close();
		          } catch (IOException ignored) {}
		          try {
		            out.close();
		          } catch (IOException ignored) {}
		        }
	        }
	  }
	  
	  private static void createDirIfNotExists(String path) {
	        File dest = new File(path);
	        String fullPath = new File(path).getAbsolutePath();
	        File f = new File(fullPath);
	        if(f.exists()) { return; }
	        File parent = dest.getParentFile();
	        if(parent != null) {
	        	parent.mkdirs();
	        }
	  }
}
