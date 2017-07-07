package comandos;

import java.io.IOException;

import mensajeria.Paquete;
import servidor.Servidor;
//REVISADO
public class ComandoSalir extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.SALIR) {

			// Cierro todo
			try {
				escuchaCliente.getEntrada().close();
				escuchaCliente.getSalida().close();
				escuchaCliente.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Lo elimino de los clientes conectados
			Servidor.getClientesConectados().remove(this);
			Paquete paquete = (Paquete) gson.fromJson(cadenaLeida, Paquete.class);
			// Indico que se desconecto
			Servidor.log.append(paquete.getIp() + " se ha desconectado." + System.lineSeparator());

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
