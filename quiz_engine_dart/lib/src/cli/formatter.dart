/// Provides pretty-printed terminal output helpers.
class Formatter {
  static const _lineWidth = 70;
  static const _border = '═';
  static const _corner = ('╔', '╗', '╚', '╝');
  static const _side = '║';

  // ANSI colour codes
  static const _reset = '\x1B[0m';
  static const _bold = '\x1B[1m';
  static const _green = '\x1B[32m';
  static const _red = '\x1B[31m';
  static const _yellow = '\x1B[33m';
  static const _cyan = '\x1B[36m';

  /// Wraps [content] in a box with optional [title].
  static String boxed(String content, {String? title}) {
    final topLine = _corner.$1 +
        _border * (_lineWidth - 2) +
        _corner.$2;
    final bottomLine = _corner.$3 +
        _border * (_lineWidth - 2) +
        _corner.$4;

    final buffer = StringBuffer();
    buffer.writeln(topLine);
    if (title != null && title.isNotEmpty) {
      buffer.writeln(_padLine(' $_bold$title$_reset '));
      buffer.writeln(_corner.$1 + '─' * (_lineWidth - 2) + _corner.$2);
    }
    for (final line in content.split('\n')) {
      buffer.writeln(_padLine(' $line '));
    }
    buffer.write(bottomLine);
    return buffer.toString();
  }

  static String _padLine(String content) {
    final plain = _stripAnsi(content);
    final padding = _lineWidth - 2 - plain.length;
    return '$_side$content${' ' * (padding < 0 ? 0 : padding)}$_side';
  }

  /// Formats a list of rows as an ASCII table.
  static String table(List<String> headers, List<List<String>> rows) {
    final colWidths = List<int>.filled(headers.length, 0);
    for (var i = 0; i < headers.length; i++) {
      colWidths[i] = headers[i].length;
    }
    for (final row in rows) {
      for (var i = 0; i < row.length && i < colWidths.length; i++) {
        if (row[i].length > colWidths[i]) colWidths[i] = row[i].length;
      }
    }

    final sep =
        '+${colWidths.map((w) => '-' * (w + 2)).join('+')}+';
    final buf = StringBuffer();
    buf.writeln(sep);
    buf.writeln(_tableRow(headers, colWidths));
    buf.writeln(sep);
    for (final row in rows) {
      buf.writeln(_tableRow(row, colWidths));
    }
    buf.write(sep);
    return buf.toString();
  }

  static String _tableRow(List<String> cells, List<int> widths) {
    final parts = <String>[];
    for (var i = 0; i < widths.length; i++) {
      final cell = i < cells.length ? cells[i] : '';
      parts.add(' ${cell.padRight(widths[i])} ');
    }
    return '|${parts.join('|')}|';
  }

  /// Returns [text] formatted as a success (green) message.
  static String success(String text) => '$_green$_bold✓ $text$_reset';

  /// Returns [text] formatted as an error (red) message.
  static String error(String text) => '$_red$_bold✗ $text$_reset';

  /// Returns [text] formatted as a warning (yellow) message.
  static String warning(String text) => '$_yellow⚠ $text$_reset';

  /// Returns [text] formatted in cyan.
  static String info(String text) => '$_cyan$text$_reset';

  /// Returns a progress bar string for [fraction] ∈ [0, 1].
  static String progressBar(double fraction, {int width = 40}) {
    final filled = (fraction * width).round().clamp(0, width);
    final empty = width - filled;
    final pct = (fraction * 100).toStringAsFixed(1);
    return '[${'█' * filled}${' ' * empty}] $pct%';
  }

  /// Strips ANSI escape sequences from [s].
  static String _stripAnsi(String s) =>
      s.replaceAll(RegExp(r'\x1B\[[0-9;]*m'), '');
}
