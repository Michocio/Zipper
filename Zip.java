/**
 * Klasa obsługuje funkcje tworzące archiwum .zip
 * Osobne klasy dla folderów i pojedyńczych plików
 */


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Zip {

private void recursiveAdd(ZipOutputStream zos,String name)
{
	FileInputStream in = null; byte[] buffer = new byte[1024];
	try {
		in = new FileInputStream(name);
	}catch(IOException ex){
		System.out.print("\nNastępił błąd IO. Błąd zapisu archiwum\n");
 	   //ex.printStackTrace();
 	}
	
	int len;
	///Twórz archiwum
	try {
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}
	} catch(SecurityException s)
	{
		System.out.print("\nProgram próbował utworzyć archiwum z pliku do którego nie ma uprawnień\n");
	}
	catch(IOException ex){
		System.out.print("\nNastępił błąd IO. Błąd zapisu archiwum\n");
	   //ex.printStackTrace();
	}
	///Zamykaj strumienie
	try {
		in.close();
	} catch(IOException ex){
		System.out.print("\nNastępił błąd IO. Błąd zapisu archiwum\n");
	 	   //ex.printStackTrace();
	 	}
	
}
	
	
	public void zipFile(Path filename, Path dest, String dirString)
	{
		//Buffer do pisania co w pliku wejściwoym
		byte[] buffer = new byte[1024];
		 
    	try{
    		
    		//Uwtórz nazwę
    		String name= filename.toString();
    		int index=name.lastIndexOf(".");
    		if(index>0)//Jeżeli określony format to usuń cześć ".xxx"
    			name=name.substring(0, index);
    		
    		FileOutputStream fos = new FileOutputStream(dest.toString()+"/"+name+".zip");
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry(filename.toString());
    		zos.putNextEntry(ze);

    		FileInputStream in = new FileInputStream(dirString+"/"+filename);
 
    		int len;
    		///Twórz archiwum
    		while ((len = in.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}
    		///Zamykaj strumienie
    		in.close();
    		zos.closeEntry();
    		zos.close();
 
    		System.out.println("\nSkompresowano!\n");
 
    	}catch(SecurityException s)
    	{
    		System.out.print("\nProgram próbował utworzyć archiwum z pliku do którego nie ma uprawnień\n");
    	}
    	catch(IOException ex){
    		System.out.print("\nNastępił błąd IO. Błąd zapisu archiwum\n");
    	   //ex.printStackTrace();
    	}
    }

	public void zipDir(Path dirname, Path dest, String dirString) {
		List<String> fileList= new ArrayList<>();
		fileList.addAll(getFileList(dirname.toFile()));
		//Buffer do pisania co w pliku wejściwoym
		for(String file: fileList)
		{
			System.out.print("\n"+file+"\n");	
		
		}
				 
		
		/*try{
			
			FileOutputStream fos = new FileOutputStream(dest.toString()+"/"+dirname+".zip");
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry(dirname.toString());
    		zos.putNextEntry(ze);
    		for(String file: fileList)
    		{
    			FileInputStream in = new FileInputStream(dirString+"/"+dest+"/"+file);
    			System.out.print("\n	**Kompresuje: "+ file+"\n");	
        		int len;
        		///Twórz archiwum
        		while ((len = in.read(buffer)) > 0) {
        			zos.write(buffer, 0, len);
        		}
        		///Zamykaj strumienie
        		in.close();
        				
    		}
    		zos.closeEntry();
    		zos.close();
 
    		System.out.println("\nSkompresowano!\n");
		
		
		}catch(SecurityException s)
    	{
    		System.out.print("\nProgram próbował utworzyć archiwum z pliku do którego nie ma uprawnień\n");
    	}
    	catch(IOException ex){
    		System.out.print("\nNastępił błąd IO. Błąd zapisu archiwum\n");
    	   //ex.printStackTrace();
    	}*/
		
		
		
		
	}

	/**
	 * Uwaga! Funckja zaczerpnięta z 
	 * http://kodejava.org/how-do-i-compress-or-zip-a-directory-recursively/
	 * Mam nadzieję, że się nie pogniewasz- robiłem tą samą funkcje już
	 * przy bitwie na teksty:)
	 * @param directory
	 * @return List<String> Lista ścieżek do plików w danym folderze
	 */
	private List<String> getFileList(File directory) {
		List<String> fileList= new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getAbsolutePath());
                } else {
                    getFileList(file);
                }
            }
        }
		return fileList;
 
    }
	
	
	
	
}
