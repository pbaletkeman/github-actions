/// Custom exceptions for the quiz engine.
library;

/// Thrown when no questions are found in the database.
class NoQuestionsException implements Exception {
  final String message;
  const NoQuestionsException([this.message = 'No questions found in database.']);

  @override
  String toString() => 'NoQuestionsException: $message';
}

/// Thrown when the requested question count exceeds available questions.
class InsufficientQuestionsException implements Exception {
  final int requested;
  final int available;

  const InsufficientQuestionsException({
    required this.requested,
    required this.available,
  });

  @override
  String toString() =>
      'InsufficientQuestionsException: Requested $requested questions but only $available available.';
}

/// Thrown when a quiz session is not found.
class SessionNotFoundException implements Exception {
  final String sessionId;
  const SessionNotFoundException(this.sessionId);

  @override
  String toString() => 'SessionNotFoundException: Session "$sessionId" not found.';
}

/// Thrown when a file cannot be imported.
class ImportException implements Exception {
  final String path;
  final String reason;

  const ImportException({required this.path, required this.reason});

  @override
  String toString() => 'ImportException: Cannot import "$path": $reason';
}

/// Thrown when a database operation fails.
class DatabaseException implements Exception {
  final String message;
  const DatabaseException(this.message);

  @override
  String toString() => 'DatabaseException: $message';
}
