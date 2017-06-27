package com.coreweb.dto;

import java.beans.PropertyDescriptor;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.coreweb.domain.IiD;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public abstract class DTO implements IiD, Comparable, Comparator, Cloneable {

	String orden = "";
	
	private Long id = new Long(-1);

	private char dbEstado = DBEstado.DB_EDITABLE;

	private Date modificado = new Date();

	private String usuarioMod = "new";

	private String auxi = "";
	
	private String ip_pc = "";	

	transient private Misc m = new Misc();

	private boolean checked = false; // agregar en todos los DTO

	
	
	
	// public abstract void setDomainObject(Domain dom);

	// public abstract Domain getDomainObject();

	//
	
	
	
	
	public boolean esNuevo() {
		return (this.id < 0);
	}

	public Misc getMisc() {
		if (this.m == null){
			this.m = new Misc();
		}
		return m;
	}

	public String getUltimaModificacion() {
		if (this.esNuevo() == true) {
			return "--nuevo--";
		}
		String out = "" + this.getUsuarioMod() + ", "
				+ this.getMisc().dateToString(this.modificado, Misc.D_MMMM_YYYY2);
		return out;
	}

	public Class getDomainFromDTO() {
		return null;
	}

	public boolean isEsNuevo() {
		return this.esNuevo();
	}

	public String getAuxi() {
		return auxi;
	}

	public void setAuxi(String auxi) {
		this.auxi = auxi;
	}

	public void setEsNuevo(boolean esNuevo) throws Exception {
		throw new Exception("Intentando modificar si es nuevo un DTO");
	}

	public String getUsuarioMod() {
		return usuarioMod;
	}

	public void setUsuarioMod(String usuarioMod) {
		this.usuarioMod = usuarioMod;
	}

	public boolean isChecked() {
		return checked;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getModificado() {
		return modificado;
	}

	public void setModificado(Date modificado) {
		this.modificado = modificado;
	}

	public char getDbEstado() {
		return dbEstado;
	}

	public void setDbEstado(char dbEstado) {
		this.dbEstado = dbEstado;
	}

	public boolean esReadonly() {
		return this.dbEstado == DBEstado.DB_READONLY;
	}
	
	public boolean isReadonly(){
		return this.esReadonly();
	}

	public void setReadonly() {
		this.dbEstado = DBEstado.DB_READONLY;
	}

	public void setDeleted() {
		this.dbEstado = DBEstado.DB_DELETE;
	}

	public List<String> getColumnNames() {

		System.out.println("DTO.getColumnNames() no implementado en: "
				+ this.getClass().getName());
		return null;
	}

	public List<String> getFieldNames() {
		System.out.println("DTO.getFieldNames() no implementado en: "
				+ this.getClass().getName());
		return null;
	}

	public Object getFieldValue(String fieldName) throws Exception {
		Object value = new PropertyDescriptor(fieldName, this.getClass())
				.getReadMethod().invoke(this);
		return value;

	}

	public int compareTo(Object compareObject) {
		int out = 0;

		DTO dtoAux = (DTO) compareObject;

		if ((this.getId().longValue() == 0)
				&& (dtoAux.getId().longValue() == 0)) {
			out = this.toString().compareTo(compareObject.toString());
		} else {
			out = (int) (this.getId().longValue() - dtoAux.getId().longValue());
		}
		return out;
	}

	@Override
	public int compare(Object ob1, Object ob2) {
		DTO o1 = (DTO) ob1;
		DTO o2 = (DTO) ob2;

		int out = 0;
		long idnuevo = -1;
		long idO1 = o1.getId().longValue();
		long idO2 = o2.getId().longValue();

		if ((idO1 == idnuevo) && (idO2 != idnuevo)) {
			out = 1;
		} else if ((idO1 != idnuevo) && (idO2 == idnuevo)) {
			out = -1;
		} else {
			out = (int) (idO1 - idO2);
		}

		return out;
	}

	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			// No debería ocurrir
		}
		return clone;
	}

	public String getTextOrden() {
		return this.getMisc().ceros(this.getId() + "", 8);
	}

	/**
	 * Retorna el MyPair de un DTO. Es necesario reimplementar el método
	 * getCampoMyPair para definir que campos se usaran
	 * 
	 * @return
	 * @throws Exception
	 */
	public MyPair toMyPair() throws Exception {
		MyPair ma = new MyPair();
		ma.setId(this.getId());
		String[] campos = this.getCamposMyPair();
		String valor = "";
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			Object dato = this.getMisc().getValue(this, campo);
			valor += dato+" - ";
		}
		this.getMisc().setValue(ma, "text", valor);
		return ma;
	}

	/**
	 * Retorna el MyArray de un DTO. Es necesario reimplementar el método
	 * getCamposMyArray para definir que campos se usaran
	 * 
	 * @return
	 * @throws Exception
	 */
	public MyArray toMyArray() throws Exception {
		MyArray ma = new MyArray();
		ma.setId(this.getId());
		String[] campos = this.getCamposMyArray();
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			Object dato = this.getMisc().getValue(this, campo);
			this.getMisc().setValue(ma, "pos" + (i + 1), dato);
		}
		return ma;
	}

	/**
	 * Los campos que se usan para pasar de Dto a MyArray. Si no se define, sólo
	 * usa el campo Id.
	 * 
	 * @return
	 */
	public String[] getCamposMyArray() {
		String[] campos = {};
		return campos;
	}

	/**
	 * Los campos que se usan para pasar de Dto a MyPair. Si no se define, sólo
	 * usa el campo Id.
	 * 
	 * @return
	 */
	public String[] getCamposMyPair() {
		String[] campos = {};
		return campos;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getIp_pc() {
		return ip_pc;
	}

	public void setIp_pc(String ip_pc) {
		this.ip_pc = ip_pc;
	}

	
	/*
	 * public Comparator<DTO> COMPARATOR = new Comparator<DTO>() { // This is
	 * where the sorting happens. public int compare(DTO o1, DTO o2) { int out =
	 * 0; long idnuevo = -1; long idO1 = o1.getId().longValue(); long idO2 =
	 * o2.getId().longValue();
	 * 
	 * 
	 * if ((idO1 == idnuevo)&&(idO2 != idnuevo)) { out = 1; } else if ((idO1 !=
	 * idnuevo)&&(idO2 == idnuevo)) { out = -1; }else{ out = (int) (idO1 -
	 * idO2); }
	 * 
	 * return out; } };
	 */
}