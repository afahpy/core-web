package com.coreweb.util;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.batik.dom.util.HashTable;

import com.coreweb.domain.IiD;

public class MyArray extends MyAuxObject {

	transient Misc m = new Misc();
	
	
	private static String POS_VACIO = "";
	private Object pos1 = POS_VACIO;
	private Object pos2 = POS_VACIO;
	private Object pos3 = POS_VACIO;
	private Object pos4 = POS_VACIO;
	private Object pos5 = POS_VACIO;
	private Object pos6 = POS_VACIO;
	private Object pos7 = POS_VACIO;
	private Object pos8 = POS_VACIO;
	private Object pos9 = POS_VACIO;
	private Object pos10 = POS_VACIO;
	private Object pos11 = POS_VACIO;
	private Object pos12 = POS_VACIO;
	private Object pos13 = POS_VACIO;
	private Object pos14 = POS_VACIO;
	private Object pos15 = POS_VACIO;
	private Object pos16 = POS_VACIO;
	private Object pos17 = POS_VACIO;
	private Object pos18 = POS_VACIO;
	private Object pos19 = POS_VACIO;
	private Object pos20 = POS_VACIO;
	private Object pos21 = POS_VACIO;

	
	public MyArray() {
	}

	
	/**
	 * Pasa un MyArray a un arreglo de objetos. Considera 21 datos
	 * @return
	 * @throws Exception
	 */
	public Object[] toArregloObjetos() throws Exception{
		ArrayList<Object> arr = new ArrayList<>();
		for (int i = 1; i < 22; i++) {
			Object dato = this.m.getValue(this, "pos"+i);
			if (POS_VACIO != dato){
				arr.add(dato);
			}
		}
		return arr.toArray();
	}
	
	
	//------------------------------
	/*
	 * Para manejar nombres en el MyArray 
	 */
	
	private Hashtable<String, String> nomPosiciones;

	public void setNombrePosiciones(String[] nombres){
		nomPosiciones = new Hashtable<>();
		for (int i = 1; i <= nombres.length; i++) {
			String nom  = nombres[i-1].toLowerCase();
			String pos = "pos"+i;
			nomPosiciones.put(nom, pos);
		}
	}
	public Object getDato(String nombre) throws Exception{
		nombre = nombre.toLowerCase();
		String pos = nomPosiciones.get(nombre);
		Object obj = m.getValue(this, pos);
		return obj;
	}
	public void setDato(String nombre, Object valor) throws Exception{
		nombre = nombre.toLowerCase();
		String pos = nomPosiciones.get(nombre);
		m.setValue(this, pos, valor);
	}
	//---------------------------
	public MyPair toMyPair(){
		MyPair mp = new MyPair();
		mp.setId(this.getId());
		mp.setText(this.datosToString());
		return mp;
	}
	
	private String datosToString(){
		String out = "";
		try {
			for (int i = 1; i < 21; i++) {
				Object o = m.getValue(this, "pos" + i);
				if (o != POS_VACIO){
					out = out + o + " - ";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out ;
	}
	
	public Object getPos(int pos) throws Exception{
		return m.getValue(this, "pos" + pos);
	}
	
	public void setPos(int pos, Object dato) throws Exception{
		m.setValue(this, "pos"+pos, dato);
	}
	
	public String toString() {
		if (this.esNuevo() == true) {
			return " ";
		}

		String out = this.datosToString();
		return out + " (" + this.getId() + ")";
	}

	public MyArray(Object[] dato) {
		this.setId((long) dato[0]);
		try {
			this.pos1 = dato[1];
			this.pos2 = dato[2];
			this.pos3 = dato[3];
			this.pos4 = dato[4];
			this.pos5 = dato[5];
			this.pos6 = dato[6];
			this.pos7 = dato[7];
			this.pos8 = dato[8];
			this.pos9 = dato[9];
			this.pos10 = dato[10];
			this.pos11 = dato[11];
			this.pos12 = dato[12];
			this.pos13 = dato[13];
			this.pos14 = dato[14];
			this.pos15 = dato[15];
			this.pos16 = dato[16];
			this.pos17 = dato[17];
			this.pos18 = dato[18];
			this.pos19 = dato[19];
			this.pos20 = dato[20];
			this.pos21 = dato[21];
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public MyArray(Object pos1) {
		this.pos1 = pos1;
	}

	public MyArray(Object pos1, Object pos2) {

		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public MyArray(Object pos1, Object pos2, Object pos3) {

		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4,
			Object pos5) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4,
			Object pos5, Object pos6) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4,
			Object pos5, Object pos6, Object pos7) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4,
			Object pos5, Object pos6, Object pos7, Object pos8) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
		this.pos8 = pos8;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4,
			Object pos5, Object pos6, Object pos7, Object pos8, Object pos9) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
		this.pos8 = pos8;
		this.pos9 = pos9;
	}

	public MyArray(Object pos1, Object pos2, Object pos3, Object pos4,
			Object pos5, Object pos6, Object pos7, Object pos8, Object pos9,
			Object pos10) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.pos4 = pos4;
		this.pos5 = pos5;
		this.pos6 = pos6;
		this.pos7 = pos7;
		this.pos8 = pos8;
		this.pos9 = pos9;
		this.pos10 = pos10;
	}

