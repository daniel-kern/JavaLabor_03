package main;

import java.io.*;
import java.util.Scanner;
import java.util.LinkedList;

public class Main {
	private static File wd = new File (System.getProperty ("user.dir"));
			
	public static void main (String[] args) {
		Scanner scanner = new Scanner (System.in);
		
		while (true) {
			String input = scanner.nextLine ();
			
			String[] cmd = input.split (" ");
			if (cmd[0].equals ("exit")) {
				exit (cmd);
			} else if (cmd[0].equals ("pwd")) {
				pwd (cmd);
			} else if (cmd[0].equals ("ls")) {
				ls (cmd);
			} else if (cmd[0].equals ("cd")) {
				cd (cmd);
			} else if (cmd[0].equals ("rm")) {
				rm (cmd);
			} else if (cmd[0].equals ("mkdir")) {
				mkdir (cmd);
			} else if (cmd[0].equals ("mv")) {
				mv (cmd);
			} else if (cmd[0].equals ("cp")) {
				cp (cmd);
			} else if (cmd[0].equals ("cat")) {
				cat (cmd);
			} else if (cmd[0].equals ("length")) {
				length (cmd);
			} else if (cmd[0].equals ("head")) {
				head (cmd);
			} else if (cmd[0].equals ("tail")) {
				tail (cmd);
			} else if (cmd[0].equals ("wc")) {
				wc (cmd);
			} else if (cmd[0].equals ("grep")) {
				grep (cmd);
			} else {
				System.out.println ("Ismeretlen parancs.");
			}
		}
	}
	
	protected static void exit (String[] cmd) {
		System.exit (0);
	}

	protected static void pwd (String[] cmd) {
		try {
			System.out.print (wd.getCanonicalPath () + " (");
		} catch (IOException e) {
			System.out.println ("ERROR: " + e.getMessage ());
		}
		
		File[] files = wd.listFiles();
		
		System.out.println (files.length + ")");
	}
	
	protected static void ls (String[] cmd) {
		File[] files = wd.listFiles ();
		
		if (cmd.length == 2 && cmd[1].equals ("-l")) {
			for (File file: files) {
				if (file.isFile ()) {
					System.out.println ("f | " + file.length () + " byte(s) | " + file.getName ());
				} else {
					System.out.println ("d | " + file.length () + " byte(s) | " + file.getName ());
				}
			}
			
			return;
		}
		
		for (File file: files) {
			System.out.println (file.getName ());
		}
	}
	
