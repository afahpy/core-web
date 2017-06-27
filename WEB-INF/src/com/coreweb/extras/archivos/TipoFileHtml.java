package com.coreweb.extras.archivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;

import javax.activation.MimetypesFileTypeMap;

import com.coreweb.util.Misc;

public class TipoFileHtml implements Comparator{
	
	Misc m = new Misc();
	static String IMG_DIRECTORIO = "/core/images/folder.png";
	static String IMG_ARCHIVO = "/core/images/file.png";
	
	String image = "";
	
	File f;
	
	public TipoFileHtml(File f){
		this.f = f;
	}
	
	public String getNombre(){
		String out = this.f.getName();
		return out;
	}
	
	public boolean esDirectorio(){
		return this.f.isDirectory();
	}
	
	public String getImage(){
		String out = IMG_ARCHIVO;
		if (this.f.isDirectory() == true){
			out = IMG_DIRECTORIO;
		}
		return out;
	}
	
	public FileInputStream getInputStream() throws FileNotFoundException{
		return new FileInputStream(this.f);
	}
	
	
	public String getMimetypesFile(){
		return new MimetypesFileTypeMap().getContentType(this.f);
	}
	
	public String getTamanio(){
		String out = m.formatoNumero(Math.round(Math.ceil(this.f.length()/1024.0)))+" kb";
		return out;
	}
	
	public File getFile(){
		return this.f;
	}

	@Override
	public int compare(Object o1, Object o2) {
		String ff1 = ((File) o1).getName().toLowerCase();
		String ff2 = ((File) o2).getName().toLowerCase();
		return ff1.compareTo(ff2);
	}
	
}
