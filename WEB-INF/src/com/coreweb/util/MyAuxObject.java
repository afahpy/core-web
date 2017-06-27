package com.coreweb.util;

import java.util.Comparator;

import com.coreweb.domain.IiD;
import com.coreweb.dto.DTO;

public abstract class MyAuxObject implements IiD, Comparable, Comparator{ //, Cloneable {

	private static long ID_NUEVO = -1;

	Long id = new Long(getNewId());

	String orden = "-1";
	String auxi = "";
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public static synchronized long getNewId() {
		ID_NUEVO--;
		return ID_NUEVO;
	}

	
	@Override
	public boolean esNuevo() {
		// TODO Auto-generated method stub
		return this.id.longValue() < 0;
	}

	public boolean equals(Object o) {
		boolean b = false;
		try {
			MyAuxObject aux = (MyAuxObject) o;
			b = ((this.getId().longValue() == aux.getId().longValue())); // &&(this.getText().compareTo(aux.getText())==0));
		} catch (Exception e) {
			b = false;
		}
		return b;
	}

	public int compareTo(Object compareObject) {
		int out = -1;
		MyAuxObject aux = (MyAuxObject) compareObject;
		if ((this.getId().longValue() == 0) && (aux.getId().longValue() == 0)) {
			out = this.toString().compareTo(compareObject.toString());
		} else {
			out = (int) (this.getId().longValue() - aux.getId().longValue());
		}

		return out;
	}

	@Override
	public int compare(Object ob1, Object ob2) {
		MyAuxObject o1 = (MyAuxObject) ob1;
		MyAuxObject o2 = (MyAuxObject) ob2;

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

	public int xhashCode() throws Exception {
		/*
		 * Cómo el hash era el ID, entonces planchaba los objetos que tenian el mismo id
		 * por ejemplo, id 4 para el maypair de RubroCliente y id 4 para el de TipoCliente
		 */
		if (1 == 1){
			throw new Exception("No debería usarse este método xhashCode "+this);
		}
		return this.getId().intValue();
		
	}

	public Object xclone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			// No debería ocurrir
		}
		return clone;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getAuxi() {
		return auxi;
	}

	public void setAuxi(String auxi) {
		this.auxi = auxi;
	}

	
	//=======================================================================
	//=======================================================================
	//=======================================================================
	
	/***************************************************************************************
	 * Agregago para el error de seleccionar de una lista, 
	 * El zul no lo usa pero da error, es decir, pide que exista pero no cambia nada en el proceso
	 * no se que pasa :(
	 */

	
	
	/*

	private String text = "texto MyArray";
	private Object pos1 = "-error ver-";
	private Object pos2 = "-error ver-";
	private Object pos3 = "-error ver-";
	private Object pos4 = "-error ver-";
	private Object pos5 = "-error ver-";
	private Object pos6 = "-error ver-";
	private Object pos7 = "-error ver-";
	private Object pos8 = "-error ver-";
	private Object pos9 = "-error ver-";
	private Object pos10 = "-error ver-";
	private Object pos11 = "-error ver-";
	private Object pos12 = "-error ver-";
	private Object pos13 = "-error ver-";
	private Object pos14 = "-error ver-";
	private Object pos15 = "-error ver-";
	private Object pos16 = "-error ver-";
	private Object pos17 = "-error ver-";
	private Object pos18 = "-error ver-";
	private Object pos19 = "-error ver-";
	private Object pos20 = "-error ver-";


	
		
	public String getText() {
		System.out.println("=====================ver: GetText "+this.getClass().getName());
		try {
			String x = null;
			x.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("===========================================================");
		return "-- error ---";
	}

	public void setText(String text) {
		System.out.println("=====================ver SetText: "+text+" "+this.getClass().getName());
		try {
			String x = null;
			x.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("===========================================================");
	}

	public Object getPos1() {
		System.out.println("=====================ver getPos1:"+pos1+" "+this.getClass().getName());
		return pos1;
	}

	public void setPos1(Object pos1) {
		System.out.println("=====================ver SetPos1:"+pos1+" "+this.getClass().getName());
	}

	public Object getPos2() {
		System.out.println("=====================ver getPos2:"+pos2+" "+this.getClass().getName());
		return pos2;
	}

	public void setPos2(Object pos2) {
		System.out.println("=====================ver SetPos2:"+pos2+" "+this.getClass().getName());
	}

	public Object getPos3() {
		System.out.println("=====================ver getPos3:"+pos3+" "+this.getClass().getName());
		return pos3;
	}

	public void setPos3(Object pos3) {
		System.out.println("=====================ver SetPos3:"+pos3+" "+this.getClass().getName());
	}

	public Object getPos4() {
		System.out.println("=====================ver getPos4:"+pos4+" "+this.getClass().getName());
		return pos4;
	}

	public void setPos4(Object pos4) {
		System.out.println("=====================ver SetPot4:"+pos4+" "+this.getClass().getName());

	}

	public Object getPos5() {
		System.out.println("=====================ver getPos5:"+pos5+" "+this.getClass().getName());
		return pos5;
	}

	public void setPos5(Object pos5) {
		System.out.println("=====================ver SetPos5:"+pos5+" "+this.getClass().getName());

	}

	public Object getPos6() {
		return pos6;
	}

	public void setPos6(Object pos6) {

	}

	public Object getPos7() {
		return pos7;
	}

	public void setPos7(Object pos7) {

	}

	public Object getPos8() {
		return pos8;
	}

	public void setPos8(Object pos8) {

	}

	public Object getPos9() {
		return pos9;
	}

	public void setPos9(Object pos9) {

	}

	public Object getPos10() {
		return pos10;
	}

	public void setPos10(Object pos10) {

	}

	public Object getPos11() {
		return pos11;
	}

	public void setPos11(Object pos11) {

	}

	public Object getPos12() {
		return pos12;
	}

	public void setPos12(Object pos12) {

	}

	public Object getPos13() {
		return pos13;
	}

	public void setPos13(Object pos13) {

	}

	public Object getPos14() {
		return pos14;
	}

	public void setPos14(Object pos14) {

	}

	public Object getPos15() {
		return pos15;
	}

	public void setPos15(Object pos15) {

	}

	public Object getPos16() {
		return pos16;
	}

	public void setPos16(Object pos16) {

	}

	public Object getPos17() {
		return pos17;
	}

	public void setPos17(Object pos17) {

	}

	public Object getPos18() {
		return pos18;
	}

	public void setPos18(Object pos18) {

	}

	public Object getPos19() {
		return pos19;
	}

	public void setPos19(Object pos19) {

	}

	public Object getPos20() {
		return pos20;
	}

	public void setPos20(Object pos20) {

	}	
	
	
	*/
	
	
}
