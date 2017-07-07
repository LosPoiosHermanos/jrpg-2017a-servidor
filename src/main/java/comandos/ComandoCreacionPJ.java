package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;
//REVISADO
public class ComandoCreacionPJ extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {

		if (comando == Comando.CREACIONPJ) {
			// Casteo el paquete personaje
			escuchaCliente.setPaquetePersonaje((PaquetePersonaje) (gson.fromJson(cadenaLeida, PaquetePersonaje.class)));

			// Guardo el personaje en ese usuario
			Servidor.getConector().registrarPersonaje(escuchaCliente.getPaquetePersonaje(),
					escuchaCliente.getPaqueteUsuario());

			// Le envio el id del personaje
			try {
				escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaquetePersonaje(),
						escuchaCliente.getPaquetePersonaje().getClass()));
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
