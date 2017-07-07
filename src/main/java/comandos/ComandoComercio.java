package comandos;

import java.io.IOException;

import estados.Estado;
import mensajeria.PaqueteComercio;
import servidor.EscuchaCliente;
import servidor.Servidor;
//REVISADO
public class ComandoComercio extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.COMERCIO) {
			// Le reenvio al id del personaje que quieren comerciar
			escuchaCliente.setPaqueteComercio((PaqueteComercio) gson.fromJson(cadenaLeida, PaqueteComercio.class));
			Servidor.log.append(escuchaCliente.getPaqueteComercio().getId() + " quiere comerciar con "
					+ escuchaCliente.getPaqueteComercio().getIdEnemigo() + System.lineSeparator());

			// seteo estado de comercio
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteComercio().getId())
					.setEstado(Estado.estadoComercio);
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteComercio().getIdEnemigo())
					.setEstado(Estado.estadoComercio);
			escuchaCliente.getPaqueteComercio().setMiTurno(true);
			try {
				escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteComercio()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				if (conectado.getIdPersonaje() == escuchaCliente.getPaqueteComercio().getIdEnemigo()) {
					int aux = escuchaCliente.getPaqueteComercio().getId();
					escuchaCliente.getPaqueteComercio().setId(escuchaCliente.getPaqueteComercio().getIdEnemigo());
					escuchaCliente.getPaqueteComercio().setIdEnemigo(aux);
					escuchaCliente.getPaqueteComercio().setMiTurno(false);
					try {
						conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteComercio()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
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
