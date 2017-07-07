package comandos;

import java.io.IOException;

import estados.Estado;
import mensajeria.PaqueteBatalla;
import servidor.EscuchaCliente;
import servidor.Servidor;
//REVISADO
public class ComandoBatalla extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.BATALLA) {

			// Le reenvio al id del personaje batallado que quieren
			// pelear
			escuchaCliente.setPaqueteBatalla((PaqueteBatalla) gson.fromJson(cadenaLeida, PaqueteBatalla.class));

			Servidor.log.append(escuchaCliente.getPaqueteBatalla().getId() + " quiere batallar con "
					+ escuchaCliente.getPaqueteBatalla().getIdEnemigo() + System.lineSeparator());
			try {
				// seteo estado de batalla
				Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatalla().getId())
						.setEstado(Estado.estadoBatalla);
				Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatalla().getIdEnemigo())
						.setEstado(Estado.estadoBatalla);
				escuchaCliente.getPaqueteBatalla().setMiTurno(true);
				escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatalla()));
				for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
					if (conectado.getIdPersonaje() == escuchaCliente.getPaqueteBatalla().getIdEnemigo()) {
						int aux = escuchaCliente.getPaqueteBatalla().getId();
						escuchaCliente.getPaqueteBatalla().setId(escuchaCliente.getPaqueteBatalla().getIdEnemigo());
						escuchaCliente.getPaqueteBatalla().setIdEnemigo(aux);
						escuchaCliente.getPaqueteBatalla().setMiTurno(false);
						conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatalla()));
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (Servidor.atencionConexiones) {
				Servidor.atencionConexiones.notify();
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
