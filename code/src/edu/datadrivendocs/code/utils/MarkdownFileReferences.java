package edu.datadrivendocs.code.utils;

import java.io.*;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Utility class that scans a documentation directory for markdown (.md) files
 * and reports which markdown files are not referenced by any other markdown
 * file. This is useful to find orphaned documentation pages.
 *
 * <p>Behavior summary:
 * <ul>
 *   <li>Recursively traverses the provided root directory.</li>
 *   <li>Collects all markdown file basenames (e.g. "page.md").</li>
 *   <li>Parses markdown files for parenthesized links (e.g. "(path/to/page.md)")
 *       and treats those as references.</li>
 *   <li>Reports the set difference: files present but not referenced.</li>
 * </ul>
 *
 * Note: This class performs simple text-based parsing and assumes references
 * are expressed on a single line inside parentheses. It intentionally does
 * not attempt a full markdown AST parse to keep the implementation lightweight.
 */
public class MarkdownFileReferences {

    private static final Logger log = Logger.getLogger(MarkdownFileReferences.class.toString());

    /**
     * Main program entry point. Scans the default "docs" directory for markdown
     * files, identifies which are referenced and which are not, and logs a
     * summary plus the list of unreferenced files.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException if an I/O error occurs while reading directories or files
     */
    public static void main(String[] args) throws IOException {
        // Specify the path to the directory containing the markdown files
        String path = "docs";

        // Get all distinct file names from nested markdown files
        Set<String> allMarkdownFiles = getAllMarkdownFiles(path);

        // Get all referenced file names from markdown files
        Set<String> referencedFiles = getReferencedFiles(path);

        // Find files that are not being referenced
        Set<String> unreferencedFiles = new HashSet<>(allMarkdownFiles);
        unreferencedFiles.removeAll(referencedFiles);

        // Display the results
        log.info(()-> MessageFormat.format("All distinct markdown files: {0}", allMarkdownFiles.size()));

        log.info(()-> MessageFormat.format("Referenced markdown files: {0}", referencedFiles.size()));

        log.info(()-> MessageFormat.format("Unreferenced markdown files: {0}", unreferencedFiles.size()));

        for (String file : unreferencedFiles) {
            log.info(()-> MessageFormat.format(" - {0}", file));
        }
    }

    /**
     * Recursively collects the basenames of all markdown files found under the
     * supplied directory path. Directory entries named "common" are skipped.
     *
     * @param path directory path to scan
     * @return a Set of markdown file basenames (including the ".md" extension)
     * @throws IOException if an I/O error occurs while accessing the filesystem
     */
    private static Set<String> getAllMarkdownFiles(String path) throws IOException {
        Set<String> markdownFiles = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry) && !entry.getFileName().toString().equals("common")) {
                    markdownFiles.addAll(getAllMarkdownFiles(entry.toString()));
                }
                if(entry.getFileName().toString().endsWith(".md"))
                    markdownFiles.add(entry.getFileName().toString());
            }
        }
        return markdownFiles;
    }

    /**
     * Recursively scans markdown files under the given directory and returns a
     * set of markdown filenames that are referenced from other markdown files.
     *
     * @param path directory path to scan
     * @return a Set of referenced markdown basenames (including the ".md" extension)
     * @throws IOException if an I/O error occurs while reading files
     */
    private static Set<String> getReferencedFiles(String path) throws IOException {
        Set<String> referencedFiles = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    referencedFiles.addAll(getReferencedFiles(entry.toString()));
                }
                if(entry.getFileName().toString().endsWith(".md")) {
                    referencedFiles.addAll(processFileName(entry));
                }
            }
        }
        return referencedFiles;
    }

    /**
     * Parses a single markdown file and extracts referenced markdown file names.
     * The parser is conservative: it looks for the first '(' and ')' on each
     * line and, if the enclosed text ends with ".md", extracts the basename.
     * *
     * Example: For a line like "See [details](pages/intro.md)" this method will
     * extract "intro.md".
     * *
     * @param entry Path to the markdown file to process
     * @return a Set of referenced markdown basenames found within the file
     * @throws IOException if an I/O error occurs while reading the file
     */
    private static Set<String> processFileName(Path entry) throws IOException {
        Set<String> referencedFiles = new HashSet<>();
        List<String> lines = Files.readAllLines(entry);
        for (String line : lines) {
            // Assuming that references to other markdown files follow the format "(filename)"
            int start = line.indexOf('(');
            int end = line.indexOf(')');
            if (start != -1 && end != -1 && end > start) {
                String referencedFile = line.substring(start + 1, end);
                if(referencedFile.endsWith(".md"))
                    referencedFiles.add(getFileNameOnly(referencedFile));
            }
        }
        return referencedFiles;
    }

    /**
     * Returns the basename of a path using '/' as the separator. If the input
     * contains no forward slash, the original string is returned unchanged.
     *
     * @param path a file path or filename
     * @return the basename (the substring after the last '/'), or the original input if no '/'
     */
    private static String getFileNameOnly(String path) {
        // Get the last index of the forward slash '/' in the path
        int lastSlashIndex = path.lastIndexOf('/');

        // Extract the file name by using substring from lastSlashIndex + 1
        return path.substring(lastSlashIndex + 1);
    }
}