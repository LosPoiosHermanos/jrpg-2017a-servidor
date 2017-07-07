package comandos;

import java.io.IOException;
//REVISADO
public class ChainOfResponsability extends NextServidor {
	private NextServidor NextServidor;

	public void setNextServidor(NextServidor proximo) {
		NextServidor = proximo;
	}

	@Override
	public void solicitudDelComando(int comando) {
		ComandoRegistro registro = new ComandoRegistro();
		this.setNextServidor(registro);

		ComandoCreacionPJ creacionPJ = new ComandoCreacionPJ();
		registro.setNextServidor(creacionPJ);

		ComandoInicioSesion inicionSesion = new ComandoInicioSesion();
		creacionPJ.setNextServidor(inicionSesion);

		ComandoSalir salir = new ComandoSalir();
		inicionSesion.setNextServidor(salir);

		ComandoConexion conexion = new ComandoConexion();
		salir.setNextServidor(conexion);

		ComandoMovimiento movimiento = new ComandoMovimiento();
		conexion.setNextServidor(movimiento);

		ComandoMostrarMapas mostrarMapas = new ComandoMostrarMapas();
		movimiento.setNextServidor(mostrarMapas);

		ComandoBatalla batalla = new ComandoBatalla();
		mostrarMapas.setNextServidor(batalla);

		ComandoAtacar atacar = new ComandoAtacar();
		batalla.setNextServidor(atacar);

		ComandoFinalizarBatalla finalizarBatalla = new ComandoFinalizarBatalla();
		atacar.setNextServidor(finalizarBatalla);

		ComandoActualizarPersonaje actualizarPersonaje = new ComandoActualizarPersonaje();
		finalizarBatalla.setNextServidor(actualizarPersonaje);

		ComandoComercio comercio = new ComandoComercio();
		actualizarPersonaje.setNextServidor(comercio);

		ComandoTrueque trueque = new ComandoTrueque();
		comercio.setNextServidor(trueque);

		ComandoFinalizarComercio finalizarComercio = new ComandoFinalizarComercio();
		trueque.setNextServidor(finalizarComercio);
		
		ComandoEnviarChat enviarChat = new ComandoEnviarChat();
		finalizarComercio.setNextServidor(enviarChat);

		try {
			NextServidor.solicitudDelComando(comando);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public NextServidor getNextServidor() {
		return NextServidor;
	}

}
