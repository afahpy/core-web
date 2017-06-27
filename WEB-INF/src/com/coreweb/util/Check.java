package com.coreweb.util;

import java.util.Date;

import org.zkoss.lang.Classes;
import org.zkoss.util.Dates;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.CustomConstraint;
import org.zkoss.zul.SimpleConstraint;

import com.coreweb.control.GenericViewModel;

public class Check {

	public static String MENSAJE_NO_EMPTY = "Este campo no debe quedar vacio";
	public static String MENSAJE_NO_FUTURO = "Fecha mayor a la actual no permitida";
	public static String MENSAJE_NO_PRESENTE = "Fecha actual no permitida";
	public static String MENSAJE_NO_PASADO = "Fecha menor a la actual no permitida";
	public static String MENSAJE_NO_CERO = "No se permite valor cero en este campo";
	public static String MENSAJE_NO_POSITIVO = "No se permite valor mayor a cero en este campo";
	public static String MENSAJE_NO_NEGATIVO = "No se permite valor negativo en este campo";
	public static String MENSAJE_MAYOR_A = "El valor debe ser mayor a ";
	public static String MENSAJE_MENOR_A = "El valor debe ser menor a ";
	public static String MENSAJE_MAYOR_IGUAL_A = "El valor debe ser mayor o igual a ";
	public static String MENSAJE_MENOR_IGUAL_A = "El valor debe ser menor o igual a ";
	public static String MENSAJE_RUC = "Ruc no valido\nEl formato es 9999999-9";
	public static String MENSAJE_EMAIL = "Debe Ingresar un correo válido";
	public static String MENSAJE_EMAILS = "Los sgtes correos son inválidos: \n";
	public static String MENSAJE_LONGITUD_MAX = "La longitud máxima permitida es de: \n";
	public static String TRUE_FALSE = "Debe Ingresar \nT para Verdadero, o \nF para Falso ";

	private GenericViewModel vm = null;

	public Check(GenericViewModel vm) {
		this.vm = vm;
	}

	// No admite empty o null
	public MiConstraint getNoVacio() {
		return new MiConstraint(this.vm, MiConstraint.NO_VACIO);
	}

	// No admite fecha mayor a la actual
	public MiConstraint getNoFuturo() {
		return new MiConstraint(this.vm, MiConstraint.NO_FUTURO);
	}

	// No admite fecha menor a la actual
	public MiConstraint getNoPasado() {
		return new MiConstraint(this.vm, MiConstraint.NO_PASADO);
	}

	// No admite fecha actual
	public MiConstraint getNoPresente() {
		return new MiConstraint(this.vm, MiConstraint.NO_PRESENTE);
	}

	// No admite valor mayor a cero
	public MiConstraint getNoPositivo() {
		return new MiConstraint(this.vm, MiConstraint.NO_POSITIVO);
	}

	// No admite valor cero
	public MiConstraint getNoCero() {
		return new MiConstraint(this.vm, MiConstraint.NO_CERO);
	}

	// No admite valores menores a cero
	public MiConstraint getNoNegativo() {
		return new MiConstraint(this.vm, MiConstraint.NO_NEGATIVO);
	}

	// Solo admite valor mayor a ?
	public MiConstraint mayorA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MAYOR_A, value);
	}

	// Solo admite valor menor a ?
	public MiConstraint menorA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MENOR_A, value);
	}

	// Solo admite valor mayor o igual a ?
	public MiConstraint mayorIgualA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MAYOR_IGUAL_A, value);
	}

	// Solo admite valor menor o igual a ?
	public MiConstraint menorIgualA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MENOR_IGUAL_A, value);
	}

	// Valida el digito verificador del ruc
	public MiConstraint getRuc() {
		System.out.println("--------------------- RUC");
		return new MiConstraint(this.vm, MiConstraint.RUC);
	}

	// Valida el formato del correo
	public MiConstraint getEmail() {
		return new MiConstraint(this.vm, MiConstraint.EMAIL);
	}

	// Valida el formato de varios correos separados con ';'
	public MiConstraint getEmails() {
		return new MiConstraint(this.vm, MiConstraint.EMAILS);
	}

	// Valida el formato del correo
	public MiConstraint getTrueFalse() {
		return new MiConstraint(this.vm, MiConstraint.TRUE_FALSE);
	}
	
	// Valida la longitud maxima de caracteres
	public MiConstraint getLongitudMax(int max){
		return new MiConstraint(this.vm, MiConstraint.LONGITUD_MAX, max);
	}

}
