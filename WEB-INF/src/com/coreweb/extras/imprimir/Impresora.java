package com.coreweb.extras.imprimir;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Impresora {

	CheckFecha time = new CheckFecha();
	
	
	public static void xmain(String args[]) throws Exception {

		// String nombreImpresora = "PDF";
		String nombreImpresora = "HP-afah-P1102w";
		// "HP-LaserJet-Professional-P-1102w
		// HP-afah-P1102w
		// Hewlett-Packard-HP-LaserJet-Professional-P-1102w

		Impresora print = new Impresora();
		List<String> l = print.listaImpresoras();
		for (int i = 0; i < l.size(); i++) {
			System.out.println(i + ")" + l.get(i));
		}
		if (1 == 1) {
			return;
		}

		String archivo = "/home/daniel/Desktop/borrar/yo/pr_02.pdf";

		print.imprimirPdf(archivo, nombreImpresora);

	}

	public boolean existe(String nombreImpresora) {
		boolean out = false;
		PrintService[] lista = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < lista.length; i++) {
			PrintService pr = lista[i];
			String namePrinter = pr.getName();
			if (namePrinter.compareTo(nombreImpresora) == 0) {
				out = true;
			}
		}
		return out;
	}

	public void imprimirPdf(String pathArchivoPdf, String nombreImpresora) throws Exception {
		
		System.out.println("2 - buscar impresora "+pathArchivoPdf + time);
		PrintService pr = buscarPrinter(nombreImpresora);
		System.out.println("5 - buscar impresora "+pathArchivoPdf + time);
		imprimir(pathArchivoPdf, pr);
	}

	public List<String> listaImpresoras() {
		List<String> out = new ArrayList<>();
		PrintService[] lista = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < lista.length; i++) {
			PrintService pr = lista[i];
			String namePrinter = pr.getName();
			out.add(namePrinter);
		}
		return out;
	}

	private PrintService buscarPrinter(String name) throws Exception {
		PrintService out = null;

		System.out.println("3 - buscarPrinter(String name) "+name + time);

		PrintService[] lista = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < lista.length; i++) {
			System.out.println("3." + i  + time);
			PrintService pr = lista[i];
			String namePrinter = pr.getName();
			if (namePrinter.compareTo(name) == 0) {
				out = pr;
			}
		}

		System.out.println("4 - encontro "+out + time);
		return out;
	}

	private void imprimir(String pdfFile, PrintService pr) throws Exception {
		
		System.out.println("6 - imprimir "+pdfFile + time);
		
		FileInputStream inputStream = null;
		try {
			System.out.println("7 - imprimir "+pdfFile + time);
			inputStream = new FileInputStream(pdfFile);
			System.out.println("8 - imprimir "+pdfFile + time);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("no se encontró el archivo para imrpimir [" + pdfFile + "]");
		}

		DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;

		System.out.println("9 - imprimir "+pdfFile + time);

		Doc document = new SimpleDoc(inputStream, docFormat, null);

		System.out.println("10 - imprimir "+pdfFile + time);

		PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
		System.out.println("11 - imprimir "+pdfFile + time);

		DocPrintJob printJob = pr.createPrintJob();
		System.out.println("12 - imprimir "+pdfFile + time);
		try {
			printJob.print(document, attributeSet);
			System.out.println("13 - imprimir "+pdfFile + time);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error al imprimir el documento ");
		} finally {
			inputStream.close();
		}

		System.out.println("14 - imprimir "+pdfFile + time);
	}
	
	
	public static void main(String[] args) {
		
		CheckFecha ch1 = new CheckFecha();
		Date d1 = new Date();
		Date d2 = new Date();
		long t1 = d1.getTime();
		long t2 = d2.getTime();
		
		System.out.println(ch1);
		while (t2 - t1 < (3*1000) ){
			d2 = new Date();
			t2 = d2.getTime();
		}
		System.out.println(ch1);
		
		System.out.println(d1+" "+ch1);
		System.out.println(d2+" "+ch1);
		System.out.println(d2.getTime() - d1.getTime()+" "+ch1);
	}
}



class CheckFecha {
	
	String yy = "ID_"+System.currentTimeMillis();
	
	Date old = new Date();
	
	public String toString(){
		String out = "";
		Date cc = new Date();
		long diff = cc.getTime() - old.getTime();
		out = cc + "  "+yy+" dif:"+diff;
		old = cc;
		return " ["+out+ "] ";
	}
	
	
}
