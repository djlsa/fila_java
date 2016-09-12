package uab.efolioa;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class Fila {
	private static final int MAXIMO_SENHAS_ESPERA = 3;
	private static final HashSet<SenhaTipo> TIPOS_SENHA = new HashSet<SenhaTipo>(Arrays.asList(SenhaTipo.values()));

	private HashMap<SenhaTipo, Integer> numeroSenhasEmEspera = new HashMap<SenhaTipo, Integer>();
	private LinkedHashSet<Balcao> balcoes = new LinkedHashSet<Balcao>();
	private LinkedHashSet<Senha> senhas = new LinkedHashSet<Senha>();
	private Calendar horaAtual = Calendar.getInstance();
	private Calendar tempoAtendimento = Calendar.getInstance();
	private int senhasAtendidas = 0;
	
	public Fila() {
		SenhaTipo[] tiposSenha = SenhaTipo.values();
		for(SenhaTipo tipo : tiposSenha) {
			numeroSenhasEmEspera.put(tipo, 0);
		}
		horaAtual.setTimeInMillis(0);
		tempoAtendimento.setTimeInMillis(0);
	}
	
	public void adicionaBalcoes(Balcao[] balcoes) {
		for(Balcao balcao : balcoes) {
			adicionaBalcao(balcao);
		}
	}

	public void adicionaBalcao(Balcao balcao) {
		balcoes.add(balcao);
	}

	public void removeBalcao(Balcao balcao) {
		balcoes.remove(balcao);
	}

	public boolean adicionaSenha(Senha senha) {
		if(senhas.add(senha)) {
			SenhaTipo tipo = senha.getTipo();
			numeroSenhasEmEspera.put(tipo, numeroSenhasEmEspera.get(tipo) + 1);
			return true;
		}
		return false;
	}
	
	public Senha getProximaSenha(Balcao balcao) {
		@SuppressWarnings("unchecked")
		HashSet<SenhaTipo> tiposSenha = (HashSet<SenhaTipo>) TIPOS_SENHA.clone();
		Iterator<Senha> senhas = this.senhas.iterator();
		while(senhas.hasNext()) {
			Senha senha = senhas.next();
			SenhaTipo tipo = senha.getTipo();
			int senhasEmEspera = numeroSenhasEmEspera.get(tipo);
			if(senhasEmEspera > MAXIMO_SENHAS_ESPERA || balcao.atendeSenhasTipo(tipo)) {
				numeroSenhasEmEspera.put(tipo, senhasEmEspera - 1);
				++senhasAtendidas;
				senha.setHoraAtendimento(horaAtual.getTime());
				tempoAtendimento.add(Calendar.SECOND, (int) senha.getTempoAtendimento().getTime() / 1000);
				senhas.remove();
				System.out.format("%1$tT %2$s -> %3$s\n", horaAtual.getTime(), senha, balcao);
				return senha;
			} else {
				tiposSenha.remove(tipo);
			}
			if(tiposSenha.size() == 0)
				return null;
		}
		return null;
	}

	public void avancaSimulacao(Date hora) {
		if(horaAtual.getTimeInMillis() == 0)
			horaAtual.setTime(hora);
		if(hora.before(horaAtual.getTime())) {
			throw new IllegalArgumentException();
		}
		do {
			for(Balcao balcao : balcoes) {
				balcao.atualiza(horaAtual.getTime());
			}
			if(horaAtual.getTime().before(hora)) {
				horaAtual.add(Calendar.SECOND, 1);
			} else if(senhas.size() == 0) {
				horaAtual.setTime(hora);
			}
		} while(horaAtual.getTime().before(hora));
	}
	
	public LinkedHashSet<Senha> getFila() {
		return senhas;
	}
	
	public Date calculaMedia() {
		return (senhasAtendidas != 0) ? new Date(tempoAtendimento.getTime().getTime() / senhasAtendidas) : new Date(0);
	}
	
	public String toString() {
		return String.format("%1$tT Senhas em espera: %2$s", horaAtual.getTime(), senhas.size()) + ((senhas.size() > 0) ? String.format(" -> %1$s", senhas) : "");
	}
}
