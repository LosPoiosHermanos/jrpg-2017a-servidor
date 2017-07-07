package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;
//REVISADO
public class ComandoMostrarMapas extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		
		
		if (comando == Comando.MOSTRARMAPAS) {

			// Indico en el log que el usuario se conecto a ese mapa
			escuchaCliente.setPaquetePersonaje( (PaquetePersonaje) gson.fromJson(cadenaLeida, PaquetePersonaje.class));
			Servidor.log.append(escuchaCliente.getSocket().getInetAddress().getHostAddress() + " ha elegido el mapa "
					+ escuchaCliente.getPaquetePersonaje().getMapa() + System.lineSeparator());

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
