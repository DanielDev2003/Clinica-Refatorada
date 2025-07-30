import java.io.IOException;


public interface RepositoryFile {
    String salveProntuario() throws IOException;
    Prontuario carregueProntuario(String arquivoCsv) throws IOException;
}
