/**
 * Klasa monitorujaca dany folder i uruchamiajaca proces "zipowania"
 */

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;



import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;


public class Monitor {

	private WatchService watcher=null;
	
	
	///Konstruktor
	public Monitor(String dirString, String destString) throws InterruptedException
	{
		Path dir=Paths.get(dirString);
		Path dest=Paths.get(destString);
		
		
		//Walidacja podanych ścieżek
		try {
			this.check(dir,dest);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.print("\nNastąpił poważny błąd. Zamykam program.");
			Thread.sleep(10000);
			System.exit(0); 
		}

		
		
		for (;;) //Nieskonczona petla
		{
			WatchKey key=null;
		try {
			watcher=FileSystems.getDefault().newWatchService();
		} catch (IOException e1) {
			System.out.print("\n2\n");
			e1.printStackTrace();
		}
		if(watcher!=null)
		{
			try {
				///Rejestruj wymienione zdarzenia
				 key =dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
			}
			catch(SecurityException s)
			{
				System.out.print("\nNie mam uprawnień do monitorowania:"+dir+"\n");
				continue;
			}
			catch (IOException e) {
				System.out.print("\nNastępił błąd IO\n");
				System.out.print("\nZamykam program z powodu błędu\n");
				Thread.sleep(10000);
				System.exit(0); 
			}
			
		}
		
			try {
				key=watcher.take();
			} catch (InterruptedException e) {
				System.out.print("\nNastępił błąd przy monitorowaniu danego folderu\n");
				//e.printStackTrace();
			}
			 for (WatchEvent<?> event: key.pollEvents()) 
			 {
			        WatchEvent.Kind<?> kind = event.kind();
			        WatchEvent<Path> ev = (WatchEvent<Path>)event;
			        
			        
			        if (kind == OVERFLOW) {
	                    continue;
	                }
			        
			        Path filename = ev.context();
			      
			        if(kind==ENTRY_CREATE)
			        {
			        	
			        	Zip test=new Zip();
			        	System.out.print("\nKompresuje: "+ filename+"\n");	
			        	//if(!filename.toFile().isFile())
			        	//	System.out.print("\nN\n");	
			        		//test.zipDir(filename,dest,dirString);	
			        	//else	
			        		test.zipFile(filename,dest,dirString);	
			        }
			        else if(kind==ENTRY_DELETE)
			        {
			        	
			        	String delete= destString+"/"+filename+".zip";
			        	//System.out.print("\nusun: "+ delete+"\n");	
			        	Path remove=Paths.get(delete);
			        	  try {
							Files.deleteIfExists(remove);
						} catch (IOException  e) 
						{
							System.out.print("\nNastąpił błąd usuwania archiwum!\n");
							continue;
							
						}
	
			        }
   
			 }
		
	
		}
	
	
	
	}

	///Sprawdz poprawnosc podanych ścieżek
	private void check(Path dir, Path dest) throws InterruptedException {
		
		if(!Files.isDirectory(dir)) 
		{
			System.out.print("\nPodana ścieżka do monitorowania nie jest katalogiem. Zamykam program.\n");
			Thread.sleep(10000);
			System.exit(0); 
			}
		if(!Files.isDirectory(dest))
		{
			System.out.print("\nPodana ścieżka do zapisu .zip nie jest katalogiem. Zamykam program.\n");
			Thread.sleep(10000);
			System.exit(0); 
			}

		if(!Files.exists(dir)) 
		{
			System.out.print("\nKatalog do monitorowania nie istnieje! Zamykam program.\n");
			Thread.sleep(10000);
			System.exit(0); 
			}
		if(!Files.exists(dest))
		{
			System.out.print("\nKatalog do zapisu .zip nie istnieje! Zamykam program.\n");
			Thread.sleep(10000);
			System.exit(0); 
			}
		
		if(!Files.isReadable(dir))
		{
			System.out.print("\nProgram nie ma prawa czytać z podanego katalogu. Zamykam program.\n");
			Thread.sleep(10000);
			System.exit(0); 
			
		}
		

		if(!Files.isWritable(dest))
		{
			System.out.print("\nProgram nie ma prawa tworzyć archiwum w podanej lokacji. Zamykam program.\n");
			Thread.sleep(10000);
			System.exit(0); 
			
		}
		
	}
	
}
