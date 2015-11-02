package n.p.main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;

public class MainClass {

    private static final String REPOS_DIR = "C:\\work\\REPOS";
    private static final String MAVEN_REPOSITORY = "C:\\Users\\natalia.podolskaya\\.m2";

    private static final String GIT_LOGIN = "natalia.podolskaya";
    private static final String GIT_PASSWORD = "InydInyh$#12";

    private static final String GIT_CMD_STATUS = "git status";
    private static final String GIT_CMD_PULL = "git pull";

    private static final String GIT_EXAMPLE_REPO = "D:\\old\\iOS_Site\\";

    public static void main(String... args) throws Exception {
        Repository existingRepo = new FileRepositoryBuilder().setGitDir(new File(GIT_EXAMPLE_REPO + ".git")).build();
        // construct repo...
        Git git = new Git(existingRepo);
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider("", "");
        Collection<Ref> remoteRefs = git.lsRemote()
                .setCredentialsProvider(cp)
                .setRemote("origin")
                .setTags(true)
                .setHeads(false)
                .call();
        for (Ref ref : remoteRefs) {
            System.out.println(ref.getName() + " -&gt; " + ref.getObjectId().name());
        }
        /*
        // Open an existing repository
        Repository existingRepo = new FileRepositoryBuilder().setGitDir(new File(GIT_EXAMPLE_REPO + ".git")).build();

        // Get a reference
        Ref master = existingRepo.getRef("master");

        // Get the object the reference points to
        ObjectId masterTip = master.getObjectId();

        // Rev-parse
        ObjectId obj = existingRepo.resolve("HEAD^{tree}");

        // Load raw object contents
        ObjectLoader loader = existingRepo.open(masterTip);
        loader.copyTo(System.out);
        */
        /*
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", "cd /d " + GIT_EXAMPLE_REPO + " && " + GIT_CMD_STATUS);
        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
        p.waitFor();

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", "cd /d " + GIT_EXAMPLE_REPO + " && dir");

        Charset charset = Charset.forName("Cp866");
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), charset));

        String line;
        while (true) {
            line = r.readLine();

            if (line == null) {
                break;
            }
            System.out.println(line);
        }

        processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", "cd /d " + GIT_EXAMPLE_REPO + " && " + GIT_CMD_PULL);
        p = processBuilder.start();
        p.waitFor();
        r = new BufferedReader(new InputStreamReader(p.getInputStream(), charset));

        while (true) {
            line = r.readLine();

            if (line == null) {
                break;
            }
            System.out.println(line);
        }*/
    }
}
