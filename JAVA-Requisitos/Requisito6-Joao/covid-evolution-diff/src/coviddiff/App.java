package coviddiff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
/**
 * Demonstra os ficheiros covid19spreading.rdf
 *  das duas últimas tags assim como as suas diferenças.
 * 
 * @author Joao Macedo
 *
 */
public class App {

	private static Git git;
	private static String path = "../covid-evolution-diff_files";
	
	/**
	 * Corre a aplicação.
	 * @param args
	 */
	public static void main(String[] args){
		connectGit();
	}


/**
 * Faz a conexão com o projeto no github e retira os ficheiros covid19spreading.rdf 
 * bem como as suas diferenças.
 */
	private static void connectGit() {
		Repository repository = null;
		File folder = new File(getPath());
		deleteFolder(folder);
		try {
			git = Git.cloneRepository()
					.setURI("http://github.com/vbasto-iscte/ESII1920")
					.setDirectory(new File(getPath()))
					.call();
			repository = git.getRepository();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Ref> listRefsTags = null;
		ObjectId tag1 = null;
		ObjectId tag2 = null;
		try {
			listRefsTags = git.tagList().call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int i=0;
		for (Ref refTag : listRefsTags) {
			if(listRefsTags.size()-i<=2) {
				if(tag1==null)
					tag1= refTag.getObjectId();
				else
					tag2= refTag.getObjectId();
			}
			i++;
		}

		RevWalk walk = new RevWalk(repository);
		ObjectId commit1id = null;
		ObjectId commit2id = null;
		try {
			RevCommit commit1 = walk.parseCommit(tag1);
			RevCommit commit2 = walk.parseCommit(tag2);
			commit1id = commit1.getTree();
			commit2id = commit2.getTree();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		walk.close();

		ObjectReader reader = repository.newObjectReader();
		TreeWalk treewalk;
		try {
			treewalk = new TreeWalk(repository);
			treewalk.addTree(commit1id);
			treewalk.setRecursive(true);
			treewalk.setFilter(PathFilter.create("covid19spreading.rdf"));
			if (!treewalk.next()) {
				throw new IllegalStateException("Did not find expected file 'covid19spreading.rdf'");
			}

			ObjectId objectId = treewalk.getObjectId(0);
			ObjectLoader loader = repository.open(objectId);

			// and then one can the loader to read the file
			loader.copyTo(new FileOutputStream(new File("../covid-evolution-diff_files/TestesGeracaoII.txt")));
//			loader.copyTo(System.out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			treewalk = new TreeWalk(repository);
			treewalk.addTree(commit2id);
			treewalk.setRecursive(true);
			treewalk.setFilter(PathFilter.create("covid19spreading.rdf"));
			if (!treewalk.next()) {
				throw new IllegalStateException("Did not find expected file 'covid19spreading.rdf'");
			}

			ObjectId objectId = treewalk.getObjectId(0);
			ObjectLoader loader = repository.open(objectId);

			// and then one can the loader to read the file
			loader.copyTo(new FileOutputStream(new File("../covid-evolution-diff_files/TestesGeracaoIII.txt")));
//			loader.copyTo(System.out);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<DiffEntry> diff = null;
		CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
		CanonicalTreeParser newTreeParser = new CanonicalTreeParser();


		try {
			oldTreeParser.reset(reader, commit1id);
			newTreeParser.reset(reader, commit2id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		try {
			diff = git.diff().setNewTree(newTreeParser).setOldTree(oldTreeParser).call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (DiffEntry entry : diff) {
			DiffFormatter formatter;
				try {
					formatter = new DiffFormatter(new FileOutputStream(new File("../covid-evolution-diff_files/CovidDiff.txt")));
					formatter.setRepository(repository);
					try {
						formatter.format(entry);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					formatter.close();

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
	}
	

/**
 * Caso o diretório já exista, é apagado.
 * @param folder Folder a ser apagado.
 */
	private static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}

	/**
	 * Dá return ao path da App.
	 * @return path
	 */
	public static String getPath() {
		return path;
	}
}