	protected static void cd (String[] cmd) {
		if (cmd.length != 2) {
			System.out.println("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		if (cmd[1].equals("..")) {
			wd = wd.getParentFile ();
			
			return;
		}
		
		File dir = new File (wd,  cmd[1]);
		
		if (!dir.exists ()) {
			System.out.println ("Nincs ilyen könyvtár.");
			return;
		}
		
		wd = dir;
	}
	
	protected static void rm (String[] cmd) {
		if (cmd.length != 2) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File file = new File (wd, cmd[1]);
		
		if (file.isFile ()) {
			boolean flag = file.delete ();
			if (!flag) {
				System.out.println ("A fájl törlése sikertelen.");
			}
			
			return;
		}
		
		System.out.println ("Könyvtár vagy nem létező fájlnév.");
	}
	
	public static void mkdir (String[] cmd) {
		if (cmd.length != 2) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File file = new File (wd, cmd[1]);
		
		if (file.isDirectory ()) {
			System.out.println ("A megadott néven már létezik könyvtár.");
			
			return;
		}
		
		file.mkdir ();
	}
	
	public static void mv (String[] cmd) {
		if (cmd.length != 3) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File renamed = new File (wd, cmd[1]);
		File nameGiver = new File (wd, cmd[2]);
		
		if (renamed.isFile ()) {
			boolean flag = renamed.renameTo (nameGiver);
			
			if (!flag) {
				System.out.println ("Hiba történt.");
			}
		}
		
	}
	
	public static void cp (String[] cmd) {
		if (cmd.length != 3) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File from = new File (wd, cmd[1]);
		File to = new File (wd, cmd[2]);
		
		try (FileInputStream fis = new FileInputStream (from);
			 FileOutputStream fos = new FileOutputStream (to)) {
			 int myByte;
			 while ((myByte = fis.read ()) != -1) {
			 	fos.write (myByte);
			 }

		} catch (IOException e) {
			System.out.println ("ERROR: " + e.getMessage ());
		}
	}
	
	protected static void cat (String[] cmd) {
		if (cmd.length != 2) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File file = new File (wd, cmd[1]);
		
		if (!file.isFile ()) {
			System.out.println ("A fájl nem létezik.");
			
			return;
		}
		
	    try (FileReader fr = new FileReader (file);
	         BufferedReader br = new BufferedReader (fr)) {

	         String line;
	         while ((line = br.readLine ()) != null) {
	        	 System.out.println (line);
	         }

	       } catch (IOException e) {
	           System.out.println ("ERROR: " + e.getMessage ());
	       }
	}
	
	protected static void length (String[] cmd) {
		if (cmd.length != 2) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File file = new File (wd, cmd[1]);
		
		if (!file.isFile ()) {
			System.out.println ("A fájl nem létezik.");
			
			return;
		}
		
		System.out.println (file.length () + " byte(s).");
	}
	
	protected static void head (String[] cmd) {
		if (cmd.length == 2) {
			File file = new File (wd, cmd[1]);
			
			if (!file.isFile ()) {
				System.out.println ("A fájl nem létezik.");
				
				return;
			}
			
			try {
				Scanner scanner = new Scanner (file);
				
				for (int i = 0; i < 10 && scanner.hasNextLine (); i++) {
					System.out.println (scanner.nextLine ());
				}
				
				scanner.close ();
			} catch (FileNotFoundException e) {
				System.out.println ("ERROR: " + e.getMessage ());
			}
			
			return;
		}
		
		if (cmd.length == 4) {
			File file = new File (wd, cmd[3]);
			
			if (!file.isFile ()) {
				System.out.println ("A fájl nem létezik.");
				
				return;
			}
			
			try {
				Scanner scanner = new Scanner (file);
				
				for (int i = 0; i < Integer.valueOf (cmd[2]) && scanner.hasNextLine (); i++) {
					System.out.println (scanner.nextLine ());
				}
				
				scanner.close ();
			} catch (FileNotFoundException e) {
				System.out.println ("ERROR: " + e.getMessage ());
			}
			
			return;
		}
		
		System.out.println ("Hiányzó / túl sok paraméter.");
	}
	
	protected static void tail (String[] cmd) {
		if (cmd.length == 2) {
			File file = new File (wd, cmd[1]);
			if (!file.isFile ()) {
				System.out.println ("A fájl nem létezik.");
				
				return;
			}
			
			LinkedList <String> ll = new LinkedList <String> ();
			try (Scanner scanner = new Scanner (file)) {
				while (scanner.hasNextLine ()) {
					ll.add (scanner.nextLine ());
				}
				
				if (ll.size () < 10) {
					for (int i = ll.size (); i > 0; i--) {
						System.out.println (ll.get(i - 1));
					}
				} else {
					for (int i = ll.size (); i > ll.size () - 10; i--) {
						System.out.println (ll.get (i - 1));
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println ("ERROR: " + e.getMessage ());
			}
			
			
			return;
		}
		
		if (cmd.length == 4) {
			File file = new File (wd, cmd[3]);
			if (!file.isFile ()) {
				System.out.println ("A fájl nem létezik.");
				
				return;
			}
			
			LinkedList <String> ll = new LinkedList <String> ();
			try (Scanner scanner = new Scanner (file)) {
				while (scanner.hasNextLine ()) {
					ll.add (scanner.nextLine ());
				}
				
				if (ll.size () < Integer.valueOf (cmd[2])) {
					for (int i = ll.size (); i > 0; i--) {
						System.out.println (ll.get (i - 1));
					}
				} else {
					for (int i = ll.size (); i > ll.size () - Integer.valueOf (cmd[2]); i--) {
						System.out.println (ll.get (i - 1));
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println ("ERROR: " + e.getMessage ());
			}
			return;
		}
		
		System.out.println ("Hiányzó / túl sok paraméter.");
	}
	
	protected static void wc (String[] cmd) {
		if (cmd.length != 2) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File file = new File (wd, cmd[1]);
		if (!file.isFile ()) {
			System.out.println ("A fájl nem létezik.");
			
			return;
		}
		
		try (Scanner scanner = new Scanner (file)) {
			int rowCount = 0;
			int wordCount = 0;
			int letterCount = 0;
			
			while (scanner.hasNextLine ()) {
				rowCount++;
				String line = scanner.nextLine ();
				
				String[] words = line.split (" ");
				wordCount += words.length;
				
				String[] letters = line.split ("");
				letterCount += letters.length;
			}
			
			System.out.println ("rows: " + rowCount + " | words: " + wordCount + " | letters: " + letterCount);
		} catch (FileNotFoundException e) {
			System.out.println ("ERROR: " + e.getMessage ());
		} 
	}
	
	protected static void grep (String[] cmd) {
		if (cmd.length != 3) {
			System.out.println ("Hiányzó / túl sok paraméter.");
			
			return;
		}
		
		File file = new File (wd, cmd[2]);
		if (!file.isFile ()) {
			System.out.println ("A fájl nem létezik.");
		}
		
		try (Scanner scanner = new Scanner (file)) {
			while (scanner.hasNextLine ()) {
				String line = scanner.nextLine ();;
				if (line.matches (".*" + cmd[1] + ".*")) {
					System.out.println (line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println ("ERROR: " + e.getMessage ());
		}
	}
}