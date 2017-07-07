package comandos;

import java.io.IOException;

import mensajeria.PaqueteTrueque;
import servidor.EscuchaCliente;
import servidor.Servidor;
//REVISADO
public class ComandoTrueque extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.TRUEQUE) {
			escuchaCliente.setPaqueteTrueque( (PaqueteTrueque) gson.fromJson(cadenaLeida, PaqueteTrueque.class));
			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				if (conectado.getIdPersonaje() == escuchaCliente.getPaqueteTrueque().getIdEnemigo()) {
					try {
						conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteTrueque()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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
