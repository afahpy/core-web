package com.coreweb.extras.archivos;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zkmax.zul.Filedownload;
import org.zkoss.zul.Textbox;

import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.util.Misc;

public class ViewModelManejadorArchivos {

	Misc m = new Misc();
	
	boolean modoAdm = false;
	String direBase = "";
	
	String direCCWeb = "";

	File fileCC = null;

	// la lista de archivos corrientes
	List<TipoFileHtml> listaArchivos = new ArrayList<>();
/*
	@Init(superclass = true)
	public void initViewModelManejadorArchivos(
			@ExecutionArgParam("direBase") String direBase, @ExecutionArgParam("modoAdm") boolean modoAdm) {
		this.direBase = direBase;
		this.fileCC = new File(this.direBase);
		this.modoAdm = modoAdm;
		this.actualizarArchivos();
	}
*/

	@Init(superclass = true)
	public void initViewModelManejadorArchivos(){
		String direBase = (String) Executions.getCurrent().getAttribute("direBase");
		boolean modoAdm = (boolean) Executions.getCurrent().getAttribute("modoAdm"); 
		this.direBase = direBase;
		this.fileCC = new File(this.direBase);
		this.modoAdm = modoAdm;
		this.actualizarArchivos();
	}

	
	@AfterCompose(superclass = true)
	public void afterComposeViewModelManejadorArchivos() {

	}

	@Command
	public void clickFile(@BindingParam("file") TipoFileHtml file)
			throws FileNotFoundException {
		if (file.esDirectorio() == true) {
			this.fileCC = file.getFile();
			this.actualizarArchivos();
			BindUtils.postNotifyChange(null, null, this, "*");

		} else {
			// es archivo
			Filedownload.save(file.getInputStream(), file.getMimetypesFile(),
					file.getNombre());
		}

	}
	
	@Command
	public void eliminarFile(@BindingParam("file") TipoFileHtml file){
		String msg = "Está seguro que desea eliminar el archivo/directorio\n"+file.getNombre();
		boolean siDel = this.m.mensajeEliminar(msg);
		if (siDel == true){
			this.deleteFolder(file.getFile());
			this.actualizarArchivos();
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		
	}
	
	private void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
    	               

	
	@Command
	public void agregarFile(@BindingParam("evt") UploadEvent event) {
		String path = this.fileCC.getPath() + "/" + event.getMedia().getName();

		if (event.getMedia().isBinary() == true) {
			m.uploadFile(path, (InputStream) new ByteArrayInputStream(event.getMedia().getByteData()));
		} else {
			InputStream s = new BufferedInputStream(new ReaderInputStream(event.getMedia().getReaderData()));
			m.uploadFile(path, s);
		}
		this.actualizarArchivos();
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	
	
	
	@Command
	public void volverNivel(){
		if (this.getDireCCWeb().compareTo("./")!=0){
			this.fileCC = this.fileCC.getParentFile();
			this.actualizarArchivos();
			BindUtils.postNotifyChange(null, null, this, "*");
		}else{
			// no hace nada, está en el raiz
		}
		
	}
	
	@Command
	public void crearDirectorio(){
		Textbox txb = new Textbox();
		txb.setWidth("200px");
		txb.setValue("");
		txb.setPlaceholder("nombre");
		BodyPopupAceptarCancelar bpac = new BodyPopupAceptarCancelar();
		bpac.setHighWindows("150px");
		bpac.addComponente("Directorio", txb);
		bpac.showPopup("Crear directorio");
		String nombre = txb.getValue().trim();
		if ((bpac.isClickAceptar() == true)&&(nombre.length()>0)){
			File f = new File(this.fileCC.getPath()+"/"+nombre);
			f.mkdir();
			this.actualizarArchivos();
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		
	}

	public String getDireBase() {
		return direBase;
	}

	public void setDireBase(String direBase) {
		this.direBase = direBase;
	}

	private void actualizarArchivos() {
		this.listaArchivos = new ArrayList<>();

		File[] fList = this.fileCC.listFiles();
		if (fList != null){
			Arrays.sort(fList, new TipoFileHtml(null));
			
			for (int i = 0; i < fList.length; i++) {
				File ff = fList[i];
				this.listaArchivos.add(new TipoFileHtml(ff));
			}
		}
	}

	public List<TipoFileHtml> getListaArchivos() {
		return listaArchivos;
	}

	public void setListaArchivos(List<TipoFileHtml> listaArchivos) {
		this.listaArchivos = listaArchivos;
	}

	public String getDireCCWeb() {
		this.direCCWeb = "./";
		try {
			String cc = this.fileCC.getPath();
			int pIni = this.direBase.length();
			this.direCCWeb = "./" + cc.substring(pIni);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return direCCWeb;
	}

	public void setDireCCWeb(String direCCWeb) {
		this.direCCWeb = direCCWeb;
	}

	public static void main(String[] args) throws Exception {
		String f1 = "/home/daniel/datos/varios/propuestas/scg33/proyecto-scg33/directorios/logia4/";

		File ff = new File(f1);
		
		File[] fa = ff.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File file = fa[i];
			String out = ""+ Math.round(Math.ceil(file.length()/1024.0));
			System.out.println(file.getName()+"("+out+")");
		}
		System.out.println("==========================");
		Arrays.sort(fa, new TipoFileHtml(ff));
		for (int i = 0; i < fa.length; i++) {
			File file = fa[i];
			System.out.println(file.getName());
		}
		
		
	}

	public boolean isModoAdm() {
		return modoAdm;
	}

	public void setModoAdm(boolean modoAdm) {
		this.modoAdm = modoAdm;
	}

}

// ===================================================
// ===================================================
// ===================================================

