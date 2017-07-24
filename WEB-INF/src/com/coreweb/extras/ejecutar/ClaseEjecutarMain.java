package com.coreweb.extras.ejecutar;

import java.lang.reflect.Method;

public class ClaseEjecutarMain {

	public static void main(String[] args) {
		try {
			if (args != null && args[0].trim().length() == 0) {
				System.out.println("Debe especificar una clase");
				return;
			}
			// los parámetros no funionan. Ver esto.
			String[] params = new String[args.length - 1];
			for (int i = 1; i < args.length; i++) {
				params[i-1] = args[i];
			}

			Class<?> cls = Class.forName(args[0]);
			Method meth = cls.getMethod("main", String[].class);
			meth.invoke(null, (Object) params); // static method doesn't have an
												// instance
			System.out.println("------------fin");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
