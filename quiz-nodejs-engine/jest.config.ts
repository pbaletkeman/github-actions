import type { Config } from 'jest';

const config: Config = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  testMatch: ['**/test/**/*.test.ts'],
  transform: {
    '^.+\\.tsx?$': [
      'ts-jest',
      {
        tsconfig: {
          experimentalDecorators: true,
          emitDecoratorMetadata: true,
          esModuleInterop: true,
          strict: true,
          target: 'ES2020',
          module: 'commonjs',
        },
      },
    ],
  },
  collectCoverageFrom: [
    'src/**/*.ts',
    '!src/main.ts',
    '!src/cli/**/*.ts',
    '!src/**/*.d.ts',
    '!src/database/database.ts',
    '!src/database/migrations/**/*.ts',
  ],
  coverageThreshold: {
    global: {
      lines: 90,
      functions: 90,
      branches: 85,
      statements: 90,
    },
  },
  coverageReporters: ['text', 'text-summary', 'html', 'lcov'],
  coverageDirectory: 'coverage',
};

export default config;
