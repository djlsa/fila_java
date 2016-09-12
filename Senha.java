package uab.efolioa;
import java.util.Date;

public class Senha {
	public static final int DIGITOS = 3;

	private SenhaTipo tipo;
	private int numero;
	private Date horaTirada;
	private Date horaAtendimento;
	private Date tempoAtendimento;
	private Date horaDespachada;

	public Senha(SenhaTipo tipo, int numero, Date horaTirada) {
		this.tipo = tipo;
		this.numero = numero;
		this.horaTirada = horaTirada;
	}

	public SenhaTipo getTipo() {
		return tipo;
	}
	
	public Date getHoraTirada() {
		return horaTirada;
	}
	
	public Date getHoraAtendimento() {
		return horaAtendimento;
	}
	
	public void setHoraAtendimento(Date horaAtendimento) {
		this.horaAtendimento = horaAtendimento;
		this.tempoAtendimento = new Date(horaAtendimento.getTime() - horaTirada.getTime());
		this.horaDespachada = new Date(horaAtendimento.getTime() + tipo.getTempoAtendimento().getTime());
	}
	
	public Date getTempoAtendimento() {
		return tempoAtendimento;
	}
	
	public Date getHoraDespachada() {
		return horaDespachada;
	}

	public String toString() {
		return String.format("%1$s%2$0" + DIGITOS + "d", tipo, numero);
	}
}