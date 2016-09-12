package uab.efolioa;
import java.util.Date;
import java.util.HashSet;

public class Balcao {

	private String nome;
	private Fila fila;
	private HashSet<SenhaTipo> tiposSenha = new HashSet<SenhaTipo>();
	private Senha senhaAtendida = null;
	
	public Balcao(String nome, Fila fila, SenhaTipo[] tiposSenha) {
		this.nome = nome;
		this.fila = fila;
		for(SenhaTipo tipo : tiposSenha) {
			adicionaTipoSenha(tipo);
		}
	}

	public Balcao(String nome, Fila fila, SenhaTipo tipoSenha) {
		this(nome, fila, new SenhaTipo[] { tipoSenha });
	}
	
	public void adicionaTipoSenha(SenhaTipo tipo) {
		tiposSenha.add(tipo);
	}
	
	public void removeTipoSenha(SenhaTipo tipo) {
		tiposSenha.remove(tipo);
	}
	
	public boolean atendeSenhasTipo(SenhaTipo tipo) {
		return tiposSenha.contains(tipo);
	}

	public void atualiza(Date horaAtual) {
		if(senhaAtendida == null || horaAtual.compareTo(senhaAtendida.getHoraDespachada()) >= 0) {
			senhaAtendida = fila.getProximaSenha(this);
		}
	}
	
	public String toString() {
		return nome;
	}
}
