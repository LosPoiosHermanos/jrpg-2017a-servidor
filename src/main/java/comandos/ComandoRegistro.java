package comandos;

import java.io.IOException;

import mensajeria.Paquete;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;
//REVISADO
public class ComandoRegistro extends NextServidor {

	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {

		if (comando == Comando.REGISTRO) {
			Paquete paqueteSv = new Paquete(null, 0);
			// Paquete que le voy a enviar al usuario
			paqueteSv.setComando(Comando.REGISTRO);

			escuchaCliente.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(cadenaLeida, PaqueteUsuario.class)).clone());
			try {
				// Si el usuario se pudo registrar le envio un msj de exito
				if (Servidor.getConector().registrarUsuario(escuchaCliente.getPaqueteUsuario())) {
					paqueteSv.setMensaje(Paquete.msjExito);
					escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));
					// Si el usuario no se pudo registrar le envio un msj de
					// fracaso
				} else {
					paqueteSv.setMensaje(Paquete.msjFracaso);
					escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				nextServidor.solicitudDelComando(comando);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public NextServidor getNextServidor() {
		return nextServidor;
	}

}
