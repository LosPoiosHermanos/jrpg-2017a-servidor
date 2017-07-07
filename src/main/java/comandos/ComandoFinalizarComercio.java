package comandos;

import java.io.IOException;

import estados.Estado;
import mensajeria.PaqueteFinalizarComercio;
import servidor.EscuchaCliente;
import servidor.Servidor;
//REVISADO
public class ComandoFinalizarComercio extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.FINALIZARCOMERCIO) {
			escuchaCliente.setPaqueteFinalizarComercio((PaqueteFinalizarComercio) gson.fromJson(cadenaLeida,
					PaqueteFinalizarComercio.class));
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarComercio().getId()).setEstado(Estado.estadoJuego);
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarComercio().getIdEnemigo())
					.setEstado(Estado.estadoJuego);
			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				if (conectado.getIdPersonaje() == escuchaCliente.getPaqueteFinalizarComercio().getIdEnemigo()) {
					try {
						conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteFinalizarComercio()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

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
