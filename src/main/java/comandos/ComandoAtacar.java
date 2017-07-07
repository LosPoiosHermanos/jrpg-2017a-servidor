package comandos;

import java.io.IOException;

import mensajeria.PaqueteAtacar;
import servidor.EscuchaCliente;
import servidor.Servidor;
//REVISADO
public class ComandoAtacar extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.ATACAR) {
			escuchaCliente.setPaqueteAtacar((PaqueteAtacar) gson.fromJson(cadenaLeida, PaqueteAtacar.class));
			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				if (conectado.getIdPersonaje() == escuchaCliente.getPaqueteAtacar().getIdEnemigo()) {
					try {
						conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteAtacar()));
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
