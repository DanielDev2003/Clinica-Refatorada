import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvImplRepository implements RepositoryFile {
    private Prontuario prontuario;

    public CsvImplRepository(Prontuario prontuario){
        this.prontuario = prontuario;
    }

    @Override
    public String salveProntuario() throws IOException{
        List<String> l = new ArrayList<>();

        l.add("nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos");

		String l1 = prontuario.getNomePaciente() + ",";

		if (prontuario.getInternacao() != null) {
			l1 += prontuario.getTipoLeitoEnum() + "," + prontuario.getQtdeDiasInternacao() + ",,";
			l.add(l1);
		}

		if (prontuario.getProcedimentos().size() > 0) {
			Map<TipoProcedimentoEnum, Long> procedimentosAgrupados = prontuario.getProcedimentos().stream().collect(
					Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

			List<TipoProcedimentoEnum> procedimentosOrdenados = new ArrayList<>(procedimentosAgrupados.keySet());
			Collections.sort(procedimentosOrdenados);

			for (TipoProcedimentoEnum chave : procedimentosOrdenados) {
				String l2 = prontuario.getNomePaciente() + ",,," + chave + "," + procedimentosAgrupados.get(chave);
				l.add(l2);
			}
		}

		if (l.size() == 1) {
			l1 += ",,,";
			l.add(l1);
		}

		Path path = Paths.get(prontuario.getNomePaciente().replaceAll(" ", "_").concat(String.valueOf(System.currentTimeMillis())).concat(".csv"));

		Files.write(path, l);

		return path.toString();
    }

    @Override
    public Prontuario carregueProntuario(String arquivoCsv) throws IOException{
        Path path =  Paths.get(arquivoCsv);
        
        Stream<String> linhas = Files.lines(path);
        
		linhas.skip(1).forEach((str) -> {
			
            System.out.println(str);

            String[] dados = str.split(",");

            String nomePaciente = dados[0].trim();

            LeitoFactory leitoFactory = new LeitoFactory();
            OperacaoFactory operacaoFactory = new OperacaoFactory();

            Leito tipoLeito = dados[1] != null && !dados[1].trim().isEmpty() ? leitoFactory.criarInstanciaLeito(dados[1].trim()) : null;

            int qtdeDiasInternacao = dados[2] != null && !dados[2].trim().isEmpty() ? Integer.parseInt(dados[2].trim()) : -1;

            Operacao tipoProcedimento = dados[3] != null && !dados[3].trim().isEmpty() ? operacaoFactory.criarInstanciaOperacao(dados[3].trim()) : null;

            int qtdeProcedimentos = dados.length == 5 && dados[4] != null && !dados[4].trim().isEmpty() ? Integer.parseInt(dados[4].trim()) : -1;

            prontuario.setNomePaciente(nomePaciente);

            if (tipoLeito != null && qtdeDiasInternacao > 0) {
                prontuario.setInternacao(new Internacao(tipoLeito, qtdeDiasInternacao)); 
            }

            if (tipoProcedimento != null && qtdeProcedimentos > 0) {
                while (qtdeProcedimentos > 0) {
                    prontuario.addProcedimento(new Procedimento(tipoProcedimento));
                    qtdeProcedimentos--;
                }
            }
			
		});
        linhas.close();
        return prontuario;  
    };
}

