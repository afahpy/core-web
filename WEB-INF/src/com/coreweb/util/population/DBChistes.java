package com.coreweb.util.population;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import com.coreweb.domain.Register;
import com.coreweb.domain.Tecorei;

public class DBChistes {

	public static String file = "./WEB-INF/chistes.txt";

	
	public static void chistes() {

		try {
			Register rr = Register.getInstance();
			long i = 0;
			String chiste = "";
			boolean grabar = false;

			File f = new File(file);
			System.out.println(f.getAbsolutePath());
			System.out.println(f.getAbsolutePath());
			BufferedReader entrada;
			entrada = new BufferedReader(new FileReader(f));
			String linea;
			while (entrada.ready()) {
				linea = entrada.readLine();
				chiste += linea;
				
				if ((linea.trim().length() == 0)&&(chiste.trim().length() > 0)){
					if (chiste.trim().length() < 255){
						Tecorei te = new Tecorei();
						te.setTexto(chiste.trim());
						rr.saveObject(te, "chiste");
						System.out.println((++i)+") " + chiste);
					}
					chiste = "";
				}
				chiste += "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void ver(){
		try {
			
			Random rand = new Random(System.currentTimeMillis());
			int n = 43;
			for (int i = 0; i < 10; i++) {
				System.out.println(rand.nextInt(n));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		chistes();
		//ver();
	}

}
