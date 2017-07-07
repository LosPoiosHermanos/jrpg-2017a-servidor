package comandos;

import java.io.IOException;

import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;
//REVISADO
public class ComandoInicioSesion extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.INICIOSESION) {
			
			Paquete paqueteSv = new Paquete(null, 0);
			paqueteSv.setComando(Comando.INICIOSESION);

			// Recibo el paquete usuario
			escuchaCliente.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(cadenaLeida, PaqueteUsuario.class)));

			// Si se puede loguear el usuario le envio un mensaje de
			// exito y el paquete personaje con los datos
			try {
				if (Servidor.getConector().loguearUsuario(escuchaCliente.getPaqueteUsuario())) {

					PaquetePersonaje paquetePersonaje = new PaquetePersonaje();
					paquetePersonaje = Servidor.getConector().getPersonaje(escuchaCliente.getPaqueteUsuario());

					paquetePersonaje.setComando(Comando.INICIOSESION);
					paquetePersonaje.setMensaje(Paquete.msjExito);
					escuchaCliente.setIdPersonaje(paquetePersonaje.getId());

					escuchaCliente.getSalida().writeObject(gson.toJson(paquetePersonaje));

				} else {
					paqueteSv.setMensaje(Paquete.msjFracaso);
					escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));
				}
			} catch (IOException e) {
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
