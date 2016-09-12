package uab.efolioa;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programa {

	private static final Pattern REGEX_COMANDOS = Pattern.compile("(?i)sair|([0-2]*\\d:[0-5]\\d:[0-5]\\d) ([A-Z]\\d{" + Senha.DIGITOS + "}|fila|media)");

	private Fila fila = new Fila();
	private Scanner scanner = new Scanner(System.in);
	private SimpleDateFormat formatoHora = new SimpleDateFormat("H:mm:ss", Locale.UK);

	public Programa() {
		Balcao[] balcoes = new Balcao[] {
			new Balcao("Balcão 1", fila, new SenhaTipo[] { SenhaTipo.A, SenhaTipo.B }),
			new Balcao("Balcão 2", fila, SenhaTipo.C),
			new Balcao("Balcão 3", fila, SenhaTipo.D)
		};
		fila.adicionaBalcoes(balcoes);
	}

	public void lerEProcessarComandos() {
		Matcher partesEntrada;
		Date hora;
		String comando = "";
		do {
			partesEntrada = REGEX_COMANDOS.matcher(scanner.nextLine().trim());
			if(partesEntrada.matches()) {
				comando = partesEntrada.group(2);
				if(comando == null)
					comando = "sair";
				else {
					try {
						hora = formatoHora.parse(partesEntrada.group(1));
						fila.avancaSimulacao(hora);
						switch(comando) {
							case "fila":
								System.out.println(fila);
								break;
							case "media":
								System.out.format("%1$tT Tempo médio de espera: %2$tMm%2$tSs\n", hora, fila.calculaMedia());
								break;
							default:
								try {
									Senha senha = new Senha(SenhaTipo.valueOf("" + comando.charAt(0)), Integer.parseInt(comando.substring(1)), hora);
									if(fila.adicionaSenha(senha)) {
										System.out.format("%1$tT senha %2$s\n", hora, senha);
									} else {
										System.out.println("Senha duplicada");
									}
								} catch(IllegalArgumentException e) {
									System.out.println("Tipo de senha não reconhecido");
								}
						}
					} catch (ParseException e) {
						System.out.println("Erro no formato da hora, tem de ser do tipo hh:mm:ss");
					} catch (IllegalArgumentException e) {
						System.out.println("Erro na hora, é anterior à última introduzida");
					}
				}
			} else {
				System.out.println("Entrada não reconhecida, introduza 'hh:mm:ss comando' ou 'sair'");
			}
		} while(!comando.equals("sair"));
	}

	public Matcher parse(String input) {
		return REGEX_COMANDOS.matcher(input);
	}

	public static void main(String[] args) {
		Programa p = new Programa();
		p.lerEProcessarComandos();
	}
}
