public class Internacao {

	private Leito leito;
	private int qtdeDias;

	public Internacao(Leito leito, int qtdeDias) {
		this.leito = leito;
		this.qtdeDias = qtdeDias;
	}

	Leito getTipoLeito() {
		return this.leito;
	}

	TipoLeitoEnum gTipoLeitoEnum(){
		return this.leito.getTipoLeitoEnum();
	}
	int getQtdeDias() {
		return this.qtdeDias;
	}

	double calcularDiaria(){
		return leito.calcularDiaria(qtdeDias);
	}

}
