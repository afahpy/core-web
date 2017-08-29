package com.coreweb.templateABM;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zk.ui.event.KeyEvent;

import com.coreweb.Config;
import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.DTO;


public class Page extends GenericViewModel {

	private Body body;
	private Toolbar tool;
	private Footer footer;
	private String aliasABMx = "--AliasTemplateABM--";
	private Div bodyMask;

	private String textoEnmascarar = "ANULADO";

	@Init(superclass = true)
	public void initPage() {

	}

	@AfterCompose(superclass = true)
	public void afterComposePage(
			@ContextParam(ContextType.EXECUTION) Execution execution,
			@QueryParam("body") String bodyUrl,
			@QueryParam("aliasForm") String aliasForm) {
		try {
			if (bodyUrl == null) {
				bodyUrl = (String) execution.getAttribute("body");
				aliasForm = (String) execution.getAttribute("aliasForm");
			}

			String labelF = this.getUs().formLabel(aliasForm);
			this.setTextoFormularioCorriente(labelF);
			this.setAliasABM(aliasForm);

			Include body = (Include) this.mainComponent.getFellow("body");
			String oldUrl = body.getSrc();
			body.setSrc(bodyUrl);

			this.bodyMask = (Div) this.mainComponent.getFellow("bodyMask");

		} catch (Exception ex) {
			System.out.println("error al incluir url del body:" + bodyUrl
					+ "\n exception:" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	
	@Command @NotifyChange("*")
	public void ctrlKeyClick(@BindingParam("key") String key) throws Exception{
		int keyCode = Integer.parseInt(key);
		
		switch (keyCode) {
		
		case KeyEvent.F1 : this.getBody().clickF1();
			break;
		
		case KeyEvent.F2 : this.getBody().clickF2();
			break;
		
		case KeyEvent.F3 : this.getBody().clickF3();
			break;
		
		case KeyEvent.F4 : this.getBody().clickF4();
			break;
		
		case KeyEvent.F5 : this.getBody().clickF5();
			break;
		
		case KeyEvent.F6 : this.getBody().clickF6();
			break;
		
		case KeyEvent.F7 : this.getBody().clickF7();
			break;
		
		case KeyEvent.F8 : this.getBody().clickF8();
			break;
		
		case KeyEvent.F9 : this.getBody().clickF9();
			break;
		
		case KeyEvent.F10 : this.getBody().clickF10();
			break;
		
		case KeyEvent.F11 : this.getBody().clickF11();
			break;
		
		case KeyEvent.F12 : this.getBody().clickF12();
			break;
		
		case Config.CTRL_A : this.getBody().clickCtrA();			
			break;

		case Config.CTRL_B : this.getBody().clickCtrB();
			break;
					
		case Config.CTRL_F : this.getBody().clickCtrF();
			break;
		
		case Config.CTRL_I : this.getBody().clickCtrI();
			break;
		
		case Config.CTRL_L : this.getBody().clickCtrL();
			break;
		
		case Config.CTRL_S : this.getBody().clickCtrS();
			break;
			
		case Config.CTRL_U : this.getBody().clickCtrU();
			break;
			
		case Config.CTRL_X : this.getBody().clickCtrX();
			break;
		
		case Config.CTRL_Y : this.getBody().clickCtrY();
			break;
		}
	}
	
	
	public void grabarDTOCorriente() throws Exception {
		if (this.body.isGrabadoAlternativo() == true){
			this.body.metodoGrabadoAlternativo();
		}else{
			DTO dtoCC = this.body.getDTOCorriente();
			dtoCC = this.saveDTO(dtoCC);
			this.body.setDTOCorriente(dtoCC);
			this.body.afterSave();
			this.body.setDTOCorrienteDirty(dtoCC);
		}
		
	}
	
	public DTO getDto(){
		return this.getDTOCorriente();
	}

	public void borrarDTOCorriente() {
		DTO dtoCC = this.body.getDTOCorriente();
		try {
			this.deleteDTO(dtoCC);
			this.getBody().setDTOCorrienteDirty(this.getBody().nuevoDTO());
		} catch (Exception e) {
			System.out.println("Error borrando DTO: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public DTO getDTOCorriente() {
		return this.body.getDTOCorriente();
	}

	public void refreshDTOCorriente() throws Exception {
		DTO dtoAux;
		if (this.getDTOCorriente().esNuevo() == true) {
			dtoAux = this.getBody().nuevoDTO();
		} else {
			long id = this.getDTOCorriente().getId();
			String entidad = this.getBody().getEntidadPrincipal();
			dtoAux = this.getDTOById(entidad, id + "");
		}
		this.getBody().setDTOCorrienteDirty(dtoAux);

	}

	public String getAliasFormularioCorriente() {
		if (this.getBody() == null) {
			return Config.ALIAS_HABILITADO_SI_O_SI;
		}
		return super.getAliasFormularioCorriente();// this.aliasABM;
	}

	public void setAliasABM(String aliasABM) {
		this.setAliasFormularioCorriente(aliasABM);
	}

	public String getTextoEnmascarar() {
		return textoEnmascarar;
	}

	public void setTextoEnmascarar(String textoEnmascarar) {
		this.textoEnmascarar = textoEnmascarar;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Toolbar getTool() {
		return tool;
	}

	public void setTool(Toolbar tool) {
		this.tool = tool;
	}

	public Footer getFooter() {
		return footer;
	}

	public void setFooter(Footer footer) {
		this.footer = footer;
	}

	public Div getBodyMask() {
		return bodyMask;
	}

	public void setBodyMask(Div bodyMask) {
		this.bodyMask = bodyMask;
	}

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return true;
	}

}
