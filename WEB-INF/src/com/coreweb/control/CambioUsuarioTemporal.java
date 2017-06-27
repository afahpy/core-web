package com.coreweb.control;

import org.zkoss.zul.Textbox;

import com.coreweb.Config;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.login.ControlInicio;
import com.coreweb.login.LoginUsuario;
import com.coreweb.login.LoginUsuarioDTO;

public class CambioUsuarioTemporal {

	Control ctr;

	public CambioUsuarioTemporal(Control control) {
		this.ctr = control;
	}

	public void cambioUsuarioTemporal() {

		// si hay un temporal, cambio al original
		if (this.isHayUsuarioTemporal() == true) {
			this.restaurarUsuarioOriginal();
			return;
		}

		// si no hay temporal, entonces pido el nuevo usuario

		try {

			String user = "";
			String pass = "";
			LoginUsuario login = new LoginUsuario();

			boolean seguirCambioUsuarioOk = true;

			Textbox tUser = new Textbox();
			Textbox tPass = new Textbox();
			tPass.setType("password");

			while (seguirCambioUsuarioOk == true) {
				tUser.setValue(user);
				tPass.setValue(pass);

				BodyPopupAceptarCancelar bAC = new BodyPopupAceptarCancelar();
				bAC.setWidthWindows("300px");
				bAC.setHighWindows("200px");
				bAC.addComponente("Usuario", tUser);
				bAC.addComponente("Clave", tPass);
				bAC.showPopup("Cambio Temporar de Usuario");
				if (bAC.isClickAceptar() == true) {
					user = tUser.getValue();
					pass = tPass.getValue();

					LoginUsuarioDTO uDto = login.log(user, pass);

					if (uDto.isLogeado() == true) {

						// guardar el usuario original
						LoginUsuarioDTO uDtoOriginal = this.ctr.getUs();
						this.ctr.setAtributoSession(Config.USUARIO_ORIGINAL,
								uDtoOriginal);

						// nuevo usuario
						this.ctr.setUs(uDto);

						// cambiar el color de las barras
						ControlInicio ci = (ControlInicio) this.ctr
								.getAtributoSession(Config.CONTROL_INICIO);
						ci.setColorBarra(Config.COLOR_BARRA_CAMBIO_USUARIO);

						this.ctr.mensajePopupTemporalWarning("Cambio de Usuario: "
								+ uDto.getNombre());

						seguirCambioUsuarioOk = false;
						//TODO: grabar en el log que hubo un cambio de usuario

					} else {
						this.ctr.mensajePopupTemporalWarning("Usuario o clave incorrecta", 1);
					}

				} else {
					seguirCambioUsuarioOk = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.ctr.mensajeError(e.getMessage());
		}
	}

	public void restaurarUsuarioOriginal() {
		LoginUsuarioDTO uOri = (LoginUsuarioDTO) this.ctr
				.getAtributoSession(Config.USUARIO_ORIGINAL);
		if (uOri != null) {
			this.ctr.setUs(uOri);
			this.ctr.setAtributoSession(Config.USUARIO_ORIGINAL, null);
			
			// restaurar color de la barrar
			ControlInicio co = (ControlInicio)this.ctr.getAtributoSession(Config.CONTROL_INICIO);
			co.setColorBarra(Config.COLOR_BARRA);
			this.ctr.mensajePopupTemporal("Se cambió al usuario: "+uOri.getNombre());
			// TODO: registrar que se volvio al usuario original
		}
	}

	public boolean isHayUsuarioTemporal() {
		LoginUsuarioDTO uOri = (LoginUsuarioDTO) this.ctr
				.getAtributoSession(Config.USUARIO_ORIGINAL);
		return (uOri != null);
	}

}
