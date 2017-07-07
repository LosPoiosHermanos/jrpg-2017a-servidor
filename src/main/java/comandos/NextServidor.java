package comandos;

import java.io.IOException;

import servidor.EscuchaCliente;
//REVISADO
public abstract class NextServidor extends Comando {
	protected static EscuchaCliente escuchaCliente;

	public abstract void setNextServidor(NextServidor probador);

	public abstract void solicitudDelComando(int comando) throws IOException;

	public abstract NextServidor getNextServidor();
	
	public void setEscuchaCliente(EscuchaCliente escuchaCliente) {
		NextServidor.escuchaCliente = escuchaCliente;
	}
	
}
