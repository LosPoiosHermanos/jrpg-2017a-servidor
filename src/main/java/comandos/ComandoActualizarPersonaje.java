package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;
//REVISADO
public class ComandoActualizarPersonaje extends NextServidor{
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando){
		if (comando == Comando.ACTUALIZARPERSONAJE) {
			
			escuchaCliente.setPaquetePersonaje( (PaquetePersonaje) gson.fromJson(cadenaLeida, PaquetePersonaje.class));
			Servidor.getConector().actualizarPersonaje(escuchaCliente.getPaquetePersonaje());

			Servidor.getPersonajesConectados().remove(escuchaCliente.getPaquetePersonaje().getId());
			Servidor.getPersonajesConectados().put(escuchaCliente.getPaquetePersonaje().getId(), escuchaCliente.getPaquetePersonaje());

			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				try {
					conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaquetePersonaje()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}else {
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
