package com.coreweb.extras.reporte.prueba;

import com.coreweb.extras.reporte.ReporteSimple;

import net.sf.dynamicreports.report.builder.component.DimensionComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.exception.DRException;

public class PruebaReporteSimple extends ReporteSimple {

	void createBody() {
		this.copias = 3;

		VerticalListBuilder vv = cmp.verticalList();

		for (int i = 0; i < 1; i++) {
			vv.add(this.textoNegritaDerecha(i + "  textooooooo"));
			vv.add(this.textoParValor("par", "valor"));
		}

		this.body = vv;
	}

	public void creaFactura() {
		Dato d = new Dato();
		this.creaFactura(d);
	}

	
	public void creaFactura(Dato d) {
		// cuántas copias de lo mismo, en este caso son 3
		this.copias = 3;
		// la altura de cada copia (no se si funciona bien)
		this.altura = 800;

		/*
		 * Hay que crear un VerticalListBuilder e ir poniendo linea por línea lo que se quere.
		 * A cada línea se le dice la altura que se quiere, y se pone dentro un HorizontalListBuilder
		 * para ir poniendo el texto. En el horizontal se dice el espacio hasta el texto, y para cada texto
		 * el espacio máximo, de esa forma dos palabras abccdefgaij y abc ocupan el mismo ancho.
		 * De esta forma se condtruye la factura.
		 * Al final, el VerticalListBuilder inicial se le asigna a this.body y listo.
		 */
		
		
		VerticalListBuilder tt = cmp.verticalList();

		// ---------------------------------------------------------------
		HorizontalListBuilder r1 = cmp.horizontalList().setFixedHeight(50);
		r1.add(espacioAncho(300));
		r1.add(texto(d.numeroFactura));
		tt.add(r1);
		// ---------------------------------------------------------------
		HorizontalListBuilder r2 = cmp.horizontalList().setFixedHeight(20);
		r2.add(espacioAncho(0));
		r2.add(textoAncho(d.fechaFactura, 100));
		r2.add(espacioAncho(0));
		r2.add(textoAncho(d.contado, 10));
		r2.add(espacioAncho(0));
		r2.add(textoAncho(d.credito, 10));
		tt.add(r2);

		tt.add(r2);
		tt.add(r2);
		tt.add(r2);
		tt.add(r2);
		
	
		this.body = tt;

	}

	public static void main(String[] args) throws DRException {
		PruebaReporteSimple r = new PruebaReporteSimple();
		Dato d = new Dato();
		r.creaFactura(d);
		// r.createBody();
		r.show(true);
	}

	@Override
	public void setDatosReportes() {
		// TODO Auto-generated method stub
//		Dato d = new Dato();
//		this.creaFactura(d);
		
	}
}

class Dato {

	String numeroFactura = "1234567";
	String fechaFactura = "16-04-2016";
	String contado = "X";
	String credito = " ";
	String razonSocial = "Vidrio Sociedad Anónima del Paraguay";
	String ruc = "800123321-9";

}