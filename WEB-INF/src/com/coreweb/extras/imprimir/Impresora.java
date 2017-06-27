package com.coreweb.extras.imprimir;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Impresora {

	public static void main(String args[]) throws Exception {

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
		PrintService pr = buscarPrinter(nombreImpresora);
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

		PrintService[] lista = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < lista.length; i++) {
			PrintService pr = lista[i];
			String namePrinter = pr.getName();
			if (namePrinter.compareTo(name) == 0) {
				out = pr;
			}
		}

		return out;
	}

	private void imprimir(String pdfFile, PrintService pr) throws Exception {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(pdfFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("no se encontró el archivo para imrpimir [" + pdfFile + "]");
		}

		DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		Doc document = new SimpleDoc(inputStream, docFormat, null);

		PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

		DocPrintJob printJob = pr.createPrintJob();
		try {
			printJob.print(document, attributeSet);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error al imprimir el documento ");
		} finally {
			inputStream.close();
		}

	}

}
