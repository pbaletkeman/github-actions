package com.quizengine.cli;

import com.quizengine.service.ImportService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Command(name = "import", description = "Import questions from Markdown files", mixinStandardHelpOptions = true)
public class ImportCommand implements Runnable {

    @Option(names = {"-f", "--file"}, description = "Path to Markdown file")
    private Path file;

    @Option(names = {"-d", "--dir"}, description = "Path to directory of Markdown files")
    private Path directory;

    private final ImportService importService;

    public ImportCommand(ImportService importService) {
        this.importService = importService;
    }

    @Override
    public void run() {
        try {
            ImportService.ImportResult result;
            if (file != null) {
                result = importService.importFromFile(file);
            } else if (directory != null) {
                result = importService.importFromDirectory(directory);
            } else {
                ConsoleFormatter.printError("Please specify --file or --dir");
                return;
            }
            ConsoleFormatter.printSuccess(result.message());
        } catch (IOException e) {
            ConsoleFormatter.printError("Import failed: " + e.getMessage());
        }
    }
}
