package algo.process;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Subprocess {

    private Subprocess() {
    }

    public static Process call(String cmd, boolean redirectError) throws IOException {
        String[] args = cmd.split(" ");
        return call(Arrays.asList(args), redirectError);
    }

    public static Process call(List<String> args, boolean redirectError) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(args);
        builder.redirectErrorStream(redirectError);
        return builder.start();
    }
}
