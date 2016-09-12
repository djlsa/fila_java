package uab.efolioa;
import java.util.Calendar;
import java.util.Date;

public enum SenhaTipo {
	A(2),
	B(2),
	C(5),
	D(5);

	private Calendar calendario = Calendar.getInstance();
	private Date tempoAtendimento;
	
	private SenhaTipo(int tempoAtendimento) {
		calendario.setTimeInMillis(0);
		calendario.set(Calendar.MINUTE, tempoAtendimento);
		this.tempoAtendimento = calendario.getTime();
	}
	
	public Date getTempoAtendimento() {
		return tempoAtendimento;
	}
}
