package n.p.main;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.notes.Note;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

public class MainClass {

    private static final String REPOS_DIR = "C:\\work\\REPOS";
    private static final String MAVEN_REPOSITORY = "C:\\Users\\natalia.podolskaya\\.m2";

    private static final String GIT_LOGIN = "natalia.podolskaya";
    private static final String GIT_PASSWORD = "InydInyh$#12";

    private static final String GIT_CMD_STATUS = "git status";
    private static final String GIT_CMD_PULL = "git pull";

    private static final String GIT_EXAMPLE_REPO = "D:\\gitTool\\";

    public static void main(String... args) throws Exception {
        Repository existingRepo = getExistingRepo(GIT_EXAMPLE_REPO);
        Git git = new Git(existingRepo);

        deleteBranch(git, "notExistingBranch");
        String newBranchName = "PBI-8135";
        createNewBranch(git, newBranchName);

        git.checkout().setName(newBranchName).call();

        commitAll(git, existingRepo, "learning with https://github.com/centic9/jgit-cookbook");

        printStatus(git);
        listExistingBranches(git);
        // Go back on "master" branch and remove the created one
        git.checkout().setName("master");

        fetchRemoteCommits(git);

        listNotes(git, existingRepo);

        showLog(git, existingRepo);

        walkAllCommits(git);
        //git.branchDelete().setBranchNames("newBranch");
        /*
        // Creation of a temp folder that will contain the Git repository
        File workingDirectory = File.createTempFile("nuxeo-git-test", "");
        workingDirectory.delete();
        workingDirectory.mkdirs();

// Create a new file and add it to the index
        File newFile = new File(workingDirectory, "myNewFile");
        newFile.createNewFile();
        git.add().addFilepattern("myNewFile").call();

// Now, we do the commit with a message
        RevCommit rev = git.commit().setAuthor("gildas", "gildas@example.com").setMessage("My first commit").call();
        /*
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File(GIT_EXAMPLE_REPO + ".git"))
                .readEnvironment()  // scan environment GIT_* variables
                .findGitDir()       // scan up the file system tree
                .build();
        ObjectId head = repository.resolve("HEAD");
        Ref HEAD = repository.getRef("refs/heads/master");
        RevWalk walk = new RevWalk(repository);
        */
        // AnyObjectId objectIdOfCommit;
        // RevCommit commit = walk.parseCommit(objectIdOfCommit);
        /*

        Git git = new Git(repository);
        Iterable<RevCommit> logs = git.log().call();
        for (RevCommit log : logs) {
            System.out.println(log.toString());
        }

        AddCommand add = git.add();

        add.addFilepattern("src/main/java/n/p/main/MainClass.java").call();

        PullCommand pullCommand = git.pull();
        System.out.println(pullCommand.getRemoteBranchName());
        System.out.println(pullCommand.getRemote());
        pullCommand.call();
*/
        /*
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

    private static void commitAll(final Git git, final Repository repository, String message) throws IOException, GitAPIException {
        // and then commit the changes
        git.commit()
                .setAll(true)
                .setMessage(message)
                .call();

        System.out.println("Committed all changes to repository at " + repository.getDirectory());
    }

    private static void fetchRemoteCommits(final Git git) throws GitAPIException {
        System.out.println("Starting fetch");
        FetchResult result = git.fetch().setCheckFetchedObjects(true).call();
        System.out.println("Messages: " + result.getMessages());
    }

    private static Repository getExistingRepo(String repositoryDir) throws IOException {
        return new FileRepositoryBuilder().setGitDir(new File(repositoryDir, ".git")).build();
    }

    private static void createNewBranch(final Git git, String newBranchName) throws GitAPIException {
        try {
            git.branchCreate().setName(newBranchName).call();
        } catch (RefAlreadyExistsException ex) {
            System.out.println("Branch " + newBranchName + " has not been created: " + ex.getMessage());
        }
    }

    private static void deleteBranch(final Git git, String branchName) throws GitAPIException {
        //try {
            git.branchDelete().setBranchNames(branchName).call();
        //} catch (RefAlreadyExistsException ex) {
          //  System.out.println("Branch " + branchName + " has not been created: " + ex.getMessage());
        //}
    }

    private static void printStatus(final Git git) throws GitAPIException {
        Status status = git.status().call();
        System.out.println("Printing status... isClean: " + status.isClean());
        if (status.getAdded().size() > 0) {
            System.out.println("Added:");
            for (String fileName : status.getAdded()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getChanged().size() > 0) {
            System.out.println("Changed:");
            for (String fileName : status.getChanged()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getConflicting().size() > 0) {
            System.out.println("Conflicting:");
            for (String fileName : status.getConflicting()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getIgnoredNotInIndex().size() > 0) {
            System.out.println("Ignored:");
            for (String fileName : status.getIgnoredNotInIndex()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getMissing().size() > 0) {
            System.out.println("Missing:");
            for (String fileName : status.getMissing()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getModified().size() > 0) {
            System.out.println("Modified:");
            for (String fileName : status.getModified()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getRemoved().size() > 0) {
            System.out.println("Removed:");
            for (String fileName : status.getRemoved()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getUncommittedChanges().size() > 0) {
            System.out.println("UncommittedChanges:");
            for (String fileName : status.getUncommittedChanges()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getUntracked().size() > 0) {
            System.out.println("Untracked:");
            for (String fileName : status.getUntracked()) {
                System.out.println("\t" + fileName);
            }
        }
        if (status.getUntrackedFolders().size() > 0) {
            System.out.println("UntrackedFolders:");
            for (String fileName : status.getUntrackedFolders()) {
                System.out.println("\t" + fileName);
            }
        }
        for (String key : status.getConflictingStageState().keySet()) {
            IndexDiff.StageState stageState = status.getConflictingStageState().get(key);
            System.out.println("Name=" + stageState.name() + ";HasBase=" + stageState.hasBase() + ";HasOurs="
                    + stageState.hasOurs() + ";HasTheirs=" + stageState.hasTheirs());
        }

        System.out.println("Added: " + status.getAdded());
        System.out.println("Changed: " + status.getChanged());
        System.out.println("Conflicting: " + status.getConflicting());
        System.out.println("ConflictingStageState: " + status.getConflictingStageState());
        System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
        System.out.println("Missing: " + status.getMissing());
        System.out.println("Modified: " + status.getModified());
        System.out.println("Removed: " + status.getRemoved());
        System.out.println("Untracked: " + status.getUntracked());
        System.out.println("UntrackedFolders: " + status.getUntrackedFolders());
    }

    private static void showLog(final Git git, final Repository repository) throws GitAPIException, IOException {
        Iterable<RevCommit> logs = git.log()
                .call();
        int count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits overall on current branch");

        logs = git.log()
                .add(repository.resolve("remotes/origin/master"))
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits overall on test-branch");

        logs = git.log()
                .all()
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits overall in repository");

        logs = git.log()
                // for all log.all()
                .addPath("README.md")
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits on README.md");

        logs = git.log()
                // for all log.all()
                .addPath("pom.xml")
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits on pom.xml");
    }

    private static void listExistingBranches(final Git git) throws GitAPIException {
        // List the existing branches
        List<Ref> listRefsBranches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        System.out.println("Branches:");
        for (Ref refBranch : listRefsBranches) {
            System.out.println("\t" + refBranch.getName());
        }
    }

    private static void listNotes(final Git git, final Repository repository) throws GitAPIException, IOException {
        List<Note> call = git.notesList().call();
        System.out.println("Listing " + call.size() + " notes");
        for (Note note : call) {
            System.out.println("Note: " + note + " " + note.getName() + " " + note.getData().getName() + "\nContent: ");

            // displaying the contents of the note is done via a simple blob-read
            ObjectLoader loader = repository.open(note.getData());
            loader.copyTo(System.out);
        }
    }

    private static void listTags(final Git git, final Repository repository) throws GitAPIException, IncorrectObjectTypeException, MissingObjectException {
        List<Ref> call = git.tagList().call();
        for (Ref ref : call) {
            System.out.println("Tag: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());

            // fetch all commits for this tag
            LogCommand log = git.log();

            Ref peeledRef = repository.peel(ref);
            if(peeledRef.getPeeledObjectId() != null) {
                log.add(peeledRef.getPeeledObjectId());
            } else {
                log.add(ref.getObjectId());
            }

            Iterable<RevCommit> logs = log.call();
            for (RevCommit rev : logs) {
                System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            }
        }
    }

    private static void walkAllCommits(final Git git) throws IOException, GitAPIException {
        Iterable<RevCommit> commits = git.log().all().call();
        int count = 0;
        for (RevCommit commit : commits) {
            System.out.println("LogCommit: " + commit);
            count++;
        }
        System.out.println(count);
    }
}
