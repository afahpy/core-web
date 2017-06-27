package com.coreweb.util;

import java.util.Comparator;

public class ComparadorArrayObjetos implements Comparator<Object[]> {

	int[] n;

	public ComparadorArrayObjetos(int... param) {
		this.n = param;
	}

	@Override
	public int compare(Object[] o1, Object[] o2) {
		String a = "";
		String b = "";
		for (int i = 0; i < this.n.length; i++) {
			a = a + o1[this.n[i]].toString();
			b = b + o2[this.n[i]].toString();
		}
		return (b.compareTo(a));
	}

}
