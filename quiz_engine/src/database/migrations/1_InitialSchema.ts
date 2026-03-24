import { MigrationInterface, QueryRunner, Table, TableIndex } from 'typeorm';

export class InitialSchema1700000000000 implements MigrationInterface {
  name = 'InitialSchema1700000000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.createTable(
      new Table({
        name: 'questions',
        columns: [
          { name: 'id', type: 'integer', isPrimary: true, isGenerated: true, generationStrategy: 'increment' },
          { name: 'questionText', type: 'varchar', length: '2000' },
          { name: 'optionA', type: 'varchar', length: '500' },
          { name: 'optionB', type: 'varchar', length: '500' },
          { name: 'optionC', type: 'varchar', length: '500' },
          { name: 'optionD', type: 'varchar', length: '500' },
          { name: 'optionE', type: 'varchar', length: '500', isNullable: true },
          { name: 'correctAnswer', type: 'varchar', length: '1' },
          { name: 'explanation', type: 'varchar', length: '2000', isNullable: true },
          { name: 'section', type: 'varchar', length: '100', isNullable: true },
          { name: 'difficulty', type: 'varchar', length: '50', isNullable: true },
          { name: 'sourceFile', type: 'varchar', length: '255', isNullable: true },
          { name: 'usageCycle', type: 'integer', default: 1 },
          { name: 'timesUsed', type: 'integer', default: 0 },
          { name: 'lastUsedAt', type: 'datetime', isNullable: true },
          { name: 'createdAt', type: 'datetime', default: "datetime('now')" },
        ],
      }),
      true,
    );

    await queryRunner.createTable(
      new Table({
        name: 'quiz_sessions',
        columns: [
          { name: 'sessionId', type: 'varchar', length: '36', isPrimary: true },
          { name: 'startedAt', type: 'datetime', default: "datetime('now')" },
          { name: 'endedAt', type: 'datetime', isNullable: true },
          { name: 'numQuestions', type: 'integer' },
          { name: 'numCorrect', type: 'integer', default: 0 },
          { name: 'percentageCorrect', type: 'decimal', precision: 5, scale: 2, default: 0 },
          { name: 'timeTakenSeconds', type: 'integer', isNullable: true },
        ],
      }),
      true,
    );

    await queryRunner.createTable(
      new Table({
        name: 'quiz_responses',
        columns: [
          { name: 'id', type: 'integer', isPrimary: true, isGenerated: true, generationStrategy: 'increment' },
          { name: 'sessionId', type: 'varchar', length: '36' },
          { name: 'questionId', type: 'integer' },
          { name: 'userAnswer', type: 'varchar', length: '1' },
          { name: 'isCorrect', type: 'integer', default: 0 },
          { name: 'timeTakenSeconds', type: 'integer', isNullable: true },
          { name: 'answeredAt', type: 'datetime', default: "datetime('now')" },
        ],
        foreignKeys: [
          {
            columnNames: ['sessionId'],
            referencedTableName: 'quiz_sessions',
            referencedColumnNames: ['sessionId'],
            onDelete: 'CASCADE',
          },
          {
            columnNames: ['questionId'],
            referencedTableName: 'questions',
            referencedColumnNames: ['id'],
            onDelete: 'CASCADE',
          },
        ],
      }),
      true,
    );

    await queryRunner.createIndex(
      'quiz_responses',
      new TableIndex({
        name: 'UQ_session_question',
        columnNames: ['sessionId', 'questionId'],
        isUnique: true,
      }),
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('quiz_responses', true);
    await queryRunner.dropTable('quiz_sessions', true);
    await queryRunner.dropTable('questions', true);
  }
}
