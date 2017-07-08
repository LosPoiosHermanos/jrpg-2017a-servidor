package servidor;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;

import comandos.ChainOfResponsability;
import comandos.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteAtacar;
import mensajeria.PaqueteBatalla;
import mensajeria.PaqueteChat;
import mensajeria.PaqueteComercio;
import mensajeria.PaqueteDeMovimientos;
import mensajeria.PaqueteDePersonajes;
import mensajeria.PaqueteFinalizarBatalla;
import mensajeria.PaqueteFinalizarComercio;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteTrueque;
import mensajeria.PaqueteUsuario;
//REVISADO
public class EscuchaCliente extends Thread {

	private final Socket socket;
	private final ObjectInputStream entrada;
	private final ObjectOutputStream salida;
	private int idPersonaje;
	private final Gson gson = new Gson();

	private PaquetePersonaje paquetePersonaje;
	private PaqueteMovimiento paqueteMovimiento;
	private PaqueteBatalla paqueteBatalla;
	private PaqueteAtacar paqueteAtacar;
	private PaqueteFinalizarBatalla paqueteFinalizarBatalla;
	private PaqueteUsuario paqueteUsuario;
	private PaqueteTrueque paqueteTrueque;
	private PaqueteFinalizarComercio paqueteFinalizarComercio;
	private PaqueteDeMovimientos paqueteDeMovimiento;
	private PaqueteDePersonajes paqueteDePersonajes;

	private PaqueteComercio paqueteComercio;
	private PaqueteChat paqueteChat;

	public EscuchaCliente(String ip, Socket socket, ObjectInputStream entrada, ObjectOutputStream salida) throws IOException{
		this.socket = socket;
		this.entrada = entrada;
		this.salida = salida;
		paquetePersonaje = new PaquetePersonaje();
	}

	public void run() {
		try {

			Paquete paquete;
			paqueteUsuario = new PaqueteUsuario();
			ChainOfResponsability chain = new ChainOfResponsability();

			String cadenaLeida = (String) entrada.readObject();

			while (!((paquete = gson.fromJson(cadenaLeida, Paquete.class)).getComando() == Comando.DESCONECTAR)) {

				chain.setCadena(cadenaLeida);
				chain.setEscuchaCliente(this);
				chain.solicitudDelComando(paquete.getComando());

				cadenaLeida = (String) entrada.readObject();
			}

			entrada.close();
			salida.close();
			socket.close();

			Servidor.getPersonajesConectados().remove(paquetePersonaje.getId());
			Servidor.getUbicacionPersonajes().remove(paquetePersonaje.getId());
			Servidor.getClientesConectados().remove(this);

			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				paqueteDePersonajes = new PaqueteDePersonajes(Servidor.getPersonajesConectados());
				paqueteDePersonajes.setComando(Comando.CONEXION);
				conectado.salida.writeObject(gson.toJson(paqueteDePersonajes, PaqueteDePersonajes.class));
			}

			Servidor.log.append(paquete.getIp() + " se ha desconectado." + System.lineSeparator());

		} catch (IOException | ClassNotFoundException e) {
			Servidor.log.append("Error de conexion: " + e.getMessage() + System.lineSeparator());
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getEntrada() {
		return entrada;
	}

	public ObjectOutputStream getSalida() {
		return salida;
	}

	public PaquetePersonaje getPaquetePersonaje() {
		return paquetePersonaje;
	}

	public int getIdPersonaje() {
		return idPersonaje;
	}

	public PaqueteMovimiento getPaqueteMovimiento() {
		return paqueteMovimiento;
	}

	public void setPaqueteMovimiento(PaqueteMovimiento paqueteMovimiento) {
		this.paqueteMovimiento = paqueteMovimiento;
	}

	public PaqueteBatalla getPaqueteBatalla() {
		return paqueteBatalla;
	}

	public void setPaqueteBatalla(PaqueteBatalla paqueteBatalla) {
		this.paqueteBatalla = paqueteBatalla;
	}

	public PaqueteAtacar getPaqueteAtacar() {
		return paqueteAtacar;
	}

	public void setPaqueteAtacar(PaqueteAtacar paqueteAtacar) {
		this.paqueteAtacar = paqueteAtacar;
	}

	public PaqueteFinalizarBatalla getPaqueteFinalizarBatalla() {
		return paqueteFinalizarBatalla;
	}

	public void setPaqueteFinalizarBatalla(PaqueteFinalizarBatalla paqueteFinalizarBatalla) {
		this.paqueteFinalizarBatalla = paqueteFinalizarBatalla;
	}

	public PaqueteDeMovimientos getPaqueteDeMovimiento() {
		return paqueteDeMovimiento;
	}

	public void setPaqueteDeMovimiento(PaqueteDeMovimientos paqueteDeMovimiento) {
		this.paqueteDeMovimiento = paqueteDeMovimiento;
	}

	public PaqueteDePersonajes getPaqueteDePersonajes() {
		return paqueteDePersonajes;
	}

	public void setPaqueteDePersonajes(PaqueteDePersonajes paqueteDePersonajes) {
		this.paqueteDePersonajes = paqueteDePersonajes;
	}

	public void setIdPersonaje(int idPersonaje) {
		this.idPersonaje = idPersonaje;
	}

	public void setPaquetePersonaje(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}

	public PaqueteUsuario getPaqueteUsuario() {
		return paqueteUsuario;
	}

	public void setPaqueteUsuario(PaqueteUsuario paqueteUsuario) {
		this.paqueteUsuario = paqueteUsuario;
	}

	public PaqueteTrueque getPaqueteTrueque() {
		return paqueteTrueque;
	}

	public void setPaqueteTrueque(PaqueteTrueque paqueteTrueque) {
		this.paqueteTrueque = paqueteTrueque;
	}

	public PaqueteFinalizarComercio getPaqueteFinalizarComercio() {
		return paqueteFinalizarComercio;
	}

	public void setPaqueteFinalizarComercio(PaqueteFinalizarComercio paqueteFinalizarComercio) {
		this.paqueteFinalizarComercio = paqueteFinalizarComercio;
	}

	public PaqueteComercio getPaqueteComercio() {
		return paqueteComercio;
	}

	public void setPaqueteComercio(PaqueteComercio paqueteComercio) {
		this.paqueteComercio = paqueteComercio;
	}

	public Gson getGson() {
		return gson;
	}

	public PaqueteChat getPaqueteChat() {
		return paqueteChat;
	}

	public void setPaqueteChat(PaqueteChat paqueteChat) {
		this.paqueteChat = paqueteChat;
	}
	
}
