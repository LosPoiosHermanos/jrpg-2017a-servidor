package comandos;

import java.io.IOException;

import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import servidor.Servidor;
//REVISADO
public class ComandoConexion extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.CONEXION) {
			escuchaCliente.setPaquetePersonaje( (PaquetePersonaje) (gson.fromJson(cadenaLeida, PaquetePersonaje.class)).clone());

			Servidor.getPersonajesConectados().put(escuchaCliente.getPaquetePersonaje().getId(),
					(PaquetePersonaje) escuchaCliente.getPaquetePersonaje().clone());
			Servidor.getUbicacionPersonajes().put(escuchaCliente.getPaquetePersonaje().getId(),
					(PaqueteMovimiento) new PaqueteMovimiento(escuchaCliente.getPaquetePersonaje().getId()).clone());

			synchronized (Servidor.atencionConexiones) {
				Servidor.atencionConexiones.notify();
			}

		} else {
			try {
				nextServidor.solicitudDelComando(comando);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public NextServidor getNextServidor() {
		return nextServidor;
	}

}
