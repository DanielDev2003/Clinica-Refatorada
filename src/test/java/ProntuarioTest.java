import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProntuarioTest {

	@After
	public void cleanUp() {

		DirectoryStream<Path> stream = null;
		try {
			stream = Files.newDirectoryStream(Paths.get("."), "*.csv");
			for (Path path : stream) {
				Files.delete(path);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
	}

	@Test
	public void testSomenteProcedimentos() {
		Prontuario prontuario = new Prontuario("Paul McCartney");

		prontuario.addProcedimento(new Procedimento(new Basica()));
		prontuario.addProcedimento(new Procedimento(new Avancado()));

		Impressao impressao = new ImpressaoConsole(prontuario);
		final String respostaEsperada = "----------------------------------------------------------------------------------------------" +
				"\nA conta do(a) paciente Paul McCartney tem valor total de __ R$ 550,00 __" +
				"\n" +
				"\nConforme os detalhes abaixo:" +
				"\n" +
				"\nValor Total Procedimentos:		R$ 550,00" +
				"\n					1 procedimento básico" +
				"\n					1 procedimento avançado" +
				"\n" +
				"\nVolte sempre, a casa é sua!" +
				"\n----------------------------------------------------------------------------------------------";

		assertEquals(respostaEsperada, impressao.imprimaConta());
	}

	@Test
	public void testInternacaoComProcedimentos() {
		Prontuario prontuario = new Prontuario("Nando Reis");
		prontuario.setInternacao(new Internacao(new Apartamento(), 4));

		prontuario.addProcedimento(new Procedimento(new Basica()));
		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Avancado()));

		Impressao impressao = new ImpressaoConsole(prontuario);

		final String respostaEsperada = "----------------------------------------------------------------------------------------------" +
				"\nA conta do(a) paciente Nando Reis tem valor total de __ R$ 1.210,00 __" +
				"\n" +
				"\nConforme os detalhes abaixo:" +
				"\n" +
				"\nValor Total Diárias:			R$ 360,00" +
				"\n					4 diárias em apartamento" +
				"\n" +
				"\nValor Total Procedimentos:		R$ 850,00" +
				"\n					1 procedimento básico" +
				"\n					2 procedimentos comuns" +
				"\n					1 procedimento avançado" +
				"\n" +
				"\nVolte sempre, a casa é sua!" +
				"\n----------------------------------------------------------------------------------------------";

		assertEquals(respostaEsperada, impressao.imprimaConta());
	}

	@Test
	public void testSomenteInternacao() {
		Prontuario prontuario = new Prontuario("MC Criolo");
		prontuario.setInternacao(new Internacao(new Enfermaria(), 1));
		Impressao impressao = new ImpressaoConsole(prontuario);
		final String respostaEsperada = "----------------------------------------------------------------------------------------------" +
				"\nA conta do(a) paciente MC Criolo tem valor total de __ R$ 40,00 __" +
				"\n" +
				"\nConforme os detalhes abaixo:" +
				"\n" +
				"\nValor Total Diárias:			R$ 40,00" +
				"\n					1 diária em enfermaria" +
				"\n" +
				"\nVolte sempre, a casa é sua!" +
				"\n----------------------------------------------------------------------------------------------";

		assertEquals(respostaEsperada, impressao.imprimaConta());
	}

	@Test
	public void testCarregarArquivoSemInternacao() {
		String path = "src/test/resources/prontuario_exportado_sem_internacao.csv";

		Prontuario prontuario = null;
		try {
			prontuario = new Prontuario(null);
			RepositoryFile csv = new CsvImplRepository(prontuario);
			csv.carregueProntuario(path);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		assertEquals("Ermenegildo Godofredo", prontuario.getNomePaciente());
		assertNull(prontuario.getInternacao());

		Map<TipoProcedimentoEnum, Long> procedimentosAgrupados = prontuario.getProcedimentos().stream().collect(
				Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

		assertEquals(10L, procedimentosAgrupados.get(TipoProcedimentoEnum.BASICO).longValue());
		assertEquals(2L, procedimentosAgrupados.get(TipoProcedimentoEnum.COMUM).longValue());
		assertNull(procedimentosAgrupados.get(TipoProcedimentoEnum.AVANCADO));
	}

	@Test
	public void testCarregarArquivoSemProcedimentos() {
		String path = "src/test/resources/prontuario_exportado_sem_procedimentos.csv";

		Prontuario prontuario = null;

		try {
			prontuario = new Prontuario(null);
			RepositoryFile csv = new CsvImplRepository(prontuario);
			csv.carregueProntuario(path);
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}

		assertEquals("Ruither Silveira", prontuario.getNomePaciente());
		assertEquals(0, prontuario.getProcedimentos().size());
		Internacao internacao = prontuario.getInternacao();
		assertEquals(10, internacao.getQtdeDias());
		assertEquals(TipoLeitoEnum.APARTAMENTO, internacao.gTipoLeitoEnum());
	}

	@Test
	public void testCarregarArquivoCompleto() {
		String path = "src/test/resources/prontuario_exportado_completo.csv";

		Prontuario prontuario = null;

		try {
			prontuario = new Prontuario(null);
			RepositoryFile csv = new CsvImplRepository(prontuario);
			csv.carregueProntuario(path);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		assertEquals("Adalgisa da Silva", prontuario.getNomePaciente());
		Internacao internacao = prontuario.getInternacao();
		assertEquals(20, internacao.getQtdeDias());
		assertEquals(TipoLeitoEnum.ENFERMARIA, internacao.gTipoLeitoEnum());

		Map<TipoProcedimentoEnum, Long> procedimentosAgrupados = prontuario.getProcedimentos().stream().collect(
				Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

		assertEquals(20L, procedimentosAgrupados.get(TipoProcedimentoEnum.BASICO).longValue());
		assertEquals(15L, procedimentosAgrupados.get(TipoProcedimentoEnum.AVANCADO).longValue());
		assertNull(procedimentosAgrupados.get(TipoProcedimentoEnum.COMUM));
	}
	


	@Test
	public void testSalvarProntuarioVazio() {
		Prontuario prontuario = new Prontuario("Sebastião Junior");
		String path = null;
		RepositoryFile csv = new CsvImplRepository(prontuario);
		try {
			path = csv.salveProntuario();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}

		String ln = System.lineSeparator();
		String conteudoEsperado = 
			"nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos" + ln +
			"Sebastião Junior,,,," + ln;

		assertNotNull(path);

		Path file = Paths.get(path);

		String conteudoObtido = null;

		try {
			conteudoObtido = new String(Files.readAllBytes(file));
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}

		assertTrue(Files.exists(file));
		assertEquals(conteudoEsperado, conteudoObtido);
	}

	@Test
	public void testSalvarProntuarioSomenteInternacao() {
		Prontuario prontuario = new Prontuario("Michael Phelps");
		
		prontuario.setInternacao(new Internacao(new Apartamento(), 50));
		String path = null;
		RepositoryFile csv = new CsvImplRepository(prontuario);
		try {
			path = csv.salveProntuario();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}
		String ln = System.lineSeparator();
		String conteudoEsperado = 
			"nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos" + ln +
			"Michael Phelps,APARTAMENTO,50,," + ln;

		assertNotNull(path);

		Path file = Paths.get(path);

		String conteudoObtido = null;

		try {
			conteudoObtido = new String(Files.readAllBytes(file));
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}

		assertTrue(Files.exists(file));
		assertEquals(conteudoEsperado, conteudoObtido);
	}

	@Test
	public void testSalvarProntuarioSomenteProcedimentos() {
		Prontuario prontuario = new Prontuario("Richard Stallman");

		prontuario.addProcedimento(new Procedimento(new Avancado()));
		prontuario.addProcedimento(new Procedimento(new Avancado()));
		prontuario.addProcedimento(new Procedimento(new Avancado()));
		prontuario.addProcedimento(new Procedimento(new Avancado()));

		prontuario.addProcedimento(new Procedimento(new Basica()));
		prontuario.addProcedimento(new Procedimento(new Basica()));

		prontuario.addProcedimento(new Procedimento(new Comum()));

		String path = null;
		RepositoryFile csv = new CsvImplRepository(prontuario);
		try {
			path = csv.salveProntuario();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}
		String ln = System.lineSeparator();
		String conteudoEsperado = "nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos" + ln +
				"Richard Stallman,,,BASICO,2" + ln +
				"Richard Stallman,,,COMUM,1" + ln +
				"Richard Stallman,,,AVANCADO,4" + ln;

		assertNotNull(path);

		Path file = Paths.get(path);

		String conteudoObtido = null;

		try {
			conteudoObtido = new String(Files.readAllBytes(file));
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}

		assertTrue(Files.exists(file));
		assertEquals(conteudoEsperado, conteudoObtido);
	}

	@Test
	public void testSalvarProntuarioCompleto() {
		Prontuario prontuario = new Prontuario("Steve Jobs");

		prontuario.setInternacao(new Internacao(new Enfermaria(), 40));

		prontuario.addProcedimento(new Procedimento(new Avancado()));
		prontuario.addProcedimento(new Procedimento(new Avancado()));

		prontuario.addProcedimento(new Procedimento(new Basica()));
		prontuario.addProcedimento(new Procedimento(new Basica()));
		prontuario.addProcedimento(new Procedimento(new Basica()));
		prontuario.addProcedimento(new Procedimento(new Basica()));

		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Comum()));
		prontuario.addProcedimento(new Procedimento(new Comum()));


		String path = null;
		RepositoryFile csv = new CsvImplRepository(prontuario);
		try {
			path = csv.salveProntuario();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}
		String ln = System.lineSeparator();
		String conteudoEsperado = "nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos" + ln +
				"Steve Jobs,ENFERMARIA,40,," + ln +
				"Steve Jobs,,,BASICO,4" + ln +
				"Steve Jobs,,,COMUM,6" + ln +
				"Steve Jobs,,,AVANCADO,2" + ln;

		assertNotNull(path);

		Path file = Paths.get(path);

		String conteudoObtido = null;

		try {
			conteudoObtido = new String(Files.readAllBytes(file));
		} catch (IOException ioException) {
			ioException.printStackTrace();
			fail(ioException.getMessage());
		}

		assertTrue(Files.exists(file));
		assertEquals(conteudoEsperado, conteudoObtido);
	}
}
