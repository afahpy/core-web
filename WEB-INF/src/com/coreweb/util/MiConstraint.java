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

public class MiConstraint extends SimpleConstraint implements Constraint,
		CustomConstraint {

	public static final int NO_VACIO = 1;
	public static final int NO_FUTURO = 2;
	public static final int NO_PASADO = 3;
	public static final int NO_PRESENTE = 4;
	public static final int NO_POSITIVO = 5;
	public static final int NO_CERO = 6;
	public static final int NO_NEGATIVO = 7;
	public static final int MAYOR_A = 8;
	public static final int MENOR_A = 9;
	public static final int MAYOR_IGUAL_A = 10;
	public static final int MENOR_IGUAL_A = 11;
	public static final int RUC = 12;
	public static final int EMAIL = 13;
	public static final int EMAILS = 14;
	public static final int TRUE_FALSE = 15;
	public static final int LONGITUD_MAX = 16;

	private int constraint = 0;
	private Number value = 0;
	private Misc misc = new Misc();
	private GenericViewModel vm = null;

	public MiConstraint(GenericViewModel vm, int constraint, Number value) {
		super("no empty");
		this.vm = vm;
		this.constraint = constraint;
		this.value = value;
	}

	public MiConstraint(GenericViewModel vm, int constraint) {
		super("no empty");
		this.vm = vm;
		this.constraint = constraint;
	}

	private static int compareTo(Comparable v1, Object v2) {
		return v1.compareTo(v2);
	}

	@Override
	public void validate(Component comp, Object value)
			throws WrongValueException {
		try {
			//System.out.println("============= deshabilitado:"+this.vm.isDeshabilitado()+" ["+value+"]"+comp);
			if (this.vm.isDeshabilitado() == true) {
				return;
			}

			MiValidate(comp, value);

		} catch (WrongValueException ex) {
			/*
			 * if (comp instanceof InputElement){ InputElement im =
			 * (InputElement)comp; String st = im.getStyle();
			 * im.setStyle(st+";border: 1px solid red"); }
			 */

			Clients.showNotification(ex.getMessage(), "error", comp,
					"end_center", 3000, true);

		}

	}

	public void MiValidate(Component comp, Object value)
			throws WrongValueException {

		if (value == null) {
			throw new WrongValueException(comp, Check.MENSAJE_NO_EMPTY);

			// Check de Numeros
		} else if (value instanceof Number) {
			final int cmp = compareTo((Comparable) value,
					Classes.coerce(value.getClass(), null, false)); // compara
																	// con cero
			if (cmp > 0) {
				if (this.constraint == this.NO_POSITIVO) {
					throw new WrongValueException(comp,
							Check.MENSAJE_NO_POSITIVO);
				}
			} else if (cmp == 0) {
				if (this.constraint == this.NO_CERO) {
					throw new WrongValueException(comp, Check.MENSAJE_NO_CERO);
				}
			} else {
				if (this.constraint == this.NO_NEGATIVO) {
					throw new WrongValueException(comp,
							Check.MENSAJE_NO_NEGATIVO);
				}
			}

			if ((this.constraint == this.MAYOR_A)
					&& (misc.compararNumeros((Number) value, this.value) != 1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MAYOR_A
						+ this.value);
			}
			if ((this.constraint == this.MENOR_A)
					&& (misc.compararNumeros((Number) value, this.value) != -1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MENOR_A
						+ this.value);
			}
			if ((this.constraint == this.MAYOR_IGUAL_A)
					&& (misc.compararNumeros((Number) value, this.value) == -1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MAYOR_IGUAL_A
						+ this.value);
			}
			if ((this.constraint == this.MENOR_IGUAL_A)
					&& (misc.compararNumeros((Number) value, this.value) == 1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MENOR_IGUAL_A
						+ this.value);
			}

			// Check de String
		} else if (value instanceof String) {
			String s = (String) value;
			if (this.constraint == this.NO_VACIO && s.isEmpty()) {
				throw new WrongValueException(comp, Check.MENSAJE_NO_EMPTY);
			}
			if (this.constraint == RUC) {
				Ruc ruc = new Ruc();
				// ver si es un ruc valido
				if (ruc.validarRuc(s) == false) {
					throw new WrongValueException(comp, Check.MENSAJE_RUC);
				}

				/*
				 * // ver que no sea un ruc repetido try { Class empresa =
				 * (Class) this.misc.ejecutarMetoto(this.vm, "getRucEmpresa");
				 * IiD id = (IiD) this.misc.ejecutarMetoto(this.vm,
				 * "getIdEmpresa"); Register rr = Register.getInstance(); if
				 * (rr.existe(empresa, "ruc", Config.TIPO_STRING, value, id)) {
				 * throw new WrongValueException(comp,
				 * "El RUC '"+value+"' ya existe"); }
				 * 
				 * } catch (WrongValueException e) { throw e; } catch (Exception
				 * e) { e.printStackTrace(); throw new WrongValueException(comp,
				 * "Error en metodo getRucEmpresa"); }
				 */

			}
			if ((this.constraint == this.EMAIL)
					&& (misc.checkEmail(s) == false)) {
				throw new WrongValueException(comp, Check.MENSAJE_EMAIL);
			}
			if (this.constraint == this.EMAILS) {
				boolean mailsOk = (boolean) misc.chequearMultipleCorreos(s
						.trim().split(";"))[0];
				String mailsMsg = (String) misc.chequearMultipleCorreos(s
						.trim().split(";"))[1];
				if ((mailsOk == false) && (s.trim().length() == 0)) {
					throw new WrongValueException(comp, Check.MENSAJE_EMAILS
							+ mailsMsg);
				}
			}

			if ((this.constraint == this.TRUE_FALSE)
					&& (misc.checkTrueFalse(s) == false)) {
				throw new WrongValueException(comp, Check.TRUE_FALSE);
			}
			
			if (this.constraint == this.LONGITUD_MAX) {
				int max = (Integer) this.value;
				String val = (String) value;
				
				if (val.trim().length() > max) {
					throw new WrongValueException(comp, Check.MENSAJE_LONGITUD_MAX + max);
				}
			}

			// Check de Fechas
		} else if (value instanceof Date) {
			final Date date = Dates.beginOfDate((Date) value, null);
			final int cmp = date.compareTo(Dates.today());
			if (cmp > 0) {
				if (this.constraint == this.NO_FUTURO) {
					throw new WrongValueException(comp, Check.MENSAJE_NO_FUTURO);
				}
			} else if (cmp == 0) {
				if (this.constraint == this.NO_PRESENTE) {
					throw new WrongValueException(comp,
							Check.MENSAJE_NO_PRESENTE);
				}
			} else {
				if (this.constraint == this.NO_PASADO) {
					throw new WrongValueException(comp, Check.MENSAJE_NO_PASADO);
				}
			}
		}
		
//		System.out.println("------- constraint ok");
		
	}

	public void showCustomError(Component comp, WrongValueException ex) {
		if (ex != null) {
			Clients.showNotification(ex.getMessage(), "error", comp,
					"end_center", 3000, true);
		}
	}

}