	public Object getPos1() {
		return pos1;
	}

	public void setPos1(Object pos1) {
		this.pos1 = pos1;
	}

	public Object getPos2() {
		return pos2;
	}

	public void setPos2(Object pos2) {
		this.pos2 = pos2;
	}

	public Object getPos3() {
		return pos3;
	}

	public void setPos3(Object pos3) {
		this.pos3 = pos3;
	}

	public Object getPos4() {
		return pos4;
	}

	public void setPos4(Object pos4) {
		this.pos4 = pos4;
	}

	public Object getPos5() {
		return pos5;
	}

	public void setPos5(Object pos5) {
		this.pos5 = pos5;
	}

	public Object getPos6() {
		return pos6;
	}

	public void setPos6(Object pos6) {
		this.pos6 = pos6;
	}

	public Object getPos7() {
		return pos7;
	}

	public void setPos7(Object pos7) {
		this.pos7 = pos7;
	}

	public Object getPos8() {
		return pos8;
	}

	public void setPos8(Object pos8) {
		this.pos8 = pos8;
	}

	public Object getPos9() {
		return pos9;
	}

	public void setPos9(Object pos9) {
		this.pos9 = pos9;
	}

	public Object getPos10() {
		return pos10;
	}

	public void setPos10(Object pos10) {
		this.pos10 = pos10;
	}

	public Object getPos11() {
		return pos11;
	}

	public void setPos11(Object pos11) {
		this.pos11 = pos11;
	}

	public Object getPos12() {
		return pos12;
	}

	public void setPos12(Object pos12) {
		this.pos12 = pos12;
	}

	public Object getPos13() {
		return pos13;
	}

	public void setPos13(Object pos13) {
		this.pos13 = pos13;
	}

	public Object getPos14() {
		return pos14;
	}

	public void setPos14(Object pos14) {
		this.pos14 = pos14;
	}

	public Object getPos15() {
		return pos15;
	}

	public void setPos15(Object pos15) {
		this.pos15 = pos15;
	}

	public Object getPos16() {
		return pos16;
	}

	public void setPos16(Object pos16) {
		this.pos16 = pos16;
	}

	public Object getPos17() {
		return pos17;
	}

	public void setPos17(Object pos17) {
		this.pos17 = pos17;
	}

	public Object getPos18() {
		return pos18;
	}

	public void setPos18(Object pos18) {
		this.pos18 = pos18;
	}

	public Object getPos19() {
		return pos19;
	}

	public void setPos19(Object pos19) {
		this.pos19 = pos19;
	}

	public Object getPos20() {
		return pos20;
	}

	public void setPos20(Object pos20) {
		this.pos20 = pos20;
	}
	
	public Object getPos21() {
		return pos21;
	}

	public void setPos21(Object pos21) {
		this.pos21 = pos21;
	}

	public static void main(String[] args) throws Exception {
		MyArray m = new MyArray();
		
		m.setPos(10,"1000");
		System.out.println(m.getPos(10));
		
	}

}
