package comandos;

import java.io.IOException;

import mensajeria.PaqueteMovimiento;
import servidor.Servidor;
//REVISADO
public class ComandoMovimiento extends NextServidor {
	private NextServidor nextServidor;

	public void setNextServidor(NextServidor proximo) {
		nextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		if (comando == Comando.MOVIMIENTO) {
			escuchaCliente.setPaqueteMovimiento(
					(PaqueteMovimiento) (gson.fromJson((String) cadenaLeida, PaqueteMovimiento.class)));

			Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setPosX(escuchaCliente.getPaqueteMovimiento().getPosX());
			Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setPosY(escuchaCliente.getPaqueteMovimiento().getPosY());
			Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setDireccion(escuchaCliente.getPaqueteMovimiento().getDireccion());
			Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setFrame(escuchaCliente.getPaqueteMovimiento().getFrame());

			synchronized (Servidor.atencionMovimientos) {
				Servidor.atencionMovimientos.notify();
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
