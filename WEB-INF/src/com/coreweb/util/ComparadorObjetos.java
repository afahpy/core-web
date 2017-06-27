package com.coreweb.util;

import java.util.Comparator;

import org.zkoss.zul.GroupComparator;

public class ComparadorObjetos implements Comparator, GroupComparator {

	Misc m = new Misc();

	/**
	 * Campos para agrupar
	 */
	String[] campos = {};

	public ComparadorObjetos(String[] campos) {
		this.campos = campos;
	}

	@Override
	public int compare(Object o1, Object o2) {
		int out = 0;
		try {

			String st1 = "";
			String st2 = "";
			for (int i = 0; i < this.campos.length; i++) {
				String campo = this.campos[i];
				st1 += this.m.ejecutarMetoto(o1, campo).toString();
				st2 += this.m.ejecutarMetoto(o2, campo).toString();
			}
			out = st1.compareTo(st2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return out;
	}

	@Override
	public int compareGroup(Object o1, Object o2) {
		if ( this.compare(o1, o2) == 0){
			return 0;
		}
		return 1;
	}

}

/*
 * 
 * package com.coreweb.util;
 * 
 * import java.util.Comparator;
 * 
 * public class ComparadorArrayObjetos implements Comparator<Object[]> {
 * 
 * int[] n;
 * 
 * public ComparadorArrayObjetos(int... param) { this.n = param; }
 * 
 * @Override public int compare(Object[] o1, Object[] o2) { String a = "";
 * String b = ""; for (int i = 0; i < this.n.length; i++) { a = a +
 * o1[this.n[i]].toString(); b = b + o2[this.n[i]].toString(); } return
 * (b.compareTo(a)); }
 * 
 * }
 */