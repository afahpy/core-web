package com.coreweb.extras.backup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Filedownload;

import com.coreweb.Config;
import com.coreweb.control.SimpleViewModel;

public class BackupControlBody extends SimpleViewModel {

	@Init(superclass = true)
	public void initBackUpControlBody() {
	}

	@AfterCompose(superclass = true)
	public void afterComposeBackUpControlBody() {

	}

	private String referenciaBackup = "";
	private String log = "";
	private boolean descargar = false;

	@Command
	@NotifyChange("*")
	public void generarBackup() throws Exception {
		String fecha = this.m.dateToString(new Date(),
				this.m.YYYY_MM_DD_HORA_MIN_SEG_MIL);
		String nombreBackup = "backup-" + fecha + ".dump.sql";
		this.log = nombreBackup + "\n";
		ProcessBuilder builder = new ProcessBuilder(Config.SCRIPT_BACKUP,
				Config.DIRECTORIO_BACKUP, nombreBackup);

		Map<String, String> env = builder.environment();
		env.put("PGPASSWORD", "postgres");

		Process p = builder.start();
		p.waitFor();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String s = "";
		while ((s = br.readLine()) != null) {
			this.log += s + "\n";
		}

		int a = p.exitValue();
		if (a == 0) {
			this.descargar = true;
		} else {
			this.descargar = false;
		}
		this.referenciaBackup = Config.BACKUP + nombreBackup;

	}

	@Command
	public void descargarBackup() throws FileNotFoundException {
		Filedownload.save(this.referenciaBackup, null);

	}

	@Command
	@NotifyChange("*")
	public void verCatalina() throws Exception {
		this.log = "";

		Process process = Runtime.getRuntime().exec(Config.SCRIPT_VER_CATALINA);

		process.waitFor();

		String s = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				process.getInputStream()));

		while ((s = br.readLine()) != null) {
			this.log += s + "\n";
		}
	}

	public String getReferenciaBackup() {
		return referenciaBackup;
	}

	public void setReferenciaBackup(String referenciaBackup) {
		this.referenciaBackup = referenciaBackup;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public boolean isDescargar() {
		return descargar;
	}

	public void setDescargar(boolean descargar) {
		this.descargar = descargar;
	}

}
