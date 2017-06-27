package com.coreweb.util;

import com.coreweb.domain.Register;

public class Ruc {

	public boolean validarRuc(String ruc){
		boolean out = false;	
		try {
			String[] valor = ruc.split("-");
			if (Integer.parseInt(valor[1]) == this.calcularDigitoVerificador(valor[0])) {
				out = true;
			}			
		} catch (Exception e) {
			System.out.println("[error] el ruc ["+ruc+"] es invalidao");
		}
		return out;
	}	
	
	/**
	 * Funcion para el calculo del Digito Verificador del Ruc
	 * Fuente: www.set.gov.py
	 * 
	 */
	public int calcularDigitoVerificador(String numero) {
		int p_basemax = 11;
	    int v_total, v_resto, k, v_numero_aux, v_digit;
	    String v_numero_al = "";

	    for (int i = 0; i < numero.length(); i++) {
	            char c = numero.charAt(i);
	            if(Character.isDigit(c)) {
                    v_numero_al += c;
	            } else{
                    v_numero_al += (0+c);
	            }
	    }

	    k = 2;
	    v_total = 0;

	    for(int i = v_numero_al.length() - 1; i >= 0; i--) {
	            k = k > p_basemax ? 2 : k;
	            v_numero_aux = v_numero_al.charAt(i) - 48;
	            v_total += v_numero_aux * k++;
	    }

	    v_resto = v_total % 11;
	    v_digit = v_resto > 1 ? 11 - v_resto : 0;
	    return v_digit;	    
	}

	
	public int calcularDigitoVerificadorOld(String numero) {
		int p_basemax = 10;
	    int v_total, v_resto, k, v_numero_aux, v_digit;
	    String v_numero_al = "";

	    for (int i = 0; i < numero.length(); i++) {
	            char c = numero.charAt(i);
	            if(Character.isDigit(c)) {
	                    v_numero_al += c;
	            } 
	    }

	    k = 2;
	    v_total = 0;

	    for(int i = v_numero_al.length() - 1; i >= 0; i--) {
	            k = k > p_basemax ? 2 : k;
	            v_numero_aux = v_numero_al.charAt(i) - 48;
	            v_total += v_numero_aux * k++;
	    }

	    v_resto = v_total % 11;
	    v_digit = v_resto > 1 ? 11 - v_resto : 0;
	    return v_digit;	    
	}
	
	
	
	public static void main(String[] args) {
		Ruc r = new Ruc();

		
		System.out.println(r.validarRuc("7213746-1"));
		System.out.println(r.validarRuc("7213746-2"));
		System.out.println(r.validarRuc("7213746-3"));
		System.out.println(r.validarRuc("7213746-4"));
		System.out.println(r.validarRuc("7213746-5"));
		System.out.println(r.validarRuc("7213746-6"));
		System.out.println(r.validarRuc("7213746-7"));
		System.out.println(r.validarRuc("7213746-8"));
		System.out.println(r.validarRuc("7213746-9"));
		System.out.println(r.validarRuc("7213746-0"));

		System.out.println("------------");
		for (int i = 0; i < 10; i++) {
			String ruc = "1023860A-"+i;
			System.out.println(ruc + "  "+r.validarRuc(ruc));
		}
		
	}
}