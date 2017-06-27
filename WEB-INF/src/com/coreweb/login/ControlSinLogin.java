package com.coreweb.login;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;

public class ControlSinLogin extends Control {

	public ControlSinLogin(){
		super(null);
	}

	public ControlSinLogin(Assembler ass) {
		super(ass);
		// TODO Auto-generated constructor stub
	}

	@Init(superclass = true)
	public void initControlSinLogin() throws Exception{
		
		
		String user = Executions.getCurrent().getParameter("usuario");
		String clave = Executions.getCurrent().getParameter("clave");
		
		System.out.println("**********Usuario:"+user+"   clave:"+clave);
		
		LoginUsuario login = new LoginUsuario();
		LoginUsuarioDTO uDto = login.log(user, clave);
	
		this.setUs(uDto);
		this.setAliasFormularioCorriente(Config.ALIAS_HABILITADO_SI_O_SI);

	}
	
}
