import 'dart:io';

/// Interactive prompt helpers for the CLI.
class Prompts {
  /// Reads a single non-empty line from stdin, printing [prompt] first.
  /// Retries until a non-empty value is entered.
  static String readLine(String prompt) {
    while (true) {
      stdout.write(prompt);
      final line = stdin.readLineSync()?.trim() ?? '';
      if (line.isNotEmpty) return line;
    }
  }

  /// Asks [question] and returns true if the user answers 'y' or 'yes'.
  static bool confirm(String question) {
    stdout.write('$question [y/N] ');
    final answer = stdin.readLineSync()?.trim().toLowerCase() ?? '';
    return answer == 'y' || answer == 'yes';
  }

  /// Asks the user to press Enter to continue.
  static void pressEnterToContinue() {
    stdout.write('\nPress ENTER to continue...');
    stdin.readLineSync();
  }

  /// Prompts for a valid single-letter answer in [validLabels].
  static String readAnswer(List<String> validLabels) {
    final valid = validLabels.map((l) => l.toUpperCase()).toSet();
    while (true) {
      stdout.write('Your answer (${validLabels.join('/')}): ');
      final input = stdin.readLineSync()?.trim().toUpperCase() ?? '';
      if (valid.contains(input)) return input;
      stdout.writeln('Invalid choice. Please enter one of: ${validLabels.join(', ')}');
    }
  }
}
