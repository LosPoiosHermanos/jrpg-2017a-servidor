package servidor;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import comandos.Comando;
import estados.Estado;
import mensajeria.PaqueteDeMovimientos;
//REVISADO
public class AtencionMovimientos extends Thread {

	private final Gson gson = new Gson();

	public AtencionMovimientos() {

	}

	public void run() {

		synchronized (this) {

			try {

				while (true) {

					// Espero a que se conecte alguien
					wait();

					// Le reenvio la conexion a todos
					for (EscuchaCliente conectado : Servidor.getClientesConectados()) {

						if (conectado.getPaquetePersonaje().getEstado() == Estado.estadoJuego) {

							PaqueteDeMovimientos pdp = (PaqueteDeMovimientos) new PaqueteDeMovimientos(
									Servidor.getUbicacionPersonajes()).clone();
							pdp.setComando(Comando.MOVIMIENTO);
//							synchronized (conectado) {
//								conectado.getSalida().writeObject(gson.toJson(pdp));
//							}
							// fer elimino todo lo de arriba
							 conectado.getSalida().writeObject(gson.toJson(pdp));
						}
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Falló al intentar atender movimientos.");
				e.printStackTrace();
			}
		}
	}
}